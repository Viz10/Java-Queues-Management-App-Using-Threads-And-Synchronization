package MODEL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogFileWriter {

    private static LogFileWriter instance = null;
    private static BufferedWriter fileWriter=null;

    private LogFileWriter() throws IOException {
        fileWriter = new BufferedWriter(new FileWriter("Log.txt", true));
    }

    public static LogFileWriter getInstance() throws IOException {
            if (instance == null) {
                instance = new LogFileWriter();
            }
            return instance;
    }
    public void writeLog(String log) throws IOException {
        fileWriter.write(log);
        fileWriter.newLine();
        fileWriter.flush();
    }
    public static void clear() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
