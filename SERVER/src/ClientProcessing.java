import java.io.*;
import java.net.Socket;
import java.security.PublicKey;
import java.util.HashMap;

public class ClientProcessing implements Runnable {
    MyLog myLog = new MyLog();
    PGP pgp = new PGP();
    private Socket socket = null; //сокет для общения
    private ChatServer server;// экземплря сервера
    // сокет, через который сервер общается с клиентом, кроме него - клиент и сервер никак не связаны
    private Socket socketDialog;
    // поток чтения из сокета
    private BufferedReader in;
    // поток завписи в сокет
    private BufferedWriter out;
    private PublicKey publicKeyClient;
    private HashMap<Integer, PublicKey> hashMap = new HashMap<Integer, PublicKey>();
    private static int clients_count = 0;

    public ClientProcessing(Socket socket, ChatServer chatServer) throws IOException, ClassNotFoundException {
        this.socketDialog = socket;
        try {
            // создам поток для сериализации публичного ключа сервера и очищает буфер (сбрасываем его содержимое в выходной
            // поток (на клиент))
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(pgp.getPublicKey());
            objectOutputStream.flush();

            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            publicKeyClient = (PublicKey)objectInputStream.readObject();
            //System.out.println("Ключ клиента: " + publicKeyClient);

            clients_count++;
            hashMap.put(clients_count, publicKeyClient);

            this.socket = socket;
            this.server = chatServer;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //ПОТОК ДЛЯ ОТПРАВКИ СООБЩЕНИЙИ
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //ПОТОК ДЛЯ ПРИЕМА СООБЩЕНИЙ

        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
    @Override
    public void run() {
        try {
            while (true) {
                if(in.ready()) {
                    String message = in.readLine();
                    System.out.println("prishlo ot usera: " + message);
                    message = pgp.decrypt(message);
                    System.out.println("Message from user: " + message);
                    myLog.printStory(message);
                    server.sendMessageToAllClients(message);
                }
            }

        } catch (Exception ignored) { }
        finally {
            this.downService();
        }
    }

    public void sendMessage(String message)  {
        try {
            for (HashMap.Entry entry: hashMap.entrySet()) {
                //System.out.println(entry.getValue());
                message = pgp.encrypt(message, (PublicKey)entry.getValue());
                System.out.println("ushlo k useru: " + message);
                out.write(message); // ответ клиенту
                out.newLine();
                out.flush(); // убирает накопление сообщений
            }

        } catch (IOException ioException) {
            System.err.println(ioException);

        } catch (Exception exception) {
            System.err.println(exception);
        }

    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                server.deleteThread(this);
                out.close();
                in.close();
                socket.close();
                clients_count--;
            }

        } catch (IOException e) {System.err.println(e);}
    }
}

