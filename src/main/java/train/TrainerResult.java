package train;

import common.FileHelper;
import common.NgramWithProb;
import common.NgramsByLength;
import common.SegmentWithTagProbs;

import java.io.IOException;
import java.util.ArrayList;
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
        List<SegmentWithTagProbs> segmentWithTagProbses = FileHelper.parseLexFile(lexFilename);
        List<NgramsByLength> ngramsByLengths = parseGramFile(gramFilename);

        return new TrainerResult(ngramsByLengths, segmentWithTagProbses);
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
