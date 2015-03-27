import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private List<String> segments;

    public Sentence(List<String> segments){
        this.segments = new ArrayList<>();
        this.segments.add("[s]");
        this.segments.addAll(segments);
        this.segments.add("[e]");
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
