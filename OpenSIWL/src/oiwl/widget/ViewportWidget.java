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
public abstract class ViewportWidget extends WidgetWithLayout {
    private Widget m_widget;
    int m_viewx=0, m_viewy=0;

    public void view(Widget widget) {
        m_widget = widget;
    }

    public Widget getViewedWidget() {
        return this.m_widget;
    }

    public int getViewXOffset() {
        return this.m_viewx;
    }

    public int getViewYOffset() {
        return this.m_viewy;
    }

    public void setViewXOffset(int x) {
        this.m_viewx = x;
    }

    public void setViewYOffset(int y) {
        this.m_viewy = y;
    }
    
    void draw(Graphics g, int xoff, int yoff, int x, int y, int width, int height) {
        Widget widget = this.getViewedWidget();
        if (widget != null) {
            int viewx = this.getViewXOffset();
            int viewy = this.getViewYOffset();
            widget.draw(g, xoff - viewx, yoff - viewy,
                    x + viewx, x + viewy, width, height);
        }

        Layout layout = this.getLayout();
        if (layout != null) {
            int layoutx = getLayoutXPos();
            int layouty = getLayoutYPos();
            layout.draw(g, xoff + layoutx, yoff + layouty,
                    x - layoutx, y - layouty, width, height);
        }
    }
}
