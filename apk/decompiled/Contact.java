package com.badguy.terrortime;

import java.security.*;
import java.util.*;

public class Contact
{
    private TextAppField clientId;
    private TextAppField contactId;
    private boolean refreshKeys;
    private Set<PublicKey> rsaPublicKeySet;
    
    public Contact(final String value, final String value2) {
        this.contactId = new TextAppField();
        this.clientId = new TextAppField();
        if (value2 != null && value != null) {
            this.contactId.setValue(value2);
            this.clientId.setValue(value);
        }
        this.rsaPublicKeySet = new HashSet<PublicKey>();
        this.refreshKeys = true;
    }
    
    public final boolean addPublicKey(final PublicKey publicKey) {
        return this.rsaPublicKeySet.add(publicKey);
    }
    
    public final boolean addPublicKeys(final Set<PublicKey> set) {
        return this.rsaPublicKeySet.addAll(set);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Contact contact = Contact.class.cast(o);
        if (!this.contactId.equals(contact.contactId)) {
            return false;
        }
        if (!this.clientId.equals(contact.clientId)) {
            return false;
        }
        final Set<PublicKey> rsaPublicKeySet = this.rsaPublicKeySet;
        if (rsaPublicKeySet != null) {
            final Set<PublicKey> rsaPublicKeySet2 = contact.rsaPublicKeySet;
            if (rsaPublicKeySet2 != null && !rsaPublicKeySet.equals(rsaPublicKeySet2)) {
                return false;
            }
        }
        return true;
    }
    
    public final String getClientId() {
        return this.clientId.getValue();
    }
    
    public final String getContactId() {
        return this.contactId.getValue();
    }
    
    public final Set<PublicKey> getPublicKeys() {
        if (this.refreshKeys) {
            this.rsaPublicKeySet = VCardHelper.getPublicKeys(this.contactId.getValue());
            this.refreshKeys = false;
        }
        return this.rsaPublicKeySet;
    }
    
    public final boolean removePublicKey(final PublicKey publicKey) {
        return this.rsaPublicKeySet.remove(publicKey);
    }
    
    public final void setClientId(final String value) {
        if (value != null) {
            this.clientId.setValue(value);
        }
    }
    
    public final void setContactId(final String value) {
        if (value != null) {
            this.contactId.setValue(value);
        }
    }
    
    public final void setPublicKeys(final Set<PublicKey> rsaPublicKeySet) {
        this.rsaPublicKeySet = rsaPublicKeySet;
    }
    
    public final void toggleRefreshOn() {
        this.refreshKeys = true;
    }
}
