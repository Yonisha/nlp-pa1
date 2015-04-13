package decode;

import common.Sentence;
import common.FileHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The POS tagger decoder. Its purpose is to simply invoke the SentenceDecoder for each sentence.
 * This tagger decodes all sentences in parallel, as each sentence decoding is self-contained.
 */
public class PosTaggerDecoder {

    private List<String> outputLines = new ArrayList<String>();
    private ISentenceDecoder sentenceDecoder;

    public PosTaggerDecoder(ISentenceDecoder sentenceDecoder){
        this.sentenceDecoder = sentenceDecoder;
    }

    /**
     * The core function of the tagger.
     * It decodes all sentences in parallel and writes the results to the output file.
     */
    public void decode(String inputFileName, String outputFileName) throws IOException {
        List<Sentence> sentences = getSentences(inputFileName);

        // Decoding all sentences in parallel. Since the decoding results order is important and should preserve the original order of the sentences,
        // each sentence is assigned with an ID and then the results are ordered using this ID.
        List<SentenceDecodingResult> decodingResults = sentences.parallelStream().map(s -> decodeSentence(s)).collect(Collectors.toList());
        decodingResults.sort(Comparator.comparingInt(value -> value.getId()));

        // Writing sentences to output file.
        decodingResults.forEach(res -> outputLines.addAll(res.getTags()));
        FileHelper.writeLinesToFile(outputLines, outputFileName);
    }

    // TOOD: duplicate code.
    private List<Sentence> getSentences(String inputFileName) throws IOException {
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

        return sentences;
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
