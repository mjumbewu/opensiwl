/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public abstract class LinearLayout extends Layout {
    /**
     * A line of text with a definite position.
     * @author mjumbewu
     */
    protected class LinearCell extends Cell {
        int offset = 0;
        int padding = 0;
        boolean expand = true;
        boolean fill = false;
        int ortho = 0;
        int axial = 0;

        int alignment = Alignment.HCENTER | Alignment.VCENTER;
    }
    
    /**
     * Create a positioned line of text
     * @return A new positioned line of text
     */
    protected Cell makeCell() {
        LinearCell cell = new LinearCell();
        return cell;
    }

    public int getAlignment(int aIndex) {
        return ((LinearCell)this.getCell(aIndex)).alignment;
    }

    /**
     * Get the horizontal component of the TextItem's alignment
     * @return The horizontal component of the alignment
     */
    public int getHAlignment(int aIndex) {
        return (this.getAlignment(aIndex) & Alignment.HCOMPONENT);
    }

    /**
     * Get the vertical component of the TextItem's alignment
     * @return The vertical component of the alignment
     */
    public int getVAlignment(int aIndex) {
        return (this.getAlignment(aIndex) & Alignment.VCOMPONENT);
    }

    /**
     * Set the horizontal and vertical alignment of the TextItem
     * @param aAlignment The alignment flags
     * @throws IllegalArgumentException if either a vertical of horizontal
     *         alignment is invalid
     * @see setHAlignment
     * @see setVAlignment
     */
    public void setAlignment(int aIndex, int aAlignment) {
        this.setHAlignment(aIndex, aAlignment & Alignment.HCOMPONENT);
        this.setVAlignment(aIndex, aAlignment & Alignment.VCOMPONENT);
    }

    /**
     * Set the horizontal component of the TextItem's alignment
     * @param aAlignment The horizontal component of the alignment
     * @throws IllegalArgumentException if the alignment does not match exactly
     *         one of the horizontal alignment flags
     */
    public void setHAlignment(int aIndex, int aAlignment) {
        if ((aAlignment != Alignment.LEFT) &&
            (aAlignment != Alignment.HCENTER) &&
            (aAlignment != Alignment.RIGHT)) {
            throw new IllegalArgumentException(
                    "Illegal horizontal alignment specified: " +
                    Integer.toString(aAlignment));
        }
        ((LinearCell)this.getCell(aIndex)).alignment = 
                aAlignment | this.getVAlignment(aIndex);
    }

    /**
     * Set the vertical component of the TextItem's alignment
     * @param aAlignment The vertical component of the alignment
     * @throws IllegalArgumentException if the alignment does not match exactly
     *         one of the vertical alignment flags
     */
    public void setVAlignment(int aIndex, int aAlignment) {
        if ((aAlignment != Alignment.TOP) &&
            (aAlignment != Alignment.VCENTER) &&
            (aAlignment != Alignment.BOTTOM)) {
            throw new IllegalArgumentException(
                    "Illegal vertical alignment specified: " +
                    Integer.toString(aAlignment));
        }
        ((LinearCell)this.getCell(aIndex)).alignment = 
                aAlignment | this.getHAlignment(aIndex);
    }
    
    abstract protected int getAxialPos();
    abstract protected int getOrthoPos();
    
    abstract protected int getAxialSize();
    abstract protected int getOrthoSize();
    abstract protected void setAxialSize(int size);
    abstract protected void setOrthoSize(int size);
    
    abstract protected int getStretchedAxialSize();
    abstract protected int getStretchedOrthoSize();
    abstract protected void setStretchedAxialSize(int size);
    abstract protected void setStretchedOrthoSize(int size);
    
    abstract protected int getAxialPos(Widget item);
    abstract protected int getOrthoPos(Widget item);
    abstract protected void setAxialPos(Widget item, int pos);
    abstract protected void setOrthoPos(Widget item, int pos);
    
    abstract protected int getAxialSize(Widget item);
    abstract protected int getOrthoSize(Widget item);
    abstract protected void setAxialSize(Widget item, int size);
    abstract protected void setOrthoSize(Widget item, int size);
    
    abstract protected int getMinAxialSize(Widget item);
    abstract protected int getMinOrthoSize(Widget item);
    
    abstract protected int getAxialAlign(int index);
    abstract protected int getOrthoAlign(int index);
    
    /**
     * Calculate the ortho and the axial as the ortho of the widest line and
     * the sum of the heights of the lines, respectively, in this TextItem.
     */
    protected synchronized void recalculateSizes() {
        int num_items = this.getWidgetCount();
        
        int axial = 0;
        int ortho = 0;
        
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            
            axial += getAxialSize(box.item) + 2*box.padding;
            ortho = Math.max(ortho, getOrthoSize(box.item));
        }
        
        this.setStretchedAxialSize(axial);
        this.setStretchedOrthoSize(ortho);
        this.validateSizes();
        this.recalculateOffsets();
    }

    /**
     * Calculate the relative positions of the tops of the lines in this
     * TextItem.  The offset-most line always has a offset=0.  Every offset from there
     * on is the offset of the previous line offset by the axial of the previous
     * line.
     */
    protected synchronized void recalculateOffsets() {
        int num_items = this.getWidgetCount();
        
        int available_size = this.getAxialSize();
        int expanding_boxes = num_items;
        
        // First, go through and find out how many cells are [not] expanding
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            if (!box.expand) {
                --expanding_boxes;
                available_size -= getMinAxialSize(box.item) - 2*box.padding;
            }
        }
        
        int ax_offset = this.getAxialPos(), 
                or_offset = this.getOrthoPos(), 
                offset = 0;
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            
            // Set the box offset
            box.offset = offset;
            
            // Calculate the size of the current box
            box.ortho = this.getOrthoSize();
            int min_axial_size = getMinAxialSize(box.item) + 2*box.padding;
            if (box.expand) 
                box.axial =
                    Math.max(available_size / expanding_boxes, min_axial_size);
            else 
                box.axial = min_axial_size;
            
            // Make sure the Widget is properly sized
            if (box.fill) {
                setAxialSize(box.item, box.axial - 2*box.padding);
                setOrthoSize(box.item, box.ortho);
            } else {
                setAxialSize(box.item, min_axial_size - 2*box.padding);
                setOrthoSize(box.item, getOrthoSize(box.item));
            }
            
            // Axially position
            int ax_align = this.getAxialAlign(index);
            int ax_pos;

            if ((ax_align & Alignment.MIN) != 0)
                ax_pos = box.offset + box.padding;
            else if ((ax_align & Alignment.CENTER) != 0)
                ax_pos = (box.axial - getAxialSize(box.item))/2 + box.offset;
            else /* MAX */
                ax_pos = box.axial - box.padding - getAxialSize(box.item) + box.offset;

            // Orthogonally position
            int or_align = this.getOrthoAlign(index);
            int or_pos;

            if ((or_align & Alignment.MIN) != 0)
                or_pos = 0;
            else if ((or_align & Alignment.CENTER) != 0)
                or_pos = (box.ortho - getOrthoSize(box.item))/2;
            else /* MAX */
                or_pos = box.ortho - getOrthoSize(box.item);

            setAxialPos(box.item, ax_pos + ax_offset);
            setOrthoPos(box.item, or_pos + or_offset);
            
            // Update the offset and available axial for the next box
            offset += box.axial;
            available_size -= box.axial;
            --expanding_boxes;
        }
    }
}
