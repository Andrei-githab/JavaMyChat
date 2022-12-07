import java.io.*;

public class MyLog {
    private static FileWriter Logs;

    public void printStory(String strStory) throws IOException {
        Logs = new FileWriter("log.txt", true);
        Logs.write(strStory);
        Logs.flush();
    }
}