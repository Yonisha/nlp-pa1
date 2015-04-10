package decode;

import common.Commons;
import common.Sentence;
import temp.NgramWithProb;
import temp.NgramsByLength;
import temp.ProbWithPreviousTag;
import temp.SegmentWithTagProbs;
import train.TrainerResult;

import java.util.*;

public class SentenceDecoder implements ISentenceDecoder {

    private int order;
    private List<NgramsByLength> stateTransitionProbabilities;
    private List<SegmentWithTagProbs> symbolEmissionProbabilities;
    private List<String> tags;

    public SentenceDecoder(TrainerResult trainerResult, int order){
        this.stateTransitionProbabilities = trainerResult.getStateTransitionProbabilities();
        this.symbolEmissionProbabilities = trainerResult.getSymbolEmissionProbabilities();
        this.order = order;
        tags = createTags();
    }

    public List<String> decode(Sentence sentence) {

        List<String> segments = sentence.getSegments();
        ProbWithPreviousTag[][] matrix = new ProbWithPreviousTag[segments.size()][tags.size()];

        // initialize
        List<String> prefix = new ArrayList<>();
        for (int i = 1; i < order; i++) {
            prefix.add("[s]");
        }


        String[] prefixArr = new String[prefix.size() + 1];
        for (int j = 0; j < tags.size(); j++) {
            List<String> newPrefix = new ArrayList<>();
            newPrefix.addAll(prefix);
            newPrefix.add(tags.get(j));

            String[] prefixArray = newPrefix.toArray(prefixArr);
            double transitionProb = getTransitionProbForTags(prefixArray);
            double emissionProb = getEmissionProbForTagAndWord(tags.get(j), segments.get(0));
            matrix[0][j] = new ProbWithPreviousTag(tags.get(j), null, transitionProb + emissionProb);
        }

        for (int i = 1; i < segments.size(); i++) {
            for (int j = 0; j < tags.size(); j++) {

                double emissionProb = getEmissionProbForTagAndWord(tags.get(j), segments.get(i));

                double maxTransitionProbWithProbOfPrevious = Double.NEGATIVE_INFINITY;
                ProbWithPreviousTag previousTagWithMaxProb = null;

                for (int k = 0; k < tags.size(); k++) {
                    double currentTransitionProb = getTransitionProbForTags(matrix[i - 1][k].getTag(), tags.get(j));
                    double probOfPrevious = matrix[i-1][k].getProb();
                    double transitionProbWithProbOfPrevious = currentTransitionProb + probOfPrevious;

                    if (transitionProbWithProbOfPrevious >= maxTransitionProbWithProbOfPrevious){
                        maxTransitionProbWithProbOfPrevious = transitionProbWithProbOfPrevious;
                        previousTagWithMaxProb = matrix[i-1][k];
                    }
                }

                double finalProb = emissionProb + maxTransitionProbWithProbOfPrevious;
                matrix[i][j] = new ProbWithPreviousTag(tags.get(j), previousTagWithMaxProb, finalProb);
            }
        }

        // get best tagging
        double maxProb = Double.NEGATIVE_INFINITY;
        ProbWithPreviousTag max = null;
        for (int j = 0; j < tags.size(); j++) {
            ProbWithPreviousTag current = matrix[segments.size() - 1][j];
            if (current.getProb() >= maxProb){
                maxProb = current.getProb();
                max = current;
            }
        }

        List<String> taggingResult = new ArrayList<>();
        ProbWithPreviousTag currentTag = max;
        taggingResult.add(currentTag.getTag());
        while (currentTag.getPrevious() != null){
            currentTag = currentTag.getPrevious();
            taggingResult.add(0, currentTag.getTag());
        }

        return taggingResult;
    }

    private double getEmissionProbForTagAndWord(String tag, String word) {
        Optional<SegmentWithTagProbs> matchingSegment = this.symbolEmissionProbabilities.stream().filter(s -> s.getName().equals(word)).findFirst();
        if (matchingSegment.isPresent()){
            return getLogProb(getProbByTag(tag, matchingSegment.get()));
        }else{
            Optional<SegmentWithTagProbs> unkSegment = this.symbolEmissionProbabilities.stream().filter(s -> s.getName().equals("UNK")).findFirst();
            if (unkSegment.isPresent()){
                return getLogProb(getProbByTag(tag, unkSegment.get()));
            }else{
                return -Double.MAX_VALUE;
            }
        }
    }

    private double getProbByTag(String tag, SegmentWithTagProbs segment){
        Enumeration<String> keys = segment.getTagsProbs().keys();
        while (keys.hasMoreElements()){
            String currentTagForSegment = keys.nextElement();
            if (currentTagForSegment.equals(tag)){
                double prob = segment.getTagsProbs().get(tag);
                return prob;
            }
        }
        return 0;
    }

    private double getTransitionProbForTags(String... tags) {

        // TODO: should ommit the -1 after refactoring stateTransitionProbabilities
        int requiredTransitionProbOrder = tags.length - 1;

        NgramsByLength allNgramTransitions = this.stateTransitionProbabilities.get(requiredTransitionProbOrder);
        String currentTagNgram = Commons.join(tags, " ");
        Optional<NgramWithProb> first = allNgramTransitions.getNgramsWithProb().stream().filter(n -> n.getNgram().equals(currentTagNgram)).findFirst();

        return getLogProb(first.isPresent() ? first.get().getProb() : 0);
    }

    private List<String> createTags(){
        List<String> uniqueTags = new ArrayList<>();
        NgramsByLength unigrams = this.stateTransitionProbabilities.get(0);
        for (NgramWithProb ngramWithProb: unigrams.getNgramsWithProb()){
            String currentTag = ngramWithProb.getNgramTags()[0];

            if (!uniqueTags.contains(currentTag)){
                uniqueTags.add(currentTag);
            }
        }

        uniqueTags.remove("[s]");
        uniqueTags.remove("[e]");

        return uniqueTags;
    }

    private double getLogProb(double prob){
        if (prob == 0)
            return -Double.MAX_VALUE;

        return Math.log(prob);
    }
}
