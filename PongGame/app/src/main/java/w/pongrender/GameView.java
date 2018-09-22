package w.pongrender;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Sprite paddle1;
    private Sprite paddle2;
    private Sprite ball;
    private Sprite depthIndicator;
    private Sprite scoreboard;
    private int screenX;
    private int screenY;
    private RendeX computeX;
    private Bitmap grid;

    private final float P1SIZE=0.2f;
    private final float P2SIZE=0.1f;
    private final float BALLSIZE=0.05f;

    private float WINDOWSIZE;
    private final float WINDOWGAP=0.3f;
    private final float GRIDNUMBER=5f;
    private final float GRIDWIDTH=3f;

    public GameView(Context context){
        super(context);
        getHolder().addCallback(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenX = displayMetrics.widthPixels;
        screenY = displayMetrics.heightPixels;
        computeX= new RendeX(screenX,screenY,MainActivity.getRoll(),MainActivity.getPitch(),0f,0f,getContext());

        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        Log.v("gv created","bhnnbbbbbbbhb");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        paddle1 = new Sprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.paddle),(int)(screenX*P1SIZE),(int)(screenY*P1SIZE),false));
        paddle2 = new Sprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.paddle),(int)(screenX*P2SIZE),(int)(screenY*P2SIZE),false),500,500);
        ball = new Sprite(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ball),(int)(screenX*BALLSIZE),(int)(screenY*BALLSIZE),false),200,200);
        grid=makeGrid();
        scoreboard=new Sprite(makeScoreboard());
        depthIndicator=new Sprite(makeDepthIndicator());
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
        Log.d("update()","started");
        computeX.iterate(MainActivity.getRoll(),MainActivity.getPitch(),0f,0f);
        Log.d("update()","after iterate");
        //float depth=computeX.renderDepth();
        //ball.update(computeX.renderBall(),depth);
        //int[] depthA={(int)(WINDOWGAP*screenX*depth),(int)(WINDOWGAP*screenY*depth)};
        //depthIndicator.update(depthA,depth);
        Log.d("update()","before p1");
        paddle1.update(computeX.renderPaddle1());
        Log.d("update()","after p1");
        paddle2.update(computeX.renderPaddle2());
        scoreboard.update(computeX.isGoal());
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawBitmap(grid, 0, 0, null);
            //int[] temp={0,0};
            //paddle1.update(temp);
            paddle1.draw(canvas);
            paddle2.draw(canvas);
            ball.draw(canvas);

            Log.d("draw()","finished draw");
        }
    }
    public Bitmap makeGrid(){
        Bitmap grid=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.background),screenX,screenY,false);
        Canvas canvas=new Canvas(grid);
        Paint paint= new Paint();
        paint.setColor(getResources().getColor(R.color.gridColor));
        paint.setStrokeWidth(GRIDWIDTH);
        WINDOWSIZE=1-(WINDOWGAP);

        canvas.drawLine(0,0,screenX*(1-WINDOWSIZE),screenY*(1-WINDOWSIZE),paint);
        canvas.drawLine(screenX*(WINDOWSIZE),screenY*(WINDOWSIZE),screenX*(1),screenY*(1),paint);
        canvas.drawLine(0,screenY*(1),screenX*(1-WINDOWSIZE),screenY*(WINDOWSIZE),paint);
        canvas.drawLine(screenX*(1),screenY*(0),screenX*(WINDOWSIZE),screenY*(1-WINDOWSIZE),paint);

        for(int i=1;i<=((int)GRIDNUMBER);i++){
            WINDOWSIZE=1-(WINDOWGAP/GRIDNUMBER*i);
            canvas.drawLine(screenX * (WINDOWSIZE), screenY * (WINDOWSIZE), screenX * (WINDOWSIZE), screenY * (1 - WINDOWSIZE), paint);
            canvas.drawLine(screenX * (WINDOWSIZE), screenY * (WINDOWSIZE), screenX * (1 - WINDOWSIZE), screenY * (WINDOWSIZE), paint);
            canvas.drawLine(screenX * (1 - WINDOWSIZE), screenY * (WINDOWSIZE), screenX * (1 - WINDOWSIZE), screenY * (1 - WINDOWSIZE), paint);
            canvas.drawLine(screenX * (WINDOWSIZE), screenY * (1 - WINDOWSIZE), screenX * (1 - WINDOWSIZE), screenY * (1 - WINDOWSIZE), paint);
        }
        return grid;
    }
    public Bitmap makeScoreboard(){
        Bitmap grid=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.background),screenX,screenY,false);
        Canvas canvas=new Canvas(grid);
        Paint paint= new Paint();
        paint.setColor(getResources().getColor(R.color.gridColor));
        paint.setStrokeWidth(GRIDWIDTH);
        WINDOWSIZE=1-(WINDOWGAP);

        canvas.drawLine(0,0,screenX*(1-WINDOWSIZE),screenY*(1-WINDOWSIZE),paint);
        canvas.drawLine(screenX*(WINDOWSIZE),screenY*(WINDOWSIZE),screenX*(1),screenY*(1),paint);
        canvas.drawLine(0,screenY*(1),screenX*(1-WINDOWSIZE),screenY*(WINDOWSIZE),paint);
        canvas.drawLine(screenX*(1),screenY*(0),screenX*(WINDOWSIZE),screenY*(1-WINDOWSIZE),paint);


        return grid;
    }
    public Bitmap makeDepthIndicator(){
        Bitmap grid=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.background),screenX,screenY,false);
        Canvas canvas=new Canvas(grid);
        Paint paint= new Paint();
        paint.setColor(getResources().getColor(R.color.gridColor));
        paint.setStrokeWidth(GRIDWIDTH);
        WINDOWSIZE=1-(WINDOWGAP);

        canvas.drawLine(0,0,screenX*(1-WINDOWSIZE),screenY*(1-WINDOWSIZE),paint);
        canvas.drawLine(screenX*(WINDOWSIZE),screenY*(WINDOWSIZE),screenX*(1),screenY*(1),paint);
        canvas.drawLine(0,screenY*(1),screenX*(1-WINDOWSIZE),screenY*(WINDOWSIZE),paint);
        canvas.drawLine(screenX*(1),screenY*(0),screenX*(WINDOWSIZE),screenY*(1-WINDOWSIZE),paint);


        return grid;
    }
}