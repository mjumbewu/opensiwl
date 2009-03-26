/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

/**
 * I would just have classes that derive from WidgetWithLayout derive directly
 * from Layout, except that would limit the type of layout that those widgets
 * could have.  For example, a list item would be a widget with a layout because
 * it could be layed out horizontally, vertically, etc.  If the list item
 * derived from layout, there would have to be a different type of list item
 * for each type of layout.
 * @author mjumbewu
 */
public abstract class WidgetWithLayout extends Widget implements WidgetParent {

    Layout m_layout = null;
    
    public void handleChildRedraw(Widget item, int x, int y, int w, int h) {
        this.getParent().handleChildRedraw(item, x, y, w, h);
    }
    
    protected void setLayout(Layout aLayout) {
        if (aLayout == null)
            throw new IllegalArgumentException("Layout should not be null");
        m_layout = aLayout;
        m_layout.setParent(this);
        m_layout.setLocalPos(this.getDefaultLayoutXPos(), this.getDefaultLayoutYPos());
        m_layout.setSize(this.getDefaultLayoutWidth(), this.getDefaultLayoutHeight());
    }
    
    public Layout getLayout() {
        return m_layout;
    }
    
    protected int getDefaultLayoutXPos() {
        return 0;
    }
    
    protected int getDefaultLayoutYPos() {
        return 0;
    }
    
    protected int getDefaultLayoutWidth() {
        return this.getWidth();
    }
    
    protected int getDefaultLayoutHeight() {
        return this.getHeight();
    }
    
    public void setWidth(int aSize) {
        Layout layout = getLayout();
        if (layout != null)
            layout.setWidth(this.getDefaultLayoutWidth());
    }
    
    public void setHeight(int aSize) {
        Layout layout = getLayout();
        if (layout != null)
            layout.setHeight(this.getDefaultLayoutHeight());
    }
    
    public int getMinWidth() {
        Layout layout = this.getLayout();
        if (layout != null)
            return layout.getMinWidth();
        return 0;
    }
    
    public int getMinHeight() {
        Layout layout = this.getLayout();
        if (layout != null)
            return layout.getMinHeight();
        return 0;
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
    public boolean handlePointerEvent(int type, PointerTracker pointer) {
        boolean already_handled = this.getLayout().handlePointerEvent(type, pointer);
        return already_handled;
    }

    public void cancelPointerEvents() {
        super.cancelPointerEvents();
        this.cancelLayoutPointerEvents();
    }

    public void cancelLayoutPointerEvents() {
        this.getLayout().cancelPointerEvents();
    }
    
    void draw(Graphics g, int xoff, int yoff, int x, int y, int width, int height) {
        int layoutx = getLayoutXPos();
        int layouty = getLayoutYPos();
        getLayout().draw(g, xoff + layoutx, yoff + layouty,
                x - layoutx, y - layouty, width, height);
    }

    public int getLayoutXPos() {
        return 0;
    }

    public int getLayoutYPos() {
        return 0;
    }

    public int getWidth() {
        return getLayout().getWidth();
    }

    public int getHeight() {
        return getLayout().getHeight();
    }

    public int getChildXPos(Widget child) {
        if (child == this.getLayout())
            return this.getLayoutXPos();
        else
            throw new IllegalArgumentException("WidgetWithLayout should only " +
                    "have a Layout as a child.");
    }

    public int getChildYPos(Widget child) {
        if (child == this.getLayout())
            return this.getLayoutYPos();
        else
            throw new IllegalArgumentException("WidgetWithLayout should only " +
                    "have a Layout as a child.");
    }
}
