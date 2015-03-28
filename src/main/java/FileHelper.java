import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileHelper{
    public static void writeLinesToFile(List<String> lines, String fileName) throws IOException {
        File file = new File(fileName);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);

        for (String line: lines){
            fileWriter.append(line + "\r\n");
        }

        fileWriter.close();
    }
}
