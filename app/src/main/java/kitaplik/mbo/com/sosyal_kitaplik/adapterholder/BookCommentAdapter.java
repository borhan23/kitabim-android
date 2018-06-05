package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookComment;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.User;

public class BookCommentAdapter extends RecyclerView.Adapter<BookCommentHolder> {
    Context c;
    ArrayList<BookComment> bcomment;
    private String TAG = "comment";
    private DatabaseReference myRef;
    User userinfo;

    public BookCommentAdapter(Context c, ArrayList<BookComment> bcomment){
        this.c = c;
        this.bcomment = bcomment;
    }

    @Override
    public BookCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.list_book_comment,parent,false);
        return new BookCommentHolder(v);
    }

    @Override
    public void onBindViewHolder(final BookCommentHolder holder, int position) {
        BookComment bc = bcomment.get(position);
        holder.txtcomment.setText(bc.getComment());
        holder.txtcommentdate.setText(bc.getComment_date());

        if(bc.isSpoiler() == true){
            holder.txtspoiler.setVisibility(View.VISIBLE);
            holder.btnspoiler.setVisibility(View.VISIBLE);
            holder.txtcomment.setVisibility(View.GONE);
        }else
        {
            holder.txtspoiler.setVisibility(View.GONE);
            holder.btnspoiler.setVisibility(View.GONE);
        }

        holder.btnspoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.txtcomment.getVisibility() == View.GONE) {
                    holder.txtcomment.setVisibility(View.VISIBLE);
                }else if(holder.txtcomment.getVisibility() == View.VISIBLE) {
                    holder.txtcomment.setVisibility(View.GONE);
                }
            }
        });

        kullanıcıvericek(bc.getUser_id(), holder);
        Log.d(TAG,bc.getComment());
    }

    private void kullanıcıvericek(String user_id, final BookCommentHolder holder) {
        myRef = FirebaseDatabase.getInstance().getReference();

        userinfo = new User();

        myRef.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userinfo=dataSnapshot.getValue(User.class);
                holder.txtusername.setText(userinfo.getUser_name());
                holder.txtname.setText(userinfo.getFull_name());
                Picasso.get()
                        .load(userinfo.getUser_image())
                        .resize(100, 100)
                        .centerCrop()
                        .into(holder.userimage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return bcomment.size();
    }
}
