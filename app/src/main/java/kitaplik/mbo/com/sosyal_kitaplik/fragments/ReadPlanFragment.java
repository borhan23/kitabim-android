package kitaplik.mbo.com.sosyal_kitaplik.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.AddReadPlanBook;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.ReadPlanAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.ReadingPlan;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Statistic;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadPlanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadPlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadPlanFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private FloatingActionButton btnaddplan;
    private FirebaseUser firebaseUser;
    private String TAG = "citation";
    private ArrayList<ReadingPlan> readingPlans = new ArrayList<>();
    ReadPlanAdapter readPlanAdapter;

    public ReadPlanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadPlanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadPlanFragment newInstance(String param1, String param2) {
        ReadPlanFragment fragment = new ReadPlanFragment();
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
        View view = inflater.inflate(R.layout.fragment_read_plan, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = (RecyclerView) view.findViewById(R.id.readingplanrv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        btnaddplan = (FloatingActionButton) view.findViewById(R.id.btnaddreadplan);

        btnaddplan.setOnClickListener(this);

        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        veriCek();

        return view;
    }

    private void veriCek() {

        myRef.child("ReadingPlan").child(firebaseUser.getUid()).orderByChild("planning_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                readingPlans.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    ReadingPlan rp = snapshot.getValue(ReadingPlan.class);
                    readingPlans.add(rp);
                }

                readPlanAdapter = new ReadPlanAdapter(getContext(),readingPlans);
                recyclerView.setAdapter(readPlanAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    @Override
    public void onClick(View v) {
        if(v == btnaddplan){
            Intent intent =  new Intent(getContext(), AddReadPlanBook.class);
            startActivity(intent);
        }
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
