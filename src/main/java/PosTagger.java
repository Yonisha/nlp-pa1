import java.io.IOException;

public class PosTagger {

    public static void main(String[] args) throws IOException {

        int maxNgramLength = 2;

        PosTaggerTrainer trainer = new PosTaggerTrainer(maxNgramLength);
        trainer.train();
    }
}
