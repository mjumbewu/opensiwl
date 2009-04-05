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
public class FlowLayout extends Layout {

    /**
     * A LinearLayout that contains all the rows of the FlowLayout
     */
    LinearLayout column = new LinearLayout(Orientation.VERTICAL);
    /**
     * A map from each widget to its corresponding row
     */
    Hashtable item_rows = new Hashtable();
    /**
     * The alignment
     */
    int halign = Alignment.SPREAD;

    public FlowLayout() {
        column.supressDraw();
    }

    public void manage(Widget item) {
        this.addWidget(item, -1);
    }

    public void unmanage(Widget item) {
        this.removeWidget(item);
    }

    public int getTargetWidth() {
        return this.getParent().getChildWidth(this);
    }

    protected void recalculateLayout() {
        int target_width = this.getTargetWidth();

        column.unmanageAll();
//        column.setHeight(this.getSuggestedHeight());
//        column.setWidth(this.getSuggestedWidth());

        int inserted_items = 0;
        int total_items = this.getWidgetCount();
        while (inserted_items < total_items) {
            LinearLayout row = new LinearLayout(Orientation.HORIZONTAL);
            row.supressDraw();

            // If the alignment is set to center or right, then each row needs
            // to be pushed away from the left side.
            if (halign == Alignment.HCENTER || halign == Alignment.RIGHT)
                row.manage(new Spacer(), -1, 0, true, true);

            boolean row_empty = true;
            Widget next_item = this.getWidget(inserted_items);
            do {
                // If the alignment is set to justify, then spacers go in
                // between each pair of items on the same row
                if (halign == Alignment.JUSTIFY && !row_empty)
                    row.manage(new Spacer(), -1, 0, true, true);

                // Add the item.
                row.manage(next_item, -1, 0, /*expand*/false, /*fill*/true);
                this.item_rows.put(next_item, row);

                // Remember, when the row layout began to manage the item, it
                // stole its parentage.  So we have to steal it back.
                next_item.setParent(this);

                // Go on to the next item, if it exists
                try {
                    next_item = this.getWidget(++inserted_items);
                } catch (java.lang.ArrayIndexOutOfBoundsException e) {
                    break;
                }
            } while (row.getWidth() + next_item.getWidth() <= target_width);

            // If the alignment is set to center or left, then each row needs
            // to be pushed away from the right side.
            if (halign == Alignment.HCENTER || halign == Alignment.LEFT)
                row.manage(new Spacer(), -1, 0, true, true);

            column.manage(row);
        }
//        column.recalculateLayout();
        this.setStretchedWidth(column.getMinWidth());
        this.setStretchedHeight(column.getMinHeight());
    }

    public int getChildXPos(Widget child) {
        return column.getChildXPos(child);
    }

    public int getChildYPos(Widget child) {
        return column.getChildYPos(child);
    }

    public int getChildWidth(Widget child) {
        if (child == column) {
            return this.getTargetWidth();
        } else {
            return ((LinearLayout)item_rows.get(child)).getChildWidth(child);
        }
    }

    public int getChildHeight(Widget child) {
        if (child == column) {
            return this.getParent().getChildHeight(this);
        } else {
            return ((LinearLayout)item_rows.get(child)).getChildHeight(child);
        }
    }


}
