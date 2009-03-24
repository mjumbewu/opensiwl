/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 * 
 * @author mjumbewu
 */
public class FlowLayout extends Layout {

    LinearLayout column = new LinearLayout(Orientation.VERTICAL);
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

    protected void recalculateLayout() {
        int target_width = this.getSuggestedWidth();

        column.unmanageAll();
        column.setSuggestedHeight(this.getSuggestedHeight());
        column.setSuggestedWidth(this.getSuggestedWidth());

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
        column.recalculateLayout();
    }

}
