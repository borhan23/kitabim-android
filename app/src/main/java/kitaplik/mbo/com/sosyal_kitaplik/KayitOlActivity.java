package kitaplik.mbo.com.sosyal_kitaplik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kitaplik.mbo.com.sosyal_kitaplik.entities.User;

public class KayitOlActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editUsername,editName,editMail,editPass,editRepass;
    private Button btnSubmit;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseUser firebaseUser;
    private String logger = "logs";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        editUsername = (EditText) findViewById(R.id.editUsername);
        editName = (EditText) findViewById(R.id.editName);
        editMail = (EditText) findViewById(R.id.editMail);
        editPass = (EditText) findViewById(R.id.editPass);
        editRepass = (EditText) findViewById(R.id.editRepass);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        btnSubmit.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        if(v == btnSubmit){
            kayitOl();
        }
    }

    private void kayitOl() {
        String user_name = editUsername.getText().toString().trim();
        String full_name  = editName.getText().toString().trim();
        String email = editMail.getText().toString().trim();
        String password  = editPass.getText().toString().trim();
        String rePassword  = editRepass.getText().toString().trim();

        if(TextUtils.isEmpty(user_name)){
            Toast.makeText(this,"Lütfen kullanıcı adınızı giriniz.",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(full_name)){
            Toast.makeText(this,"Lütfen isminizi giriniz.",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Lütfen mail adresinizi giriniz.",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Lütfen şifrenizi giriniz.",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(rePassword)){
            Toast.makeText(this,"Lütfen şifrenizi tekrar giriniz.",Toast.LENGTH_LONG).show();
            return;
        }

        if(!rePassword.equals(password)){
            Toast.makeText(KayitOlActivity.this,"Girdiğiniz şifreler uyuşmuyor.",Toast.LENGTH_LONG).show();
            return;
        }

            UserKontrol();

    }

    private void UserKontrol() {
        progressDialog.setMessage("Kaydınız gerçekleştiriliyor.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        myRef.child("Users").orderByChild("user_name").equalTo(editUsername.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(),"Bu kullanıcı daha önce alınmış",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }else{
                    kayitYap();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void kayitYap() {
        String user_name = editUsername.getText().toString().trim();
        String full_name  = editName.getText().toString().trim();
        String email = editMail.getText().toString().trim();
        String password  = editPass.getText().toString().trim();

        final User userInfo = new User();
        userInfo.setUser_name(user_name);
        userInfo.setFull_name(full_name);
        userInfo.setRole(false);

        progressDialog.setMessage("Kaydınız gerçekleştiriliyor.");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            userInfo.setUser_id(user.getUid());
                            myRef.child("Users").child(user.getUid()).setValue(userInfo);

                            Log.d(logger,"Kayıt başarılı.");
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            progressDialog.dismiss();
                        }else if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Şifreniz en az 6 karakterden oluşmalıdır.",Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Girdiğiniz mail formatı geçersiz.",Toast.LENGTH_LONG).show();
                            } catch(FirebaseAuthUserCollisionException e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Girdiğiniz maile ait kullanıcı mevcut.",Toast.LENGTH_LONG).show();
                            } catch(Exception e) {
                                progressDialog.dismiss();
                                Log.e(logger, e.getMessage());
                            }
                        }
                    }
                });
    }


}
