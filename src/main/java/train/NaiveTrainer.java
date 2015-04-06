package train;

import common.FileHelper;
import temp.SegmentWithTagCounts;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NaiveTrainer {

    private List<SegmentWithTagCounts> segmentsWithTagsCount = new ArrayList<>();

    public NaiveTrainResult train(String trainFile) throws IOException {
        countSegmentsTags(trainFile);

        return buildTrainResult();
    }

    private NaiveTrainResult buildTrainResult() {
        NaiveTrainResult naiveTrainResult = new NaiveTrainResult();

        segmentsWithTagsCount.parallelStream().forEach(segment -> {
            Enumeration<String> keys = segment.getTagsCounts().keys();

            int maxTagCount = 0;
            String mostCommonTag = null;
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                int tagCount = segment.getTagsCounts().get(key);

                if (tagCount > maxTagCount) {
                    mostCommonTag = key;
                }
            }

            naiveTrainResult.addTagForSegment(segment.getText(), mostCommonTag);
        });

        return naiveTrainResult;
    }

    private void countSegmentsTags(String trainFile) throws IOException {
        List<String> inputLines = FileHelper.readLinesFromFile(trainFile);
        for (String line: inputLines) {
            if (line.equals("")) {
                continue;
            }

            String[] split = line.split("\t");
            String segmentName = split[0];
            String tag = split[1];

            SegmentWithTagCounts segment = getSegment(segmentName);
            segment.increment(tag);
        }
    }

    private SegmentWithTagCounts getSegment(String segmentName) {
        for (SegmentWithTagCounts segmentItem : segmentsWithTagsCount) {
            if (segmentItem.getText().equalsIgnoreCase(segmentName)) {
                return segmentItem;
            }
        }

        SegmentWithTagCounts segment = new SegmentWithTagCounts(segmentName);
        segmentsWithTagsCount.add(segment);

        return segment;
    }
}
