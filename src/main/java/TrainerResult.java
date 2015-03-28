import java.util.List;

public class TrainerResult{
    private List<String> stateTransitionProbabilities;
    private List<String> symbolEmissionProbabilities;

    public TrainerResult(List<String> stateTransitionProbabilities, List<String> symbolEmissionProbabilities){
        this.stateTransitionProbabilities = stateTransitionProbabilities;
        this.symbolEmissionProbabilities = symbolEmissionProbabilities;
    }

    public List<String> getStateTransitionProbabilities(){
        return this.stateTransitionProbabilities;
    }

    public List<String> getSymbolEmissionProbabilities(){
        return this.symbolEmissionProbabilities;
    }
}
