package chatprogram;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPhoneTest {
    @Test
    void setNumberPhone() {
        UserPhone userPhone = new UserPhone("+7 (981) 132-38-78");
        String test = userPhone.getNumberPhone();
        assertEquals("79811323878", test);
    }
}