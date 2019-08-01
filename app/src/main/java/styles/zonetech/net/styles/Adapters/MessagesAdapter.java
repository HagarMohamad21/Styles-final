package styles.zonetech.net.styles.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;
import styles.zonetech.net.styles.ViewHolders.ReceivedMessageViewHolder;
import styles.zonetech.net.styles.ViewHolders.SentMessageViewHolder;


public class MessagesAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    Fonts fonts;
    private Context mContext;
    private ArrayList<Models> messageList;

    public MessagesAdapter(Context mContext, ArrayList<Models> messageList) {
        this.mContext = mContext;
        this.messageList = messageList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.sent_message_list_item, null, false);
            return new SentMessageViewHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.received_message_list_item, viewGroup, false);
            return new ReceivedMessageViewHolder(view);
        }

        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        fonts=new Fonts(mContext);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageViewHolder) holder).bind(messageList.get(i),fonts);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageViewHolder) holder).bind(messageList.get(i),fonts);
    }}


    @Override
    public int getItemCount() {
        return messageList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).isSender()){
    return VIEW_TYPE_MESSAGE_SENT;
}
      else {return VIEW_TYPE_MESSAGE_RECEIVED;}

    }





}

