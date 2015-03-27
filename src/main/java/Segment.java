import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by yonisha on 3/26/2015.
 */
public class Segment {

    private String name;
    private Dictionary<String, Integer> tagsCounts = new Hashtable<String, Integer>();

    public Segment(String name) {
        this.name = name;
    }
    public String getText(){
        return name;
    }

    public void increment(String tag) {
        Integer tagCount = tagsCounts.get(tag);

        if (tagCount == null) {
            tagsCounts.put(tag, 1);
        }else{
            tagCount++;
        }
    }

    public Dictionary<String, Double> getProbabilities() {
        double total = 0;

        Enumeration<Integer> elements = tagsCounts.elements();
        while (elements.hasMoreElements()) {
            Integer count = elements.nextElement();
            total += count;
        }

        Dictionary<String, Double> probabilities = new Hashtable<String, Double>();

        Enumeration<String> keys = tagsCounts.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Integer count = tagsCounts.get(key);

            probabilities.put(key, count / total);
        }

        return probabilities;
    }
}
