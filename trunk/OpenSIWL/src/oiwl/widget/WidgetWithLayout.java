/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public abstract class WidgetWithLayout extends Widget implements WidgetParent {

    Layout m_layout;
    
    public void handleChildRedraw(Widget item, int x, int y, int w, int h) {
        this.getParent().handleChildRedraw(item, x, y, w, h);
    }
    
    public void setLayout(Layout aLayout) {
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
    
    public boolean handleEvent(int type, Object data) {
        return this.getLayout().handleEvent(type, data);
    }
}
