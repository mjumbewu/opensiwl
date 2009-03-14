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
    int m_state;
    
    public StatefulButton(int initState) {
        super();
        m_state = initState;
    }
    
    public abstract boolean isValidState(int state);
    
    public int getState() {
        return this.m_state;
    }
    
    protected void onStateChanged(int old_state) {}
    
    protected void setState(int state) {
        int old_state = this.getState();
        if (old_state != state) {
            if (isValidState(state)) {
                this.m_state = state;
                this.onStateChanged(old_state);
                this.getEventSender().sendEvent(Event.STATE_CHANGED, this, new Integer(old_state));
            } else {
                throw new IllegalArgumentException("Invalid state for Button");
            }
        }    
    }
}
