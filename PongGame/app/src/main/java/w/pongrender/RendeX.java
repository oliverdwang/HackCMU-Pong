package w.pongrender;

import android.util.Log;
import android.util.TypedValue;
import android.content.Context;

public class RendeX {
	
	private int ball_R;
	private int paddle_width;
	private int paddle_height;

	private float ball_R_ratio = 0.05f;
	private float paddle_ratio = 0.2f;

	private float xSensitivity;
	private float ySensitivity;
	private float singleSideXRange;
	private float singleSideYRange;
	
	private float tiltX;
	private float tiltY;
	
	private float[] ball_p = {0, 0, 0};
	private float[] ball_v = {0, 0, 0};
	
	private float[] p1_p = {0, 0}; //Master
	private float[] p2_p = {0, 0};
	private float[] p1_v = {0, 0}; //Slave
	private float[] p2_v = {0, 0};
	
	private int dimenX;
	private int dimenY;

	//Iterate() specific variables
	private float deltaX_p1;
	private float deltaY_p1;
	private float deltaX_p2;
	private float deltaY_p2;
	
	public RendeX(int dimenX, int dimenY, float tiltX, float tiltY, Context context) {
		//Set initial vlaues
		this.dimenX = dimenX;
		this.dimenY = dimenY;
		this.tiltX = tiltX;
		this.tiltY = tiltY;

		TypedValue outValue = new TypedValue();
		context.getResources().getValue(R.dimen.xSensitivity, outValue, true);
		this.xSensitivity = outValue.getFloat();
		context.getResources().getValue(R.dimen.ySensitivity, outValue, true);
		this.ySensitivity = outValue.getFloat();
		context.getResources().getValue(R.dimen.singleSideXRange, outValue, true);
		this.singleSideXRange = (int) outValue.getFloat();
		context.getResources().getValue(R.dimen.singleSideYRange, outValue, true);
		this.singleSideYRange = (int) outValue.getFloat();

		this.paddle_width = (int)(dimenX*paddle_ratio);
		this.paddle_height = (int)(dimenY*paddle_ratio);

		//Generate starting ball position, velocity, and paddle position
	}
	
	public void iterate(float tiltX_p1, float tiltY_p1, float tiltX_p2, float tiltY_p2) {
		deltaX_p1 = this.tiltX - tiltX_p1;
		deltaY_p1 = this.tiltY - tiltY_p1;
		deltaX_p2 = this.tiltX - tiltX_p2;
		deltaY_p2 = this.tiltY - tiltY_p2;

		//Cap delta X
		//For self
		if(deltaX_p1 < this.tiltX - singleSideXRange) {
			deltaX_p1 = this.tiltX - singleSideXRange;
		} else if(deltaX_p1 > this.tiltX + singleSideXRange) {
			deltaX_p1 = this.tiltX + singleSideXRange;
		}
		//For other
		if(deltaX_p2 < this.tiltX - singleSideXRange) {
			deltaX_p2 = this.tiltX - singleSideXRange;
		} else if(deltaX_p2 > this.tiltX + singleSideXRange) {
			deltaX_p2 = this.tiltX + singleSideXRange;
		}
		Log.v("RendeX Iterate","deltaX_p1=" + Float.toString(deltaX_p1) + " deltaX_p2=" + Float.toString(deltaX_p2));

		//Cap delta Y
		//For self
		if(deltaY_p1 < this.tiltX - singleSideYRange) {
			deltaY_p1 = this.tiltY - singleSideYRange;
		} else if(deltaY_p1 > this.tiltY + singleSideYRange) {
			deltaY_p1 = this.tiltY + singleSideYRange;
		}
		//For other
		if(deltaY_p2 < this.tiltX - singleSideYRange) {
			deltaY_p2 = this.tiltY - singleSideYRange;
		} else if(deltaY_p2 > this.tiltY + singleSideYRange) {
			deltaY_p2 = this.tiltY + singleSideYRange;
		}
		Log.v("RendeX Iterate","deltaY_p1=" + Float.toString(deltaY_p1) + " deltaY_p2=" + Float.toString(deltaY_p2));


		//Set new velocities
		//For self
		p1_v[0] = deltaX_p1;
		p1_v[1] = deltaY_p1;
		//For other
		p2_v[0] = deltaX_p2;
		p2_v[1] = deltaY_p2;

		//Set new positions
		//For self
		p1_p[0] = p1_p[0]+p1_v[0]*xSensitivity;
		p1_p[1] = p1_p[1]+p1_v[1]*ySensitivity;
		//For other
		p2_p[0] = p2_p[0]+p2_v[0]*xSensitivity;
		p2_p[1] = p2_p[1]+p2_v[1]*ySensitivity;
	}
	
	public int[] renderBall() {
		int[] out = new int[2];
		out[0] = (int)(ball_p[0]/100.0 * dimenX);
		out[1] = (int)(ball_p[1]/100.0 * dimenY);
		return out;
	}

	public float renderDepth() {
		return ball_p[2]/100;
	}

	public int[] renderPaddle1() {
		int[] out = new int[2];
		out[0] = (int)(p1_p[0]/100.0 * dimenX);
		out[1] = (int)(p1_p[1]/100.0 * dimenY);
		return out;
	}
	
	public int[] renderPaddle2() {
		int[] out = new int[2];
		out[0] = (int)(p2_p[0]/100.0 * dimenX);
		out[1] = (int)(p2_p[1]/100.0 * dimenY);
		return out;
	}
	
	public int isGoal() {
		if(p1_p[0] < ball_p[0] && p1_p[0] + paddle_width > ball_p[0]+2*ball_R
				&& p1_p[1] < ball_p[1] && p1_p[1] + paddle_height > ball_p[1]+2*ball_R) {
			return -1;
		}
		else if(p2_p[0] < ball_p[0] && p2_p[0] + paddle_width > ball_p[0]+2*ball_R
				&& p2_p[1] < ball_p[1] && p2_p[1] + paddle_height > ball_p[1]+2*ball_R) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	
}
