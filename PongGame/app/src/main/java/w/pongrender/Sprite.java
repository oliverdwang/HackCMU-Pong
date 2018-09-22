package w.pongrender;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Sprite {
    private Bitmap image;
    private int x,y;

    public Sprite(Bitmap bmp) {
        image = bmp;
        x=0;
        y=0;
    }
    public Sprite(Bitmap bmp,int inx, int iny) {
        image = bmp;
        x=inx;
        y=iny;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }
    public void update(int[]coords){
        x = coords[0];
        y = coords[1];

    }
}
