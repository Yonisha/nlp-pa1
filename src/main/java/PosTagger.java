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

        // HMM Viterbi tagging
        PosTaggerTrainer trainer = new PosTaggerTrainer(maxNgramLength);
        TrainerResult trainerResult = trainer.train(trainFile, lexFile, gramFile);
        SentenceDecoder sentenceDecoder = new SentenceDecoder(trainerResult);

        PosTaggerDecoder decoder = new PosTaggerDecoder(sentenceDecoder);
        decoder.decode(testFile, taggedTestFile);

        PosTaggerEvaluator evaluator = new PosTaggerEvaluator(maxNgramLength, useSmoothing);
        evaluator.evaluate(testFile, taggedTestFile, goldFile, evaluationFile);
    }
}
