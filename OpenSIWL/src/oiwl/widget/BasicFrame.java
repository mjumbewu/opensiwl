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
public class BasicFrame extends Frame {
    private Vector m_widgets;
    private InternalWidgetListener listener;
    private Graphics m_graphics;
    
    public BasicFrame() {
        super();
        m_graphics = this.getGraphics();
    }
    
    public void draw(Graphics g, int x,int y, int w, int h) {
        int num_widgets = this.getTotalWidget();
        for (int i = 0; i < num_widgets; ++i) {
            ItemWidget iw = this.getItemAt(i);
            if (iw.intersects(x, y, w, h)) iw.draw(g);
        }
    }
    
    private class InternalWidgetListener implements EventListener {
        BasicFrame frame;
        
        InternalWidgetListener(BasicFrame frame) {
            this.frame = frame;
        }
        
        void redrawFrame(Widget widget, 
                int oldx, int oldy, int oldw, int oldh,
                int newx, int newy, int neww, int newh) {
            int l = Math.min(oldx, newx);
            int t = Math.min(oldy, newy);
            int r = Math.max(oldx+oldw, newx+neww);
            int b = Math.max(oldy+oldh, newy+newh);
            
            frame.draw(m_graphics, l, t, r-l, b-t);
        }
        
        void widgetMoved(Widget widget, Point point) {
            int width = widget.getWidth();
            int height = widget.getHeight();
            redrawFrame(widget,
                    point.x, point.y, width, height,
                    widget.getXPos(), widget.getYPos(), width, height);
        }
        
        void widgetSized(Widget widget, Size size) {
            int left = widget.getXPos();
            int top = widget.getYPos();
            redrawFrame(widget,
                    left, top, size.w, size.h,
                    left, top, widget.getWidth(), widget.getHeight());
        }
        
        public void onEvent(int type, Widget sender, Object data) {
            if (type == Event.MOVED)
                widgetMoved(sender, (Point)data);
        }
    }
    
    public void addWidget(ItemWidget aWidget) {
        if (this.getIndex(aWidget) == -1)
            m_widgets.addElement(aWidget);
    }
    
    public int getIndex(ItemWidget aWidget) {
        return this.m_widgets.indexOf(aWidget);
    }
    
    public ItemWidget getItemAt(int index) {
        return (ItemWidget)this.m_widgets.elementAt(index);
    }
    
    public void removeItemWidget(ItemWidget aWidget) {
        this.m_widgets.removeElement(aWidget);
    }
    
    public int getTotalWidget() {
        return this.m_widgets.size();
    }
    
    public void pointerDown (int x, int y) {}
    public void pointerDrag (int x, int y) {}
    public void pointerUp   (int x, int y) {}
    public void pointerFlick(int x, int y) {}
    
    public void pointerPressed(int x, int y) {
        this.pointerDown(x, y);
    }
    
    public void pointerDragged(int x, int y) {
        this.pointerDrag(x, y);
    }
    
    public void pointerReleased(int x, int y) {
        this.pointerUp(x, y);
    }
}
