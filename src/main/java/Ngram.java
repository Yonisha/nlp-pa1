import java.util.List;

public class Ngram{
    private List<String> tags;

    public Ngram(List<String> tags){
        this.tags = tags;
    }

    @Override
    public String toString(){
        String text = "";
        for (String tag: tags){
            text += tag + " ";
        }
        return text.trim();
    }
}
