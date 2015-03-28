public class State{
    private String tag;
    private State previousState;
    private double maxProb;

    public State(String tag, State previousState, double maxProb){
        this.tag = tag;
        this.previousState = previousState;
        this.maxProb = maxProb;
    }

    public String getTag(){
        return this.tag;
    }

    public State getPreviousState(){
        return this.previousState;
    }

    public double getMaxProb(){
        return this.maxProb;
    }
}
