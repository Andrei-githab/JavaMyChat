package chatprogram;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class PGPTest {
    PGP testPGP = new PGP();
    String testMess = "Это тестовое сообщения (rus). This is a test message(eng)";
    @Test
    public void PGP() {
        PublicKey publicKeyTest = testPGP.getPublicKey();
        assertNotNull(publicKeyTest);
        System.out.println("Публичный ключ: " + publicKeyTest);
    }

    @Test
    void encrypt() throws Exception {
        PGP testPGPe = new PGP();
        PublicKey publicKeyTestEncrypt = testPGPe.getPublicKey();
        String testMessEncrypt = testPGPe.encrypt(testMess, publicKeyTestEncrypt);
        System.out.println("Зашифрованное сообщение: " + testMessEncrypt);
    }

    @Test
    void decrypt() throws Exception {
        PGP testPGPdecrypt= new PGP();
        PublicKey publicKeyDecrypt = testPGPdecrypt.getPublicKey();
        String testMessDecrypt = testPGPdecrypt.encrypt(testMess, publicKeyDecrypt);
        assertEquals("Это тестовое сообщения (rus). This is a test message(eng)", testPGPdecrypt.decrypt(testMessDecrypt));
        System.out.println("Расшифрованное сообщение: " + testPGPdecrypt.decrypt(testMessDecrypt));
    }
}