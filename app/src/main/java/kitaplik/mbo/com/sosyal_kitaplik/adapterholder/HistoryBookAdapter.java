package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import kitaplik.mbo.com.sosyal_kitaplik.ReadingHistory;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;

public class HistoryBookAdapter extends RecyclerView.Adapter<HistoryBookHolder> {
    Context c;
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Library> library;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="citation";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    private Book book;

    public HistoryBookAdapter(Activity c, ArrayList<Library> library){
        this.c = c;
        this.library = library;

    }
    @Override
    public HistoryBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.list_reading_history,parent,false);
        return new HistoryBookHolder(v);
    }

    @Override
    public void onBindViewHolder(HistoryBookHolder holder, final int position) {
        final Library lb = library.get(position);
        kitapVeriAl(lb.getBook_id(), holder);
        holder.txthistorystart.setText("Başlama tarihi: "+lb.getStarted_date());

        holder.btnselectbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =  new Intent(c, ReadingHistory.class);

                String bookid = lb.getBook_id();
                intent.putExtra("book_id",bookid);

                c.startActivity(intent);
            }
        });
    }

    private void kitapVeriAl(String book_id, final HistoryBookHolder holder) {
            myRef = FirebaseDatabase.getInstance().getReference();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            book = new Book();

            myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    book=dataSnapshot.getValue(Book.class);
                    holder.txthistorybook.setText(book.getBook_name());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

    }

    @Override
    public int getItemCount() {
        return library.size();
    }
}
