import java.util.*;

public class SentenceDecoder implements ISentenceDecoder{

    private List<NgramsByLength> stateTransitionProbabilities;
    private List<String> symbolEmissionProbabilities;


    public SentenceDecoder(TrainerResult trainerResult){
        this.stateTransitionProbabilities = trainerResult.getStateTransitionProbabilities();
        this.symbolEmissionProbabilities = trainerResult.getSymbolEmissionProbabilities();
    }

    public List<String> decode(InputSentence sentence){

        List<String> alphabet = sentence.getSegments();
        // move to main decoder and do once
        Set<State> states = createStates();
        initialize(states, sentence);


        return sentence.getSegments();
    }

//    private List<State> viterbiRec(int iteration, Set<State> states, List<String> segmentsInSentence){
//        // Base case
//        if (segmentsInSentence.size() == 0) {
//            State maxLastIteration = getMaxLastIteration(iteration, states);
//            ArrayList<State> bestPath = new ArrayList<>();
//            bestPath.add(maxLastIteration);
//
//            return bestPath;
//        }
//
//        String currentSegment = segmentsInSentence.get(0);
//        segmentsInSentence.remove(0);
//
//        return viterbiRec(iteration++, states, segmentsInSentence);
//    }

//    private State getMaxLastIteration(int iteration, Set<State> states) {
//
//        State stateWithMaxProb = null;
//        double maxProb = 0;
//        for (State state : states) {
//            double currentMaxProb = state.getLastInnerState().getMaxProb();
//            if (state.getIteration() == iteration && currentMaxProb > maxProb) {
//                maxProb = currentMaxProb;
//                stateWithMaxProb = state;
//            }
//        }
//
//        return stateWithMaxProb;
//    }

//    private Set<State> initialize(Set<State> states, InputSentence sentence) {
//        NgramsByLength bigrams = this.stateTransitionProbabilities.get(1);
//        List<NgramWithProb> ngramsWithProb = bigrams.getNgramsWithProb();
//        for (NgramWithProb ngramWithProb: ngramsWithProb) {
//            String tag1 = ngramWithProb.getNgramTags()[0];
//            String tag2 = ngramWithProb.getNgramTags()[1];
//            if (tag1.equals("[s]")) {
//                double transitionProb = ngramWithProb.getProb();
//
//                Iterator<State> iterator = states.iterator();
//                while (iterator.hasNext()){
//                    State currentState = iterator.next();
//                    if (currentState.getTag().equals(tag2)){
//                        double emissionProb = getEmissionProbForSegmentAndTag(sentence.getSegments().get(0), tag2);
//                        currentState.addInnerState(transitionProb*emissionProb, new State(tag1));
//                    }
//                }
//            }
//        }
//
//        return states;
//    }


    private Set<State> initialize(Set<State> states, InputSentence sentence) {
        NgramsByLength bigrams = this.stateTransitionProbabilities.get(1);
        List<NgramWithProb> ngramsWithProb = bigrams.getNgramsWithProb();
        for (NgramWithProb ngramWithProb: ngramsWithProb) {
            String tag1 = ngramWithProb.getNgramTags()[0];
            String tag2 = ngramWithProb.getNgramTags()[1];
            if (tag1.equals("[s]")) {
                double transitionProb = ngramWithProb.getProb();

                Iterator<State> iterator = states.iterator();
                while (iterator.hasNext()){
                    State currentState = iterator.next();
                    if (currentState.getTag().equals(tag2)){
                        double emissionProb = getEmissionProbForSegmentAndTag(sentence.getSegments().get(0), tag2);
                        currentState.addInnerState(transitionProb*emissionProb, new State(tag1));
                    }
                }
            }
        }

        return states;
    }


    private double getEmissionProbForSegmentAndTag(String segment, String tag){
        for (String line : this.symbolEmissionProbabilities) {
            String[] partsOfLine = line.split("\t");
            String wordInLine = partsOfLine[0];
            if (segment.equals(wordInLine)){
                for (int i = 1; i < partsOfLine.length; i++) {
                    String[] tagAndProb = partsOfLine[i].split(" ");
                    if (tagAndProb[0].equals(tag)){
                        return Double.parseDouble(tagAndProb[1]);
                    }
                }
            }
        }

        // TODO hack
        String unknown = this.symbolEmissionProbabilities.get(this.symbolEmissionProbabilities.size()-1);
        String[] partsOfLine = unknown.split("\t");
        String wordInLine = partsOfLine[0];
        for (int i = 1; i < partsOfLine.length; i++) {
            String[] tagAndProb = partsOfLine[i].split(" ");
            if (tagAndProb[0].equals(tag)){
                return Double.parseDouble(tagAndProb[1]);
            }
        }

        return 0;
    }


    private Set<State> createStates(){
        Set<String> uniqueTags = new HashSet<>();
        NgramsByLength unigrams = this.stateTransitionProbabilities.get(0);
        for (NgramWithProb ngramWithProb: unigrams.getNgramsWithProb()){
            uniqueTags.add(ngramWithProb.getNgramTags()[0]);
        }

        Set<State> states = new HashSet<>();
        for (String uniqueTag: uniqueTags){
            states.add(new State(uniqueTag));
        }

        return states;
    }
}
