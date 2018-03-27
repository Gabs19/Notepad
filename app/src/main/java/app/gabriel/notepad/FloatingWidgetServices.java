package app.gabriel.notepad;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Gabriel on 27/03/2018.
 */

public class FloatingWidgetServices extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private View mFloat;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFloat = LayoutInflater.from( this ).inflate( R.layout.overlay_layout, null );

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager = (WindowManager) getSystemService( WINDOW_SERVICE );
        windowManager.addView( mFloat, params );

        LayoutInflater inflater = (LayoutInflater) getSystemService( LAYOUT_INFLATER_SERVICE );

        implementClickListerner();


        final View collapsedView = mFloat.findViewById( R.id.collapse_view );
        final View expandedView = mFloat.findViewById( R.id.expanded_container );

        ImageView closeBtn = (ImageView) mFloat.findViewById( R.id.close_btn );

        closeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        } );

        ImageView closeButton = (ImageView) mFloat.findViewById( R.id.close_button );
        closeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility( View.VISIBLE );
                expandedView.setVisibility( View.GONE );

            }
        } );


        mFloat.findViewById( R.id.root_container ).setOnTouchListener( new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility( View.GONE );
                                expandedView.setVisibility( View.VISIBLE );
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout( mFloat, params );
                        return true;
                }
                return false;
            }
        } );


    }

    private boolean isViewCollapsed() {
        return mFloat == null || mFloat.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }
    private void implementClickListerner(){
        mFloat.findViewById(R.id.open_btn).setOnClickListener( (View.OnClickListener) this );


    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.open_btn:
                Intent intent = new Intent( FloatingWidgetServices.this, MainActivity.class );
                intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( intent );

                stopSelf();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloat != null) windowManager.removeView(mFloat);
    }
}

