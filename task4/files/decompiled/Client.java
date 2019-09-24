package com.badguy.terrortime;

import android.util.*;
import java.util.function.*;
import com.badguy.terrortime.crypto.*;
import android.support.v4.util.*;
import java.security.*;
import javax.crypto.*;
import java.security.spec.*;
import javax.crypto.spec.*;
import java.util.*;
import java.util.stream.*;
import android.content.*;

public class Client
{
    private BlobAppField checkPin;
    private Set<PublicKey> clientKeySet;
    private List<Contact> contacts;
    private TextAppField encryptPin;
    private List<Message> messages;
    private BlobAppField oAuth2AccessToken;
    private IntAppField oAuth2AccessTokenExpiration;
    private TextAppField oAuth2ClientId;
    private BlobAppField oAuth2ClientSecret;
    private BlobAppField oAuth2RenewToken;
    private IntAppField oAuth2RenewTokenExpiration;
    private TextAppField oAuth2ServerIP;
    private BlobAppField privateKey;
    private BlobAppField publicKey;
    private String publicKeyFingerprint;
    private TextAppField registerServerIP;
    private PrivateKey rsaPrivateKey;
    private PublicKey rsaPublicKey;
    private TextAppField xmppServerIP;
    private TextAppField xmppUserName;
    
    public Client(final String s) {
        this.registerServerIP = new TextAppField();
        this.xmppUserName = new TextAppField();
        this.xmppServerIP = new TextAppField();
        this.oAuth2ClientId = new TextAppField();
        this.oAuth2ClientSecret = new BlobAppField();
        this.oAuth2AccessToken = new BlobAppField();
        this.oAuth2RenewToken = new BlobAppField();
        this.oAuth2ServerIP = new TextAppField();
        this.oAuth2AccessTokenExpiration = new IntAppField();
        this.oAuth2RenewTokenExpiration = new IntAppField();
        this.privateKey = new BlobAppField();
        this.publicKey = new BlobAppField();
        this.checkPin = new BlobAppField();
        this.encryptPin = new TextAppField();
        this.contacts = new ArrayList<Contact>();
        this.messages = new ArrayList<Message>();
        this.clientKeySet = new HashSet<PublicKey>();
        this.rsaPublicKey = null;
        this.rsaPrivateKey = null;
        this.publicKeyFingerprint = null;
        if (s != null) {
            this.oAuth2ClientId.setValue(s);
            this.xmppUserName.setValue(s);
        }
    }
    
    public final void addAllContacts(final List<Contact> list) {
        if (list != null) {
            list.forEach(new _$$Lambda$Client$OF4irCVvHD9CrDMRD59yU9HT9Qs(this));
        }
    }
    
    public final void addAllMessages(final List<Message> list) {
        if (list != null) {
            final Iterator<Message> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.addMessage(iterator.next());
            }
        }
    }
    
    public final void addContact(final Contact contact) {
        if (contact != null && !this.getContact(contact.getContactId()).isPresent()) {
            this.contacts.add(contact);
        }
    }
    
    public final void addContact(final String s) {
        if (s != null && !this.getContact(s).isPresent()) {
            this.contacts.add(new Contact(this.getOAuth2ClientId(), s));
        }
    }
    
    public final void addMessage(final Message message) {
        if (message != null) {
            this.messages.add(new Message(this.getOAuth2ClientId(), message.getContactId(), message.getContent(), message.isFromClient()));
        }
    }
    
    public final boolean addPublicKey(final PublicKey publicKey) {
        return this.clientKeySet.add(publicKey);
    }
    
    public final boolean addPublicKeys(final Set<PublicKey> set) {
        return this.clientKeySet.addAll(set);
    }
    
    public final Long countContact(final String s) {
        return this.messages.stream().filter(new _$$Lambda$Client$djO3CNH8MRoAhjVnoHcnAJSuwMA(s)).count();
    }
    
    public final Integer countMessages(final String s, final boolean b) {
        return this.getMessages(s, b).size();
    }
    
    public byte[] decryptClientBytes(final String s, final byte[] array) throws Exception {
        byte[] aesDecrypt_ECB = null;
        final SecretKey generateSymmetricKey = this.generateSymmetricKey();
        Label_0030: {
            if (array == null) {
                Log.d("decryptClientBytes", "Empty (NULL) Client variable passed to function. This very well might NOT be an error.");
                break Label_0030;
            }
            try {
                aesDecrypt_ECB = CryptHelper.aesDecrypt_ECB(generateSymmetricKey, array);
                if (array != null) {
                    Log.d("decryptClientBytes", "Ran successfully on non-NULL value.");
                }
                return aesDecrypt_ECB;
            }
            catch (Exception ex) {
                throw new Exception(ex);
            }
        }
    }
    
    public final Optional<byte[]> decryptMessage(final byte[] array) {
        final Message message = new Message(this.getXmppUserName(), "", array, true);
        if (!this.decryptMessage(message)) {
            return Optional.empty();
        }
        return Optional.of(message.getContent());
    }
    
    public final boolean decryptMessage(final Message message) {
        if (message == null) {
            return false;
        }
        try {
            message.setContent(Messaging.decryptMessage(message, this.getRsaPrivateKey().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$CA_4KDL4ayDN_awQixHsotezqSM.INSTANCE), this.getPublicKeyFingerprint().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$K_BfC2wVH_wVfQH2jGk9LYOSi8I.INSTANCE)).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$6isRKf6DuVm8vF1iOvC0uukRRY4.INSTANCE));
            return true;
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Unable to decrypt message", t);
            message.setContent("<decryption failure>".getBytes());
            return false;
        }
    }
    
    public byte[] encryptClientBytes(final String s, final byte[] array) throws Exception {
        byte[] aesEncrypt_ECB = null;
        final SecretKey generateSymmetricKey = this.generateSymmetricKey();
        Label_0030: {
            if (array == null) {
                Log.d("encryptClientBytes", "Empty (NULL) Client variable passed to function. This very well might NOT be an error.");
                break Label_0030;
            }
            try {
                aesEncrypt_ECB = CryptHelper.aesEncrypt_ECB(generateSymmetricKey, array);
                if (array != null) {
                    Log.d("encryptClientBytes", "Ran successfully on non-NULL value.");
                }
                return aesEncrypt_ECB;
            }
            catch (Exception ex) {
                throw new Exception(ex);
            }
        }
    }
    
    public final boolean encryptMessage(final Message message) {
        if (message == null) {
            return false;
        }
        try {
            final Set<PublicKey> contactKeys = this.getContactKeys(message.getContactId());
            if (contactKeys.isEmpty()) {
                throw new Exception("No public key for contact");
            }
            final Set<PublicKey> publicKeys = this.getPublicKeys();
            if (!publicKeys.isEmpty()) {
                message.setContent(Messaging.encryptMessage(message, publicKeys, contactKeys).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$nqavyqI0yM7vNejtlFsc_X73W0w.INSTANCE));
                return true;
            }
            throw new Exception("No public key for client");
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Unable to encrypt message", t);
            message.setContent("<encryption failure>".getBytes());
            return false;
        }
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Client client = Client.class.cast(o);
        if (this.registerServerIP.equals(client.registerServerIP) && this.xmppUserName.equals(client.xmppUserName) && this.xmppServerIP.equals(client.xmppServerIP) && this.oAuth2ClientId.equals(client.oAuth2ClientId) && this.oAuth2ClientSecret.equals(client.oAuth2ClientSecret) && this.oAuth2AccessToken.equals(client.oAuth2AccessToken) && this.oAuth2ServerIP.equals(client.oAuth2ServerIP) && this.oAuth2AccessTokenExpiration.equals(client.oAuth2AccessTokenExpiration) && this.oAuth2RenewTokenExpiration.equals(client.oAuth2RenewTokenExpiration) && this.privateKey.equals(client.privateKey) && this.publicKey.equals(client.publicKey) && this.checkPin.equals(client.checkPin) && this.encryptPin.equals(client.encryptPin)) {
            final PrivateKey rsaPrivateKey = this.rsaPrivateKey;
            if (rsaPrivateKey != null && client.rsaPrivateKey != null) {
                if (!Arrays.equals(rsaPrivateKey.toString().getBytes(), client.rsaPrivateKey.toString().getBytes())) {
                    return false;
                }
            }
            else {
                if (this.rsaPrivateKey != null && client.rsaPrivateKey == null) {
                    return false;
                }
                if (this.rsaPrivateKey == null && client.rsaPrivateKey != null) {
                    return false;
                }
            }
            final PublicKey rsaPublicKey = this.rsaPublicKey;
            if (rsaPublicKey != null && client.rsaPublicKey != null) {
                if (!Arrays.equals(rsaPublicKey.toString().getBytes(), client.rsaPublicKey.toString().getBytes())) {
                    return false;
                }
            }
            else {
                if (this.rsaPublicKey != null && client.rsaPublicKey == null) {
                    return false;
                }
                if (this.rsaPublicKey == null && client.rsaPublicKey != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public final boolean generatePublicPrivateKeys() {
        try {
            final Pair<String, String> generatePublicPrivateKeys = Keygen.generatePublicPrivateKeys();
            this.setPublicKey(generatePublicPrivateKeys.first.getBytes());
            this.setPrivateKey(this.getEncryptPin(), generatePublicPrivateKeys.second.getBytes());
            final Pair<PublicKey, PrivateKey> decodePEMKeyPair = CryptHelper.decodePEMKeyPair(generatePublicPrivateKeys.first, generatePublicPrivateKeys.second);
            this.setRsaPublicKey(decodePEMKeyPair.first);
            this.setRsaPrivateKey(decodePEMKeyPair.second);
            this.setPublicKeyFingerprint(CryptHelper.computeKeyFingerprint(this.rsaPublicKey.getEncoded()));
            return true;
        }
        finally {
            final Throwable t;
            Log.e("generatePublicPrivateKeys", "Failed to generate keypair", t);
            return false;
        }
    }
    
    public final SecretKey generateSymmetricKey() throws Exception {
        final String value = this.encryptPin.getValue();
        if (!this.encryptPin.isDefaultValue()) {
            final byte[] value2 = this.checkPin.getValue();
            byte[] array = null;
            byte[] array2 = value2;
            byte[] digest;
            try {
                digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes("UTF-8"));
                byte[] value3 = value2;
                if (value2 == null) {
                    value3 = (array2 = digest);
                    array = digest;
                    this.checkPin.setValue(value3);
                }
                array2 = value3;
            }
            catch (Exception ex) {
                Log.e("EXCEPTION LOG", "generateSymmetricKey: ", (Throwable)ex);
                digest = array;
            }
            if (new BlobAppField(digest).equals(this.checkPin)) {
                try {
                    return new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(new PBEKeySpec(value.toCharArray(), array2, 10000, 256)).getEncoded(), "AES");
                }
                catch (Exception ex2) {
                    Log.e("EXCEPTION LOG", "generateSymmetricKey: ", (Throwable)ex2);
                    throw new RuntimeException(ex2);
                }
            }
            throw new RuntimeException("Invalid Pin");
        }
        throw new RuntimeException("Unset Pin");
    }
    
    public final byte[] getCheckPin() {
        return this.checkPin.getValue();
    }
    
    public final Optional<Contact> getContact(final String s) {
        if (s != null) {
            return this.contacts.stream().filter(new _$$Lambda$Client$FPwS0v5nzxnLmZDXzmYdDsGA7cA(s)).findFirst();
        }
        return Optional.empty();
    }
    
    public final Set<PublicKey> getContactKeys(final String s) {
        try {
            return this.getContact(s).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$oEoeCWrVIKmkWCtANnm11MAbGGc.INSTANCE).getPublicKeys();
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Unable to get contact's public key", t);
            return Collections.emptySet();
        }
    }
    
    public final List<Contact> getContactList() {
        return this.contacts;
    }
    
    public final String getEncryptPin() {
        return this.encryptPin.getValue();
    }
    
    public byte[] getEncrypted_oAuth2AccessToken() {
        return this.oAuth2AccessToken.getValue();
    }
    
    public byte[] getEncrypted_oAuth2ClientSecret() {
        return this.oAuth2ClientSecret.getValue();
    }
    
    public byte[] getEncrypted_oAuth2RenewToken() {
        return this.oAuth2RenewToken.getValue();
    }
    
    public byte[] getEncrypted_privateKey() {
        return this.privateKey.getValue();
    }
    
    public final List<Message> getMessageList() {
        if (this.messages == null) {
            this.messages = new ArrayList<Message>();
        }
        return this.messages;
    }
    
    public final List<Message> getMessages(final String s) {
        return this.messages.stream().filter(new _$$Lambda$Client$0Gv2EWXx5edD4m0j_7tFQy_9Yq8(s)).collect(Collectors.toCollection((Supplier<List<Message>>)_$$Lambda$Client$OGSS2qx6njxlnp0dnKb4lA3jnw8.INSTANCE));
    }
    
    public final List<Message> getMessages(final String s, final boolean b) {
        return this.messages.stream().filter(new _$$Lambda$Client$RvLwVaSZdo56VGeC57UBTCbPC5w(s, b)).collect(Collectors.toCollection((Supplier<List<Message>>)_$$Lambda$Client$OGSS2qx6njxlnp0dnKb4lA3jnw8.INSTANCE));
    }
    
    public final byte[] getOAuth2AccessToken(final String s) throws Exception {
        return this.decryptClientBytes(s, this.oAuth2AccessToken.getValue());
    }
    
    public final Integer getOAuth2AccessTokenExpiration() {
        return this.oAuth2AccessTokenExpiration.getValue();
    }
    
    public final String getOAuth2ClientId() {
        return this.oAuth2ClientId.getValue();
    }
    
    public final byte[] getOAuth2ClientSecret(final String s) throws Exception {
        return this.decryptClientBytes(s, this.oAuth2ClientSecret.getValue());
    }
    
    public final byte[] getOAuth2RenewToken(final String s) throws Exception {
        return this.decryptClientBytes(s, this.oAuth2RenewToken.getValue());
    }
    
    public final Integer getOAuth2RenewTokenExpiration() {
        return this.oAuth2RenewTokenExpiration.getValue();
    }
    
    public final String getOAuth2ServerIP() {
        return this.oAuth2ServerIP.getValue();
    }
    
    public final byte[] getPrivateKey(final String s) throws Exception {
        return this.decryptClientBytes(s, this.privateKey.getValue());
    }
    
    public final byte[] getPublicKey() {
        return this.publicKey.getValue();
    }
    
    public final Optional<String> getPublicKeyFingerprint() {
        return Optional.ofNullable(this.publicKeyFingerprint);
    }
    
    public final Set<PublicKey> getPublicKeys() {
        return this.clientKeySet;
    }
    
    public final String getRegisterServerIP() {
        return this.registerServerIP.getValue();
    }
    
    public final Optional<PrivateKey> getRsaPrivateKey() {
        return Optional.ofNullable(this.rsaPrivateKey);
    }
    
    public final Optional<PublicKey> getRsaPublicKey() {
        return Optional.ofNullable(this.rsaPublicKey);
    }
    
    public final String getXmppServerIP() {
        return this.xmppServerIP.getValue();
    }
    
    public final String getXmppUserName() {
        return this.xmppUserName.getValue();
    }
    
    public final boolean removePublicKey(final PublicKey publicKey) {
        return this.clientKeySet.remove(publicKey);
    }
    
    public final void setCheckPin(final byte[] value) {
        if (value != null) {
            this.checkPin.setValue(value);
        }
    }
    
    public final void setContactList(final List<Contact> contacts) {
        if (contacts != null) {
            this.contacts = contacts;
        }
    }
    
    public final void setEncryptPin(final String value) {
        if (value != null) {
            this.encryptPin.setValue(value);
        }
    }
    
    public void setEncrypted_oAuth2AccessToken(final byte[] value) {
        this.oAuth2AccessToken.setValue(value);
    }
    
    public void setEncrypted_oAuth2ClientSecret(final byte[] value) {
        if (value != null) {
            this.oAuth2ClientSecret.setValue(value);
        }
    }
    
    public void setEncrypted_oAuth2RenewToken(final byte[] value) {
        if (value != null) {
            this.oAuth2RenewToken.setValue(value);
        }
    }
    
    public void setEncrypted_privateKey(final byte[] value) {
        if (value != null) {
            this.privateKey.setValue(value);
            try {
                this.setRsaPrivateKey(CryptHelper.convertPrivatePEMtoPrivateKey(new String(this.getPrivateKey(this.getEncryptPin()))).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$pBdDMCuCdoHjV0KLOJ8jHAtVEag.INSTANCE));
            }
            finally {
                final Throwable t;
                Log.e("EXCEPTION", "unable to set private key", t);
            }
        }
    }
    
    public final void setMessageList(final List<Message> messages) {
        if (messages != null) {
            this.messages = messages;
        }
    }
    
    public final void setOAuth2AccessToken(final String s, final byte[] array) throws Exception {
        if (array != null) {
            this.oAuth2AccessToken.setValue(this.encryptClientBytes(s, array));
        }
    }
    
    public final void setOAuth2AccessTokenExpiration(final Integer value) {
        if (value != null) {
            this.oAuth2AccessTokenExpiration.setValue(value);
        }
    }
    
    public final void setOAuth2ClientId(final String value) {
        if (value != null) {
            this.oAuth2ClientId.setValue(value);
        }
    }
    
    public final void setOAuth2ClientSecret(final String s, final byte[] array) throws Exception {
        if (array != null) {
            this.oAuth2ClientSecret.setValue(this.encryptClientBytes(s, array));
        }
    }
    
    public final void setOAuth2RenewToken(final String s, final byte[] array) throws Exception {
        if (array != null) {
            this.oAuth2RenewToken.setValue(this.encryptClientBytes(s, array));
        }
    }
    
    public final void setOAuth2RenewTokenExpiration(final Integer value) {
        if (value != null) {
            this.oAuth2RenewTokenExpiration.setValue(value);
        }
    }
    
    public final void setOAuth2ServerIP(final String value) {
        if (value != null) {
            this.oAuth2ServerIP.setValue(value);
        }
    }
    
    public final void setPrivateKey(final String s, final byte[] array) throws Exception {
        if (array != null && s != null) {
            this.privateKey.setValue(this.encryptClientBytes(s, array));
            try {
                this.setRsaPrivateKey(CryptHelper.convertPrivatePEMtoPrivateKey(new String(array)).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$63WDeVJkarpIOu65SgJm9WCk9ec.INSTANCE));
            }
            finally {
                final Throwable t;
                Log.e("EXCEPTION", t.getMessage(), t);
            }
        }
    }
    
    public final void setPublicKey(final byte[] value) {
        if (value != null) {
            this.publicKey.setValue(value);
            try {
                this.setRsaPublicKey(CryptHelper.convertPublicPEMtoPublicKey(new String(value)).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$Client$OqAgwdbZoVbn91jchC8nAsBPmRw.INSTANCE));
                if (this.rsaPublicKey != null) {
                    this.setPublicKeyFingerprint(CryptHelper.computeKeyFingerprint(this.rsaPublicKey.getEncoded()));
                }
            }
            finally {
                final Throwable t;
                Log.e("EXCEPTION", "unable to set public key", t);
            }
        }
    }
    
    public final void setPublicKeyFingerprint(final String publicKeyFingerprint) {
        this.publicKeyFingerprint = publicKeyFingerprint;
    }
    
    public final void setPublicKeys(final Set<PublicKey> clientKeySet) {
        this.clientKeySet = clientKeySet;
    }
    
    public final void setRegisterServerIP(final String value) {
        if (value != null) {
            this.registerServerIP.setValue(value);
        }
    }
    
    public final void setRsaPrivateKey(final PrivateKey rsaPrivateKey) {
        this.rsaPrivateKey = rsaPrivateKey;
    }
    
    public final void setRsaPublicKey(final PublicKey rsaPublicKey) {
        this.addPublicKey(this.rsaPublicKey = rsaPublicKey);
    }
    
    public final void setXmppServerIP(final String value) {
        if (value != null) {
            this.xmppServerIP.setValue(value);
        }
    }
    
    public final void setXmppUserName(final String value) {
        final TextAppField xmppUserName = this.xmppUserName;
        if (xmppUserName != null) {
            xmppUserName.setValue(value);
        }
    }
    
    public final void unsetCheckPin() {
        this.checkPin.setValue(null);
    }
    
    public void validateAccessToken(final Context ex) throws Exception {
        if (ex != null) {
            final String encryptPin = this.getEncryptPin();
            final String[] split = this.getOAuth2ServerIP().split(":");
            final StringBuilder sb = new StringBuilder();
            sb.append("https://");
            sb.append(split[0]);
            final String string = sb.toString();
            final String oAuth2ClientId = this.getOAuth2ClientId();
            final String s = new String(this.getOAuth2ClientSecret(encryptPin));
            int intValue;
            if (split.length == 2) {
                intValue = Integer.valueOf(split[1]);
            }
            else {
                intValue = 443;
            }
            try {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append("/oauth2/token");
                final String string2 = sb2.toString();
                try {
                    if (new ClientCredentialTokenRequest((Context)ex, string2, oAuth2ClientId, s, "client_credentials", "chat", "", intValue).getValidTokenAsByteArray(this, (Context)ex) != null) {
                        return;
                    }
                    throw new RuntimeException("Token Request failed.");
                }
                catch (Exception ex) {}
            }
            catch (Exception ex2) {}
            throw new RuntimeException(ex);
        }
        throw new RuntimeException("Null context");
    }
}
