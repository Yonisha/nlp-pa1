import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by yonisha on 3/26/2015.
 */
public class Trainer {
    private static List<Segment> segments = new ArrayList<Segment>();


    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/NLP/heb-pos.train"));


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
        createGramFile();
    }

    private static void createGramFile() {
        File gram = new File("c:/NLP/heb-pos.gram");
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

        for (Segment segment : segments) {
            String outputLine = segment.getText();
            Dictionary<String, Double> probabilities = segment.getProbabilities();

            Enumeration<String> keys = probabilities.keys();
            while (keys.hasMoreElements()) {
                String currentKey = keys.nextElement();
                Double prob = probabilities.get(currentKey);
                outputLine += "\t" + currentKey + "\t" + prob;
            }


            fileWriter.append(outputLine + "\r\n");
        }

        fileWriter.close();
    }
}
