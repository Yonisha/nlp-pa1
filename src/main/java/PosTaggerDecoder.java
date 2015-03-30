import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PosTaggerDecoder {

    private ISentenceDecoder sentenceDecoder;

    public PosTaggerDecoder(ISentenceDecoder sentenceDecoder){
        this.sentenceDecoder = sentenceDecoder;
    }

    public void decode(String inputFileName, String outputFileName) throws IOException {
        List<String> inputLines = FileHelper.readLinesFromFile(inputFileName);
        List<InputSentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();
        for (String line: inputLines) {
            if (line.equals("")){
                sentences.add(new InputSentence(currentSentenceSegments));
                currentSentenceSegments = new ArrayList<>();
            }else{
                currentSentenceSegments.add(line);
            }
        }

        List<String> outputLines = new ArrayList<>();
        for (InputSentence sentence: sentences){
            List<String> tags = sentenceDecoder.decode(sentence);
            for (int i = 0; i <tags.size(); i++) {
                outputLines.add(sentence.getSegments().get(i) + "\t" + tags.get(i));
            }
            outputLines.add("");
        }

        FileHelper.writeLinesToFile(outputLines, outputFileName);
    }
}
