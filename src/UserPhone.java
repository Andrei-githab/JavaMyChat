/**
 * Класс номера телефона (user) со свойствами phonenamber;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12
 */
public class UserPhone {
    private String numberphone;
    /**
     * Конструктор класса создает и приводит к стандартному виду номер телефона пользователя
     * @param numberphone - не обработанный номер телефона пользователя
     */
    public UserPhone(String numberphone){
        setNumberPhone(numberphone);
    }

    public String getNumberPhone(){
        return numberphone;
    }

    public void setNumberPhone(String numberphone){
        normalizationUserPhone(numberphone);
    }

    protected void normalizationUserPhone(String numberphone){
        String nphone;
        nphone = numberphone;
        nphone = nphone.replaceAll("(\\D)*", "");
        int len = nphone.length();
        if (len >= 10){
            nphone = nphone.substring(len - 10);
            nphone = "+7" + nphone;
            this.numberphone = nphone;
        }
        else {
            System.out.println("errr");
        }
    }
}
