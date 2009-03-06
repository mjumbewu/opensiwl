/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

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
public class Event {
    private static int ev_id = 1;
    protected static int NewEventType() {
        return ev_id++;
    }
    
    public static final int PRESSED = NewEventType();
    public static final int DRAGGED = NewEventType();
    public static final int RELEASED = NewEventType();
    public static final int FLICKED = NewEventType();
    
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
    
    
    private static int st_id = 1;
    protected static int NewEventState() {
        return st_id++;
    }
    
    public static final int START_STATE = NewEventState();
    public static final int ACCEPT_STATE = NewEventState();
    
    int m_eventType;
    int m_eventState = START_STATE;
    Widget m_eventSender;
    
    public Event(int type, Widget sender) {
        this.m_eventType = type;
        this.m_eventSender = sender;
    }
    
    public int getType() {
        return this.m_eventType;
    }
    
    public int getState() {
        return this.m_eventState;
    }
    
    public Widget getSender() {
        return this.m_eventSender;
    }
    
    protected void gotoState(int state) {
        m_eventState = state;
    }
    
    public void resetEvent() {
        m_eventState = START_STATE;
    }
    
    public void acceptEvent() {
        m_eventState = ACCEPT_STATE;
    }
    
    public void advance(int type, Object data) {
        gotoState(ACCEPT_STATE);
    }
}
