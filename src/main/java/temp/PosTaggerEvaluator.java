package temp;

import common.FileHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PosTaggerEvaluator{

    private int maxNgramLength;
    private boolean useSmoothing;
    private SentenceEvaluator sentenceEvaluator;

    public PosTaggerEvaluator(int maxNgramLength, boolean useSmoothing){
        this.maxNgramLength = maxNgramLength;
        this.useSmoothing = useSmoothing;
        this.sentenceEvaluator = new SentenceEvaluator();
    }

    public void evaluate(String testFileName, String testTaggedFileName, String goldFileName, String evaluationFile) throws IOException {

        List<String> taggedFileInputLines = FileHelper.readLinesFromFile(testTaggedFileName);
        List<String> goldFileInputLines = FileHelper.readLinesFromFile(goldFileName);

        if (taggedFileInputLines.size() != goldFileInputLines.size()){
            throw new IllegalArgumentException("There was a problem with the decoder. The number of lines in the gold file and in the tagged file must be the same!");
        }

        List<WordWithTag> decoderTaggingForCurrentSentence = new ArrayList<>();
        List<WordWithTag> goldTaggingForCurrentSentence = new ArrayList<>();
        List<SentenceStatistics> sentenceAccuracies = new ArrayList<>();
        int sentenceNum = 0;
        for (int i = 0; i < taggedFileInputLines.size(); i++) {
            if (goldFileInputLines.get(i).equals("")){
                sentenceNum++;
                SentenceStatistics evaluationForSentence = this.sentenceEvaluator.evaluate(sentenceNum, decoderTaggingForCurrentSentence, goldTaggingForCurrentSentence);
                sentenceAccuracies.add(evaluationForSentence);

                decoderTaggingForCurrentSentence = new ArrayList<>();
                goldTaggingForCurrentSentence = new ArrayList<>();
            }else{
                decoderTaggingForCurrentSentence.add(createWordWithTagFromLine(taggedFileInputLines.get(i)));
                goldTaggingForCurrentSentence.add(createWordWithTagFromLine(goldFileInputLines.get(i)));
            }
        }

        List<String> outputLines = createOutputLines(testFileName, goldFileName, sentenceAccuracies);
        FileHelper.writeLinesToFile(outputLines, evaluationFile);
    }

    private WordWithTag createWordWithTagFromLine(String line){
        String[] partsOfLine = line.split("\t");
        return new WordWithTag(partsOfLine[0], partsOfLine[1]);
    }

    private List<String> createOutputLines(String testFileName, String goldFileName, List<SentenceStatistics> sentencesStatistics){

        List<String> outputLines = new ArrayList<>();
        outputLines.add("# Part-of-Speech Tagging Evaluation");
        outputLines.add("#");
        outputLines.add("# Model: " + this.maxNgramLength);
        outputLines.add("# Smoothing: " + (useSmoothing ? "y" : "n"));
        outputLines.add("# Test File: " + testFileName);
        outputLines.add("# Gold File: " + goldFileName);
        outputLines.add("");
        outputLines.add("# sent-num:\tword-accuracy\tsent-accuracy");

        sentencesStatistics.forEach(ss -> outputLines.add(createOutputLinePerSentence("" + ss.getSentenceNum(), ss.getWordAccuracy(), ss.getSentenceAccuracy())));
        outputLines.add("#----------------------------------------------------------------------------------------------");

        double wordsWithCorrectTaggingForAllSentences = 0;
        double totalWordsInAllSentences = 0;
        int totalCorrectSentences = 0;
        for (SentenceStatistics sentenceStatistics: sentencesStatistics){
            wordsWithCorrectTaggingForAllSentences += sentenceStatistics.getNumberOfCorrectTaggedSegments();
            totalWordsInAllSentences += sentenceStatistics.getNumberOfSegmentsInSentence();
            totalCorrectSentences += sentenceStatistics.getSentenceAccuracy();
        }

        double wordAccuracyAll = wordsWithCorrectTaggingForAllSentences/totalWordsInAllSentences;
        double sentenceAccuracyAll = totalCorrectSentences / (double)sentencesStatistics.size();

        // avg row
        outputLines.add(createOutputLinePerSentence("macro-avg", wordAccuracyAll, sentenceAccuracyAll));
        return outputLines;
    }

    private String createOutputLinePerSentence(String sentenceNum, double wordAccuracy, double sentenceAccuracy){
        return sentenceNum + "\t" + wordAccuracy + "\t" + sentenceAccuracy;
    }
}
