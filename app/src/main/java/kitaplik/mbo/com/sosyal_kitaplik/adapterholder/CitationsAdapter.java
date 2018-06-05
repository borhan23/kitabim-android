package kitaplik.mbo.com.sosyal_kitaplik.adapterholder;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Citations;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.User;

public class CitationsAdapter extends RecyclerView.Adapter<CitationsHolder> {
    Context c;
    ArrayList<Citations> citations;
    DatabaseReference databaseReference;
    LayoutInflater inflater;
    private ImageView imageUser;
    private String TAG="citation";
    private FirebaseUser firebaseUser;
    private DatabaseReference myRef;
    private User user;
    private Book book;
    private Statistic statistic;
    private Library library;
    private String userName = "";
    private String fullName = "";
    private String userimage = "";
    private Integer readpage = 0;
    private Integer citatcount = 0;
    private String readingbook = "";
    ListView listreading;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    public CitationsAdapter(Context c, ArrayList<Citations> citations){
        this.c = c;
        this.citations = citations;
    }

    @Override
    public CitationsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(c).inflate(R.layout.list_item,parent,false);

        Log.d(TAG,"On create");
        return new CitationsHolder(v);
    }

    @Override
    public void onBindViewHolder(CitationsHolder holder, int position) {
    final Citations ct = citations.get(position);
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    myRef = FirebaseDatabase.getInstance().getReference();
    user = new User();
    kullaniciBilgisi(holder, ct.getUser_id());
    kitapVeriAl(ct.getBook_id(), holder);
    holder.flowcontentpage.setText(ct.getCitation_page()+".Sayfa");
    holder.flowcontentTxt.setText(ct.getContent());
    holder.txtcitationdate.setText(ct.getCitation_Date());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
                View mView = inflater.inflate(R.layout.dialog_userinfo, null);
                final ImageView mUserImage = (ImageView) mView.findViewById(R.id.dUserImage);
                final TextView mName = (TextView) mView.findViewById(R.id.dName);
                final TextView mUsername = (TextView) mView.findViewById(R.id.dUserName);
                final TextView mBookCount = (TextView) mView.findViewById(R.id.dBookCount);
                final TextView mPaperCount = (TextView) mView.findViewById(R.id.dPaperCount);
                final TextView mCitatCount = (TextView) mView.findViewById(R.id.dCitatCount);
                listreading = (ListView) mView.findViewById(R.id.listreading);
                arrayList = new ArrayList<>();



//Profil kartında kullanıcının kişisel bilgilerini çekmek için
                myRef.child("Users").child(ct.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        user = dataSnapshot.getValue(User.class);
                        userName = user.getUser_name();
                        fullName = user.getFull_name();
                        userimage = user.getUser_image();
                        kullaniciVerisi(userName, fullName, userimage, mUserImage, mName, mUsername);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

//Profil kartında kullanıcı istatistiklerini çekmek için
                myRef.child("Statistic").child(ct.getUser_id()).child("total_values").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        statistic = dataSnapshot.getValue(Statistic.class);
                        readpage = statistic.getUser_page_number();
                        citatcount = statistic.getTotal_citation();
                        istatistikVerisi(readpage, citatcount, mPaperCount, mCitatCount);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//Profil kartında okunan kitap sayısı gösterilmesi
                kitapsayisi(mBookCount, ct.getUser_id());

//Profil Kartında okunan kitabın gösterilmesi
                myRef.child("Library").child(ct.getUser_id()).child("reading_book").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            library = snapshot.getValue(Library.class);
                            if(library != null) {
                                kitapverisi(arrayAdapter, listreading, arrayList, library.getBook_id());
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });



    }

    private void kitapsayisi(final TextView mBookCount, String user_id) {
        final ArrayList<String> arrayList1 = new ArrayList<>();
        myRef.child("Library").child(user_id).child("readed_book").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList1.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    library = snapshot.getValue(Library.class);
                    if(library != null) {
                        arrayList1.add(library.getBook_id());
                    }
                }
                Log.d(TAG, "onDataChange: "+arrayList1.size());
                mBookCount.setText("Okunan Kitap Sayısı: "+arrayList1.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kitapverisi(ArrayAdapter<String> arrayAdapter, final ListView listreading, final ArrayList<String> arrayList, String bookid) {
        book = new Book();
        arrayAdapter=new ArrayAdapter<String>(c,android.R.layout.simple_list_item_1,arrayList);
        final ArrayAdapter<String> finalArrayAdapter = arrayAdapter;
        myRef.child("Books").child(bookid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                book = dataSnapshot.getValue(Book.class);
                String bookname = book.getBook_name();
                String bookimage = book.getBook_image();
                arrayList.add(bookname);
                listreading.setAdapter(finalArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void istatistikVerisi(Integer readpage, Integer citatcount, TextView mPaperCount, TextView mCitatCount) {
        mPaperCount.setText("Okunan Sayfa: "+readpage.toString());
        mCitatCount.setText("Alıntı Sayısı: "+citatcount.toString());
    }

    private void kullaniciVerisi(String userName, String fullName, String userimage, ImageView mUserImage, TextView mName, TextView mUsername) {
        mUsername.setText(userName);
        mName.setText(fullName);
        if(userimage == ""){

        }else {
            Picasso.get()
                    .load(userimage)
                    .resize(100, 100)
                    .centerCrop()
                    .into(mUserImage);
        }
    }

    private void kitapVeriAl(String book_id, final CitationsHolder holder) {
        book = new Book();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                book=dataSnapshot.getValue(Book.class);
                holder.flowcontentbook.setText(book.getBook_name()+", "+book.getBook_author());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kullaniciBilgisi(final CitationsHolder holder, String user_id) {
        myRef.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = dataSnapshot.getValue(User.class);
                holder.nameTxt.setText(user.getFull_name());
                holder.usernameTxt.setText(user.getUser_name());

                if(user.getUser_image() == null){

                }else {
                    Picasso.get()
                            .load(user.getUser_image())
                            .resize(50, 50)
                            .centerCrop()
                            .into(holder.imageUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return citations.size();
    }
}
