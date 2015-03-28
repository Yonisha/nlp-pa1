import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by yonisha on 3/26/2015.
 */
public class trainer {
    private static List<Segment> segments = new ArrayList<Segment>();


    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/NLP/heb-pos-small.train"));
        int maxNGramLength = 2;

        List<Sentence> sentences = new ArrayList<Sentence>();
        List<String> currentSentenceSegments = new ArrayList<String>();


        String line = bufferedReader.readLine();
        while (line != null) {
            String[] split = line.split("\t");

            // fix condition for empty line
            if (split.length != 2) {
                line = bufferedReader.readLine();
                sentences.add(new Sentence(currentSentenceSegments));
                currentSentenceSegments = new ArrayList<String>();
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
        createGramFile(sentences, maxNGramLength);
    }

    private static void createGramFile(List<Sentence> sentences, int maxNGramLength) throws IOException {
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
    private static Segment getSegment(String segmentName) {
        for (Segment segmentItem : segments) {
            if (segmentItem.getText().equalsIgnoreCase(segmentName)) {
                return segmentItem;
            }
        }

        Segment segment = new Segment(segmentName);
        segments.add(segment);

        return segment;
    }

    private static void createLexFile() throws IOException {
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

    private static String getLexLinePerSegment(Segment segment){
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
