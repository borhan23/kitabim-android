package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class ReadPlanHolder extends RecyclerView.ViewHolder {
    TextView txtplandate, txtplantime, txtplanbook, txtplanpage;
    ImageButton btnplandelete;

    public ReadPlanHolder(View itemView) {
        super(itemView);

        txtplanpage = (TextView) itemView.findViewById(R.id.txtplanpagelist);
        txtplandate = (TextView) itemView.findViewById(R.id.txtplandate);
        txtplantime = (TextView) itemView.findViewById(R.id.txtplantime);
        txtplanbook = (TextView) itemView.findViewById(R.id.txtplanbook);
        btnplandelete = (ImageButton) itemView.findViewById(R.id.btndelete_plan);
    }
}
