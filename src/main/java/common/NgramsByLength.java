package common;

import java.util.List;

public class NgramsByLength {

    private List<NgramWithProb> ngrams;
    private int length;

    public NgramsByLength(int length, List<NgramWithProb> ngrams){
        this.ngrams = ngrams;
        this.length = length;
    }

    public String header(){
        return "\\" + length + "-grams\\";
    }

    public int getLength() {
        return length;
    }

    public List<NgramWithProb> getNgramsWithProb(){
        return ngrams;
    }
}
