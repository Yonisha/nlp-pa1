import java.util.List;

public class NgramsByLength{

    private List<Ngram> ngrams;
    private int length;

    public NgramsByLength(int length, List<Ngram> ngrams){
        this.ngrams = ngrams;
        this.length = length;
    }
}
