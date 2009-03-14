/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

import java.util.TimerTask;
import java.util.Date;
import java.util.Vector;

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
    private EventSender m_eventSender = new EventSender();
    
    protected EventSender getEventSender() {
        return m_eventSender;
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
        m_eventSender.addListener(evl);
    }

    public void removeEventListener(EventListener evl) {
        m_eventSender.removeListener(evl);
    }

    /**
     * Get the minimum width of the Layout
     * @return The width of the Layout
     */
    public int getSuggestedWidth() {
        return this.m_width;
    }

    /**
     * Set the width minimum of the Layout
     * @param aWidth The width of the Layout
     */
    protected void setSuggestedWidth(int width) {
        if (m_width != width) {
            int w = this.getWidth();
            int h = this.getHeight();

            this.m_width = width;
            this.sendSizeChange(w, h);
        }
    }

    /**
     * Get the minimum height of the Layout
     * @return The height of the Layout
     */
    public int getSuggestedHeight() {
        return this.m_height;
    }

    /**
     * Set the minimum height of the Layout
     * @param aHeight The height of the Layout
     */
    protected void setSuggestedHeight(int height) {
        if (m_height != height) {
            int w = this.getWidth();
            int h = this.getHeight();

            this.m_height = height;
            this.sendSizeChange(w, h);
        }
    }

    /**
     * Set the minimum width and height of the Layout
     * @param w The width of the Layout
     * @param h The height of the Layout
     */
    protected void setSuggestedSize(int w, int h) {
        this.setSuggestedWidth(w);
        this.setSuggestedHeight(h);
    }
    
    /**
     * Get the height of this Widget.
     * @return The height of this widget
     */
    public int getHeight() {
        return Math.max(this.getMinHeight(), this.getSuggestedHeight());
    }

    /**
     * Get the minimum height that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual height.  For some (e.g. Layout)
     * it may be smaller.
     * @return The minimum height that this Widget desires to be
     */
    int getMinHeight() {
        return this.m_height;
    }

    /**
     * Suggest a height for this Widget.
     * @param height The height suggestion
     */
    void setHeight(int height) {
        this.setSuggestedHeight(height);
    }

    /**
     * Get the width of this Widget.
     * @return The width of this widget
     */
    public int getWidth() {
        return Math.max(this.getMinWidth(), this.getSuggestedWidth());
    }
    
    /**
     * Get the minimum width that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual width.  For some (e.g. Layout)
     * it may be smaller.
     */
    int getMinWidth() {
        return this.m_width;
    }

    /**
     * Suggest a width for this Widget.
     * @param width The width suggestion
     */
    void setWidth(int width) {
        this.setSuggestedWidth(width);
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
            this.m_eventSender.sendEvent(Event.RESIZED, this, new SizeData(oldw,oldh));
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
            this.m_eventSender.sendEvent(Event.MOVED, this, new LocationData(oldx,oldy));
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

    class MoveTask extends TimerTask
    {
        Widget widget;
        MovementData data;
        float friction;
        
        // Rate that an object must be moving in either the x or y direction
        // in order to still be considered moving.
        float movement_threshold = 0.5f;
        
        public MoveTask(Widget widget, MovementData data, float friction)
        {
            this.widget = widget;
            this.data = data;
            this.friction = friction;
        }
        
        public void run()
        {
            long current_time = (new Date()).getTime();
            data.dt = current_time - data.t0;
            float dt_in_seconds = data.dt / 1000.0f;
            
            // dx = int(v * dt)
            // v  = v0 + dv
            // dv = int(a * dt)
            // v  = v0 + int(a * dt)
            //    = v0 + a * t
            // dx = int([v0 + a * t] * dt)
            //    = int(v0 * dt) + int(a * t * dt)
            //    = v0 * t + int(a * t * dt)
            //    = v0 * t + 1/2 * a * t^2
            float dv_due_to_friction = this.friction * dt_in_seconds;
            float new_vx = data.vx + dv_due_to_friction;
            float new_vy = data.vy + dv_due_to_friction;
            
            if (new_vx < movement_threshold && new_vy < movement_threshold) {
                data.timer.cancel();
                return;
            }
            
            float dx_due_to_friction = dv_due_to_friction * dt_in_seconds / 2;
            data.dx = (int)(data.vx * dt_in_seconds + dx_due_to_friction);
            data.dy = (int)(data.vy * dt_in_seconds + dx_due_to_friction);
            
            widget.handleEvent(Event.FLICKED, data);
        }
    }
}
