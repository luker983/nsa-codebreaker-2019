package com.badguy.terrortime;

import android.util.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.support.v7.app.*;
import android.content.*;
import android.os.*;
import java.lang.ref.*;
import android.view.inputmethod.*;

public class RegisterActivity extends AppCompatActivity
{
    private boolean cancel;
    private ClientDBHandlerClass clientDB;
    TextView mCancelView;
    private EditText mChatUserNameField;
    private EditText mOAUTH2ServerIPAddrField;
    private EditText mPasswordField;
    private EditText mPinField;
    private ProgressBar mProgressBar;
    Button mRegisterButton;
    private View mRegisterFormView;
    private EditText mXMPPServerIPAddrField;
    
    public RegisterActivity() {
        this.clientDB = null;
        this.mXMPPServerIPAddrField = null;
        this.mOAUTH2ServerIPAddrField = null;
        this.mChatUserNameField = null;
        this.mPasswordField = null;
        this.mPinField = null;
        this.mProgressBar = null;
        this.mRegisterButton = null;
        this.mCancelView = null;
        this.mRegisterFormView = null;
        this.cancel = false;
    }
    
    private void registerNewTerrorist() throws Exception {
        if (this.clientDB != null) {
            this.mProgressBar.setVisibility(0);
            new KeyGenerationTask(this).execute((Object[])new Void[] { null });
            return;
        }
        throw new RuntimeException("Failed to connect to Client database. Try again or select \"cancel\".");
    }
    
    private boolean validateFields(final Map<EditText, String> map) {
        final boolean b = true;
        final ParameterValidatorClass parameterValidatorClass = new ParameterValidatorClass();
        this.mXMPPServerIPAddrField.setError((CharSequence)null);
        this.mChatUserNameField.setError((CharSequence)null);
        this.mPasswordField.setError((CharSequence)null);
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
                if (editText == this.mXMPPServerIPAddrField && !parameterValidatorClass.isValidIpAddress(string)) {
                    editText.setError((CharSequence)this.getString(2131624008));
                    b2 = false;
                    break;
                }
                if (editText == this.mOAUTH2ServerIPAddrField && !parameterValidatorClass.isValidIpAddress(string)) {
                    editText.setError((CharSequence)this.getString(2131624008));
                    b2 = false;
                    break;
                }
                if (editText == this.mChatUserNameField && !parameterValidatorClass.isValidUserName(string)) {
                    editText.setError((CharSequence)this.getString(2131624009));
                    b2 = false;
                    break;
                }
                if (editText == this.mPasswordField && !parameterValidatorClass.isValidPassword(string)) {
                    editText.setError((CharSequence)this.getString(2131624006));
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
    
    @Override
    protected final void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427360);
        this.cancel = false;
        this.mXMPPServerIPAddrField = this.findViewById(2131230965);
        this.mOAUTH2ServerIPAddrField = this.findViewById(2131230853);
        this.mChatUserNameField = this.findViewById(2131230957);
        this.mPasswordField = this.findViewById(2131230861);
        this.mPinField = this.findViewById(2131230864);
        this.mRegisterFormView = this.findViewById(2131230876);
        this.mCancelView = this.findViewById(2131230875);
        this.mRegisterButton = this.findViewById(2131230873);
        (this.mProgressBar = this.findViewById(2131230865)).setVisibility(4);
        final HashMap<EditText, String> hashMap = new HashMap<EditText, String>();
        hashMap.put(this.mXMPPServerIPAddrField, "xmppServerIPField");
        hashMap.put(this.mOAUTH2ServerIPAddrField, "oauth2ServerIPField");
        hashMap.put(this.mChatUserNameField, "clientIdField");
        hashMap.put(this.mPasswordField, "clientSecretField");
        hashMap.put(this.mPinField, "pinField");
        this.clientDB = ClientDBHandlerClass.getInstance(this.mPinField.getText().toString(), this.getApplicationContext());
        this.mRegisterButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(View iterator) {
                boolean b = false;
                if (RegisterActivity.this.validateFields(hashMap)) {
                    RegisterActivity.this.mRegisterButton.setEnabled(false);
                    try {
                        if (RegisterActivity.this.clientDB.nClients() != 0) {
                            b = true;
                            RegisterActivity.this.mRegisterButton.setEnabled(true);
                            new AlertDialog.Builder((Context)RegisterActivity.this).setTitle("WARNING").setMessage("Select 'Continue' to reconfigure Terrortime for new user. All current user data will be lost.").setCancelable(false).setNegativeButton("Continue", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    RegisterActivity.this.clientDB.dropAllTables();
                                    RegisterActivity.this.clientDB.close();
                                    RegisterActivity.this.clientDB = ClientDBHandlerClass.getInstance(RegisterActivity.this.mPinField.getText().toString(), RegisterActivity.this.getApplicationContext());
                                    RegisterActivity.this.mRegisterButton.performClick();
                                }
                            }).setPositiveButton("Cancel", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                                public void onClick(final DialogInterface dialogInterface, final int n) {
                                    for (final EditText editText : hashMap.keySet()) {
                                        editText.getText().clear();
                                        editText.setError((CharSequence)null);
                                    }
                                    RegisterActivity.this.mCancelView.performClick();
                                }
                            }).create().show();
                        }
                        if (RegisterActivity.this.clientDB == null) {
                            throw new RuntimeException("Failed to connect to Client database. Try again or select \"cancel\".");
                        }
                        if (!b) {
                            RegisterActivity.this.registerNewTerrorist();
                        }
                    }
                    catch (Exception ex) {
                        Log.e("EXCEPTION LOG", "setOnClickListener: ", (Throwable)ex);
                        Toast.makeText(RegisterActivity.this.getApplicationContext(), (CharSequence)ex.getMessage(), 1).show();
                    }
                }
                else {
                    iterator = (View)hashMap.keySet().iterator();
                    while (((Iterator)iterator).hasNext()) {
                        final EditText editText = ((Iterator<EditText>)iterator).next();
                        try {
                            final String s = hashMap.get(editText);
                            if (editText == null) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Fatal Error: ");
                                sb.append(s);
                                sb.append(" was null.");
                                throw new RuntimeException(sb.toString());
                            }
                            if (editText.getError() != null) {
                                editText.requestFocus();
                                Toast.makeText(RegisterActivity.this.getApplicationContext(), (CharSequence)editText.getError().toString(), 1).show();
                                break;
                            }
                            continue;
                        }
                        catch (Exception ex2) {
                            Log.e("EXCEPTION LOG", "setOnClickListener: ", (Throwable)ex2);
                            Toast.makeText(RegisterActivity.this.getApplicationContext(), (CharSequence)ex2.getMessage(), 1).show();
                        }
                    }
                }
            }
        });
        this.mCancelView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                view.requestFocus();
                RegisterActivity.this.finish();
            }
        });
    }
    
    @Override
    protected final void onDestroy() {
        super.onDestroy();
        final ClientDBHandlerClass clientDB = this.clientDB;
        if (clientDB != null) {
            clientDB.close();
            this.clientDB = null;
        }
    }
    
    private static class KeyGenerationTask extends AsyncTask<Void, Integer, Client>
    {
        WeakReference<RegisterActivity> activityWeakReference;
        private String errorMsg;
        private Client mClient;
        
        KeyGenerationTask(RegisterActivity registerActivity) throws Exception {
            this.errorMsg = "";
            this.activityWeakReference = new WeakReference<RegisterActivity>(registerActivity);
            registerActivity = this.activityWeakReference.get();
            (this.mClient = new Client(registerActivity.mChatUserNameField.getText().toString())).setXmppUserName(registerActivity.mChatUserNameField.getText().toString());
            this.mClient.setXmppServerIP(registerActivity.mXMPPServerIPAddrField.getText().toString());
            this.mClient.setOAuth2ServerIP(registerActivity.mOAUTH2ServerIPAddrField.getText().toString());
            this.mClient.setEncryptPin(registerActivity.mPinField.getText().toString());
        }
        
        protected Client doInBackground(final Void... array) {
            try {
                this.mClient.generateSymmetricKey();
                this.mClient.generatePublicPrivateKeys();
                final Client mClient = this.mClient;
                this.errorMsg = "SUCCESS";
            }
            catch (Exception ex) {
                this.mClient = null;
                this.errorMsg = ex.getMessage();
                Log.e("EXCEPTION LOG", "KeyGenerationTask: ", (Throwable)ex);
            }
            return this.mClient;
        }
        
        protected void onPostExecute(final Client client) {
            super.onPostExecute((Object)client);
            final RegisterActivity registerActivity = this.activityWeakReference.get();
            boolean b = true;
            registerActivity.mProgressBar.setVisibility(8);
            if (client != null) {
                try {
                    client.setOAuth2ClientSecret(registerActivity.mPinField.getText().toString(), registerActivity.mPasswordField.getText().toString().getBytes());
                    registerActivity.clientDB.addOrUpdateClient(client);
                }
                catch (Exception ex) {
                    b = false;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Registration failed. Select OK to close window. ");
                    sb.append(ex.getMessage());
                    this.errorMsg = sb.toString();
                    Log.e("ERROR LOG", ex.getMessage());
                }
            }
            else {
                b = false;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Registration failed. ");
                sb2.append(this.errorMsg);
                Log.e("ERROR LOG", sb2.toString());
            }
            if (!b) {
                if (registerActivity.clientDB != null) {
                    registerActivity.clientDB.dropAllTables();
                    registerActivity.clientDB.close();
                }
                final AlertDialog.Builder setTitle = new AlertDialog.Builder((Context)registerActivity).setTitle("ERROR");
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Registration failed. Select OK to close window. ");
                sb3.append(this.errorMsg);
                setTitle.setMessage(sb3.toString()).setCancelable(false).setPositiveButton("OK", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        Log.v("keygen task", "Key generation failed");
                        dialogInterface.dismiss();
                        registerActivity.finish();
                    }
                }).create().show();
            }
            else {
                Log.v("keygen task", "Key generation completed successfully.");
                registerActivity.finish();
            }
        }
        
        protected void onPreExecute() {
            super.onPreExecute();
            final RegisterActivity registerActivity = this.activityWeakReference.get();
            final View currentFocus = registerActivity.getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager)registerActivity.getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
        
        protected void onProgressUpdate(final Integer... array) {
        }
    }
}
