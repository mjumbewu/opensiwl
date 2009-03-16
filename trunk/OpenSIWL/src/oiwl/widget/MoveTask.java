package oiwl.widget;

import java.util.Timer;
import java.util.TimerTask;

class MoveTask extends TimerTask {

    Widget widget;
    PointerTracker orig_pointer;
    PointerTracker flicked_pointer;
    float friction;
    Timer timer;
    
    /**
     * The amount of time that the task should wait before scheduling
     * itself again.
     */
    long TIMER_DELAY = 10;
    // Rate that an object must be moving in either the x or y direction
    // in order to still be considered moving.
    float movement_threshold = 0.5F;
    Widget outer;

    public MoveTask(Widget widget, PointerTracker pointer, float friction) {
        super();
        this.widget = widget;
        this.orig_pointer = pointer.copy();
    }

    public void run() {
        flicked_pointer.updateDragTime();
        long dt = flicked_pointer.getTime() - orig_pointer.getTime();
        float x_accel;
        float y_accel;
        if (orig_pointer.getXVel() < 0) {
            x_accel = this.friction;
        } else {
            x_accel = -this.friction;
        }
        if (orig_pointer.getYVel() < 0) {
            y_accel = this.friction;
        } else {
            y_accel = -this.friction;
        }
        // dx = int(v * dt)
        // v  = v0 + dv
        // dv = int(a * dt)
        // v  = v0 + int(a * dt)
        //    = v0 + a * t
        // dx = int([v0 + a * t] * dt)
        //    = int(v0 * dt) + int(a * t * dt)
        //    = v0 * t + int(a * t * dt)
        //    = v0 * t + 1/2 * a * t^2
        int x = orig_pointer.getXPos() + (int) (orig_pointer.getXVel() * dt + x_accel * dt * dt / 2);
        int y = orig_pointer.getYPos() + (int) (orig_pointer.getYVel() * dt + y_accel * dt * dt / 2);
        flicked_pointer.updatePointerPosition(x, y);
        if (flicked_pointer.getXVel() < movement_threshold && flicked_pointer.getYVel() < movement_threshold) {
            timer.cancel();
            return;
        }
        outer.handlePointerEvent(Event.FLICKED, flicked_pointer);
        timer.schedule(this, TIMER_DELAY);
    }
}
