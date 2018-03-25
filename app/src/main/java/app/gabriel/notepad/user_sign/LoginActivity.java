package app.gabriel.notepad.user_sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.gabriel.notepad.MainActivity;
import app.gabriel.notepad.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnLog;
    private TextInputLayout log_email, log_pass;

    private  ProgressDialog progressDialog;

    private FirebaseAuth fAuth;
//    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_email = (TextInputLayout) findViewById(R.id.inpunt_log_email);
        log_pass = (TextInputLayout) findViewById(R.id.input_log_pass);

        btnLog = (Button) findViewById(R.id.btn_log);

        fAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lemial = log_email.getEditText().getText().toString().trim();
                String Lpass = log_pass.getEditText().getText().toString().trim();

                if(!TextUtils.isEmpty(Lemial) && !TextUtils.isEmpty(Lpass)){
                    login(Lemial,Lpass);
                }

            }
        });

    }

    private void login(String email, String password){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando...");

        progressDialog.show();

        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,"Logado Com sucesso",Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(LoginActivity.this,"ERROR" + task.getException().getMessage(),Toast.LENGTH_SHORT);

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case  android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
