/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author mjumbewu
 * 
 * To subclass FlickableFrame, all that must be done is, if pointerDown, 
 * pointerUp, pointerDrag, or pointerFlick are overridden, they must each call
 * the FlickableFrame version of their respective methods, e.g.:
 * 
 *   void pointerDown(int x, int y) {
 *     super.pointerDown(x, y);
 *     
 *     // ... derived class-specific code
 *   }
 * 
 * The methods pointerDown/Up/Drag all have the expected behavior.  The
 * pointerFlick method is called after the user flicks their pointer to release
 * the frame.  There is a variable called 'flick_duration' that determines how 
 * often the method is called, in milliseconds.  The method will continue to be 
 * called until the distance moved is less than 1 pixel.  It's like a drag, but
 * after the pointer has been released (and yes, pointerUp will still be called
 * when the frame is flicked).
 */
public class FlickableFrame extends BasicFrame {
    public static final int X = 0;
    public static final int Y = 1;
    
    float[] deceleration = {0.9f,0.9f};
    float[] elasticity = {0,0};
    
    // If this object has not had a moving event in longer than
    // 'stillness_duration_threshold', it is not considered to be moving (i.e.
    // it has a zero velocity).
    long stillness_duration_threshold = 100;
    // If this object has not moved farther than 'movement_distance_threshold'
    // since the last moving event, it is not considered to be moving.
    int[] movement_distance_threshold = {1,1};
    
    // 'is_holding' records whether the user's pointing device is pressing on 
    // this object.
    boolean is_holding = false;
    // 'is_dragging' records whether the user's pointing device is moving while
    // pressing on this object.
    boolean is_dragging = false;
    
    // When the user releases the pointing device and this object is moving, 
    // the 'flicker' timer manages the movement of the object from that point
    // on (unless the user replaces their pointing device on the object).
    Timer flicker = null;
    long flick_duration = 50;
    
    public FlickableFrame()
    {
    }
    
    /*
     * 'Holding' means that the user's pointing device is pressed on the object.
     * 
     * 'Dragging' means that the user's pointing device is moving after having
     * been pressed on the object and before having been released.
     * 
     * 'Moving' means that the velocity is over the threshold for movement.
     * The velocity can only be calculated while the object is 'polling'.
     * 
     * 'Polling' means that, every so often the object will determine its
     * position and divide the change by the poll duration to get its velocity.
     */
    public boolean isStill() { return !isMoving(); }
    public boolean isFree() { return !isHolding(); }
    public boolean isHolding() { return is_holding; }
    public boolean isDragging() { return is_dragging; }
    public boolean isMoving() {
        // If we have been still too long, no movement.
        if (deltaTime() > stillness_duration_threshold)
            return false;
        
        // If we haven't moved far enough, no movement.
        if (Math.abs(deltaX()) < movement_distance_threshold[X] &&
            Math.abs(deltaY()) < movement_distance_threshold[Y])
            return false;
        
        // Otherwise, movement.
        return true;
    }
    
    /*
     * The following are variables and methods for recording the position of the
     * user's pointing device and the time of the recording.
     */
    Date last_move_time;
    Date current_time;
    int[] prev_pos = {0,0};
    int[] curr_pos = {0,0};
    float[] offset = {0,0};
    
    void resetPointerPosition(int x, int y)
    {
        offset[X] = 0;
        offset[Y] = 0;
        
        prev_pos[X] = curr_pos[X] = x;
        prev_pos[Y] = curr_pos[Y] = y;
        
        has_moved = true;
    }
    void resetDragTime()
    {
        last_move_time = new Date(0);
        current_time = new Date();
        
        has_moved = true;
    }
    
    void updatePointerPosition(int x, int y)
    {
        offset[X] = 0;
        offset[Y] = 0;
        
        prev_pos[X] = curr_pos[X];
        prev_pos[Y] = curr_pos[Y];
        
        curr_pos[X] = x;
        curr_pos[Y] = y;
        
        has_moved = true;
    }
    void updateDragTime()
    {
        last_move_time = current_time;
        current_time = new Date();
        
        has_moved = true;
    }
    
    void killFlicker()
    {
        System.out.println("kill flicker");
        if (flicker != null) flicker.cancel();
        flicker = null;
    }
    
    public void pointerDown(int x, int y)
    {
        System.out.println("down");
        
        super.pointerDown(x, y);
        
        // Stop the auto_scrolling if it is going on.
        killFlicker();
        
        // Reset the velocity and time.
        is_holding = true;
        resetPointerPosition(x,y);
        resetDragTime();
    }
    
    public void pointerDrag(int x, int y)
    {
        System.out.println("drag");
        
        super.pointerDrag(x, y);
        
        is_dragging = true;
        updatePointerPosition(x,y);
        updateDragTime();
    }
    
    float flickX() { return (getVelocity()[X] * flick_duration); }
    float flickY() { return (getVelocity()[Y] * flick_duration); }
    void decelerate() { velocity[X] *= deceleration[X]; //Math.pow(deceleration[X], flick_duration / 1000f);
                        velocity[Y] *= deceleration[Y]; } //Math.pow(deceleration[Y], flick_duration / 1000f); }
    
    public void pointerFlick(int x, int y)
    {
        System.out.print("flick: ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        // This method should be overridden by any subclass that wishes to take
        // advantage of the flicking of this object.
    }
    
    public void pointerUp(int x, int y)
    {
        System.out.println("up");
        
        super.pointerUp(x, y);
        
        is_holding = false;
        is_dragging = false;
        updateDragTime();
        
        flicker = new Timer();
        flicker.schedule(new FlickTask(this), 0, flick_duration);
    }
    
    class FlickTask extends TimerTask
    {
        FlickableFrame frame;
        
        public static final int X = 0;
        public static final int Y = 1;
        
        float[] continue_threshold = {1,1};
        
        public FlickTask(FlickableFrame frame)
        {
            this.frame = frame;
        }
        
        public void run()
        {
            float dx = frame.flickX();
            float dy = frame.flickY();
            
            if (dx < continue_threshold[X] && 
                dy < continue_threshold[Y]) {
                frame.killFlicker();
                return;
            }
            
            frame.offset[X] += dx;
            frame.offset[Y] += dy;
            
            frame.pointerFlick((int)(frame.curr_pos[X]+frame.offset[X]), 
                               (int)(frame.curr_pos[Y]+frame.offset[Y]));
            frame.decelerate();
        }
    }
    
    /*
     * The following are tools to determine the current velocity of the frame,
     * and keep track of whether the frame has moved since the last calculation.
     * If the frame has not moved, then the functions will return their stored
     * values, so as not to do unnecessary floating-point calculations and 
     * function calls.float
     */
    boolean has_moved = false;
    float[] velocity = {0,0};
    
    int deltaX() { return curr_pos[X] - prev_pos[X]; }
    int deltaY() { return curr_pos[Y] - prev_pos[Y]; }
    long deltaTime() { return current_time.getTime() - last_move_time.getTime(); }
    
    public synchronized float[] getVelocity()
    {
        // Only update the velocity if we have to.
        if (!has_moved)
        {
        }
        else if (!isMoving())
        {
            velocity[X] = velocity[Y] = 0;
        }
        else
        {
            int dx = deltaX();
            int dy = deltaY();
            long dt = deltaTime();

            velocity[X] = (float) dx / dt;
            velocity[Y] = (float) dy / dt;
        }
        
//        System.out.print(velocity[X]);
//        System.out.print(", ");
//        System.out.println(velocity[Y]);
        
        // ::NOTE:: This 'has_moved' way of keeping track of state is vulnerable
        //          to race conditions.  I just hope it won't be noticed too 
        //          much.  And hopefully the 'synchronized' will solve it.
        has_moved = false;
        return velocity;
    }
}
