import java.util.ArrayList;
import java.util.List;

public class NGramsCreator{

    public List<NgramsByLength> create(int maxNgramLength, List<Sentence> sentences){

        List<NgramsByLength> ngramsByLengths = new ArrayList<NgramsByLength>();

        for (int i = 1; i <maxNgramLength+1; i++) {
            ngramsByLengths.add(createNgramsByLength(i, sentences));
        }

        return ngramsByLengths;

    }

    private NgramsByLength createNgramsByLength(int length, List<Sentence> sentences){
        List<Ngram> ngrams = new ArrayList<Ngram>();

        for (Sentence sentence: sentences){
            createPerSentence(length, sentence);
        }
        //distinct
        return new NgramsByLength(length, ngrams);
    }

    private List<Ngram> createPerSentence(int length, Sentence sentence){
        List<String> segments = sentence.getSegments();
        List<String> segmentsWithStartAndEnd = new ArrayList<String>();
        for (int i = 0; i <length-1; i++) {
            segmentsWithStartAndEnd.add("[s]");
        }
        segmentsWithStartAndEnd.addAll(segments);
        for (int i = 0; i <length-1; i++) {
            segmentsWithStartAndEnd.add("[e]");
        }

        List<Ngram> ngrams = new ArrayList<Ngram>();
        List<String> currentNgramTags = new ArrayList<String>();

        for (int i = 0; i <segmentsWithStartAndEnd.size() - length + 1; i++) {
            currentNgramTags = new ArrayList<String>();
            for (int j = i; j <length; j++) {
                currentNgramTags.add(segmentsWithStartAndEnd.get(j));
            }
            ngrams.add(new Ngram(currentNgramTags));
        }

        return ngrams;
    }
}
