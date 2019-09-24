package com.badguy.terrortime;

import java.util.*;
import org.jivesoftware.smack.chat2.*;
import org.jxmpp.jid.*;
import android.widget.*;
import android.os.*;
import java.util.function.*;
import android.support.v7.widget.*;
import android.support.v4.content.*;
import android.view.*;
import org.jivesoftware.smack.*;
import android.util.*;
import android.support.v7.app.*;
import android.content.*;

public class ChatActivity extends AppCompatActivity
{
    private List<Message> mAdapterMessageList;
    private Chat mChat;
    private ChatManager mChatManager;
    private Client mClient;
    private AbstractXMPPConnection mConnection;
    private Jid mContactJid;
    private ContactList mContactList;
    private BroadcastReceiver mContactReceiver;
    private MessageListAdapter mMessageAdapter;
    private RecyclerView mMessageRecycler;
    private Button mSend;
    private EditText mSendText;
    
    public ChatActivity() {
        this.mMessageRecycler = null;
        this.mMessageAdapter = null;
        this.mConnection = null;
        this.mChatManager = null;
        this.mChat = null;
        this.mSend = null;
        this.mSendText = null;
        this.mAdapterMessageList = null;
        this.mClient = null;
        this.mContactList = null;
        this.mContactJid = null;
        this.mContactReceiver = null;
    }
    
    @Override
    protected final void onCreate(Bundle stringExtra) {
        super.onCreate(stringExtra);
        final Intent intent = this.getIntent();
        stringExtra = (Bundle)intent.getStringExtra("jid");
        this.mAdapterMessageList = (List<Message>)intent.getParcelableArrayListExtra("messages");
        this.setTitle((CharSequence)stringExtra);
        this.setContentView(2131427356);
        final TerrorTimeApplication terrorTimeApplication = (TerrorTimeApplication)this.getApplication();
        try {
            this.mConnection = terrorTimeApplication.getXMPPTCPConnection().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ChatActivity$Ksr1_XlDga0IT16Tq4Y_QHv_4QI.INSTANCE);
            this.mClient = terrorTimeApplication.getClient().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ChatActivity$8yUyKQwn0DFL8EEckDr_dCx6pB0.INSTANCE);
            this.mContactList = terrorTimeApplication.getContactList().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ChatActivity$QoDALnkTLgb2ljruKgBNgrQWrHA.INSTANCE);
            this.mContactJid = this.mContactList.getJidFromString((String)stringExtra).orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ChatActivity$LrcnlbksNlG_WUc_CyR_VLf7Bww.INSTANCE);
            this.mChatManager = ChatManager.getInstanceFor(this.mConnection);
            if (this.mChatManager == null) {
                throw new Exception("Chat manager is null");
            }
            this.mMessageRecycler = this.findViewById(2131230871);
            this.mMessageAdapter = new MessageListAdapter((Context)this, this.mAdapterMessageList);
            this.mMessageRecycler.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)this));
            this.mMessageRecycler.setAdapter((RecyclerView.Adapter)this.mMessageAdapter);
            this.mChat = this.mChatManager.chatWith(this.mContactJid.asEntityBareJidIfPossible());
            this.mSend = this.findViewById(2131230758);
            this.mSendText = this.findViewById(2131230788);
            this.mMessageRecycler.smoothScrollToPosition(this.mAdapterMessageList.size());
            this.mContactReceiver = new BroadcastReceiver() {
                public void onReceive(final Context context, final Intent intent) {
                    final Message message = (Message)intent.getParcelableExtra("message");
                    if (message.getContactId().equals(ChatActivity.this.mContactJid.asBareJid().toString())) {
                        ChatActivity.this.mAdapterMessageList.add(message);
                        ChatActivity.this.mMessageAdapter.notifyItemInserted(ChatActivity.this.mAdapterMessageList.size());
                        ChatActivity.this.mMessageRecycler.smoothScrollToPosition(ChatActivity.this.mAdapterMessageList.size());
                    }
                }
            };
            LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).registerReceiver(this.mContactReceiver, new IntentFilter("XMPP_CHAT_MESSAGE"));
            this.mSend.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(View string) {
                    string = (InterruptedException)ChatActivity.this.mSendText.getText().toString();
                    try {
                        ChatActivity.this.mChat.send((CharSequence)string);
                        ChatActivity.this.mSendText.getText().clear();
                        return;
                    }
                    catch (InterruptedException string) {}
                    catch (SmackException.NotConnectedException ex) {}
                    Log.e("EXCEPTION", "Unable to send message", (Throwable)string);
                    new AlertDialog.Builder((Context)ChatActivity.this).setTitle("ERROR").setMessage("Unable to send message. Please check your connection and try again.").setCancelable(false).setPositiveButton("Continue", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                        }
                    }).create().show();
                }
            });
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Error getting connection info", t);
            final AlertDialog.Builder setTitle = new AlertDialog.Builder((Context)this).setTitle("ERROR");
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to setup chat with ");
            sb.append((String)stringExtra);
            setTitle.setMessage(sb.toString()).setCancelable(false).setPositiveButton("Continue", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    ChatActivity.this.finish();
                }
            }).create().show();
        }
    }
    
    public void onDestroy() {
        final Intent intent = new Intent();
        intent.setAction("XMPP_CHAT_CLOSED");
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).sendBroadcast(intent);
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).unregisterReceiver(this.mContactReceiver);
        this.mMessageAdapter = null;
        this.mConnection = null;
        this.mChatManager = null;
        this.mChat = null;
        this.mSend = null;
        this.mAdapterMessageList = null;
        this.mClient = null;
        this.mContactList = null;
        this.mContactJid = null;
        this.mContactReceiver = null;
        super.onDestroy();
    }
}
