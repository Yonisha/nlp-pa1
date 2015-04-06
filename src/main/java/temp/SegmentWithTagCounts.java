package temp;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class SegmentWithTagCounts {

    private String name;
    private Dictionary<String, Integer> tagsCounts = new Hashtable<String, Integer>();

    public SegmentWithTagCounts(String name) {
        this.name = name;
    }

    public String getText(){
        return name;
    }

    public Dictionary<String, Integer> getTagsCounts(){
        return tagsCounts;
    }

    public void increment(String tag) {
        Integer tagCount = tagsCounts.get(tag);

        if (tagCount == null) {
            tagsCounts.put(tag, 1);
        }else{
            tagsCounts.put(tag, tagCount + 1);
        }
    }

    public boolean appearsOnce(){
        return tagsCounts.size() == 1 && tagsCounts.elements().nextElement() == 1;
    }

    public Dictionary<String, Double> getProbabilities(List<SegmentWithTagCounts> segments) {

        Dictionary<String, Double> probabilities = new Hashtable<String, Double>();

        Enumeration<String> keys = tagsCounts.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Integer count = tagsCounts.get(key);

            int totalTagCountInAllSegments = 0;
            for (SegmentWithTagCounts segment: segments){
                Enumeration<String> tagsInSegment = segment.getTagsCounts().keys();
                while (tagsInSegment.hasMoreElements()){
                    String currentTag = tagsInSegment.nextElement();
                    if (currentTag.equals(key)){
                        totalTagCountInAllSegments += segment.getTagsCounts().get(currentTag);
                    }
                }
            }

            probabilities.put(key, (double)count / totalTagCountInAllSegments);
        }

        return probabilities;
    }
}
