package com.schoofi.adapters;

import android.view.View;

import com.schoofi.utils.Message;
import com.stfalcon.chatkit.messages.MessageHolders;


public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<Message> {

    public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }
}
