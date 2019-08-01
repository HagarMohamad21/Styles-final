package styles.zonetech.net.styles.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import styles.zonetech.net.styles.Models.Models;
import styles.zonetech.net.styles.R;
import styles.zonetech.net.styles.Helpers.Fonts;

public class SentMessageViewHolder extends RecyclerView.ViewHolder {
    TextView messageBodyTxt;

    public SentMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        messageBodyTxt=itemView.findViewById(R.id.messageBodyTxt);
    }

    public void bind(Models models, Fonts fonts) {
        messageBodyTxt.setTypeface(fonts.lightFont);
        messageBodyTxt.setText(models.getMessageDescription());
    }
}
