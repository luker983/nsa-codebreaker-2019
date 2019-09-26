package com.badguy.terrortime;

import android.support.v7.widget.*;
import java.util.function.*;
import android.support.v4.content.*;
import java.io.*;
import android.util.*;
import android.support.v7.app.*;
import android.content.*;
import java.util.*;
import java.util.stream.*;
import android.view.*;
import android.os.*;
import org.jxmpp.jid.*;
import org.jivesoftware.smack.chat2.*;
import android.widget.*;
import org.jivesoftware.smack.*;

public class ContactActivity extends AppCompatActivity
{
    private ChatManager mChatManager;
    private BroadcastReceiver mChatReceiver;
    private Client mClient;
    private List<Message> mClientMessageList;
    private AbstractXMPPConnection mConnection;
    private TerrorTimeConnectionListener mConnectionListener;
    private ContactList mContactList;
    private ContactListAdapter mContactListAdapter;
    private List<String> mContactNames;
    private BroadcastReceiver mContactReceiver;
    private String mCurrentChatJid;
    private IncomingChatMessageListener mIncomingListener;
    private HashMap<String, ArrayList<Message>> mMessageMap;
    private OutgoingChatMessageListener mOutgoingListener;
    private TerrorTimeReconnectionListener mReconnectionListener;
    private ReconnectionManager mReconnectionManager;
    
    public ContactActivity() {
        this.mConnection = null;
        this.mContactList = null;
        this.mContactNames = null;
        this.mClient = null;
        this.mClientMessageList = null;
        this.mMessageMap = null;
        this.mOutgoingListener = null;
        this.mIncomingListener = null;
        this.mChatManager = null;
        this.mContactListAdapter = null;
        this.mCurrentChatJid = null;
        this.mContactReceiver = null;
        this.mChatReceiver = null;
        this.mReconnectionManager = null;
        this.mReconnectionListener = null;
        this.mConnectionListener = null;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427357);
        this.setSupportActionBar(this.findViewById(2131230943));
        final TerrorTimeApplication terrorTimeApplication = (TerrorTimeApplication)this.getApplication();
        final ListView listView = this.findViewById(2131230722);
        this.mMessageMap = new HashMap<String, ArrayList<Message>>();
        try {
            this.mConnection = terrorTimeApplication.getXMPPTCPConnection().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ContactActivity$D4F499BgZVEklXb226uaj3IXBnI.INSTANCE);
            this.mReconnectionManager = terrorTimeApplication.getReconnectionManager().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ContactActivity$_90Q2hWxX0EVjE2LXELHIYLAKCs.INSTANCE);
            this.mContactList = terrorTimeApplication.getContactList().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ContactActivity$qVLZd6Duq1_UMne65D4aTeBipKc.INSTANCE);
            this.mClient = terrorTimeApplication.getClient().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ContactActivity$_ZcPEsTSp8t6NQjXBYyfGzNOO_Y.INSTANCE);
            this.mChatManager = ChatManager.getInstanceFor(this.mConnection);
            if (this.mChatManager == null) {
                throw new Exception("Chat manager is null");
            }
            this.mConnectionListener = new TerrorTimeConnectionListener();
            this.mConnection.addConnectionListener(this.mConnectionListener);
            this.mReconnectionListener = new TerrorTimeReconnectionListener();
            this.mReconnectionManager.addReconnectionListener(this.mReconnectionListener);
            this.mContactNames = this.mContactList.getContactNames();
            final Iterator<Jid> iterator = this.mContactList.getContactJids().iterator();
            while (iterator.hasNext()) {
                this.mMessageMap.computeIfAbsent(iterator.next().asBareJid().toString(), (Function<? super String, ? extends ArrayList<Message>>)_$$Lambda$ContactActivity$Abp1UWZl8642CTt7XId3R1NFrZ4.INSTANCE);
            }
            this.mClientMessageList = this.mClient.getMessageList();
            final Stream<Object> stream = this.mClientMessageList.stream().map((Function<? super Object, ?>)_$$Lambda$ContactActivity$Y__yy_3w9K5VEeJUPl9He0_AkV0.INSTANCE).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).stream();
            final Client mClient = this.mClient;
            mClient.getClass();
            stream.filter(new _$$Lambda$Ftf3U58d3DozcaQ_VdP0FmeJP4A(mClient)).forEach(new _$$Lambda$ContactActivity$Oj_gvTG7mtFmhx5tGcieEtbR1Qg(this));
            (this.mContactListAdapter = new ContactListAdapter((Context)this, 2131427362, 2131230770, this.mContactNames, this.mContactList.getAvailabilityMap(), this.mMessageMap, this.mContactList)).setNotifyOnChange(true);
            listView.setAdapter((ListAdapter)this.mContactListAdapter);
            this.mOutgoingListener = new ChatOutgoingMessageListener();
            this.mIncomingListener = new ChatIncomingMessageListener();
            this.mChatManager.addOutgoingListener(this.mOutgoingListener);
            this.mChatManager.addIncomingListener(this.mIncomingListener);
            this.mChatReceiver = new BroadcastReceiver() {
                public void onReceive(final Context context, final Intent intent) {
                    ContactActivity.this.mCurrentChatJid = null;
                }
            };
            LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).registerReceiver(this.mChatReceiver, new IntentFilter("XMPP_CHAT_CLOSED"));
            this.mContactReceiver = new BroadcastReceiver() {
                public void onReceive(final Context context, final Intent intent) {
                    ContactActivity.this.mContactListAdapter.notifyDataSetChanged();
                }
            };
            LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).registerReceiver(this.mContactReceiver, new IntentFilter("XMPP_CONTACTS_CHANGED"));
            listView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                    try {
                        ContactActivity.this.mCurrentChatJid = ContactActivity.this.mContactList.getJidAtIndex(n).orElseThrow((Supplier<? extends Throwable>)new _$$Lambda$ContactActivity$3$7ZxwTSnCYivpey1cpUtKDC6jPJQ(n)).asBareJid().toString();
                        final ArrayList list = ContactActivity.this.mMessageMap.computeIfAbsent(ContactActivity.this.mCurrentChatJid, (Function<? super String, ? extends ArrayList>)_$$Lambda$ContactActivity$3$IoGMCdBuM1l5Yod757VGhOYsa7U.INSTANCE);
                        final Intent intent = new Intent((Context)ContactActivity.this, (Class)ChatActivity.class);
                        intent.putExtra("jid", ContactActivity.this.mCurrentChatJid);
                        intent.putExtra("messages", (Serializable)list);
                        ContactActivity.this.startActivity(intent);
                        final TextView textView = (TextView)view.findViewById(2131230837);
                        final ImageView imageView = (ImageView)view.findViewById(2131230847);
                        textView.setVisibility(4);
                        imageView.setVisibility(4);
                        ContactActivity.this.mContactListAdapter.updateCount(ContactActivity.this.mCurrentChatJid, list.size());
                    }
                    finally {
                        final Throwable t;
                        Log.e("EXCEPTION", "Unable to load chat activity", t);
                        new AlertDialog.Builder((Context)ContactActivity.this).setTitle("ERROR").setMessage("Unable to load chat window. Please try again.").setCancelable(false).setPositiveButton("Continue", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                            }
                        }).create().show();
                    }
                }
            });
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Unable to get connection info", t);
            new AlertDialog.Builder((Context)this).setTitle("ERROR").setMessage("Unable to setup contact list").setCancelable(false).setPositiveButton("Continue", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    ContactActivity.this.startActivity(new Intent((Context)ContactActivity.this, (Class)LoginActivity.class));
                    ContactActivity.this.finish();
                }
            }).create().show();
        }
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131492864, menu);
        return true;
    }
    
    public void onDestroy() {
        final ChatManager mChatManager = this.mChatManager;
        if (mChatManager != null) {
            final IncomingChatMessageListener mIncomingListener = this.mIncomingListener;
            if (mIncomingListener != null && this.mOutgoingListener != null) {
                mChatManager.removeIncomingListener(mIncomingListener);
                this.mChatManager.removeOutgoingListener(this.mOutgoingListener);
            }
        }
        final AbstractXMPPConnection mConnection = this.mConnection;
        if (mConnection != null) {
            final TerrorTimeConnectionListener mConnectionListener = this.mConnectionListener;
            if (mConnectionListener != null) {
                mConnection.removeConnectionListener(mConnectionListener);
            }
        }
        final ReconnectionManager mReconnectionManager = this.mReconnectionManager;
        if (mReconnectionManager != null) {
            final TerrorTimeReconnectionListener mReconnectionListener = this.mReconnectionListener;
            if (mReconnectionListener != null) {
                mReconnectionManager.removeReconnectionListener(mReconnectionListener);
            }
        }
        LocalBroadcastManager.getInstance(this.getApplicationContext()).unregisterReceiver(this.mChatReceiver);
        LocalBroadcastManager.getInstance(this.getApplicationContext()).unregisterReceiver(this.mContactReceiver);
        this.mConnectionListener = null;
        this.mReconnectionManager = null;
        this.mReconnectionListener = null;
        this.mChatReceiver = null;
        this.mContactReceiver = null;
        this.mContactList = null;
        this.mContactNames = null;
        this.mClient = null;
        this.mClientMessageList = null;
        this.mMessageMap = null;
        this.mOutgoingListener = null;
        this.mIncomingListener = null;
        this.mChatManager = null;
        this.mContactListAdapter = null;
        this.mCurrentChatJid = null;
        final AbstractXMPPConnection mConnection2 = this.mConnection;
        if (mConnection2 != null && mConnection2.isConnected()) {
            ((TerrorTimeApplication)this.getApplication()).disconnect();
        }
        super.onDestroy();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() != 2131230832) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.startActivity(new Intent((Context)this, (Class)LoginActivity.class));
        this.finish();
        return true;
    }
    
    public void sendChatBroadcast(final String s, final Message message) {
        this.mMessageMap.compute(s, new _$$Lambda$ContactActivity$x1GFCQmzoSl98GjK3ZI8stGf8ns(message));
        final Intent intent = new Intent();
        intent.setAction("XMPP_CHAT_MESSAGE");
        intent.putExtra("message", (Parcelable)message);
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).sendBroadcast(intent);
        final Intent intent2 = new Intent();
        intent2.setAction("XMPP_CONTACTS_CHANGED");
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).sendBroadcast(intent2);
    }
    
    private class ChatIncomingMessageListener implements IncomingChatMessageListener
    {
        @Override
        public void newIncomingMessage(final EntityBareJid entityBareJid, final org.jivesoftware.smack.packet.Message message, final Chat chat) {
            ContactActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Message message = new Message(ContactActivity.this.mClient.getXmppUserName(), entityBareJid.asBareJid().toString(), message.getBody().getBytes(), false);
                    if (ContactActivity.this.mCurrentChatJid != null && entityBareJid.asBareJid().toString().equals(ContactActivity.this.mCurrentChatJid)) {
                        ContactActivity.this.mContactListAdapter.incrementCount(ContactActivity.this.mCurrentChatJid);
                    }
                    if (ContactActivity.this.mClient.decryptMessage(message)) {
                        ContactActivity.this.mClientMessageList.add(new Message(ContactActivity.this.mClient.getXmppUserName(), entityBareJid.asBareJid().toString(), message.getBody().getBytes(), false));
                    }
                    else {
                        ContactActivity.this.mClientMessageList.add(message);
                    }
                    ContactActivity.this.sendChatBroadcast(entityBareJid.asBareJid().toString(), message);
                }
            });
        }
    }
    
    private class ChatOutgoingMessageListener implements OutgoingChatMessageListener
    {
        @Override
        public void newOutgoingMessage(final EntityBareJid entityBareJid, final org.jivesoftware.smack.packet.Message message, final Chat chat) {
            ContactActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Message message = new Message(ContactActivity.this.mClient.getXmppUserName(), entityBareJid.asBareJid().toString(), message.getBody().getBytes(), true);
                    if (ContactActivity.this.mCurrentChatJid != null && entityBareJid.asBareJid().toString().equals(ContactActivity.this.mCurrentChatJid)) {
                        ContactActivity.this.mContactListAdapter.incrementCount(ContactActivity.this.mCurrentChatJid);
                    }
                    Message message2 = new Message(ContactActivity.this.mClient.getXmppUserName(), entityBareJid.asBareJid().toString(), message.getBody().getBytes(), true);
                    if (!ContactActivity.this.mClient.encryptMessage(message)) {
                        message2 = message;
                    }
                    ContactActivity.this.sendChatBroadcast(entityBareJid.asBareJid().toString(), message2);
                    ContactActivity.this.mClientMessageList.add(message);
                    message.setBody(new String(message.getContent()));
                }
            });
        }
    }
    
    private class TerrorTimeConnectionListener implements ConnectionListener
    {
        @Override
        public void authenticated(final XMPPConnection xmppConnection, final boolean b) {
            Log.d("connection_status", "authenticated");
        }
        
        @Override
        public void connected(final XMPPConnection xmppConnection) {
            Log.d("connection_status", "connected");
        }
        
        @Override
        public void connectionClosed() {
            Log.d("connection_status", "closed");
        }
        
        @Override
        public void connectionClosedOnError(final Exception ex) {
            Log.e("connection_status", "closed on error ", (Throwable)ex);
            ContactActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Toast text = Toast.makeText(ContactActivity.this.getApplicationContext(), (CharSequence)"Lost connection to TerrorTime server.", 1);
                    final Exception val$e = ex;
                    Toast text2 = text;
                    if (val$e instanceof XMPPException.StreamErrorException) {
                        text2 = text;
                        if (((XMPPException.StreamErrorException)val$e).getStreamError().getCondition().name().equals("conflict")) {
                            text2 = Toast.makeText(ContactActivity.this.getApplicationContext(), (CharSequence)"Disconnected from TerrorTime. Only one device may be signed in at a time.", 1);
                            ContactActivity.this.startActivity(new Intent((Context)ContactActivity.this, (Class)LoginActivity.class));
                            ContactActivity.this.finish();
                        }
                    }
                    text2.setGravity(17, 0, 0);
                    text2.show();
                }
            });
        }
    }
    
    private class TerrorTimeReconnectionListener implements ReconnectionListener
    {
        @Override
        public void reconnectingIn(final int n) {
            ContactActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Context applicationContext = ContactActivity.this.getApplicationContext();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Reconnecting in ");
                    sb.append(n);
                    sb.append(" seconds.");
                    final Toast text = Toast.makeText(applicationContext, (CharSequence)sb.toString(), 1);
                    text.setGravity(17, 0, 0);
                    text.show();
                }
            });
        }
        
        @Override
        public void reconnectionFailed(final Exception ex) {
            ContactActivity.this.mReconnectionManager.disableAutomaticReconnection();
            ContactActivity.this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Toast text = Toast.makeText(ContactActivity.this.getApplicationContext(), (CharSequence)"Unable to reconnect to TerrorTime. Returning to login screen.", 1);
                    text.setGravity(17, 0, 0);
                    text.show();
                    ContactActivity.this.startActivity(new Intent((Context)ContactActivity.this, (Class)LoginActivity.class));
                    ContactActivity.this.finish();
                }
            });
        }
    }
}
