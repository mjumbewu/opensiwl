/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public interface WidgetParent {
    public boolean isValidChild(Widget item);
    public void handleChildRedraw(int x, int y, int w, int h);

    public int getGlobalXPos();
    public int getGlobalYPos();
}
