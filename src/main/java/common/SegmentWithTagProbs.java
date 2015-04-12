package common;

import java.util.Dictionary;
import java.util.Enumeration;
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

    // TODO: can be used in SentenceDecoder??
    public String getTagWithMaxProbability() {
        Enumeration<String> keys = this.tagsProbs.keys();

        String tagWithMaxProb = null;
        double maxProbability = -Double.MAX_VALUE;
        while (keys.hasMoreElements()) {
            String currentKey = keys.nextElement();
            double probability = this.tagsProbs.get(currentKey);

            if (probability >= maxProbability) {
                maxProbability = probability;
                tagWithMaxProb = currentKey;
            }
        }

        return tagWithMaxProb;
    }
}
