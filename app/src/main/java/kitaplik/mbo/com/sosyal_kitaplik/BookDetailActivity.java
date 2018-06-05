package kitaplik.mbo.com.sosyal_kitaplik;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.BookCommentAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookComment;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.LikedBooks;
import kitaplik.mbo.com.sosyal_kitaplik.entities.RatesLikes;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private String TAG = "detail";
    private DatabaseReference mref;
    private BookStatistic bookstatistic;
    private Book book;
    private DatabaseReference myRef;
    private ArrayList<BookComment> bcomment = new ArrayList<>();
    BookCommentAdapter commentadapter;
    Button btnusercomment, btnrate;
    TextView bookname, bookauthor, bookpagenumber, bookdateofissue, bookdetail, bookrate, txtrated;
    TextInputEditText txtusercomment;
    private FirebaseUser user;
    CheckBox cbspoiler;
    private Button btnlike;
    private Statistic statistic;
    ImageButton btnscrolltop;
    ScrollView scrolldetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);


        ImageView bookimage = (ImageView) findViewById(R.id.grid_book_image);
        bookname = (TextView) findViewById(R.id.grid_book_name);
        bookauthor = (TextView) findViewById(R.id.txtbook_author);
        bookpagenumber = (TextView) findViewById(R.id.txtbook_page);
        bookdateofissue = (TextView) findViewById(R.id.txtdateofissue);
        final TextView likecount = (TextView) findViewById(R.id.txtlikecount);
        final TextView citationcount = (TextView) findViewById(R.id.txtcitationscount);
        bookdetail = (TextView) findViewById(R.id.txtBookDetail);
        bookrate = (TextView) findViewById(R.id.txtbookdetailrate);
        txtrated = (TextView) findViewById(R.id.txtrated);
        RatingBar bookratebar = (RatingBar) findViewById(R.id.ratingBar);
        btnusercomment = (Button) findViewById(R.id.btncomment);
        btnrate = (Button) findViewById(R.id.btnrate);
        btnlike = (Button) findViewById(R.id.btnlike);
        txtusercomment = (TextInputEditText) findViewById(R.id.txtusercomment);
        cbspoiler = (CheckBox) findViewById(R.id.cbspoiler);
        btnscrolltop = (ImageButton) findViewById(R.id.btnscrolltop);
        scrolldetail = (ScrollView) findViewById(R.id.scrolldetail);


        recyclerView = (RecyclerView) findViewById(R.id.listCycleView);

        mref = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle = getIntent().getExtras();



        btnusercomment.setOnClickListener(this);
        btnrate.setOnClickListener(this);
        btnlike.setOnClickListener(this);
        btnscrolltop.setOnClickListener(this);

        bookstatistic = new BookStatistic();

        //Kitaba ait istatistik bilgisinin alınması ve gösterilmesi
        mref.child("BookStatistic").child(bundle.get("book_id").toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                bookstatistic=dataSnapshot.getValue(BookStatistic.class);
                citationcount.setText("Alıntı sayısı: "+bookstatistic.getTotal_citation().toString());
                likecount.setText("Beğeni sayısı: "+bookstatistic.getTotal_like());
            }else{
                citationcount.setText("Alıntı sayısı: 0");
                likecount.setText("Beğeni sayısı: 0");
            }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(bundle.getString("book_name")!= null)
        {
            bookname.setText(bundle.get("book_name").toString());
        }

        if(bundle.getString("book_author")!= null)
        {
            bookauthor.setText("Yazar: "+bundle.get("book_author").toString());
        }

        if(bundle.getString("book_pagenumber")!= null)
        {
            bookpagenumber.setText("Sayfa sayısı: "+bundle.get("book_pagenumber").toString());
        }

        if(bundle.getString("book_dateofissue")!= null)
        {
            bookdateofissue.setText("Basım tarihi: "+bundle.get("book_dateofissue").toString());
        }

        if(bundle.getString("book_detail")!= null)
        {
            bookdetail.setText(bundle.get("book_detail").toString());
        }

        if(bundle.get("book_rate") != null)
        {
            bookrate.setText(String.valueOf(bundle.getFloat("book_rate")));
            bookratebar.setRating(bundle.getFloat("book_rate"));
        }

        if(bundle.getString("book_imageurl")!= null)
        {
            Picasso.get()
                    .load(bundle.get("book_imageurl").toString())
                    .resize(140, 200)
                    .into(bookimage);
        }

        recyclerView = (RecyclerView) findViewById(R.id.bookcommentrview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        myRef =FirebaseDatabase.getInstance().getReference();

        veriCek(bundle.getString("book_id"));
        oyKontrol(bundle.getString("book_id"));
        begenKontrol(bundle.getString("book_id"));
    }

    private void begenKontrol(String book_id) {
        myRef.child("LikedBooks").child(user.getUid()).child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d(TAG,"Beğenmiş"+dataSnapshot);
                    LikedBooks lb = dataSnapshot.getValue(LikedBooks.class);
                    btnlike.setText("Beğendin");

                }else {
                    Log.d(TAG,"Bğenmemiş"+dataSnapshot);
                    btnlike.setText("Beğen");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void oyKontrol(String book_id) {
        myRef.child("RatesLikes").child(book_id).child("rates").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d(TAG,"Oy vermiş"+dataSnapshot);
                    RatesLikes rl = dataSnapshot.getValue(RatesLikes.class);
                    btnrate.setVisibility(View.GONE);
                    txtrated.setText("Oyunuz: "+rl.getUser_rate());
                }else {
                    Log.d(TAG,"Oy vermemiş"+dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void veriCek(String book_id) {
        myRef.child("BookComment").child(book_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                bcomment.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    BookComment bc = snapshot.getValue(BookComment.class);
                    bcomment.add(bc);

                }
                commentadapter = new BookCommentAdapter(getApplicationContext(),bcomment);
                recyclerView.setAdapter(commentadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == btnscrolltop){
            scrolldetail.scrollTo(0,0);
        }

        if(v == btnusercomment){
            Bundle bundle = getIntent().getExtras();
            yorumYap(bundle.getString("book_id"));
        }

        if(v == btnrate){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_book_rate, null);
            final RatingBar ratebar = (RatingBar) mView.findViewById(R.id.ratevalue);
            final Button btnrateconfirm = (Button) mView.findViewById(R.id.btnrateconfirm);
            final TextView txtrate = (TextView) mView.findViewById(R.id.txtrate);

            ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {

                    txtrate.setText(String.valueOf(rating));

                }
            });

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            btnrateconfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = getIntent().getExtras();

                    oyver(bundle.getString("book_id"), ratebar.getRating());
                    dialog.dismiss();

                }
            });
        }
        if(v == btnlike){
            Bundle bundle = getIntent().getExtras();
            begen(bundle.getString("book_id"));
        }
    }

    private void begen(String book_id) {
        LikedBooks likedBooks = new LikedBooks();
        Log.d(TAG, "+++"+btnlike.getText().toString());
        if(btnlike.getText().toString() == "Beğen") {
            likedBooks.setBook_id(book_id);
            likedBooks.setLiked_date(sistemTarihiniGetir());
            likedBooks.setUser_id(user.getUid());

            myRef.child("LikedBooks").child(user.getUid()).child(book_id).setValue(likedBooks);
            btnlike.setText("Beğendin");
            begenGuncelle(book_id);
        }else if(btnlike.getText().toString() == "Beğendin"){
            myRef.child("LikedBooks").child(user.getUid()).child(book_id).removeValue();
            btnlike.setText("Beğen");
            begenGuncelle(book_id);
        }
    }

    private void begenGuncelle(final String book_id) {
        myRef.child("BookStatistic").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                BookStatistic bs = dataSnapshot.getValue(BookStatistic.class);
                bookstatistic= new BookStatistic();

                bookstatistic.setTotal_citation(bs.getTotal_citation());
                bookstatistic.setTotal_rate_number(bs.getTotal_rate_number());
                bookstatistic.setBook_id(bs.getBook_id());
                if(btnlike.getText().toString() == "Beğendin"){
                    Integer total_like = bs.getTotal_like() + 1;
                    bookstatistic.setTotal_like(total_like);
                }else if(btnlike.getText().toString() == "Beğen"){
                    Integer total_like = bs.getTotal_like() - 1;
                    bookstatistic.setTotal_like(total_like);
                }

                myRef.child("BookStatistic").child(book_id).setValue(bookstatistic);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("Statistic").child(user.getUid()).child("total_values").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Statistic st = dataSnapshot.getValue(Statistic.class);
                statistic= new Statistic();
                if(st != null) {
                    Log.d(TAG, "onDataChange: "+st);
                    statistic.setTotal_citation(st.getTotal_citation());
                    statistic.setUser_page_number(st.getUser_page_number());
                    statistic.setUser_id(st.getUser_id());
                    if (btnlike.getText().toString() == "Beğendin") {
                        Integer total_like = st.getLike_count() + 1;
                        statistic.setLike_count(total_like);
                    } else if (btnlike.getText().toString() == "Beğen") {
                        Integer total_like = st.getLike_count() - 1;
                        statistic.setLike_count(total_like);
                    }
                    myRef.child("Statistic").child(user.getUid()).child("total_values").setValue(statistic);
                }else if(st == null){
                    statistic.setTotal_citation(0);
                    statistic.setUser_page_number(0);
                    statistic.setUser_id(user.getUid());
                    statistic.setLike_count(1);
                    myRef.child("Statistic").child(user.getUid()).child("total_values").setValue(statistic);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void oyver(final String book_id, final float rating) {


        RatesLikes rt= new RatesLikes();

        rt.setUser_id(user.getUid());
        rt.setUser_rate(Double.valueOf(rating));
        Log.d(TAG,"++++"+Double.valueOf(rating));
        myRef.child("RatesLikes").child(book_id).child("rates").child(user.getUid()).setValue(rt);
        myRef.child("BookStatistic").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                BookStatistic bs = dataSnapshot.getValue(BookStatistic.class);
                bookstatistic= new BookStatistic();

                bookstatistic.setTotal_citation(bs.getTotal_citation());
                bookstatistic.setTotal_like(bs.getTotal_like());
                Integer rate_number = bs.getTotal_rate_number() + 1;
                bookstatistic.setTotal_rate_number(rate_number);
                bookstatistic.setBook_id(bs.getBook_id());

                myRef.child("BookStatistic").child(book_id).setValue(bookstatistic);

                oyHesapla(bookstatistic.getTotal_rate_number(), rating, book_id);
                btnrate.setVisibility(View.GONE);
                txtrated.setText("Oyunuz: "+rating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
// Verilen oya göre gerekli bilgileri alıp ortalama oyu tekrar hesaplıyor ve yeni oya atıyor.
    private void oyHesapla(final Integer total_rate_number, final float rating, final String book_id) {
        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Book b = dataSnapshot.getValue(Book.class);
                book= new Book();

                book.setBook_name(b.getBook_name());
                book.setBook_author(b.getBook_author());
                book.setShort_description(b.getShort_description());
                book.setPage_number(b.getPage_number());
                book.setBook_category(b.getBook_category());
                book.setBook_id(b.getBook_id());
                book.setDate_of_issue(b.getDate_of_issue());
                book.setRelease_date(b.getRelease_date());
                book.setBook_image(b.getBook_image());

                Double carpim = (total_rate_number-1)*b.getBook_rate(); //Oy verenlerin sayısıyla kitabın ort oyunu çarptım
                Double toplam = carpim + rating;    //yeni verilen oyu bu çarpıma ekledim
                Double yenioy = (double) toplam / total_rate_number;     //yeni çıkan toplamla da verilen oy sayısını bölerek yeni oyuma ulaştım.

                yenioy = yenioy*100;
                yenioy = Double.valueOf(Math.round(yenioy));
                yenioy = yenioy /100;

                book.setBook_rate(yenioy);

                myRef.child("Books").child(book_id).setValue(book);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void yorumYap(String book_id) {

        BookComment bcomment = new BookComment();
        bcomment.setBook_id(book_id);
        bcomment.setComment(txtusercomment.getText().toString());
        bcomment.setComment_date(sistemTarihiniGetir());
        bcomment.setSpoiler(cbspoiler.isChecked());
        bcomment.setUser_id(user.getUid());

        if(!TextUtils.isEmpty(txtusercomment.getText().toString().trim()) && txtusercomment.getText().length() >10){
            myRef.child("BookComment").child(book_id).push().setValue(bcomment);
            txtusercomment.setText("");
            txtusercomment.clearFocus();
        }else if(txtusercomment.getText().length() <10){
            Toast.makeText(this,"Yorumunuz 10 karakterden az olamaz.",Toast.LENGTH_LONG).show();
        }

    }

    public String sistemTarihiniGetir()
    {
        SimpleDateFormat bicim=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date tarih=new Date();
        return bicim.format(tarih);
    }
}
