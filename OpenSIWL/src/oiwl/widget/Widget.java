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

    void setParent(WidgetParent ip) {
        this.m_parent = ip;
    }
    
    public WidgetParent getParent() {
        return this.m_parent;
    }
    
    protected void requestRedraw() {
        this.getParent().handleChildRedraw(this.getXPos(), 
                this.getYPos(), this.getWidth(), this.getHeight());
    }
    
    /**
     * Add an object to the set of this widget's listeners.
     * @param evl The EventListener that receives events for this widget.
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
    
    int getMinHeight() {
        return this.getHeight();
    }
    
    void setHeight(int height) {
        this.m_height = height;
    }

    /**
     * Get the width of this Widget.
     * @return The width of this widget
     */
    public int getWidth() {
        return this.m_width;
    }
    
    int getMinWidth() {
        return this.getWidth();
    }
    
    void setWidth(int width) {
        this.m_width = width;
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
     * Set the x-coordinate of this widget's top-left corner
     * @param aPos The  x-coordinate of this widget's top-left corner
     */
    void setXPos(int aPos) {
        if (m_xpos != aPos) {
            m_xpos = aPos;
        }
    }

    /**
     * Get the x-coordinate of this widget's top-left corner
     * @return The x-coordinate of this widget's top-left corner
     */
    public int getXPos() {
        return m_xpos;
    }

    /**
     * Set the y-coordinate of this widget's top-left corner
     * @param aPos The y-coordinate of this widget's top-left corner
     */
    void setYPos(int aPos) {
        if (m_ypos != aPos) {
            m_ypos = aPos;
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
