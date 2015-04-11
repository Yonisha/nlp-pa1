package train;

import common.FileHelper;
import temp.NgramWithProb;
import temp.NgramsByLength;
import temp.SegmentWithTagProbs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class TrainerResult{
    private static final String NGRAM_HEADER_TEMPLATE = "\\%s-grams\\";
    private List<NgramsByLength> stateTransitionProbabilities;
    private List<SegmentWithTagProbs> symbolEmissionProbabilities;

    public TrainerResult(List<NgramsByLength> stateTransitionProbabilities, List<SegmentWithTagProbs> symbolEmissionProbabilities){
        this.stateTransitionProbabilities = stateTransitionProbabilities;
        this.symbolEmissionProbabilities = symbolEmissionProbabilities;
    }

    public List<NgramsByLength> getStateTransitionProbabilities(){
        return this.stateTransitionProbabilities;
    }

    public List<SegmentWithTagProbs> getSymbolEmissionProbabilities(){
        return this.symbolEmissionProbabilities;
    }

    public static TrainerResult buildTrainResult(String lexFilename, String gramFilename) throws IOException {
        List<SegmentWithTagProbs> segmentWithTagProbses = parseLexFile(lexFilename);
        List<NgramsByLength> ngramsByLengths = parseGramFile(gramFilename);

        return new TrainerResult(ngramsByLengths, segmentWithTagProbses);
    }

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

    private static List<NgramsByLength> parseGramFile(String gramFilename) throws IOException {
        List<String> lexFileLines = FileHelper.readLinesFromFile(gramFilename);
        List<NgramsByLength> ngramsByLengths = new ArrayList<>();

        int currentLength = 1;
        String currentNgram = String.format(NGRAM_HEADER_TEMPLATE, currentLength);

        List<NgramWithProb> ngramWithProbs = new ArrayList<>();

        lexFileLines = removeDataHeaderLines(lexFileLines);

        for (String line : lexFileLines) {
            if (line.equalsIgnoreCase(currentNgram)) {
                continue;
            }

            String[] ngramWithProb = line.split("\t");
            if (ngramWithProb.length == 1) {
                ngramsByLengths.add(new NgramsByLength(currentLength, ngramWithProbs));
                currentLength++;
                currentNgram = String.format(NGRAM_HEADER_TEMPLATE, currentLength);
                ngramWithProbs = new ArrayList<>();

                continue;
            }

            double prob = Double.parseDouble(ngramWithProb[0]);
            String ngram = ngramWithProb[1];
            ngramWithProbs.add(new NgramWithProb(ngram, prob));
        }

        ngramsByLengths.add(new NgramsByLength(currentLength, ngramWithProbs));

        return ngramsByLengths;
    }

    private static List<String> removeDataHeaderLines(List<String> lexFileLines) {
        while (true) {
            String line = lexFileLines.get(0);

            if (line.equalsIgnoreCase("")) {
                lexFileLines.remove(0);

                break;
            }

            lexFileLines.remove(0);
        }

        return lexFileLines;
    }
}
