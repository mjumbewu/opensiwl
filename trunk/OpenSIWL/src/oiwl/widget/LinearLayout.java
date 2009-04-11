/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Hashtable;

/**
 *
 * @author mjumbewu
 */
public class LinearLayout extends Layout {
    private LinearLayoutProperties m_props;
    private Hashtable m_cells = new Hashtable();
    private int m_orientation;
    
    public LinearLayout(int orient) {
        m_orientation = orient;
        if (orient == Orientation.VERTICAL)
            m_props = new VerticalLayoutProperties();
        else if (orient == Orientation.HORIZONTAL)
            m_props = new HorizontalLayoutProperties();
        else
            throw new IllegalArgumentException("Invalid orientation argument: "
                    + Integer.toString(orient));
    }

    public void manage(Widget item) {
        this.manage(item, -1, true);
    }
    
    /**
     * A line of text with a definite position.
     * @author mjumbewu
     */
    protected class LinearCell {
        Widget item;
        int offset = 0;
        int ortho_size = 0;
        int axial_size = 0;

        boolean expand = true;
        int alignment = Alignment.HCENTER | Alignment.VCENTER;
    }
    
    public void manage(Widget item, int index, boolean expand) {
        LinearCell box = new LinearCell();
        box.item = item;
        box.expand = expand;

        // Must add the cell before the widget, because adding the widget may
        // cause a redraw, in turn causing a recalculateLayout, which needs the
        // cell size.
        m_cells.put(item, box);
        this.addWidget(item, index);
    }
    
    public void unmanage(Widget item) {
        int index = this.getIndexOf(item);
        this.removeWidget(item);
        m_cells.remove(item);
    }

    public void unmanageAll() {
        this.clearWidgets();
        m_cells.clear();
    }

    protected LinearCell getCell(Widget item) {
        return (LinearCell)m_cells.get(item);
    }

    public boolean getExpand(Widget item) {
        return this.getCell(item).expand;
    }

    public void setExpand(Widget item, boolean aExpand) {
        this.getCell(item).expand = aExpand;
    }

    public int getAlignment(Widget item) {
        return this.getCell(item).alignment;
    }

    /**
     * Get the horizontal component of the TextItem's alignment
     * @return The horizontal component of the alignment
     */
    public int getHAlignment(Widget item) {
        return (this.getAlignment(item) & Alignment.HCOMPONENT);
    }

    /**
     * Get the vertical component of the TextItem's alignment
     * @return The vertical component of the alignment
     */
    public int getVAlignment(Widget item) {
        return (this.getAlignment(item) & Alignment.VCOMPONENT);
    }

    protected abstract class LinearLayoutProperties {
        abstract protected int getAxialSize();
        abstract protected int getOrthoSize();

        abstract protected int getStretchedAxialSize();
        abstract protected int getStretchedOrthoSize();
        abstract protected void setStretchedAxialSize(int size);
        abstract protected void setStretchedOrthoSize(int size);

        abstract protected int getAxialPos(Widget item);
        abstract protected int getOrthoPos(Widget item);

        abstract protected int getAxialSize(Widget item);
        abstract protected int getOrthoSize(Widget item);

        abstract protected int getMinAxialSize(Widget item);
        abstract protected int getMinOrthoSize(Widget item);

        abstract protected int getAxialAlign(Widget item);
        abstract protected int getOrthoAlign(Widget item);
    }
    
    protected LinearLayoutProperties getProperties() {
        return this.m_props;
    }
    
    /**
     * Calculate the orthogonal and the axial size.  For example, in a vertical
     * oriented layout, the orthogonal size is the width of the widest item and
     * axial size is the sum of the heights of the items.
     */
    protected synchronized void recalculateLayout() {
        int num_items = this.getWidgetCount();

        int axial = 0;
        int ortho = 0;

        for (int index = 0; index < num_items; ++index) {
            Widget item = this.getWidget(index);
            LinearCell box = this.getCell(item);

            axial += getProperties().getMinAxialSize(box.item);
            ortho = Math.max(ortho, getProperties().getMinOrthoSize(box.item));
        }

        // We validate the sizes before actualy setting them so that we can
        // grab the old width and old height to send in an event.  If we just
        // tried to gram the sizes, getWidth/Height would just call this 
        // method again (inf. loop).
        this.validateSizes();
//        int oldw = this.getWidth();
//        int oldh = this.getHeight();
//
//        // Now set the stretched (minimum) sizes and reposition the contents.
//        getProperties().setStretchedAxialSize(axial);
//        getProperties().setStretchedOrthoSize(ortho);
        this.recalculateOffsets();
        
        // Finally, send the event.  Note, the size did not necessarily change
        // (i.e. if the requested size is greater than the stretched size).
//        this.sendSizeChange(oldw, oldh);
    }

    /**
     * Calculate the relative positions of the tops of the lines in this
     * TextItem.  The offset-most line always has a offset=0.  Every offset from there
     * on is the offset of the previous line offset by the axial_size of the previous
     * line.
     */
    protected synchronized void recalculateOffsets() {
        int num_items = this.getWidgetCount();
        
        int layout_ax_size = getProperties().getAxialSize();
        int layout_or_size = getProperties().getOrthoSize();

        int available_size = layout_ax_size;
        int expanding_boxes = num_items;
        
        // First, go through and find out how many cells are [not] expanding
        for (int index = 0; index < num_items; ++index) {
            Widget item = this.getWidget(index);
            LinearCell box = this.getCell(item);
            if (!box.expand) {
                --expanding_boxes;
                available_size -= getProperties().getMinAxialSize(box.item);
            }
        }
        
        int offset = 0;
        
        // Calculate and set the axial_size and orthogonal position and size for
        // each managed Widget object.  Every time the Layout receives a
        // MOVED or RESIZED event it invalidates its size again, so turn off
        // each Widget object's events before adjusting, or else we'll get
        // an explosion of recalculations -- really slow.
        //
        // TODO: Instead of supressing the Widget objects' events altogether,
        //       we should simply have this Layout ignore them.
        for (int index = 0; index < num_items; ++index) {
            Widget item = this.getWidget(index);
            LinearCell box = this.getCell(item);
            
            // Momentarily disable the Widget object's events
            box.item.getEventSender().supressEvents();
            
            // Set the box offset
            box.offset = offset;
            
            // Calculate the size of the current box
            box.ortho_size = layout_or_size;
            int min_axial_box_size = getProperties().getMinAxialSize(box.item);
            if (box.expand) 
                box.axial_size =
                    Math.max(available_size / expanding_boxes, min_axial_box_size);
            else 
                box.axial_size = min_axial_box_size;
            
            // Update the offset and available axial_size for the next box
            offset += box.axial_size;
            if (box.expand) {
                available_size -= box.axial_size;
                --expanding_boxes;
            }
            
            // Reenable events for the Widget
            box.item.getEventSender().allowEvents();
        }
    }

    public int getChildXPos(Widget child) {
        LinearCell box = this.getCell(child);
        int box_pos, box_size, child_pos, align;

        if (this.m_orientation == Orientation.HORIZONTAL) {
            box_pos = box.offset;
            box_size = box.axial_size;
            align = this.getProperties().getAxialAlign(child);
        } else {
            box_pos = 0;
            box_size = box.ortho_size;
            align = this.getProperties().getOrthoAlign(child);
        }

        if ((align & Alignment.MIN) != 0)
            child_pos = 0;
        else if ((align & Alignment.CENTER) != 0)
            child_pos = (box_size - child.getWidth())/2;
        else /* MAX */
            child_pos = box_size - child.getWidth();

        return box_pos + child_pos;
    }

    public int getChildYPos(Widget child) {
        LinearCell box = this.getCell(child);
        int box_pos, box_size, child_pos, align;

        if (this.m_orientation == Orientation.VERTICAL) {
            box_pos = box.offset;
            box_size = box.axial_size;
            align = this.getProperties().getAxialAlign(child);
        } else {
            box_pos = 0;
            box_size = box.ortho_size;
            align = this.getProperties().getOrthoAlign(child);
        }

        if ((align & Alignment.MIN) != 0)
            child_pos = 0;
        else if ((align & Alignment.CENTER) != 0)
            child_pos = (box_size - child.getHeight())/2;
        else /* MAX */
            child_pos = box_size - child.getHeight();

        return box_pos + child_pos;
    }

    public int getChildWidth(Widget child) {
        LinearCell box = this.getCell(child);
        int box_size, child_size;

        if (this.m_orientation == Orientation.HORIZONTAL) {
            box_size = box.axial_size;
        } else {
            box_size = this.getWidth();
        }

        child_size = box_size;
        return child_size;
    }

    public int getChildHeight(Widget child) {
        LinearCell box = this.getCell(child);
        int box_size, child_size;

        if (this.m_orientation == Orientation.VERTICAL) {
            box_size = box.axial_size;
        } else {
            box_size = this.getHeight();
        }

        child_size = box_size;
        return child_size;
    }

    class VerticalLayoutProperties extends LinearLayoutProperties {
        protected int getAxialSize() { return getHeight(); }
        protected int getOrthoSize() { return getWidth(); }

        protected int getStretchedAxialSize() { return getMinHeight(); }
        protected int getStretchedOrthoSize() { return getMinWidth(); }
        protected void setStretchedAxialSize(int size) { setStretchedHeight(size); }
        protected void setStretchedOrthoSize(int size) { setStretchedWidth(size); }

        protected int getAxialPos(Widget item) { return getChildYPos(item); }
        protected int getOrthoPos(Widget item) { return getChildXPos(item); }

        protected int getAxialSize(Widget item) { return item.getHeight(); }
        protected int getOrthoSize(Widget item) { return item.getWidth(); }

        protected int getMinAxialSize(Widget item) { return item.getMinHeight(); }
        protected int getMinOrthoSize(Widget item) { return item.getMinWidth(); }

        protected int getAxialAlign(Widget item) { return getVAlignment(item) << 3; }
        protected int getOrthoAlign(Widget item) { return getHAlignment(item) << 6; }
    }

    class HorizontalLayoutProperties extends LinearLayoutProperties {
        protected int getOrthoSize() { return getHeight(); }
        protected int getAxialSize() { return getWidth(); }

        protected int getStretchedOrthoSize() { return getMinHeight(); }
        protected int getStretchedAxialSize() { return getMinWidth(); }
        protected void setStretchedOrthoSize(int size) { setStretchedHeight(size); }
        protected void setStretchedAxialSize(int size) { setStretchedWidth(size); }

        protected int getOrthoPos(Widget item) { return getChildYPos(item); }
        protected int getAxialPos(Widget item) { return getChildXPos(item); }

        protected int getOrthoSize(Widget item) { return item.getHeight(); }
        protected int getAxialSize(Widget item) { return item.getWidth(); }

        protected int getMinOrthoSize(Widget item) { return item.getMinHeight(); }
        protected int getMinAxialSize(Widget item) { return item.getMinWidth(); }

        protected int getOrthoAlign(Widget item) { return getVAlignment(item) << 3; }
        protected int getAxialAlign(Widget item) { return getHAlignment(item) << 6; }
    }
}
