package decode;

import common.Commons;
import common.Sentence;
import common.NgramWithProb;
import common.NgramsByLength;
import common.SegmentWithTagProbs;
import train.TrainerResult;

import java.util.*;

/**
 * The HMM sentence decoder. This decoder uses dynamic programming in order to tag a sentence in linear time.
 * The decoder uses a matrix in order to find the most probable tag for each segment,
 * with relation to the previous tag assigned for the previous segment.
 */
public class SentenceDecoder implements ISentenceDecoder {

    private List<NgramsByLength> stateTransitionProbabilities;
    private List<SegmentWithTagProbs> symbolEmissionProbabilities;
    private List<String> tags;

    public SentenceDecoder(TrainerResult trainerResult){
        this.stateTransitionProbabilities = trainerResult.getStateTransitionProbabilities();
        this.symbolEmissionProbabilities = trainerResult.getSymbolEmissionProbabilities();
        tags = createTags();
    }

    public List<String> decode(Sentence sentence) {

        List<String> segments = sentence.getSegments();

        // initialize
        State[][] matrix = new State[segments.size()][tags.size()];
        for (int j = 0; j <tags.size(); j++) {
            double transitionProb = getTransitionProbForTwoTags("[s]", tags.get(j));
            double emissionProb = getEmissionProbForTagAndWord(tags.get(j), segments.get(0));
            matrix[0][j] = new State(tags.get(j), null, transitionProb + emissionProb);
        }

        for (int i = 1; i < segments.size(); i++) {
            for (int j = 0; j < tags.size(); j++) {

                double emissionProb = getEmissionProbForTagAndWord(tags.get(j), segments.get(i));

                double maxTransitionProbWithProbOfPrevious = Double.NEGATIVE_INFINITY;
                State previousTagWithMaxProb = null;

                for (int k = 0; k < tags.size(); k++) {
                    double currentTransitionProb = getTransitionProbForTwoTags(matrix[i-1][k].getTag(), tags.get(j));
                    double probOfPrevious = matrix[i-1][k].getProb();
                    double transitionProbWithProbOfPrevious = currentTransitionProb + probOfPrevious;

                    if (transitionProbWithProbOfPrevious >= maxTransitionProbWithProbOfPrevious){
                        maxTransitionProbWithProbOfPrevious = transitionProbWithProbOfPrevious;
                        previousTagWithMaxProb = matrix[i-1][k];
                    }
                }

                double finalProb = emissionProb + maxTransitionProbWithProbOfPrevious;
                matrix[i][j] = new State(tags.get(j), previousTagWithMaxProb, finalProb);
            }
        }

        List<String> bestTagging = getBestTagging(matrix);

        return bestTagging;
    }

    private List<String> getBestTagging(State[][] matrix) {
        double maxProb = Double.NEGATIVE_INFINITY;
        State max = null;
        for (int j = 0; j < tags.size(); j++) {
            State current = matrix[matrix.length - 1][j];
            if (current.getProb() >= maxProb){
                maxProb = current.getProb();
                max = current;
            }
        }

        List<String> taggingResult = new ArrayList<>();
        State currentTag = max;
        taggingResult.add(currentTag.getTag());
        while (currentTag.getPrevious() != null){
            currentTag = currentTag.getPrevious();
            taggingResult.add(0, currentTag.getTag());
        }

        return taggingResult;
    }

    private double getEmissionProbForTagAndWord(String tag, String word) {
        Optional<SegmentWithTagProbs> matchingSegment = this.symbolEmissionProbabilities.stream().filter(s -> s.getName().equals(word)).findFirst();
        if (matchingSegment.isPresent()) {
            return getProbByTag(tag, matchingSegment.get());
        } else {
            Optional<SegmentWithTagProbs> unkSegment = this.symbolEmissionProbabilities.stream().filter(s -> s.getName().equals("UNK")).findFirst();
            if (unkSegment.isPresent()){
                return getProbByTag(tag, unkSegment.get());
            } else {
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

        return -Double.MAX_VALUE;
    }

    private double getTransitionProbForTwoTags(String firstTag, String secondTag) {
        NgramsByLength allBigramTransitions = this.stateTransitionProbabilities.get(1);
        String currentTagBigram = firstTag + " " + secondTag;
        Optional<NgramWithProb> first = allBigramTransitions.getNgramsWithProb().stream().filter(n -> n.getNgram().equals(currentTagBigram)).findFirst();

        return first.isPresent() ? first.get().getProb() : Commons.getLogProb(0);
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
}
