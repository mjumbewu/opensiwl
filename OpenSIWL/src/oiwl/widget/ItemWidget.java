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
public abstract class ItemWidget extends Widget {

    /**
     * Update the specified section of the widget.
     * @param g The Graphics context to draw to.
     * @param x The x-coordinate of the top-left corner
     * @param y The y-coordinate of the top-left corner
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
