/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public class Event {
    protected static int NewID() {
        int id = 1;
        return id++;
    }
    
    public static final int PRESSED = NewID();
    public static final int DRAGGED = NewID();
    public static final int RELEASED = NewID();
    public static final int FLICKED = NewID();
    
    public static final int TAPPED = NewID();
    public static final int DCLICKED = NewID();
    public static final int FLICK = NewID();
    
    public static final int TEXT_DONE = NewID();
    public static final int POSITION_CHANGED = NewID();
    public static final int MULTI_SELECTED = NewID();
    
    /**
     * The constant for the MOVED event type.  The auxiliary data for this 
     * type of event will be a Point object representing the previous position
     * of the moved Item.
     */
    public static final int MOVED = NewID();

    /**
     * The constant for the RESIZED event type.  The auxiliary data for this 
     * type of event will be a Size object representing the previous size
     * of the resized Item.
     */
    public static final int RESIZED = NewID();
    
    /**
     * For when Widget (e.g. Button) state changes.
     */
    public static final int STATE_CHANGED = NewID();
}
