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
    
    public static final int TAPPED = 0;
    public static final int DCLICKED = 1;
    public static final int FLICK = 2;
    public static final int FLICKED = 2;
    
    public static final int TEXT_DONE = 3;
    public static final int POSITION_CHANGED = 4;
    public static final int MULTI_SELECTED = 5;
    
    /**
     * The constant for the MOVED event type.  The auxiliary data for this 
     * type of event will be a Point object representing the previous position
     * of the moved Item.
     */
    public static final int MOVED = 6;

    /**
     * The constant for the RESIZED event type.  The auxiliary data for this 
     * type of event will be a Size object representing the previous size
     * of the resized Item.
     */
    public static final int RESIZED = 7;
    
    /**
     * For when Widget (e.g. Button) state changes.
     */
    public static final int STATE_CHANGED = 8;
}
