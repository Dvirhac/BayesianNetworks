
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyCpt implements Comparable<MyCpt> {
    private Vertex vertex;
    private String[] label_array;
    private String label;
    private ArrayList<MyEntry> cpt = new ArrayList<>();
    static int numOfJoins;
    static int numOfElimination;

    MyCpt(MyCpt other) {
        this.label_array = other.label_array;
        for (MyEntry entry : other.getCpt()) {
            MyEntry myEntry = new MyEntry(entry);
            this.cpt.add(myEntry);
        }
    }
    MyCpt() {

    }
    public static void removeParent(MyCpt myCpt, String parent) {

        String[] parents_array = myCpt.getVertex().getParents_st().split(",");
        int index = 0;
        for (int i = 0; i < parents_array.length; i++) {
            if (parents_array[i].equals(parent)) {
                index = i;
                break;
            }
        }

        for (int i = 0; i < myCpt.getCpt().size(); i++) {
            MyEntry entry = myCpt.getCpt().get(i);
            if (entry.getKey()[index].equals(myCpt.getVertex().getValues()[myCpt.getVertex().getValues().length - 1])) {
                myCpt.getCpt().remove(i);
                i--;
            }
            List<String> list = new ArrayList<>(Arrays.asList(entry.getKey()));
            list.remove(index);
            entry.setKey(list.toArray(new String[0]));
        }


    }

    public static void removeChild(MyCpt myCpt, String mode) {

        ArrayList<MyEntry> entries = new ArrayList<>();

        for (int i = 0; i < myCpt.getCpt().size(); i++) {
            MyEntry entry = myCpt.getCpt().get(i);
            List<String> list_entry = new ArrayList<>(Arrays.asList(entry.getKey()));
            if (!list_entry.get(list_entry.size() - 1).equals(mode)) {
                myCpt.getCpt().remove(i);
                i--;
            }
        }


        for (MyEntry entry : myCpt.getCpt()) {
            List<String> list = new ArrayList<>(Arrays.asList(entry.getKey()));
            list.remove(list.size() - 1);
            entry.setKey(list.toArray(new String[0]));
            entries.add(entry);
        }
        myCpt.setCpt(entries);
    }

    public void add(MyEntry entry) {
        this.cpt.add(entry);
    }

    void build(String[] vars) {
        StringBuilder bs = new StringBuilder();
        for (int i = 4; i < vars.length; i++) {
            bs.append(vars[i]).append("\n");
        }
        String[] cpt = bs.toString().split("\n");

        for (int i = 0; i < cpt.length; i++) {
            StringBuilder entrySb = new StringBuilder();
            String[] cpts = cpt[i].split(",");
            for (int j = 0; j < cpts.length; j++) {
                boolean numeric = true;
                try {
                    Double num = Double.parseDouble(cpts[j]);
                } catch (NumberFormatException e) {
                    numeric = false;
                }
                if (numeric) {
                    String mode = cpts[j - 1].replaceAll("=", "");
                    makeEntry(entrySb, cpts[j], mode);
                } else {
                    if (!cpts[j].startsWith("=")) {
                        entrySb.append(cpts[j]).append(",");
                    }
                }
            }
        }
    }

    private void makeEntry(StringBuilder entry, String probability, String mode) {
        StringBuilder new_Entry = new StringBuilder(entry);
        new_Entry.append(mode).append(",");
        String[] subEntry = new_Entry.toString().split(",");
        MyEntry myEntry = new MyEntry(subEntry, new BigDecimal(probability));
        this.add(myEntry);

    }

    public static MyCpt joinCpt(MyCpt first, MyCpt second, String var) {
        MyCpt new_myCpt = new MyCpt();
        int cpt1Size = getSize(first);
        int cpt2Size = getSize(second);
        int whoStart;

        if (cpt1Size > cpt2Size)
            whoStart = 1;
        else if (cpt2Size > cpt1Size)
            whoStart = 2;
        else {
            byte[] f = new byte[0];
            try {
                f = first.label.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            int sum_first = 0;
            for (byte b : f)
                sum_first += b;

            int sum_second = 0;

            byte[] s = new byte[0];
            try {
                s = second.label.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (byte b : s)
                sum_second += b;

            if (sum_first > sum_second) whoStart = 1;
            else whoStart = 2;
        }

        String label = createLabel(first.getLabel(), second.getLabel(), var, whoStart);
        new_myCpt.setLabel(label);
        new_myCpt.setLabel_array(label.split(","));
        if (whoStart == 1) {
            makeCptFromJoin(first, second, new_myCpt);
        } else {
            makeCptFromJoin(second, first, new_myCpt);
        }

        return new_myCpt;
    }

    private static void makeCptFromJoin(MyCpt first, MyCpt second, MyCpt new_myCpt) {
        String[] common_array = findCommon(first.getLabel(), second.getLabel());
        List<Integer> common_indexes_first_list = new ArrayList<>();
        List<Integer> common_indexes_second_list = new ArrayList<>();
        findcommonIndexes(first, common_array, common_indexes_first_list);
        findcommonIndexes(second, common_array, common_indexes_second_list);
        for (MyEntry entry_first : first.getCpt()) {
            List<String> first_entry_list = new ArrayList<>(Arrays.asList(entry_first.getKey()));
            StringBuilder bs = new StringBuilder();
            boolean commmonSize = false;
            for (MyEntry entry_second : second.getCpt()) {
                List<String> second_entry_list = new ArrayList<>(Arrays.asList(entry_second.getKey()));
                for (int z = 0; z < common_array.length; z++) {
                    bs = new StringBuilder();
                    if (first_entry_list.get(common_indexes_first_list.get(z)).equals(second_entry_list.get(common_indexes_second_list.get(z)))) {
                        commmonSize = true;
                    } else {
                        commmonSize = false;
                    }
                }
                if (commmonSize) {
                    for (int i = 0; i < first_entry_list.size(); i++) {
                        bs.append(first_entry_list.get(i)).append(",");
                    }

                    for (int j = 0; j < second_entry_list.size(); j++) {
                        if (notIn(j, common_indexes_second_list)) {
                            bs.append(second_entry_list.get(j)).append(",");
                        }
                    }
                    String[] new_entry = bs.toString().split(",");
                    numOfJoins++;
                    MyEntry entry = new MyEntry(new_entry, entry_first.getValue().multiply(entry_second.getValue()));
                    new_myCpt.add(entry);
                    bs = new StringBuilder();
                    commmonSize = false;
                }
            }
        }
    }

    private static boolean notIn(int j, List<Integer> common_array) {
        return !common_array.contains(j);
    }

    public void eliminate(String var, int numOfValues) {
        ArrayList<MyEntry> entries = new ArrayList<>();
        int eliminateIndex = findIndex(var);
        int size_of_table = this.label_array.length;
        int index = 0;
        StringBuilder sb;
        BigDecimal prob;
        ArrayList<Integer> indexesBeenVisited = new ArrayList<>();
        for (int firstRunner = 0; firstRunner < this.cpt.size() - 1; firstRunner++) {
            if (isContains(firstRunner, indexesBeenVisited))
                continue;
            MyEntry first_entrty = this.cpt.get(firstRunner);
            sb = new StringBuilder();
            prob = first_entrty.getValue();
            index = 0;
            boolean canAdd = false;
            for (int secendRunner = firstRunner + 1; secendRunner < this.cpt.size(); secendRunner++) {
                MyEntry second_entry = this.cpt.get(secendRunner);
                for (int i = 0; i < size_of_table; i++) {
                    if (i != eliminateIndex) {
                        if (first_entrty.getKey()[i].equals(second_entry.getKey()[i])) {
                            canAdd = true;
                        } else {
                            canAdd = false;
                            break;
                        }
                    }
                }

                if (canAdd) {
                    indexesBeenVisited.add(secendRunner);
                    numOfElimination++;
                    prob = prob.add(second_entry.getValue());
                    index++;
                }
                if (index == numOfValues - 1) {
                    for (int j = 0; j < this.cpt.get(firstRunner).getKey().length; j++) {
                        if (j != eliminateIndex) {
                            sb.append(first_entrty.getKey()[j]).append(",");
                        }
                    }
                    MyEntry entry = new MyEntry(sb.toString().split(","), prob);
                    entries.add(entry);
                    break;
                }
            }

        }
        String new_header;
        if (this.label_array[0].equals(var)) {
            new_header = this.label.replaceAll(var + ",", "");
        } else {
            new_header = this.label.replaceAll("," + var, "");
        }
        String[] header_array = new_header.split(",");
        this.setLabel(new_header);
        this.setLabel_array(header_array);
        this.setCpt(entries);


    }

    private boolean isContains(int firstRunner, ArrayList<Integer> indexesBeenVisited) {
        boolean ans = false;
        for (int index : indexesBeenVisited){
            if (index == firstRunner) ans = true;
        }
        return ans;
    }

    private int findIndex(String var) {
        for (int i = 0 ; i <  this.getLabel_array().length ; i ++ ){
            if (this.getLabel_array()[i].equals(var)){
                return i;
            }
        }
        return -1;
    }

    private static void findcommonIndexes(MyCpt myCpt, String[] common_array, List<Integer> common_indexes_list) {
        for (int i = 0 ; i < common_array.length; i ++){
            for (int j = 0; j < myCpt.getLabel_array().length  ; j ++ ){
                if (common_array[i].equals(myCpt.getLabel_array()[j])){
                    common_indexes_list.add(j);
                    break;
                }
            }
        }
    }

    private static String createLabel(String first, String second, String var, int whoStart) {
        StringBuilder bs = new StringBuilder();
        if (whoStart == 1){
            bs.append(first);
            for (int i = 0 ; i < second.length() ; i ++){
                if (bs.toString().indexOf(second.charAt(i)) < 0){
                    bs.append(",").append(second.charAt(i));
                }
            }
        }
        else {
            bs.append(second);
            for (int i = 0 ; i < first.length() ; i ++){
                if (bs.toString().indexOf(first.charAt(i)) < 0){
                    bs.append(",").append(first.charAt(i));
                }
            }
        }
        return bs.toString();

    }

    private static String [] findCommon(String label, String label1) {
        StringBuilder bs = new StringBuilder();
        for (char c : label.toCharArray()){
            for (char h : label1.toCharArray()){
                if (c == h && c != ','){
                    bs.append(c).append(",");
                    break;
                }
            }

        }
        return bs.toString().split(",");
    }

    public void addComplement(String[] values){
        ArrayList<MyEntry> entries = new ArrayList<>();
        ArrayList<String> values_list = new ArrayList<>(Arrays.asList(values));
        int index_mode = cpt.get(0).getKey().length - 1;
        BigDecimal prob = new BigDecimal(1);
        for (MyEntry entry : cpt) {
            values_list.remove(entry.getKey()[index_mode]);
            prob = prob.subtract(entry.getValue());
            if (values_list.size() == 1) {
                String[] new_entry = entry.getKey().clone();
                new_entry[index_mode] = values_list.get(0);
                MyEntry myEntry = new MyEntry(new_entry, prob);
                entries.add(myEntry);
                prob = new BigDecimal(1);
                values_list = new ArrayList<>(Arrays.asList(values));
            }
        }
        cpt.addAll(entries);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for (MyEntry entry : cpt){
            sb.append(entry.toString()).append("\n");
        }
        return sb.toString();
    }

    private static int getSize(MyCpt myCpt) {
        return myCpt.label_array.length;
    }

    @Override
    public int compareTo(MyCpt o) {
        return Integer.compare(this.cpt.size(), o.cpt.size());
    }

    public ArrayList<MyEntry> getCpt() { return cpt; }
    public void setCpt(ArrayList<MyEntry> cpt) { this.cpt = cpt; }
    public String [] getLabel_array() { return label_array; }
    public void setLabel_array(String[] label_array) { this.label_array = label_array; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public void setVertex(Vertex vertex) { this.vertex = vertex; }
    public Vertex getVertex() { return vertex; }


}
