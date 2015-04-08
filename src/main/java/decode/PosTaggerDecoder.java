package decode;

import common.Sentence;
import common.FileHelper;
import common.SentenceDecodingResult;
import decode.ISentenceDecoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PosTaggerDecoder {

    private List<String> outputLines = new ArrayList<String>();
    private ISentenceDecoder sentenceDecoder;

    public PosTaggerDecoder(ISentenceDecoder sentenceDecoder){
        this.sentenceDecoder = sentenceDecoder;
    }

    public void decode(String inputFileName, String outputFileName) throws IOException {
        List<String> inputLines = FileHelper.readLinesFromFile(inputFileName);
        List<Sentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();

        int sentenceId = 1;
        for (String line: inputLines) {
            if (line.equals("")){
                sentences.add(new Sentence(sentenceId++, currentSentenceSegments));
                currentSentenceSegments = new ArrayList<>();
            }else{
                currentSentenceSegments.add(line);
            }
        }

        List<SentenceDecodingResult> decodingResults = sentences.parallelStream().map(s -> decodeSentence(s)).collect(Collectors.toList());
        decodingResults.sort(Comparator.comparingInt(value -> value.getId()));

        decodingResults.forEach(res -> outputLines.addAll(res.getTags()));

        FileHelper.writeLinesToFile(outputLines, outputFileName);
    }

    private SentenceDecodingResult decodeSentence(Sentence sentence) {
        List<String> sentenceTags = new ArrayList<>();
        List<String> tags = sentenceDecoder.decode(sentence);
        for (int i = 0; i <tags.size(); i++) {
            sentenceTags.add(sentence.getSegments().get(i) + "\t" + tags.get(i));
        }

        // TODO: remove?
        sentenceTags.add("");

        return new SentenceDecodingResult(sentence.getId(), sentenceTags);
    }
}
