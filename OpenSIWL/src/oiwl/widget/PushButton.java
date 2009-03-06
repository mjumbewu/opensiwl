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
        if (!this.handleEvent(Event.TAPPED, null)) {
            this.getEventSender().sendEvent(Event.TAPPED, this, null);
            this.onTapped();
        }
    }
    
    public void doDTap() {
        this.getEventSender().sendEvent(Event.DTAPPED, this, null);
        this.handleEvent(Event.DTAPPED, null);
    }
    
    TapEventMachine tapEvent = new TapEventMachine(this);
    DoubleTapEventMachine dtapEvent = new DoubleTapEventMachine(this, 300);
    
    public boolean handleEvent(int type, Object data) {
        tapEvent.advance(type, data);
        if (tapEvent.getState() == TapEventMachine.ACCEPT_STATE) {
            this.tapEvent.reset();
            this.doTap();
            return true;
        }
        
        dtapEvent.advance(type, data);
        if (dtapEvent.getState() == DoubleTapEventMachine.ACCEPT_STATE) {
            this.dtapEvent.reset();
            return true;
        }
        
        return false;
    }
    
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        int c0 = g.getColor();
        if (tapEvent.getState() != TapEventMachine.HOLDING_STATE)
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
