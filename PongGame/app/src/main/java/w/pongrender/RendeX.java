package w.pongrender;
public class RendeX {
	
	private int ball_R;
	private int paddle_width;
	private int paddle_height;
	
	final private double ball_R_ratio = 0.05;
	final private double paddle_width_ratio = 0.2;
	final private double paddle_height_ratio = 0.1;

	
	private float tiltX;
	private float tiltY;
	
	private float[] ball_p = {0, 0, 0};
	private float[] ball_v = {0, 0, 0};
	
	private float[] p1_p = {0, 0, 0};
	private float[] p2_p = {0, 0, 0}; 
	private float[] p1_v = {0, 0, 0};
	private float[] p2_v = {0, 0, 0};
	
	private int dimenX;
	private int dimenY;
	
	public RendeX(int dimenX, int dimenY, float tiltX, float tiltY) {
		this.dimenX = dimenX;
		this.dimenY = dimenY;
		this.tiltX = tiltX;
		this.tiltY = tiltY;
		this.ball_R = (int)(dimenX*ball_R_ratio);
		this.paddle_width = (int)(dimenX*paddle_width_ratio);
		this.paddle_height = (int)(dimenY*paddle_height_ratio);
	}
	
	public void iterate(float tiltX, float tiltY) {
		float deltaX = this.tiltX - tiltX;
		float deltaY = this.tiltY - tiltY;
		
		
	}
	
	public int[] renderBalls() {
		int[] out = new int[2];
		out[0] = (int)(ball_p[0]/100.0 * dimenX);
		out[1] = (int)(ball_p[1]/100.0 * dimenY);
		return out;
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
