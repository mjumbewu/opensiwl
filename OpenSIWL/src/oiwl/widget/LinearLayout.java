/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public class LinearLayout extends Layout {
    private LinearLayoutProperties m_props;
    
    public LinearLayout(int orient) {
        if (orient == Orientation.VERTICAL)
            m_props = new VerticalLayoutProperties(this);
        else if (orient == Orientation.HORIZONTAL)
            m_props = new HorizontalLayoutProperties(this);
        else
            throw new IllegalArgumentException("Invalid orientation argument: "
                    + Integer.toString(orient));
    }
    
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
    
    protected abstract class LinearLayoutProperties {
        private LinearLayout m_layout;
        public LinearLayoutProperties(LinearLayout layout) {
            m_layout = layout;
        }
        
        public LinearLayout getLayout() {
            return m_layout;
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
    }
    
    protected LinearLayoutProperties getProperties() {
        return this.m_props;
    }
    
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
            
            axial += getProperties().getAxialSize(box.item) + 2*box.padding;
            ortho = Math.max(ortho, getProperties().getOrthoSize(box.item));
        }
        
        getProperties().setStretchedAxialSize(axial);
        getProperties().setStretchedOrthoSize(ortho);
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
        
        int available_size = getProperties().getAxialSize();
        int expanding_boxes = num_items;
        
        // First, go through and find out how many cells are [not] expanding
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            if (!box.expand) {
                --expanding_boxes;
                available_size -= getProperties().getMinAxialSize(box.item) - 2*box.padding;
            }
        }
        
        int ax_offset = getProperties().getAxialPos(), 
                or_offset = getProperties().getOrthoPos(), 
                offset = 0;
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            
            // Set the box offset
            box.offset = offset;
            
            // Calculate the size of the current box
            box.ortho = getProperties().getOrthoSize();
            int min_axial_size = getProperties().getMinAxialSize(box.item) + 2*box.padding;
            if (box.expand) 
                box.axial =
                    Math.max(available_size / expanding_boxes, min_axial_size);
            else 
                box.axial = min_axial_size;
            
            // Make sure the Widget is properly sized
            if (box.fill) {
                getProperties().setAxialSize(box.item, box.axial - 2*box.padding);
                getProperties().setOrthoSize(box.item, box.ortho);
            } else {
                getProperties().setAxialSize(box.item, min_axial_size - 2*box.padding);
                getProperties().setOrthoSize(box.item, getProperties().getOrthoSize(box.item));
            }
            
            // Axially position
            int ax_align = getProperties().getAxialAlign(index);
            int ax_pos;

            if ((ax_align & Alignment.MIN) != 0)
                ax_pos = box.offset + box.padding;
            else if ((ax_align & Alignment.CENTER) != 0)
                ax_pos = (box.axial - getProperties().getAxialSize(box.item))/2 + box.offset;
            else /* MAX */
                ax_pos = box.axial - box.padding - getProperties().getAxialSize(box.item) + box.offset;

            // Orthogonally position
            int or_align = getProperties().getOrthoAlign(index);
            int or_pos;

            if ((or_align & Alignment.MIN) != 0)
                or_pos = 0;
            else if ((or_align & Alignment.CENTER) != 0)
                or_pos = (box.ortho - getProperties().getOrthoSize(box.item))/2;
            else /* MAX */
                or_pos = box.ortho - getProperties().getOrthoSize(box.item);

            getProperties().setAxialPos(box.item, ax_pos + ax_offset);
            getProperties().setOrthoPos(box.item, or_pos + or_offset);
            
            // Update the offset and available axial for the next box
            offset += box.axial;
            available_size -= box.axial;
            --expanding_boxes;
        }
    }

    class VerticalLayoutProperties extends LinearLayoutProperties {
        public VerticalLayoutProperties(LinearLayout layout)  { super(layout); }
        
        protected int getAxialPos() { return getLayout().getYPos(); }
        protected int getOrthoPos() { return getLayout().getXPos(); }

        protected int getAxialSize() { return getLayout().getHeight(); }
        protected int getOrthoSize() { return getLayout().getWidth(); }
        protected void setAxialSize(int size) { getLayout().setHeight(size); }
        protected void setOrthoSize(int size) { getLayout().setWidth(size); }

        protected int getStretchedAxialSize() { return getLayout().getStretchedHeight(); }
        protected int getStretchedOrthoSize() { return getLayout().getStretchedWidth(); }
        protected void setStretchedAxialSize(int size) { getLayout().setStretchedHeight(size); }
        protected void setStretchedOrthoSize(int size) { getLayout().setStretchedWidth(size); }

        protected int getAxialPos(Widget item) { return item.getYPos(); }
        protected int getOrthoPos(Widget item) { return item.getXPos(); }
        protected void setAxialPos(Widget item, int pos) { item.setYPos(pos); }
        protected void setOrthoPos(Widget item, int pos) { item.setXPos(pos); }

        protected int getAxialSize(Widget item) { return item.getHeight(); }
        protected int getOrthoSize(Widget item) { return item.getWidth(); }
        protected void setAxialSize(Widget item, int size) { item.setHeight(size); }
        protected void setOrthoSize(Widget item, int size) { item.setWidth(size); }

        protected int getMinAxialSize(Widget item) { return item.getMinHeight(); }
        protected int getMinOrthoSize(Widget item) { return item.getMinWidth(); }

        protected int getAxialAlign(int index) { return getLayout().getVAlignment(index) << 3; }
        protected int getOrthoAlign(int index) { return getLayout().getHAlignment(index) << 6; }
    }

    class HorizontalLayoutProperties extends LinearLayoutProperties {
        public HorizontalLayoutProperties(LinearLayout layout)  { super(layout); }
        
        protected int getOrthoPos() { return getLayout().getYPos(); }
        protected int getAxialPos() { return getLayout().getXPos(); }

        protected int getOrthoSize() { return getLayout().getHeight(); }
        protected int getAxialSize() { return getLayout().getWidth(); }
        protected void setOrthoSize(int size) { getLayout().setHeight(size); }
        protected void setAxialSize(int size) { getLayout().setWidth(size); }

        protected int getStretchedOrthoSize() { return getLayout().getStretchedHeight(); }
        protected int getStretchedAxialSize() { return getLayout().getStretchedWidth(); }
        protected void setStretchedOrthoSize(int size) { getLayout().setStretchedHeight(size); }
        protected void setStretchedAxialSize(int size) { getLayout().setStretchedWidth(size); }

        protected int getOrthoPos(Widget item) { return item.getYPos(); }
        protected int getAxialPos(Widget item) { return item.getXPos(); }
        protected void setOrthoPos(Widget item, int pos) { item.setYPos(pos); }
        protected void setAxialPos(Widget item, int pos) { item.setXPos(pos); }

        protected int getOrthoSize(Widget item) { return item.getHeight(); }
        protected int getAxialSize(Widget item) { return item.getWidth(); }
        protected void setOrthoSize(Widget item, int size) { item.setHeight(size); }
        protected void setAxialSize(Widget item, int size) { item.setWidth(size); }

        protected int getMinOrthoSize(Widget item) { return item.getMinHeight(); }
        protected int getMinAxialSize(Widget item) { return item.getMinWidth(); }

        protected int getOrthoAlign(int index) { return getLayout().getVAlignment(index) << 3; }
        protected int getAxialAlign(int index) { return getLayout().getHAlignment(index) << 6; }
    }
}
