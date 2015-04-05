import common.Sentence;
import common.NGram;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<NGram> bigrams = new ArrayList<NGram>();
        List<NGram> unigrams = new ArrayList<NGram>();
        for (Sentence sentence: sentences){
            List<NGram> bigramsPerSentence = createPerSentence(2, sentence);
            List<NGram> unigramsPerSentence = createPerSentence(1, sentence);
            bigrams.addAll(bigramsPerSentence);
            unigrams.addAll(unigramsPerSentence);
        }

        // Bonus: createNgramsByLengthFromNgrams
        // unigrams are calculated twice in total
        HashMap<String, Double> ngramsWithProbs = length == 1 ? createUnigramsFromNgrams(unigrams) : createBigramsFromNgrams(bigrams, createUniqueNgramsWithCount(unigrams));
        List<NgramWithProb> ngramWithProbs2 = new ArrayList<>();
        for (String ngram: ngramsWithProbs.keySet()){
            ngramWithProbs2.add(new NgramWithProb(ngram, ngramsWithProbs.get(ngram)));
        }
        return new NgramsByLength(length, ngramWithProbs2);
    }

    private  HashMap<String, Double> createUnigramsFromNgrams(List<NGram> ngrams){
        HashMap<String, Integer> uniqueNgramsWithCount = createUniqueNgramsWithCount(ngrams);
        int amountOfUnigrams = ngrams.size();
        HashMap<String, Double> uniqueNgramsWithProb = new HashMap<>();
        for(String unigram: uniqueNgramsWithCount.keySet()){
            uniqueNgramsWithProb.put(unigram, (double) uniqueNgramsWithCount.get(unigram) / amountOfUnigrams);
        }

        return uniqueNgramsWithProb;
    }

    private HashMap<String, Double> createBigramsFromNgrams(List<NGram> ngrams, HashMap<String, Integer> unigrams){
        HashMap<String, Integer> uniqueNgramsWithCount = createUniqueNgramsWithCount(ngrams);

        HashMap<String, Double> uniqueNgramsWithProb = new HashMap<>();
        for(String bigram: uniqueNgramsWithCount.keySet()){
            String firstTag = bigram.split(" ")[0];
            int countOfRelevantUnigram = unigrams.get(firstTag);
            uniqueNgramsWithProb.put(bigram, (double)uniqueNgramsWithCount.get(bigram)/countOfRelevantUnigram);
        }

        return uniqueNgramsWithProb;
    }

    private HashMap<String, Integer> createUniqueNgramsWithCount(List<NGram> ngrams){
        HashMap<String, Integer> uniqueNgramsWithCount = new HashMap<>();
        for(NGram ngram: ngrams){
            if (uniqueNgramsWithCount.containsKey(ngram.toString())){
                int count = uniqueNgramsWithCount.get(ngram.toString()) + 1;
                uniqueNgramsWithCount.put(ngram.toString(), count);
            }else{
                uniqueNgramsWithCount.put(ngram.toString(), 1);
            }
        }

        return uniqueNgramsWithCount;
    }

//    private NgramsByLength createNgramsByLengthFromNgrams(int length, List<Ngram> ngrams){
//        List<NgramWithProb> ngramsWithProb = new ArrayList<>();
//
//        return new NgramsByLength(length, ngramsWithProb);
//    }

    private List<NGram> createPerSentence(int length, Sentence sentence){
        List<String> segments = sentence.getSegments();

        List<NGram> ngrams = new ArrayList<NGram>();

        for (int i = 0; i <segments.size() - length + 1; i++) {
            List<String> currentNgramTags = new ArrayList<String>();
            for (int j = i; j <i+length; j++) {
                currentNgramTags.add(segments.get(j));
            }
            ngrams.add(new NGram(currentNgramTags));
        }

        return ngrams;
    }
}
