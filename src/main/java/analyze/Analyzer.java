package analyze;

import common.FileHelper;
import common.NGram;
import common.Sentence;
import temp.SegmentWithTagCounts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Analyzer {
    public static void main(String[] args) throws IOException {
        String trainFile = "C:/NLP/heb-pos.train";
        String goldFile = "C:/NLP/heb-pos.gold";

        System.out.println("Train file analysis:");
        analyzeData(trainFile);

        System.out.println("Gold file analysis:");
        analyzeData(goldFile);
    }

    private static void analyzeData(String fileName) throws IOException {
        List<String> lines = FileHelper.readLinesFromFile(fileName);

        // Segment unigrams
        List<Sentence> sentences = getSentences(lines, 0);
        List<String> segmentUnigrams = sentences.stream().map(s -> s.getSegments()).flatMap(m -> m.stream()).collect(Collectors.toList());
        int segmentUnigramTokens = segmentUnigrams.size();
        List<String> segmentUnigramTypes = segmentUnigrams.stream().distinct().collect(Collectors.toList());
        int segmentUnigramTypesCount = segmentUnigramTypes.size();

        System.out.println("# of segment-unigram tokens: " + segmentUnigramTokens);
        System.out.println("# of segment-unigram types: " + segmentUnigramTypesCount);

        // Tags unigrams
        sentences = getSentences(lines, 0);
        List<String> tagUnigrams = sentences.stream().map(s -> s.getTags()).flatMap(m -> m.stream()).collect(Collectors.toList());
        int tagUnigramTokens = tagUnigrams.size();
        int tagUnigramTypes = tagUnigrams.stream().distinct().collect(Collectors.toList()).size();

        System.out.println("# of tag-unigram tokens: " + tagUnigramTokens);
        System.out.println("# of tag-unigram types: " + tagUnigramTypes);

        // TODO: check if correct!!!
        // Tags bigrams
        sentences = getSentences(lines, 1);
        List<NGram> bigrams = sentences.stream().map(s -> s.getTagsNGrams()).flatMap(m -> m.stream()).collect(Collectors.toList());
        int tagBigramTokens = bigrams.size();
        int tagBigramTypes = bigrams.stream().distinct().collect(Collectors.toList()).size();

        System.out.println("# of tag-bigram tokens: " + tagBigramTokens);
        System.out.println("# of tag-bigram types: " + tagBigramTypes);

        // Calculating lexical ambiguity
        List<SegmentWithTagCounts> segmentsWithTagCounts = FileHelper.getSegmentsWithTagCounts(fileName);

        // TODO: correct??
        int totalDistinctPosTagsForAllSegmentTypes = segmentsWithTagCounts.stream().mapToInt(s -> s.getTagsCounts().size()).sum();
        double lexicalAmbiguity = totalDistinctPosTagsForAllSegmentTypes / (double)segmentsWithTagCounts.size();

        System.out.println("Lexical ambiguity: " + lexicalAmbiguity);
        System.out.println();
    }

    private static List<Sentence> getSentences(List<String> lines, int order) {
        List<Sentence> sentences = new ArrayList<>();
        List<String> currentSentenceSegments = new ArrayList<>();
        List<String> currentSentenceTags = new ArrayList<>();

        for (String line: lines) {
            if (line.equals("")) {
                sentences.add(new Sentence(currentSentenceSegments, currentSentenceTags, order));

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
