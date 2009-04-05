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
public class Panel extends WidgetWithLayout implements WidgetParent {
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    
    int m_attatchment;
    
    public Panel(int attatch) {
        super();
//        this.setSize(50, 50);
        this.m_attatchment = attatch;
        if (attatch == TOP || attatch == BOTTOM)
            setLayout(new LinearLayout(Orientation.HORIZONTAL));
        else
            setLayout(new LinearLayout(Orientation.VERTICAL));
    }
    
    public boolean isValidChild(Widget item) {
        return (ItemWidget.class.isInstance(item) ||
                StaticWidget.class.isInstance(item) ||
                Layout.class.isInstance(item));
    }
    
    public int getAttachment() {
        return this.m_attatchment;
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        if (this.intersectsLocal(x, y, w, h)) {
            g.drawRect(xoff, yoff, this.getWidth(), this.getHeight());
            Layout layout = this.getLayout();
            int layoutx = this.getLayoutXPos();
            int layouty = this.getLayoutYPos();
            getLayout().draw(g, xoff + layoutx, yoff + layouty,
                    x - layoutx, y - layouty, w, h);
        }
    }
}
