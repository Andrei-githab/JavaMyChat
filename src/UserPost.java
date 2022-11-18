
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс (UserPost) ;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12 , СПБГУТ
 */
public class UserPost {
    private String adressUser;
    private int portUser;
    private String nicknameUser;
    private Socket socket;
    private BufferedReader in;
    private BufferedReader inUser;
    private BufferedWriter out;

    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;

    public UserPost(String adress1, int port1) throws IOException {
    }

    public String getAdressUser() {
        return adressUser;
    }

    public void setAdressUser(String adressUser) {
        this.adressUser = adressUser;
    }

    public int getPortUser() {
        return portUser;
    }

    public void setPortUser(int portUser) {
        this.portUser = portUser;
    }

    public void sendMessageUser(String adressUser, int portUser, String nickname) throws IOException{
        try {
            this.socket = new Socket(adressUser, portUser);
            inUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Hello " + nickname + "\n");
            out.flush();
            nicknameUser = nickname;
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException ioex) {
            System.out.println("Socket failed");
            System.out.println("An exception of type was thrown IOException: " + ioex);
        }
    }
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str.equals("stop")) {
                        UserPost.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    }
                    System.out.println(str); // пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                UserPost.this.downService();
            }
        }
    }

    public class WriteMsg extends Thread {
        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    time = new Date(); // текущая дата
                    dt1 = new SimpleDateFormat("mm:ss"); //
                    dtime = dt1.format(time); // время
                    userWord = inUser.readLine(); // сообщения с консоли
                    if (userWord.equals("stop")) {
                        out.write("stop" + "\n");
                        UserPost.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    } else {
                        out.write(nicknameUser + ": " + userWord + " " + dtime + "\n"); // отправляем на сервер
                    }
                    out.flush(); // чистим
                } catch (IOException e) {
                    UserPost.this.downService(); // в случае исключения тоже харакири
                }
            }
        }
    }
}