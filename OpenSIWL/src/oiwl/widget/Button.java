/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

/**
 * If we think about it, a button is just a Widget with state.  Push buttons,
 * toggle buttons, check buttons, radio buttons, safety buttons, switch 
 * buttons, etc.
 * 
 * Some other possible states that certain buttons might want to take into 
 * account are:
 *  - UNKNOWN/NEBULOUS/MULTI (e.g. for tri-state checkboxes)
 *  - CHANGING(?) (for safety/switch buttons; not sure if it's useful)
 * 
 * And radio buttons would have to have access to the group to which they are
 * a part so that just one can have an ACTIVE state at a time.
 * @author mjumbewu
 */
public class Button extends WidgetWithLayout implements WidgetParent {
    public static int ACTIVE = 0;
    public static int INACTIVE = 1;
    
    int m_state;
    
    public Button() {
        super();
    }
    
    public boolean isValidChild(Widget item) {
        return (StaticWidget.class.isInstance(item) ||
                Layout.class.isInstance(item));
    }
    
    public boolean isValidState(int state) {
        return (state == ACTIVE || state == INACTIVE);
    }
    
    public int getState() {
        return this.m_state;
    }
    
    protected void setState(int state) {
        if (isValidState(state)) {
            int old_state = this.getState();
            this.m_state = state;
            this.getEventSender().sendEvent(Event.STATE_CHANGED, this, new Integer(state));
        } else {
            throw new IllegalArgumentException("Invalid state for Button");
        }    
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        if (this.intersectsLocal(x, y, w, h)) {
            g.drawRect(xoff, yoff, this.getWidth(), this.getHeight());
            Layout layout = this.getLayout();
            int layoutx = layout.getLocalXPos();
            int layouty = layout.getLocalYPos();
            getLayout().draw(g, xoff + layoutx, yoff + layouty,
                    x - layoutx, y - layouty, w, h);
        }
    }
}
