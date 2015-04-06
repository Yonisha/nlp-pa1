package temp;

public class WordWithTag {
    private String word;
    private String tag;

    public WordWithTag(String word, String tag){
        this.word = word;
        this.tag = tag;
    }

    public String getWord(){
        return this.word;
    }

    public String getTag(){
        return this.tag;
    }
}
