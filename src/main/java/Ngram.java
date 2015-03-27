import java.util.List;

public class Ngram{
    private List<String> tags;

    public Ngram(List<String> tags){
        this.tags = tags;
    }

    public String getKey(){
        String text = "";
        for (String tag: tags){
            text += tag + " ";
        }
        return text.trim();
    }

    @Override
    public String toString(){
        return getKey() + "\r\n";
    }
}
