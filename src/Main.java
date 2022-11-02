import java.io.*;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InvalidLenPhonEx {
        try {
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
            String message = "";
            int len;
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            // Поля ввода login, email и password для user

            System.out.println("<><><><><><><><><><> Регистрация <><><><><><><><><><>\n");
            System.out.print("Введите login >>> ");
            String userLogin = scanreg.nextLine();
            System.out.print("Введите Email >>> ");
            String userEmail = scanreg.nextLine();
            System.out.print("Введите телефон >>> ");
            String userPhone = scanreg.nextLine();
            if (userPhone.length() < 10) {
                throw new InvalidLenPhonEx("Вы ввели не ту длину телефона, в то время как минимальная длина: 10");
            }

            System.out.print("Введите password >>> ");
            String userPassword = scanreg.nextLine();


            // Создание нового объекта user1 и передача введенных данных и проверка что они вводились
            User user1 = new User("Andru", "andru@email.com", "+7(981)4989567", "qwerty");
            // User user1 = new User(userLogin, userEmail, userPhone, userPassword);
            System.out.println("\n[Login: " + user1.getLogin() + "  Email:  " + user1.getEmail() + "  Телефон: " +
                    user1.getPhoneUser() + "  Password: " + user1.getPassword() + "]  [Time: " + datereg + "]");

            // Начало сеанса чата
            System.out.println("\n<><><><><><><><><><> Чат <><><><><><><><><><>\n");
            String starseans = "\n[SESSION START: " + dateinfo + "]\r\n\n";
            writer.write(starseans);
            writer.close();

            while (!(message.toLowerCase()).equals("logout")) {
                System.out.print(user1.getLogin() + " > ");
                message = scanchat.nextLine();
                if (!(message.toLowerCase()).equals("logout")) {
                    user1.sendMessageUser(message);
                }
            }
        }
        catch (RuntimeException rex){
            System.out.println("RuntimeException: " + rex);
        }
    }

    public static class InvalidLenPhonEx extends Exception{
        public InvalidLenPhonEx(String se){
            super(se);
            System.out.println("Вы ввели не ту длину телефона, в то время как минимальная длина: 10");
        }
    }
}