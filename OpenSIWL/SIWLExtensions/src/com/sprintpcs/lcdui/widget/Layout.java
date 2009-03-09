/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

abstract class ItemInfo {
    abstract public void setItem(Object o);
    abstract public Object getItem();

    abstract public int getXPos();
    abstract public int getYPos();
    abstract public void setXPos(int x);
    abstract public void setYPos(int y);

    abstract public int getWidth();
    abstract public int getHeight();
    abstract public void setWidth(int w);
    abstract public void setHeight(int h);

    abstract public int getMinWidth();
    abstract public int getMinHeight();
}

class ItemWidgetInfo extends ItemInfo {
    ItemWidget item;

    public void setItem(Object o) {
        if (ItemWidget.class.isInstance(o))
            item = (ItemWidget)o;
        else
            throw new IllegalArgumentException();
    }

    public Object getItem() { return item; }

    public int getXPos() { return item.getXPos(); }
    public int getYPos() { return item.getYPos(); }
    public void setXPos(int x) { item.setXPos(x); }
    public void setYPos(int y) { item.setYPos(y); }

    public int getWidth() { return item.getWidth(); }
    public int getHeight() { return item.getHeight(); }
    public void setWidth(int w) {}
    public void setHeight(int h) {}

    public int getMinWidth() { return item.getWidth(); }
    public int getMinHeight() { return item.getHeight(); }
}

class LayoutInfo extends ItemInfo {
    Layout layout;

    public void setItem(Object o) {
        if (Layout.class.isInstance(o))
            layout = (Layout)o;
        else
            throw new IllegalArgumentException();
    }

    public Object getItem() { return layout; }

    public int getXPos() { return layout.getXPos(); }
    public int getYPos() { return layout.getYPos(); }
    public void setXPos(int x) { layout.setXPos(x); }
    public void setYPos(int y) { layout.setYPos(y); }

    public int getWidth() { return layout.getWidth(); }
    public int getHeight() { return layout.getHeight(); }
    public void setWidth(int w) { layout.setWidth(w); }
    public void setHeight(int h) { layout.setHeight(h); }

    public int getMinWidth() { return layout.getMinWidth(); }
    public int getMinHeight() { return layout.getMinHeight(); }
}

class TextItemInfo extends ItemInfo {
    TextItem item;

    public void setItem(Object o) {
        if (TextItem.class.isInstance(o))
            item = (TextItem)o;
        else
            throw new IllegalArgumentException();
    }

    public Object getItem() { return item; }

    public int getXPos() { return item.getX(0); }
    public int getYPos() { return item.getY(0); }
    public void setXPos(int x) { item.setCoordinates(0, x, item.getY(0)); }
    public void setYPos(int y) { item.setCoordinates(0, item.getX(0), y); }

    public int getWidth() { return item.getFont(0).stringWidth(item.getText(0)); }
    public int getHeight() { return item.getFont(0).getHeight(); }
    public void setWidth(int w) {}
    public void setHeight(int h) {}

    public int getMinWidth() { return getWidth(); }
    public int getMinHeight() { return getHeight(); }
}

/**
 *
 * @author mjumbewu
 */
public abstract class Layout extends ItemWidget {
    private int m_suggestedWidth = 0;
    private int m_suggestedHeight = 0;

    /**
     * The internal representation of a line of text
     * @author mjumbewu
     */
    protected class Cell {
        ItemInfo item = null;
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
    public void manage(Object item) {
        this.manage(item, -1);
    }

    /**
     * Manage an Widget with the Layout.
     * @param text The Widget to be managed.
     * @param index The desired index for the Widget
     */
    public void manage(Object item, int index) {
        this.addCell(this.makeCell(), item, index);
    }

    /**
     * An internal method to initialized a given Cell with the given values and
     * insert it into the set of Cell objects
     * @param newCell The Cell to initialize
     * @param item The Widget to put in the Cell
     * @param index The index at which to inset the Cell; if index=-1 then the
     *              Cell is appended
     */
    protected void addCell(Cell newCell, Object item, int index) {
        // ::NOTE:: I don't want to hard code the item types, but I'm going to
        //          until I can think of a better solution.
        if (Layout.class.isInstance(item))
            newCell.item = new LayoutInfo();
        else if (TextItem.class.isInstance(item))
            newCell.item = new TextItemInfo();
        else if (ItemWidget.class.isInstance(item))
            newCell.item = new ItemWidgetInfo();
        else {
            System.out.println("Cannot add a " + item.getClass().toString());
            throw new IllegalArgumentException("Cannot add a " + item.getClass().toString());
        }

        newCell.item.setItem(item);

        if (index == -1)
            m_cells.addElement(newCell);
        else
            m_cells.insertElementAt(newCell, index);

        this.invalidateSizes();
        this.recalculateSizes();
    }

    /**
     * Remove the Widget from the Layout
     * @param aIndex The index of the Widget to be removed
     * @return The removed Widget
     */
    public Object unmanage(int aIndex) {
        Cell cell = this.getCell(aIndex);
        Object item = cell.item;
        this.removeCell(cell);
        return item;
    }

    public Object unmanage(Object item) {
        int index = this.getIndexOf(item);
        return unmanage(index);
    }
    
    protected void removeCell(Cell trashCell) {
        m_cells.removeElement(trashCell);
        this.invalidateSizes();
    }
    
    /**
     * Get the Widget contained in a given Cell
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
     * Get the index of the given Widget.
     * @param item The Widget
     * @return The index of the Widget, or -1 if no such Widget is managed by this
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
    
    void setWidth(int w) {
        m_suggestedWidth = w;
    }

    void setHeight(int h) {
        m_suggestedHeight = h;
    }

    public void setSize(int w, int h) {
        setWidth(w);
        setHeight(h);
    }

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
    public void draw(Graphics g) {
        this.invalidateSizes();
        this.recalculateSizes();
    }
}
