package kitaplik.mbo.com.sosyal_kitaplik;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.BookStatistic;
import kitaplik.mbo.com.sosyal_kitaplik.entities.ReadingPlan;
import kitaplik.mbo.com.sosyal_kitaplik.entities.User;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.BookMainFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.BooksFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.CitationsFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.FeedbackFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.FlowFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.LibraryFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.LikesFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.NewBooksFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.ProfilSettingsFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.ReadPlanFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.StatisticFragment;
import kitaplik.mbo.com.sosyal_kitaplik.fragments.TrendsFragment;

public class MainFlow extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , ProfilSettingsFragment.OnFragmentInteractionListener, LibraryFragment.OnFragmentInteractionListener
    , LikesFragment.OnFragmentInteractionListener, CitationsFragment.OnFragmentInteractionListener, StatisticFragment.OnFragmentInteractionListener, ReadPlanFragment.OnFragmentInteractionListener
    , FeedbackFragment.OnFragmentInteractionListener, BooksFragment.OnFragmentInteractionListener, TrendsFragment.OnFragmentInteractionListener, NewBooksFragment.OnFragmentInteractionListener, BookMainFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    private FirebaseUser firebaseUser;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference mdataref;
    Fragment fragment= null;
    private CircleImageView imageuser;
    private  TextView txtName;
    private TextView txtUsername;
    private String TAG="zamankontrol";
    private BottomNavigationView navigation;
    private static final int NOTIF_ID = 1;

//Ekranın altındaki gezinme barı
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction transaction=fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_book:
                    transaction.replace(R.id.frameLayout, new BookMainFragment()).commit();
                    return true;
                case R.id.navigation_flow:
                    transaction.replace(R.id.frameLayout ,new FlowFragment()).commit();
                    return true;
                case R.id.navigation_trend:
                    transaction.replace(R.id.frameLayout, new TrendsFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flow);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mdataref = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) { // when auth state change
                    finish();
                }
            }
        };

        goFlow(2);


        mTextMessage = (TextView) findViewById(R.id.message);

    }

    public String sistemTarihiniGetir()
    {
        SimpleDateFormat bicim=new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date tarih=new Date();
        return bicim.format(tarih);
    }

    private void goFlow(int sayi) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mdataref.child("ReadingPlan").child(firebaseUser.getUid()).orderByChild("planning_date").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            ReadingPlan rp = snapshot.getValue(ReadingPlan.class);
                            String zaman = rp.getPlanning_date() + " " + rp.getPlanning_time();
                            String gercekzaman = sistemTarihiniGetir();
                            if(zaman.equals(gercekzaman)){
                                bildir(rp.getBook_id(), rp.getPlanning_page());
                                mdataref.child("ReadingPlan").child(firebaseUser.getUid()).child(rp.getPush_id()).removeValue();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        },0, 20000);

        switch (sayi) {
            case 2:
                transaction.replace(R.id.frameLayout ,new FlowFragment()).commit();
                navigation.setSelectedItemId(R.id.navigation_flow);
                break;
        }

    }

    private void bildir(String book_id, final Integer planning_page) {
        Intent resultIntent = new Intent(this, ReadPlanFragment.class);
        final PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), resultIntent, 0);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mdataref.child("Books").child(book_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Book b = dataSnapshot.getValue(Book.class);

                mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(b.getBook_name()+" kitabından \n"+planning_page+" sayfa okumayı unutmayın :)").setBigContentTitle("Okuma vaktiniz geldi"));
                mBuilder.setSmallIcon(R.drawable.book);
                mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                mBuilder.setAutoCancel(true);
                mBuilder.setContentIntent(pIntent);


                // NotificationManager nesnesi oluşturuyoruz.
                NotificationManager notificationManager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);
                // NotificationManager ile bildirimi inşa ediyoruz.
                notificationManager.notify(0,mBuilder.build());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void veriCek() {
        mdataref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User u = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);

                    txtName.setText(u.getFull_name());
                    txtUsername.setText(u.getUser_name());
                    if(u.getUser_image() == null){

                    }else {
                        Picasso.get()
                                .load(u.getUser_image())
                                .resize(300, 300)
                                .centerCrop()
                                .into(imageuser);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_flow, menu);

        imageuser = (CircleImageView) findViewById(R.id.user_image);
        txtName = (TextView) findViewById(R.id.txtName);
        txtUsername = (TextView) findViewById(R.id.txtUserName);

        veriCek();
        return true;
    }

//Yan açılır menü
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_library) {
            transaction.replace(R.id.frameLayout, new LibraryFragment()).commit();
        } else if (id == R.id.nav_likes) {
            transaction.replace(R.id.frameLayout, new LikesFragment()).commit();
        } else if (id == R.id.nav_citations) {
            transaction.replace(R.id.frameLayout, new CitationsFragment()).commit();
        } else if (id == R.id.nav_statistic) {
            transaction.replace(R.id.frameLayout, new StatisticFragment()).commit();
        } else if (id == R.id.nav_plans) {
            transaction.replace(R.id.frameLayout, new ReadPlanFragment()).commit();
        } else if (id == R.id.nav_settings){
            transaction.replace(R.id.frameLayout, new ProfilSettingsFragment()).commit();
        }else if (id == R.id.nav_feedback){
            transaction.replace(R.id.frameLayout, new FeedbackFragment()).commit();
        }else if (id == R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainFlow.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
