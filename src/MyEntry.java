import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MyEntry
{

    private ArrayList<String> varVal = new ArrayList<>();
    private ArrayList<Double> probs = new ArrayList<>();


    public ArrayList<String> getVarVal() {
        return varVal;
    }

    public void setVarVal(ArrayList<String> varVal) {
        this.varVal = varVal;
    }

    public ArrayList<Double> getProbs() {
        return probs;
    }

    public void setProbs(ArrayList<Double> probs) {
        this.probs = probs;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for ( int i = 0 ; i < this.varVal.size(); i++){
            sb.append(varVal.get(i)).append("P:"). append(probs.get(i)).append("\n");
        }
        return sb.toString();
    }

}

