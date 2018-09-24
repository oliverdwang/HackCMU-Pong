package w.pongrender;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void clickHost(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isHost", true);
        startActivity(intent);
    }

    public void clickConnect(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isHost", false);
        startActivity(intent);
    }
}
