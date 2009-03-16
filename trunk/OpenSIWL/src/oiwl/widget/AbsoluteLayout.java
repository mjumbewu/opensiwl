/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public class AbsoluteLayout extends Layout {

    /**
     * Set the x-position of the given Widget, relative to the top-left corner
     * of this Layout
     * @param item The Widget
     * @param pos The x-position
     */
    public void setXPos(Widget item, int pos) {
        item.setLocalXPos(pos);
    }
    
    /**
     * Set the y-position of the given Widget, relative to the top-left corner
     * of this Layout
     * @param item The Widget
     * @param pos The y-position
     */
    public void setYPos(Widget item, int pos) {
        item.setLocalYPos(pos);
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
        this.addWidgetSafely(item, -1);
        item.setLocalPos(x, y);
    }

    public void unmanage(Widget item) {
        this.removeWidget(item);
    }
}
