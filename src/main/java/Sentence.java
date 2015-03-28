import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private List<String> segments;

    public Sentence(List<String> segments, int maxNgramLength){
        this.segments = new ArrayList<>();
        for (int i = 0; i <maxNgramLength-1; i++) {
            this.segments.add("[s]");
        }
        this.segments.addAll(segments);
        for (int i = 0; i <maxNgramLength-1; i++) {
            this.segments.add("[e]");
        }
    }

    public List<String> getSegments(){
        return this.segments;
    }

    @Override
    public String toString(){
        String text = "";
        for (String tag: segments) {
            text += tag + " ";
        }
        return text;
    }
}
