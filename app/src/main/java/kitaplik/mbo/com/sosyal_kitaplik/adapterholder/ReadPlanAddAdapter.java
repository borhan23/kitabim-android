package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import kitaplik.mbo.com.sosyal_kitaplik.AddReadPlanBook;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.ReadingPlan;

public class ReadPlanAddAdapter extends RecyclerView.Adapter<ReadPlanAddHolder> {
    Context c;
    ArrayList<Book> books;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG="citation";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    ReadingPlan readingPlan;

    public ReadPlanAddAdapter(Activity c, ArrayList<Book> books){
        this.c = c;
        this.books = books;

    }

    @Override
    public ReadPlanAddHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.list_citation_add_book,parent,false);
        return new ReadPlanAddHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReadPlanAddHolder holder, int position) {
        final Book b = books.get(position);
        holder.txtaddcitatbookname.setText(b.getBook_name());
        holder.btnselectbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
                View mView = LayoutInflater.from(c).inflate(R.layout.dialog_add_readplan, null);

                final TextView txtplanbook = (TextView) mView.findViewById(R.id.txtplanbook);
                final ImageView imageplanbook = (ImageView) mView.findViewById(R.id.planbookimage);
                final EditText editplanpage = (EditText) mView.findViewById(R.id.txtplanpage);
                final EditText dateDisplay = (EditText) mView.findViewById(R.id.edittext_tarih);
                final EditText timeDisplay = (EditText) mView.findViewById(R.id.edittext_saat);
                Button btndate = (Button) mView.findViewById(R.id.button_tarih_sec);
                Button btntime = (Button) mView.findViewById(R.id.button_saat_sec);
                Button btnaddplan = (Button) mView.findViewById(R.id.btnplansave);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                txtplanbook.setText(b.getBook_name());
                Picasso.get()
                        .load(b.getBook_image())
                        .resize(90, 130)
                        .into(imageplanbook);

                myRef = FirebaseDatabase.getInstance().getReference();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                readingPlan = new ReadingPlan();

                btntime.setOnClickListener(new View.OnClickListener() {//saatButona Click Listener ekliyoruz

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();//
                        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);//Güncel saati aldık
                        final int minute = mcurrentTime.get(Calendar.MINUTE);//Güncel dakikayı aldık
                        TimePickerDialog timePicker; //Time Picker referansımızı oluşturduk

                        //TimePicker objemizi oluşturuyor ve click listener ekliyoruz
                        timePicker = new TimePickerDialog(c, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                if(selectedHour < 10 && selectedMinute < 10){
                                    timeDisplay.setText("0"+selectedHour + ":" + "0" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }
                                if(selectedHour < 10 && selectedMinute >= 10){
                                    timeDisplay.setText("0"+selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }
                                if(selectedHour >= 10 && selectedMinute < 10){
                                    timeDisplay.setText(selectedHour + ":" + "0" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }
                                if(selectedHour >= 10 && selectedMinute >= 10){
                                    timeDisplay.setText(selectedHour + ":" + selectedMinute);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }

                            }
                        }, hour, minute, true);//true 24 saatli sistem için
                        timePicker.setTitle("Saat Seçiniz");
                        timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", timePicker);
                        timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", timePicker);

                        timePicker.show();
                    }
                });

                btndate.setOnClickListener(new View.OnClickListener() {//tarihButona Click Listener ekliyoruz

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Calendar mcurrentTime = Calendar.getInstance();
                        int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                        int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                        DatePickerDialog datePicker;//Datepicker objemiz
                        datePicker = new DatePickerDialog(c, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                monthOfYear += 1;
                                if(monthOfYear < 10 && dayOfMonth < 10){
                                    dateDisplay.setText("0"+dayOfMonth + "/" +"0"+ monthOfYear + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }
                                if(monthOfYear >= 10 && dayOfMonth < 10){
                                    dateDisplay.setText("0"+dayOfMonth + "/" + monthOfYear + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }
                                if(monthOfYear < 10 && dayOfMonth >= 10){
                                    dateDisplay.setText(dayOfMonth + "/" + "0" + monthOfYear + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }
                                if(monthOfYear >= 10 && dayOfMonth >= 10){
                                    dateDisplay.setText(dayOfMonth + "/" + monthOfYear + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                }

                            }
                        }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                        datePicker.setTitle("Tarih Seçiniz");
                        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                        datePicker.show();

                    }
                });

                btnaddplan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(editplanpage.getText().toString().trim()) && !dateDisplay.getText().toString().matches("") && !timeDisplay.getText().toString().matches("")) {
                            final Integer planpage = Integer.parseInt(editplanpage.getText().toString().trim());
                            final String plantime = timeDisplay.getText().toString().trim();
                            final String plandate = dateDisplay.getText().toString().trim();
                            if(planpage <= b.getPage_number()) {
                                readingPlan.setBook_id(b.getBook_id());
                                readingPlan.setPlanning_page(planpage);
                                readingPlan.setPlanning_date(plandate);
                                readingPlan.setPlanning_time(plantime);
                                readingPlan.setUser_id(firebaseUser.getUid());

                                String pushId = myRef.child("ReadingPlan").child(firebaseUser.getUid()).push().getKey();
                                readingPlan.setPush_id(pushId);

                                myRef.child("ReadingPlan").child(firebaseUser.getUid()).child(pushId).setValue(readingPlan);

                                Toast.makeText(c, "Başarıyla kaydedildi.", Toast.LENGTH_LONG).show();
                                ((AddReadPlanBook)c).finish();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(c,"Girdiğiniz sayfa sayısı kitabın sayfa sayısından büyük",Toast.LENGTH_LONG).show();
                            }
                        }else {
                        Toast.makeText(c,"Alanları eksiksiz doldurunuz.",Toast.LENGTH_LONG).show();
                    }

                    }
                });

            }
        });
    }
    @Override
    public int getItemCount() {
        return books.size();
    }
}

