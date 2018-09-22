package com.oliverdwang.pong_tilt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TiltActivity {

    private TextView textViewAzimuth;
    private TextView textViewPitch;
    private TextView textViewRoll;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewAzimuth = findViewById(R.id.textView_azimuth);
        textViewPitch = findViewById(R.id.textView_pitch);
        textViewRoll = findViewById(R.id.textView_roll);
        buttonUpdate = findViewById(R.id.button_update);

        textViewAzimuth.setText(Float.toString(azimuth));
        textViewPitch.setText(Float.toString(pitch));
        textViewRoll.setText(Float.toString(roll));

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewAzimuth.setText(Float.toString(azimuth));
                textViewPitch.setText(Float.toString(pitch));
                textViewRoll.setText(Float.toString(roll));
            }
        });
    }
}
