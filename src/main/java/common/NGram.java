package common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yonisha on 4/5/2015.
 */
public class NGram {
    List<String> parts = new ArrayList<>();

    public NGram(List<String> parts) {
        this.parts = parts;
    }

    public List<String> getParts() {
        return this.parts;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NGram)) {
            return false;
        }

        NGram nGram = (NGram)obj;

        boolean result = true;
        for (int i = 0; i < this.parts.size(); i++) {
            result = result && nGram.parts.get(i).equalsIgnoreCase(this.parts.get(i));
        }

        return Arrays.equals(this.getParts().toArray(), nGram.getParts().toArray());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for( String s : this.getParts())
        {
            result = result * prime + s.hashCode();
        }

        return result;
    }

    @Override
    public String toString(){
        String text = "";
        for (String part: parts){
            text += part + " ";
        }
        return text.trim();
    }
}
