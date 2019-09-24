package com.badguy.terrortime.crypto;

import javax.crypto.spec.*;
import android.support.v4.util.*;
import org.bouncycastle.openssl.jcajce.*;
import java.util.*;
import java.io.*;
import org.bouncycastle.jce.provider.*;
import org.bouncycastle.util.io.pem.*;
import java.security.spec.*;
import java.util.function.*;
import javax.crypto.*;
import java.security.*;

public class CryptHelper
{
    public static byte[] aesDecrypt(final SecretKey secretKey, final byte[] array, final byte[] array2) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(2, secretKey, new IvParameterSpec(array2));
        return instance.doFinal(array);
    }
    
    public static byte[] aesDecrypt_ECB(final SecretKey secretKey, final byte[] array) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
        instance.init(2, secretKey);
        return instance.doFinal(array);
    }
    
    public static Pair<byte[], byte[]> aesEncrypt(final SecretKey secretKey, final byte[] array) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        final byte[] generateRandom = generateRandom(16);
        instance.init(1, secretKey, new IvParameterSpec(generateRandom));
        return new Pair<byte[], byte[]>(generateRandom, instance.doFinal(array));
    }
    
    public static byte[] aesEncrypt_ECB(final SecretKey secretKey, final byte[] array) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        final Cipher instance = Cipher.getInstance("AES/ECB/PKCS5Padding");
        instance.init(1, secretKey);
        return instance.doFinal(array);
    }
    
    public static String computeKeyFingerprint(final byte[] array) throws NoSuchAlgorithmException {
        final MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(array);
        return Base64.getEncoder().encodeToString(instance.digest());
    }
    
    public static String convertKeyToPEM(final Key key) throws IOException {
        final StringWriter stringWriter = new StringWriter();
        final PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(new JcaMiscPEMGenerator(key));
        pemWriter.flush();
        pemWriter.close();
        return stringWriter.toString();
    }
    
    public static Optional<PrivateKey> convertPrivatePEMtoPrivateKey(final String s) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        final PemObject pemObject = new PemReader(new StringReader(s)).readPemObject();
        if (pemObject == null) {
            return Optional.ofNullable((PrivateKey)null);
        }
        return Optional.ofNullable(KeyFactory.getInstance("RSA", new BouncyCastleProvider()).generatePrivate(new PKCS8EncodedKeySpec(pemObject.getContent())));
    }
    
    public static Optional<PublicKey> convertPublicPEMtoPublicKey(final String s) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        final PemObject pemObject = new PemReader(new StringReader(s)).readPemObject();
        if (pemObject == null) {
            return Optional.ofNullable((PublicKey)null);
        }
        return Optional.ofNullable(KeyFactory.getInstance("RSA", new BouncyCastleProvider()).generatePublic(new X509EncodedKeySpec(pemObject.getContent())));
    }
    
    public static Pair<PublicKey, PrivateKey> decodePEMKeyPair(final String s, final String s2) throws Throwable {
        return new Pair<PublicKey, PrivateKey>(convertPublicPEMtoPublicKey(s).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$CryptHelper$vbNDnHFXcHxBXi36SJEI309CQ4E.INSTANCE), convertPrivatePEMtoPrivateKey(s2).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$CryptHelper$NPqLhrm_ALq6B6zs86mS0C8aXlo.INSTANCE));
    }
    
    public static byte[] generateRandom(final int n) {
        final SecureRandom secureRandom = new SecureRandom();
        final byte[] array = new byte[n];
        secureRandom.nextBytes(array);
        return array;
    }
    
    public static final byte[] hmacSHA256(final SecretKey secretKey, final byte[] array) throws NoSuchAlgorithmException, InvalidKeyException {
        final Mac instance = Mac.getInstance("HmacSHA256");
        instance.init(secretKey);
        return instance.doFinal(array);
    }
    
    public static SecretKey unwrapKey(final PrivateKey privateKey, final String s) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, NoSuchProviderException {
        final byte[] decode = Base64.getDecoder().decode(s);
        final Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        instance.init(4, privateKey);
        return (SecretKey)instance.unwrap(decode, "AES", 3);
    }
    
    public static String wrapKey(final PublicKey publicKey, final SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, NoSuchProviderException {
        final Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        instance.init(3, publicKey);
        return Base64.getEncoder().encodeToString(instance.wrap(secretKey));
    }
}
