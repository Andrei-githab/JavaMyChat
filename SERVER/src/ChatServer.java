import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatServer {
    MyLog log = new MyLog();
    private ArrayList<ClientProcessing> serverList = new ArrayList<ClientProcessing>();
    private ServerSocket serverSocket;
    public ChatServer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    public void startServer() throws UnknownHostException, ClassNotFoundException {
        System.out.println("==========> SERVER STARTED ==========>");
        System.out.println("SERVER ADDRESS " + InetAddress.getLocalHost());

        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ClientProcessing serverThread = new ClientProcessing(socket, this);
                serverList.add(serverThread); // добавить новое соединенние в список
                System.out.println("Новый пользователь > " + serverThread);
                log.printInfo("Новый пользователь > " + serverThread);
                new Thread(serverThread).start();
            }
        } catch (IOException ioException) {
            System.out.println(ioException);
        }
    }
    public static final int PORT = 9999; // порт который мы прослушаваем
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        ChatServer chatServer = new ChatServer(serverSocket);
        chatServer.startServer();
    }

    // отправка сообщения по всем потокам
    public void sendMessageToAllClients(String message) {
        for (ClientProcessing client : serverList) {
            client.sendMessage(message);
        }
    }

    //удаление потока
    public void deleteThread(ClientProcessing serverThread){
        serverList.remove(serverThread);
    }

}