import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PosTagger {

    public static void main(String[] args) throws IOException {

        int maxNgramLength = 2;

        PosTaggerTrainer trainer = new PosTaggerTrainer(maxNgramLength);
        TrainerResult trainerResult = trainer.train();

        PosTaggerDecoder decoder = new PosTaggerDecoder(trainerResult);
        decoder.decode();

    }
}
