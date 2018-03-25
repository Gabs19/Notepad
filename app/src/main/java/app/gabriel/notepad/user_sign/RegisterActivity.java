package app.gabriel.notepad.user_sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.gabriel.notepad.MainActivity;
import app.gabriel.notepad.R;

public class RegisterActivity extends AppCompatActivity {

    private Button btnreg;
    private TextInputLayout nome, email, pass;

    private FirebaseAuth fAuth;
    private DatabaseReference firebaseDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnreg = (Button) findViewById(R.id.btn_reg);
        nome = (TextInputLayout) findViewById(R.id.input_reg_nome);
        email = (TextInputLayout) findViewById(R.id.input_reg_email);
        pass = (TextInputLayout) findViewById(R.id.input_reg_pass);

        fAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String unome = nome.getEditText().getText().toString().trim();
                String uemail = email.getEditText().getText().toString().trim();
                String upass = pass.getEditText().getText().toString().trim();

                registrer(unome,uemail,upass);

            }
        });

    }

    private void registrer(final String nome, String email, String password){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verificando...");

        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    firebaseDatabase.child(fAuth.getCurrentUser().getUid()).child("basic").child("name").setValue(nome).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    progressDialog.dismiss();
                                    Intent mainIntent = new Intent (RegisterActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                    Toast.makeText(RegisterActivity.this,"Usuario Criado",Toast.LENGTH_SHORT);
                                }
                                else{

                                    progressDialog.dismiss();

                                    Toast.makeText(RegisterActivity.this,"Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                }
                        }
                    });

                }else{
                    progressDialog.dismiss();

                    Toast.makeText(RegisterActivity.this,"Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
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
