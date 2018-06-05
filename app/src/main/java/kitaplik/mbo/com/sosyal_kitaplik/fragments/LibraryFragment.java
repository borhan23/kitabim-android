package kitaplik.mbo.com.sosyal_kitaplik.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kitaplik.mbo.com.sosyal_kitaplik.LibraryAddBook;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.ReadedBooks;
import kitaplik.mbo.com.sosyal_kitaplik.WillReadBooks;
import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.ReadedBooksAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.adapterholder.ReadingBookAdapter;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Book;
import kitaplik.mbo.com.sosyal_kitaplik.entities.Library;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LibraryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LibraryFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnaddbook;
    private ImageButton btnreaded, btnwillread;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    private ArrayList<Library> library= new ArrayList<>();
    ReadingBookAdapter readingBookAdapter;
    private String TAG = "reading";
    private ArrayList<Book> books;


    public LibraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
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
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        btnaddbook = (Button) view.findViewById(R.id.btnreadingbook);
        btnreaded = (ImageButton) view.findViewById(R.id.btnokuduklarim);
        btnwillread = (ImageButton) view.findViewById(R.id.btnokuyacaklarim);
        btnaddbook.setOnClickListener(this);
        btnreaded.setOnClickListener(this);
        btnwillread.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.readingbooksrv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);


        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        veriCek();

        return view;
    }


    private void veriCek() {
        myRef.child("Library").child(firebaseUser.getUid()).child("reading_book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                library.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Library lb = snapshot.getValue(Library.class);
                    library.add(lb);
                }

                readingBookAdapter = new ReadingBookAdapter(getContext(), library);
                recyclerView.setAdapter(readingBookAdapter);

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

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v == btnaddbook){
            Intent intent =  new Intent(getContext(), LibraryAddBook.class);
            startActivity(intent);
        }
        if(v == btnreaded){
            Intent intent =  new Intent(getContext(), ReadedBooks.class);
            startActivity(intent);
        }
        if(v == btnwillread){
            Intent intent =  new Intent(getContext(), WillReadBooks.class);
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
