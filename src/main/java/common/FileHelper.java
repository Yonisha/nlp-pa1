package common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper{

    public static List<String> readLinesFromFile(String fileName) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        List<String> lines = new ArrayList<>();

        String line = bufferedReader.readLine();
        while (line != null) {
            lines.add(line);
            line = bufferedReader.readLine();
        }

        return lines;
    }

    public static void writeLinesToFile(List<String> lines, String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);

        for (String line: lines){
            fileWriter.append(line + "\n");
        }

        fileWriter.close();
    }
}
