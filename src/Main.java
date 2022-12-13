import java.io.*;
import java.net.*;
import java.util.*;
public class Main {
    // IPv4-адрес XXX.XXX.XXX.XXX
    public static String ipAddress;
    // PORT
    public static int PORT = 9999;
    public static void main(String[] args) throws Exception {
        try {
            Scanner scanip = new Scanner(System.in);
            // Создание нового объекта user1 и передача введенных данных и проверка что они вводились
            System.out.println("Имя хоста: " + InetAddress.getLocalHost());
            System.out.print("Адрес сервера: " );
            String ipAddress = scanip.nextLine();

            UserPost up1 = new UserPost();
            User user1 = new User("AndruVladiv", "andruvladimir0v@gmail.com", 21,"+7(981)1323878", "qwerty");
            up1.sendMessageUser(ipAddress, PORT, user1.getLogin());

        }
        catch (RuntimeException rex){
            System.out.println("An exception of type was thrown RuntimeException: " + rex);
        }
    }
}