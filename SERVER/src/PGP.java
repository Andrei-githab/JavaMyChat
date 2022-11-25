import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Base64;
/**
 * Класс шифрования PGP;
 * @author Владимиров Андрей ИБС - 12, Владимир Яровой ИБС - 12, СПБГУТ
 */
public class PGP {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    public PGP() {}
    /**
     * Метод шифрования
     * @param message - сообщение для шифровки
     */
    public String encrypt(String message) throws Exception{
        byte[] messageToBytes = message.getBytes();
        // Создание шифра RSA c режимом шифрования ECB (Electronic Codebook (Режим электронной кодовой книги)) и схемой
        // дополнения PKCS5Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Инициализация шифра в режиме шифрования
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        // Шифрование блока данных
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }
    private String encode(byte[] data){
        return Base64.getEncoder().encodeToString(data);
    }
    /**
     * Метод расшифрования
     * @param encryptedMessage - сообщение для дешифровки
     */
    public String decrypt(String encryptedMessage, PublicKey pkay) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Инициализация шифра в режиме расшифровки
        cipher.init(Cipher.DECRYPT_MODE, pkay);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage,"UTF8");
    }
    private byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}