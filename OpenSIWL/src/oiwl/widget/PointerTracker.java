/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Date;

/**
 *
 * @author mjumbewu
 */
public class PointerTracker {
    // prev_pos and curr_pos both refer to the position of the pointer.
    int orig_pointer_x, prev_pointer_x, curr_pointer_x;
    int orig_pointer_y, prev_pointer_y, curr_pointer_y;
    
    // last_move_time and current_time  refer to the time at which the screen
    // was polled for the pointer position on a press or drag.
    Date original_time, last_drag_time, current_time;

    float velocity_x;
    float velocity_y;
    
    boolean needs_updated_velocity;

    PointerTracker copy() {
        PointerTracker dup = new PointerTracker();
        dup.orig_pointer_x = this.orig_pointer_x;
        dup.orig_pointer_y = this.orig_pointer_y;
        dup.prev_pointer_x = this.prev_pointer_x;
        dup.prev_pointer_y = this.prev_pointer_y;
        dup.curr_pointer_x = this.curr_pointer_x;
        dup.curr_pointer_y = this.curr_pointer_y;

        dup.original_time = new Date(this.original_time.getTime());
        dup.last_drag_time = new Date(this.last_drag_time.getTime());
        dup.current_time = new Date(this.current_time.getTime());
        
        dup.velocity_x = this.velocity_x;
        dup.velocity_y = this.velocity_y;
        dup.needs_updated_velocity = this.needs_updated_velocity;

        return dup;
    }

    void resetPointerPosition(int x, int y)
    {
        orig_pointer_x = prev_pointer_x = curr_pointer_x = x;
        orig_pointer_y = prev_pointer_y = curr_pointer_y = y;

        needs_updated_velocity = true;
    }

    void resetDragTime()
    {
        last_drag_time = new Date(0);
        original_time = current_time = new Date();

        needs_updated_velocity = true;
    }

    void updatePointerPosition(int x, int y)
    {
        prev_pointer_x = curr_pointer_x;
        prev_pointer_y = curr_pointer_y;

        curr_pointer_x = x;
        curr_pointer_y = y;

        needs_updated_velocity = true;
    }

    void updateDragTime()
    {
        last_drag_time = current_time;
        current_time = new Date();

        needs_updated_velocity = true;
    }

    /**
     * Return the last known position of the pointer.
     */
    public int getXPos() { return this.curr_pointer_x; }
    public int getYPos() { return this.curr_pointer_y; }

    /**
     * Return the time of the last pointer update.
     */
    public long getTime() { return this.current_time.getTime(); }

    /**
     * Return the last known velocity of the pointer.
     */
    public float getXVel() {
        if (needs_updated_velocity) this.calcVelocity();
        return this.velocity_x; }
    public float getYVel() {
        if (needs_updated_velocity) this.calcVelocity();
        return this.velocity_y; }

    /*
     * The following are tools to determine the current velocity of the frame,
     * and keep track of whether the frame has moved since the last calculation.
     * If the frame has not moved, then the functions will return their stored
     * values, so as not to do unnecessary floating-point calculations and
     * function calls.float
     */
    
    int dragDeltaX() { return curr_pointer_x - prev_pointer_x; }
    int dragDeltaY() { return curr_pointer_y - prev_pointer_y; }
    long dragDeltaTime() { return current_time.getTime() - last_drag_time.getTime(); }
    
    int totalDragDistance() {
        int dx = this.orig_pointer_x - this.curr_pointer_x;
        int dy = this.orig_pointer_y - this.curr_pointer_y;
        return (int)Math.sqrt(dx*dx + dy*dy);
    }

    void setStillnessThreshold(long t) {
        if (stillness_duration_threshold != t) {
            stillness_duration_threshold = t;
            needs_updated_velocity = true;
        }
    }

    void setMovementThreshold(int d) {
        if (drag_movement_threshold != d) {
            drag_movement_threshold = d;
            needs_updated_velocity = true;
        }
    }

    // If this object has not had a moving event in longer than
    // 'stillness_duration_threshold', it is not considered to be moving (i.e.
    // it has a zero velocity).
    long stillness_duration_threshold = 100;

    // If this object has not been dragged more than 'drag_movement_threshold'
    // pixels, it is not considered to be moving (i.e. it has a zero
    // velocity).  This is like the stillness escape velocity.
    int drag_movement_threshold = 5;

    public boolean isMoving() {
        // If we have been still too long, no movement.
        if (dragDeltaTime() > stillness_duration_threshold) {
            return false;
        }

        // If we haven't moved far enough, no movement.
        else if (totalDragDistance() < drag_movement_threshold) {
            return false;
        }

        return true;
    }
    private synchronized void calcVelocity()
    {
        // Only update the velocity if we have to (in response to a press or
        // drag).
        if (needs_updated_velocity)
        {
            if (!isMoving()) {
                velocity_x = 0f;
                velocity_y = 0f;
            }

            else {
                float dx = dragDeltaX();
                float dy = dragDeltaY();
                long  dt = dragDeltaTime();

                velocity_x = (float) dx / dt * 1000;
                velocity_y = (float) dy / dt * 1000;
            }
        }

        // ::NOTE:: This 'needs_updated_velocity' way of keeping track of state is vulnerable
        //          to race conditions.  I just hope it won't be noticed too
        //          much.  And hopefully the 'synchronized' will solve it.
        needs_updated_velocity = false;
    }
}
