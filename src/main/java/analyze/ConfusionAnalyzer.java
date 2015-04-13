package analyze;

import common.FileHelper;
import java.io.IOException;
import java.util.*;

public class ConfusionAnalyzer {
    public static void main(String[] args) throws IOException {
        List<String> taggedFileInputLines = FileHelper.readLinesFromFile(args[0]);
        List<String> goldFileInputLines = FileHelper.readLinesFromFile(args[1]);

        if (taggedFileInputLines.size() != goldFileInputLines.size()){
            throw new IllegalArgumentException("There was a problem with the decoder. The number of lines in the gold file and in the tagged file must be the same!");
        }

        Set<String> tagSet = new HashSet<>();
        for (int i = 0; i < goldFileInputLines.size(); i++) {
            if (goldFileInputLines.get(i).equals("")) {
                continue;
            }

            tagSet.add(getTagFromLine(goldFileInputLines.get(i)));
        }

        for (int i = 0; i < taggedFileInputLines.size(); i++) {
            if (taggedFileInputLines.get(i).equals("")) {
                continue;
            }

            tagSet.add(getTagFromLine(taggedFileInputLines.get(i)));
        }

        List<String> tags = new ArrayList<>(tagSet);
        int[][] confusionMatrix = new int[tags.size()][tags.size()];

        for (int i = 0; i < taggedFileInputLines.size(); i++) {
            if (goldFileInputLines.get(i).equals("")){
                continue;
            } else {
                String goldTag = getTagFromLine(goldFileInputLines.get(i));
                String taggerTag = getTagFromLine(taggedFileInputLines.get(i));

                int goldIndex = tags.indexOf(goldTag);
                int taggerIndex = tags.indexOf(taggerTag);

                confusionMatrix[taggerIndex][goldIndex]++;
            }
        }

        printMatrix(confusionMatrix, tags);
    }

    private static String getTagFromLine(String line){
        String[] partsOfLine = line.split("\t");
        return partsOfLine[1];
    }

    private static void printMatrix(int[][] confusionMatrix, List<String> tags) throws IOException {
        System.out.print("Tagger\\Gold\t");
        for (int i = 0; i < confusionMatrix.length; i++) {
            System.out.print(tags.get(i) + "\t");
        }

        System.out.println();
        for (int i = 0; i < confusionMatrix.length; i++) {
            System.out.print(tags.get(i) + "\t");
            for (int j = 0; j < confusionMatrix.length; j++) {
                System.out.print(confusionMatrix[i][j] + "\t");
            }

            System.out.println();
        }
    }
}
