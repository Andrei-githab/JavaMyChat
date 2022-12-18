package chatprogram;
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
            Scanner scanreg = new Scanner(System.in);
            Scanner scanip = new Scanner(System.in);
            Scanner yesno = new Scanner (System.in);
            String decision;
            boolean yn;
            // Создание нового объекта user1 и передача введенных данных и проверка что они вводились
            System.out.println("Имя хоста: " + InetAddress.getLocalHost());
            System.out.print("Адрес сервера: " );
            String ipAddress = scanip.nextLine();

            System.out.print("Вы зарегистрированы [yes or no]: > ");
            decision = yesno.nextLine();
            switch(decision){
                case "yes":
                    UserRegAuthDB userAuthDB = new UserRegAuthDB();
                    UserPost userPostAuth = new UserPost();
                    System.out.println("<><><><><><><><><><> Авторизация <><><><><><><><><><>\n");
                    System.out.print("Введите Email >>> ");
                    String userEmailAuth = scanreg.nextLine();
                    System.out.print("Введите password >>> ");
                    String userPasswordAuth = scanreg.nextLine();
                    userAuthDB.authorization(userEmailAuth, userPasswordAuth);
                    userPostAuth.sendMessageUser(ipAddress, PORT, ">>");
                    break;
                case "no":
                    // Создание нового объекта userRegistration для последующей регистрации
                    UserRegAuthDB userRegDB = new UserRegAuthDB();
                    UserPost userPostReg = new UserPost();
                    System.out.println("<><><><><><><><><><> Регистрация <><><><><><><><><><>\n");
                    System.out.print("Введите login >>> ");
                    String userLogin = scanreg.nextLine();
                    System.out.print("Введите Email >>> ");
                    String userEmail = scanreg.nextLine();
                    System.out.print("Введите телефон >>> ");
                    String userPhone = scanreg.nextLine();
                    System.out.print("Введите password >>> ");
                    String userPassword = scanreg.nextLine();
                    System.out.print("Введите возраст >>> ");
                    int userAge = scanreg.nextInt();
                    // Создание нового объекта userReg
                    User userReg = new User(userLogin, userEmail, userAge, userPhone, userPassword);
                    // регистрация пользователя
                    userRegDB.registration(userReg.getLogin(), userReg.getEmail(), userReg.getAge(), userReg.getPhoneUser(), userReg.getPassword());
                    // запуск чата после регистрации
                    userPostReg.sendMessageUser(ipAddress, PORT, userReg.getLogin());
                    break;
                default :
                    System.out.println("please enter again ");
            }
        }
        catch (RuntimeException rex){
            System.out.println("An exception of type was thrown RuntimeException: " + rex);
        }
    }
}