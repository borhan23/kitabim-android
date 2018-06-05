package kitaplik.mbo.com.sosyal_kitaplik;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import kitaplik.mbo.com.sosyal_kitaplik.entities.Feedbacks;

public class FeedBackTitle extends AppCompatActivity {

    private DatabaseReference myRef;
    private ListView listtitle;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private Feedbacks feedbacks;
    private FirebaseUser firebaseUser;
    private TextView txttitle;
    private String TAG = "feed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_title);

        listtitle = (ListView) findViewById(R.id.listtitle);
        txttitle = (TextView) findViewById(R.id.textblack);
        arrayList = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        myRef.child("FeedBack_title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    arrayList.add(snapshot.getValue().toString());
                }
                arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.list_feedback_title, R.id.textblack,arrayList);
                listtitle.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listtitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                feedback(listtitle.getItemAtPosition(position).toString());

            }
        });
    }

    private void feedback(String s) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_add_feedback, null);
        final EditText editfeedname = (EditText) mView.findViewById(R.id.editfeedbaslik);
        final EditText editfeed = (EditText) mView.findViewById(R.id.editfeed);
        Button btnsavefeed = (Button) mView.findViewById(R.id.btnsendfeedback);
        editfeedname.setText(s);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();


        btnsavefeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedbaslik =  editfeedname.getText().toString().trim();
                String feedback =  editfeed.getText().toString().trim();

                if(TextUtils.isEmpty(feedbaslik) || TextUtils.isEmpty(feedback)){
                    Toast.makeText(getApplicationContext(),"Lütfen boş alan bırakmayınız.",Toast.LENGTH_LONG).show();
                }else{
                    feedbacks = new Feedbacks();
                    feedbacks.setFeedback_name(feedbaslik);
                    feedbacks.setFeedback(feedback);
                    feedbacks.setUser_id(firebaseUser.getUid());
                    feedbacks.setAnswer("");
                    feedbacks.setCreate_date(sistemTarihiniGetir());
                    String pushId = myRef.child("Feedbacks").child(firebaseUser.getUid()).push().getKey();
                    feedbacks.setFeed_id(pushId);
                    myRef.child("Feedbacks").child(firebaseUser.getUid()).child(pushId).setValue(feedbacks);

                    Toast.makeText(getApplicationContext(), "Talebiniz oluşturuldu",Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    finish();
                }
            }
        });
    }

    public String sistemTarihiniGetir()
    {
        SimpleDateFormat bicim=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date tarih=new Date();
        return bicim.format(tarih);
    }

}
