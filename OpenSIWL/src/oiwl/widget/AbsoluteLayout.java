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
     * A line of text with a definite position.
     * @author mjumbewu
     */
    protected class AbsoluteCell extends Cell {
    }
    
    /**
     * Create a positioned line of text
     * @return A new positioned line of text
     */
    protected Cell makeCell() {
        AbsoluteCell cell = new AbsoluteCell();
        return cell;
    }

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
    
    /**
     * Calculate the ortho and the axial as the ortho of the widest line and
     * the sum of the heights of the lines, respectively, in this TextItem.
     */
    protected synchronized void recalculateSizes() {
        int num_items = this.getWidgetCount();
        
        int width = 0;
        int height = 0;
        
        for (int index = 0; index < num_items; ++index) {
            AbsoluteCell box = (AbsoluteCell)this.getCell(index);
            
            width += box.item.getWidth();
            height = Math.max(height, box.item.getHeight());
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