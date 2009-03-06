/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;
import java.util.Date;

class TapEvent extends Event {
    public static final int HOLDING_STATE = NewEventState();
    
    public TapEvent(Widget sender) {
        super(Event.TAPPED, sender);
    }
    
    public void advance(int type, Object data) {
        int state = this.getState();
        Widget sender = getSender();
        
        if (state == START_STATE) {
            if (type == Event.PRESSED) {
                LocationData pressPoint = (LocationData)data;
                if (sender.containsGlobal(pressPoint.x, pressPoint.y)) {
                    gotoState(HOLDING_STATE);
                    sender.redraw();
                }
            }
        }
        
        else if (state == HOLDING_STATE) {
            // If the pointer is dragged outside of te button, cancel any 
            // pending tapEvent events.
            if (type == Event.DRAGGED) {
                LocationData dragPoint = (LocationData)data;
                if (!sender.containsGlobal(dragPoint.x, dragPoint.y)) {
                    resetEvent();
                    sender.redraw();
                }
            }
            
            // When the user releases the pointer inside the button, send a tapEvent 
            // event if there is one pending.  If the release is outside of the 
            // button then cancel any pending tapEvent events.
            else if (type == Event.RELEASED) {
                LocationData releasePoint = (LocationData)data;
                if (sender.containsGlobal(releasePoint.x, releasePoint.y)) {
                    acceptEvent();
                } else {
                    resetEvent();
                }
                sender.redraw();
            }
        }
    }
}

class DoubleTapEvent extends Event {
    private long dtap_time;
    public static final int TAPPED_ONCE_STATE = NewEventState();
    
    public DoubleTapEvent(Widget sender, long dtap_time) {
        super(Event.DTAPPED, sender);
        this.dtap_time = dtap_time;
    }
    
    private long first_tap_time;
    public void advance(int type, Object data) {
        int state = this.getState();
        
        if (state == START_STATE) {
            if (type == Event.TAPPED) {
                first_tap_time = (new Date()).getTime();
                gotoState(TAPPED_ONCE_STATE);
            }
        }
        
        else if (state == TAPPED_ONCE_STATE) {
            if (type == Event.TAPPED) {
                long tap_time = (new Date()).getTime();
                System.out.println(tap_time - first_tap_time);
                if (tap_time - first_tap_time <= dtap_time) {
                    acceptEvent();
                } else {
                    first_tap_time = (new Date()).getTime();
                    gotoState(TAPPED_ONCE_STATE);
                }
            }
        }
    }
}

/**
 *
 * @author mjumbewu
 */
public class PushButton extends Button {
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
        Layout layout = this.getLayout();
        layout.supressDraw();
        if (image != null) layout.unmanage(image);
        image = si;
        if (image != null) layout.manage(image, 0);
        layout.allowDraw();
    }
    
    protected void onTapped() {}
    
    public void doTap() {
        this.getEventSender().sendEvent(Event.TAPPED, this, null);
        this.handleEvent(Event.TAPPED, null);
        this.onTapped();
    }
    
    public void doDTap() {
        this.getEventSender().sendEvent(Event.DTAPPED, this, null);
        this.handleEvent(Event.DTAPPED, null);
    }
    
    TapEvent tapEvent = new TapEvent(this);
    DoubleTapEvent dtapEvent = new DoubleTapEvent(this, 300);
    
    public boolean handleEvent(int type, Object data) {
        tapEvent.advance(type, data);
        if (tapEvent.getState() == TapEvent.ACCEPT_STATE) {
            this.tapEvent.resetEvent();
            this.doTap();
            return true;
        }
        
        dtapEvent.advance(type, data);
        if (dtapEvent.getState() == DoubleTapEvent.ACCEPT_STATE) {
            this.dtapEvent.resetEvent();
            return true;
        }
        
        return false;
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        int c0 = g.getColor();
        if (tapEvent.getState() != TapEvent.HOLDING_STATE)
            g.setColor(0x0000ff00);
        else
            g.setColor(0x000000ff);
        g.fillRect(xoff, yoff, this.getWidth(), this.getHeight());
        g.setColor(c0);

        Layout layout = this.getLayout();
        int layoutx = layout.getLocalXPos();
        int layouty = layout.getLocalYPos();
        getLayout().draw(g, xoff + layoutx, yoff + layouty,
                x - layoutx, y - layouty, w, h);
    }
}
