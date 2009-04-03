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
    public void handleChildRedraw(Widget item, int x, int y, int w, int h);

    /**
     * Get the x-position of this Widget in Frame coordinates.
     * @return The x-position of the Widget
     */
    public int getXPos();
    /**
     * Get the y-position of this Widget in Frame coordinates.
     * @return The y-position of the Widget
     */
    public int getYPos();
    
    /**
     * Get the x-position of the child in local coordinates
     * @param child The child Widget in question
     * @return The x-position of the child
     */
    public int getChildXPos(Widget child);
    /**
     * Get the y-position of the child in local coordinates
     * @param child The child Widget in question
     * @return The y-position of the child
     */
    public int getChildYPos(Widget child);

    /**
     * Get the width of the child
     * @param child The child Widget in question
     * @return The width of the child
     */
    public int getChildWidth(Widget child);
    /**
     * Get the height of the child
     * @param child The child Widget in question
     * @return The height of the child
     */
    public int getChildHeight(Widget child);
}
