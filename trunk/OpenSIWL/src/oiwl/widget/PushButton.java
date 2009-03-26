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

    public static final int TAPPED_EVENT = Event.NewEventType();
    public static final int CLICKED_EVENT = TAPPED_EVENT;
    public static final int DTAPPED_EVENT = Event.NewEventType();
    public static final int DCLICKED_EVENT = DTAPPED_EVENT;

    public PushButton() {
        super();
        this.setLayout(new LinearLayout(Orientation.HORIZONTAL));
    }

    private StaticImage image = null;
    private StaticText label = null;

    public void setLabel(StaticText st) {
        Layout layout = this.getLayout();
        layout.supressDraw();
        if (label != null) layout.unmanage(label);
        label = st;
        if (label != null) layout.manage(label);
        layout.allowDraw();
    }

    public void setImage(StaticImage si) {
        LinearLayout layout = (LinearLayout)this.getLayout();
        layout.supressDraw();
        if (image != null) layout.unmanage(image);
        image = si;
        if (image != null) layout.manage(image, 0, 0, false, false);
        layout.allowDraw();
    }

    public void tap() {
        this.onTapped();
        getEventSender().sendEvent(PushButton.TAPPED_EVENT, this, null);
        resetPointerState();
        redraw();
    }
    
    protected void onTapped() {}
    
    public static final int HOLDING_STATE = NewWidgetState();

    public boolean handlePointerEvent(int type, PointerTracker pointer) {
        int state = this.getPointerState();

        if (state == INACTIVE_STATE) {
            if (type == Event.PRESSED) {
                setPointerState(HOLDING_STATE);
                redraw();
                return false;
            }
        }

        else if (state == HOLDING_STATE) {
            // If the pointer is dragged outside of te button, cancel any
            // pending tapEvent events.
            if (type == Event.DRAGGED) {
                if (!contains(pointer.getXPos(), pointer.getYPos())) {
                    cancelPointerEvents();
                    redraw();
                    return false;
                }
            }

            // When the user releases the pointer inside the button, send a tapEvent
            // event if there is one pending.
            else if (type == Event.RELEASED) {
                tap();
                return true;
            }
        }
        
        return false;
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        int c0 = g.getColor();
        if (getPointerState() == HOLDING_STATE)
            g.setColor(0x0000ff00);
        else if (isActive())
            g.setColor(0x00ff0000);
        else
            g.setColor(0x008080ff);
        g.fillRect(xoff, yoff, this.getWidth(), this.getHeight());
        g.setColor(c0);

        super.draw(g, xoff, yoff, x, y, w, h);
    }
}
