import java.util.Dictionary;
import java.util.Hashtable;

public class SegmentWithTagProbs {

    private String name;
    private Dictionary<String, Double> tagsProbs = new Hashtable<String, Double>();

    public SegmentWithTagProbs(String name, Dictionary<String, Double> tagsProbs) {
        this.name = name;
        this.tagsProbs = tagsProbs;
    }

    public String getName(){
        return this.name;
    }

    public Dictionary<String, Double> getTagsProbs(){
        return this.tagsProbs;
    }
}
