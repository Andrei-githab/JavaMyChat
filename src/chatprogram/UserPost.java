package chatprogram;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Класс (UserPost) ;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12 , СПБГУТ
 */
public class UserPost implements IUserPost{
    PGP pgp = new PGP();
    private String adressUser;
    private int portUser;
    private String nicknameUser;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader inUser;
    private PublicKey publicKeyServer;
    private Date date_long;
    private SimpleDateFormat date;

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
     *
     * @param adressUser адрес сервера
     * @param portUser   порт, такой же как у сервера
     * @param nickname   имя пользователя
     */
    public void sendMessageUser(String adressUser, int portUser, String nickname) throws IOException {
        try {
            // запрашиваем у сервера доступ на соединение
            this.socket = new Socket(adressUser, portUser);

            // потоки чтения из сокета / записи в сокет, и чтения с консоли
            inUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // создам поток для сериализации публичного ключа
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            // записывает в поток публичный ключ (PublicKey)
            objectOutputStream.writeObject(pgp.getPublicKey());
            // очищает буфер и сбрасываем его содержимое в выходной поток (на сервер)
            objectOutputStream.flush();

            // считываем из потока объект (получаем ключ от сервера)
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            publicKeyServer = (PublicKey) objectInputStream.readObject();
            // System.out.println("Ключ сервера: " + spub);
            // получаем (nickname) и присваиваем имя пользователя (nicknameUser)
            nicknameUser = nickname;
            // нить читающая сообщения из сокета в бесконечном цикле
            new ReadMsg().start();
            // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
            new WriteMsg().start();

        } catch (IOException ioex) {
            // Обрывается соединение при возникновении ошибки сокета
            System.out.println("Socket failed");
            System.out.println("An exception of type IOException was thrown by the sendMessageUser method: " + ioex);

        } catch (Exception e) {
            System.out.println("An exception of type Exception was thrown by the sendMessageUser method: " + e);
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
        } catch (IOException ignored) {
        }
    }

    /**
     * Метод чтения сообщений с сервера
     */
    private class ReadMsg extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    // ждем сообщения с сервера
                    message = in.readLine();
                    // дишифруем сообщение от сервера
                    message = pgp.decrypt(message);
                    if (message.equals("chat_stop")) {
                        UserPost.this.downService();
                        break; // выходим из цикла если пришло "chat_stop"
                    }
                    // пишем сообщение с сервера на консоль
                    System.out.println(message);
                }
            } catch (IOException ioException) {
                System.out.println("An exception of type IOException was thrown by the ReadMsg method: " + ioException);
                UserPost.this.downService();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Метод отправки сообщений приходящих с консоли на сервер
     */
    public class WriteMsg extends Thread {
        @Override
        public void run() {
            while (true) {
                String messUser, sMessUser;
                try {
                    // ждем сообщения пользователя с консоли
                    messUser = inUser.readLine();
                    if (messUser.equalsIgnoreCase("chat_stop")) {
                        // выходим из цикла если пришло "chat_stop" т.е. выходим из чата
                        UserPost.this.downService();
                        break;
                    } else {
                        date_long = new Date(); // текущая дата
                        date = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                        // формируем строку дата + nickname + сообщение
                        sMessUser =  nicknameUser + "|" + date.format(date_long) + "|" + messUser;
                        // шифруем строку
                        sMessUser = pgp.encrypt(sMessUser, publicKeyServer);
                        // отправляем на сервер
                        out.write(sMessUser + "\n");
                        out.flush();
                    }

                } catch (IOException ioException) {
                    System.out.println("An exception of type IOException was thrown by the WriteMsg method: " + ioException);
                    UserPost.this.downService();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}