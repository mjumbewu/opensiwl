/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 *
 * @author mjumbewu
 */
public class ViewportLayout extends Layout {

    class PanelBox {
        Panel panel;
        int size;
        int base_offset;
        int min_side_offset;
        int max_side_offset;
    }
    
    class ViewBox {
        Widget viewed=null;
        int xoffset=0;
        int yoffset=0;
        int x, y;
        int width;
        int height;
    }

    ViewBox m_viewBox = new ViewBox();
    Hashtable m_panelBoxes = new Hashtable();

//    public boolean isValidChild(Widget child) {
//        if (Panel.class.isInstance(child))
//            return true;
//        else
//            return false;
//    }
//
    public void manage(Widget item) {
        if (Panel.class.isInstance(item))
            this.manage((Panel)item, 50);
        else
            this.view(item);
    }
    
    public void manage(Panel item, int size) {
        if (this.isValidChild(item)) {
            PanelBox box = new PanelBox();
            box.panel = item;
            box.size = size;
            this.m_panelBoxes.put(item, box);
        }
        this.addWidget(item, -1);
    }

    public void unmanage(Widget item) {
        this.removeWidget(item);
        this.m_panelBoxes.remove(item);
    }

    synchronized protected void recalculateLayout() {
        this.supressDraw();

        // Make sure the viewed Widget is on the bottom
        Widget viewed = this.getViewedWidget();
        if (viewed != null) {
            int view_index = this.getIndexOf(viewed);
            if (view_index != 0) {
                this.removeWidget(viewed);
                this.addWidget(viewed, 0);
            }
        }

        // Position the panels
        int top_offset = 0;
        int left_offset = 0;
        int right_offset = 0;
        int bottom_offset = 0;

        Enumeration boxes = this.m_panelBoxes.elements();
        while (boxes.hasMoreElements()) {
            PanelBox box = (PanelBox)boxes.nextElement();
            switch (box.panel.getAttachment()) {
                case Panel.TOP:
                    box.base_offset = top_offset;
                    box.min_side_offset = left_offset;
                    box.max_side_offset = right_offset;

                    top_offset += box.size;
                    break;
                case Panel.LEFT:
                    box.base_offset = left_offset;
                    box.min_side_offset = top_offset;
                    box.max_side_offset = bottom_offset;

                    left_offset += box.size;
                    break;
                case Panel.RIGHT:
                    box.base_offset = right_offset;
                    box.min_side_offset = top_offset;
                    box.max_side_offset = bottom_offset;

                    right_offset += box.size;
                    break;
                case Panel.BOTTOM:
                    box.base_offset = bottom_offset;
                    box.min_side_offset = left_offset;
                    box.max_side_offset = right_offset;

                    bottom_offset += box.size;
                    break;
            }
        }

        this.m_viewBox.x = left_offset;
        this.m_viewBox.y = top_offset;
        this.m_viewBox.width = this.getWidth() - right_offset - left_offset;
        this.m_viewBox.height = this.getHeight() - bottom_offset - top_offset;
        
        // Mark the sizes as valid, so we don't do this too often.
        this.validateSizes();
        this.allowDraw();
    }

    public int getChildXPos(Widget child) {
        if (!this.areSizesValid()) this.recalculateLayout();

        if (Panel.class.isInstance(child)) {
            PanelBox panelBox = (PanelBox)this.m_panelBoxes.get(child);
            Panel panel = (Panel)child;
            switch (panel.getAttachment()) {
                case Panel.LEFT:
                    return panelBox.base_offset;
                case Panel.TOP:
                case Panel.BOTTOM:
                    return panelBox.min_side_offset;
                case Panel.RIGHT:
                    return this.getWidth() - panelBox.base_offset - panelBox.size;
                default:
                    throw new java.lang.IllegalStateException("Unsupported panel attachment");
            }
        }

        else if (child == this.getViewedWidget()) {
            return this.m_viewBox.x - this.m_viewBox.xoffset;
        }

        throw new IllegalArgumentException("Unrecognized viewport child");
    }

    public int getChildYPos(Widget child) {
        if (Panel.class.isInstance(child)) {
            PanelBox panelBox = (PanelBox)this.m_panelBoxes.get(child);
            Panel panel = (Panel)child;
            switch (panel.getAttachment()) {
                case Panel.LEFT:
                case Panel.RIGHT:
                    return panelBox.min_side_offset;
                case Panel.TOP:
                    return panelBox.base_offset;
                case Panel.BOTTOM:
                    return this.getHeight() - panelBox.base_offset - panelBox.size;
                default:
                    throw new java.lang.IllegalStateException("Unsupported panel attachment");
            }
        }

        else if (child == this.getViewedWidget()) {
            return this.m_viewBox.y - this.m_viewBox.yoffset;
        }

        throw new IllegalArgumentException("Unrecognized viewport child");
    }

    public int getChildWidth(Widget child) {
        if (Panel.class.isInstance(child)) {
            PanelBox panelBox = (PanelBox)this.m_panelBoxes.get(child);
            Panel panel = (Panel)child;
            switch (panel.getAttachment()) {
                case Panel.LEFT:
                case Panel.RIGHT:
                    return panelBox.size;
                case Panel.TOP:
                case Panel.BOTTOM:
                    return panelBox.max_side_offset - panelBox.min_side_offset;
                default:
                    throw new java.lang.IllegalStateException("Unsupported panel attachment");
            }
        }

        else if (child == this.getViewedWidget()) {
            return Math.max(this.m_viewBox.width, this.m_viewBox.viewed.getMinWidth());
        }

        throw new IllegalArgumentException("Unrecognized viewport child");
    }

    public int getChildHeight(Widget child) {
        if (Panel.class.isInstance(child)) {
            PanelBox panelBox = (PanelBox)this.m_panelBoxes.get(child);
            Panel panel = (Panel)child;
            switch (panel.getAttachment()) {
                case Panel.LEFT:
                case Panel.RIGHT:
                    return panelBox.max_side_offset - panelBox.min_side_offset;
                case Panel.TOP:
                case Panel.BOTTOM:
                    return panelBox.size;
                default:
                    throw new java.lang.IllegalStateException("Unsupported panel attachment");
            }
        }

        else if (child == this.getViewedWidget()) {
            return Math.max(this.m_viewBox.height, this.m_viewBox.viewed.getMinHeight());
        }

        throw new IllegalArgumentException("Unrecognized viewport child");
    }

//    void setParent(WidgetParent parent) {
//        if (Frame.class.isInstance(parent)) {
//            super.setParent(parent);
//        } else {
//            throw new IllegalArgumentException("FrameLayout must have a frame as a parent.");
//        }
//    }
//
    public int getMinWidth() {
        return 0;
    }

    public int getMinHeight() {
        return 0;
    }
//
//    public int getMaxWidth() {
//        return ((Frame)this.getParent()).getWidth();
//    }
//
//    public int getMaxHeight() {
//        return ((Frame)this.getParent()).getHeight();
//    }
//
    public void view(Widget widget) {
        this.m_viewBox.viewed = widget;
        this.addWidget(widget, 0);
    }

    public Widget getViewedWidget() {
        return this.m_viewBox.viewed;
    }

    public int getViewXOffset() {
        return this.m_viewBox.xoffset;
    }

    public int getViewYOffset() {
        return this.m_viewBox.yoffset;
    }

    public void setViewXOffset(int x) {
        this.m_viewBox.xoffset = x;
    }

    public void setViewYOffset(int y) {
        this.m_viewBox.yoffset = y;
    }
}
