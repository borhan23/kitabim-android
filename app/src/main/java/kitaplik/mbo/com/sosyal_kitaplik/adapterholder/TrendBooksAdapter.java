package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kitaplik.mbo.com.sosyal_kitaplik.BookDetailActivity;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;

public class TrendBooksAdapter extends RecyclerView.Adapter<TrendBooksHolder> {
    Context c;
    ArrayList<BookStatistic> bookStatistics;
    List<BookStatistic> bookStatistics2 = new ArrayList<>();
    DatabaseReference myRef;
    LayoutInflater inflater;
    Book book;
    String TAG = "trend";

    public TrendBooksAdapter(Context c, ArrayList<BookStatistic> bookStatistics){
        this.c = c;
        this.bookStatistics = bookStatistics;
    }

    @Override
    public TrendBooksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_trend_books,parent,false);

        return new TrendBooksHolder(v);
    }

    @Override
    public void onBindViewHolder(final TrendBooksHolder holder, int position) {
        final BookStatistic b = bookStatistics.get(position);
        Log.d(TAG, "kitapVeriAl: +++"+bookStatistics.get(0).getBook_id());
        myRef = FirebaseDatabase.getInstance().getReference();
        holder.txtcitation.setText("Alıntı Sayısı: "+b.getTotal_citation());
        holder.txtlike.setText("Beğeni Sayısı: "+b.getTotal_like());
        kitapVeriAl(holder, b.getBook_id());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                kitapDetay(b.getBook_id());
            }
        });

    }

    private void kitapDetay(String book_id) {
        book = new Book();
        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book = dataSnapshot.getValue(Book.class);
                Intent intent =  new Intent(c, BookDetailActivity.class);

                String bookname = book.getBook_name();
                String bookauthor = book.getBook_author();
                String bookdetail = book.getShort_description();
                String bookimageurl = book.getBook_image();
                Integer bookpagenumber = book.getPage_number();
                String bookdateofissue = book.getDate_of_issue();
                Double bookrate = book.getBook_rate();
                String bookid = book.getBook_id();

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kitapVeriAl(final TrendBooksHolder holder, String book_id) {
        book = new Book();
        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book = dataSnapshot.getValue(Book.class);

                holder.txtbookname.setText(book.getBook_name());
                holder.bookratebar.setRating((book.getBook_rate()).floatValue());
                holder.txtbookrate.setText(String.valueOf(book.getBook_rate()));

                Picasso.get()
                        .load(book.getBook_image())
                        .resize(120, 160)
                        .into(holder.bookimage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        bookStatistics2 = bookStatistics.subList(0,10);
        return bookStatistics2.size();
    }
}
