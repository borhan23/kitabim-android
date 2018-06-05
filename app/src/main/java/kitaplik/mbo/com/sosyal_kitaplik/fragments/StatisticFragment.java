package kitaplik.mbo.com.sosyal_kitaplik.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.ReadingHistBooks;
import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.HistoryBookAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatisticFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageButton btnrecords;
    private TextView txtuserpage, txtuserreaddate, txtuserlike, txtusercitation;
    private Statistic statistic;
    private String TAG = "statistic";
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    private Integer toplamZaman = 0;

    private OnFragmentInteractionListener mListener;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        btnrecords = (ImageButton) view.findViewById(R.id.btnuserrecords);
        txtuserpage = (TextView) view.findViewById(R.id.txtuserreadpage);
        txtuserreaddate = (TextView) view.findViewById(R.id.txtuserreadtime);
        txtusercitation = (TextView) view.findViewById(R.id.txtusercitat);
        txtuserlike = (TextView) view.findViewById(R.id.txtuserlike);

        btnrecords.setOnClickListener(this);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        statistic = new Statistic();

        veriCek();

        return view;
    }

    private void veriCek() {
        myRef.child("Statistic").child(firebaseUser.getUid()).child("total_values").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statistic = dataSnapshot.getValue(Statistic.class);
                if(statistic != null) {
                    txtuserlike.setText(String.valueOf(statistic.getLike_count()));
                    txtusercitation.setText(String.valueOf(statistic.getTotal_citation()));
                    txtuserpage.setText(String.valueOf(statistic.getUser_page_number()));
                    okumasuresi();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void okumasuresi() {
        myRef.child("Statistic").child(firebaseUser.getUid()).child("read_dates").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot snapshot2:snapshot.getChildren()) {
                        statistic = snapshot2.getValue(Statistic.class);
                        if(statistic != null) {
                            toplamZaman = toplamZaman + statistic.getRead_time();
                        }
                    }
                }
                txtuserreaddate.setText(String.valueOf(toplamZaman));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnrecords){
            Intent intent =  new Intent(getContext(), ReadingHistBooks.class);
            startActivity(intent);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
