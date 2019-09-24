package com.badguy.terrortime;

import java.security.*;
import java.util.function.*;
import android.util.*;
import android.support.v7.app.*;
import android.widget.*;
import java.util.*;
import android.support.v4.content.*;
import android.content.*;
import android.view.*;
import android.os.*;
import java.lang.ref.*;
import android.view.inputmethod.*;

public class LoginActivity extends AppCompatActivity
{
    private EditText mChatUserNameField;
    private Button mLoginButton;
    private BroadcastReceiver mLoginReceiver;
    private UserLoginTask mLoginTask;
    private EditText mPinField;
    private ProgressBar mProgressBar;
    
    public LoginActivity() {
        this.mLoginTask = null;
        this.mChatUserNameField = null;
        this.mPinField = null;
        this.mLoginButton = null;
        this.mProgressBar = null;
        this.mLoginReceiver = null;
    }
    
    private void execTask() {
        if (this.mLoginTask != null) {
            return;
        }
        (this.mLoginTask = new UserLoginTask(this)).execute((Object[])new Void[] { null });
    }
    
    private void launchContactActivityAndFinish() {
        this.startActivity(new Intent((Context)this, (Class)ContactActivity.class));
        this.finish();
    }
    
    private void savePublicKeyToVCard(final Client client) {
        final boolean b = false;
        boolean b2 = false;
        try {
            VCardHelper.savePublicKey(client.getRsaPublicKey().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$LoginActivity$YcawHEfealIw5425nWdRe1UadVY.INSTANCE));
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Public key error", t);
            b2 = b;
        }
        if (!b2) {
            this.showAlertAndFinishActivity("Unable to save public key to server");
        }
    }
    
    private void showAlertAndFinishActivity(final String message) {
        new AlertDialog.Builder((Context)this).setTitle("ERROR").setMessage(message).setCancelable(false).setPositiveButton("Close", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                LoginActivity.this.startActivity(new Intent((Context)LoginActivity.this, (Class)MainActivity.class));
                LoginActivity.this.finish();
            }
        }).create().show();
    }
    
    private boolean validateFields(final Map<EditText, String> map) {
        final boolean b = true;
        final ParameterValidatorClass parameterValidatorClass = new ParameterValidatorClass();
        this.mChatUserNameField.setError((CharSequence)null);
        this.mPinField.setError((CharSequence)null);
        final Iterator<EditText> iterator = map.keySet().iterator();
        boolean b2;
        while (true) {
            b2 = b;
            if (!iterator.hasNext()) {
                break;
            }
            final EditText editText = iterator.next();
            try {
                final String s = map.get(editText);
                if (editText == null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Fatal Error: ");
                    sb.append(s);
                    sb.append(" was null.");
                    throw new RuntimeException(sb.toString());
                }
                final String string = editText.getText().toString();
                if (string.isEmpty()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(s);
                    sb2.append(": ");
                    sb2.append(this.getString(2131624004));
                    editText.setError((CharSequence)sb2.toString());
                    b2 = false;
                    break;
                }
                if (editText == this.mChatUserNameField && !parameterValidatorClass.isValidUserName(string)) {
                    editText.setError((CharSequence)this.getString(2131624009));
                    b2 = false;
                    break;
                }
                if (editText == this.mPinField && !parameterValidatorClass.isValidPin(string)) {
                    editText.setError((CharSequence)this.getString(2131624007));
                    b2 = false;
                    break;
                }
                continue;
            }
            catch (Exception ex) {
                Log.e("EXCEPTION LOG", "validateFields: ", (Throwable)ex);
                Toast.makeText(this.getApplicationContext(), (CharSequence)ex.getMessage(), 1).show();
            }
        }
        return b2;
    }
    
    public final void launchSettingsActivity(final String s, final String s2) {
        final Intent intent = new Intent((Context)this, (Class)SettingsActivity.class);
        intent.putExtra("pin", s);
        intent.putExtra("name", s2);
        this.startActivity(intent);
    }
    
    @Override
    protected final void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427358);
        this.mChatUserNameField = this.findViewById(2131230824);
        this.mPinField = this.findViewById(2131230830);
        this.mLoginButton = this.findViewById(2131230827);
        (this.mProgressBar = this.findViewById(2131230866)).setVisibility(4);
        final HashMap<EditText, String> hashMap = new HashMap<EditText, String>();
        hashMap.put(this.mChatUserNameField, "chatUserNameField");
        hashMap.put(this.mPinField, "pinField");
        this.mLoginReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                LoginActivity.this.mProgressBar.setVisibility(8);
                try {
                    if (!intent.getBooleanExtra("result", false)) {
                        throw new Exception("Connection failed");
                    }
                    LoginActivity.this.savePublicKeyToVCard(((TerrorTimeApplication)LoginActivity.this.getApplication()).getClient().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$LoginActivity$1$nC2Mlzaksg0IlR_njHSIBo11qtU.INSTANCE));
                    LoginActivity.this.launchContactActivityAndFinish();
                }
                finally {
                    LoginActivity.this.showAlertAndFinishActivity("Unable to connect to chat server");
                }
            }
        };
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).registerReceiver(this.mLoginReceiver, new IntentFilter("XMPP_INITIALIZE"));
        this.mLoginButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(View iterator) {
                LoginActivity.this.mLoginTask = null;
                if (LoginActivity.this.validateFields(hashMap)) {
                    final Iterator<EditText> iterator2 = hashMap.keySet().iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().setError((CharSequence)null);
                    }
                    LoginActivity.this.execTask();
                    for (final EditText editText : hashMap.keySet()) {
                        if (editText.getError() != null) {
                            editText.requestFocus();
                            Toast.makeText(LoginActivity.this.getApplicationContext(), (CharSequence)editText.getError().toString(), 1).show();
                            break;
                        }
                    }
                }
                else {
                    iterator = (View)hashMap.keySet().iterator();
                    while (((Iterator)iterator).hasNext()) {
                        final EditText editText2 = ((Iterator<EditText>)iterator).next();
                        try {
                            if (editText2.getError() != null) {
                                editText2.requestFocus();
                                Toast.makeText(LoginActivity.this.getApplicationContext(), (CharSequence)editText2.getError().toString(), 1).show();
                                break;
                            }
                            continue;
                        }
                        catch (Exception ex) {
                            Log.e("EXCEPTION LOG", "setOnClickListener: ", (Throwable)ex);
                            Toast.makeText(LoginActivity.this.getApplicationContext(), (CharSequence)ex.getMessage(), 1).show();
                        }
                    }
                }
            }
        });
    }
    
    @Override
    protected final void onDestroy() {
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).unregisterReceiver(this.mLoginReceiver);
        final UserLoginTask mLoginTask = this.mLoginTask;
        if (mLoginTask != null) {
            mLoginTask.cancel(true);
        }
        super.onDestroy();
    }
    
    public static class UserLoginTask extends AsyncTask<Void, Integer, Client>
    {
        private WeakReference<LoginActivity> activityWeakReference;
        private Client client;
        private ClientDBHandlerClass clientDB;
        private String errorMsg;
        private boolean success;
        
        UserLoginTask(final LoginActivity loginActivity) {
            this.errorMsg = "";
            this.success = true;
            this.activityWeakReference = new WeakReference<LoginActivity>(loginActivity);
            final LoginActivity loginActivity2 = this.activityWeakReference.get();
            final EditText editText = loginActivity2.findViewById(2131230824);
            final EditText editText2 = loginActivity2.findViewById(2131230830);
            this.client = null;
            try {
                this.clientDB = ClientDBHandlerClass.getInstance(editText2.getText().toString(), loginActivity.getApplicationContext());
                if (this.clientDB == null) {
                    this.success = false;
                    this.errorMsg = "Unknown application database error. Could not connect to database.";
                    Log.e("ERROR", "Failed to open client database. clientDB was null.");
                }
                else {
                    this.client = this.clientDB.getClient(editText.getText().toString());
                    Log.d("LoginActivity", "Connected to client database successfully.");
                }
                if (this.success && this.client == null) {
                    this.success = false;
                    this.errorMsg = "Check client id and pin.";
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Bad Client id: ");
                    sb.append(editText.getText().toString());
                    Log.e("LoginActivity", sb.toString());
                }
                else if (this.success) {
                    this.client.setEncryptPin(editText2.getText().toString());
                }
            }
            catch (Exception ex) {
                Log.e("ERROR", "UserLoginTask: ", (Throwable)ex);
                if (this.errorMsg.isEmpty()) {
                    this.errorMsg = "Check client id and pin.";
                }
                this.success = false;
            }
            if (!this.success) {
                this.cancel(true);
            }
        }
        
        protected final Client doInBackground(final Void... array) {
            final LoginActivity loginActivity = this.activityWeakReference.get();
            try {
                if (this.success && this.client == null) {
                    this.success = false;
                    this.errorMsg = "Unknown error: Null Client";
                    Log.e("ERROR", "LoginActivity: Null client in UserLoginTask background thread");
                }
                else if (this.success) {
                    this.client.generateSymmetricKey();
                    this.client.validateAccessToken(loginActivity.getApplicationContext());
                    Log.d("LOGINACTIVITY", "Token request successful. ");
                }
            }
            catch (Exception ex) {
                this.success = false;
                if (this.errorMsg.isEmpty()) {
                    this.errorMsg = "Check client id and pin.";
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("LoginActivitiy: ");
                sb.append(ex.getMessage());
                Log.e("ERROR", sb.toString());
            }
            Log.d("LoginActivity", "Login background thread success.");
            return this.client;
        }
        
        protected final void onCancelled() {
            super.onCancelled();
            final LoginActivity loginActivity = this.activityWeakReference.get();
            this.success = false;
            if (this.errorMsg.length() == 0) {
                this.errorMsg = "Login failed for unknown reason.";
            }
            Log.v("LoginActivity", "Login cancelled");
            final ClientDBHandlerClass clientDB = this.clientDB;
            if (clientDB != null) {
                clientDB.close();
            }
            loginActivity.mChatUserNameField.getText().clear();
            loginActivity.mPinField.getText().clear();
            loginActivity.mChatUserNameField.setError((CharSequence)null);
            loginActivity.mPinField.setError((CharSequence)null);
            final StringBuilder sb = new StringBuilder();
            sb.append("Login failed. Select OK to close window. ");
            sb.append(this.errorMsg);
            loginActivity.showAlertAndFinishActivity(sb.toString());
        }
        
        protected final void onPostExecute(final Client client) {
            super.onPostExecute((Object)client);
            final LoginActivity loginActivity = this.activityWeakReference.get();
            if (!this.isCancelled() && client != null) {
                try {
                    this.clientDB.addOrUpdateClient(client);
                }
                catch (Exception ex) {
                    this.success = false;
                    this.errorMsg = "Client Database Error";
                }
                final ClientDBHandlerClass clientDB = this.clientDB;
                if (clientDB != null) {
                    clientDB.close();
                }
            }
            else {
                final ClientDBHandlerClass clientDB2 = this.clientDB;
                if (clientDB2 != null) {
                    clientDB2.close();
                }
            }
            if (!this.success) {
                loginActivity.mProgressBar.setVisibility(8);
                final StringBuilder sb = new StringBuilder();
                sb.append("Login failed. Select OK to close window. ");
                sb.append(this.errorMsg);
                loginActivity.showAlertAndFinishActivity(sb.toString());
            }
            else {
                ((TerrorTimeApplication)loginActivity.getApplication()).initializeXMPPTCPConnection(client);
            }
        }
        
        protected final void onPreExecute() {
            super.onPreExecute();
            final LoginActivity loginActivity = this.activityWeakReference.get();
            final View currentFocus = loginActivity.getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager)loginActivity.getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
            if (!this.isCancelled() && this.success) {
                loginActivity.mLoginButton.setEnabled(false);
                loginActivity.mProgressBar.setVisibility(0);
            }
        }
        
        protected void onProgressUpdate(final Integer... array) {
        }
    }
}
