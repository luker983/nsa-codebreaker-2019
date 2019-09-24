package com.badguy.terrortime.crypto;

import com.badguy.terrortime.*;
import android.util.*;
import org.json.*;
import java.util.function.*;
import java.util.*;
import android.support.v4.util.*;
import javax.crypto.spec.*;
import org.bouncycastle.jce.provider.*;
import javax.crypto.*;
import java.security.*;
import java.security.spec.*;

public class Messaging
{
    public static final Optional<byte[]> decryptMessage(Message ex, final PrivateKey privateKey, String string) {
        try {
            final JSONObject jsonObject = new JSONObject(new String(((Message)ex).getContent()));
            Label_0449: {
                if (!jsonObject.has("messageKey") || !jsonObject.has("message") || !jsonObject.has("messageSig")) {
                    break Label_0449;
                }
                final JSONObject jsonObject2 = jsonObject.getJSONObject("messageKey");
                final JSONObject jsonObject3 = jsonObject.getJSONObject("message");
                Label_0437: {
                    if (!jsonObject3.has("msg") || !jsonObject3.has("iv")) {
                        break Label_0437;
                    }
                    if (!jsonObject2.has(string)) {
                        ex = (NoSuchProviderException)new StringBuilder();
                        ((StringBuilder)ex).append("No fingerprint found matching ");
                        ((StringBuilder)ex).append(string);
                        Log.v("decryptMessage", ((StringBuilder)ex).toString());
                        return Optional.empty();
                    }
                    string = jsonObject2.getString(string);
                    try {
                        final SecretKey unwrapKey = CryptHelper.unwrapKey(privateKey, string);
                        final String string2 = jsonObject3.getString("msg");
                        final byte[] decode = Base64.getDecoder().decode(jsonObject3.getString("iv"));
                        final String string3 = jsonObject.getString("messageSig");
                        final byte[] decode2 = Base64.getDecoder().decode(string2);
                        if (!Arrays.equals(CryptHelper.hmacSHA256(unwrapKey, decode2), Base64.getDecoder().decode(string3))) {
                            Log.v("decryptMessage", "HMAC signature does not match");
                            return Optional.empty();
                        }
                        final JSONObject jsonObject4 = new JSONObject(new String(CryptHelper.aesDecrypt(unwrapKey, decode2, decode)));
                        if (jsonObject4.has(((Message)ex).getClientId()) && jsonObject4.has(((Message)ex).getContactId()) && jsonObject4.has("body")) {
                            final JSONArray jsonArray = jsonObject4.getJSONArray(((Message)ex).getClientId());
                            final JSONArray jsonArray2 = jsonObject4.getJSONArray(((Message)ex).getContactId());
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                if (!jsonObject2.has(jsonArray.getString(i))) {
                                    Log.v("decryptMessage", "Mismatched recipients");
                                    return Optional.empty();
                                }
                            }
                            for (int j = 0; j < jsonArray2.length(); ++j) {
                                if (!jsonObject2.has(jsonArray2.getString(j))) {
                                    Log.v("decryptMessage", "Mismatched recipients");
                                    return Optional.empty();
                                }
                            }
                            return Optional.of(jsonObject4.getString("body").getBytes());
                        }
                        Log.v("decryptMessage", "Invalid internal msg format");
                        return Optional.empty();
                        Log.v("decryptMessage", "Invalid message spec");
                        return Optional.empty();
                        Log.v("decryptMessage", "Invalid message spec");
                        return Optional.empty();
                    }
                    catch (NoSuchProviderException ex) {}
                    catch (InvalidAlgorithmParameterException ex) {}
                    catch (InvalidKeyException ex) {}
                    catch (IllegalBlockSizeException ex) {}
                    catch (BadPaddingException ex) {}
                    catch (NoSuchAlgorithmException ex) {}
                    catch (NoSuchPaddingException ex) {}
                    catch (JSONException ex) {}
                }
            }
        }
        catch (NoSuchProviderException ex) {}
        catch (InvalidAlgorithmParameterException ex) {}
        catch (InvalidKeyException ex) {}
        catch (IllegalBlockSizeException ex) {}
        catch (BadPaddingException ex) {}
        catch (NoSuchAlgorithmException ex) {}
        catch (NoSuchPaddingException ex) {}
        catch (JSONException ex2) {}
        Log.e("decryptMessage", "unable to decrypt message", (Throwable)ex);
        return Optional.empty();
    }
    
    public static final Optional<byte[]> encryptMessage(final Message message, final Set<PublicKey> set, final Set<PublicKey> set2) {
        try {
            final SecretKey secretKey = generateMessageKey().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Messaging$peSwfA0s4V_o_UFWxRkaqv6FvqY.INSTANCE);
            final HashMap<String, String> hashMap = new HashMap<String, String>();
            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
            for (final PublicKey publicKey : set2) {
                hashMap.put(CryptHelper.computeKeyFingerprint(publicKey.getEncoded()), CryptHelper.wrapKey(publicKey, secretKey));
            }
            for (final PublicKey publicKey2 : set) {
                hashMap2.put(CryptHelper.computeKeyFingerprint(publicKey2.getEncoded()), CryptHelper.wrapKey(publicKey2, secretKey));
            }
            final JSONObject jsonObject = new JSONObject();
            final JSONObject jsonObject2 = new JSONObject();
            final JSONObject jsonObject3 = new JSONObject();
            final JSONObject jsonObject4 = new JSONObject();
            final JSONArray jsonArray = new JSONArray();
            final JSONArray jsonArray2 = new JSONArray();
            for (final Map.Entry<String, String> entry : hashMap.entrySet()) {
                jsonObject2.put((String)entry.getKey(), (Object)entry.getValue());
                jsonArray.put((Object)entry.getKey());
            }
            for (final Map.Entry<String, String> entry2 : hashMap2.entrySet()) {
                jsonObject2.put((String)entry2.getKey(), (Object)entry2.getValue());
                jsonArray2.put((Object)entry2.getKey());
            }
            jsonObject4.put(message.getClientId(), (Object)jsonArray2);
            jsonObject4.put(message.getContactId(), (Object)jsonArray);
            jsonObject4.put("body", (Object)new String(message.getContent()));
            final Pair<byte[], byte[]> aesEncrypt = CryptHelper.aesEncrypt(secretKey, jsonObject4.toString().getBytes());
            final String encodeToString = Base64.getEncoder().encodeToString(aesEncrypt.first);
            final byte[] hmacSHA256 = CryptHelper.hmacSHA256(secretKey, aesEncrypt.second);
            final String encodeToString2 = Base64.getEncoder().encodeToString(aesEncrypt.second);
            final String encodeToString3 = Base64.getEncoder().encodeToString(hmacSHA256);
            jsonObject3.put("iv", (Object)encodeToString);
            jsonObject3.put("msg", (Object)encodeToString2);
            jsonObject.put("messageKey", (Object)jsonObject2);
            jsonObject.put("message", (Object)jsonObject3);
            jsonObject.put("messageSig", (Object)encodeToString3);
            return Optional.ofNullable(jsonObject.toString().getBytes());
        }
        finally {
            final Throwable t;
            Log.e("encryptMessage", "unable to encrypt message", t);
            return Optional.empty();
        }
    }
    
    private static final Optional<SecretKey> generateMessageKey() {
        final SecureRandom secureRandom = new SecureRandom();
        Object generateSecret = new byte[32];
        secureRandom.nextBytes((byte[])generateSecret);
        final SecretKeySpec secretKeySpec = new SecretKeySpec((byte[])generateSecret, "AES");
        final Provider provider = null;
        try {
            generateSecret = new BouncyCastleProvider();
            generateSecret = SecretKeyFactory.getInstance("AES", (Provider)generateSecret).generateSecret(secretKeySpec);
            return Optional.ofNullable(generateSecret);
        }
        catch (NoSuchAlgorithmException generateSecret) {}
        catch (InvalidKeySpecException ex) {}
        Log.e("generateMessageKey", "Unable to generate message key", (Throwable)generateSecret);
        generateSecret = provider;
        return Optional.ofNullable(generateSecret);
    }
}
