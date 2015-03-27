import java.util.List;

public class NgramsByLength{

    private List<NgramWithProb> ngrams;
    private int length;

    public NgramsByLength(int length, List<NgramWithProb> ngrams){
        this.ngrams = ngrams;
        this.length = length;
    }

    @Override
    public String toString(){
        String text = "\\" + length + "-grams\\\r\n";
        for (NgramWithProb ngram: ngrams) {
            text += ngram.toString();
        }
        return text;
    }
}
