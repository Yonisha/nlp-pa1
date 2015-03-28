import java.util.ArrayList;
import java.util.List;

public class SentenceEvaluator{
    public SentenceAccuracy evaluate(int sentenceNum, List<WordWithTag> decoderTaggingForCurrentSentence, List<WordWithTag> goldTaggingForCurrentSentence){
        List<Double> wordAccuracies = new ArrayList<>();
        for (int i = 0; i < decoderTaggingForCurrentSentence.size(); i++) {
            wordAccuracies.add(1.0);
        }
        return new SentenceAccuracy(sentenceNum, wordAccuracies, 1);
    }
}
