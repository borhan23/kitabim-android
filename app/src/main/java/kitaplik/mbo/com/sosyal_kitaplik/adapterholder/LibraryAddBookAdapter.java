package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kitaplik.mbo.com.sosyal_kitaplik.LibraryAddBook;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;

public class LibraryAddBookAdapter extends RecyclerView.Adapter<LibraryAddBookHolder> {
    Context c;
    ArrayList<Book> books;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="library";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;

    public LibraryAddBookAdapter(Activity c, ArrayList<Book> books){
        this.c = c;
        this.books = books;
    }
    @Override
    public LibraryAddBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.list_read_book,parent,false);

        Log.d(TAG,"On create");
        return new LibraryAddBookHolder(v);
    }

    @Override
    public void onBindViewHolder(LibraryAddBookHolder holder, int position) {
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
                Statistic st = new Statistic();
                lb.setUser_id(firebaseUser.getUid());
                lb.setBook_id(b.getBook_id());
                lb.setStarted_date(sistemTarihiniGetir());
                st.setBook_id(b.getBook_id());
                st.setReaded_page(0);
                st.setRead_time(0);
                st.setReaded_date(sistemTarihiniGetir());
                myRef.child("Library").child(firebaseUser.getUid()).child("reading_book").child(b.getBook_id()).setValue(lb);
                myRef.child("Statistic").child(firebaseUser.getUid()).child("read_dates").child(b.getBook_id()).push().setValue(st);
                Toast.makeText(c,"Başarıyla eklendi.",Toast.LENGTH_LONG).show();
                ((LibraryAddBook)c).finish();
            }
        });
    }
    public String sistemTarihiniGetir()
    {
        SimpleDateFormat bicim=new SimpleDateFormat("dd/MM/yyyy");
        Date tarih=new Date();
        return bicim.format(tarih);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
