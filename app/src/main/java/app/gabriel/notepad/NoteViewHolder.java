package app.gabriel.notepad;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Gabriel on 25/03/2018.
 */

public class NoteViewHolder extends ViewHolder {

    View view;

    TextView textTitle, textTime;
    CardView noteCard;

    public NoteViewHolder(View itemView) {
        super(itemView);

        view = itemView;

        textTitle = (TextView) view.findViewById(R.id.note_title);
        textTime = (TextView)  view.findViewById(R.id.note_time);
        noteCard = (CardView) view.findViewById( R.id.note_card );
    }

    public void setNoteTitle(String title){
        textTitle.setText(title);

    }

    public void setNoteTime(String time){

        textTime.setText(time);
    }
}
