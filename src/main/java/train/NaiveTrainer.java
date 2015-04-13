package train;

import common.Commons;
import common.FileHelper;
import common.SegmentWithTagCounts;
import common.SegmentWithTagProbs;

import java.io.IOException;
import java.util.*;

/**
 * The naive trainer. For each segment, this trainer finds the most frequently used tag.
 */
public class NaiveTrainer {

    public NaiveTrainerResult train(String trainFile, String lexFile) throws IOException {
        List<SegmentWithTagCounts> segmentsWithTagsCount = FileHelper.getSegmentsWithTagCounts(trainFile);
        List<SegmentWithTagProbs> segmentsWithTagProbs = new ArrayList<>();

        for (SegmentWithTagCounts segmentWithTagCounts : segmentsWithTagsCount) {
            int totalSegmentOccurrences = getTotalSegmentOccurrences(segmentWithTagCounts);
            Dictionary<String, Double> probabilities = new Hashtable<String, Double>();

            Dictionary<String, Integer> tagsCounts = segmentWithTagCounts.getTagsCounts();
            Enumeration<String> keys = tagsCounts.keys();

            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                Integer count = tagsCounts.get(key);
                double probability = count / (double)totalSegmentOccurrences;
                double logProb = Commons.getLogProb(probability);

                probabilities.put(key, logProb);
            }

            segmentsWithTagProbs.add(new SegmentWithTagProbs(segmentWithTagCounts.getText(), probabilities));
        }

        List<String> outputLines = new ArrayList<>();
        for (SegmentWithTagProbs segmentWithTagCounts : segmentsWithTagProbs) {
            outputLines.add(getLexLinePerSegment(segmentWithTagCounts));
        }

        FileHelper.writeLinesToFile(outputLines, lexFile);

        return new NaiveTrainerResult(segmentsWithTagProbs);
    }

    private int getTotalSegmentOccurrences(SegmentWithTagCounts segmentWithTagCounts) {
        int occurrences = 0;

        Enumeration<Integer> elements = segmentWithTagCounts.getTagsCounts().elements();
        while (elements.hasMoreElements()) {
            occurrences += elements.nextElement();
        }

        return occurrences;
    }

    private String getLexLinePerSegment(SegmentWithTagProbs segment){
        String outputLine = segment.getName();
        Dictionary<String, Double> probabilities = segment.getTagsProbs();

        Enumeration<String> keys = probabilities.keys();
        while (keys.hasMoreElements()) {
            String currentKey = keys.nextElement();
            Double prob = probabilities.get(currentKey);
            outputLine += "\t" + currentKey + " " + prob;
        }

        return outputLine;
    }
}
