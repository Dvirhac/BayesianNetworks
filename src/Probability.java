import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;

import static java.math.RoundingMode.HALF_UP;

public class Probability {

    private List<MyCpt> cpts = new ArrayList();
    private Graph graph;
    private String hidden;
    private String var;
    private String var_value;
    private ArrayList<Evidance> evidances_list = new ArrayList<>();
    MathContext mathContext = new MathContext(5,HALF_UP);


    Probability(Graph graph, String  query){
        MyCpt.numOfElimination = 0;
        MyCpt.numOfJoins = 0;
        this.graph = graph;
        initData(query);
    }

    private void initData(String query) {
        String [] query_st  = query.split("\\)");
        String query_without_P = query_st[0].replaceAll("P","");
        String query_ready = query_without_P.replaceAll("\\(","");
        String [] var_evidances = query_ready.split("\\|");
        String [] var_value = var_evidances[0].split("=");
        var = var_value[0];
        this.var_value = var_value[1];
        String [] evidances = var_evidances[1].split(",");

        for (String evidance : evidances){
            String [] evidance_value = evidance.split("=");
            Evidance new_evidance = new Evidance(evidance_value[0],evidance_value[1]);
            evidances_list.add(new_evidance);
        }

        String hidden_vars = query_st[1];
        if (hidden_vars.equals(",")){
            this.hidden = "";
            return;
        }

        else {
            String hiddens = hidden_vars.replaceAll(",","");
            String [] hiddenVars = hiddens.split("-");
            StringBuilder stringBuilder = new StringBuilder();
            for (String hidden : hiddenVars){
                stringBuilder.append(hidden).append(",");
            }
            String s = stringBuilder.toString().substring(0,stringBuilder.toString().length() - 1);
            this.hidden = s;
        }
    }

    public void init(Graph graph) {


        for (Vertex vertex : graph.getVertices()) {
            MyCpt myCpt = new MyCpt(vertex.getMyCPT());
            cpts.add(myCpt);
            myCpt.setVertex(vertex);
            myCpt.addComplement(vertex.getValues());
            myCpt.setVertex(vertex);
            StringBuilder sb = new StringBuilder();
            boolean isDone = false;
            for (Evidance evidance : evidances_list){
                if (evidance.getKey().equals(vertex.getLabel())){
                    if (vertex.getParents_st().equals("")){
                        cpts.remove(myCpt);
                        continue;
                    }
                    for (String parent : vertex.getParents_st().split(",")) {
                        boolean isEvidance = false;
                        for (Evidance evidance1 : evidances_list){
                            if (evidance1.getKey().equals(parent)){
                                isEvidance = true;
                                break;
                            }
                        }
                        if (!isEvidance) {
                            sb.append(parent).append(",");
                        } else {
                            MyCpt.removeParent(myCpt,parent);
                        }
                    }

                    String s = sb.toString().substring(0,sb.toString().length() - 1);
                    String mode ="";
                    for (Evidance evidance1 : evidances_list){
                        if (evidance1.getKey().equals(vertex.getLabel())){
                            mode = evidance1.getValue();
                            break;
                        }
                    }
                    MyCpt.removeChild(myCpt, mode);
                    myCpt.setLabel(s);
                    myCpt.setLabel_array(s.split(","));
                    isDone = true;
                    break;
                }


            }

            if (isDone)
                continue;

            for (String parent : vertex.getParents_st().split(",")){
                if (parent.equals("")){
                    break;
                }
                boolean isEvidance = false;
                for (Evidance evidance : evidances_list){
                    if (evidance.getKey().equals(parent)){
                        isEvidance = true;
                        break;
                    }
                }
                if (isEvidance){
                    MyCpt.removeParent(myCpt,parent);
                }

                else {
                    sb.append(parent).append(",");
                }
            }
            sb.append(vertex.getLabel());
            myCpt.setLabel(sb.toString());
            myCpt.setLabel_array(sb.toString().split(","));

        }

        boolean _continue = true;
        while (_continue){
            int num_of_leafs = 0;
            for (int i = 0 ; i < cpts.size() ; i ++){
                Vertex vertex = cpts.get(i).getVertex();
                if (vertex.isLeaf()){
                    if (checkToRemove(vertex.getLabel())){
                        num_of_leafs++;
                        cpts.remove(i);
                        i--;
                    }
                }
            }
            if (num_of_leafs == 0)
                _continue = false;
        }
        Collections.sort(cpts);

    }

    public void start(){
        boolean isExists = checkAnswer();
        BigDecimal ans = new BigDecimal("0");
        if (isExists){
            Vertex vertex = graph.findNodeByName(var);
            MyCpt myCpt = new MyCpt(vertex.getMyCPT());
            myCpt.addComplement(vertex.getValues());

            for (MyEntry entry : myCpt.getCpt()){
                if (entry.getKey()[entry.getKey().length - 1].equals(var_value)) {
                    boolean isEntry = checkEvidencesValues(entry);
                    if (isEntry){
                        ans = entry.getValue();
                        break;
                    }

                }
            }

            System.out.println(ans.setScale(5,HALF_UP).toString());
            return;
        }

        init(graph);

        String [] hiddenArray = hidden.split(",");
        for (String hidden : hiddenArray){
            joinTables(hidden, true);
        }
        while (cpts.size() > 1){
            joinTables(var , false);
        }

        BigDecimal denominator = cpts.get(0).getCpt().get(0).getValue();
        for (int i = 1 ; i < cpts.get(0).getCpt().size(); i ++){

            denominator = denominator.add(cpts.get(0).getCpt().get(i).getValue());
            MyCpt.numOfElimination++;
        }
        BigDecimal Numerator  = new BigDecimal(0);

        for (MyEntry entry : cpts.get(0).getCpt()){
            if (entry.getKey()[0].equals(var_value)){
                Numerator  = entry.getValue();
                break;
            }
        }


        ans = Numerator .divide(denominator, mathContext);
        System.out.print(ans.setScale(5,HALF_UP).toString()+",");
        System.out.print(MyCpt.numOfElimination+",");
        System.out.println(MyCpt.numOfJoins);

    }

    private boolean checkEvidencesValues(MyEntry entry) {
        boolean ans = true;
        Collections.sort(evidances_list);
        for (int i = 0; i < evidances_list.size() ; i ++){
            if (!evidances_list.get(i).getValue().equals(entry.getKey()[i])){
                ans = false;
            }
        }
        return ans;
    }

    private boolean checkAnswer() {
        Vertex vertex = Graph.findNodeByName(var, graph.getVertices());
        StringBuilder stringBuilder = new StringBuilder();
        for (Evidance evidance : evidances_list){
            stringBuilder.append(evidance.getKey()).append(",");
        }
        String [] evidances = stringBuilder.toString().split(",");
        String[] parents = vertex.getParents_st().split(",");
        Arrays.sort(evidances);
        Arrays.sort(parents);
        return Arrays.equals(evidances, parents);
    }

    private boolean checkToRemove(String label) {
        StringBuilder bs = new StringBuilder();
        bs.append(var).append(",");
        for (Evidance evidance : evidances_list){
            bs.append(evidance.getKey()).append(",");
        }
        String [] st = bs.toString().split(",");
        for (String var : st){

            if (var.equals(label)){
                return false;
            }
        }
        return true;
    }

    private void joinTables(String hidden ,boolean isToEliminate) {
        while (true) {
            MyCpt h1 = new MyCpt();
            MyCpt h2 = new MyCpt();
            int c = 0 ;
            for (MyCpt myCpt : cpts) {
                if (myCpt.getLabel().contains(hidden) && c == 0){
                    h1 = myCpt;
                    c++;
                }
                else if (myCpt.getLabel().contains(hidden) && c == 1){
                    h2 = myCpt;
                    c++;
                    MyCpt cpt = MyCpt.joinCpt(h1, h2 , hidden);
                    cpts.remove(h1);
                    cpts.remove(h2);
                    cpts.add(cpt);

                    Collections.sort(cpts);
                    break;
                }

            }
            if (c != 2)
                break;
        }
            MyCpt toBeEliminated = new MyCpt();
            for (MyCpt myCpt : cpts) {
                if (myCpt.getLabel().contains(hidden)) {
                    toBeEliminated = myCpt;
                    if (isToEliminate) {
                        cptElimination(toBeEliminated, hidden);
                    }
                    break;
                }
        }
    }

    void cptElimination(MyCpt toBeEliminated, String var){

        toBeEliminated.eliminate(var, Graph.findNodeByName(var, graph.getVertices()).getValues().length);
    }

}