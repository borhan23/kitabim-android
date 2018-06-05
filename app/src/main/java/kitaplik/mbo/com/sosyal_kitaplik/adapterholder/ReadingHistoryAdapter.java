package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;

public class ReadingHistoryAdapter extends RecyclerView.Adapter<ReadingHistoryHolder> {
    Context c;
    ArrayList<Statistic> statistic;
    private String TAG = "comment";

    public ReadingHistoryAdapter(Context c, ArrayList<Statistic> statistic){
        this.c = c;
        this.statistic = statistic;
    }

    @Override
    public ReadingHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.list_history,parent,false);
        return new ReadingHistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(ReadingHistoryHolder holder, int position) {
        Statistic st = statistic.get(position);
        holder.txthistorydate.setText("Tarih: "+st.getReaded_date());
        holder.txthistorypage.setText("Okunan Sayfa Sayısı: "+String.valueOf(st.getReaded_page()));
        holder.txthistorytime.setText("Geçirilen Zaman: "+String.valueOf(st.getRead_time())+"dk.");
    }

    @Override
    public int getItemCount() {
        return statistic.size();
    }
}
