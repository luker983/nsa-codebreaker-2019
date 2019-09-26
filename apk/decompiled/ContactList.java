package com.badguy.terrortime;

import org.jivesoftware.smack.roster.*;
import org.jxmpp.jid.*;
import org.jxmpp.jid.parts.*;
import android.content.*;
import android.support.v4.content.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.*;
import org.jivesoftware.smack.*;
import android.util.*;
import org.jivesoftware.smack.packet.*;

public class ContactList
{
    private HashMap<String, Boolean> mAvailabilityMap;
    private Client mClient;
    private HashMap<Integer, Jid> mContactMap;
    private List<String> mContacts;
    private HashMap<String, RosterEntry> mJidMap;
    private Roster mRoster;
    
    public ContactList(final Roster mRoster, final Client mClient) {
        this.mClient = mClient;
        (this.mRoster = mRoster).addRosterListener(new ContactListener());
        this.mJidMap = new HashMap<String, RosterEntry>();
        this.mContactMap = new HashMap<Integer, Jid>();
        this.mContacts = new ArrayList<String>();
        this.mAvailabilityMap = new HashMap<String, Boolean>();
    }
    
    private void addContacts(final Collection<Jid> collection) {
        collection.forEach(new _$$Lambda$ContactList$_BrIVKQb6QNNE8wSx7jdWEQKac4(this));
    }
    
    private void sendBroadcast(final String action) {
        final Intent intent = new Intent();
        intent.setAction(action);
        LocalBroadcastManager.getInstance(TerrorTimeApplication.getAppContext()).sendBroadcast(intent);
    }
    
    public HashMap<String, Boolean> getAvailabilityMap() {
        return this.mAvailabilityMap;
    }
    
    public List<Jid> getContactJids() {
        return this.mContactMap.values().stream().collect((Collector<? super Jid, ?, List<Jid>>)Collectors.toList());
    }
    
    public List<String> getContactNames() {
        return this.mContacts;
    }
    
    public Optional<Jid> getJidAtIndex(final int n) {
        return Optional.ofNullable(this.mContactMap.get(n));
    }
    
    public Optional<Jid> getJidFromString(final String s) {
        return Optional.ofNullable(this.getRosterEntry(s)).orElseGet((Supplier<? extends Optional<RosterEntry>>)_$$Lambda$ContactList$Vi20EXpwvwX_vQG5DZy6UxPK9iM.INSTANCE).map((Function<? super RosterEntry, ? extends Jid>)_$$Lambda$ContactList$7IaM_DzmRIGb710fxba30_RRcHo.INSTANCE);
    }
    
    public Optional<String> getLocalPart(final String s) {
        return Optional.ofNullable(this.getRosterEntry(s)).orElseGet((Supplier<? extends Optional<RosterEntry>>)_$$Lambda$ContactList$Vi20EXpwvwX_vQG5DZy6UxPK9iM.INSTANCE).map((Function<? super RosterEntry, ?>)_$$Lambda$ContactList$lug0BpjG90OFpXLjcQRVfOcwvaI.INSTANCE).map((Function<? super Object, ? extends String>)_$$Lambda$ContactList$VP9Vi6dnBqtMmiP_oVVUGWH3Gxo.INSTANCE);
    }
    
    public Optional<Roster> getRoster() {
        return Optional.ofNullable(this.mRoster);
    }
    
    public Optional<RosterEntry> getRosterEntry(final String s) {
        if (s == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.mJidMap.get(s));
    }
    
    public Set<Jid> getSetOfJids() {
        final Roster mRoster = this.mRoster;
        if (mRoster == null) {
            return Collections.emptySet();
        }
        return mRoster.getEntries().stream().map((Function<? super Object, ?>)_$$Lambda$ContactList$09gMaIPFwd_9UDclAHTGVNxlQOs.INSTANCE).collect((Collector<? super Object, ?, Set<Jid>>)Collectors.toSet());
    }
    
    public Optional<Jid> getUserJid() {
        try {
            return (Optional<Jid>)Optional.of(TerrorTimeApplication.getInstance().getXMPPTCPConnection().orElseThrow((Supplier<? extends Throwable>)_$$Lambda$ContactList$yM6Y_bc5OGVMb_5HpOjHLyarW_4.INSTANCE).getUser());
        }
        finally {
            final Throwable t;
            Log.e("EXCEPTION", "Failed to get user jid", t);
            return Optional.empty();
        }
    }
    
    public void refreshContactList() {
        this.mJidMap.clear();
        this.mContactMap.clear();
        this.mContacts.clear();
        final Roster mRoster = this.mRoster;
        if (mRoster != null) {
            this.addContacts((Collection<Jid>)mRoster.getEntries().stream().map((Function<? super Object, ?>)_$$Lambda$ContactList$09gMaIPFwd_9UDclAHTGVNxlQOs.INSTANCE).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
    }
    
    private class ContactListener implements RosterListener
    {
        @Override
        public void entriesAdded(final Collection<Jid> collection) {
            ContactList.this.addContacts(collection);
            ContactList.this.sendBroadcast("XMPP_CONTACTS_CHANGED");
        }
        
        @Override
        public void entriesDeleted(final Collection<Jid> collection) {
            ContactList.this.refreshContactList();
            ContactList.this.sendBroadcast("XMPP_CONTACTS_CHANGED");
        }
        
        @Override
        public void entriesUpdated(final Collection<Jid> collection) {
            ContactList.this.refreshContactList();
            ContactList.this.sendBroadcast("XMPP_CONTACTS_CHANGED");
        }
        
        @Override
        public void presenceChanged(final Presence presence) {
            final Jid from = presence.getFrom();
            final boolean b = presence.getType() == Presence.Type.available;
            final Localpart localpartOrNull = presence.getFrom().getLocalpartOrNull();
            if (localpartOrNull == null) {
                ContactList.this.mAvailabilityMap.put(from.toString(), b);
            }
            else {
                ContactList.this.mAvailabilityMap.put(localpartOrNull.toString(), b);
            }
            if (b) {
                final Contact contact = ContactList.this.mClient.getContact(from.asBareJid().toString()).orElse(null);
                if (contact != null) {
                    contact.toggleRefreshOn();
                }
            }
            ContactList.this.sendBroadcast("XMPP_CONTACTS_CHANGED");
        }
    }
}
