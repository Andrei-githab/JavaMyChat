package chatprogram;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.security.PublicKey;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class PGPTest {
    PGP testPGP = new PGP();
    PublicKey publicKeyTest;
    String testMess = "Test";
    @Test
    public void initPGP() {
        publicKeyTest = testPGP.getPublicKey();
        System.out.println("Get public key: " + publicKeyTest);
    }

    @Test
    void encrypt() throws Exception {
        PGP testPGPe = new PGP();
        PublicKey publicKey = testPGPe.getPublicKey();
        String testMessEncrypt = testPGPe.encrypt(testMess, publicKey);
    }

    @Test
    void decrypt() throws Exception {
        PGP testPGPdecrypt = new PGP();
        PublicKey publicKey = testPGPdecrypt.getPublicKey();
        String testMessDecrypt = testPGPdecrypt.encrypt(testMess, publicKey);
        assertEquals("Test", testPGPdecrypt.decrypt(testMessDecrypt));
    }
}