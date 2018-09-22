package w.pongrender;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends TiltActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This is Richard's onCreate space
        



        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//        Log.d("Tilt Pitch", Float.toString(pitch)); //You can directly pass pitch. Pitch is for up down.
//        Log.d("Tilt Roll", Float.toString(roll)); //You can directly pass roll. Roll is for left right.

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(new GameView(this));
    }

    public static float getPitch() {
        return pitch;
    }

    public static float getRoll() {
        return roll;
    }





    // This is Richard's space, for bluetooth stuff





}
