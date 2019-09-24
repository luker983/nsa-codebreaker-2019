package com.badguy.terrortime;

import android.util.*;
import android.widget.*;
import android.support.v7.app.*;
import android.content.*;
import android.os.*;
import java.util.*;
import android.view.*;

public class SettingsActivity extends AppCompatActivity
{
    private ClientDBHandlerClass clientDB;
    private EditText mChatUserNameField;
    public TextView mClearTokenView;
    private EditText mOAUTH2ServerIPAddrField;
    private EditText mPasswordField;
    private EditText mPinField;
    public ProgressBar mProgressBar;
    public Button mSettingsButton;
    private EditText mXMPPServerIPAddrField;
    private String name;
    private String pin;
    
    public SettingsActivity() {
        this.clientDB = null;
        this.mProgressBar = null;
        this.mChatUserNameField = null;
        this.mPinField = null;
        this.mXMPPServerIPAddrField = null;
        this.mOAUTH2ServerIPAddrField = null;
        this.mPasswordField = null;
        this.mSettingsButton = null;
        this.mClearTokenView = null;
    }
    
    private void updateClientSettings(final Map<EditText, String> map) {
        new CheckedClient(this, this.mChatUserNameField.getText().toString(), this.mPinField.getText().toString(), map).updateClientSettings();
    }
    
    private boolean validateFields(final Map<EditText, String> map) {
        final boolean b = true;
        final ParameterValidatorClass parameterValidatorClass = new ParameterValidatorClass();
        this.mXMPPServerIPAddrField.setError((CharSequence)null);
        this.mPasswordField.setError((CharSequence)null);
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
                if (!string.isEmpty() && editText == this.mXMPPServerIPAddrField && !parameterValidatorClass.isValidIpAddress(string)) {
                    editText.setError((CharSequence)this.getString(2131624008));
                    b2 = false;
                    break;
                }
                if (!string.isEmpty() && editText == this.mOAUTH2ServerIPAddrField && !parameterValidatorClass.isValidIpAddress(string)) {
                    editText.setError((CharSequence)this.getString(2131624008));
                    b2 = false;
                    break;
                }
                if (!string.isEmpty() && editText == this.mPasswordField && !parameterValidatorClass.isValidPassword(string)) {
                    editText.setError((CharSequence)this.getString(2131624006));
                    b2 = false;
                    break;
                }
                if (editText == this.mChatUserNameField) {
                    if (editText.getText().toString().isEmpty()) {
                        editText.setError((CharSequence)this.getString(2131624004));
                        b2 = false;
                        break;
                    }
                    if (!parameterValidatorClass.isValidUserName(string)) {
                        editText.setError((CharSequence)this.getString(2131624009));
                        b2 = false;
                        break;
                    }
                    continue;
                }
                else {
                    if (editText != this.mPinField) {
                        continue;
                    }
                    if (editText.getText().toString().isEmpty()) {
                        editText.setError((CharSequence)this.getString(2131624004));
                        b2 = false;
                        break;
                    }
                    if (!parameterValidatorClass.isValidPin(string)) {
                        editText.setError((CharSequence)this.getString(2131624007));
                        b2 = false;
                        break;
                    }
                    continue;
                }
            }
            catch (Exception ex) {
                Log.e("EXCEPTION LOG", "validateFields: ", (Throwable)ex);
                Toast.makeText(this.getApplicationContext(), (CharSequence)ex.getMessage(), 1).show();
            }
        }
        return b2;
    }
    
    public void alertAndFinish(String s) {
        if ((s = s) == null) {
            s = "";
        }
        final AlertDialog.Builder setTitle = new AlertDialog.Builder((Context)this).setTitle("ERROR");
        final StringBuilder sb = new StringBuilder();
        sb.append("Settings failed. Select OK to close window. ");
        sb.append(s);
        setTitle.setMessage(sb.toString()).setCancelable(false).setPositiveButton("OK", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                Log.v("SettingsActivity", "Closing SettingsActivity after error.");
                dialogInterface.dismiss();
                SettingsActivity.this.finish();
            }
        }).create().show();
    }
    
    @Override
    protected final void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427361);
        (this.mProgressBar = this.findViewById(2131230867)).setVisibility(4);
        this.mChatUserNameField = this.findViewById(2131230825);
        this.mPinField = this.findViewById(2131230831);
        this.mXMPPServerIPAddrField = this.findViewById(2131230966);
        this.mOAUTH2ServerIPAddrField = this.findViewById(2131230854);
        this.mPasswordField = this.findViewById(2131230862);
        this.mSettingsButton = this.findViewById(2131230955);
        this.mClearTokenView = this.findViewById(2131230765);
        final HashMap<EditText, String> hashMap = new HashMap<EditText, String>();
        hashMap.put(this.mChatUserNameField, "chatUserNameField");
        hashMap.put(this.mPinField, "pinField");
        hashMap.put(this.mXMPPServerIPAddrField, "xmppServerIPField");
        hashMap.put(this.mOAUTH2ServerIPAddrField, "oauth2ServerIPField");
        hashMap.put(this.mPasswordField, "passwordField");
        this.clientDB = null;
        this.mClearTokenView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(View iterator) {
                SettingsActivity.this.mSettingsButton.setEnabled(true);
                SettingsActivity.this.mClearTokenView.setEnabled(true);
                if (SettingsActivity.this.validateFields(hashMap)) {
                    final SettingsActivity this$0 = SettingsActivity.this;
                    this$0.name = this$0.mChatUserNameField.getText().toString();
                    final SettingsActivity this$2 = SettingsActivity.this;
                    this$2.pin = this$2.mPinField.getText().toString();
                    SettingsActivity.this.updateClientSettings(hashMap);
                    Log.d("SettingsActivity", "Completed activity without error.");
                }
                else {
                    iterator = (View)hashMap.keySet().iterator();
                    while (((Iterator)iterator).hasNext()) {
                        final EditText editText = ((Iterator<EditText>)iterator).next();
                        try {
                            if (editText.getError() != null) {
                                editText.requestFocus();
                                Toast.makeText(SettingsActivity.this.getApplicationContext(), (CharSequence)editText.getError().toString(), 1).show();
                                break;
                            }
                            continue;
                        }
                        catch (Exception ex) {
                            Log.e("EXCEPTION LOG", "setOnClickListener: ", (Throwable)ex);
                            Toast.makeText(SettingsActivity.this.getApplicationContext(), (CharSequence)ex.getMessage(), 1).show();
                        }
                    }
                }
            }
        });
        this.mSettingsButton.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(View iterator) {
                if (SettingsActivity.this.validateFields(hashMap)) {
                    SettingsActivity.this.mSettingsButton.setEnabled(true);
                    SettingsActivity.this.mClearTokenView.setEnabled(true);
                    final SettingsActivity this$0 = SettingsActivity.this;
                    this$0.name = this$0.mChatUserNameField.getText().toString();
                    final SettingsActivity this$2 = SettingsActivity.this;
                    this$2.pin = this$2.mPinField.getText().toString();
                    SettingsActivity.this.updateClientSettings(hashMap);
                    Log.d("SettingsActivity", "Completed activity without error.");
                }
                else {
                    iterator = (View)hashMap.keySet().iterator();
                    while (((Iterator)iterator).hasNext()) {
                        final EditText editText = ((Iterator<EditText>)iterator).next();
                        try {
                            if (editText.getError() != null) {
                                editText.requestFocus();
                                Toast.makeText(SettingsActivity.this.getApplicationContext(), (CharSequence)editText.getError().toString(), 1).show();
                                break;
                            }
                            continue;
                        }
                        catch (Exception ex) {
                            Log.e("EXCEPTION LOG", "setOnClickListener: ", (Throwable)ex);
                            Toast.makeText(SettingsActivity.this.getApplicationContext(), (CharSequence)ex.getMessage(), 1).show();
                        }
                    }
                }
            }
        });
    }
}
