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
        segmentsWithTagsCount = FileHelper.getSegmentsWithTagCounts(trainFile);

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
}
