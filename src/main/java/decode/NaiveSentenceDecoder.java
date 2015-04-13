package decode;

import common.Sentence;
import common.SegmentWithTagProbs;
import train.NaiveTrainerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The naive sentence decoder. For each sentence, this decoder simply finds the most frequently used tag.
 */
public class NaiveSentenceDecoder implements ISentenceDecoder {

    private List<SegmentWithTagProbs> symbolEmissionProbabilities;

    public NaiveSentenceDecoder(NaiveTrainerResult naiveTrainResult){
        this.symbolEmissionProbabilities = naiveTrainResult.getSymbolEmissionProbabilities();
    }

    public List<String> decode(Sentence sentence){
        ArrayList<String> sentenceTags = new ArrayList<>();

        for (String segment : sentence.getSegments()) {
            Optional<SegmentWithTagProbs> first = this.symbolEmissionProbabilities.stream().filter(s -> s.getName().equals(segment)).findFirst();

            String tagForSegment;
            if (first.isPresent()) {
                tagForSegment = first.get().getTagWithMaxProbability();
            } else {
                tagForSegment = "NNP";
            }

            sentenceTags.add(tagForSegment);
        }

        return sentenceTags;
    }
}
