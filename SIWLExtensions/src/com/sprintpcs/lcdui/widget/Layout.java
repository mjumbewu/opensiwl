/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author mjumbewu
 */
public abstract class Layout extends ItemWidget 
        implements DynamicallySizedWidget {
    private int m_suggestedWidth = 0;
    private int m_suggestedHeight = 0;

    /**
     * The internal representation of a item box or cell
     * @author mjumbewu
     */
    protected class Cell {
        ItemInfo item = null;
    }
    
    private Vector m_cells = new Vector();

    /**
     * The method used to create a new Cell
     * @return A new Cell object
     */
    protected abstract Cell makeCell();
    
    /**
     * Return the Vector of Cells in the Layout
     * @return The Vector of Cells in the Layout
     */
    protected Vector getCells() {
        return m_cells;
    }
    
    /**
     * Return the Cell indexed by aCellNumber
     * @param aIndex The index of the Cell
     * @return The Cell indexed by aCellNumber
     */
    protected Cell getCell(int aIndex) {
        return (Cell)m_cells.elementAt(aIndex);
    }
    
    /**
     * Manage an item with the Layout.  All unspecified parameters are set
     * to the default.
     * @param text The Widget
     */
    public void manage(Object item) {
        this.manage(item, -1);
    }

    /**
     * Manage an item with the Layout.
     * @param item The item to be managed.
     * @param index The desired index for the item
     */
    public void manage(Object item, int index) {
        this.addCell(this.makeCell(), item, index);
    }

    /**
     * An internal method to initialize a given Cell with the given values and
     * insert it into the set of Cell objects
     * @param newCell The Cell to initialize
     * @param item The item to put in the Cell
     * @param index The index at which to insert the Cell; if index=-1 then the
     *              Cell is appended
     */
    protected void addCell(Cell newCell, Object item, int index) {
        newCell.item = ItemInfo.create(item);

        if (index == -1)
            m_cells.addElement(newCell);
        else
            m_cells.insertElementAt(newCell, index);

        this.invalidateSizes();
    }

    /**
     * Remove the item from the Layout
     * @param aIndex The index of the item to be removed
     * @return The removed item
     */
    public Object unmanage(int aIndex) {
        Cell cell = this.getCell(aIndex);
        Object item = cell.item.getItem();
        this.removeCell(cell);
        return item;
    }

    /**
     * Remove the item from the Layout
     * @param item The item to be removed
     * @return The removed item
     */
    public Object unmanage(Object item) {
        int index = this.getIndexOf(item);
        return unmanage(index);
    }

    public void unmanageAll() {
        m_cells.removeAllElements();
        this.invalidateSizes();
    }
    
    protected void removeCell(Cell trashCell) {
        m_cells.removeElement(trashCell);
        this.invalidateSizes();
    }
    
    /**
     * Get the item contained in a given Cell
     * @param aIndex The index of the cell
     * @return The Widget in a Cell
     */
    public Object getItem(int aIndex) {
        return ((Cell)m_cells.elementAt(aIndex)).item.getItem();
    }
    
    /**
     * Get the number of Widget objects in the Layout
     * @return The number of objects in the Layout
     */
    public int getItemCount() {
        return m_cells.size();
    }
    
    /**
     * Get the index of the given item.
     * @param item The item
     * @return The index of the item, or -1 if no such item is managed by this
     *         Layout.
     */
    public int getIndexOf(Object item) {
        int num_item = this.getItemCount();
        for (int i = 0; i < num_item; ++i)
            if (this.getItem(i) == item)
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
    public abstract void recalculateSizes();

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
     * Set the width of the Layout.  This size is ultimately only a suggestion
     * as the Layout will take up as much room as it needs to.
     * @param w The suggested width of the Layout
     */
    public void setWidth(int w) {
        m_suggestedWidth = w;
    }

    /**
     * Set the height of the Layout.  This size is ultimately only a suggestion
     * as the Layout will take up as much room as it needs to.
     * @param h The suggested height of the Layout
     */
    public void setHeight(int h) {
        m_suggestedHeight = h;
    }

    /**
     * Set the size of the Layout.  This size is ultimately only a suggestion
     * as the Layout will take up as much room as it needs to.
     * @param w The suggested witdth of the Layout
     * @param h The suggested height of the Layout
     */
    public void setSize(int w, int h) {
        setWidth(w);
        setHeight(h);
        this.invalidateSizes();
        this.recalculateSizes();
    }

    /**
     * Set the size of the Layout according to the size of the given Widget.
     * @param wid The Widget to match the size of
     */
    public void setSizeToWidget(Widget wid) {
        this.setPosition(0, 0);
        this.setSize(wid.getWidth(), wid.getHeight());
    }

    /**
     * Get the effective width of the Layout object.  This will be the greater
     * of the stretched width (the contents' bounding box) and the minimum 
     * width (as likely set by this object's container).
     * @return The effective width
     */
    public int getWidth() {
        return Math.max(m_suggestedWidth, this.getStretchedWidth());
    }
    
    /**
     * Get the effective height of the Layout object.  This will be the greater
     * of the stretched height (the contents' bounding box) and the minimum 
     * height (as likely set by this object's container).
     * @return The effective height
     */
    public int getHeight() {
        return Math.max(m_suggestedHeight, this.getStretchedHeight());
    }

    /**
     * Get the minimum (stretched) width of the item
     * @return The stretched width
     */
    public int getMinWidth() {
        return this.getStretchedWidth();
    }

    /**
     * Get the minimym (stretched) height of the item
     * @return The stretched height
     */
    public int getMinHeight() {
        return this.getStretchedHeight();
    }
    
    /**
     * "Draw" the item.  This method will simply perform some operations that
     * need to be performed in order for the item to display correctly.
     * @param g The target Graphics context
     */
    public void draw(Graphics g) {
        this.recalculateSizes();
    }
}
