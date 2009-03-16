/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public abstract class StatefulButton extends Button {
    /**
     * For when Widget (e.g. Button) state changes.
     */
    public static final int STATE_CHANGED_EVENT = Event.NewEventType();

    int m_state;
    
    public StatefulButton(int initState) {
        super();
        m_state = initState;
    }
    
    public abstract boolean isValidState(int state);
    
    public int getPointerState() {
        return this.m_state;
    }
    
    protected void onStateChanged(int old_state) {}
    
    protected void setPointerState(int state) {
        int old_state = this.getPointerState();
        if (old_state != state) {
            if (isValidState(state)) {
                this.m_state = state;
                this.onStateChanged(old_state);
                this.getEventSender().sendEvent(STATE_CHANGED_EVENT, this, new Integer(old_state));
            } else {
                throw new IllegalArgumentException("Invalid state for Button");
            }
        }    
    }
}
