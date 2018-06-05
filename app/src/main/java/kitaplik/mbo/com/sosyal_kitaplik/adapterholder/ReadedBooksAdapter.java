package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

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

import kitaplik.mbo.com.sosyal_kitaplik.BookDetailActivity;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;
import kitaplik.mbo.com.sosyal_kitaplik.entities.User;

public class ReadedBooksAdapter extends RecyclerView.Adapter<ReadedBooksHolder> {
    Context c;
    ArrayList<Library> library;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG = "readed";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    Book book;
    private FirebaseUser firebaseUser;

    public ReadedBooksAdapter(Context c, ArrayList<Library> library) {
        this.c = c;
        this.library = library;
    }

    @Override
    public ReadedBooksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_readed_book, parent, false);

        Log.d(TAG, "On create");
        return new ReadedBooksHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadedBooksHolder holder, final int position) {
        Log.d(TAG, "On Create");
        final Library lb = library.get(position);
        kitapVeriAl(lb.getBook_id(), holder);
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Library").child(firebaseUser.getUid()).child("readed_book").child(lb.getBook_id()).removeValue();
            }
        });

    }

    private void kitapVeriAl(String book_id, final ReadedBooksHolder holder) {
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
        return library.size();
    }
}
