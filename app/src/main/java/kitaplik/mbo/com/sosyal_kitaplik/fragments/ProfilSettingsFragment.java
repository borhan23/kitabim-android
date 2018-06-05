package kitaplik.mbo.com.sosyal_kitaplik.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import kitaplik.mbo.com.sosyal_kitaplik.R;
import kitaplik.mbo.com.sosyal_kitaplik.entities.User;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilSettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageView changeName;
    private CircleImageView imageuser;
    private TextView txtName, txtUsername, txtChangePhoto;
    private EditText editChangeName, editTextUsername, editChangeMail;
    private Button btnPassword, btnSave;
    private Uri img_uri;
    private DatabaseReference mdataref;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    String TAG ="changepass";
    private ProgressDialog progressDialog;

    public ProfilSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilSettingsFragment newInstance(String param1, String param2) {
        ProfilSettingsFragment fragment = new ProfilSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_profil_settings, container, false);

        imageuser = (CircleImageView) view.findViewById(R.id.user_image);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtUsername = (TextView) view.findViewById(R.id.txtUserName);
        editChangeName = (EditText) view.findViewById(R.id.editChangeName);
        editTextUsername = (EditText) view.findViewById(R.id.editTextUsername);
        editChangeMail = (EditText) view.findViewById(R.id.editChangeTextMail);
        btnPassword = (Button) view.findViewById(R.id.btnChangePass);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        changeName = (ImageView) view.findViewById(R.id.changename);
        txtChangePhoto = (TextView) view.findViewById(R.id.txtChangePhoto);

        txtChangePhoto.setOnClickListener(this);
        btnPassword.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        changeName.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mdataref = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(getActivity());

        veriCek();

        return view;
    }

    private void veriCek() {
        mdataref.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    User u = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);

                    txtName.setText(u.getFull_name());
                    txtUsername.setText(u.getUser_name());

                    editChangeName.setText(u.getFull_name());
                    editChangeMail.setText(firebaseUser.getEmail());
                    editTextUsername.setText(u.getUser_name());

                    if(u.getUser_image() == null){

                    }else{
                    Picasso.get()
                            .load(u.getUser_image())
                            .resize(100, 100)
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
        if (v == changeName){
            editChangeName.setEnabled(true);
            btnSave.setEnabled(true);
        }
        if(v == btnSave){

            mdataref.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        User u = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                        User userInfo= new User();
                        userInfo.setUser_id(firebaseUser.getUid());
                        userInfo.setFull_name(editChangeName.getText().toString().trim());
                        userInfo.setUser_name(editTextUsername.getText().toString().trim());
                        userInfo.setUser_image(u.getUser_image());
                        mdataref.child("Users").child(firebaseUser.getUid()).setValue(userInfo);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            editChangeName.setEnabled(false);
            btnSave.setEnabled(false);
            Toast.makeText(getActivity(),"Profiliniz başarıyla güncellendi.",Toast.LENGTH_LONG).show();

        }
        if(v == btnPassword){
            final String email = firebaseUser.getEmail();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int choice) {
                    switch (choice) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful()){
                                     Toast.makeText(getContext(),"Şifre sıfırlama linki mail adresinize gönderildi.",Toast.LENGTH_LONG).show();
                                 }
                                }
                            });
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Şifrenizi sıfırlamak istediğinize emin misiniz?")
                    .setPositiveButton("Evet", dialogClickListener)
                    .setNegativeButton("Hayır", dialogClickListener).show();
        }

        if(v == txtChangePhoto){
            Intent intent = new Intent(Intent.ACTION_PICK);

            intent.setType("image/*");

            startActivityForResult(intent, GALLERY_INTENT);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            progressDialog.setMessage("Resim yükleniyor...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            final Uri img_uri = data.getData();

            StorageReference filepath = mStorage.child("Users").child(firebaseUser.getUid()+"/"+img_uri.getLastPathSegment());

            final User userinfo= new User();
            userinfo.setUser_id(firebaseUser.getUid());
            userinfo.setFull_name(editChangeName.getText().toString().trim());
            userinfo.setUser_name(editTextUsername.getText().toString().trim());


            filepath.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageuser.setImageURI(img_uri);
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    userinfo.setUser_image(downloadUrl.toString());
                    mdataref.child("Users").child(firebaseUser.getUid()).setValue(userinfo);
                    Toast.makeText(getActivity(),"Resim değiştirme başarılı.",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
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
