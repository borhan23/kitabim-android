package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.FeedBackDetail;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Feedbacks;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackHolder> {
    Context c;
    ArrayList<Feedbacks> feedbacks;
    DatabaseReference myRef;
    FirebaseUser firebaseUser;

    public FeedBackAdapter(Context c, ArrayList<Feedbacks> feedbacks){
        this.c = c;
        this.feedbacks = feedbacks;
    }
    @Override
    public FeedBackHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.list_feedbacks,parent,false);

        return new FeedBackHolder(v);
    }

    @Override
    public void onBindViewHolder(FeedBackHolder holder, final int position) {
        final Feedbacks fb = feedbacks.get(position);
        holder.txtfeeddate.setText(fb.getCreate_date());
        holder.txtfeedname.setText(fb.getFeedback_name());
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.btndeletefeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Feedbacks").child(firebaseUser.getUid()).child(fb.getFeed_id()).removeValue();
            }
        });

        holder.btnfeeddetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, FeedBackDetail.class);

                String feedname = feedbacks.get(position).getFeedback_name();
                String feed = feedbacks.get(position).getFeedback();
                String feeddate = feedbacks.get(position).getCreate_date();
                String feedid = feedbacks.get(position).getFeed_id();
                String feedanswer = feedbacks.get(position).getAnswer();

                intent.putExtra("feed_name",feedname);
                intent.putExtra("feed",feed);
                intent.putExtra("feed_date",feeddate);
                intent.putExtra("feed_id",feedid);
                intent.putExtra("feed_answer",feedanswer);

                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }
}
