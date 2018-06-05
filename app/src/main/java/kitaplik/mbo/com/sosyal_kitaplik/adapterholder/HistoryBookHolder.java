package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class HistoryBookHolder extends RecyclerView.ViewHolder {
    TextView txthistorybook, txthistorystart;
    Button btnselectbook;

    public HistoryBookHolder(View itemView) {
        super(itemView);

        txthistorybook = (TextView) itemView.findViewById(R.id.txthistoryname);
        txthistorystart = (TextView) itemView.findViewById(R.id.txthistorystart);
        btnselectbook = (Button) itemView.findViewById(R.id.btnselecthistorybook);
    }
}
