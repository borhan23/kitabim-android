package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class BookCommentHolder extends RecyclerView.ViewHolder {

    TextView txtcomment, txtusername, txtname, txtcommentdate, txtspoiler;
    ImageView userimage;
    Button btnspoiler;

    public BookCommentHolder(View itemView) {
        super(itemView);

        txtcomment = (TextView) itemView.findViewById(R.id.txtcomment);
        txtusername = (TextView) itemView.findViewById(R.id.comment_username);
        txtname = (TextView) itemView.findViewById(R.id.comment_name);
        userimage = (ImageView) itemView.findViewById(R.id.user_image);
        btnspoiler = (Button) itemView.findViewById(R.id.btnspoiler);
        txtcommentdate = (TextView) itemView.findViewById(R.id.txtcommentdate);
        txtspoiler = (TextView) itemView.findViewById(R.id.txtspoiler);
    }
}
