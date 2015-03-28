import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

public class PosTaggerTrainer {

    private int maxNgramLength;
    private List<Segment> segments = new ArrayList<Segment>();

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

            // fix condition for empty line
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

        for (int i = 0; i <sentences.size(); i++) {
            System.out.println(sentences.get(i).toString());
        }

        // write Lex file
        List<String> lexOutputLines = createLexOutputLines();
        writeLinesToFile(lexOutputLines, "c:/NLP/heb-pos.lex");

        // write grams file
        List<String> gramOutputLines = createGramFile(sentences, maxNgramLength);
        writeLinesToFile(gramOutputLines, "c:/NLP/heb-pos.gram");

        return new TrainerResult(gramOutputLines, lexOutputLines);
    }

    private List<String> createGramFile(List<Sentence> sentences, int maxNGramLength) throws IOException {

        List<String> outputLines = new ArrayList<>();
        NGramsCreator nGramsCreator = new NGramsCreator();
        List<NgramsByLength> ngramsByLengths = nGramsCreator.create(maxNGramLength, sentences);
        for (NgramsByLength ngramsByLength: ngramsByLengths){
            outputLines.add(ngramsByLength.toString() + "\r\n");
        }

        return outputLines;
    }

    private void writeLinesToFile(List<String> lines, String fileName) throws IOException {
        File file = new File("c:/NLP/heb-pos.lex");
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);

        for (String line: lines){
            fileWriter.append(line);
        }

        fileWriter.flush();
    }

    private Segment getSegment(String segmentName) {
        for (Segment segmentItem : segments) {
            if (segmentItem.getText().equalsIgnoreCase(segmentName)) {
                return segmentItem;
            }
        }

        Segment segment = new Segment(segmentName);
        segments.add(segment);

        return segment;
    }

    private List<String> createLexOutputLines() throws IOException {

        List<String> outputLines = new ArrayList<>();
        Segment unknownSegment = new Segment("UNK");

        for (Segment segment : segments) {
            if (segment.appearsOnce()){
                unknownSegment.increment(segment.getTagsCounts().keys().nextElement());
                continue;
            }

            String outputLine = getLexLinePerSegment(segment);
            outputLines.add(outputLine);
        }

        String outputLineForUNK = getLexLinePerSegment(unknownSegment);
        outputLines.add(outputLineForUNK);

        return outputLines;
    }

    private String getLexLinePerSegment(Segment segment){
        String outputLine = segment.getText();
        Dictionary<String, Double> probabilities = segment.getProbabilities();

        Enumeration<String> keys = probabilities.keys();
        while (keys.hasMoreElements()) {
            String currentKey = keys.nextElement();
            Double prob = probabilities.get(currentKey);
            outputLine += "\t" + currentKey + "\t" + prob;
        }

        return outputLine + "\r\n";
    }
}
