import java.io.IOException;

public class PosTagger {

    public static void main(String[] args) throws IOException {

        int maxNgramLength = 2;
        boolean useSmoothing = true; // need to implement

        String trainFile = "C:/NLP/heb-pos.train";
        String lexFile = "C:/NLP/heb-pos.lex";
        String gramFile = "C:/NLP/heb-pos.gram";
        String testFile = "C:/NLP/heb-pos.test";
        String taggedTestFile = "C:/NLP/heb-pos.tagged";
        String evaluationFile = "C:/NLP/heb-pos.eval";
        String goldFile = "C:/NLP/heb-pos.gold";

        // Naive tagging
        NaiveSentenceDecoder naiveSentenceDecoder = new NaiveSentenceDecoder();
        PosTaggerDecoder naiveDecoder = new PosTaggerDecoder(naiveSentenceDecoder);
        naiveDecoder.decode(testFile, taggedTestFile);

        long startTime = System.currentTimeMillis();

        // HMM Viterbi tagging
        PosTaggerTrainer trainer = new PosTaggerTrainer(maxNgramLength);
        TrainerResult trainerResult = trainer.train(trainFile, lexFile, gramFile);
        SentenceDecoder sentenceDecoder = new SentenceDecoder(trainerResult);

        long trainEndTime = System.currentTimeMillis();
        System.out.println("Finished training after " + (trainEndTime - startTime) / 1000d  + " seconds");

        PosTaggerDecoder decoder = new PosTaggerDecoder(sentenceDecoder);
        decoder.decode(testFile, taggedTestFile);

        long decodeEndTime = System.currentTimeMillis();
        System.out.println("Finished decoding after " + (decodeEndTime - startTime) / 1000d  + " seconds");

        PosTaggerEvaluator evaluator = new PosTaggerEvaluator(maxNgramLength, useSmoothing);
        evaluator.evaluate(testFile, taggedTestFile, goldFile, evaluationFile);

        long evaluateEndTime = System.currentTimeMillis();
        System.out.println("Finished evaluation after " + (evaluateEndTime - startTime) / 1000d  + " seconds");
    }
}
