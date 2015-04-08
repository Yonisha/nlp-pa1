package decode;

import common.Sentence;

import java.util.List;

public interface ISentenceDecoder{
    List<String> decode(Sentence sentence);
}
