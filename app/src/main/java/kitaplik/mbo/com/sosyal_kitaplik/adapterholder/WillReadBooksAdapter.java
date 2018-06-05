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
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;

public class WillReadBooksAdapter extends RecyclerView.Adapter<WillReadBooksHolder> {
    Context c;
    ArrayList<Library> library;
    DatabaseReference databaseReference;
    LayoutInflater inflater;

    private String TAG = "willread";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    Book book;
    private FirebaseUser firebaseUser;

    public WillReadBooksAdapter(Context c, ArrayList<Library> library) {
        this.c = c;
        this.library = library;
    }

    @Override
    public WillReadBooksHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_will_read, parent, false);

        Log.d(TAG, "On create");
        return new WillReadBooksHolder(v);
    }

    @Override
    public void onBindViewHolder(WillReadBooksHolder holder, int position) {
        Log.d(TAG, "On Create");
        final Library lb = library.get(position);
        kitapVeriAl(lb.getBook_id(), holder);
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "++++++++++++"+firebaseUser.getUid()+"  "+lb.getBook_id());
                myRef.child("Library").child(firebaseUser.getUid()).child("will_read_book").child(lb.getBook_id()).removeValue();
            }
        });
    }
    private void kitapVeriAl(String book_id, final WillReadBooksHolder holder) {
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
