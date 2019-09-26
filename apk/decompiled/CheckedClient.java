package com.badguy.terrortime;

import android.widget.*;
import android.util.*;
import android.view.inputmethod.*;
import android.view.*;
import android.support.v7.app.*;
import android.content.*;
import android.os.*;
import java.lang.ref.*;
import java.util.*;

public class CheckedClient
{
    private String clientId;
    private SettingsActivity context;
    private Map<EditText, String> fMap;
    private String pin;
    
    public CheckedClient(final SettingsActivity context, final String clientId, final String pin, final Map<EditText, String> fMap) {
        this.clientId = null;
        this.pin = null;
        this.context = null;
        if (context == null) {
            Log.e("ERROR", "Activity context was null.");
        }
        else if (pin == null) {
            Log.e("ERROR", "Pin was null.");
        }
        else if (clientId == null) {
            Log.e("ERROR", "Client id was null.");
        }
        else if (fMap == null) {
            Log.e("ERROR", "Field map was null.");
        }
        else {
            this.context = context;
            this.clientId = clientId;
            this.pin = pin;
            this.fMap = fMap;
            final View currentFocus = context.getCurrentFocus();
            if (currentFocus != null) {
                ((InputMethodManager)context.getSystemService("input_method")).hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }
    
    private boolean clientIdIsCorrect() {
        boolean b = true;
        final ClientDBHandlerClass instance = ClientDBHandlerClass.getInstance(this.pin, this.context.getApplicationContext());
        if (instance == null) {
            b = false;
            this.taskAlertAndFinish("Activity: Failed to open client database. clientDB was null.");
        }
        boolean b2 = b;
        if (b) {
            b2 = b;
            if (instance.countClients(this.clientId) == 0) {
                b2 = false;
            }
        }
        if (instance != null) {
            instance.close();
        }
        return b2;
    }
    
    private void taskAlertAndFinish(String s) {
        final SettingsActivity context = this.context;
        if ((s = s) == null) {
            s = "";
        }
        final AlertDialog.Builder setTitle = new AlertDialog.Builder((Context)context).setTitle("ERROR");
        final StringBuilder sb = new StringBuilder();
        sb.append("Settings failed. Select OK to close window. ");
        sb.append(s);
        setTitle.setMessage(sb.toString()).setCancelable(false).setPositiveButton("OK", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                Log.v("SettingsActivity", "Closing SettingsActivity after error.");
                dialogInterface.dismiss();
                context.finish();
            }
        }).create().show();
    }
    
    public void updateClientSettings() {
        if (!this.clientIdIsCorrect()) {
            this.context.alertAndFinish("Bad Client Id");
        }
        else {
            final ClientDBHandlerClass instance = ClientDBHandlerClass.getInstance(this.pin, this.context.getApplicationContext());
            if (instance == null) {
                this.context.alertAndFinish("Activity: Failed to open client database. clientDB was null.");
            }
            else {
                final Client client = instance.getClient(this.clientId);
                if (client == null) {
                    instance.close();
                    this.taskAlertAndFinish("Activity: Unknown error. Did not get client from database.");
                }
                else {
                    client.setEncryptPin(this.pin);
                    if (instance != null) {
                        instance.close();
                    }
                    new modCheckedClient(this.context, client, this.fMap).execute((Object[])new Void[] { null });
                }
            }
        }
    }
    
    private static class modCheckedClient extends AsyncTask<Void, Integer, Boolean>
    {
        private WeakReference<SettingsActivity> activityWeakReference;
        Client client;
        String errorMsg;
        Map<EditText, String> withThis;
        
        public modCheckedClient(SettingsActivity settingsActivity, final Client client, final Map<EditText, String> withThis) {
            this.client = null;
            this.errorMsg = "";
            this.withThis = null;
            this.activityWeakReference = new WeakReference<SettingsActivity>(settingsActivity);
            settingsActivity = this.activityWeakReference.get();
            this.client = client;
            this.withThis = withThis;
            Log.d("CheckedClient", "Task constructor complete");
        }
        
        private final boolean updateClientDataBase() {
            final SettingsActivity settingsActivity = this.activityWeakReference.get();
            boolean b = true;
            ClientDBHandlerClass clientDBHandlerClass = null;
            ClientDBHandlerClass instance = null;
            Log.d("CheckedClient", "Starting updateClientDataBase");
            boolean b3;
            if (this.client != null) {
                for (final EditText editText : this.withThis.keySet()) {
                    try {
                        final String s = this.withThis.get(editText);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("UpdateClientDataBase, field name processing: ");
                        sb.append(s);
                        Log.d("CheckedClient", sb.toString());
                        if (editText == null) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Fatal Error: ");
                            sb2.append(s);
                            sb2.append(" was null.");
                            throw new RuntimeException(sb2.toString());
                        }
                        editText.getText().toString();
                        if (s.equals("xmppServerIPField") && !editText.getText().toString().isEmpty()) {
                            this.client.setXmppServerIP(editText.getText().toString());
                        }
                        else if (s.equals("oauth2ServerIPField") && !editText.getText().toString().isEmpty()) {
                            this.client.setOAuth2ServerIP(editText.getText().toString());
                        }
                        else if (s.equals("passwordField") && !editText.getText().toString().isEmpty()) {
                            this.client.setOAuth2ClientSecret(this.client.getEncryptPin(), editText.getText().toString().getBytes());
                        }
                    }
                    catch (Exception ex) {
                        Log.e("EXCEPTION LOG", "updateClientDataBase: ", (Throwable)ex);
                        this.errorMsg = "Unknown error: Failed to update client settings";
                        b = false;
                    }
                    Log.d("CheckedClient", "UpdateClientDataBase, completed client object update.");
                }
                boolean b2 = b;
                if (b) {
                    instance = ClientDBHandlerClass.getInstance(this.client.getEncryptPin(), settingsActivity.getApplicationContext());
                    b2 = b;
                    if (instance == null) {
                        this.errorMsg = "Activity: Failed to open client database. clientDB was null";
                        b2 = false;
                    }
                    Log.d("CheckedClient", "UpdateClientDataBase, completed clientDB acquire.");
                }
                b3 = b2;
                clientDBHandlerClass = instance;
                if (b2) {
                    try {
                        this.client.setEncrypted_oAuth2AccessToken(null);
                        this.client.setOAuth2AccessTokenExpiration(0);
                        instance.addOrUpdateClient(this.client);
                    }
                    catch (Exception ex2) {
                        this.errorMsg = "Activity: Failed to update client in database";
                        b2 = false;
                    }
                    Log.d("CheckedClient", "UpdateClientDataBase, completed client update in database.");
                    b3 = b2;
                    clientDBHandlerClass = instance;
                }
            }
            else {
                this.errorMsg = "Unknown error: client is null";
                b3 = false;
            }
            if (clientDBHandlerClass != null) {
                clientDBHandlerClass.close();
            }
            Log.d("CheckedClient", "Task updateClientDataBase complete");
            return b3;
        }
        
        protected final Boolean doInBackground(final Void... array) {
            boolean b = true;
            boolean b2 = true;
            if (!this.isCancelled()) {
                try {
                    this.client.generateSymmetricKey();
                    Log.d("CheckedClient", "Task generateSymmetricKey complete");
                }
                catch (Exception ex) {
                    b2 = false;
                    Log.e("EXCEPTION LOG", this.errorMsg = "Bad Pin");
                }
                b = b2;
                if (b2) {
                    b = b2;
                    if (!this.updateClientDataBase()) {
                        this.errorMsg = "Failed to update client database";
                        b = false;
                    }
                }
            }
            Log.d("CheckedClient", "Task doInBackground complete");
            return b;
        }
        
        protected final void onCancelled() {
            this.activityWeakReference.get().finish();
        }
        
        protected final void onPostExecute(final Boolean b) {
            final SettingsActivity settingsActivity = this.activityWeakReference.get();
            settingsActivity.mProgressBar.setVisibility(8);
            if (!b) {
                settingsActivity.alertAndFinish(this.errorMsg);
            }
            else {
                settingsActivity.finish();
            }
        }
        
        protected final void onPreExecute() {
            final SettingsActivity settingsActivity = this.activityWeakReference.get();
            settingsActivity.mSettingsButton.setEnabled(false);
            settingsActivity.mClearTokenView.setEnabled(false);
            settingsActivity.mProgressBar.setVisibility(0);
        }
        
        protected void onProgressUpdate(final Integer... array) {
        }
    }
}
