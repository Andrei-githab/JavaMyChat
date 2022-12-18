package chatprogram;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPhoneTest {
    @Test
    void setNumberPhone() {
        String numberphone = "+7 (981) 132-38-78";
        System.out.println(numberphone);
        UserPhone userPhone = new UserPhone(numberphone);
        String test = userPhone.getNumberPhone();
        assertEquals("+79811323878", test);
        System.out.println(test);
    }
}