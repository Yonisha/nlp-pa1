package decode;

/**
 * This class represents a state in the process of a sentence decoding.
 * This state represents a segment's tag result as well as the probability for the assigned tag and the previous state
 * that brought it to its maximum.
 */
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
