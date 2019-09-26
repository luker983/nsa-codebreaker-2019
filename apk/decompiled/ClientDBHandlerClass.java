package com.badguy.terrortime;

import android.content.*;
import android.util.*;
import android.database.sqlite.*;
import android.database.*;
import java.util.stream.*;
import java.util.*;

public class ClientDBHandlerClass extends SQLiteOpenHelper
{
    private static final String ACCESS_TOKEN = "atok";
    private static final String ACCESS_TOKEN_EXP = "atokexp";
    private static final String AUTH_SERVER_IP = "asip";
    private static final String CHECK_PIN = "checkpin";
    private static final String CLIENT_ID = "cid";
    private static final String CLIENT_SECRET = "csecret";
    private static final String CONTACT_ID = "contactid";
    private static String CREATE_CLIENTS_TABLE;
    private static String CREATE_CLIENT_CONTACTS;
    private static String CREATE_CLIENT_MESSAGES;
    private static final String DATABASE_NAME = "clientDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String FROM_CLIENT = "fromclient";
    private static final String MESSAGE_CONTENT = "msg";
    private static final String PRIVATE_KEY = "privkey";
    private static final String PUBLIC_KEY = "pubkey";
    private static String QUERY_CLIENTS_TABLE;
    private static String QUERY_CONTACTS_TABLE;
    private static String QUERY_MESSAGE_TABLE;
    private static final String REG_SERVER_IP = "rsip";
    private static final String RENEW_TOKEN = "rtok";
    private static final String RENEW_TOKEN_EXP = "rtokexp";
    public static final String TABLE_CLIENTS = "Clients";
    public static final String TABLE_CONTACTS = "Contacts";
    public static final String TABLE_MESSAGES = "Messages";
    private static final String TIMESTAMP = "tstamp";
    private static final String XMPP_NAME = "xname";
    private static final String XMPP_SERVER_IP = "xsip";
    private static final List<String> clientsColumnNames;
    private static ClientDBHandlerClass sInstance;
    private String cryptPin;
    
    static {
        clientsColumnNames = Arrays.asList("cid", "rsip", "xname", "xsip", "atok", "rtok", "asip", "atokexp", "rtokexp", "pubkey", "privkey", "checkpin");
        ClientDBHandlerClass.CREATE_CLIENTS_TABLE = "CREATE TABLE IF NOT EXISTS Clients(cid TEXT NOT NULL,rsip TEXT,xname TEXT,xsip TEXT,csecret BLOB,atok BLOB,rtok BLOB,asip TEXT,atokexp INTEGER,rtokexp INTEGER,pubkey BLOB,privkey BLOB,checkpin BLOB)";
        ClientDBHandlerClass.QUERY_CLIENTS_TABLE = "SELECT * FROM Clients WHERE ";
        ClientDBHandlerClass.CREATE_CLIENT_CONTACTS = "CREATE TABLE IF NOT EXISTS Contacts(contactid TEXT NOT NULL, cid TEXT NOT NULL )";
        ClientDBHandlerClass.QUERY_CONTACTS_TABLE = "SELECT * FROM Contacts WHERE ";
        ClientDBHandlerClass.CREATE_CLIENT_MESSAGES = "CREATE TABLE IF NOT EXISTS Messages(tstamp INTEGER, cid TEXT NOT NULL,contactid TEXT NOT NULL, fromclient INTEGER,msg BLOB )";
        ClientDBHandlerClass.QUERY_MESSAGE_TABLE = "SELECT * FROM Messages WHERE ";
    }
    
    private ClientDBHandlerClass(final String cryptPin, final Context context) {
        super(context, "clientDB.db", (SQLiteDatabase$CursorFactory)null, 1);
        this.cryptPin = null;
        this.cryptPin = cryptPin;
    }
    
    public static ClientDBHandlerClass getInstance(final String cryptPin, final Context context) {
        if (ClientDBHandlerClass.sInstance == null) {
            synchronized (ClientDBHandlerClass.class) {
                if (ClientDBHandlerClass.sInstance == null) {
                    ClientDBHandlerClass.sInstance = new ClientDBHandlerClass(cryptPin, context.getApplicationContext());
                }
            }
        }
        final ClientDBHandlerClass sInstance = ClientDBHandlerClass.sInstance;
        sInstance.cryptPin = cryptPin;
        return sInstance;
    }
    
    public final void addContact(final Contact contact) {
        if (contact == null) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                final ContentValues contentValues = new ContentValues();
                contentValues.put("contactid", contact.getContactId());
                contentValues.put("cid", contact.getClientId());
                writableDatabase.insertOrThrow("Contacts", (String)null, contentValues);
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "addContact: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final void addMessage(final Message message) {
        if (message == null) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                final ContentValues contentValues = new ContentValues();
                contentValues.put("contactid", message.getContactId());
                contentValues.put("cid", message.getClientId());
                contentValues.put("msg", message.getContent());
                if (!message.isFromClient()) {
                    contentValues.put("fromclient", Integer.valueOf(0));
                }
                else {
                    contentValues.put("fromclient", Integer.valueOf(1));
                }
                contentValues.put("tstamp", Integer.valueOf(0));
                writableDatabase.insertOrThrow("Messages", (String)null, contentValues);
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "addMessage: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final void addOrUpdateClient(final Client client) throws Exception {
        if (client == null) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        final Integer countClients = this.countClients(client.getOAuth2ClientId());
        writableDatabase.beginTransaction();
        try {
            final ContentValues contentValues = new ContentValues();
            contentValues.put("cid", client.getOAuth2ClientId());
            contentValues.put("rsip", client.getRegisterServerIP());
            contentValues.put("xname", client.getXmppUserName());
            contentValues.put("xsip", client.getXmppServerIP());
            contentValues.put("csecret", client.getEncrypted_oAuth2ClientSecret());
            contentValues.put("atok", client.getEncrypted_oAuth2AccessToken());
            contentValues.put("rtok", client.getEncrypted_oAuth2RenewToken());
            contentValues.put("asip", client.getOAuth2ServerIP());
            contentValues.put("atokexp", client.getOAuth2AccessTokenExpiration());
            contentValues.put("rtokexp", client.getOAuth2RenewTokenExpiration());
            contentValues.put("pubkey", client.getPublicKey());
            contentValues.put("privkey", client.getEncrypted_privateKey());
            contentValues.put("checkpin", client.getCheckPin());
            if (countClients == 0) {
                writableDatabase.insertOrThrow("Clients", (String)null, contentValues);
            }
            else {
                if (this.countClients(client.getOAuth2ClientId()) == 0) {
                    throw new RuntimeException("Terrortime is configured for another user");
                }
                contentValues.remove("cid");
                writableDatabase.update("Clients", contentValues, "cid=?", new String[] { client.getOAuth2ClientId() });
            }
            writableDatabase.setTransactionSuccessful();
            writableDatabase.endTransaction();
            return;
        }
        catch (Exception ex) {
            Log.e("ERROR", "addOrUpdateClient: ", (Throwable)ex);
            throw new RuntimeException(ex);
        }
        writableDatabase.endTransaction();
    }
    
    public final void clearClientDB() {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                if (this.countAllTableRecords("Clients") > 0) {
                    writableDatabase.delete("Clients", (String)null, (String[])null);
                }
                if (this.countAllTableRecords("Contacts") > 0) {
                    writableDatabase.delete("Contacts", (String)null, (String[])null);
                }
                if (this.countAllTableRecords("Messages") > 0) {
                    writableDatabase.delete("Messages", (String)null, (String[])null);
                }
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "clearClientDB: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final Integer countAllTableRecords(final String s) {
        final Integer value = 0;
        if (s == null) {
            return value;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(s);
        sb.append(";");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), (String[])null);
        Integer value2 = value;
        if (rawQuery != null) {
            value2 = value;
            if (!rawQuery.isClosed()) {
                value2 = rawQuery.getCount();
                rawQuery.close();
            }
        }
        return value2;
    }
    
    public final Integer countClients(final String s) {
        final Integer value = 0;
        if (s == null) {
            return value;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_CLIENTS_TABLE);
        sb.append("cid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s });
        Integer value2 = value;
        if (rawQuery != null) {
            value2 = value;
            if (!rawQuery.isClosed()) {
                value2 = rawQuery.getCount();
                rawQuery.close();
            }
        }
        return value2;
    }
    
    public final Integer countContacts(final String s) {
        final Integer value = 0;
        if (s == null) {
            return value;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_CONTACTS_TABLE);
        sb.append("cid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s });
        Integer value2 = value;
        if (rawQuery != null) {
            value2 = value;
            if (!rawQuery.isClosed()) {
                value2 = rawQuery.getCount();
                rawQuery.close();
            }
        }
        return value2;
    }
    
    public final Integer countContacts(final String s, final String s2) {
        final Integer value = 0;
        if (s2 == null || s == null) {
            return value;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_CONTACTS_TABLE);
        sb.append("contactid");
        sb.append(" =? AND ");
        sb.append("cid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s2, s });
        Integer value2 = value;
        if (rawQuery != null) {
            value2 = value;
            if (!rawQuery.isClosed()) {
                value2 = rawQuery.getCount();
                rawQuery.close();
            }
        }
        return value2;
    }
    
    public final Integer countMessages(final String s) {
        final Integer value = 0;
        if (s == null) {
            return value;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_MESSAGE_TABLE);
        sb.append("cid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s });
        Integer value2 = value;
        if (rawQuery != null) {
            value2 = value;
            if (!rawQuery.isClosed()) {
                value2 = rawQuery.getCount();
                rawQuery.close();
            }
        }
        return value2;
    }
    
    public final Integer countMessages(final String s, final String s2) {
        final Integer value = 0;
        if (s2 == null || s == null) {
            return value;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return value;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_MESSAGE_TABLE);
        sb.append("cid");
        sb.append(" =? AND ");
        sb.append("contactid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s, s2 });
        Integer value2 = value;
        if (rawQuery != null) {
            value2 = value;
            if (!rawQuery.isClosed()) {
                value2 = rawQuery.getCount();
                rawQuery.close();
            }
        }
        return value2;
    }
    
    public final void deleteClient(final String s) {
        if (s == null) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        while (true) {
            try {
                try {
                    if (this.countAllTableRecords("Clients") > 0 && this.countClients(s) > 0) {
                        writableDatabase.delete("Clients", "cid=?", new String[] { s });
                    }
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                }
                finally {}
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Log.e("EXCEPTION LOG", "deleteClient: ", (Throwable)ex);
                continue;
            }
            break;
        }
        return;
        writableDatabase.endTransaction();
    }
    
    public final void deleteContact(final String s) {
        if (s == null || this.countContacts(s) == 0) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                if (this.countAllTableRecords("Contacts") > 0) {
                    writableDatabase.delete("Contacts", "cid =? ", new String[] { s });
                }
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "deleteContact: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final void deleteContact(final String s, final String s2) {
        if (s2 == null || s == null || this.countContacts(s, s2) == 0) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                if (this.countAllTableRecords("Contacts") > 0) {
                    writableDatabase.delete("Contacts", "contactid =? AND cid =? ", new String[] { s2, s });
                }
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "deleteContact: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final void deleteMessage(final String s) {
        if (s == null || this.countMessages(s) == 0) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                if (this.countAllTableRecords("Messages") > 0) {
                    writableDatabase.delete("Messages", "cid =? ", new String[] { s });
                }
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "deleteMessage: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final void deleteMessage(final String s, final String s2) {
        if (s2 == null || s == null || this.countMessages(s, s2) == 0) {
            return;
        }
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase == null) {
            return;
        }
        writableDatabase.beginTransaction();
        try {
            try {
                if (this.countAllTableRecords("Messages") > 0) {
                    writableDatabase.delete("Messages", "contactid =? AND cid =? ", new String[] { s2, s });
                }
                writableDatabase.setTransactionSuccessful();
            }
            finally {}
        }
        catch (Exception ex) {
            Log.e("EXCEPTION LOG", "deleteMessage: ", (Throwable)ex);
        }
        writableDatabase.endTransaction();
        return;
        writableDatabase.endTransaction();
    }
    
    public final void dropAllTables() {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        if (writableDatabase != null) {
            writableDatabase.execSQL("DROP TABLE IF EXISTS Clients");
            writableDatabase.execSQL("DROP TABLE IF EXISTS Contacts");
            writableDatabase.execSQL("DROP TABLE IF EXISTS Messages");
        }
    }
    
    public final Client getClient(final String s) {
        final String s2 = ClientDBHandlerClass.clientsColumnNames.stream().collect((Collector<? super Object, ?, String>)Collectors.joining(","));
        final Client client = null;
        Client client2 = null;
        final Cursor cursor = null;
        final Cursor cursor2 = null;
        if (s != null) {
            if (this.countClients(s) != 0) {
                final SQLiteDatabase readableDatabase = this.getReadableDatabase();
                if (readableDatabase == null) {
                    return null;
                }
                Cursor rawQuery = cursor2;
                Client client3 = client;
                Cursor cursor3 = cursor;
                try {
                    try {
                        rawQuery = cursor2;
                        client3 = client;
                        cursor3 = cursor;
                        final StringBuilder sb = new StringBuilder();
                        rawQuery = cursor2;
                        client3 = client;
                        cursor3 = cursor;
                        sb.append(ClientDBHandlerClass.QUERY_CLIENTS_TABLE);
                        rawQuery = cursor2;
                        client3 = client;
                        cursor3 = cursor;
                        sb.append("cid");
                        rawQuery = cursor2;
                        client3 = client;
                        cursor3 = cursor;
                        sb.append(" =? ");
                        rawQuery = cursor2;
                        client3 = client;
                        cursor3 = cursor;
                        final Cursor cursor4 = rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s });
                        client3 = client;
                        cursor3 = cursor4;
                        if (cursor4.moveToFirst()) {
                            rawQuery = cursor4;
                            client3 = client;
                            cursor3 = cursor4;
                            rawQuery = cursor4;
                            client3 = client;
                            cursor3 = cursor4;
                            client2 = new Client(s);
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setEncryptPin(this.cryptPin);
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setCheckPin(cursor4.getBlob(cursor4.getColumnIndex("checkpin")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setRegisterServerIP(cursor4.getString(cursor4.getColumnIndex("rsip")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setXmppUserName(cursor4.getString(cursor4.getColumnIndex("xname")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setXmppServerIP(cursor4.getString(cursor4.getColumnIndex("xsip")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setEncrypted_oAuth2ClientSecret(cursor4.getBlob(cursor4.getColumnIndex("csecret")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setEncrypted_oAuth2AccessToken(cursor4.getBlob(cursor4.getColumnIndex("atok")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setEncrypted_oAuth2RenewToken(cursor4.getBlob(cursor4.getColumnIndex("rtok")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setOAuth2ServerIP(cursor4.getString(cursor4.getColumnIndex("asip")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setOAuth2AccessTokenExpiration(cursor4.getInt(cursor4.getColumnIndex("atokexp")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setOAuth2RenewTokenExpiration(cursor4.getInt(cursor4.getColumnIndex("rtokexp")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setPublicKey(cursor4.getBlob(cursor4.getColumnIndex("pubkey")));
                            rawQuery = cursor4;
                            client3 = client2;
                            cursor3 = cursor4;
                            client2.setEncrypted_privateKey(cursor4.getBlob(cursor4.getColumnIndex("privkey")));
                        }
                        Client client4 = client2;
                        if (cursor4 == null) {
                            return client4;
                        }
                        client4 = client2;
                        if (!cursor4.isClosed()) {
                            cursor3 = cursor4;
                            client4 = client2;
                            cursor3.close();
                            return client4;
                        }
                        return client4;
                    }
                    finally {
                        if (rawQuery != null && !rawQuery.isClosed()) {
                            rawQuery.close();
                        }
                        Client client4 = client3;
                        // iftrue(Label_0721:, cursor3.isClosed())
                        Block_13: {
                            break Block_13;
                            Label_0721: {
                                return client4;
                            }
                        }
                        client4 = client3;
                    }
                }
                catch (Exception ex) {}
            }
        }
        return null;
    }
    
    public final ArrayList<Contact> getContacts(String string) {
        final ArrayList<Contact> list = new ArrayList<Contact>();
        if (string == null) {
            return list;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return list;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_CONTACTS_TABLE);
        sb.append("cid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { string });
        if (rawQuery != null && !rawQuery.isClosed() && rawQuery.getCount() > 0 && rawQuery.moveToFirst()) {
            do {
                string = rawQuery.getString(rawQuery.getColumnIndex("contactid"));
                list.add(new Contact(rawQuery.getString(rawQuery.getColumnIndex("cid")), string));
            } while (rawQuery.moveToNext());
            rawQuery.close();
        }
        return list;
    }
    
    public final ArrayList<Message> getMessages(final String s) {
        final ArrayList<Message> list = new ArrayList<Message>();
        if (s == null) {
            return list;
        }
        final SQLiteDatabase readableDatabase = this.getReadableDatabase();
        if (readableDatabase == null) {
            return list;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(ClientDBHandlerClass.QUERY_MESSAGE_TABLE);
        sb.append("cid");
        sb.append(" =? ");
        final Cursor rawQuery = readableDatabase.rawQuery(sb.toString(), new String[] { s });
        if (rawQuery != null && !rawQuery.isClosed() && rawQuery.getCount() > 0 && rawQuery.moveToFirst()) {
            do {
                final String string = rawQuery.getString(rawQuery.getColumnIndex("contactid"));
                final String string2 = rawQuery.getString(rawQuery.getColumnIndex("cid"));
                final byte[] blob = rawQuery.getBlob(rawQuery.getColumnIndex("msg"));
                boolean b = true;
                if (rawQuery.getInt(rawQuery.getColumnIndex("fromclient")) == 0) {
                    b = false;
                }
                list.add(new Message(string2, string, blob, b));
            } while (rawQuery.moveToNext());
            rawQuery.close();
        }
        return list;
    }
    
    public final Integer nClients() {
        return this.countAllTableRecords("Clients");
    }
    
    public final void onCreate(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ClientDBHandlerClass.CREATE_CLIENTS_TABLE);
        sqLiteDatabase.execSQL(ClientDBHandlerClass.CREATE_CLIENT_CONTACTS);
        sqLiteDatabase.execSQL(ClientDBHandlerClass.CREATE_CLIENT_MESSAGES);
    }
    
    public final void onOpen(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ClientDBHandlerClass.CREATE_CLIENTS_TABLE);
        sqLiteDatabase.execSQL(ClientDBHandlerClass.CREATE_CLIENT_CONTACTS);
        sqLiteDatabase.execSQL(ClientDBHandlerClass.CREATE_CLIENT_MESSAGES);
    }
    
    public final void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        if (n != n2) {
            this.dropAllTables();
            sqLiteDatabase.close();
            this.onCreate(sqLiteDatabase);
        }
    }
}
