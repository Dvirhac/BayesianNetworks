import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyCPT {

    private ArrayList<MyEntry> cpt;
    public MyCPT(){
        cpt = new ArrayList<>();
    }

    public static MyCPT build(String[] vars) {
        MyCPT myCPT = new MyCPT();
        String p = vars[2].substring(9);
        String [] parents = p.split(",");


        StringBuilder bs = new StringBuilder();
        for (int i = 4 ; i < vars.length ; i ++) {
            bs.append(vars[i]).append("\n");
        }

        String allData = bs.toString().replaceAll("=true,", "");
        String [] linesData = allData.split("\n");
        for ( String line : linesData){
            String[] lineData = line.split(",");
           // StringBuilder stringData = new StringBuilder();
            String string = "";
            int parentIndex = 0;
            for (int data = 0 ; data < lineData.length-1; data++) {
                string+=parents[parentIndex]+ "="+ lineData[data] +",";
               // stringData.append(parents[parentIndex]).append("=").append(lineData[data]).append(",");
                parentIndex++;
            }
            MyEntry myEntry = new MyEntry();
            myEntry.getVarVal().add(string);
            myEntry.getProbs().add(Double.parseDouble(lineData[lineData.length-1]));
            myCPT.cpt.add(myEntry);


        }

        return myCPT;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (MyEntry myEntry: cpt){
            stringBuilder.append(myEntry.toString()).append("\n");
        }
        return stringBuilder.toString();
    }
}

