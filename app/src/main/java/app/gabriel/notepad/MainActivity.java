package app.gabriel.notepad;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final int APP__PERMISSION_REQUEST = 102;

    private FirebaseAuth fAuth;

    private FloatingActionButton floatBtn;
    private RecyclerView mNotesList;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference fNotesDatabase;
    private Object item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatBtn = (FloatingActionButton) findViewById( R.id.float_btn );

        mNotesList = (RecyclerView) findViewById( R.id.main_notes_layout );

        gridLayoutManager = new GridLayoutManager( this,2, GridLayoutManager.VERTICAL,false );

        mNotesList.setHasFixedSize( true );
        mNotesList.setLayoutManager( gridLayoutManager );
        mNotesList.addItemDecoration( new GridSpacingItemDecoration( 2, dpTopx(10),true ) );

        fAuth = FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() != null){
            fNotesDatabase = FirebaseDatabase.getInstance().getReference().child( "Notas" ).child( fAuth.getCurrentUser().getUid() );
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays( this )){
            Intent intent = new Intent( Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse( "package:" + getPackageName() ) );
            startActivityForResult( intent, APP__PERMISSION_REQUEST);
        }
        else{

            super.onOptionsItemSelected( (MenuItem) item );
        }

        floatBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newnote = new Intent( MainActivity.this,NewNoteActivity.class );
                startActivity( newnote );
            }
        } );
        updateUI();

    }

    private void initializeView(){
        Button floatBtn = (Button) findViewById( R.id.main_ballon_btn );
        floatBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startService( new Intent(MainActivity.this, FloatingWidgetServices.class) );
                    finish();
            }
        } );
    }

    private int dpTopx(int i) {
        Resources r = getResources();
        return Math.round( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, i , r.getDisplayMetrics()) );
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
                final String NoteId = getRef( position ).getKey();

                fNotesDatabase.child( NoteId ).addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild( "title" ) && dataSnapshot.hasChild( "timestamp" )){

                            String title = dataSnapshot.child( "title" ).getValue().toString();
                            String timestamp = dataSnapshot.child( "timestamp" ).getValue().toString();

                            viewHolder.setNoteTitle( title );
                            viewHolder.setNoteTime( timestamp );


                            viewHolder.view.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent( MainActivity.this,NewNoteActivity.class );
                                    intent.putExtra( "noteId",NoteId );
                                    startActivity( intent );
                                }
                            } );
                        }

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
//            case R.id.main_new_note_btn:
//                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
//                startActivity(intent);
//                break;
            case R.id.main_ballon_btn:
                Intent intentl = new Intent( MainActivity.this,  FloatingWidgetServices.class );
                startService(intentl);
                finish();
                break;
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode == APP__PERMISSION_REQUEST && resultCode == RESULT_OK){
            super.onOptionsItemSelected( (MenuItem) item );
        }
        else{
            Toast.makeText( MainActivity.this,"Draw over other app permission not enable.",Toast.LENGTH_SHORT ).show();
        }
    }



}
