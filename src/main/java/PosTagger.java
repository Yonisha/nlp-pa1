import java.io.IOException;

public class PosTagger {

    public static void main(String[] args) throws IOException {

        int maxNgramLength = 2;
        boolean useSmoothing = true; // need to implement

        String testFile = "C:/NLP/heb-pos-small.test";
        String taggedTestFile = "C:/NLP/heb-pos-small.tagged";
        String evaluationFile = "C:/NLP/heb-pos-small.eval";
        String goldFile = "C:/NLP/heb-pos.gold";

        // Naive tagging
        NaiveSentenceDecoder naiveSentenceDecoder = new NaiveSentenceDecoder();
        PosTaggerDecoder naiveDecoder = new PosTaggerDecoder(naiveSentenceDecoder);
        naiveDecoder.decode(testFile, taggedTestFile);

        // HMM Viterbi tagging
        PosTaggerTrainer trainer = new PosTaggerTrainer(maxNgramLength);
        TrainerResult trainerResult = trainer.train();
        SentenceDecoder sentenceDecoder = new SentenceDecoder(trainerResult);

        PosTaggerDecoder decoder = new PosTaggerDecoder(sentenceDecoder);
        decoder.decode(testFile, taggedTestFile);

        PosTaggerEvaluator evaluator = new PosTaggerEvaluator(maxNgramLength, useSmoothing);
//        evaluator.evaluate(testFile, taggedTestFile, goldFile, evaluationFile);
        // TODO should use above row that actually gets the taggedTestFile and not following row which is here only for testing the evaluate code
//        evaluator.evaluate(testFile, "C:/NLP/heb-pos-2.gold", goldFile, evaluationFile);
    }
}
