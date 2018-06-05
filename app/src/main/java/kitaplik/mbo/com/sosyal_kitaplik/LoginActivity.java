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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnGirisYap;
    private EditText editTextEmail,editTextPassword,txtsonuc;
    private int sayi1,sayi2,sonuc,val;
    private TextView sifreUnuttum,kayitOl,txtsayi1,txtsayi2;
    String TAG = "main";

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        girisKontrol();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnGirisYap = (Button) findViewById(R.id.btnGirisYap);
        sifreUnuttum = (TextView) findViewById(R.id.sifreUnuttum);
        kayitOl = (TextView) findViewById(R.id.kayitOl);
        txtsayi1 = (TextView) findViewById(R.id.edittxtsayi1);
        txtsayi2 = (TextView) findViewById(R.id.edittxtsayi2);
        txtsonuc = (EditText) findViewById(R.id.edittxtsonuc);

        //val = Integer.parseInt(txtsonuc.getText().toString());

        progressDialog = new ProgressDialog(this);

        Random random = new Random();
        sayi1 = random.nextInt(10) + 1;
        sayi2 = random.nextInt(10) + 1;

        txtsayi1.setText(String.valueOf(sayi1));
        txtsayi2.setText(String.valueOf(sayi2));

        btnGirisYap.setOnClickListener(this);
        sifreUnuttum.setOnClickListener(this);
        kayitOl.setOnClickListener(this);
    }

    public void girisKontrol() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            Intent i=new Intent(this,MainFlow.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        if(v == btnGirisYap){
            userLogin();
        }

        if(v == sifreUnuttum){
            startActivity(new Intent(this, SifreUnuttumActivity.class));
        }

        if(v == kayitOl){
            startActivity(new Intent(this, KayitOlActivity.class));
        }
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Lütfen mail adresinizi giriniz.",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Lütfen şifrenizi giriniz.",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(txtsonuc.getText().toString())){
            Toast.makeText(this,"Güvenlik sorusunu yapınız.",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Giriş Yapılıyor...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        sonuc = sayi1 * sayi2;
        Log.d(TAG,"+++++++++++"+sonuc);

        if(txtsonuc.getText().toString() != "") {
            val = Integer.parseInt(txtsonuc.getText().toString());

        }

        if(val == sonuc && txtsonuc.getText().toString() != "") {
          email = editTextEmail.getText().toString().trim();
          password  = editTextPassword.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainFlow.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "E-mail veya şifre hatalı", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(this,"Güvenlik sorusu yanlış.",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

    }
}
