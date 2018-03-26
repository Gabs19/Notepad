package app.gabriel.notepad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        updateUI();
    }

    private void updateUI(){

            if(fAuth.getCurrentUser() != null){
                   Log.i("MainActivity", "fAuth != null");
            }
            else{
                Intent StartIntent = new Intent(MainActivity.this,StartActivity.class);
                startActivity(StartIntent);
                finish();
                Log.i("MainActivity", "fAuth == null");
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

         getMenuInflater().inflate(R.menu.main_menu,menu);

         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.main_new_note_btn:
                Intent intent = new Intent(MainActivity.this,NewNoteActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }
}
