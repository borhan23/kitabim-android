package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class ReadingHistoryHolder extends RecyclerView.ViewHolder {
    TextView txthistorydate, txthistorypage, txthistorytime;

    public ReadingHistoryHolder(View itemView) {
        super(itemView);

        txthistorydate = (TextView) itemView.findViewById(R.id.txthistorydate);
        txthistorypage = (TextView) itemView.findViewById(R.id.txthistorypage);
        txthistorytime = (TextView) itemView.findViewById(R.id.txthistorytime);
    }
}
