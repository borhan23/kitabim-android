package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class WillReadAddHolder extends RecyclerView.ViewHolder {
    TextView txtbookrate,txtbookname;
    ImageView bookimage;
    RatingBar bookratebar;
    Button btnekle;

    public WillReadAddHolder(View itemView) {
        super(itemView);
        txtbookname = (TextView) itemView.findViewById(R.id.grid_book_name);
        txtbookrate = (TextView) itemView.findViewById(R.id.txtbookrate);
        bookimage = (ImageView) itemView.findViewById(R.id.grid_book_image);
        bookratebar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        btnekle = (Button) itemView.findViewById(R.id.btnaddlibrary);

    }
}
