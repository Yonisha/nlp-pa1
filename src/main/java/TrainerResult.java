import java.util.List;

public class TrainerResult{
    private List<NgramsByLength> stateTransitionProbabilities;
    private List<String> symbolEmissionProbabilities;

    public TrainerResult(List<NgramsByLength> stateTransitionProbabilities, List<String> symbolEmissionProbabilities){
        this.stateTransitionProbabilities = stateTransitionProbabilities;
        this.symbolEmissionProbabilities = symbolEmissionProbabilities;
    }

    public List<NgramsByLength> getStateTransitionProbabilities(){
        return this.stateTransitionProbabilities;
    }

    public List<String> getSymbolEmissionProbabilities(){
        return this.symbolEmissionProbabilities;
    }
}
