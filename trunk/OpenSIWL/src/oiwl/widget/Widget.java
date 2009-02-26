/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

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
    private EventSender m_events;

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
     * Request a redraw of this Widget.  The WidgetParent is responsible for
     * figuring out what to do with the request.
     */
    protected void redraw() {
        this.getParent().handleChildRedraw(this.getXPos(), 
                this.getYPos(), this.getWidth(), this.getHeight());
    }
    
    /**
     * Add an object to the set of this Widget object's listeners.
     * @param evl The EventListener that receives events for this Widget.
     */
    public void addEventListener(EventListener evl) {
        m_events.addListener(evl);
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
            this.m_events.sendEvent(Event.RESIZED, this, new Size(w,h));
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
            this.m_events.sendEvent(Event.RESIZED, this, new Size(w,h));
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
     * Set the x-coordinate of this Widget object's top-left corner
     * @param aPos The  x-coordinate of this Widget object's top-left corner
     */
    void setXPos(int aPos) {
        if (m_xpos != aPos) {
            int x = this.getXPos();
            int y = this.getYPos();

            m_xpos = aPos;
            this.m_events.sendEvent(Event.MOVED, this, new Point(x,y));
        }
    }

    /**
     * Get the x-coordinate of this Widget object's top-left corner
     * @return The x-coordinate of this Widget object's top-left corner
     */
    public int getXPos() {
        return m_xpos;
    }

    /**
     * Set the y-coordinate of this Widget object's top-left corner
     * @param aPos The y-coordinate of this Widget object's top-left corner
     */
    void setYPos(int aPos) {
        if (m_ypos != aPos) {
            int x = this.getXPos();
            int y = this.getYPos();

            m_ypos = aPos;
            this.m_events.sendEvent(Event.MOVED, this, new Point(x,y));
        }
    }
    
    /**
     * Get the y-coordinate of this widget's top-left corner
     * @return The y-coordinate of this widget's top-left corner
     */
    public int getYPos() {
        return m_ypos;
    }
    
    void setPos(int x, int y) {
        this.setXPos(x);
        this.setYPos(y);
    }
    
    public int getLeft() {
        return this.getXPos();
    }
    
    public int getTop() {
        return this.getYPos();
    }
    
    public int getRight() {
        return this.getLeft() + this.getWidth();
    }
    
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
    public boolean contains(int x, int y) {
        return ((x >= this.getXPos()) && 
                (y >= this.getYPos()) &&
                (x < this.getXPos() + this.getWidth()) &&
                (y < this.getYPos() + this.getHeight()));
    }
    
    /**
     * Determine whether the rectangular region specified by the bounds 
     * intersects with this Widget
     * @param l The left edge of the region
     * @param t The top of the region
     * @param w The width of the region
     * @param h The height of the region
     * @return True if the region intersect this Widget.  False otherwise.
     */
    public boolean intersects(int l, int t, int w, int h) {
        int r = l + w;
        int b = t + h;
        
        return ((l < this.getRight()) &&
                (r > this.getLeft()) &&
                (t < this.getBottom()) &&
                (b > this.getTop()));
    }

    /**
     * Update the specified section of the widget.
     * @param g The Graphics context to draw to.
     * @param x The x-coordinate of the top-left corner, relative to the 
     *          Widget's position
     * @param y The y-coordinate of the top-left corner, relataive to the 
     *          Widget's position
     * @param width The width of the section
     * @param height The height of the section
     */
    abstract void draw(Graphics g, int x, int y, int width, int height);
    
    /**
     * This is equivalent to calling render with <code>x=0</code>,
     * <code>y=0</code>, <code>width=getWidth()</code>, and
     * <code>height=getHeight()</code>.
     * @param g The Graphics context to draw to.
     */
    void draw(Graphics g) {
        this.draw(g, 0, 0, this.getWidth(), this.getHeight());
    }
}
