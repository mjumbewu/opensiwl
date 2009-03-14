package oiwl.widget;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

public class MovementData {
    public MovementData(int x0, int y0, long t0, float vx, float vy) { 
        this.x0 = x0; this.y0 = y0; this.t0 = t0;
        this.vx = vx; this.vy = vy;
    }
    
    public int x0;
    public int y0;
    public long t0;
    
    public float vx;
    public float vy;
    
    public int dx;
    public int dy;
    public long dt;
    
    Timer timer;
    
}
