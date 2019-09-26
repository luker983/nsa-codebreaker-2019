package com.badguy.terrortime;

import android.support.v7.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;

public class MainActivity extends AppCompatActivity
{
    String msg;
    
    static {
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("terrortime");
    }
    
    public MainActivity() {
        this.msg = "Android : ";
    }
    
    public final void launchLoginActivity(final View view) {
        this.startActivity(new Intent((Context)this, (Class)LoginActivity.class));
    }
    
    public final void launchRegisterActivity(final View view) {
        this.startActivity(new Intent((Context)this, (Class)RegisterActivity.class));
    }
    
    @Override
    protected final void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131427359);
    }
    
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.getMenuInflater().inflate(2131492865, menu);
        return true;
    }
    
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.msg, "The onDestroy() event");
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() != 2131230835) {
            return super.onOptionsItemSelected(menuItem);
        }
        this.startActivity(new Intent((Context)this, (Class)SettingsActivity.class));
        return true;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(this.msg, "The onPause() event");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(this.msg, "The onResume() event");
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(this.msg, "The onStart() event");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(this.msg, "The onStop() event");
    }
}
