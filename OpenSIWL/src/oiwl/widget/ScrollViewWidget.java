/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

class ScrollEventMachine extends EventMachine {
    public static final int SCROLLING_STATE = NewEventState();

    public ScrollEventMachine() {
        super(Event.CLICKED);
    }


}
/**
 *
 * @author mjumbewu
 */
public class ScrollViewWidget extends WidgetWithLayout {
    
    public ScrollViewWidget() {
        setLayout(new LinearLayout(Orientation.VERTICAL));
    }

    public void setLayout(Layout layout) {
        super.setLayout(layout);
    }

    public boolean isValidChild(Widget item) {
        return this.getParent().isValidChild(item);
    }

    ScrollEventMachine scrollEvent = new ScrollEventMachine();

    public boolean handleEvent(int type, Object data) {
        boolean already_handled = false;

        // Don't send pointer events to
        if (scrollEvent.getState() != ScrollEventMachine.SCROLLING_STATE) {
            already_handled = super.handleEvent(type, data);
        }

        if (already_handled) {
            scrollEvent.reset();
            return true;
        }

        return false;
    }
}
