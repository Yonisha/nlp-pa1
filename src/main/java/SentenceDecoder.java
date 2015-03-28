import java.util.List;

public class SentenceDecoder{

    private List<String> stateTransitionProbabilities;
    private List<String> symbolEmissionProbabilities;
    private List<Tag> tags;

    public SentenceDecoder(TrainerResult trainerResult){
        this.stateTransitionProbabilities = trainerResult.getStateTransitionProbabilities();
        this.symbolEmissionProbabilities = trainerResult.getSymbolEmissionProbabilities();
    }

    public List<String> decode(InputSentence sentence){

        List<String> alphabet = sentence.getSegments();
        List<Tag> states = tags;

        int numberOfSegments = sentence.getSegments().size();
        int numberOfTags = tags.size();

//        State[][] matrix = new State[numberOfSegments][numberOfTags];

        return sentence.getSegments();
    }

    private void initialize(){

    }
}
