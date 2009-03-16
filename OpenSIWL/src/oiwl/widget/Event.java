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
    
    public static final int TEXT_DONE = NewEventType();
    public static final int POSITION_CHANGED = NewEventType();
    public static final int MULTI_SELECTED = NewEventType();
    
}
