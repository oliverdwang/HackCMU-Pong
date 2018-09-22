package w.pongrender;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;

public class Sprite {
    private Bitmap image;
    private Bitmap imageScale;
    private int x,y,xs,ys;

    public Sprite(Bitmap bmp) {
        image = bmp;
        imageScale = bmp;
        x=0;
        y=0;
        xs=image.getWidth();
        ys=image.getHeight();
    }
    public Sprite(Bitmap bmp,int inx, int iny) {
        image = bmp;
        imageScale = bmp;

        x=inx;
        y=iny;
        xs=image.getWidth();
        ys=image.getHeight();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(imageScale, x, y, null);
    }
    public void update(int[]coords){
        x = coords[0];
        y = coords[1];

    }
    public void update(int[]coords,float depth){
        x = coords[0];
        y = coords[1];
        imageScale = Bitmap.createScaledBitmap(image,(int)(xs*depth),(int)(ys*depth),false);

    }
    public void update(int goal){


    }

}
