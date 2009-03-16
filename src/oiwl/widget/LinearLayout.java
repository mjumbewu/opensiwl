/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Vector;

/**
 *
 * @author mjumbewu
 */
public class LinearLayout extends Layout {
    private LinearLayoutProperties m_props;
    private Vector m_cells = new Vector();
    
    public LinearLayout(int orient) {
        if (orient == Orientation.VERTICAL)
            m_props = new VerticalLayoutProperties(this);
        else if (orient == Orientation.HORIZONTAL)
            m_props = new HorizontalLayoutProperties(this);
        else
            throw new IllegalArgumentException("Invalid orientation argument: "
                    + Integer.toString(orient));
    }

    public void manage(Widget item) {
        this.manage(item, -1, 0, true, false);
    }
    
    /**
     * A line of text with a definite position.
     * @author mjumbewu
     */
    protected class LinearCell {
        Widget item;
        int offset = 0;
        int ortho = 0;
        int axial = 0;

        int padding = 0;
        boolean expand = true;
        boolean fill = false;
        int alignment = Alignment.HCENTER | Alignment.VCENTER;
    }
    
    public void manage(Widget item, int index, int padding, boolean expand, boolean fill) {
        LinearCell box = new LinearCell();
        box.item = item;
        box.padding = padding;
        box.expand = expand;
        box.fill = fill;

        // Must add the cell before the widget, because adding the widget may
        // cause a redraw, in turn causing a recalculateLayout, which needs the
        // cell size.
        if (index == -1)
            this.m_cells.addElement(box);
        else
            this.m_cells.insertElementAt(box, index);
        this.addWidgetSafely(item, index);
    }
    
    public void unmanage(Widget item) {
        int index = this.getIndexOf(item);
        this.removeWidget(item);
        m_cells.removeElementAt(index);
    }

    public void unmanageAll() {
        this.clearWidgets();
        m_cells.removeAllElements();
    }

    protected LinearCell getCell(int index) {
        return (LinearCell)m_cells.elementAt(index);
    }

    public int getPadding(int aIndex) {
        return ((LinearCell)this.getCell(aIndex)).padding;
    }

    public void setPadding(int aIndex, int aPadding) {
       ((LinearCell)this.getCell(aIndex)).padding = aPadding;
    }

    public boolean getExpand(int aIndex) {
        return ((LinearCell)this.getCell(aIndex)).expand;
    }

    public void setExpand(int aIndex, boolean aExpand) {
       ((LinearCell)this.getCell(aIndex)).expand = aExpand;
    }

    public boolean getFill(int aIndex) {
        return ((LinearCell)this.getCell(aIndex)).fill;
    }

    public void setFill(int aIndex, boolean aFill) {
       ((LinearCell)this.getCell(aIndex)).fill = aFill;
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
    protected synchronized void recalculateLayout() {
        int num_items = this.getWidgetCount();

        int axial = 0;
        int ortho = 0;

        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);

            axial += getProperties().getAxialSize(box.item) + 2*box.padding;
            ortho = Math.max(ortho, getProperties().getOrthoSize(box.item));
        }

        // We validate the sizes before actualy setting them so that we can
        // grab the old width and old height to send in an event.  If we just
        // tried to gram the sizes, getWidth/Height would just call this 
        // method again (inf. loop).
        this.validateSizes();
        int oldw = this.getWidth();
        int oldh = this.getHeight();
        
        // Now set the stretched (minimum) sizes and reposition the contents.
        getProperties().setStretchedAxialSize(axial);
        getProperties().setStretchedOrthoSize(ortho);
        this.recalculateOffsets();
        
        // Finally, send the event.  Note, the size did not necessarily change
        // (i.e. if the requested size is greater than the stretched size).
        this.sendSizeChange(oldw, oldh);
    }

    /**
     * Calculate the relative positions of the tops of the lines in this
     * TextItem.  The offset-most line always has a offset=0.  Every offset from there
     * on is the offset of the previous line offset by the axial of the previous
     * line.
     */
    protected synchronized void recalculateOffsets() {
        int num_items = this.getWidgetCount();
        
        int layout_ax_pos = getProperties().getAxialPos();
        int layout_op_pos = getProperties().getOrthoPos();
        int layout_ax_size = getProperties().getAxialSize();
        int layout_or_size = getProperties().getOrthoSize();

        int available_size = layout_ax_size;
        int expanding_boxes = num_items;
        
        // First, go through and find out how many cells are [not] expanding
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            if (!box.expand) {
                --expanding_boxes;
                available_size -= getProperties().getMinAxialSize(box.item) - 2*box.padding;
            }
        }
        
        int offset = 0;
        
        // Calculate and set the axial and orthogonal position and size for 
        // each managed Widget object.  Every time the Layout receives a
        // MOVED or RESIZED event it invalidates its size again, so turn off
        // each Widget object's events before adjusting, or else we'll get
        // an explosion of recalculations -- really slow.
        //
        // TODO: Instead of supressing the Widget objects' events altogether,
        //       we should simply have this Layout ignore them.
        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);
            
            // Momentarily disable the Widget object's events
            box.item.getEventSender().supressEvents();
            
            // Set the box offset
            box.offset = offset;
            
            // Calculate the size of the current box
            box.ortho = layout_or_size;
            int min_axial_box_size = getProperties().getMinAxialSize(box.item) + 2*box.padding;
            if (box.expand) 
                box.axial =
                    Math.max(available_size / expanding_boxes, min_axial_box_size);
            else 
                box.axial = min_axial_box_size;
            
            // Make sure the Widget is properly sized
            if (box.fill) {
                getProperties().setAxialSize(box.item, box.axial - 2*box.padding);
                getProperties().setOrthoSize(box.item, box.ortho);
            } else {
                getProperties().setAxialSize(box.item, getProperties().getMinAxialSize(box.item));
                getProperties().setOrthoSize(box.item, getProperties().getMinOrthoSize(box.item));
//                System.out.print(box.item.getClass().toString());
//                System.out.print(",");
//                System.out.print(getProperties().getOrthoSize(box.item));
//                System.out.print(",");
//                System.out.println(getProperties().getAxialSize(box.item));
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

            getProperties().setAxialPos(box.item, ax_pos);
            getProperties().setOrthoPos(box.item, or_pos);
            
            // Update the offset and available axial for the next box
            offset += box.axial;
            if (box.expand) {
                available_size -= box.axial;
                --expanding_boxes;
            }
            
            // Reenable events for the Widget
            box.item.getEventSender().allowEvents();
        }
    }

    class VerticalLayoutProperties extends LinearLayoutProperties {
        public VerticalLayoutProperties(LinearLayout layout)  { super(layout); }
        
        protected int getAxialPos() { return getLayout().getLocalYPos(); }
        protected int getOrthoPos() { return getLayout().getLocalXPos(); }

        protected int getAxialSize() { return getLayout().getHeight(); }
        protected int getOrthoSize() { return getLayout().getWidth(); }
        protected void setAxialSize(int size) { getLayout().setHeight(size); }
        protected void setOrthoSize(int size) { getLayout().setWidth(size); }

        protected int getStretchedAxialSize() { return getLayout().getStretchedHeight(); }
        protected int getStretchedOrthoSize() { return getLayout().getStretchedWidth(); }
        protected void setStretchedAxialSize(int size) { getLayout().setStretchedHeight(size); }
        protected void setStretchedOrthoSize(int size) { getLayout().setStretchedWidth(size); }

        protected int getAxialPos(Widget item) { return item.getLocalYPos(); }
        protected int getOrthoPos(Widget item) { return item.getLocalXPos(); }
        protected void setAxialPos(Widget item, int pos) { item.setLocalYPos(pos); }
        protected void setOrthoPos(Widget item, int pos) { item.setLocalXPos(pos); }

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
        
        protected int getOrthoPos() { return getLayout().getLocalYPos(); }
        protected int getAxialPos() { return getLayout().getLocalXPos(); }

        protected int getOrthoSize() { return getLayout().getHeight(); }
        protected int getAxialSize() { return getLayout().getWidth(); }
        protected void setOrthoSize(int size) { getLayout().setHeight(size); }
        protected void setAxialSize(int size) { getLayout().setWidth(size); }

        protected int getStretchedOrthoSize() { return getLayout().getStretchedHeight(); }
        protected int getStretchedAxialSize() { return getLayout().getStretchedWidth(); }
        protected void setStretchedOrthoSize(int size) { getLayout().setStretchedHeight(size); }
        protected void setStretchedAxialSize(int size) { getLayout().setStretchedWidth(size); }

        protected int getOrthoPos(Widget item) { return item.getLocalYPos(); }
        protected int getAxialPos(Widget item) { return item.getLocalXPos(); }
        protected void setOrthoPos(Widget item, int pos) { item.setLocalYPos(pos); }
        protected void setAxialPos(Widget item, int pos) { item.setLocalXPos(pos); }

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
