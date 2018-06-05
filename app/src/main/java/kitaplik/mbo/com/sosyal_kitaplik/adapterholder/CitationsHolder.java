package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class CitationsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView nameTxt,usernameTxt,flowcontentTxt, flowcontentbook, flowcontentpage, txtcitationdate;
    ImageView imageUser;
    ItemClickListener itemClickListener;

    public CitationsHolder(View itemView) {
        super(itemView);

        nameTxt = (TextView) itemView.findViewById(R.id.flow_name);
        usernameTxt = (TextView) itemView.findViewById(R.id.flow_username);
        txtcitationdate = (TextView) itemView.findViewById(R.id.txtcitationdate);
        flowcontentbook = (TextView) itemView.findViewById(R.id.flow_content_book);
        flowcontentpage = (TextView) itemView.findViewById(R.id.flow_content_page);
        flowcontentTxt = (TextView) itemView.findViewById(R.id.flow_content);
        imageUser = (ImageView) itemView.findViewById(R.id.flow_user_image);

        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
}
