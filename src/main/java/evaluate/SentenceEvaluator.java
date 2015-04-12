package evaluate;

import java.util.List;

public class SentenceEvaluator{
    public SentenceStatistics evaluate(int sentenceNum, List<String> decoderTaggingForCurrentSentence, List<String> goldTaggingForCurrentSentence){
        int correctTaggedSegments = 0;
        int numberOfSegmentsInSentence = decoderTaggingForCurrentSentence.size();
        for (int i = 0; i < numberOfSegmentsInSentence; i++) {
            if (decoderTaggingForCurrentSentence.get(i).equals(goldTaggingForCurrentSentence.get(i))){
                correctTaggedSegments += 1;
            }
        }

        return new SentenceStatistics(sentenceNum, numberOfSegmentsInSentence, correctTaggedSegments);
    }
}
