package train;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NaiveTrainResult {
    public ConcurrentMap<String, String> segmentsTags = new ConcurrentHashMap<>();

    public void addTagForSegment(String segment, String tag) {
        if (segmentsTags.containsKey(segment)) {
            throw new IllegalArgumentException("Segments should be added only once. Segment: " + segment);
        }

        segmentsTags.put(segment, tag);
    }

    public String getTagForSegment(String segment) {
        return segmentsTags.get(segment);
    }
}
