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
public abstract class WidgetWithLayout extends Widget implements WidgetParent {

    Layout m_layout;
    
    public void handleChildRedraw(Widget item, int x, int y, int w, int h) {
        this.getParent().handleChildRedraw(item, x, y, w, h);
    }
    
    protected void setLayout(Layout aLayout) {
        if (aLayout == null)
            throw new IllegalArgumentException("Layout should not be null");
        m_layout = aLayout;
        m_layout.setParent(this);
        m_layout.setLocalPos(this.getLayoutXPos(), this.getLayoutYPos());
        m_layout.setSize(this.getLayoutWidth(), this.getLayoutHeight());
    }
    
    public Layout getLayout() {
        return m_layout;
    }
    
    public int getLayoutXPos() {
        return 0;
    }
    
    public int getLayoutYPos() {
        return 0;
    }
    
    public int getLayoutWidth() {
        return this.getWidth();
    }
    
    public int getLayoutHeight() {
        return this.getHeight();
    }
    
    void setWidth(int aSize) {
        super.setWidth(aSize);
        Layout layout = getLayout();
        if (layout != null)
            layout.setWidth(this.getLayoutWidth());
    }
    
    void setHeight(int aSize) {
        super.setHeight(aSize);
        Layout layout = getLayout();
        if (layout != null)
            layout.setHeight(this.getLayoutHeight());
    }
    
    public int getMinWidth() {
        Layout layout = this.getLayout();
        if (layout != null)
            return layout.getStretchedWidth();
        return 10;
    }
    
    public int getMinHeight() {
        Layout layout = this.getLayout();
        if (layout != null)
            return layout.getStretchedHeight();
        return 10;
    }
    
    /**
     * The method called to respond to user input or any other events passed 
     * along from this Widget object's parent.  A WidgetWithLayout will first
     * check whether its layout sends an event and proceed from there.
     * @param type The type of notification passed down
     * @param data The data that goes with the particular type of notification
     * @return True if this Widget ends up emitting an event as a result of the
     *         notification; false otherwise.
     */
    public boolean handleEvent(int type, Object data) {
        boolean already_handled = this.getLayout().handleEvent(type, data);
        return already_handled;
    }

    void draw(Graphics g, int xoff, int yoff, int x, int y, int width, int height) {
        Layout layout = this.getLayout();
        int layoutx = layout.getLocalXPos();
        int layouty = layout.getLocalYPos();
        getLayout().draw(g, xoff + layoutx, yoff + layouty,
                x - layoutx, y - layouty, width, height);
    }
}
