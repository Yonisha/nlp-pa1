package train;

import common.FileHelper;
import temp.SegmentWithTagProbs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class NaiveTrainerResult {
    private List<SegmentWithTagProbs> symbolEmissionProbabilities;

    public NaiveTrainerResult(List<SegmentWithTagProbs> symbolEmissionProbabilities){
        this.symbolEmissionProbabilities = symbolEmissionProbabilities;
    }

    public List<SegmentWithTagProbs> getSymbolEmissionProbabilities(){
        return this.symbolEmissionProbabilities;
    }

    public static NaiveTrainerResult buildTrainResult(String lexFilename) throws IOException {
        List<SegmentWithTagProbs> segmentWithTagProbses = parseLexFile(lexFilename);

        return new NaiveTrainerResult(segmentWithTagProbses);
    }

    // TODO: duplicate with naive result.
    private static List<SegmentWithTagProbs> parseLexFile(String lexFilename) throws IOException {
        List<String> lexFileLines = FileHelper.readLinesFromFile(lexFilename);
        List<SegmentWithTagProbs> segmentsWithTagProbs = new ArrayList<>();

        for (String line: lexFileLines) {
            String[] parts = line.split("\t");
            String segment = parts[0];

            Dictionary<String, Double> posDictionary = new Hashtable<>();
            for (int i = 1; i < parts.length; i++) {
                String[] posWithProb = parts[i].split(" ");
                String pos = posWithProb[0];
                Double prob = Double.parseDouble(posWithProb[1]);

                posDictionary.put(pos, prob);
            }

            segmentsWithTagProbs.add(new SegmentWithTagProbs(segment, posDictionary));
        }

        return segmentsWithTagProbs;
    }
}
