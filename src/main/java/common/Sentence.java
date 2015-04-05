package common;

import common.NGram;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonisha on 4/4/2015.
 */
public class Sentence {

    private int order;
    private List<String> segments;
    private List<String> tags;

    public Sentence(List<String> segments) {
        this(segments, 0);
    }

    public Sentence(List<String> segments, int order) {
        this.order = order;
        this.segments = pad(segments, order);
    }

    public Sentence(List<String> segments, List<String> tags, int order){
        this.order = order;
        this.segments = pad(segments, order);
        this.tags = pad(tags, order);
    }

    public List<String> getTags() {
        return this.tags;
    }

    public List<String> getSegments(){
        return this.segments;
    }

    public List<NGram> getTagsNGrams() {
        List<NGram> nGrams = new ArrayList<>();

        List<String> ngramTags = getTags();
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
