public class InnerState {

    private State previousState;
    private double maxProb;

    public InnerState(double maxProb, State previousState) {
        this.maxProb = maxProb;
        this.previousState = previousState;
    }

    public State getPreviousState(){
        return this.previousState;
    }

    public double getMaxProb(){
        return this.maxProb;
    }

    public void setPreviousState(State previousState){
        this.previousState = previousState;
    }

    public void setMaxProb(double maxProb){
        this.maxProb = maxProb;
    }
}
