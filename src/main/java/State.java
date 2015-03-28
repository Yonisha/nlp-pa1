import java.util.ArrayList;
import java.util.List;

public class State{
    private String tag;
    private List<InnerState> innerStates = new ArrayList<>();

    public State(String tag){
        this.tag = tag;
    }

    public void addInnerState(double prob, State prevState) {
        new InnerState(prob, prevState);
    }

    public String getTag(){
        return this.tag;
    }

    public InnerState getLastInnerState() {
        return innerStates.get(innerStates.size() - 1);
    }

    public void removeIteration() {
        innerStates.remove(innerStates.size() - 1);
    }

    public int getIteration() {
        return innerStates.size();
    }
}
