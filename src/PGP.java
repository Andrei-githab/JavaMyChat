import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
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
    public PGP() {
        try {
            // Метод getInstance() принимает имя алгоритма шифрования, который будет использоваться.
            // В данном случае мы используем алгоритм RSA.
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            // Инициализируем ключ размером 1024 бит
            generator.initialize(1024);
            // Генерируем ключевую пару
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception ignored) {
        }
    }
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
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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
    public String decrypt(String encryptedMessage) throws Exception{
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        // Инициализация шифра в режиме расшифровки
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage,"UTF8");
    }
    private byte[] decode(String data){
        return Base64.getDecoder().decode(data);
    }
}