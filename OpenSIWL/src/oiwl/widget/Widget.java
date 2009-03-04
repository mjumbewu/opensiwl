/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

import java.util.Timer;
import java.util.Date;

/**
 *
 * @author mjumbewu
 */
public abstract class Widget {
    private int m_height;
    private int m_width;
    private int m_xpos;
    private int m_ypos;

    private WidgetParent m_parent;
    private EventSender m_events = new EventSender();

    protected EventSender getEventSender() {
        return m_events;
    }
    
    /**
     * Set the WidgetParent for this Widget.  The application should have no
     * need to call this method; only the Widget or its new WidgetParent need.
     * @param ip The new WidgetParent of this Widget
     */
    void setParent(WidgetParent ip) {
        this.m_parent = ip;
    }

    /**
     * Get the WidgetParent of this Widget.
     * @return This Widget object's WidgetParent
     */
    public WidgetParent getParent() {
        return this.m_parent;
    }

    /**
     * Add an object to the set of this Widget object's listeners.
     * @param evl The EventListener that receives events for this Widget.
     */
    public void addEventListener(EventListener evl) {
        m_events.addListener(evl);
    }

    public void removeEventListener(EventListener evl) {
        m_events.removeListener(evl);
    }

    /**
     * Get the height of this Widget.
     * @return The height of this widget
     */
    public int getHeight() {
        return this.m_height;
    }

    /**
     * Get the minimum height that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual height.  For some (e.g. Layout)
     * it may be smaller.
     * @return The minimum height that this Widget desires to be
     */
    int getMinHeight() {
        return this.getHeight();
    }

    /**
     * Suggest a height for this Widget.
     * @param height The height suggestion
     */
    void setHeight(int height) {
        if (m_height != height) {
            int w = this.getWidth();
            int h = this.getHeight();

            this.m_height = height;
            this.sendSizeChange(w, h);
        }
    }

    /**
     * Get the width of this Widget.
     * @return The width of this widget
     */
    public int getWidth() {
        return this.m_width;
    }
    
    /**
     * Get the minimum width that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual width.  For some (e.g. Layout)
     * it may be smaller.
     */
    int getMinWidth() {
        return this.getWidth();
    }

    /**
     * Suggest a width for this Widget.
     * @param width The width suggestion
     */
    void setWidth(int width) {
        if (m_width != width) {
            int w = this.getWidth();
            int h = this.getHeight();

            this.m_width = width;
            this.sendSizeChange(w, h);
        }
    }
    
    /**
     * Set the width and height of the Widget.  In some cases, this is just a
     * suggestion for the width and the height (e.g. in the case of a Layout,
     * when this sets the minimum width and height).
     * @param w The suggested width
     * @param h The suggested height
     */
    void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
    }

    /**
     * Send a resized event if the size has actually changed
     * @param oldw The previous width
     * @param oldh The previous height
     */
    protected void sendSizeChange(int oldw, int oldh) {
        if (oldw != this.getWidth() || oldh != this.getHeight())
            this.m_events.sendEvent(Event.RESIZED, this, new Size(oldw,oldh));
    }

    /**
     * Set the x-coordinate of this Widget object's top-left corner
     * @param aPos The  x-coordinate of this Widget object's top-left corner
     */
    void setLocalXPos(int aPos) {
        if (m_xpos != aPos) {
            int x = this.getLocalXPos();
            int y = this.getLocalYPos();

            m_xpos = aPos;
            this.sendPosChange(x, y);
        }
    }

    /**
     * Get the x-coordinate of this Widget object's top-left corner
     * @return The x-coordinate of this Widget object's top-left corner
     */
    public int getLocalXPos() {
        return m_xpos;
    }

    public int getGlobalXPos() {
        return getLocalXPos() + getParent().getGlobalXPos();
    }

    /**
     * Set the y-coordinate of this Widget object's top-left corner
     * @param aPos The y-coordinate of this Widget object's top-left corner
     */
    void setLocalYPos(int aPos) {
        if (m_ypos != aPos) {
            int x = this.getLocalXPos();
            int y = this.getLocalYPos();

            m_ypos = aPos;
            this.sendPosChange(x, y);
        }
    }
    
    /**
     * Get the y-coordinate of this widget's top-left corner
     * @return The y-coordinate of this widget's top-left corner
     */
    public int getLocalYPos() {
        return m_ypos;
    }

    public int getGlobalYPos() {
        return getLocalYPos() + getParent().getGlobalYPos();
    }

    /**
     * Set the x- and y-coordinates of this Widget object's top-left corner
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    void setLocalPos(int x, int y) {
        this.setLocalXPos(x);
        this.setLocalYPos(y);
    }
    
    /**
     * Send a moved event if the size has actually changed
     * @param oldx The previous x-position
     * @param oldy The previous y-position
     */
    protected void sendPosChange(int oldx, int oldy) {
        if (oldx != this.getLocalXPos() || oldy != this.getLocalYPos())
            this.m_events.sendEvent(Event.MOVED, this, new Point(oldx,oldy));
    }

    /**
     * Get the x-value of the left-most edge of the Widget
     * @return The left-most edge
     */
    public int getLeft() {
        return this.getGlobalXPos();
    }
    
    /**
     * Get the y-value of the top-most edge of the Widget
     * @return The top-most edge
     */
    public int getTop() {
        return this.getGlobalYPos();
    }
    
    /** 
     * Get the x-value of the right-most edge of the Widget
     * @return The right-most edge
     */
    public int getRight() {
        return this.getLeft() + this.getWidth();
    }
    
    /**
     * Get the y-value of the bottom-most edge of the Widget
     * @return The bottom-most edge
     */
    public int getBottom() {
        return this.getTop() + this.getHeight();
    }
    
    /**
     * Determine whether the point specified by the coordinates are within the
     * bounding area of the Widget
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return True if the point is within the bounds of the widget, false
     *         otherwise
     */
    public boolean containsGlobal(int x, int y) {
        return ((x >= this.getGlobalXPos()) &&
                (y >= this.getGlobalYPos()) &&
                (x < this.getGlobalXPos() + this.getWidth()) &&
                (y < this.getGlobalYPos() + this.getHeight()));
    }
    
    public boolean containsLocal(int x, int y) {
        return ((x >= 0) &&
                (y >= 0) &&
                (x < this.getWidth()) &&
                (y < this.getHeight()));
    }
    
    /**
     * Determine whether the rectangular region specified by the bounds 
     * intersectsGlobal with this Widget
     * @param l The left edge of the region
     * @param t The top of the region
     * @param w The width of the region
     * @param h The height of the region
     * @return True if the region intersect this Widget.  False otherwise.
     */
    public boolean intersectsGlobal(int l, int t, int w, int h) {
        int r = l + w;
        int b = t + h;
        
        return ((l < this.getRight()) &&
                (r > this.getLeft()) &&
                (t < this.getBottom()) &&
                (b > this.getTop()));
    }

    public boolean intersectsLocal(int l, int t, int w, int h) {
        int r = l + w;
        int b = t + h;
        
        return ((r > 0) &&
                (b > 0) &&
                (l < this.getWidth()) &&
                (t < this.getHeight()));
    }
    
    private boolean m_canDraw = true;
    public void supressDraw() {
        this.m_canDraw = false;
    }
    
    public void allowDraw() {
        this.m_canDraw = true;
    }
    
    public boolean canDraw() {
        return this.m_canDraw;
    }
    
    /**
     * Request a redraw of this Widget.  The WidgetParent is responsible for
     * figuring out what to do with the request.
     */
    public void redraw() {
        if (this.canDraw())
            this.getParent().handleChildRedraw(this, 
                    0, 0, this.getWidth(), this.getHeight());
    }
    
    /**
     * Update the specified section of the widget.  The given bounds are given
     * in local coordinates
     * @param g The Graphics context to draw to.
     * @param xoff The global (i.e. device coordinated) x offset where the
     *             Widget will be drawn
     * @param yoff The global y offset where the Widget will be drawn
     * @param x The local x-coordinate of the top-left corner
     * @param y The local y-coordinate of the top-left corner
     * @param width The width of the section
     * @param height The height of the section
     */
    abstract void draw(Graphics g, int xoff, int yoff,
            int x, int y, int width, int height);
    
    /**
     * This is equivalent to calling render with <code>x=0</code>,
     * <code>y=0</code>, <code>width=getWidth()</code>, and
     * <code>height=getHeight()</code>.
     * @param g The Graphics context to draw to.
     * @param xoff The global (i.e. device coordinated) x offset where the
     *             Widget will be drawn
     * @param yoff The global y offset where the Widget will be drawn
     */
    void draw(Graphics g, int xoff, int yoff) {
        this.draw(g, xoff, yoff, 0, 0, this.getWidth(), this.getHeight());
    }
    
    /**
     * Handle the given event.
     * @param type The type of event
     * @param data The data relevant for the event
     * @return True if the Widget handles the event (and should block other
     *         Widget objects from handling it as well).
     */
    public abstract boolean handleEvent(int type, Object data);

//    ///////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////
//    // Flicking
//    ///////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////
//
//    public static final int X = 0;
//    public static final int Y = 1;
//    
//    // How much velocity should we retain after each flick step?
//    float[] flick_factor = {0.9f,0.9f};
//    float[] elasticity = {0,0};
//    
//    // If this object has not had a moving event in longer than
//    // 'stillness_duration_threshold', it is not considered to be moving (i.e.
//    // it has a zero velocity).
//    long stillness_duration_threshold = 100;
//    // If this object has not dragged farther than 'drag_movement_threshold'
//    // since the last moving event, and it has not moved farther than
//    // 'flick_movement_threshold' since the last flick event, it is not 
//    // considered to be moving.
//    int[] drag_movement_threshold = {5,5};
//    int[] flick_movement_threshold = {1,1};
//    
//    // 'is_holding' records whether the user's pointing device is pressing on 
//    // this object.
//    boolean is_holding = false;
//    // 'is_dragging' records whether the user's pointing device is moving while
//    // pressing on this object.
//    boolean is_dragging = false;
//    
//    // When the user releases the pointing device and this object is moving, 
//    // the 'flicker' timer manages the movement of the object from that point
//    // on (unless the user replaces their pointing device on the object).
//    Timer flicker = null;
//    long flick_duration = 50;
//    
//    /*
//     * 'Holding' means that the user's pointing device is pressed on the object.
//     * 
//     * 'Dragging' means that the user's pointing device is moving after having
//     * been pressed on the object and before having been released.
//     * 
//     * 'Flicking' means that the frame has been flicked and has not yet 
//     * decelerated to a stop.
//     * 
//     * 'Moving' means that the velocity is over the threshold for movement.
//     * The velocity can only be calculated while the object is 'polling'.
//     */
//    public boolean isHolding() { return is_holding; }
//    public boolean isDragging() { return is_dragging; }
//    public boolean isFlicking() { return (flicker != null); }
//    public boolean isMoving() {
//        if (isHolding()) {
//            // If we have been still too long, no movement.
//            if (dragDeltaTime() > stillness_duration_threshold)
//                return false;
//
//            // If we haven't dragged far enough, no movement.
//            if (Math.abs(dragDeltaX()) < drag_movement_threshold[X] &&
//                Math.abs(dragDeltaY()) < drag_movement_threshold[Y])
//                return false;
//            
//            return true;
//        }
//        
//        if (isFlicking())
//            return true;
//        
//        // Otherwise, no movement.
//        return false;
//    }
//    
//    /*
//     * The following are variables and methods for recording the position of the
//     * user's pointing device and the time of the recording.
//     */
//    
//    // prev_pos and curr_pos both refer to the position of the pointer.
//    private int[] prev_pos = {0,0};
//    private int[] curr_pos = {0,0};
//    
//    // last_move_time and current_time  refer to the time at which the screen 
//    // was polled for the pointer position on a press or drag.
//    private Date last_move_time;
//    private Date current_time;
//    
//    // offset is the distance moved since the frame was flicked.
//    private float[] offset = {0,0};
//    
//    private void resetPointerPosition(int x, int y)
//    {
//        offset[X] = 0;
//        offset[Y] = 0;
//        
//        prev_pos[X] = curr_pos[X] = x;
//        prev_pos[Y] = curr_pos[Y] = y;
//        
//        has_updated = true;
//    }
//    
//    private void resetDragTime()
//    {
//        last_move_time = new Date(0);
//        current_time = new Date();
//        
//        has_updated = true;
//    }
//    
//    private void updatePointerPosition(int x, int y)
//    {
//        offset[X] = 0;
//        offset[Y] = 0;
//        
//        prev_pos[X] = curr_pos[X];
//        prev_pos[Y] = curr_pos[Y];
//        
//        curr_pos[X] = x;
//        curr_pos[Y] = y;
//        
//        has_updated = true;
//    }
//    
//    void updateDragTime()
//    {
//        last_move_time = current_time;
//        current_time = new Date();
//        
//        has_updated = true;
//    }
//    
//    void killFlicker()
//    {
//        if (flicker != null) flicker.cancel();
//        flicker = null;
//    }
//    
//    public void pointerDown(int x, int y)
//    {
//        System.out.print("down  ");
//        System.out.print(x);
//        System.out.print(", ");
//        System.out.println(y);
//        
//        super.pointerDown(x, y);
//        
//        // Stop the auto_scrolling if it is going on.
//        killFlicker();
//        
//        // Reset the velocity and time.
//        is_holding = true;
//        resetPointerPosition(x,y);
//        resetDragTime();
//    }
//    
//    public void pointerDrag(int x, int y)
//    {
//        System.out.print("drag  ");
//        System.out.print(x);
//        System.out.print(", ");
//        System.out.println(y);
//        
//        super.pointerDrag(x, y);
//        
//        is_dragging = true;
//        updatePointerPosition(x,y);
//        updateDragTime();
//    }
//    
//    private float flickDeltaX() { return (getVelocity()[X] * flick_duration); }
//    private float flickDeltaY() { return (getVelocity()[Y] * flick_duration); }
//    void decelerate() { velocity[X] *= flick_factor[X]; //Math.pow(flick_factor[X], flick_duration / 1000f);
//                        velocity[Y] *= flick_factor[Y]; } //Math.pow(flick_factor[Y], flick_duration / 1000f); }
//    
//    public void pointerFlick(int x, int y)
//    {
//        System.out.print("flick ");
//        System.out.print(x);
//        System.out.print(", ");
//        System.out.println(y);
//        
//        // This method should be overridden by any subclass that wishes to take
//        // advantage of the flicking of this object.
//    }
//    
//    public void pointerUp(int x, int y)
//    {
//        System.out.print("up    ");
//        System.out.print(x);
//        System.out.print(", ");
//        System.out.println(y);
//        
//        super.pointerUp(x, y);
//        
//        is_holding = false;
//        is_dragging = false;
//        updateDragTime();
//        
//        flicker = new Timer();
//        flicker.schedule(new FlickTask(this), 0, flick_duration);
//    }
//    
//    class FlickTask extends TimerTask
//    {
//        FlickableFrame frame;
//        
//        public static final int X = 0;
//        public static final int Y = 1;
//        
//        public FlickTask(FlickableFrame frame)
//        {
//            this.frame = frame;
//        }
//        
//        public void run()
//        {
//            float dx = frame.flickDeltaX();
//            float dy = frame.flickDeltaY();
//            
//            if (dx < frame.flick_movement_threshold[X] && 
//                dy < frame.flick_movement_threshold[Y]) {
//                frame.killFlicker();
//                return;
//            }
//            
//            frame.offset[X] += dx;
//            frame.offset[Y] += dy;
//            
//            frame.pointerFlick((int)(frame.curr_pos[X]+frame.offset[X]), 
//                               (int)(frame.curr_pos[Y]+frame.offset[Y]));
//            frame.decelerate();
//        }
//    }
//    
//    /*
//     * The following are tools to determine the current velocity of the frame,
//     * and keep track of whether the frame has moved since the last calculation.
//     * If the frame has not moved, then the functions will return their stored
//     * values, so as not to do unnecessary floating-point calculations and 
//     * function calls.float
//     */
//    boolean has_updated = false;
//    float[] velocity = {0,0};
//    
//    int dragDeltaX() { return curr_pos[X] - prev_pos[X]; }
//    int dragDeltaY() { return curr_pos[Y] - prev_pos[Y]; }
//    long dragDeltaTime() { return current_time.getTime() - last_move_time.getTime(); }
//    
//    public synchronized float[] getVelocity()
//    {
//        // Only update the velocity if we have to (in response to a press or 
//        // drag).
//        if (has_updated)
//        {
//            if (!isMoving())
//            {
//                velocity[X] = velocity[Y] = 0;
//            }
//            else
//            {
//                float dx = dragDeltaX();
//                float dy = dragDeltaY();
//                long  dt = dragDeltaTime();
//
//                velocity[X] = (float) dx / dt;
//                velocity[Y] = (float) dy / dt;
//            }
//        }
//        
////        System.out.print(velocity[X]);
////        System.out.print(", ");
////        System.out.println(velocity[Y]);
//        
//        // ::NOTE:: This 'has_updated' way of keeping track of state is vulnerable
//        //          to race conditions.  I just hope it won't be noticed too 
//        //          much.  And hopefully the 'synchronized' will solve it.
//        has_updated = false;
//        return velocity;
//    }
//    
}
