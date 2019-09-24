package com.badguy.terrortime;

import android.content.*;
import java.util.*;
import android.view.*;
import android.widget.*;
import org.jxmpp.jid.*;
import java.util.function.*;
import android.util.*;

public class ContactListAdapter extends ArrayAdapter
{
    private HashMap<String, Boolean> mAvailabilityMap;
    private ContactList mContactList;
    private List<String> mContactNames;
    private Context mContext;
    private HashMap<String, Integer> mMessageCountMap;
    private HashMap<String, List<Message>> mMessageMap;
    
    public ContactListAdapter(final Context mContext, final int n, final int n2, final List mContactNames, final HashMap mAvailabilityMap, final HashMap mMessageMap, final ContactList mContactList) {
        super(mContext, n, n2, mContactNames);
        this.mContactNames = (List<String>)mContactNames;
        this.mAvailabilityMap = (HashMap<String, Boolean>)mAvailabilityMap;
        this.mMessageCountMap = new HashMap<String, Integer>();
        this.mMessageMap = (HashMap<String, List<Message>>)mMessageMap;
        for (final Map.Entry<String, List<Message>> entry : this.mMessageMap.entrySet()) {
            this.mMessageCountMap.put(entry.getKey(), entry.getValue().size());
        }
        this.mContext = mContext;
        this.mContactList = mContactList;
    }
    
    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        if (inflate == null) {
            inflate = LayoutInflater.from(this.mContext).inflate(2131427362, viewGroup, false);
        }
        final Boolean b = this.mAvailabilityMap.get(this.mContactNames.get(n));
        final ImageView imageView = (ImageView)inflate.findViewById(2131230752);
        ((TextView)inflate.findViewById(2131230770)).setText((CharSequence)this.mContactNames.get(n));
        final TextView textView = (TextView)inflate.findViewById(2131230837);
        final ImageView imageView2 = (ImageView)inflate.findViewById(2131230847);
        if (b) {
            imageView.setImageResource(2131165277);
        }
        else {
            imageView.setImageResource(2131165278);
        }
        try {
            final Jid jid = this.mContactList.getJidAtIndex(n).orElseThrow((Supplier<? extends Throwable>)new _$$Lambda$ContactListAdapter$wwIf5GIASlhI74vbe90XngFWTcE(n));
            final Integer n2 = this.mMessageCountMap.get(jid.asBareJid().toString());
            final List<Message> list = this.mMessageMap.get(jid.asBareJid().toString());
            if (list != null && n2 != null) {
                final Integer value = list.size();
                if (n2 < value) {
                    textView.setText((CharSequence)Integer.valueOf(value - n2).toString());
                    textView.setVisibility(0);
                    imageView2.setVisibility(0);
                }
            }
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Unable to update message count", t);
        }
        return inflate;
    }
    
    public void incrementCount(final String s) {
        final Integer n = this.mMessageCountMap.get(s);
        Integer n2;
        if (n == null) {
            n2 = 1;
        }
        else {
            n2 = n + 1;
        }
        this.mMessageCountMap.put(s, n2);
    }
    
    public void updateCount(final String s, final Integer n) {
        this.mMessageCountMap.put(s, n);
    }
}
