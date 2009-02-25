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
    
    public void handleChildRedraw(int x, int y, int w, int h) {
        this.getParent().handleChildRedraw(x, y, w, h);
    }
    
    public void setLayout(Layout aLayout) {
        if (aLayout == null)
            throw new IllegalArgumentException("Layout should not be null");
        m_layout = aLayout;
        m_layout.setParent(this);
        m_layout.setSize(this.getWidth(), this.getHeight());
    }
    
    public Layout getLayout() {
        return m_layout;
    }
    
    void setWidth(int aSize) {
        Layout layout = getLayout();
        if (layout != null)
            layout.setWidth(aSize);
        super.setWidth(aSize);
    }
    
    void setHeight(int aSize) {
        Layout layout = getLayout();
        if (layout != null)
            layout.setHeight(aSize);
        super.setHeight(aSize);
    }
    
}
