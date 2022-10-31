import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File file = new File("log.txt");
        if (!file.exists()) {
            file.createNewFile();
            System.out.println("Был создан файл log.txt\n");
        } else {
            System.out.println("Файл log.txt готов к работе\n");
        }
        FileWriter writer = new FileWriter("log.txt", true);
        Date datereg = new Date();
        Date dateinfo = new Date();
        Scanner scanreg = new Scanner(System.in);
        Scanner scanchat = new Scanner(System.in);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // Поля ввода login, email и password для user
        System.out.println("<><><><><><><><><><> Регистрация <><><><><><><><><><>\n");
        System.out.print("Введите login >>> ");
        String userLogin = scanreg.nextLine();
        System.out.print("Введите Email >>> ");
        String userEmail = scanreg.nextLine();
        System.out.print("Введите телефон >>> ");
        String userPhone = scanreg.nextLine();
        System.out.print("Введите password >>> ");
        String userPassword = scanreg.nextLine();

        System.out.print("Введите login >>> ");
        String userLogin1 = scanreg.nextLine();
        System.out.print("Введите Email >>> ");
        String userEmail1 = scanreg.nextLine();
        System.out.print("Введите телефон >>> ");
        String userPhone1 = scanreg.nextLine();
        System.out.print("Введите password >>> ");
        String userPassword1 = scanreg.nextLine();

        // Создание нового объекта user1 и передача введенных данных и проверка что они вводились
        User user1 = new User(userLogin, userEmail, userPhone, userPassword);
        System.out.println("\n[Login: " + user1.getLogin() + "  Email:  " + user1.getEmail() + "  Телефон: " +
                user1.getPhoneUser() + "  Password: " + user1.getPassword() + "]  [Time: " + datereg + "]");

        User user2 = new User(userLogin1, userEmail1, userPhone1, userPassword1);
        System.out.println("\n[Login: " + user2.getLogin() + "  Email:  " + user2.getEmail() + "  Телефон: " +
                user2.getPhoneUser() + "  Password: " + user2.getPassword() + "]  [Time: " + datereg + "]");

        // Начало сеанса чата
        System.out.println("\n<><><><><><><><><><> Чат <><><><><><><><><><>\n");
        String starseans = "\n[SESSION START: " + dateinfo + "]\r\n\n";
        writer.write(starseans);

        System.out.print(user1.getLogin() + " > ");
        String messageuser = user1.getLogin() + ": " + scanchat.nextLine();
        messageuser += " [SENT " + dateinfo + "]" + "\r\n";
        writer.write(messageuser);

        System.out.print(user2.getLogin() + " > ");
        String messageuser1 = user2.getLogin() + ": " + scanchat.nextLine();
        messageuser1 += " [SENT " + dateinfo + "]" + "\r\n";
        writer.write(messageuser1);
        writer.close();
    }
}