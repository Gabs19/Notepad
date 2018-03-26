package app.gabriel.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference fNotesDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotesList = (RecyclerView) findViewById( R.id.main_notes_layout );

        gridLayoutManager = new GridLayoutManager( this,1, GridLayoutManager.VERTICAL,false );

        mNotesList.setHasFixedSize( true );
        mNotesList.setLayoutManager( gridLayoutManager );


        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child( "Notas" ).child( fAuth.getCurrentUser().getUid() );
        }
        updateUI();

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<NoteModel, NoteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoteModel, NoteViewHolder>(
                NoteModel.class,
                        R.layout.single_note_layout,
                NoteViewHolder.class,
                        fNotesDatabase


                ) {
            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, NoteModel model, int position) {
                String NoteId = getRef( position ).getKey();

                fNotesDatabase.child( NoteId ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String title = dataSnapshot.child( "title" ).getValue().toString();
                        String timestamp = dataSnapshot.child( "timestamp" ).getValue().toString();

                        viewHolder.setNoteTitle( title );
                        viewHolder.setNoteTime( timestamp );
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                } );

            }
        };

         mNotesList.setAdapter( firebaseRecyclerAdapter );
    }

    private void updateUI() {

        if (fAuth.getCurrentUser() != null) {
            Log.i( "MainActivity", "fAuth != null" );
        } else {
            Intent StartIntent = new Intent( MainActivity.this, StartActivity.class );
            startActivity( StartIntent );
            finish();
            Log.i( "MainActivity", "fAuth == null" );
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
                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }
}
