public class ProbWithPreviousTag{
    private String tag;
    private ProbWithPreviousTag previous;
    private double prob;

    public ProbWithPreviousTag(String tag, ProbWithPreviousTag previous, double prob){
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

    public ProbWithPreviousTag getPrevious(){
        return this.previous;
    }
}
