import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

public class MyEntry implements Map.Entry<String [], BigDecimal>
{

    private String [] key;
    private BigDecimal prob;

    public MyEntry(String [] key, BigDecimal prob) {
        this.key = key;
        this.prob = new BigDecimal(prob.toString());
    }

    @Override
    public String[] getKey() {
        return key;
    }

    @Override
    public BigDecimal getValue() {
        return prob;
    }

    @Override
    public BigDecimal setValue(BigDecimal value) {
        this.prob = new BigDecimal(value.toString());
        return prob;
    }
    MyEntry(MyEntry other) {

        this.key = other.getKey().clone();
        this.prob = other.prob;
    }
    public void setKey(String[] key) {
        this.key = key;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(key)).append(" Probability : " + getValue());
        return sb.toString();
    }
}

