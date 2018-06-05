package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class CitationAddHolder extends RecyclerView.ViewHolder {
    TextView txtaddcitatbookname;
    Button btnselectbook;

    public CitationAddHolder(View itemView) {
        super(itemView);

        txtaddcitatbookname = (TextView) itemView.findViewById(R.id.txtaddcitatbook);
        btnselectbook = (Button) itemView.findViewById(R.id.btnselectbook);

    }
}
