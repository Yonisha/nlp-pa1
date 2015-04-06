package temp;

public class SentenceStatistics {
    private int sentenceNum;
    private double wordAccuracy;
    private int sentenceAccuracy;
    private int numberOfSegmentsInSentence;
    private int numberOfCorrectTaggedSegments;

    public SentenceStatistics(int sentenceNum, int numberOfSegmentsInSentence, int numberOfCorrectTaggedSegments) {
        this.sentenceNum = sentenceNum;
        this.numberOfSegmentsInSentence = numberOfSegmentsInSentence;
        this.numberOfCorrectTaggedSegments = numberOfCorrectTaggedSegments;

        this.wordAccuracy = (double)numberOfCorrectTaggedSegments/numberOfSegmentsInSentence;
        this.sentenceAccuracy = wordAccuracy == 1 ? 1 : 0;
    }

    public double getWordAccuracy(){
        return this.wordAccuracy;
    }

    public int getSentenceAccuracy(){
        return this.sentenceAccuracy;
    }

    public int getSentenceNum(){
        return this.sentenceNum;
    }

    public int getNumberOfSegmentsInSentence(){
        return this.numberOfSegmentsInSentence;
    }

    public int getNumberOfCorrectTaggedSegments(){
        return this.numberOfCorrectTaggedSegments;
    }
}
