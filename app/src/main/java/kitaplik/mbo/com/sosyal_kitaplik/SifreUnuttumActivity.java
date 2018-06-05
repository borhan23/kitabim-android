package kitaplik.mbo.com.sosyal_kitaplik;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SifreUnuttumActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextMail;
    private Button btnGonder;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_unuttum);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextMail = (EditText) findViewById(R.id.editTextEmail);
        btnGonder = (Button) findViewById(R.id.btnGonder);

        btnGonder.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == btnGonder){
            gonder();
        }
    }

    private void gonder() {
        String email = editTextMail.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Lütfen mail adresinizi giriniz.",Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful()){
                 Toast.makeText(SifreUnuttumActivity.this,"Şifre sıfırlama linki mail adresinize gönderildi.",Toast.LENGTH_LONG).show();
                 editTextMail.setText("");
             }
             else{
                 Toast.makeText(SifreUnuttumActivity.this,"Girdiğiniz mail hesabına ait bir kayıt bulunamadı.",Toast.LENGTH_LONG).show();
             }
            }
        });
    }
}
