package w.pongrender;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Sprite paddle1;
    private int screenX;
    private int screenY;
    private RendeX computeX;

    private final float P1SIZE=0.4f;

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenX = displayMetrics.widthPixels;
        screenY = displayMetrics.heightPixels;
        computeX= new RendeX(screenX,screenY,MainActivity.getRoll(),MainActivity.getPitch());

        thread = new MainThread(getHolder(), this);
        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        paddle1 = new Sprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.paddle),(int)(screenX*P1SIZE),(int)(screenY*P1SIZE),false));
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                Log.v("E in run()",e.toString());
            }
            retry = false;
        }
    }


    public void update() {
        computeX.iterate(MainActivity.getRoll(),MainActivity.getPitch());
        paddle1.update(computeX.renderPaddle1());
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawARGB(100, 225, 225, 255);
            paddle1.draw(canvas);

        }
    }
}