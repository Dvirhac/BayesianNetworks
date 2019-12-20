import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static java.math.RoundingMode.HALF_UP;

public class Probability {

    private List<MyCpt> cpts = new ArrayList();
    private Graph graph;
    private String hidden;
    private String var;
    private String var_value;
    private ArrayList<Evidence> evidances_list = new ArrayList<>();
    MathContext mathContext = new MathContext(5,HALF_UP);


    Probability(Graph graph, String  query)
    {
        MyCpt.numOfElimination = 0;
        MyCpt.numOfJoins = 0;
        this.graph = graph;
        initData(query);
    }

    private void initData(String query)
    {
        String [] query_st  = query.split("\\)");
        String query_without_P = query_st[0].replaceAll("P","");
        String query_ready = query_without_P.replaceAll("\\(","");
        String [] var_evidences = query_ready.split("\\|");
        String [] var_value = var_evidences[0].split("=");
        var = var_value[0];
        this.var_value = var_value[1];
        String [] evidences;
        if (var_evidences.length == 1)
        {
            evidences = null;
        }
        else
        {
            evidences = var_evidences[1].split(",");

        }

        if (evidences !=  null) {
            for (String evidence : evidences) {
                String[] evidence_value = evidence.split("=");
                Evidence new_evidence = new Evidence(evidence_value[0], evidence_value[1]);
                evidances_list.add(new_evidence);
            }
        }

        String hidden_vars = query_st[1];
        if (hidden_vars.equals(",")){
            this.hidden = "";
        }

        else
        {
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

    public void initFactors()
    {


        for (Vertex vertex : graph.getVertices()) {
            isAncestor(vertex);
            if (!isAncestor(vertex))
            {
                continue;
            }
            MyCpt myCpt = new MyCpt(vertex.getMyCPT());
            cpts.add(myCpt);
            myCpt.setVertex(vertex);
            myCpt.addComplement(vertex.getValues());
            myCpt.setVertex(vertex);
            StringBuilder sb = new StringBuilder();
            boolean isDone = false;
            for (Evidence evidance : evidances_list){
                if (evidance.getKey().equals(vertex.getLabel())){
                    if (vertex.getParents_st().equals(""))
                    {
                        cpts.remove(myCpt);
                        continue;
                    }
                    for (String parent : vertex.getParents_st().split(",")) {
                        boolean isEvidance = false;
                        for (Evidence evidance1 : evidances_list){
                            if (evidance1.getKey().equals(parent))
                            {
                                isEvidance = true;
                                break;
                            }
                        }
                        if (!isEvidance)
                        {
                            sb.append(parent).append(",");
                        } else {
                            myCpt.removeParent(parent);
                        }
                    }

                    String s = sb.toString().substring(0,sb.toString().length() - 1);
                    String mode ="";
                    for (Evidence evidance1 : evidances_list){
                        if (evidance1.getKey().equals(vertex.getLabel()))
                        {
                            mode = evidance1.getValue();
                            break;
                        }
                    }
                    myCpt.removeChild(mode);
                    myCpt.setLabel(s);
                    myCpt.setLabel_array(s.split(","));
                    isDone = true;
                    break;
                }


            }

            if (isDone)
                continue;

            for (String parent : vertex.getParents_st().split(",")){
                if (parent.equals(""))
                {
                    break;
                }
                boolean isEvidance = false;
                for (Evidence evidance : evidances_list){
                    if (evidance.getKey().equals(parent))
                    {
                        isEvidance = true;
                        break;
                    }
                }
                if (isEvidance)
                {
                    myCpt.removeParent(parent);
                }

                else
                {
                    sb.append(parent).append(",");
                }
            }
            sb.append(vertex.getLabel());
            myCpt.setLabel(sb.toString());
            myCpt.setLabel_array(sb.toString().split(","));

        }

        Collections.sort(cpts);

    }


    public String start()
    {
        StringBuilder ans_sb = new StringBuilder();
        boolean isExists = checkAnswer();
        BigDecimal ans;
        if (isExists)
        {
            Vertex vertex = graph.findNodeByName(var);
            ans = findProbFromCpt(vertex);
            ans_sb.append(ans.setScale(5,HALF_UP).toString()).append(",")
                    .append(MyCpt.numOfElimination).append(",")
                    .append(MyCpt.numOfJoins);
            System.out.println(ans_sb.toString());
            return ans_sb.toString();
        }

        initFactors();
        String [] hiddenArray ;
        if (!hidden.equals("")){
            hiddenArray = hidden.split(",");
        }
        else {
            hiddenArray = new String[0];
        }
        for (String hidden : hiddenArray){
            joinTables(hidden, true);
        }
        while (cpts.size() > 1){
            joinTables(var , false);
        }
        MyCpt final_cpt = cpts.get(0);
        BigDecimal denominator = final_cpt.getCpt().get(0).getValue();
        for (int i = 1 ; i < final_cpt.getCpt().size(); i ++){

            denominator = denominator.add(cpts.get(0).getCpt().get(i).getValue());
            MyCpt.numOfElimination++;
        }
        BigDecimal numerator  = new BigDecimal(0);

        for (MyEntry entry : final_cpt.getCpt()){
            if (entry.getKey()[0].equals(var_value)){
                numerator  = entry.getValue();
                break;
            }
        }


        ans = numerator .divide(denominator, mathContext);
        ans_sb.append(ans.setScale(5,HALF_UP).toString()).append(",")
                .append(MyCpt.numOfElimination).append(",")
                .append(MyCpt.numOfJoins);
        System.out.println(ans_sb.toString());
        return ans_sb.toString();

    }

    private BigDecimal findProbFromCpt(Vertex vertex)
    {
        BigDecimal ans = new BigDecimal(0);
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
        return ans;
    }

    private boolean checkEvidencesValues(MyEntry entry)
    {
        boolean ans = true;
        Collections.sort(evidances_list);
        for (int i = 0; i < evidances_list.size() ; i ++){
            if (!evidances_list.get(i).getValue().equals(entry.getKey()[i]))
            {
                ans = false;
            }
        }
        return ans;
    }

    private boolean checkAnswer()
    {
        Vertex vertex = graph.findNodeByName(var);
        StringBuilder stringBuilder = new StringBuilder();
        for (Evidence evidance : evidances_list){
            stringBuilder.append(evidance.getKey()).append(",");
        }
        String [] evidences = stringBuilder.toString().split(",");
        String[] parents = vertex.getParents_st().split(",");
        Arrays.sort(evidences);
        Arrays.sort(parents);
        return Arrays.equals(evidences, parents);
    }

    private void joinTables(String hidden ,boolean isToEliminate)
    {
        while (true) {
            MyCpt h_1 = new MyCpt();
            MyCpt h_2;
            int num_of_tables = 0 ;
            for (MyCpt myCpt : cpts) {
                if (myCpt.getLabel().contains(hidden) && num_of_tables == 0)
                {
                    h_1 = myCpt;
                    num_of_tables++;
                }
                else if (myCpt.getLabel().contains(hidden) && num_of_tables == 1)
                {
                    h_2 = myCpt;
                    num_of_tables++;
                    MyCpt cpt = MyCpt.joinCpt(h_1, h_2 , hidden);
                    cpts.remove(h_1);
                    cpts.remove(h_2);
                    cpts.add(cpt);

                    Collections.sort(cpts);
                    break;
                }

            }
            if (num_of_tables != 2)
            {
                break;
            }
        }
        MyCpt toBeEliminated;
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

    void cptElimination(MyCpt toBeEliminated, String var)
    {

        toBeEliminated.eliminate(var,graph.findNodeByName(var).getValues().length);
    }

    private boolean isAncestor(Vertex source)
    {
        graph.resetVertices();
        Queue<Vertex> queue = new LinkedList<>();
        source.setVisited(true);
        queue.add(source);
        while(!queue.isEmpty()){
            Vertex vertex = queue.poll();
            if (isEvidenceOrQueryVar(vertex.getLabel()))
                return true;
            for (Vertex children : vertex.getChildren()){
                if (!children.isVisited()){
                    children.setVisited(true);
                    queue.add(children);
                }
            }
        }
        return false;
    }

    private boolean isEvidenceOrQueryVar(String label)
    {
        boolean is_query_var = label.equals(var);
        if (is_query_var)
            return true;
        for (Evidence evidance : evidances_list){
            if (evidance.getKey().equals(label))
                return true;
        }
        return false;
    }

}