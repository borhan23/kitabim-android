package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kitaplik.mbo.com.sosyal_kitaplik.AddCitationBook;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Citations;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.CitationsFragment;

public class CitationAddAdapter extends RecyclerView.Adapter<CitationAddHolder> {
    Context c;
    ArrayList<Book> books;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="citation";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    Citations citations;
    BookStatistic bookstatistic;
    Statistic statistic;

    public CitationAddAdapter(Activity c, ArrayList<Book> books){
        this.c = c;
        this.books = books;

    }

    @Override
    public CitationAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.list_citation_add_book,parent,false);
        return new CitationAddHolder(v);
    }

    @Override
    public void onBindViewHolder(final CitationAddHolder holder, int position) {
        final Book b = books.get(position);
        holder.txtaddcitatbookname.setText(b.getBook_name());
        holder.btnselectbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
                View mView = LayoutInflater.from(c).inflate(R.layout.dialog_add_citation, null);

                final TextView txtcitatbook = (TextView) mView.findViewById(R.id.txtcitatbook);
                final EditText editcitation = (EditText) mView.findViewById(R.id.editusercitation);
                final EditText citatpage = (EditText) mView.findViewById(R.id.citatpage);
                final Button btnaddcitat = (Button) mView.findViewById(R.id.btnconfirmcitat);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                txtcitatbook.setText(b.getBook_name());
                citations = new Citations();
                myRef = FirebaseDatabase.getInstance().getReference();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                btnaddcitat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(citatpage.getText().toString().trim()) && editcitation.getText().length() >= 10){
                            Integer citationpage = Integer.parseInt(citatpage.getText().toString().trim());
                            String citation = editcitation.getText().toString().trim();
                            citations.setBook_id(b.getBook_id());
                            citations.setCitation_Date(sistemTarihiniGetir());
                            citations.setContent(citation);
                            citations.setCitation_page(citationpage);
                            citations.setUser_id(firebaseUser.getUid());
                            sayfaKontrol(citations, b.getBook_id());
                            if(citationpage <= b.getPage_number()){
                                myRef.child("Citations").child(firebaseUser.getUid()).child(b.getBook_id()).push().setValue(citations);
                                myRef.child("MainFlow").push().setValue(citations);
                                begenGuncelle(b.getBook_id());
                                dialog.dismiss();
                                Toast.makeText(c,"Alıntı başarılı.",Toast.LENGTH_LONG).show();
                                ((AddCitationBook)c).finish();
                            }else
                                Toast.makeText(c,"Sayfa numarası kitap sayfa sayısından büyük.",Toast.LENGTH_LONG).show();
                        }else
                            Toast.makeText(c,"Boş alan bırakmayınız ve alıntınız en az 10 karakter olmalı.",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void sayfaKontrol(Citations citations, String book_id) {
    }

    private void begenGuncelle(final String book_id) {
        myRef.child("BookStatistic").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                BookStatistic bs = dataSnapshot.getValue(BookStatistic.class);
                bookstatistic= new BookStatistic();


                bookstatistic.setTotal_like(bs.getTotal_like());
                bookstatistic.setTotal_rate_number(bs.getTotal_rate_number());
                Integer total_citation = bs.getTotal_citation() + 1;
                bookstatistic.setTotal_citation(total_citation);
                bookstatistic.setBook_id(bs.getBook_id());

                myRef.child("BookStatistic").child(book_id).setValue(bookstatistic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Statistic st = dataSnapshot.getValue(Statistic.class);
                statistic= new Statistic();
                if(st != null) {
                    statistic.setLike_count(st.getLike_count());
                    statistic.setUser_page_number(st.getUser_page_number());
                    statistic.setUser_id(st.getUser_id());
                        Integer total_citation = st.getTotal_citation() + 1;
                        statistic.setTotal_citation(total_citation);

                    myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").setValue(statistic);
                }else if(st == null){
                    statistic.setTotal_citation(1);
                    statistic.setUser_page_number(0);
                    statistic.setUser_id(firebaseUser.getUid());
                    statistic.setLike_count(0);
                    myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").setValue(statistic);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String sistemTarihiniGetir()
    {
        SimpleDateFormat bicim=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date tarih=new Date();
        return bicim.format(tarih);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }
}
