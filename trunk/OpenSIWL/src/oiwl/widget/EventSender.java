/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Vector;

/**
 *
 * @author mjumbewu
 */
public class EventSender {
    private Vector m_listeners;
    private boolean m_do_events;
    
    public void addListener(EventListener evl) {
        m_listeners.addElement(evl);
    }
    
    public void removeListener(EventListener evl) {
        m_listeners.removeElement(evl);
    }
    
    public void sendEvent(int type, Item sender, Object data) {
        if (eventsAreAllowed()) {
            int num_listeners = m_listeners.size();
            for (int i = 0; i < num_listeners; ++i) {
                ((EventListener)m_listeners.elementAt(i)).onEvent(type, sender, data);
            }
        }
    }
    
    public void supressEvents() {
        m_do_events = false;
    }
    
    public void allowEvents() {
        m_do_events = true;
    }
    
    public boolean eventsAreAllowed() {
        return this.m_do_events;
    }
}
