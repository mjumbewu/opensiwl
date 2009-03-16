/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public abstract class StaticWidget extends Widget {
    public boolean handlePointerEvent(int type, PointerTracker pointer) {
        return false;
    }
}
