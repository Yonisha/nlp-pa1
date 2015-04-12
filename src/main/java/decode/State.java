package decode;

public class State {
    private String tag;
    private State previous;
    private double prob;

    public State(String tag, State previous, double prob){
        this.tag = tag;
        this.previous = previous;
        this.prob = prob;
    }

    public String getTag(){
        return tag;
    }

    public double getProb(){
        return this.prob;
    }

    public State getPrevious(){
        return this.previous;
    }
}
