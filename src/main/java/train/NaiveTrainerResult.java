package train;

import common.FileHelper;
import common.SegmentWithTagProbs;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class NaiveTrainerResult {
    private List<SegmentWithTagProbs> symbolEmissionProbabilities;

    public NaiveTrainerResult(List<SegmentWithTagProbs> symbolEmissionProbabilities){
        this.symbolEmissionProbabilities = symbolEmissionProbabilities;
    }

    public List<SegmentWithTagProbs> getSymbolEmissionProbabilities(){
        return this.symbolEmissionProbabilities;
    }

    public static NaiveTrainerResult buildTrainResult(String lexFilename) throws IOException {
        List<SegmentWithTagProbs> segmentWithTagProbses = FileHelper.parseLexFile(lexFilename);

        return new NaiveTrainerResult(segmentWithTagProbses);
    }
}
