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

    // ip адрес клиента
    private String adressUser;

    // порт соединения
    private int portUser;

    // nickname user'a (имя клиента)
    private String nicknameUser;

    //сокет для общения
    private Socket socket;

    // поток чтения из сокета
    private BufferedReader in;

    // поток записи в сокет
    private BufferedWriter out;

    // поток чтения с консоли
    private BufferedReader inUser;

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

    /**
     * Метод обмена сообщениями
     * @param adressUser адрес сервера
     * @param portUser порт, такой же как у сервера
     * @param nickname имя пользователя
     */
    public void sendMessageUser(String adressUser, int portUser, String nickname) throws IOException{
        try {
            // запрашиваем у сервера доступ на соединение
            this.socket = new Socket(adressUser, portUser);

            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // создам поток для сериализации публичного ключа
            // (Сериализация — это процесс записи объекта в выходной поток)
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // записывает в поток публичный ключ (PublicKey)
            objectOutputStream.writeObject(pgp.getPublicKey());
            // очищает буфер и сбрасываем его содержимое в выходной поток (на сервер)
            objectOutputStream.flush();

            // получаем (nickname) и присваиваем имя пользователя (nicknameUser)
            nicknameUser = nickname;

            // нить читающая сообщения из сокета в бесконечном цикле
            new ReadMsg().start();
            // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
            new WriteMsg().start();

        } catch (IOException ioex) {
            // Обрывается соединение при возникновении ошибки сокета
            System.out.println("Socket failed");
            System.out.println("An exception of type was thrown IOException: " + ioex);
        }
    }

    /**
     * Метод закрытие сокета и потоков чтения из сокета / записи в сокет
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {}
    }

    /**
     * Метод чтения сообщений с сервера
     */
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String str;
            try {
                while (true) {
                    // ждем сообщения с сервера
                    str = in.readLine();
                    // пишем сообщение с сервера на консоль
                    System.out.println(str);
                }
            } catch (IOException e) {
                UserPost.this.downService();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    private static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Метод отправки сообщений приходящих с консоли на сервер
     */
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            String userWord;
            String dmess;
            while (true) {
                try {
                    // сообщения с консоли
                    userWord = inUser.readLine();

                    // выходим из цикла если пришло "chatout"
                    if (!(userWord.toLowerCase()).equals("chatout")) {
                        // шифруем сообщение
                        userWord = nicknameUser + ": " + userWord + " [" + new Date().toString() + "]\n";
                        dmess = pgp.encrypt(userWord);
                        // отправляем на сервер
                        out.write(dmess + "\n");
                        // чистим
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