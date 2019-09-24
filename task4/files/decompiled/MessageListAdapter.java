package com.badguy.terrortime;

import android.support.v7.widget.*;
import android.content.*;
import java.util.*;
import android.widget.*;
import android.view.*;

public class MessageListAdapter extends Adapter
{
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Message> mMessageList;
    
    public MessageListAdapter(final Context mContext, final List<Message> mMessageList) {
        this.mContext = mContext;
        this.mMessageList = mMessageList;
        this.mInflater = LayoutInflater.from(mContext);
    }
    
    @Override
    public int getItemCount() {
        final List<Message> mMessageList = this.mMessageList;
        if (mMessageList == null) {
            return 0;
        }
        return mMessageList.size();
    }
    
    @Override
    public int getItemViewType(final int n) {
        if (this.mMessageList.get(n).isFromClient()) {
            return 1;
        }
        return 2;
    }
    
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int itemViewType) {
        final Message message = this.mMessageList.get(itemViewType);
        itemViewType = viewHolder.getItemViewType();
        if (itemViewType != 1) {
            if (itemViewType == 2) {
                ((ReceivedMessageHolder)viewHolder).bind(message);
            }
        }
        else {
            ((SentMessageHolder)viewHolder).bind(message);
        }
    }
    
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        if (n == 1) {
            return new SentMessageHolder(this.mInflater.inflate(2131427380, viewGroup, false));
        }
        if (n == 2) {
            return new ReceivedMessageHolder(this.mInflater.inflate(2131427379, viewGroup, false));
        }
        return null;
    }
    
    public class ReceivedMessageHolder extends ViewHolder
    {
        TextView messageText;
        TextView nameText;
        TextView timeText;
        
        ReceivedMessageHolder(final View view) {
            super(view);
            this.messageText = (TextView)view.findViewById(2131230933);
            this.timeText = (TextView)view.findViewById(2131230935);
            this.nameText = (TextView)view.findViewById(2131230934);
        }
        
        void bind(final Message message) {
            this.messageText.setText((CharSequence)new String(message.getContent()));
            this.timeText.setText((CharSequence)message.getCreatedAt().orElse(""));
            String s;
            if (message.isFromClient()) {
                s = message.getClientId();
            }
            else {
                s = message.getContactId();
            }
            this.nameText.setText((CharSequence)s.split("@")[0]);
        }
    }
    
    public class SentMessageHolder extends ViewHolder
    {
        TextView messageText;
        TextView timeText;
        
        SentMessageHolder(final View view) {
            super(view);
            this.messageText = (TextView)view.findViewById(2131230933);
            this.timeText = (TextView)view.findViewById(2131230935);
        }
        
        void bind(final Message message) {
            this.messageText.setText((CharSequence)new String(message.getContent()));
            this.timeText.setText((CharSequence)message.getCreatedAt().orElse(""));
        }
    }
}
