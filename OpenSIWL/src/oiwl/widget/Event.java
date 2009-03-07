/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Date;

/**
 * 
 * @author mjumbewu
 */
public class Event {
    private static int ev_id = 1;
    protected static int NewEventType() {
        return ev_id++;
    }
    
    public static final int PRESSED = NewEventType();
    public static final int DRAGGED = NewEventType();
    public static final int RELEASED = NewEventType();
    public static final int FLICKED = NewEventType();
    public static final int TIME_EXPIRED = NewEventType();
    
    public static final int TAPPED = NewEventType();
    public static final int CLICKED = TAPPED;
    public static final int DTAPPED = NewEventType();
    public static final int DCLICKED = DTAPPED;
    public static final int FLICK = NewEventType();
    
    public static final int TEXT_DONE = NewEventType();
    public static final int POSITION_CHANGED = NewEventType();
    public static final int MULTI_SELECTED = NewEventType();
    
    /**
     * The constant for the MOVED event type.  The auxiliary data for this 
     * type of event will be a Point object representing the previous position
     * of the moved Item.
     */
    public static final int MOVED = NewEventType();

    /**
     * The constant for the RESIZED event type.  The auxiliary data for this 
     * type of event will be a Size object representing the previous size
     * of the resized Item.
     */
    public static final int RESIZED = NewEventType();
    
    /**
     * For when Widget (e.g. Button) state changes.
     */
    public static final int STATE_CHANGED = NewEventType();
    
}

class EventPath {
    class Node {
        int type;
        Object data;
        Node prev;
    }

    Node last;
}
/**
 * An object that stores event state/progress.  An Event object is essentially
 * a finite state machine.  Different events move it from one state to the next.
 * For example, a PushButton click is implemented in three states: START,
 * HOLDING, and ACCEPT.
 * <ul>
 * <li>From the START state, a PRESSED event in the sending Widget will move the
 * click Event object to the HOLDING state</li>
 * <li>From the HOLDING state, a DRAGGED event outside of the sending Widget
 * will move the click Event back to the START state.</li>
 * <li>From the HOLDING state, a RELEASED event inside of the sending Widget
 * will move the click Event to the ACCEPT state.</li>
 * </ul>
 * Double click would be implemented in much the same way.
 * @author mjumbewu
 */
class EventMachine {
    private static int st_id = 1;
    protected static int NewEventState() {
        return st_id++;
    }
    
    public static final int START_STATE = NewEventState();
    public static final int ACCEPT_STATE = NewEventState();
    
    int m_eventType;
    int m_eventState = START_STATE;
    
    public EventMachine(int type) {
        this.m_eventType = type;
    }
    
    public int getEventType() {
        return this.m_eventType;
    }
    
    public int getState() {
        return this.m_eventState;
    }
    
    protected void gotoState(int state) {
        m_eventState = state;
    }
    
    public void reset() {
        m_eventState = START_STATE;
    }
    
    public void accept() {
        m_eventState = ACCEPT_STATE;
    }

    public void advance(int type, Object data) {
        gotoState(ACCEPT_STATE);
    }
}


abstract class TargetedEventMachine extends EventMachine {
    Widget m_target;

    public TargetedEventMachine(int type, Widget target) {
        super(type);
        m_target = target;
    }

    public Widget getTarget() {
        return this.m_target;
    }

}
class TapEventMachine extends TargetedEventMachine {
    public static final int HOLDING_STATE = NewEventState();

    public TapEventMachine(Widget target) {
        super(Event.TAPPED, target);
    }

    public void advance(int type, Object data) {
        int state = this.getState();
        Widget target = getTarget();

        if (state == START_STATE) {
            if (type == Event.PRESSED) {
                LocationData pressPoint = (LocationData)data;
                if (target.containsGlobal(pressPoint.x, pressPoint.y)) {
                    gotoState(HOLDING_STATE);
                    target.redraw();
                }
            }
        }

        else if (state == HOLDING_STATE) {
            // If the pointer is dragged outside of te button, cancel any
            // pending tapEvent events.
            if (type == Event.DRAGGED) {
                LocationData dragPoint = (LocationData)data;
                if (!target.containsGlobal(dragPoint.x, dragPoint.y)) {
                    reset();
                    target.redraw();
                }
            }

            // When the user releases the pointer inside the button, send a tapEvent
            // event if there is one pending.  If the release is outside of the
            // button then cancel any pending tapEvent events.
            else if (type == Event.RELEASED) {
                LocationData releasePoint = (LocationData)data;
                if (target.containsGlobal(releasePoint.x, releasePoint.y)) {
                    accept();
                } else {
                    reset();
                }
                target.redraw();
            }
        }
    }
}

class DoubleTapEventMachine extends TargetedEventMachine {
    private long dtap_time;
    public static final int TAPPED_ONCE_STATE = NewEventState();

    public DoubleTapEventMachine(Widget target, long dtap_time) {
        super(Event.DTAPPED, target);
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
                    accept();
                } else {
                    first_tap_time = (new Date()).getTime();
                    gotoState(TAPPED_ONCE_STATE);
                }
            }
        }
    }
}

class TimeoutEventMachine extends EventMachine {
    public TimeoutEventMachine(long duration) {
        super(Event.TIME_EXPIRED);
    }
}
