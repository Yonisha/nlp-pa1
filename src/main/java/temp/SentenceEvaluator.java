package temp;

import java.util.List;

public class SentenceEvaluator{
    public SentenceStatistics evaluate(int sentenceNum, List<WordWithTag> decoderTaggingForCurrentSentence, List<WordWithTag> goldTaggingForCurrentSentence){
        int correctTaggedSegments = 0;
        int numberOfSegmentsInSentence = decoderTaggingForCurrentSentence.size();
        for (int i = 0; i < numberOfSegmentsInSentence; i++) {
            if (decoderTaggingForCurrentSentence.get(i).getTag().equals(goldTaggingForCurrentSentence.get(i).getTag())){
                correctTaggedSegments += 1;
            }
        }
        return new SentenceStatistics(sentenceNum, numberOfSegmentsInSentence, correctTaggedSegments);
    }
}
