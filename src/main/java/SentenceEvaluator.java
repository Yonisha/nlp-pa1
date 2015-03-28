import java.util.ArrayList;
import java.util.List;

public class SentenceEvaluator{
    public SentenceAccuracy evaluate(int sentenceNum, List<WordWithTag> decoderTaggingForCurrentSentence, List<WordWithTag> goldTaggingForCurrentSentence){
        return new SentenceAccuracy(sentenceNum, new ArrayList<>(), 0);
    }
}
