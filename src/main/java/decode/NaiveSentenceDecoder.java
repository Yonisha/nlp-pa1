package decode;

import common.Sentence;
import temp.ISentenceDecoder;
import train.NaiveTrainResult;

import java.util.ArrayList;
import java.util.List;

public class NaiveSentenceDecoder implements ISentenceDecoder {

    private NaiveTrainResult naiveTrainResult;

    public NaiveSentenceDecoder(NaiveTrainResult naiveTrainResult){
        this.naiveTrainResult = naiveTrainResult;
    }

    public List<String> decode(Sentence sentence){
        ArrayList<String> sentenceTags = new ArrayList<>();

        for (String segment : sentence.getSegments()) {
            String tagForSegment = naiveTrainResult.getTagForSegment(segment);

            if (tagForSegment == null) {
                tagForSegment = "NNP";
            }

            sentenceTags.add(tagForSegment);
        }

        return sentenceTags;
    }
}
