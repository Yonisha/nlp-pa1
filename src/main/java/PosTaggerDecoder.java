import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PosTaggerDecoder {

    private TrainerResult trainerResult;
    private SentenceDecoder sentenceDecoder = new SentenceDecoder(trainerResult);

    public PosTaggerDecoder(TrainerResult trainerResult){
        this.trainerResult = trainerResult;
    }

    public void decode() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/NLP/heb-pos-small.test"));
        List<InputSentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();
        String line = bufferedReader.readLine();
        while (line != null) {
            if (line.equals("")){
                sentences.add(new InputSentence(currentSentenceSegments));
                currentSentenceSegments = new ArrayList<>();
            }else{
                currentSentenceSegments.add(line);
            }

            line = bufferedReader.readLine();
        }

        for (InputSentence sentence: sentences){
            System.out.println(sentence);
        }

        List<String> outputLines = new ArrayList<>();
        for (InputSentence sentence: sentences){
            List<String> tags = sentenceDecoder.decode(sentence);
            for (int i = 0; i <tags.size(); i++) {
                outputLines.add(sentence.getSegments().get(i) + "\t" + tags.get(i));
            }
        }

        for (String outputLine: outputLines){
            System.out.println(outputLine);
        }
    }
}
