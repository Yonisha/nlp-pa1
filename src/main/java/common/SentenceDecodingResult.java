package common;

import java.util.ArrayList;
import java.util.List;

public class SentenceDecodingResult {
    private int id;
    private List<String> tags = new ArrayList<>();

    public SentenceDecodingResult(int id, List<String> tags) {
        this.id = id;
        this.tags = tags;
    }

    public int getId() {
        return this.id;
    }

    public List<String> getTags() {
        return this.tags;
    }
}
