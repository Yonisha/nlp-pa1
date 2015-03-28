public class NgramWithProb{
    private String ngram;
    private double prob;

    public NgramWithProb(String ngram, double prob){
        this.ngram = ngram;
        this.prob = prob;
    }

    @Override
    public String toString(){
        return  prob + "\t" + ngram;
    }
}
