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
public class PushButton extends Button {
    protected void onTapped() {}
    
    public void tap() {
        this.getEventSender().sendEvent(Event.TAPPED, this, null);
        this.onTapped();
    }
    
    private Point pressPoint = null;
    private Point releasePoint = null;
    public boolean handleEvent(int type, Object data) {
        boolean already_handled = super.handleEvent(type, data);
        
        if (already_handled) {
            this.setPressed(false);
            return true;
        }
        
        else if (type == Event.PRESSED) {
            pressPoint = (Point)data;
            if (this.containsGlobal(pressPoint.x, pressPoint.y))
                this.setPressed(true);
            else
                pressPoint = null;
            
            // Press is not the same as click.  Do not block, because other
            // Widget objects may need the data from the press event.
            return false;
        }
        
        else if (type == Event.RELEASED) {
            releasePoint = (Point)data;
            if (pressPoint != null &&
                    pressPoint.x == releasePoint.x &&
                    pressPoint.y == releasePoint.y) {
                tap();
                return true;
            }
            return false;
        }
        
        return false;
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        int c0 = g.getColor();
        if (!this.isPressed())
            g.setColor(0x00000000);
        else
            g.setColor(0x000000ff);
        g.drawRect(xoff, yoff, this.getWidth(), this.getHeight());
        g.setColor(c0);
        
        Layout layout = this.getLayout();
        int layoutx = layout.getLocalXPos();
        int layouty = layout.getLocalYPos();
        getLayout().draw(g, xoff + layoutx, yoff + layouty,
                x - layoutx, y - layouty, w, h);
    }
}
