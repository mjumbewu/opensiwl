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
public abstract class Layout extends Item implements ItemParent {
    /**
     * The internal representation of a line of text
     * @author mjumbewu
     */
    protected class Cell {
        Item item = null;
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
     * Manage an Item with the Layout.  All unspecified parameters are set
     * to the default.
     * @param text The Item
     */
    public void manage(Item item) {
        this.manage(item, -1);
    }
    
    /**
     * Manage an Item with the Layout.
     * @param text The Item to be managed.
     * @param index The desired index for the Item
     */
    public void manage(Item item, int index) {
        this.addCell(this.makeCell(), item, index);
    }
    
    /**
     * An internal method to initialized a given Cell with the given values and
     * insert it into the set of Cell objects
     * @param newCell The Cell to initialize
     * @param item The Item to put in the Cell
     * @param index The index at which to inset the Cell; if index=-1 then the
     *              Cell is appended
     */
    protected void addCell(Cell newCell, Item item, int index) {
        System.out.println("Check the validity of the child...");
        if (this.isValidChild(item)) {
            System.out.println("Set the item in the new cell");
            newCell.item = item;

            System.out.println("Put the item in the vector");
            if (index == -1)
                m_cells.addElement(newCell);
            else
                m_cells.insertElementAt(newCell, index);
            
            System.out.println("invalidate sizes");
            this.invalidateSizes();
        } else {
            throw new java.lang.IllegalArgumentException("Cannot add Item of" +
                    "type " + item.getClass().getName() + " to Layout");
        }
    }
    
    public boolean isValidChild(Item item) {
        return this.getParent().isValidChild(item);
    }
    
    /**
     * Get the Item contained in a given Cell
     * @param aIndex The index of the cell
     * @return The Item in a Cell
     */
    public Item getItem(int aIndex) {
        return ((Cell)m_cells.elementAt(aIndex)).item;
    }
    
    /**
     * Get the number of lines in the TextItem
     * @return The number of lines in the TextItem
     */
    public int getItemCount() {
        return m_cells.size();
    }
    
    /**
     * Remove the line from the TextItem
     * @param aIndex The index of the item to be removed
     * @return The removed Item
     */
    public Item unmanage(int aIndex) {
        Item item = this.getItem(aIndex);
        m_cells.removeElementAt(aIndex);
        return item;
    }
    
    /**
     * Get the index of the given Item.
     * @param item The Item
     * @return The index of the Item, or -1 if no such Item is managed by this
     *         Layout.
     */
    public int getIndexOf(Item item) {
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
     * getHeight.  Only classes derived from TextItem should ever need to
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
     * Set a flag to notify the TextItem that the ortho and axial need to be
     * recalculated.
     */
    protected void invalidateSizes() {
        this.m_areSizesValid = false;
    }

    /**
     * Set a flag to notify the TextItem that the ortho and axial DO NOT need
     * to be recalculated.
     */
    protected void validateSizes() {
        this.m_areSizesValid = true;
    }

    /**
     * Get the minimum ortho of the Layout
     * @return The ortho of the Layout
     */
    public int getSpecifiedWidth() {
        return super.getWidth();
    }

    /**
     * Set the ortho minimum of the Layout
     * @param aWidth The ortho of the Layout
     */
    protected void setSpecifiedWidth(int aWidth) {
        super.setWidth(aWidth);
    }

    /**
     * Get the minimum axial of the TextItem
     * @return The axial of the TextItem
     */
    public int getSpecifiedHeight() {
        return super.getHeight();
    }

    /**
     * Set the minimum axial of the Layout
     * @param aHeight The axial of the TextItem
     */
    protected void setSpecifiedHeight(int aHeight) {
        super.setHeight(aHeight);
    }

    /**
     * Set the minimum ortho and axial of the Layout
     * @param w The ortho of the TextItem
     * @param h The axial of the TextItem
     */
    protected void setSpeciefiedSize(int w, int h) {
        this.setSpecifiedWidth(w);
        this.setSpecifiedHeight(h);
    }
    
    /**
     * Get the ortho of the bounding box of the Layout object's contents.  The
     * stretched size is the minimum size needed to encompas a Layout object's
     * complete contents.
     * @return The ortho of the bounding box
     */
    public int getStretchedWidth() {
        if (!m_areSizesValid) this.recalculateSizes();
        return m_stretched_width;
    }
    
    /**
     * Set the ortho of the bounding box
     * @param w The ortho of the bounding box
     */
    protected void setStretchedWidth(int w) {
        m_stretched_width = w;
    }
    
    /**
     * Get the axial of the bounding box of the Layout object's contents.  The
     * stretched size is the minimum size needed to encompas a Layout object's
     * complete contents.
     * @return The axial of the bounding box
     */
    public int getStretchedHeight() {
        if (!m_areSizesValid) this.recalculateSizes();
        return m_stretched_height;
    }
    
    /**
     * Set the axial of the bounding box
     * @param h The axial of the bounding box
     */
    protected void setStretchedHeight(int h) {
        m_stretched_height = h;
    }
    
    /**
     * Set the size of the bounding box of the Layout object's contents.  The
     * stretched size is the minimum size needed to encompas a Layout object's
     * complete contents.
     * @param w The ortho of the bounding box
     * @param h The axial of the bounding box
     */
    protected void setStretchedSize(int w, int h) {
        this.setStretchedWidth(w);
        this.setStretchedHeight(h);
    }
    
    /**
     * Get the effective ortho of the Layout object.  This will be the greater
     * of the stretched ortho (the contents' bounding box) and the minimum 
     * ortho (as likely set by this object's container).
     * @return The effective ortho
     */
    public int getWidth() {
        return Math.max(this.getSpecifiedWidth(), this.getStretchedWidth());
    }
    
    /**
     * Get the effective axial of the Layout object.  This will be the greater
     * of the stretched axial (the contents' bounding box) and the minimum 
     * axial (as likely set by this object's container).
     * @return The effective axial
     */
    public int getHeight() {
        return Math.max(this.getSpecifiedHeight(), this.getStretchedHeight());
    }
    
    public int getMinWidth() {
        return this.getStretchedWidth();
    }
    
    public int getMinHeight() {
        return this.getStretchedHeight();
    }
    
    /**
     * Draw the specified region (given in parent coordinates, not local) of
     * the TextItem.
     * @param g The target Graphic context
     * @param x The left edge of the region
     * @param y The top of the region
     * @param ortho The ortho of the region
     * @param axial The axial of the region
     */
    void draw(Graphics g, int x, int y, int width, int height) {
        // ::TODO:: Optimize this drawing method.  For example, only draw cells
        //          that intersect the region; consider drawing to a stored 
        //          image so that all the draw calls don't have to be made
        //          each time, but only when you need a different portion of 
        //          the image.
        
        int num_cells = this.getItemCount();
        
        // NOTE: Should we declare t, f, c, x, and y outside of the loop, or
        //       should we trust the compiler to optimize this for us?  The
        //       same question could be asked of the bitwise or.
        for (int i = 0; i < num_cells; ++i) {
            Item item = this.getItem(i);
            item.draw(g, x, y, width, height);
        }
    }
}
