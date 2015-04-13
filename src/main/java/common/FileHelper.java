package common;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
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

    public static String createFilenameWithExtension(String originalFilename, String extension) {

        return originalFilename.replaceFirst("[.][^.]+$", "." + extension);
    }

    public static List<SegmentWithTagCounts> getSegmentsWithTagCounts(String filename) throws IOException {
        List<String> inputLines = FileHelper.readLinesFromFile(filename);
        List<SegmentWithTagCounts> segmentsWithTagsCount = new ArrayList<>();

        for (String line: inputLines) {
            if (line.equals("")) {
                continue;
            }

            String[] split = line.split("\t");
            String segmentName = split[0];
            String tag = split[1];

            SegmentWithTagCounts segment = getSegment(segmentName, segmentsWithTagsCount);
            segment.increment(tag);
        }

        return segmentsWithTagsCount;
    }

    public static List<SegmentWithTagProbs> parseLexFile(String lexFilename) throws IOException {
        List<String> lexFileLines = FileHelper.readLinesFromFile(lexFilename);
        List<SegmentWithTagProbs> segmentsWithTagProbs = new ArrayList<>();

        for (String line: lexFileLines) {
            String[] parts = line.split("\t");
            String segment = parts[0];

            Dictionary<String, Double> posDictionary = new Hashtable<>();
            for (int i = 1; i < parts.length; i++) {
                String[] posWithProb = parts[i].split(" ");
                String pos = posWithProb[0];
                Double prob = Double.parseDouble(posWithProb[1]);

                posDictionary.put(pos, prob);
            }

            segmentsWithTagProbs.add(new SegmentWithTagProbs(segment, posDictionary));
        }

        return segmentsWithTagProbs;
    }

    private static SegmentWithTagCounts getSegment(String segmentName, List<SegmentWithTagCounts> segmentsWithTagCounts) {
        for (SegmentWithTagCounts segmentItem : segmentsWithTagCounts) {
            if (segmentItem.getText().equalsIgnoreCase(segmentName)) {
                return segmentItem;
            }
        }

        SegmentWithTagCounts segment = new SegmentWithTagCounts(segmentName);
        segmentsWithTagCounts.add(segment);

        return segment;
    }
}
