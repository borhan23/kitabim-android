package kitaplik.mbo.com.sosyal_kitaplik;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import kitaplik.mbo.com.sosyal_kitaplik.R;

public class FeedBackDetail extends AppCompatActivity {
    TextView detailfeedname, detailfeedcontent, feedanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_detail);

        detailfeedname = (TextView) findViewById(R.id.feeddetailname);
        detailfeedcontent = (TextView) findViewById(R.id.feeddetailcontent);
        feedanswer = (TextView) findViewById(R.id.feedanswer);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("feed_name")!= null)
        {
            detailfeedname.setText("Talep Başlığı: "+bundle.get("feed_name").toString());
        }

        if(bundle.getString("feed")!= null)
        {
            detailfeedcontent.setText(bundle.get("feed").toString());
        }

        if(bundle.getString("feed_answer") != null && TextUtils.isEmpty(bundle.getString("feed_answer").toString()))
        {
            feedanswer.setText("Henüz cevap yok...");
        } else if(bundle.getString("feed_answer") != null && bundle.getString("feed_answer").toString() != "")
        {
            feedanswer.setText(bundle.get("feed_answer").toString());

        }

    }
}
