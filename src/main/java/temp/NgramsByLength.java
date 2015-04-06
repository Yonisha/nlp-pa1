package temp;

import java.util.List;

public class NgramsByLength{

    private List<NgramWithProb> ngrams;
    private int length;

    public NgramsByLength(int length, List<NgramWithProb> ngrams){
        this.ngrams = ngrams;
        this.length = length;
    }

    public String header(){
        return "\\" + length + "-grams\\";
    }

    public List<NgramWithProb> getNgramsWithProb(){
        return ngrams;
    }

//    @Override
//    public String toString(){
//        String text =
//        for (temp.NgramWithProb ngram: ngrams) {
//            text += ngram.toString() + "\r\n";
//
//        }
//        return text;
//    }
}
