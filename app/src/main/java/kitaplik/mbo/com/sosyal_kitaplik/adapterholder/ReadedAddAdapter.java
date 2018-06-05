package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.ReadedAddBook;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;

public class ReadedAddAdapter extends RecyclerView.Adapter<ReadedAddHolder> {
    Context c;
    ArrayList<Book> books;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="library";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;

    public ReadedAddAdapter(Activity c, ArrayList<Book> books){
        this.c = c;
        this.books = books;
    }

    @Override
    public ReadedAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.list_read_book,parent,false);

        Log.d(TAG,"On create");
        return new ReadedAddHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadedAddHolder holder, int position) {
        Log.d(TAG,"On Create");
        final Book b = books.get(position);
        Log.d(TAG,"+++++" + books.toString());
        Log.d(TAG,"+++++++++++++" + b.getBook_name());
        holder.txtbookname.setText(b.getBook_name());
        holder.txtbookrate.setText(String.valueOf(b.getBook_rate()));
        holder.bookratebar.setRating(b.getBook_rate().floatValue());
        holder.txtbookrate.setText(String.valueOf(b.getBook_rate()));
        Picasso.get()
                .load(b.getBook_image())
                .resize(120, 160)
                .into(holder.bookimage);
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.btnekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Library lb = new Library();
                lb.setUser_id(firebaseUser.getUid());
                lb.setBook_id(b.getBook_id());
                myRef.child("Library").child(firebaseUser.getUid()).child("readed_book").child(b.getBook_id()).setValue(lb);
                Toast.makeText(c,"Başarıyla eklendi.",Toast.LENGTH_LONG).show();
                ((ReadedAddBook)c).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}

