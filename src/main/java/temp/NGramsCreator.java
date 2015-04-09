package temp;

import common.Sentence;
import common.NGram;

import java.util.*;

public class NGramsCreator{

    public List<NgramsByLength> create(int maxNgramLength, List<Sentence> sentences){

        List<NgramsByLength> ngramsByLengths = new ArrayList<NgramsByLength>();

        // TODO: can be improved to avoid recreating previous ngram every time.
        for (int i = 1; i < maxNgramLength + 1; i++) {
            ngramsByLengths.add(createNgramsByLength(i, sentences));
        }

        return ngramsByLengths;
    }

    private NgramsByLength createNgramsByLength(int length, List<Sentence> sentences) {
        Map<Integer, List<NGram>> ngramsDictionary = new HashMap<>();

        List<NGram> totalPerLevel = new ArrayList<>();
        for (int i = 1; i <= length; i++) {
            for (Sentence sentence: sentences){
                List<NGram> perSentence = createPerSentence(i, sentence);
                totalPerLevel.addAll(perSentence);
            }

            ngramsDictionary.put(i, totalPerLevel);
            totalPerLevel = new ArrayList<>();
        }

        HashMap<String, Double> ngramsWithProbs = createNgramsWithProbability(ngramsDictionary.get(length), ngramsDictionary.get(length - 1));
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

    private HashMap<String, Double> createNgramsWithProbability(List<NGram> ngrams, List<NGram> ngramsWithPrevLevel){
        if (ngramsWithPrevLevel == null) {
            return createUnigramsFromNgrams(ngrams);
        }

        HashMap<String, Integer> ngramsWithPreviousLevelCount = createUniqueNgramsWithCount(ngramsWithPrevLevel);
        HashMap<String, Integer> uniqueNgramsWithCount = createUniqueNgramsWithCount(ngrams);

        HashMap<String, Double> uniqueNgramsWithProb = new HashMap<>();
        for(String bigram: uniqueNgramsWithCount.keySet()){
            int indexOfLastSpace = bigram.lastIndexOf(" ");
            String firstTag = bigram.substring(0, indexOfLastSpace);
            int countOfRelevantUnigram = ngramsWithPreviousLevelCount.get(firstTag);
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
