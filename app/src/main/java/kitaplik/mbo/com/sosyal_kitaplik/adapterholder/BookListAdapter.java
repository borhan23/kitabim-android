package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.BookDetailActivity;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;

public class BookListAdapter extends RecyclerView.Adapter<BookViewHolder> {
    Context c;
    ArrayList<Book> books;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="MainFlow";
    private RecyclerView recyclerView;

    public BookListAdapter(Context c, ArrayList<Book> books){
        this.c = c;
        this.books = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.grid_item,parent,false);

        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        Log.d(TAG,"On Create");
        Book b = books.get(position);
        Log.d(TAG,"+++++" + books.toString());
        Log.d(TAG,"+++++++++++++" + b.getBook_name());
        holder.txtbookname.setText(b.getBook_name());
        holder.txtbookrate.setText(String.valueOf(b.getBook_rate()));
        holder.bookratebar.setRating(b.getBook_rate().floatValue());
        if(b.getBook_image() == ""){

        }else {
            Picasso.get()
                    .load(b.getBook_image())
                    .resize(120, 160)
                    .into(holder.bookimage);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d(TAG,"*** clicked position:"+position);
                Intent intent =  new Intent(c, BookDetailActivity.class);

                String bookname = books.get(position).getBook_name();
                String bookauthor = books.get(position).getBook_author();
                String bookdetail = books.get(position).getShort_description();
                String bookimageurl = books.get(position).getBook_image();
                Integer bookpagenumber = books.get(position).getPage_number();
                String bookdateofissue = books.get(position).getDate_of_issue();
                Double bookrate = books.get(position).getBook_rate();
                String bookid = books.get(position).getBook_id();

                intent.putExtra("book_name",bookname);
                intent.putExtra("book_author",bookauthor);
                intent.putExtra("book_detail",bookdetail);
                intent.putExtra("book_imageurl",bookimageurl);
                intent.putExtra("book_pagenumber",String.valueOf(bookpagenumber));
                intent.putExtra("book_dateofissue",bookdateofissue);
                intent.putExtra("book_rate",bookrate.floatValue());
                intent.putExtra("book_id",bookid);


                c.startActivity(intent);

                }

        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}
