package temp;

import decode.NaiveSentenceDecoder;
import decode.PosTaggerDecoder;
import decode.SentenceDecoder;
import evaluate.PosTaggerEvaluator;
import train.NaiveTrainer;
import train.NaiveTrainerResult;
import train.PosTaggerTrainer;
import train.TrainerResult;
import java.io.IOException;

public class PosTagger {

    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.out.println("Must choose an operation: Train, Decode or Evaluate.");
            System.exit(1);
        }

        String operation = args[0];

        if (operation.equalsIgnoreCase(Operation.Train.toString())) {
            train(args);
        } else if (operation.equalsIgnoreCase(Operation.Decode.toString())){
            decode(args);
        } else if (operation.equalsIgnoreCase(Operation.Evaluate.toString())) {
            evaluate(args);
        } else {
            System.out.println("Unknown operation provided. Must use Train, Decode or Evaluate.");
            System.exit(1);
        }
    }

    private static void train(String[] args) throws IOException {

        if (args.length < 4) {
            System.out.println("Not enough parameters. Usage: ./train <model> <heb-pos.train> <smoothing (y/n)>");

            return;
        }

        int model = Integer.parseInt(args[1]);
        String trainFile = args[2];

        // TODO: if model < 2 then do not extract
        boolean smoothingEnabled = args[3].equalsIgnoreCase("y");

        if (model < 2) {
            invokeNaiveTrainer(trainFile);

            return;
        }

        // TODO: derive from train filename.
        String lexFile = "C:/NLP/heb-pos.lex";
        String gramFile = "C:/NLP/heb-pos.gram";

        PosTaggerTrainer trainer = new PosTaggerTrainer(model, smoothingEnabled);

        long startTime = System.currentTimeMillis();
        trainer.train(trainFile, lexFile, gramFile);
        long trainEndTime = System.currentTimeMillis();

        System.out.println("Finished training after " + (trainEndTime - startTime) / 1000d  + " seconds");
    }

    private static void invokeNaiveTrainer(String trainFile) throws IOException {
        NaiveTrainer naiveTrainer = new NaiveTrainer();
        naiveTrainer.train(trainFile);
    }

    // TODO: what to do with the optional param file???????
    private static void decode(String[] args) throws IOException {

        if (args.length < 5) {
            System.out.println("Not enough parameters. Usage: ./decode <model> <heb-pos.test> <param-file1> [<param-file2>]");

            return;
        }

        // TODO: extract arg4 only when on HMM
        int model = Integer.parseInt(args[1]);
        String testFile = args[2];
        String paramFile1 = args[3];
        String paramFile2 = args[4];

        if (model < 2) {
            invokeNaiveDecoder(testFile, paramFile1);

            return;
        }

        // TODO: derive from test file name.
        String taggedTestFile = "C:/NLP/heb-pos.tagged";

        TrainerResult trainerResult = TrainerResult.buildTrainResult(paramFile1, paramFile2);
        SentenceDecoder sentenceDecoder = new SentenceDecoder(trainerResult);
        PosTaggerDecoder decoder = new PosTaggerDecoder(sentenceDecoder);

        long startTime = System.currentTimeMillis();
        decoder.decode(testFile, taggedTestFile);
        long decodeEndTime = System.currentTimeMillis();

        System.out.println("Finished decoding after " + (decodeEndTime - startTime) / 1000d + " seconds");
    }

    private static void invokeNaiveDecoder(String testFilename, String lexFilename) throws IOException {
        NaiveTrainerResult naiveTrainResult = NaiveTrainerResult.buildTrainResult(lexFilename);
        NaiveSentenceDecoder naiveSentenceDecoder = new NaiveSentenceDecoder(naiveTrainResult);
        PosTaggerDecoder naiveDecoder = new PosTaggerDecoder(naiveSentenceDecoder);
        naiveDecoder.decode(testFilename, "c:/nlp/heb-pos.tagged");
    }

    private static void evaluate(String[] args) throws IOException {

        if (args.length < 5) {
            System.out.println("Not enough parameters. Usage: ./evaluate <*.tagged> <heb-pos.gold> <model> <smoothing (y/n)>");

            return;
        }

        String taggedFile = args[1];
        String goldFile = args[2];
        int model = Integer.parseInt(args[3]);
        boolean smoothingEnabled = args[4].equalsIgnoreCase("y");

        // TODO: derive
        String testFile = "C:/NLP/heb-pos.test";
        String evaluationFile = "C:/NLP/heb-pos.eval";

        PosTaggerEvaluator evaluator = new PosTaggerEvaluator(model, smoothingEnabled);

        long startTime = System.currentTimeMillis();
        evaluator.evaluate(testFile, taggedFile, goldFile, evaluationFile);
        long evaluateEndTime = System.currentTimeMillis();

        System.out.println("Finished evaluation after " + (evaluateEndTime - startTime) / 1000d  + " seconds");
    }

    private enum Operation {
        Train,
        Decode,
        Evaluate
    }
}
