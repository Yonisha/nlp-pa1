import common.Sentence;
import common.FileHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class PosTaggerTrainer {

    private int maxNgramLength;
    private List<SegmentWithTagCounts> segmentsWithoutUnknown = new ArrayList<>();

    public PosTaggerTrainer(int maxNgramLength){
        this.maxNgramLength = maxNgramLength;
    }

    public TrainerResult train(String trainFile, String lexFile, String gramFile) throws IOException {
        List<Sentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();
        List<String> inputLines = FileHelper.readLinesFromFile(trainFile);

        for (String line: inputLines) {
            if (line.equals("")) {
                sentences.add(new Sentence(currentSentenceSegments, maxNgramLength));
                currentSentenceSegments = new ArrayList<>();
                continue;
            }

            String[] split = line.split("\t");
            String segmentName = split[0];
            String tag = split[1];
            currentSentenceSegments.add(tag);

            SegmentWithTagCounts segment = getSegment(segmentName);
            segment.increment(tag);
        }

        // write Lex file
        List<SegmentWithTagCounts> lexSegmentsWithCount = createLexSegments();
        List<SegmentWithTagProbs> lexSegments = createSegmentsWithTagProbs(lexSegmentsWithCount);
        List<String> lexOutputLines = createLexLines(lexSegments);
        FileHelper.writeLinesToFile(lexOutputLines, lexFile);

        // write grams file
        List<NgramsByLength> ngramsByLength = createNgramsByLength(sentences, maxNgramLength);
        List<String> gramOutputLines = createGramFile(ngramsByLength);
        FileHelper.writeLinesToFile(gramOutputLines, gramFile);

        return new TrainerResult(ngramsByLength, lexSegments);
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

    private SegmentWithTagCounts getSegment(String segmentName) {
        for (SegmentWithTagCounts segmentItem : segmentsWithoutUnknown) {
            if (segmentItem.getText().equalsIgnoreCase(segmentName)) {
                return segmentItem;
            }
        }

        SegmentWithTagCounts segment = new SegmentWithTagCounts(segmentName);
        segmentsWithoutUnknown.add(segment);

        return segment;
    }

    private List<SegmentWithTagProbs> createSegmentsWithTagProbs(List<SegmentWithTagCounts> segments){
        List<SegmentWithTagProbs> segmentsToReturn = new ArrayList<>();
        for (SegmentWithTagCounts segment : segments) {
            segmentsToReturn.add(createSegmentWithTagProbsFromSegmentWithCount(segment, segments));
        }

        return segmentsToReturn;
    }

    private List<String> createLexLines(List<SegmentWithTagProbs> segments){
        List<String> linesToReturn = new ArrayList<>();
        for (SegmentWithTagProbs segment : segments) {
            linesToReturn.add(getLexLinePerSegment(segment));
        }

        return linesToReturn;
    }

    private List<SegmentWithTagCounts> createLexSegments() throws IOException {

        SegmentWithTagCounts unknownSegment = new SegmentWithTagCounts("UNK");

        List<SegmentWithTagCounts> segmentsAfterConsideringUnknown = new ArrayList<>();
        for (SegmentWithTagCounts segment : segmentsWithoutUnknown) {
            if (segment.appearsOnce()){
                unknownSegment.increment(segment.getTagsCounts().keys().nextElement());
                continue;
            }

            segmentsAfterConsideringUnknown.add(segment);
        }

        segmentsAfterConsideringUnknown.add(unknownSegment);
        return segmentsAfterConsideringUnknown;
    }

    private SegmentWithTagProbs createSegmentWithTagProbsFromSegmentWithCount(SegmentWithTagCounts segment, List<SegmentWithTagCounts> segments){
        Dictionary<String, Double> probabilities = segment.getProbabilities(segments);
        return new SegmentWithTagProbs(segment.getText(), probabilities);
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
