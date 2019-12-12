import java.util.Arrays;
import java.util.Map;

public class Evidance implements Map.Entry<String,String> ,Comparable<Evidance> {

    private String var;
    private String value;

    Evidance(String var, String value){
        this.var = var;
        this.value = value;
    }

    @Override
    public String getKey() {
        return var;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        this.value = value;
        return value;
    }

    @Override
    public int compareTo(Evidance o) {
        return this.getKey().compareTo(o.getKey());

    }
}
