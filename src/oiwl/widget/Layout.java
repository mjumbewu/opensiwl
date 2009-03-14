/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author mjumbewu
 */
public abstract class Layout extends Widget 
        implements WidgetParent, EventListener {

    private Vector m_widgets = new Vector();

    protected void addWidgetSafely(Widget item, int index) {
        if (this.isValidChild(item)) {
            item.setParent(this);
            item.addEventListener(this);
            this.addWidget(item, index);
        } else {
            throw new java.lang.IllegalArgumentException("Cannot add Item of" +
                    "type " + item.getClass().getName() + " to Layout");
        }
    }

    protected void addWidget(Widget item, int index) {
        if (index == -1) m_widgets.addElement(item);
        else             m_widgets.insertElementAt(item, index);
        this.invalidateSizes();
    }

    protected void removeWidget(Widget item) {
        m_widgets.removeElement(item);
        this.invalidateSizes();
    }

    protected void clearWidgets() {
        m_widgets.removeAllElements();
        this.invalidateSizes();
    }

    /**
     * Get the Widget contained in a given Cell
     * @param index The index of the cell
     * @return The Widget in a Cell
     */
    public Widget getWidget(int index) {
        return (Widget)m_widgets.elementAt(index);
    }

    public Widget getFirstWidget() {
        return (Widget)m_widgets.firstElement();
    }

    public Widget getLastWidget() {
        return (Widget)m_widgets.lastElement();
    }

    /**
     * Get the index of the given Widget.
     * @param item The Widget
     * @return The index of the Widget, or -1 if no such Widget is managed by this
     *         Layout.
     */
    public int getIndexOf(Widget item) {
        return m_widgets.indexOf(item);
    }

    protected void replaceWidget(int index, Widget newItem) {
        m_widgets.setElementAt(newItem, index);
        this.invalidateSizes();
    }

    protected void replaceWidget(Widget oldItem, Widget newItem) {
        this.replaceWidget(this.getIndexOf(oldItem), newItem);
    }

    /**
     * Get the number of Widget objects in the Layout
     * @return The number of objects in the Layout
     */
    public int getWidgetCount() {
        return m_widgets.size();
    }

    /**
     * Manage an Widget with the Layout.  All unspecified parameters are set
     * to the default.
     * @param text The Widget
     */
    abstract public void manage(Widget item);
    
    public void onEvent(int type, Widget sender, Object data) {
//        if (type == Event.RESIZED)
            invalidateSizes();
    }
    
    public boolean isValidChild(Widget item) {
        return this.getParent().isValidChild(item);
    }
    
    public void handleChildRedraw(Widget item, int x, int y, int w, int h) {
        this.getParent().handleChildRedraw(item, x, y, w, h);
    }
    
    private boolean m_areSizesValid = false;
    private int m_stretched_width;
    private int m_stretched_height;
    
    /**
     * Call recalculateLayout to update the values reported by getWidth and
     * getHeight.  Only classes derived from Layout should ever need to
     * call recalculateLayout.
     */
    protected abstract void recalculateLayout();

    /**
     * Check whether the values reported by getWidth and getHeight are
     * accurate.
     * @return true if getWidth and getHeight are valid; false otherwise.
     */
    protected boolean areSizesValid() {
        return this.m_areSizesValid;
    }

    /**
     * Set a flag to notify the Layout that the width and height need to be
     * recalculated.
     */
    protected void invalidateSizes() {
        this.m_areSizesValid = false;
        this.redraw();
    }

    /**
     * Set a flag to notify the Layout that the width and height DO NOT need
     * to be recalculated.
     */
    protected void validateSizes() {
        this.m_areSizesValid = true;
    }

    /**
     * Get the width of the bounding box of the Layout object's contents.  The
     * stretched size is the minimum size needed to encompas a Layout object's
     * complete contents.
     * @return The width of the bounding box
     */
    public int getStretchedWidth() {
        if (!m_areSizesValid) this.recalculateLayout();
        return m_stretched_width;
    }
    
    /**
     * Set the width of the bounding box
     * @param w The width of the bounding box
     */
    protected void setStretchedWidth(int w) {
        m_stretched_width = w;
    }
    
    /**
     * Get the height of the bounding box of the Layout object's contents.  The
     * stretched size is the minimum size needed to encompas a Layout object's
     * complete contents.
     * @return The height of the bounding box
     */
    public int getStretchedHeight() {
        if (!m_areSizesValid) this.recalculateLayout();
        return m_stretched_height;
    }
    
    /**
     * Set the height of the bounding box
     * @param h The height of the bounding box
     */
    protected void setStretchedHeight(int h) {
        m_stretched_height = h;
    }
    
    /**
     * Set the size of the bounding box of the Layout object's contents.  The
     * stretched size is the minimum size needed to encompas a Layout object's
     * complete contents.
     * @param w The width of the bounding box
     * @param h The height of the bounding box
     */
    protected void setStretchedSize(int w, int h) {
        this.setStretchedWidth(w);
        this.setStretchedHeight(h);
    }
    
    /**
     * Get the effective width of the Layout object.  This will be the greater
     * of the stretched width (the contents' bounding box) and the minimum 
     * width (as likely set by this object's container).
     * @return The effective width
     */
    public int getWidth() {
        return Math.max(this.getSuggestedWidth(), this.getStretchedWidth());
    }
    
    /**
     * Get the effective height of the Layout object.  This will be the greater
     * of the stretched height (the contents' bounding box) and the minimum 
     * height (as likely set by this object's container).
     * @return The effective height
     */
    public int getHeight() {
        return Math.max(this.getSuggestedHeight(), this.getStretchedHeight());
    }
    
    public int getMinWidth() {
        return this.getStretchedWidth();
    }
    
    public int getMinHeight() {
        return this.getStretchedHeight();
    }
    
    /**
     * Draw the specified region (given in parent coordinates, not local) of
     * the Layout.
     * @param g The target Graphic context
     * @param x The left edge of the region
     * @param y The top of the region
     * @param width The width of the region
     * @param height The height of the region
     */
    void draw(Graphics g, int xoff, int yoff, int x, int y, int width, int height) {
        // ::TODO:: Optimize this drawing method.  For example, only draw cells
        //          that intersect the region; consider drawing to a stored 
        //          image so that all the draw calls don't have to be made
        //          each time, but only when you need a different portion of 
        //          the image.
        
        int num_items = this.getWidgetCount();
        
        for (int i = 0; i < num_items; ++i) {
            Widget item = this.getWidget(i);
            int itemx = item.getLocalXPos();
            int itemy = item.getLocalYPos();

            // Only draw the Widget if it's within the draw region.
            if (item.intersectsLocal(x-itemx, y-itemy, width, height)) {
                g.drawRect(xoff + itemx, yoff + itemy, item.getWidth(), item.getHeight());
                item.draw(g, xoff + itemx, yoff + itemy,
                        x - itemx, y - itemy, width, height);
            }
        }
    }
    
    /**
     * The method called to respond to user input or any other events passed 
     * along from this Widget object's parent.  A Layout object's handler is 
     * simple; it will simply pass the notification onto its managed Widget
     * objects.
     * @param type The type of notification passed down
     * @param data The data that goes with the particular type of notification
     * @return True if this Widget ends up emitting an event as a result of the
     *         notification; false otherwise.
     */
    public boolean handleEvent(int type, Object data) {
        int num_items = this.getWidgetCount();
        for (int i = 0; i < num_items; ++i) {
            Widget item = this.getWidget(i);
            if (item.handleEvent(type, data))
                return true;
        }
        return false;
    }
}
