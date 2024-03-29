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
public class AbsoluteLayout extends Layout {

    public int getChildXPos(Widget child) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(child);
        return pos.x;
    }

    public int getChildYPos(Widget child) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(child);
        return pos.y;
    }

    public int getChildWidth(Widget child) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(child);
        return pos.width;
    }

    public int getChildHeight(Widget child) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(child);
        return pos.height;
    }

    protected class WidgetPos {
        Widget item;
        int x=0, y=0;
        int width=0, height=0;
    }

    Hashtable m_positions = new Hashtable();

    public void manage(Widget item, int x, int y, int z, int w, int h) {
        WidgetPos pos = new WidgetPos();
        pos.item = item;
        pos.x = x;
        pos.y = y;
        pos.width = w;
        pos.height = h;

        // Must add the cell before the widget, because adding the widget may
        // cause a redraw, in turn causing a recalculateLayout, which needs the
        // cell size.
        this.m_positions.put(item, pos);
        this.addWidget(item, z);
    }

    /**
     * Set the x-position of the given Widget, relative to the top-left corner
     * of this Layout
     * @param item The Widget
     * @param x The x-position
     */
    public void setXPos(Widget item, int x) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(item);
        pos.x = x;
    }
    
    /**
     * Set the y-position of the given Widget, relative to the top-left corner
     * of this Layout
     * @param item The Widget
     * @param y The y-position
     */
    public void setYPos(Widget item, int y) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(item);
        pos.y = y;
    }

    public void setWidth(Widget item, int w) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(item);
        pos.width = w;
    }

    public void setHeight(Widget item, int h) {
        WidgetPos pos = (WidgetPos)this.m_positions.get(item);
        pos.height = h;
    }

    public void setZPos(Widget item, int pos) {
        this.removeWidget(item);
        this.addWidget(item, pos);
    }

    public void moveUp(Widget item) {
        if (item != this.getLastWidget()) {
            int zpos = this.getIndexOf(item);
            this.setZPos(item, zpos+1);
        }
    }

    public void moveDown(Widget item) {
        if (item != this.getFirstWidget()) {
            int zpos = this.getIndexOf(item);
            this.setZPos(item, zpos-1);
        }
    }

    /**
     * Calculate the ortho and the axial as the ortho of the widest line and
     * the sum of the heights of the lines, respectively, in this TextItem.
     */
    protected synchronized void recalculateLayout() {
        int num_items = this.getWidgetCount();
        
        int width = 0;
        int height = 0;
        
        for (int index = 0; index < num_items; ++index) {
            Widget item = (Widget)this.getWidget(index);
            
            width += item.getWidth();
            height = Math.max(height, item.getHeight());
        }
        
        this.setStretchedWidth(width);
        this.setStretchedHeight(height);
        this.validateSizes();
    }

    public void manage(Widget item) {
        this.manage(item, 0, 0);
    }

    public void manage(Widget item, int x, int y) {
        this.manage(item, x, y, -1, 0, 0);
    }

    public void unmanage(Widget item) {
        this.removeWidget(item);
    }
}
