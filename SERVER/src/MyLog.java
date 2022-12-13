import java.io.*;
public class MyLog implements loger {
    private static FileWriter Logs;
    public void printStory(String strLog) throws IOException {
        Logs = new FileWriter("log.txt", true);
        Logs.write(strLog + "\n");
        Logs.flush();
    }

    public void printInfo(String strLog) throws IOException {
        Logs = new FileWriter("log.txt", true);
        Logs.write("INFO: " + strLog + "\n");
        Logs.flush();
    }
}
