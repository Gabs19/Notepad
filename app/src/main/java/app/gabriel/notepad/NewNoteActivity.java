package app.gabriel.notepad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class NewNoteActivity extends AppCompatActivity {

    private Button btnCreate;

    private EditText eTitle, eContent;

    private Toolbar toolbar;

    private FirebaseAuth fAuth;
    private DatabaseReference fNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        btnCreate = (Button) findViewById(R.id.create_btn);

        eTitle = (EditText) findViewById(R.id.new_title);
        eContent = (EditText) findViewById(R.id.new_content);
        toolbar = (Toolbar) findViewById(R.id.new_note_toolbar);

        setSupportActionBar(toolbar);

        fAuth = FirebaseAuth.getInstance();
        fNotes = FirebaseDatabase.getInstance().getReference().child("Notas").child(fAuth.getCurrentUser().getUid());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = eTitle.getText().toString().trim();
                String content = eContent.getText().toString().trim();
                if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)){
                    createNote(title, content);
                }
                else {
                    Snackbar.make(view,"Campo Vazio",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void createNote(String title, String content){

        if (fAuth.getCurrentUser() != null){
                final DatabaseReference newnote = fNotes.push();

            final Map mapNote =   new HashMap();
            mapNote.put("title",title);
            mapNote.put("content",content);
            mapNote.put("timestamp", ServerValue.TIMESTAMP);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    newnote.setValue(mapNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(NewNoteActivity.this,"Nota Adicionada",Toast.LENGTH_SHORT).show();
                            }
                            else   {
                                Toast.makeText(NewNoteActivity.this,"ERRO" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            thread.start();
        }
        else{
            Toast.makeText(this,"Usuário não logado",Toast.LENGTH_SHORT).show();

        }
    }
}
