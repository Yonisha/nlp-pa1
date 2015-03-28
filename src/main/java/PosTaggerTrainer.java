import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class PosTaggerTrainer {

    private int maxNgramLength;
    private List<Segment> segmentsWithoutUnknown = new ArrayList<Segment>();

    public PosTaggerTrainer(int maxNgramLength){
        this.maxNgramLength = maxNgramLength;
    }

    public TrainerResult train() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/NLP/heb-pos-small.train"));

        List<Sentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();

        String line = bufferedReader.readLine();
        while (line != null) {
            String[] split = line.split("\t");

            // TODO fix condition for empty line
            if (split.length != 2) {
                line = bufferedReader.readLine();
                sentences.add(new Sentence(currentSentenceSegments, maxNgramLength));
                currentSentenceSegments = new ArrayList<>();
                continue;
            }

            String segmentName = split[0];
            String tag = split[1];
            currentSentenceSegments.add(tag);

            Segment segment = getSegment(segmentName);
            segment.increment(tag);

            line = bufferedReader.readLine();
        }

        // write Lex file
        List<String> lexOutputLines = createLexOutputLines();
        FileHelper.writeLinesToFile(lexOutputLines, "c:/NLP/heb-pos.lex");

        // write grams file
        List<NgramsByLength> ngramsByLength = createNgramsByLength(sentences, maxNgramLength);
        List<String> gramOutputLines = createGramFile(ngramsByLength);
        FileHelper.writeLinesToFile(gramOutputLines, "c:/NLP/heb-pos.gram");

        return new TrainerResult(ngramsByLength, lexOutputLines);
    }

    private List<NgramsByLength> createNgramsByLength(List<Sentence> sentences, int maxNGramLength) throws IOException {

        NGramsCreator nGramsCreator = new NGramsCreator();
        List<NgramsByLength> ngramsByLengths = nGramsCreator.create(maxNGramLength, sentences);
        return ngramsByLengths;
    }

    private List<String> createGramFile(List<NgramsByLength> ngramsByLength) throws IOException {
        List<String> outputLines = new ArrayList<>();
        for (NgramsByLength ngramByLength: ngramsByLength){
            outputLines.add(ngramByLength.header());
            List<NgramWithProb> ngramsWithProb = ngramByLength.getNgramsWithProb();
            for (NgramWithProb ngramWithProb: ngramsWithProb){
                outputLines.add(ngramWithProb.toString());
            }
        }

        return outputLines;
    }

    private Segment getSegment(String segmentName) {
        for (Segment segmentItem : segmentsWithoutUnknown) {
            if (segmentItem.getText().equalsIgnoreCase(segmentName)) {
                return segmentItem;
            }
        }

        Segment segment = new Segment(segmentName);
        segmentsWithoutUnknown.add(segment);

        return segment;
    }

    private List<String> createLexOutputLines() throws IOException {

        Segment unknownSegment = new Segment("UNK");

        List<Segment> segmentsAfterConsideringUnknown = new ArrayList<>();
        for (Segment segment : segmentsWithoutUnknown) {
            if (segment.appearsOnce()){
                unknownSegment.increment(segment.getTagsCounts().keys().nextElement());
                continue;
            }

            segmentsAfterConsideringUnknown.add(segment);
        }

        segmentsAfterConsideringUnknown.add(unknownSegment);

        List<String> outputLines = new ArrayList<>();
        for (Segment segment : segmentsAfterConsideringUnknown) {
            String outputLine = getLexLinePerSegment(segment, segmentsAfterConsideringUnknown);
            outputLines.add(outputLine);
        }

        return outputLines;
    }

    private String getLexLinePerSegment(Segment segment, List<Segment> segments){
        String outputLine = segment.getText();
        Dictionary<String, Double> probabilities = segment.getProbabilities(segments);

        Enumeration<String> keys = probabilities.keys();
        while (keys.hasMoreElements()) {
            String currentKey = keys.nextElement();
            Double prob = probabilities.get(currentKey);
            outputLine += "\t" + currentKey + " " + prob;
        }

        return outputLine;
    }
}
