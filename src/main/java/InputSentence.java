import java.util.List;

public class InputSentence {
    private List<String> segments;

    public InputSentence(List<String> segments){
        this.segments = segments;
    }

    public List<String> getSegments(){
        return segments;
    }

    @Override
    public String toString(){
        String text = "";
        for (String segment: this.segments){
            text += segment + " ";
        }
        return text.trim();
    }
}
