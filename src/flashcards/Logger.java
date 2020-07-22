package flashcards;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static List<String> logList = new ArrayList<>();

    public static void log(String lineToLog) {
        logList.add(lineToLog);
    }

    public static void saveLogToFile(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        for (String line : logList) {
            writer.write(line);
        }
        writer.close();
    }
}
