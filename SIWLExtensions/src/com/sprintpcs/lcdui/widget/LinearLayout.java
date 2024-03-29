/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;


/**
 * A Layout for arranging items in a single row or column
 * @author mjumbewu
 */
public class LinearLayout extends Layout {
    private LinearLayoutProperties m_props;

    /**
     * Create a LinearLayout of with the specified orientation
     * @param orient The orientation (VERTICAL or HORIZONTAL) of the layout
     */
    public LinearLayout(int orient) {
        if (orient == Orientation.VERTICAL)
            m_props = new VerticalLayoutProperties(this);
        else if (orient == Orientation.HORIZONTAL)
            m_props = new HorizontalLayoutProperties(this);
        else
            throw new IllegalArgumentException("Invalid orientation argument: "
                    + Integer.toString(orient));
    }

    public LinearLayout(int orient, boolean dfill, boolean dexpand, int dalign) {
        this(orient);
        m_defaultFill = dfill;
        m_defaultExpand = dexpand;
        m_defaultAlignment = dalign;
    }

    private boolean m_defaultFill = false;
    private boolean m_defaultExpand = true;
    private int m_defaultAlignment = Alignment.HCENTER | Alignment.VCENTER;

    public void setDefaultFill(boolean dfill) {
        m_defaultFill = dfill;
    }

    public void setDefaultExpand(boolean dexpand) {
        m_defaultExpand = dexpand;
    }

    public void setDefaultAlignment(int dalign) {
        m_defaultAlignment = dalign;
    }
    
    /**
     * 
     * @author mjumbewu
     */
    protected class LinearCell extends Cell {
        int offset = 0;
        int ortho = 0;
        int axial = 0;

        int padding = 0;
        boolean expand = m_defaultExpand;
        boolean fill = m_defaultFill;
        int alignment = m_defaultAlignment;
    }

    /**
     * Insert the given item with the given box properties
     * @param item The item to manage
     * @param index The position where the item should be inserted;
     *              an index of -1 will append the item
     * @param padding The padding around the item
     * @param expand Whether the item's box/cell should expand to fill the
     *               available space.
     * @param fill Whether the item should expand to fill the available space
     *             in its box/cell; only used for items whose size is variable
     *             (like Layout objects).
     */
    public void manage(Object item, int index, int padding, boolean expand, boolean fill) {
        LinearCell box = new LinearCell();
        box.padding = padding;
        box.expand = expand;
        box.fill = fill;
        this.addCell(box, item, index);
    }

    protected Cell makeCell() {
        LinearCell cell = new LinearCell();
        return cell;
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

    public int getPadding(Object item) {
        return this.getPadding(this.getIndexOf(item));
    }

    public void setPadding(Object item, int aPadding) {
       this.setPadding(this.getIndexOf(item), aPadding);
    }

    public boolean getExpand(Object item) {
        return this.getExpand(this.getIndexOf(item));
    }

    public void setExpand(Object item, boolean aExpand) {
       this.setExpand(this.getIndexOf(item), aExpand);
    }

    public boolean getFill(Object item) {
        return this.getFill(this.getIndexOf(item));
    }

    public void setFill(Object item, boolean aFill) {
       this.setFill(this.getIndexOf(item), aFill);
    }

    public int getAlignment(Object item) {
        return this.getAlignment(this.getIndexOf(item));
    }

    public int getHAlignment(Object item) {
        return this.getHAlignment(this.getIndexOf(item));
    }

    public int getVAlignment(Object item) {
        return this.getVAlignment(this.getIndexOf(item));
    }

    public void setAlignment(Object item, int aAlignment) {
        this.setAlignment(this.getIndexOf(item), aAlignment);
    }

    public void setHAlignment(Object item, int aAlignment) {
        this.setHAlignment(this.getIndexOf(item), aAlignment);
    }

    public void setVAlignment(Object item, int aAlignment) {
        this.setVAlignment(this.getIndexOf(item), aAlignment);
    }

    /**
     * General class for getting certain orientation-dependent information
     * notwithstanding actual orientation.
     */
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

        abstract protected int getAxialPos(ItemInfo item);
        abstract protected int getOrthoPos(ItemInfo item);
        abstract protected void setAxialPos(ItemInfo item, int pos);
        abstract protected void setOrthoPos(ItemInfo item, int pos);

        abstract protected int getAxialSize(ItemInfo item);
        abstract protected int getOrthoSize(ItemInfo item);
        abstract protected void setAxialSize(ItemInfo item, int size);
        abstract protected void setOrthoSize(ItemInfo item, int size);

        abstract protected int getMinAxialSize(ItemInfo item);
        abstract protected int getMinOrthoSize(ItemInfo item);

        abstract protected int getAxialAlign(int index);
        abstract protected int getOrthoAlign(int index);
    }

    protected LinearLayoutProperties getProperties() {
        return this.m_props;
    }

    /**
     * Calculate the orthogonal and the axial size.  A layouts axial direction
     * is the direction that corresponds to its orientation; e.g. for a VERTICAL
     * Layout, the axial size is the height, and the orthogonal size is the 
     * width.  The orthogonal size is the size of the "widest" item, and the
     * axial size is the sum of the "heights" of the items.
     */
    public synchronized void recalculateSizes() {
        int num_items = this.getItemCount();

        int axial = 0;
        int ortho = 0;

        for (int index = 0; index < num_items; ++index) {
            LinearCell box = (LinearCell)this.getCell(index);

            axial += getProperties().getAxialSize(box.item) + 2*box.padding;
            ortho = Math.max(ortho, getProperties().getOrthoSize(box.item) + 2*box.padding);
        }

        // We validate the sizes before actualy setting them so that we can
        // grab the old width and old height to send in an event.  If we just
        // tried to gram the sizes, getWidth/Height would just call this
        // method again (inf. loop).
        this.validateSizes();

        // Now set the stretched (minimum) sizes and reposition the contents.
        getProperties().setStretchedAxialSize(axial);
        getProperties().setStretchedOrthoSize(ortho);
        this.recalculateOffsets();

        System.out.print("Layout Position: ");
        System.out.print(this.getXPos());
        System.out.print(", ");
        System.out.println(this.getYPos());
        System.out.print("Layout Size: ");
        System.out.print(this.getWidth());
        System.out.print(", ");
        System.out.println(this.getHeight());
    }

    /**
     * Calculate the relative positions of the items in this Layout.  The first
     * item always has an offset=0.  Every offset from there on is the position
     * of the previous item offset by the axial size of the previous item.
     */
    protected synchronized void recalculateOffsets() {
        int num_items = this.getItemCount();

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
                available_size -= getProperties().getMinAxialSize(box.item) + 2*box.padding;
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
                getProperties().setOrthoSize(box.item, box.ortho - 2*box.padding);
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
                or_pos = box.padding;
            else if ((or_align & Alignment.CENTER) != 0)
                or_pos = (box.ortho - getProperties().getOrthoSize(box.item))/2;
            else /* MAX */
                or_pos = box.ortho - box.padding - getProperties().getOrthoSize(box.item);

            getProperties().setAxialPos(box.item, ax_pos + getProperties().getAxialPos());
            getProperties().setOrthoPos(box.item, or_pos + getProperties().getOrthoPos());

            // Update the offset and available axial for the next box
            offset += box.axial;
            if (box.expand) {
                available_size -= box.axial;
                --expanding_boxes;
            }
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

        protected int getAxialPos(ItemInfo item) { return item.getYPos(); }
        protected int getOrthoPos(ItemInfo item) { return item.getXPos(); }
        protected void setAxialPos(ItemInfo item, int pos) { item.setYPos(pos); }
        protected void setOrthoPos(ItemInfo item, int pos) { item.setXPos(pos); }

        protected int getAxialSize(ItemInfo item) { return item.getHeight(); }
        protected int getOrthoSize(ItemInfo item) { return item.getWidth(); }
        protected void setAxialSize(ItemInfo item, int size) { item.setHeight(size);}
        protected void setOrthoSize(ItemInfo item, int size) { item.setWidth(size); }

        protected int getMinAxialSize(ItemInfo item) { return item.getMinHeight(); }
        protected int getMinOrthoSize(ItemInfo item) { return item.getMinWidth(); }

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

        protected int getOrthoPos(ItemInfo item) { return item.getYPos(); }
        protected int getAxialPos(ItemInfo item) { return item.getXPos(); }
        protected void setOrthoPos(ItemInfo item, int pos) { item.setYPos(pos); }
        protected void setAxialPos(ItemInfo item, int pos) { item.setXPos(pos); }

        protected int getOrthoSize(ItemInfo item) { return item.getHeight(); }
        protected int getAxialSize(ItemInfo item) { return item.getWidth(); }
        protected void setOrthoSize(ItemInfo item, int size) { item.setHeight(size);}
        protected void setAxialSize(ItemInfo item, int size) { item.setWidth(size); }

        protected int getMinOrthoSize(ItemInfo item) { return item.getMinHeight(); }
        protected int getMinAxialSize(ItemInfo item) { return item.getMinWidth(); }

        protected int getOrthoAlign(int index) { return getLayout().getVAlignment(index) << 3; }
        protected int getAxialAlign(int index) { return getLayout().getHAlignment(index) << 6; }
    }
}
