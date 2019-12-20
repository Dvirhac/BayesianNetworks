import java.util.Map;

public class Evidence implements Map.Entry<String,String> ,Comparable<Evidence> {

    private String var;
    private String value;

    Evidence(String var, String value){
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
    public int compareTo(Evidence o) {
        return this.getKey().compareTo(o.getKey());

    }
}
