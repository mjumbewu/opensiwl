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
    /**
     * The internal representation of a line of text
     * @author mjumbewu
     */
    protected class Cell {
        Widget item = null;
    }
    
    private Vector m_cells = new Vector();
    
    /**
     * The method used to create a new line of text
     * @return A new Cell object
     */
    protected abstract Cell makeCell();
    
    /**
     * Return the Vector of Cells in the TextItem
     * @return The Vector of Cells in the TextItem
     */
    protected Vector getCells() {
        return m_cells;
    }
    
    /**
     * Return the line indexed by aCellNumber
     * @param aIndex The index of the cell
     * @return The Cell indexed by aCellNumber
     */
    protected Cell getCell(int aIndex) {
        return (Cell)m_cells.elementAt(aIndex);
    }
    
    /**
     * Manage an Widget with the Layout.  All unspecified parameters are set
     * to the default.
     * @param text The Widget
     */
    public void manage(Widget item) {
        this.manage(item, -1);
    }
    
    /**
     * Manage an Widget with the Layout.
     * @param text The Widget to be managed.
     * @param index The desired index for the Widget
     */
    public void manage(Widget item, int index) {
        this.addCell(this.makeCell(), item, index);
    }

    public void onEvent(int type, Widget sender, Object data) {
//        if (type == Event.RESIZED)
            invalidateSizes();
    }
    
    /**
     * An internal method to initialized a given Cell with the given values and
     * insert it into the set of Cell objects
     * @param newCell The Cell to initialize
     * @param item The Widget to put in the Cell
     * @param index The index at which to inset the Cell; if index=-1 then the
     *              Cell is appended
     */
    protected void addCell(Cell newCell, Widget item, int index) {
        if (this.isValidChild(item)) {
            item.setParent(this);
            item.addEventListener(this);
            newCell.item = item;

            if (index == -1)
                m_cells.addElement(newCell);
            else
                m_cells.insertElementAt(newCell, index);
            
            this.invalidateSizes();
        } else {
            throw new java.lang.IllegalArgumentException("Cannot add Item of" +
                    "type " + item.getClass().getName() + " to Layout");
        }
    }
    
    /**
     * Remove the Widget from the Layout
     * @param aIndex The index of the Widget to be removed
     * @return The removed Widget
     */
    public Widget unmanage(int aIndex) {
        Cell cell = this.getCell(aIndex);
        Widget item = cell.item;
        this.removeCell(cell);
        return item;
    }

    public Widget unmanage(Widget item) {
        int index = this.getIndexOf(item);
        return unmanage(index);
    }
    
    protected void removeCell(Cell trashCell) {
        trashCell.item.removeEventListener(this);
        m_cells.removeElement(trashCell);
        this.invalidateSizes();
    }
    
    public boolean isValidChild(Widget item) {
        return this.getParent().isValidChild(item);
    }
    
    public void handleChildRedraw(Widget item, int x, int y, int w, int h) {
        this.getParent().handleChildRedraw(item, x, y, w, h);
    }
    
    /**
     * Get the Widget contained in a given Cell
     * @param aIndex The index of the cell
     * @return The Widget in a Cell
     */
    public Widget getWidget(int aIndex) {
        return ((Cell)m_cells.elementAt(aIndex)).item;
    }
    
    /**
     * Get the number of Widget objects in the Layout
     * @return The number of objects in the Layout
     */
    public int getWidgetCount() {
        return m_cells.size();
    }
    
    /**
     * Get the index of the given Widget.
     * @param item The Widget
     * @return The index of the Widget, or -1 if no such Widget is managed by this
     *         Layout.
     */
    public int getIndexOf(Widget item) {
        int num_item = this.getWidgetCount();
        for (int i = 0; i < num_item; ++i)
            if (this.getWidget(i) == item)
                return i;
        return -1;
    }
    
    private boolean m_areSizesValid = false;
    private int m_stretched_width;
    private int m_stretched_height;
    
    /**
     * Call recalculateSizes to update the values reported by getWidth and
     * getHeight.  Only classes derived from Layout should ever need to
     * call recalculateSizes.
     */
    protected abstract void recalculateSizes();

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
        if (!m_areSizesValid) this.recalculateSizes();
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
        if (!m_areSizesValid) this.recalculateSizes();
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
        
        int num_cells = this.getWidgetCount();
        
        for (int i = 0; i < num_cells; ++i) {
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
        int num_cells = this.getWidgetCount();
        for (int i = 0; i < num_cells; ++i) {
            Widget item = this.getWidget(i);
            if (item.handleEvent(type, data))
                return true;
        }
        return false;
    }
}