package app.gabriel.notepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    private Button btnLog, btnReg;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

       btnLog = (Button) findViewById(R.id.start_log_btn);
       btnReg = (Button) findViewById(R.id.start_reg_btn);

       fAuth = FirebaseAuth.getInstance();

       updateUI();

       btnLog.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });

       btnReg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }
       });
    }

    private void registrer(){

    }

    private void login(){

    }

    private void updateUI(){
        if(fAuth.getCurrentUser() != null){
            Log.i("StartActivity" , "fAuth != null");
        }
        else{
            Intent startIntent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("MainActivity","fAuth == null");
        }

    }
}
