/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public interface EventListener {
    /**
     * This method will be called from each EventSender that this EventListener
     * listens to.
     * @param type The event type
     * @param sender The Item from which the event originated
     * @param data Axiliary data with the event
     */
    void onEvent(int type, Item sender, Object data);
}
