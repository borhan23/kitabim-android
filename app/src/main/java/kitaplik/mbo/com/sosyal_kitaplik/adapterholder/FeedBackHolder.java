package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class FeedBackHolder extends RecyclerView.ViewHolder {
    TextView txtfeeddate, txtfeedname;
    Button btnfeeddetail;
    ImageButton btndeletefeed;
    public FeedBackHolder(View itemView) {
        super(itemView);

        txtfeeddate = (TextView) itemView.findViewById(R.id.txtfeeddate);
        txtfeedname = (TextView) itemView.findViewById(R.id.txtfeedname);
        btndeletefeed = (ImageButton) itemView.findViewById(R.id.btndeletefeed);
        btnfeeddetail = (Button) itemView.findViewById(R.id.btnfeeddetail);


    }
}
