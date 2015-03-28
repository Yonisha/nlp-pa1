import java.util.List;

public class SentenceAccuracy{
    private int sentenceNum;
    private List<Double> wordAccuracies;
    private double sentenceAccuracy;

    public SentenceAccuracy(int sentenceNum, List<Double> wordAccuracies, double sentenceAccuracy) {
        this.wordAccuracies = wordAccuracies;
        this.sentenceAccuracy = sentenceAccuracy;
        this.sentenceNum = sentenceNum;
    }

    public List<Double> getWordAccuracies(){
        return this.wordAccuracies;
    }

    public double getSentenceAccuracy(){
        return this.sentenceAccuracy;
    }

    public int getSentenceNum(){
        return this.sentenceNum;
    }
}
