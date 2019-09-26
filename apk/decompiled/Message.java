package com.badguy.terrortime;

import android.os.*;
import java.text.*;
import java.util.*;

public class Message implements Parcelable
{
    public static final Parcelable$Creator<Message> CREATOR;
    private TextAppField clientId;
    private TextAppField contactId;
    private BlobAppField content;
    private boolean fromClient;
    private Date mNow;
    private String timestamp;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<Message>() {
            public Message createFromParcel(final Parcel parcel) {
                return new Message(parcel, null);
            }
            
            public Message[] newArray(final int n) {
                return new Message[n];
            }
        };
    }
    
    public Message() {
        this.contactId = new TextAppField();
        this.clientId = new TextAppField();
        this.content = new BlobAppField();
        this.fromClient = true;
        this.timestamp = null;
        this.mNow = null;
        this.mNow = Calendar.getInstance().getTime();
        this.timestamp = new SimpleDateFormat("HH:mm z").format(this.mNow);
    }
    
    private Message(final Parcel parcel) {
        this.contactId = new TextAppField();
        this.clientId = new TextAppField();
        this.content = new BlobAppField();
        boolean fromClient = true;
        this.fromClient = true;
        final Date date = null;
        this.timestamp = null;
        this.mNow = null;
        this.contactId = new TextAppField(parcel.readString());
        this.clientId = new TextAppField(parcel.readString());
        (this.content = new BlobAppField()).setValue(parcel.createByteArray());
        if (parcel.readInt() != 1) {
            fromClient = false;
        }
        this.fromClient = fromClient;
        this.timestamp = parcel.readString();
        final long long1 = parcel.readLong();
        Date mNow;
        if (long1 == -1L) {
            mNow = date;
        }
        else {
            mNow = new Date(long1);
        }
        this.mNow = mNow;
    }
    
    public Message(final Message message) {
        this(message.getClientId(), message.getContactId(), message.getContent().clone(), message.fromClient, message.timestamp);
    }
    
    public Message(final String value, final String value2, final boolean fromClient) {
        this();
        if (value2 != null) {
            this.contactId.setValue(value2);
        }
        if (value != null) {
            this.clientId.setValue(value);
        }
        this.fromClient = fromClient;
    }
    
    public Message(final String value, final String value2, final byte[] value3, final boolean fromClient) {
        this();
        if (value2 != null) {
            this.contactId.setValue(value2);
        }
        if (value != null) {
            this.clientId.setValue(value);
        }
        if (value3 != null) {
            this.content.setValue(value3);
        }
        this.fromClient = fromClient;
    }
    
    public Message(final String value, final String value2, final byte[] value3, final boolean fromClient, final String timestamp) {
        this.contactId = new TextAppField();
        this.clientId = new TextAppField();
        this.content = new BlobAppField();
        this.fromClient = true;
        this.timestamp = null;
        this.mNow = null;
        if (value2 != null) {
            this.contactId.setValue(value2);
        }
        if (value != null) {
            this.clientId.setValue(value);
        }
        if (value3 != null) {
            this.content.setValue(value3);
        }
        this.fromClient = fromClient;
        this.timestamp = timestamp;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o != null && this.getClass() == o.getClass()) {
            final Message message = Message.class.cast(o);
            return this.contactId.equals(message.contactId) && this.clientId.equals(message.clientId) && this.fromClient == message.fromClient;
        }
        return false;
    }
    
    public final String getClientId() {
        return this.clientId.getValue();
    }
    
    public final String getContactId() {
        return this.contactId.getValue();
    }
    
    public final byte[] getContent() {
        return this.content.getValue();
    }
    
    public final Optional<String> getCreatedAt() {
        return Optional.ofNullable(this.timestamp);
    }
    
    public final Date getCreationDate() {
        return this.mNow;
    }
    
    public final boolean isFromClient() {
        return this.fromClient;
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
    
    public final void setContent(final byte[] value) {
        if (value != null) {
            this.content.setValue(value);
        }
    }
    
    public final void setFromClient(final boolean fromClient) {
        this.fromClient = fromClient;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.contactId.getValue());
        parcel.writeString(this.clientId.getValue());
        parcel.writeByteArray(this.content.getValue());
        parcel.writeInt((int)(this.fromClient ? 1 : 0));
        parcel.writeString(this.timestamp);
        final Date mNow = this.mNow;
        long time;
        if (mNow == null) {
            time = -1L;
        }
        else {
            time = mNow.getTime();
        }
        parcel.writeLong(time);
    }
}
