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
    public static int TOP = 0;
    public static int BOTTOM = 1;
    public static int LEFT = 2;
    public static int RIGHT = 3;
    
    int m_attatchment;
    
    public Panel(int attatch) {
        super();
        this.setSize(50, 50);
        this.m_attatchment = attatch;
        if (attatch == TOP || attatch == BOTTOM)
            setLayout(new LinearLayout(Orientation.HORIZONTAL));
        else
            setLayout(new LinearLayout(Orientation.VERTICAL));
    }
    
    public int getGlobalXPos() {
        return getLocalXPos();
    }

    public int getGlobalYPos() {
        return getLocalYPos();
    }

    public boolean isValidChild(Widget item) {
        return (ItemWidget.class.isInstance(item) ||
                StaticWidget.class.isInstance(item) ||
                Layout.class.isInstance(item));
    }
    
    public int getAttachment() {
        return this.m_attatchment;
    }
    
    public void draw(Graphics g, int x, int y, int w, int h) {
        g.drawRect(this.getLocalXPos(), this.getLocalYPos(), this.getWidth(), this.getHeight());
        getLayout().draw(g, x, y, w, h);
    }
}
