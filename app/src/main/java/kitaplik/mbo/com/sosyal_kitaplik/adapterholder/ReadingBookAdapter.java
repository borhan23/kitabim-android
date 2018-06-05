package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;

public class ReadingBookAdapter extends RecyclerView.Adapter<ReadingBookHolder> {
    Context c;
    ArrayList<Library> library;
    ArrayList<Book> books;
    Map<String, Object> updates = new HashMap<String,Object>();
    Map<String, Object> updateTotalPage = new HashMap<String,Object>();
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private String TAG = "reading";
    private RecyclerView recyclerView;
    private DatabaseReference myRef;
    Book book;
    Library lib;
    Statistic statistic;
    private Integer sayfa = 0;
    private FirebaseUser firebaseUser;
    private Integer istatistikpage = 0;

    public ReadingBookAdapter(Context c, ArrayList<Library> library) {
        this.c = c;
        this.library = library;
    }

    @Override
    public ReadingBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.list_reading_book, parent, false);

        Log.d(TAG, "On create");
        return new ReadingBookHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReadingBookHolder holder, final int position) {
        Log.d(TAG, "On Create");
        final Library lb = library.get(position);
        kontrolTarih(holder, lb.getBook_id(), lb.getStarted_date());
        kitapVeriAl(lb.getBook_id(), holder);
        //kitapKalanSayfa(lb.getBook_id(), holder);
        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Library").child(firebaseUser.getUid()).child("reading_book").child(lb.getBook_id()).removeValue();

            }
        });
        holder.btnrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
                View mView = LayoutInflater.from(c).inflate(R.layout.dialog_read_save, null);
                final EditText txtreadpage = (EditText) mView.findViewById(R.id.txtreadpage);
                final EditText txtreadtime = (EditText) mView.findViewById(R.id.txtreadtime);
                final EditText dateDisplay = (EditText) mView.findViewById(R.id.edittext_tarih);
                Button btnsubmitrecord = (Button) mView.findViewById(R.id.btnreadsave);
                Button btndate = (Button) mView.findViewById(R.id.button_tarih_sec);
                Button btnreadsave = (Button) mView.findViewById(R.id.btnreadsave);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                btndate.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                                                   final Calendar takvim = Calendar.getInstance();
                                                   int yil = takvim.get(Calendar.YEAR);
                                                   int ay = takvim.get(Calendar.MONTH);
                                                   int gun = takvim.get(Calendar.DAY_OF_MONTH);

                                                   DatePickerDialog dpd = new DatePickerDialog(c,
                                                           new DatePickerDialog.OnDateSetListener() {
                                                               @Override
                                                               public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                                   // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                                                   // değeri 1 artırarak gösteriyoruz.
                                                                   month += 1;
                                                                   if(month < 10 && dayOfMonth < 10){
                                                                       dateDisplay.setText("0"+dayOfMonth + "/" +"0"+ month + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                                                   }
                                                                   if(month >= 10 && dayOfMonth < 10){
                                                                       dateDisplay.setText("0"+dayOfMonth + "/" + month + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                                                   }
                                                                   if(month < 10 && dayOfMonth >= 10){
                                                                       dateDisplay.setText(dayOfMonth + "/" + "0" + month + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                                                   }
                                                                   if(month >= 10 && dayOfMonth >= 10){
                                                                       dateDisplay.setText(dayOfMonth + "/" + month + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                                                                   }
                                                                   // year, month ve dayOfMonth değerleri seçilen tarihin değerleridir.
                                                                   // Edittextte bu değerleri gösteriyoruz.
                                                               }
                                                           }, yil, ay, gun);
                                                   // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                                                   // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.

                                                   // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
                                                   dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                                                   dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                                                   dpd.show();

                                               }
                });

                myRef.child("Books").child(lb.getBook_id()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        book=dataSnapshot.getValue(Book.class);
                        sayfa = book.getPage_number();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                btnreadsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!TextUtils.isEmpty(txtreadpage.getText().toString().trim()) && !TextUtils.isEmpty(txtreadtime.getText().toString().trim()) && !dateDisplay.getText().toString().matches("")){
                        final Integer readpage = Integer.parseInt(txtreadpage.getText().toString().trim());
                        final Integer readtime = Integer.parseInt(txtreadtime.getText().toString().trim());
                        final String readdate = dateDisplay.getText().toString().trim();
                        final Integer getpage = Integer.parseInt(holder.txtpaper.getText().toString().trim());
                        if(sayfa > readpage && readpage <= getpage) {
                            Log.d(TAG, "onClick:++++ "+sayfa+"   "+readpage);
                            lb.setReaded_page(readpage);
                            lb.setTotal_reading_time(readtime);
                            lb.setUser_id(firebaseUser.getUid());
                            lib = new Library();
                            myRef.child("Library").child(firebaseUser.getUid()).child("read_record").child(lb.getBook_id())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            lib = dataSnapshot.getValue(Library.class);
                                            if (lib != null) {
                                                Integer toplampage = lib.getReaded_page() + lb.getReaded_page();
                                                Integer toplamzaman = lib.getTotal_reading_time() + lb.getTotal_reading_time();
                                                if (toplampage > sayfa) {
                                                    toplampage = sayfa - lib.getReaded_page();
                                                    istatistikpage = sayfa - lib.getReaded_page();
                                                    toplampage = toplampage + lib.getReaded_page();
                                                    lb.setReaded_page(toplampage);
                                                } else {
                                                    istatistikpage = readpage;
                                                    lb.setReaded_page(toplampage);
                                                }
                                                lb.setTotal_reading_time(toplamzaman);
                                                myRef.child("Library").child(firebaseUser.getUid()).child("read_record").child(lb.getBook_id()).setValue(lb);
                                                kitapKalanSayfa(lb.getBook_id(), holder, toplampage, lb);
                                                istatistikKontrol(lb.getBook_id(), istatistikpage, readdate, readtime);
                                                istatistikTopSayfa(istatistikpage);
                                            } else if (lib == null) {
                                                myRef.child("Library").child(firebaseUser.getUid()).child("read_record").child(lb.getBook_id()).setValue(lb);
                                                kitapKalanSayfa(lb.getBook_id(), holder, lb.getReaded_page(), lb);
                                                istatistikKontrol(lb.getBook_id(), readpage, readdate, readtime);
                                                istatistikTopSayfa(readpage);
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                            Toast.makeText(c, "Başarıyla kaydedildi.", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(c,"Girdiğiniz sayfa sayısı kalan sayfa sayısından büyük",Toast.LENGTH_LONG).show();
                        }
                        }else {
                            Toast.makeText(c,"Alanları eksiksiz doldurunuz.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void istatistikTopSayfa(final Integer toplampage) {
        myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        statistic=dataSnapshot.getValue(Statistic.class);
                        if(statistic != null) {
                            updateTotalPage.put("like_count", statistic.getLike_count());
                            updateTotalPage.put("total_citation", statistic.getTotal_citation());
                            updateTotalPage.put("user_id", statistic.getUser_id());
                            Integer totalpage = toplampage + statistic.getUser_page_number();
                            updateTotalPage.put("user_page_number", totalpage);
                            myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").setValue(updateTotalPage);
                        }else  if(statistic == null) {
                            updateTotalPage.put("like_count", 0);
                            updateTotalPage.put("total_citation", 0);
                            updateTotalPage.put("user_id", statistic.getUser_id());
                            updateTotalPage.put("user_page_number", toplampage);
                            myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").setValue(updateTotalPage);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void istatistikKontrol(final String book_id, final Integer toplampage, final String readdate, final Integer readtime) {
        statistic = new Statistic();
        myRef.child("Statistic").child(firebaseUser.getUid()).child("read_dates").child(book_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        statistic=dataSnapshot.getValue(Statistic.class);
                        if(statistic != null) {
                            statistic.setBook_id(book_id);
                            statistic.setReaded_date(readdate);
                            statistic.setReaded_page(toplampage);
                            statistic.setRead_time(readtime);
                            myRef.child("Statistic").child(firebaseUser.getUid()).child("read_dates").child(book_id).push().setValue(statistic);
                        }else  if(statistic == null) {
                            statistic.setBook_id(book_id);
                            statistic.setReaded_date(readdate);
                            statistic.setReaded_page(toplampage);
                            statistic.setRead_time(readtime);
                            myRef.child("Statistic").child(firebaseUser.getUid()).child("read_dates").child(book_id).push().setValue(statistic);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void kontrolTarih(final ReadingBookHolder holder, String book_id, final String started_date) {
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef.child("Library").child(firebaseUser.getUid()).child("read_record").child(book_id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        lib=dataSnapshot.getValue(Library.class);
                        if(lib != null) {
                            holder.txtdate.setText("Başlama Tarihi: "+lib.getStarted_date());
                        }else  if(lib == null) {
                            holder.txtdate.setText("Başlama Tarihi: "+started_date);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void kitapKalanSayfa(final String book_id, final ReadingBookHolder holder, final Integer toplampage, final Library lb) {
        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                book=dataSnapshot.getValue(Book.class);
                if(toplampage < book.getPage_number()) {
                    Integer kalansayfa = book.getPage_number() - toplampage;
                    holder.txtpaper.setText(kalansayfa.toString());
                }else if(toplampage >= book.getPage_number()){
                    holder.txtremainpaper.setText("Kitap tamamlandı.");
                    holder.txtpaper.setText("");
                    holder.btnrecord.setEnabled(false);
                    Toast.makeText(c,"Tebrikler kitabı tamamladınız.",Toast.LENGTH_LONG).show();
                    updates.put("book_id",lb.getBook_id());
                    updates.put("user_id",firebaseUser.getUid());
                    myRef.child("Library").child(firebaseUser.getUid()).child("readed_book").child(book_id).setValue(updates);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kitapVeriAl(final String book_id, final ReadingBookHolder holder) {
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        book = new Book();

        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                book=dataSnapshot.getValue(Book.class);
                holder.txtbookname.setText(book.getBook_name());
                holder.txtbookrate.setText(String.valueOf(book.getBook_rate()));
                holder.bookratebar.setRating(book.getBook_rate().floatValue());
                Picasso.get()
                        .load(book.getBook_image())
                        .resize(120, 160)
                        .into(holder.bookimage);

                lib = new Library();
                myRef.child("Library").child(firebaseUser.getUid()).child("read_record").child(book_id)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                lib=dataSnapshot.getValue(Library.class);
                                if(lib != null) {
                                    Log.d(TAG, "t+++ " + lib.getTotal_reading_time());
                                    if (lib.getReaded_page() >= book.getPage_number()) {
                                        holder.txtremainpaper.setText("Kitap tamamlandı.");
                                        holder.txtpaper.setText("");
                                        holder.btnrecord.setEnabled(false);
                                    } else if (lib.getTotal_reading_time() < book.getPage_number()) {
                                        Integer kalansayfa = book.getPage_number() - lib.getReaded_page();
                                        holder.txtpaper.setText(kalansayfa.toString());
                                    }
                                }else  if(lib == null) {
                                    holder.txtpaper.setText(book.getPage_number().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return library.size();
    }
}
