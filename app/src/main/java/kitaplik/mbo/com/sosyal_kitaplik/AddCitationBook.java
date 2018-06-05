package kitaplik.mbo.com.sosyal_kitaplik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.CitationAddAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;

public class AddCitationBook extends AppCompatActivity {

    private TextView txtaddcitatbookname;
    private Button btnselectbook;

    private FirebaseUser user;
    private String TAG="MainFlow";
    private DatabaseReference myRef;
    private ArrayList<Book> books = new ArrayList<>();
    CitationAddAdapter addCitationBook;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private EditText edittextsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_citation_book);

        recyclerView = (RecyclerView) findViewById(R.id.addcitationrv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        edittextsearch = (EditText) findViewById(R.id.editsearchaddcitat);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        veriCek();
        searchKontrol();

    }
    private void searchKontrol() {
        edittextsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(edittextsearch.getText().toString());
            }
        });
    }

    private void filter(final String s) {

        if(!TextUtils.isEmpty(s)){
            books.clear();
            myRef.child("Books").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                        Book bk = ds.getValue(Book.class);
                        if(bk.getBook_name().toLowerCase().startsWith(s.toLowerCase())){
                            books.add(bk);
                        }
                    }
                    addCitationBook = new CitationAddAdapter(AddCitationBook.this, books);
                    recyclerView.setAdapter(addCitationBook);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            veriCek();
        }
    }

    private void veriCek() {
        myRef.child("Books").orderByChild("book_rate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Book b = snapshot.getValue(Book.class);
                    books.add(b);
                }

                addCitationBook = new CitationAddAdapter(AddCitationBook.this, books);
                recyclerView.setAdapter(addCitationBook);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
