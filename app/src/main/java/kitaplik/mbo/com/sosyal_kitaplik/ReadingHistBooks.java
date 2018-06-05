package kitaplik.mbo.com.sosyal_kitaplik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.HistoryBookAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;

public class ReadingHistBooks extends AppCompatActivity {
    private TextView txtaddcitatbookname;
    private Button btnselectbook;

    private FirebaseUser user;
    private String TAG="MainFlow";
    private DatabaseReference myRef;
    private ArrayList<Library> library = new ArrayList<>();
    HistoryBookAdapter historyBookAdapter;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_hist_books);

        recyclerView = (RecyclerView) findViewById(R.id.readinghistoryrv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);


        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        veriCek();
    }

    private void veriCek() {
        myRef.child("Library").child(firebaseUser.getUid()).child("read_record").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                library.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Library lb = snapshot.getValue(Library.class);
                    library.add(lb);
                }

                historyBookAdapter = new HistoryBookAdapter(ReadingHistBooks.this, library);
                recyclerView.setAdapter(historyBookAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
