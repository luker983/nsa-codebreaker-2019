package com.badguy.terrortime;

import android.app.*;
import org.jivesoftware.smackx.mam.*;
import org.jivesoftware.smackx.vcardtemp.*;
import android.os.*;
import java.util.*;
import android.support.v4.util.*;
import org.jivesoftware.smack.android.*;
import org.bouncycastle.jce.provider.*;
import java.security.*;
import java.lang.ref.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.cert.*;
import org.minidns.dnsserverlookup.android21.*;
import org.jivesoftware.smack.tcp.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.roster.*;
import android.util.*;
import android.content.*;
import android.support.v4.content.*;

public class TerrorTimeApplication extends Application
{
    private static Context mContext;
    private static TerrorTimeApplication mThis;
    private Client mClient;
    private AbstractXMPPConnection mConnection;
    private ContactList mContactList;
    private MamManager mMamManager;
    private ReconnectionManager mReconnectionManager;
    private VCardManager mVcardManager;
    
    public TerrorTimeApplication() {
        this.mConnection = null;
        this.mClient = null;
        this.mContactList = null;
        this.mMamManager = null;
        this.mVcardManager = null;
        this.mReconnectionManager = null;
    }
    
    public static Context getAppContext() {
        return TerrorTimeApplication.mContext;
    }
    
    public static TerrorTimeApplication getInstance() {
        return TerrorTimeApplication.mThis;
    }
    
    public void disconnect() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(final Void... array) {
                if (TerrorTimeApplication.this.mConnection != null) {
                    if (TerrorTimeApplication.this.mConnection.isConnected()) {
                        TerrorTimeApplication.this.mConnection.disconnect();
                    }
                    TerrorTimeApplication.this.mConnection = null;
                }
                TerrorTimeApplication.this.mVcardManager = null;
                TerrorTimeApplication.this.mMamManager = null;
                TerrorTimeApplication.this.mContactList = null;
                TerrorTimeApplication.this.mReconnectionManager = null;
                return null;
            }
        }.execute((Object[])new Void[0]);
    }
    
    public Optional<Client> getClient() {
        return Optional.ofNullable(this.mClient);
    }
    
    public Optional<ContactList> getContactList() {
        return Optional.ofNullable(this.mContactList);
    }
    
    public Optional<MamManager> getMamManager() {
        return Optional.ofNullable(this.mMamManager);
    }
    
    public Optional<ReconnectionManager> getReconnectionManager() {
        return Optional.ofNullable(this.mReconnectionManager);
    }
    
    public Optional<VCardManager> getVCardManager() {
        return Optional.ofNullable(this.mVcardManager);
    }
    
    public Optional<AbstractXMPPConnection> getXMPPTCPConnection() {
        return Optional.ofNullable(this.mConnection);
    }
    
    public void initializeXMPPTCPConnection(final Client client) {
        new XMPPLoginTask(new Pair<TerrorTimeApplication, Client>(this, client)).execute((Object[])new Void[0]);
    }
    
    public void onCreate() {
        super.onCreate();
        TerrorTimeApplication.mThis = this;
        AndroidSmackInitializer.initialize(TerrorTimeApplication.mContext = this.getApplicationContext());
        Security.addProvider(new BouncyCastleProvider());
    }
    
    public static class XMPPLoginTask extends AsyncTask<Void, Integer, Optional<AbstractXMPPConnection>>
    {
        private WeakReference<TerrorTimeApplication> mWeakApp;
        
        public XMPPLoginTask(final Pair<TerrorTimeApplication, Client> pair) {
            pair.first.mClient = pair.second;
            this.mWeakApp = new WeakReference<TerrorTimeApplication>(pair.first);
        }
        
        protected Optional<AbstractXMPPConnection> doInBackground(Void... array) {
            array = this.mWeakApp.get();
            final String[] split = ((TerrorTimeApplication)(Object)array).mClient.getXmppUserName().split("@");
            try {
                ((TerrorTimeApplication)(Object)array).mClient.validateAccessToken(((TerrorTimeApplication)(Object)array).getApplicationContext());
                final XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
                builder.setUsernameAndPassword(split[0], new String(((TerrorTimeApplication)(Object)array).mClient.getOAuth2AccessToken(((TerrorTimeApplication)(Object)array).mClient.getEncryptPin())));
                builder.setResource("chat");
                final String[] split2 = ((TerrorTimeApplication)(Object)array).mClient.getXmppServerIP().split(":");
                int intValue;
                if (split2.length == 2) {
                    intValue = Integer.valueOf(split2[1]);
                }
                else {
                    intValue = 443;
                }
                builder.setHostAddress(InetAddress.getByName(split2[0]));
                builder.setXmppDomain(split[1]);
                builder.setPort(intValue);
                builder.setHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(final String s, final SSLSession sslSession) {
                        return true;
                    }
                });
                builder.setCustomX509TrustManager(new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                    }
                    
                    @Override
                    public void checkServerTrusted(final X509Certificate[] array, final String s) throws CertificateException {
                    }
                    
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                });
                AndroidUsingLinkProperties.setup(TerrorTimeApplication.getAppContext());
                ((TerrorTimeApplication)(Object)array).mConnection = new XMPPTCPConnection(builder.build());
                ((TerrorTimeApplication)(Object)array).mConnection.setReplyTimeout(30000L);
                ((TerrorTimeApplication)(Object)array).mVcardManager = VCardManager.getInstanceFor(((TerrorTimeApplication)(Object)array).mConnection);
                ((TerrorTimeApplication)(Object)array).mContactList = new ContactList(Roster.getInstanceFor(((TerrorTimeApplication)(Object)array).mConnection), ((TerrorTimeApplication)(Object)array).mClient);
                ((TerrorTimeApplication)(Object)array).mReconnectionManager = ReconnectionManager.getInstanceFor(((TerrorTimeApplication)(Object)array).mConnection);
                ((TerrorTimeApplication)(Object)array).mReconnectionManager.enableAutomaticReconnection();
                ((TerrorTimeApplication)(Object)array).mConnection.connect();
                ((TerrorTimeApplication)(Object)array).mConnection.login();
                ((TerrorTimeApplication)(Object)array).mMamManager = MamManager.getInstanceFor(((TerrorTimeApplication)(Object)array).mConnection);
                if (!((TerrorTimeApplication)(Object)array).mMamManager.isSupported()) {
                    Log.d("MAM", "not supported");
                    ((TerrorTimeApplication)(Object)array).mMamManager = null;
                }
                else {
                    ((TerrorTimeApplication)(Object)array).mClient.setMessageList(MamHelper.getMessageArchive());
                }
                ((TerrorTimeApplication)(Object)array).mClient.addPublicKeys(VCardHelper.getPublicKeys(((TerrorTimeApplication)(Object)array).mClient.getXmppUserName()));
                return Optional.ofNullable(((TerrorTimeApplication)(Object)array).mConnection);
            }
            catch (Exception ex) {
                Log.e("EXCEPTION", "Error during xmpp connection setup", (Throwable)ex);
                if (((TerrorTimeApplication)(Object)array).mConnection != null && ((TerrorTimeApplication)(Object)array).mConnection.isConnected()) {
                    ((TerrorTimeApplication)(Object)array).disconnect();
                }
                return Optional.empty();
            }
        }
        
        protected void onPostExecute(final Optional<AbstractXMPPConnection> optional) {
            final Intent intent = new Intent();
            intent.setAction("XMPP_INITIALIZE");
            intent.putExtra("result", optional != null && optional.isPresent());
            Log.d("xmpptask", "sending initialize");
            LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).sendBroadcast(intent);
        }
    }
}
