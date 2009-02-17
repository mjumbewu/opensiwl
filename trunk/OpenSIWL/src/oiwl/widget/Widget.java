/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;
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

    private Vector m_listeners;

    /**
     * Add an object to the set of this widget's listeners.
     * @param evl The WidgetEventListener that receives events for this widget.
     */
    public void addWidgetEventListener(WidgetEventListener evl) {
        m_listeners.addElement(evl);
    }

    /**
     * Get the height of this widget.
     * @return The height of this widget
     */
    public abstract int getHeight();

    /**
     * Get the width of this widget.
     * @return The width of this widget
     */
    public abstract int getWidth();

    /**
     * Set the x-coordinate of this widget's top-left corner
     * @param aPos The  x-coordinate of this widget's top-left corner
     */
    public void setXPos(int aPos) {
        m_xpos = aPos;
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
    public void setYPos(int aPos) {
        m_ypos = aPos;
    }
    
    /**
     * Get the y-coordinate of this widget's top-left corner
     * @return The y-coordinate of this widget's top-left corner
     */
    public int getYPos() {
        return m_ypos;
    }
    
    /**
     * Determine whether the point specified by the coordinates are within the
     * bounding area of the widget
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return True if the point is within the bounds of the widget, false
     *         otherwise
     */
    public boolean isInWidget(int x, int y) {
        return ((x >= this.getXPos()) && 
                (y >= this.getYPos()) &&
                (x < this.getXPos() + this.getWidth()) &&
                (y < this.getYPos() + this.getHeight()));
    }

    /**
     * Update the specified section of the widget.
     * @param g The Graphics context to draw to.
     * @param x The x-coordinate of the top-left corner
     * @param y The y-coordinate of the top-left corner
     * @param width The width of the section
     * @param height The height of the section
     */
    abstract void render(Graphics g, int x, int y, int width, int height);
    
    /**
     * This is equivalent to calling render with <code>x=0</code>,
     * <code>y=0</code>, <code>width=getWidth()</code>, and
     * <code>height=getHeight()</code>.
     * @param g The Graphics context to draw to.
     */
    void render(Graphics g) {
        this.render(g, 0, 0, this.getWidth(), this.getHeight());
    }
}
