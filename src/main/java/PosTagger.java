import java.io.IOException;

public class PosTagger {

    public static void main(String[] args) throws IOException {

        int maxNgramLength = 2;
        boolean useSmoothing = true; // need to implement

        PosTaggerTrainer trainer = new PosTaggerTrainer(maxNgramLength);
        TrainerResult trainerResult = trainer.train();

        String testFile = "C:/NLP/heb-pos-small.test";
        String taggedTestFile = "C:/NLP/heb-pos-small.tagged";
//
//        PosTaggerDecoder decoder = new PosTaggerDecoder(trainerResult);
//        decoder.decode(testFile, taggedTestFile);
//
        String evaluationFile = "C:/NLP/heb-pos-small.eval";
        String goldFile = "C:/NLP/heb-pos.gold";
//        PosTaggerEvaluator evaluator = new PosTaggerEvaluator(maxNgramLength, useSmoothing);
//        evaluator.evaluate(testFile, taggedTestFile, goldFile, evaluationFile);
    }
}
