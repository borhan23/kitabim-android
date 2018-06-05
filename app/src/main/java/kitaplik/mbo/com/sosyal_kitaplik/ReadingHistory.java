package kitaplik.mbo.com.sosyal_kitaplik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.ReadingHistoryAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;

public class ReadingHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView bookname, bookauthor, bookpagenumber, bookdateofissue, bookrate, likecount, citationcount;
    RatingBar bookratebar;;
    private DatabaseReference mref;
    private FirebaseUser user;
    private Book book;
    private BookStatistic bookstatistic;
    private ImageView bookimage;
    private ArrayList<Statistic> statistics = new ArrayList<>();
    private ReadingHistoryAdapter readingHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_history);

        bookimage = (ImageView) findViewById(R.id.grid_book_image);
        bookname = (TextView) findViewById(R.id.grid_book_name);
        bookauthor = (TextView) findViewById(R.id.txtbook_author);
        bookpagenumber = (TextView) findViewById(R.id.txtbook_page);
        bookdateofissue = (TextView) findViewById(R.id.txtdateofissue);
        bookrate = (TextView) findViewById(R.id.txtbookdetailrate);
        likecount = (TextView) findViewById(R.id.txtlikecount);
        citationcount = (TextView) findViewById(R.id.txtcitationscount);
        bookratebar = (RatingBar) findViewById(R.id.ratingBar);

        recyclerView = (RecyclerView) findViewById(R.id.historyrv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        mref = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getExtras();

        kitapVeriAl(bundle);
        istatistikAl(bundle);
        HistoryGetir(bundle);
    }

    private void HistoryGetir(Bundle bundle) {
        mref.child("Statistic").child(user.getUid()).child("read_dates").child(bundle.get("book_id").toString()).orderByChild("readed_date")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        statistics.clear();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Statistic st = snapshot.getValue(Statistic.class);
                            if(st.getReaded_page() != 0 && st.getRead_time() != 0) {
                                statistics.add(st);
                            }
                        }

                        readingHistoryAdapter = new ReadingHistoryAdapter(getApplicationContext(),statistics);
                        recyclerView.setAdapter(readingHistoryAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void istatistikAl(Bundle bundle) {
        mref.child("BookStatistic").child(bundle.get("book_id").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bookstatistic=dataSnapshot.getValue(BookStatistic.class);
                citationcount.setText("Alıntı sayısı: "+bookstatistic.getTotal_citation().toString());
                likecount.setText("Beğeni sayısı: "+bookstatistic.getTotal_like());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kitapVeriAl(Bundle bundle) {
        book = new Book();
        mref.child("Books").child(bundle.get("book_id").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book=dataSnapshot.getValue(Book.class);

                bookauthor.setText("Yazar: "+book.getBook_author());
                bookpagenumber.setText("Sayfa sayısı: "+String.valueOf(book.getPage_number()));
                bookname.setText(book.getBook_name());
                bookdateofissue.setText("Basım tarihi: "+String.valueOf(book.getDate_of_issue()));
                bookrate.setText(String.valueOf(book.getBook_rate()));
                bookratebar.setRating(book.getBook_rate().floatValue());

                Picasso.get()
                        .load(book.getBook_image())
                        .resize(140, 200)
                        .into(bookimage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
