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
        if (this.getState() == ACTIVE)
            this.setState(INACTIVE);
        else
            this.setState(ACTIVE);
    }
    
    public boolean handleEvent(int type, Object data) {
        boolean already_handled = super.handleEvent(type, data);
        
        // If the event has somehow been handled, bail.
        if (already_handled) {
            return true;
        }
        
        // A press event is the beginning of a state-change (from a user's
        // standpoint anyway).
        else if (type == Event.PRESSED) {
            LocationData pressPoint = (LocationData)data;
            if (this.m_switch.containsGlobal(pressPoint.x, pressPoint.y)) {
//                this.startStateChange();
            }
            
            // Press is not the same as click.  Do not block, because other
            // Widget objects may need the data from the press event.
            return false;
        }
        
        // If the pointer leaves the widget, cancel the state change.
        else if (type == Event.RELEASED) {
            LocationData releasePoint = (LocationData)data;
            if (this.containsGlobal(releasePoint.x, releasePoint.y)) {
                this.toggleState();
                return true;
            }
            return false;
        }
        
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
