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





        return sentence.getSegments();
    }

    private void initialize(){

        List<NgramWithProb> tagsForStartOfSentence = getInitializationTags();
//        for (NgramWithProb ngramWithProb: tagsForStartOfSentence){
//            new State()
//        }


    }

    private List<NgramWithProb> getInitializationTags(){
        List<NgramWithProb> tags = new ArrayList<>();
        for (String line: this.stateTransitionProbabilities){
            if (line.contains("[s]")){
                String[] partsOfLine = line.split("\t");
                tags.add(new NgramWithProb(partsOfLine[1], Double.parseDouble(partsOfLine[0])));
            }
        }

        return tags;
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

        for (String line: this.stateTransitionProbabilities) {
            String[] partsOfLine = line.split("\t");
            double prob = Double.parseDouble(partsOfLine[0]);
            String[] tags = partsOfLine[1].split(" ");
            String tag1 = tags[0];
            String tag2 = tags[1];
        }

        return states;
    }
}
