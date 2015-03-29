import java.util.List;

public class TrainerResult{
    private List<NgramsByLength> stateTransitionProbabilities;
    private List<SegmentWithTagProbs> symbolEmissionProbabilities;

    public TrainerResult(List<NgramsByLength> stateTransitionProbabilities, List<SegmentWithTagProbs> symbolEmissionProbabilities){
        this.stateTransitionProbabilities = stateTransitionProbabilities;
        this.symbolEmissionProbabilities = symbolEmissionProbabilities;
    }

    public List<NgramsByLength> getStateTransitionProbabilities(){
        return this.stateTransitionProbabilities;
    }

    public List<SegmentWithTagProbs> getSymbolEmissionProbabilities(){
        return this.symbolEmissionProbabilities;
    }
}
