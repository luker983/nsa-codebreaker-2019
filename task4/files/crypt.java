import java.util.function.*;
import java.security.*;
import javax.crypto.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import java.util.*;
import java.util.stream.*;
import javax.xml.bind.*;

public class Crypt
{
    //static String pin = "587148";
    //static String encryptedPin = "0328c53a2976ecf8bf020a37f8c30c78";
    
    static String pin = "587148";
    static String encryptedPin = "0328c53a2976ecf8bf020a37f8c30c78";
    static String hash = "50b6b08d814a64c8c3f4951efc153235537f0801ae355d4c8c3a1bb92aef9fa4";

    public static void main(String []args) {
        try {
        byte[] array = javax.xml.bind.DatatypeConverter.parseHexBinary(encryptedPin);
        byte[] digest = javax.xml.bind.DatatypeConverter.parseHexBinary(hash);

        System.out.println(array);
        //byte[] digest = MessageDigest.getInstance("SHA-256").digest(pin.getBytes("UTF-8"));

        
            final SecretKey gsk = new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(encryptedPin.toCharArray(), digest, 1000, 256)).getEncoded(), "AES");

        System.out.println(decrypt(array, gsk));
        System.out.println(array);
        //System.out.println(decrypt(hexStringToByteArray(encryptedPin), gsk));
        } catch(Exception e){}
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] decrypt(final byte[] array, final SecretKey gsk) {
        byte[] aesDecrypt_ECB = null;
        aesDecrypt_ECB = aesDecrypt_ECB(gsk, array);
        return aesDecrypt_ECB;
    }

    public static byte[] encrypt(final String s, final byte[] array) {
        return null;
    }

    public static byte[] aesDecrypt_ECB(final SecretKey secretKey, final byte[] array) {
        try {
            final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
            instance.init(2, secretKey);
            return instance.doFinal(array);
        } catch(Exception e) {
            e.printStackTrace(); 
        }

        return null;
    }
}
