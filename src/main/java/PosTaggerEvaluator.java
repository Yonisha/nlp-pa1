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


        BufferedReader taggedFileBufferedReader = new BufferedReader(new FileReader(testTaggedFileName));
        BufferedReader goldFileBufferedReader = new BufferedReader(new FileReader(goldFileName));

        // assuming gold and tagged have exact same amount of lines
        String taggedFileLine = taggedFileBufferedReader.readLine();
        String goldFileLine = goldFileBufferedReader.readLine();
        List<WordWithTag> decoderTaggingForCurrentSentence = new ArrayList<>();
        List<WordWithTag> goldTaggingForCurrentSentence = new ArrayList<>();
        List<SentenceStatistics> sentenceAccuracies = new ArrayList<>();
        int sentenceNum = 0;
        while (goldFileLine != null) {
            if (goldFileLine.equals("")){
                sentenceNum++;
                SentenceStatistics evaluationForSentence = this.sentenceEvaluator.evaluate(sentenceNum, decoderTaggingForCurrentSentence, goldTaggingForCurrentSentence);
                sentenceAccuracies.add(evaluationForSentence);
            }else{
                decoderTaggingForCurrentSentence.add(createWordWithTagFromLine(taggedFileLine));
                goldTaggingForCurrentSentence.add(createWordWithTagFromLine(goldFileLine));
            }

            taggedFileLine = taggedFileBufferedReader.readLine();
            goldFileLine = goldFileBufferedReader.readLine();
        }

        List<String> outputLines = createOutputLines(testFileName, goldFileName, sentenceAccuracies);
        FileHelper.writeLinesToFile(outputLines, evaluationFile);
    }

    private WordWithTag createWordWithTagFromLine(String line){
        String[] partsOfLine = line.split("\t");
        return new WordWithTag(partsOfLine[0], partsOfLine[1]);
    }

    private List<String> createOutputLines(String testFileName, String goldFileName, List<SentenceStatistics> sentenceStatisticses){

        List<String> outputLines = new ArrayList<>();
        outputLines.add("# Part-of-Speech Tagging Evaluation");
        outputLines.add("#");
        outputLines.add("# Model: " + this.maxNgramLength);
        outputLines.add("# Smoothing: " + (useSmoothing ? "y" : "n"));
        outputLines.add("# Test File: " + testFileName);
        outputLines.add("# Gold File: " + goldFileName);
        outputLines.add("");
        outputLines.add("# sent-num:\tword-accuracy\tsent-accuracy");

        sentenceStatisticses.forEach(ss -> outputLines.add(createOutputLinePerSentence("" + ss.getSentenceNum(), ss.getWordAccuracy(), ss.getSentenceAccuracy())));
        outputLines.add("#----------------------------------------------------------------------------------------------");

        double wordsWithCorrectTaggingForAllSentences = 0;
        double totalWordsInAllSentences = 0;
        int sentenceAccuracyAll = 0;
        for (SentenceStatistics sentenceStatistics: sentenceStatisticses){
            wordsWithCorrectTaggingForAllSentences += sentenceStatistics.getNumberOfCorrectTaggedSegments();
            totalWordsInAllSentences += sentenceStatistics.getNumberOfSegmentsInSentence();
            sentenceAccuracyAll += sentenceStatistics.getSentenceAccuracy();
        }

        double wordAccuracyAll = wordsWithCorrectTaggingForAllSentences/totalWordsInAllSentences;

        // avg row
        outputLines.add(createOutputLinePerSentence("macro-avg", wordAccuracyAll, sentenceAccuracyAll));
        return outputLines;
    }

    private String createOutputLinePerSentence(String sentenceNum, double wordAccuracy, int sentenceAccuracy){
        return sentenceNum + "\t" + wordAccuracy + "\t" + sentenceAccuracy;
    }
}
