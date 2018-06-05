package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.LikedBooks;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;

public class LikedBooksAdapter extends RecyclerView.Adapter<LikedBooksHolder> {
    Context c;
    ArrayList<LikedBooks> likedBooks;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="liked";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    private Book book;
    private Statistic statistic;
    private BookStatistic bookstatistic;

    public LikedBooksAdapter(Context c, ArrayList<LikedBooks> likedBooks){
        this.c = c;
        this.likedBooks = likedBooks;
    }
    @Override
    public LikedBooksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_liked_books, parent, false);

        Log.d(TAG, "On create");
        return new LikedBooksHolder(v);
    }

    @Override
    public void onBindViewHolder(LikedBooksHolder holder, int position) {
        final LikedBooks lb = likedBooks.get(position);
        kitapVeriAl(lb.getBook_id(), holder);
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("LikedBooks").child(firebaseUser.getUid()).child(lb.getBook_id()).removeValue();
                begenAzalt(lb.getBook_id());
            }
        });
    }

    private void begenAzalt(final String book_id) {
        myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Statistic st = dataSnapshot.getValue(Statistic.class);
                statistic= new Statistic();

                statistic.setTotal_citation(st.getTotal_citation());
                statistic.setUser_page_number(st.getUser_page_number());
                statistic.setUser_id(st.getUser_id());
                Integer total_like = st.getLike_count() - 1;
                statistic.setLike_count(total_like);
                myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").setValue(statistic);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("BookStatistic").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                BookStatistic bs = dataSnapshot.getValue(BookStatistic.class);
                bookstatistic= new BookStatistic();

                bookstatistic.setTotal_citation(bs.getTotal_citation());
                bookstatistic.setTotal_rate_number(bs.getTotal_rate_number());
                bookstatistic.setBook_id(bs.getBook_id());
                Integer total_like = bs.getTotal_like() - 1;
                bookstatistic.setTotal_like(total_like);


                myRef.child("BookStatistic").child(book_id).setValue(bookstatistic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kitapVeriAl(String book_id, final LikedBooksHolder holder) {
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        book = new Book();

        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                book=dataSnapshot.getValue(Book.class);
                holder.txtbookname.setText(book.getBook_name());
                holder.txtbookrate.setText(String.valueOf(book.getBook_rate()));
                holder.bookratebar.setRating(book.getBook_rate().floatValue());
                if(book.getBook_image() == ""){

                }else {
                    Picasso.get()
                            .load(book.getBook_image())
                            .resize(120, 160)
                            .into(holder.bookimage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return likedBooks.size();
    }
}
