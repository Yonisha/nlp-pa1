package common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonisha on 4/4/2015.
 */
public class TrainerSentence {

    private List<String> segments;
    private List<String> tags;

    public TrainerSentence(List<String> segments, List<String> tags){
        this.segments = segments;
        this.tags = tags;
    }

    public List<String> getTags(int order) {
        return pad(this.tags, order);
    }

    public List<String> getSegments(int order){
        return pad(this.segments, order);
    }

    public List<NGram> getTagsNGrams(int order) {
        List<NGram> nGrams = new ArrayList<>();

        List<String> ngramTags = getTags(order);
        List<String> currentNGramItems = new ArrayList<>();

        for (int i = 0; i < ngramTags.size() - order; i++) {
            for (int j = i; j <= i + order; j++) {
                currentNGramItems.add(ngramTags.get(j));
            }

            nGrams.add(new NGram(currentNGramItems));
            currentNGramItems = new ArrayList<>();
        }

        return nGrams;
    }

    @Override
    public String toString(){
        String text = "";
        for (String tag: segments) {
            text += tag + " ";
        }
        return text;
    }

    private List<String> pad(List<String> items, int order) {
        List<String> padded = new ArrayList<>();

        for (int i = 0; i < order; i++) {
            padded.add("[s]");
        }

        padded.addAll(items);

        for (int i = 0; i < order; i++) {
            padded.add("[e]");
        }

        return padded;
    }
}
