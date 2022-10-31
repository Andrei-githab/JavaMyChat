/**
 * Класс пользователя (user) со свойствами login, email и password;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12
 */
public class User {
    private UserPhone phoneNumber;
    private String login;
    private String email;
    private String password;

    /**
     * Конструктор класса создает нового пользователя по параметрам:
     * @param loginUser - login (никнейм) пользователя
     * @param emailUser - email пользователя
     * @param phoneUser - номер телефона пользователя
     * @param passwordUser - пароль пользователя
     */
    public User(String loginUser, String emailUser, String phoneUser, String passwordUser){
        this.login = loginUser;
        this.email = emailUser;
        this.phoneNumber = new UserPhone(phoneUser);
        this.password = passwordUser;
    }

    public String getLogin(){
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public String getPhoneUser() {
        return phoneNumber.getNumberPhone();
    }

    public void setPhoneUser(String phoneUser) {
        phoneNumber.setNumberPhone(phoneUser);
    }
}