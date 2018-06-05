package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.ReadingPlan;

public class ReadPlanAdapter extends RecyclerView.Adapter<ReadPlanHolder> {
    Context c;
    ArrayList<ReadingPlan> readingPlans;
    private DatabaseReference myRef;
    LayoutInflater inflater;
    private Book book;

    public ReadPlanAdapter(Context c, ArrayList<ReadingPlan> readingPlans){
        this.c = c;
        this.readingPlans = readingPlans;
    }

    @Override
    public ReadPlanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_reading_plan,parent,false);

        return new ReadPlanHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadPlanHolder holder, int position) {
        final ReadingPlan rp = readingPlans.get(position);
        holder.txtplandate.setText("Tarih: "+rp.getPlanning_date());
        holder.txtplantime.setText("Saat: "+rp.getPlanning_time());
        holder.txtplanpage.setText("Okunacak Sayfa Sayısı: "+String.valueOf(rp.getPlanning_page()));
        myRef = FirebaseDatabase.getInstance().getReference();

        kitapVeriAl(rp.getBook_id(), holder);

        holder.btnplandelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("ReadingPlan").child(rp.getUser_id()).child(rp.getPush_id()).removeValue();
            }
        });
    }

    private void kitapVeriAl(String book_id, final ReadPlanHolder holder) {
        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book = dataSnapshot.getValue(Book.class);
                holder.txtplanbook.setText(book.getBook_name());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return readingPlans.size();
    }
}
