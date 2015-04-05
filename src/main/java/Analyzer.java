import common.NGram;
import common.TrainerSentence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by yonisha on 4/4/2015.
 */
public class Analyzer {
    public static void main(String[] args) throws IOException {
        String trainFile = "C:/NLP/heb-pos.train";
        String goldFile = "C:/NLP/heb-pos.gold";

        System.out.println("Train file analysis:");
        List<String> trainLines = FileHelper.readLinesFromFile(trainFile);
        List<TrainerSentence> trainSentences = getSentences(trainLines);
        analyzeData(trainSentences);


        System.out.println("Gold file analysis:");
        List<String> goldLines = FileHelper.readLinesFromFile(goldFile);
        List<TrainerSentence> goldSentences = getSentences(goldLines);
        analyzeData(goldSentences);
    }

    private static void analyzeData(List<TrainerSentence> sentences) {

        // Segment unigrams
        List<String> segmentUnigrams = sentences.stream().map(s -> s.getSegments(0)).flatMap(m -> m.stream()).collect(Collectors.toList());
        int segmentUnigramTokens = segmentUnigrams.size();
        int segmentUnigramTypes = segmentUnigrams.stream().distinct().collect(Collectors.toList()).size();
        System.out.println("# of segment-unigram tokens: " + segmentUnigramTokens);
        System.out.println("# of segment-unigram types: " + segmentUnigramTypes);

        // Tags unigrams
        List<String> tagUnigrams = sentences.stream().map(s -> s.getTags(0)).flatMap(m -> m.stream()).collect(Collectors.toList());
        int tagUnigramTokens = tagUnigrams.size();
        int tagUnigramTypes = tagUnigrams.stream().distinct().collect(Collectors.toList()).size();
        System.out.println("# of tag-unigram tokens: " + tagUnigramTokens);
        System.out.println("# of tag-unigram types: " + tagUnigramTypes);

        // TODO: check if correct!!!
        // Tags bigrams
        List<NGram> bigrams = sentences.stream().map(s -> s.getTagsNGrams(1)).flatMap(m -> m.stream()).collect(Collectors.toList());
        int tagBigramTokens = bigrams.size();
        int tagBigramTypes = bigrams.stream().distinct().collect(Collectors.toList()).size();

        System.out.println("# of tag-bigram tokens: " + tagBigramTokens);
        System.out.println("# of tag-bigram types: " + tagBigramTypes);
    }

    private static List<TrainerSentence> getSentences(List<String> lines) {
        List<TrainerSentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();
        List<String> currentSentenceTags = new ArrayList<>();

        for (String line: lines) {
            if (line.equals("")) {
                sentences.add(new TrainerSentence(currentSentenceSegments, currentSentenceTags));

                currentSentenceSegments = new ArrayList<>();
                currentSentenceTags = new ArrayList<>();

                continue;
            }

            String[] parts = line.split("\t");
            currentSentenceSegments.add(parts[0]);
            currentSentenceTags.add(parts[1]);
        }

        return sentences;
    }
}
