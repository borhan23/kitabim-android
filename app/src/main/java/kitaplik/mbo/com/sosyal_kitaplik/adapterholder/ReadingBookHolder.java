package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class ReadingBookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txtbookrate,txtbookname, txtremainpaper, txtdate, txtpaper;
    ImageView bookimage;
    ItemClickListener itemClickListener;
    RatingBar bookratebar;
    Button btnrecord;
    ImageButton btndelete;

    public ReadingBookHolder(View itemView) {
        super(itemView);

        txtbookname = (TextView) itemView.findViewById(R.id.txtreading);
        txtbookrate = (TextView) itemView.findViewById(R.id.txtbookrate);
        txtremainpaper = (TextView) itemView.findViewById(R.id.txtremainpaper);
        txtpaper = (TextView) itemView.findViewById(R.id.txtpaper);
        txtdate = (TextView) itemView.findViewById(R.id.txtreadstartdate);
        bookimage = (ImageView) itemView.findViewById(R.id.imgreading);
        bookratebar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        btnrecord = (Button) itemView.findViewById(R.id.btnreadingsave);
        btndelete = (ImageButton) itemView.findViewById(R.id.btndelete_reading);

        //itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(this.getLayoutPosition());
    }
}
