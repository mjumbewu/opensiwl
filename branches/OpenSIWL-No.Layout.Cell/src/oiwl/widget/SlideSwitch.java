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
public class SlideSwitch extends StatefulButton {
    public static int ACTIVE = 0;
    public static int INACTIVE = 1;
    
    StaticWidget m_switch;
    LinearLayout m_stateLabelLayout;
    StaticWidget m_active;
    StaticWidget m_inactive;
    
    public SlideSwitch() {
        super(ACTIVE);
        this.setLayout(new AbsoluteLayout());
        
        m_switch = new StaticRectangle();
        m_switch.setSize(20, 20);
        m_switch.setLocalPos(0, 0);
        
        m_active = new StaticText("A", 0x0000ff00);
        m_inactive = new StaticText("B", 0x00ff0000);
        m_stateLabelLayout = new LinearLayout(Orientation.HORIZONTAL);
        m_stateLabelLayout.manage(this.m_active);
        m_stateLabelLayout.manage(this.m_inactive);
        
        this.getLayout().manage(this.m_switch);
        this.getLayout().manage(this.m_stateLabelLayout);
    }
    
    public boolean isValidState(int state) {
        return (state == ACTIVE || state == INACTIVE);
    }
    
    public void toggleState() {
        if (this.getPointerState() == ACTIVE)
            this.setPointerState(INACTIVE);
        else
            this.setPointerState(ACTIVE);
    }
    
    public boolean handlePointerEvent(int type, PointerTracker pointer) {
        return false;
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        int c0 = g.getColor();
        g.setColor(0x00c0c0c0);
        g.fillRect(xoff, yoff, this.getWidth(), this.getHeight());
        g.setColor(c0);
        
        Layout layout = this.getLayout();
        int layoutx = layout.getLocalXPos();
        int layouty = layout.getLocalYPos();
        getLayout().draw(g, xoff + layoutx, yoff + layouty,
                x - layoutx, y - layouty, w, h);
    }

}
