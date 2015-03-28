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

        List<String> outputLines = new ArrayList<>();
        outputLines.add("# Part-of-Speech Tagging Evaluation");
        outputLines.add("#");
        outputLines.add("# Model: " + this.maxNgramLength);
        outputLines.add("# Smoothing: " + (useSmoothing ? "y" : "n"));
        outputLines.add("# Test File: " + testFileName);
        outputLines.add("# Gold File: " + goldFileName);
        outputLines.add("");
        outputLines.add("# sent-num:\tword-accuracy\tsent-accuracy");

        BufferedReader taggedFileBufferedReader = new BufferedReader(new FileReader(testTaggedFileName));
        BufferedReader goldFileBufferedReader = new BufferedReader(new FileReader(goldFileName));

        // assuming gold and tagged have exact same amount of lines
        String goldFileLine = taggedFileBufferedReader.readLine();
        String taggedFileLine = taggedFileBufferedReader.readLine();
        while (goldFileLine != null) {
            WordWithTag goldTagging = createWordWithTagFromLine(goldFileLine);
            WordWithTag decoderTagging = createWordWithTagFromLine(taggedFileLine);


            goldFileLine = taggedFileBufferedReader.readLine();
            taggedFileLine = taggedFileBufferedReader.readLine();
        }

        FileHelper.writeLinesToFile(outputLines, evaluationFile);
    }

    private WordWithTag createWordWithTagFromLine(String line){
        String[] partsOfLine = line.split("\t");
        return new WordWithTag(partsOfLine[0], partsOfLine[1]);
    }
}
