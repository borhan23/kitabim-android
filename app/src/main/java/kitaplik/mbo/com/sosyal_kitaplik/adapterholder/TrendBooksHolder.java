package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class TrendBooksHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txtbookrate,txtbookname, txtcitation, txtlike;
    ImageView bookimage;
    ItemClickListener itemClickListener;
    RatingBar bookratebar;

    public TrendBooksHolder(View itemView) {
        super(itemView);

        txtbookname = (TextView) itemView.findViewById(R.id.grid_book_name);
        txtbookrate = (TextView) itemView.findViewById(R.id.txtbookrate);
        txtcitation = (TextView) itemView.findViewById(R.id.grid_citation_count);
        txtlike = (TextView) itemView.findViewById(R.id.grid_like_count);
        bookimage = (ImageView) itemView.findViewById(R.id.grid_book_image);
        bookratebar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        itemView.setOnClickListener(this);
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
