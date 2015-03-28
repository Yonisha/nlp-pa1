import java.util.*;

public class SentenceDecoder{

    private List<String> stateTransitionProbabilities;
    private List<String> symbolEmissionProbabilities;


    public SentenceDecoder(TrainerResult trainerResult){
        this.stateTransitionProbabilities = trainerResult.getStateTransitionProbabilities();
        this.symbolEmissionProbabilities = trainerResult.getSymbolEmissionProbabilities();
    }

    public List<String> decode(InputSentence sentence){

        List<String> alphabet = sentence.getSegments();
        // move to main decoder and do once
        List<State> states = createStates();


        return sentence.getSegments();
    }

    private Set<State> initialize(Set<State> states) {
        for (String line : this.stateTransitionProbabilities) {
            if (line.contains("[s]")) {
                String[] partsOfLine = line.split("\t");
                String[] tags = partsOfLine[1].split(" ");
                String tag1 = tags[0];
                String tag2 = tags[1];
                double prob = Double.parseDouble(partsOfLine[0]);
                while (states.iterator().hasNext()){
                    State currentState = states.iterator().next();
                    if (currentState.getTag().equals(tag2)){
                        currentState.setPreviousState(new State(tag1));
                        currentState.setMaxProb(prob);
                    }
                }
            }
        }

        return states;
    }


    private List<State> createStates(){

        Set<String> uniqueTags = new HashSet<>();

        for (String line: this.stateTransitionProbabilities){
            String[] partsOfLine = line.split("\t");
            double prob = Double.parseDouble(partsOfLine[0]);
            String[] tags = partsOfLine[1].split(" ");
            uniqueTags.add(tags[0]);
            uniqueTags.add(tags[1]);
        }

        List<State> states = new ArrayList<>();

        for (String uniqueTag: uniqueTags){
            states.add(new State(uniqueTag));
        }

        return states;
    }
}
