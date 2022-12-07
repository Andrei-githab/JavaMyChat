import javax.crypto.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

class ServerSomthing extends Thread implements Server{
    MyLog myLog = new MyLog();
    PGP pgp = new PGP();
    // сокет, через который сервер общается с клиентом, кроме него - клиент и сервер никак не связаны
    private Socket socket;
    // поток чтения из сокета
    private BufferedReader in;
    // поток завписи в сокет
    private BufferedWriter out;
    private Cipher ServerDecryptCipher;
    private PublicKey publicKey;

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // создам поток для сериализации публичного ключа сервера и очищает буфер (сбрасываем его содержимое в выходной
        // поток (на клиент))
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(pgp.getPublicKey());
        objectOutputStream.flush();

        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word, sword;
        try {
            // чтение сериализованных данных из потока
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            // считываем из потока объект (Получаем ключ от клиента)
            PublicKey pubKayClient = (PublicKey)objectInputStream.readObject();
            //System.out.println("pubKayClient: " + pubKayClient); // для отладки

            while (true) {
                // получаем зашифрованное сообщение от клиента
                word = in.readLine();
                //System.out.println("polucheno ot klienta (encrypt): " + word); // для отладки
                // расшифровываем сообщение от клиента (privateKey server'a)
                word = pgp.decrypt(word);
                // шифруем сообщение от клиента (PublicKey от клиента)
                sword = pgp.encrypt(word, pubKayClient);
                if(word.equals("stop")) {
                    this.downService(); // харакири
                    break; // если пришла пустая строка - выходим из цикла прослушки
                }
                System.out.println("polucheno ot klienta (decrypt): " + word);
                //System.out.println("otpravleno klientu (encrypt): " + sword); // для отладки
                for (ServerSomthing vr : ChatServer.serverList) {
                    myLog.printStory(word);
                    vr.send(sword); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                }
            }
        } catch (NullPointerException ignored) { }

        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }
    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : ChatServer.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    ChatServer.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}

public class ChatServer {
    public static final int PORT = 9999; // порт который мы прослушаваем
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("========================       Server Started      ========================");
        System.out.println("======================== Server information ===============================" +
                           "\nserver address " + InetAddress.getLocalHost() +
                           "\nport " + server.getLocalPort() +
                           "\n===========================================================================");
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket)); // добавить новое соединенние в список
                    System.out.println("<>>> User connected from " + socket.getInetAddress().getHostAddress() + " <<<>");
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }
}