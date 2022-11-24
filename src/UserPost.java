/// out.write(encode(pgp.getPublicKey().getEncoded()));
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс (UserPost) ;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12 , СПБГУТ
 */
public class UserPost {
    PGP pgp = new PGP();
    private String kayusp;
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

    public UserPost() throws IOException {
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

    private void pressNickname() {
        System.out.print("Press your kay: ");
        try {
            kayusp = inUser.readLine();
            out.write(kayusp + "\n");
            out.flush();
        } catch (IOException ignored) {
        }

    }
    public void sendMessageUser(String adressUser, int portUser, String nickname) throws IOException{
        try {
            this.socket = new Socket(adressUser, portUser);
            inUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.pressNickname(); // перед началом необходимо спросит имя
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
                    System.out.println(str); // пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                UserPost.this.downService();
            }
        }
    }
    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            String userWord;
            String dmess;
            PGP pgp = new PGP();
            while (true) {
                try {
                    userWord = inUser.readLine();
                    if (!(userWord.toLowerCase()).equals("chatout")) {
                        dmess = pgp.encrypt(userWord);
                        out.write(nicknameUser + ": " + dmess + " [" + new Date().toString() + "]\n");
                        //out.write(nicknameUser + ": " + userWord + " [" + new Date().toString() + "]\n");
                        out.flush();
                        } else {
                        out.write(nicknameUser + " вышел из чата\n");
                        UserPost.this.downService();
                        break;

                    }

                } catch (IOException e) {
                    UserPost.this.downService();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}