public class State{
    private String tag;
    private State previousState;
    private double maxProb;

    public State(String tag){
        this.tag = tag;
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

    public void setPreviousState(State previousState){
        this.previousState = previousState;
    }

    public void setMaxProb(double maxProb){
        this.maxProb = maxProb;
    }
}
