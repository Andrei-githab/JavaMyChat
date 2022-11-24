import java.io.*;
import java.net.InetAddress;
import java.util.Date;
import java.util.Scanner;
public class Main {
    // IPv4-адрес XXX.XXX.XXX.XXX
    public static String ipAddress;
    // PORT
    public static int PORT = 6500;
    public static void main(String[] args) throws IOException {
        try {
            PGP pgp = new PGP();
            Scanner scanip = new Scanner(System.in);
            // Создание нового объекта user1 и передача введенных данных и проверка что они вводились
            System.out.println("Имя хоста: " + InetAddress.getLocalHost());
            System.out.print("Сервер  локальный (yes/no)? : " );
            String yesno = scanip.nextLine();
            if (yesno.toLowerCase().equals("yes")) {
                ipAddress = "localhost";
            } else if (yesno.toLowerCase().equals("no")) {
                System.out.print("Адрес сервера: " );
                String ipAddress = scanip.nextLine();
            } else {
                System.out.println("Error");
            }

            UserPost up1 = new UserPost();
            User user1 = new User("userLogin", "nullo", 21,"+7(981)1323878", "qwerty");
            System.out.println("\n[Login: " + user1.getLogin() + "  Email:  " + user1.getEmail() + " Возраст: "
                    + user1.getAge() + "  Телефон: " + user1.getPhoneUser() + "  Password: " + user1.getPassword()
                    + "]  [Time: " + new Date().toString() + "]");
            up1.sendMessageUser(ipAddress, PORT, user1.getLogin());

        }
        catch (RuntimeException rex){
            System.out.println("An exception of type was thrown RuntimeException: " + rex);
        }
    }
}