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

    public void train() throws IOException {
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

        createLexFile();
        createGramFile(sentences, maxNgramLength);
    }

    private void createGramFile(List<Sentence> sentences, int maxNGramLength) throws IOException {
        File gram = new File("c:/NLP/heb-pos.gram");
        gram.createNewFile();
        FileWriter fileWriter = new FileWriter(gram);

        NGramsCreator nGramsCreator = new NGramsCreator();
        List<NgramsByLength> ngramsByLengths = nGramsCreator.create(maxNGramLength, sentences);
        for (NgramsByLength ngramsByLength: ngramsByLengths){
            fileWriter.append(ngramsByLength.toString() + "\r\n");
        }

        fileWriter.close();
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

    private void createLexFile() throws IOException {
        File lex = new File("c:/NLP/heb-pos.lex");
        lex.createNewFile();
        FileWriter fileWriter = new FileWriter(lex);

        Segment unknownSegment = new Segment("UNK");

        for (Segment segment : segments) {
            if (segment.appearsOnce()){
                unknownSegment.increment(segment.getTagsCounts().keys().nextElement());
                continue;
            }

            String outputLine = getLexLinePerSegment(segment);
            fileWriter.append(outputLine);
        }

        String outputLineForUNK = getLexLinePerSegment(unknownSegment);
        fileWriter.append(outputLineForUNK);

        fileWriter.close();
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
