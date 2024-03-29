/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

/**
 * A ScrollViewWidget is a WidgetWithLayout, but its viewed widget is not
 * managed by its layout.  the layout of the ScrollViewWidget exists on top of
 * the viewed Widget.  This is useful for overlays and panels and the like.
 * @author mjumbewu
 */
public class ScrollViewWidget extends WidgetWithLayout {
    
    public ScrollViewWidget() {
        setLayout(new ViewportLayout());
    }

    public void scrollXTo(int x) {
        ViewportLayout layout = ((ViewportLayout)this.getLayout());
        layout.setViewXOffset(x);
    }

    public void scrollYTo(int y) {
        ViewportLayout layout = ((ViewportLayout)this.getLayout());
        layout.setViewYOffset(y);
    }
    
    public void scrollXBy(int dx) {
        ViewportLayout layout = ((ViewportLayout)this.getLayout());
        layout.setViewXOffset(layout.getViewXOffset() + dx);
    }

    public void scrollYBy(int dy) {
        ViewportLayout layout = ((ViewportLayout)this.getLayout());
        layout.setViewYOffset(layout.getViewYOffset() + dy);
    }

    public boolean isValidChild(Widget item) {
        return this.getParent().isValidChild(item);
    }

    MoveTask scrollTask = null;

    public static final int HOLDING_STATE = NewWidgetState();
    public static final int SCROLLING_STATE = NewWidgetState();

    public void cancelPointerEvents() {
        if (scrollTask != null) {
            scrollTask.cancel();
            scrollTask = null;
        }
        super.cancelPointerEvents();
    }
    
    public boolean handlePointerEvent(int type, PointerTracker pointer) {
        boolean already_handled = false;

        if (getPointerState() == INACTIVE_STATE) {
            if (type == Event.PRESSED) {
                // Does the layout want to handle it?
                already_handled = super.handlePointerEvent(type, pointer);

                // If not, go to the next state
                if (!already_handled) {
                    setPointerState(HOLDING_STATE);
                }

                // Do not block events (unless the layout does)
                return already_handled;
            }
        }
        
        else if (getPointerState() == HOLDING_STATE) {
            if (type == Event.DRAGGED) {

                // If we're moving then: yay, we're scrolling!
                if (pointer.isMoving()) {
                    setPointerState(SCROLLING_STATE);
                    cancelLayoutPointerEvents();
                    this.scrollXBy(pointer.dragDeltaX());
                    this.scrollYBy(pointer.dragDeltaY());
                    return true;
                }
            }
            
            // Otherwise, just check the children.
            else {
                already_handled = super.handlePointerEvent(type, pointer);
                return already_handled;
            }
        }

        else if (getPointerState() == SCROLLING_STATE) {
            if (type == Event.FLICKED && pointer.dragDeltaX() < 1 &&
                    pointer.dragDeltaY() < 1) {
                this.cancelPointerEvents();
            }
            
            if (type == Event.DRAGGED || type == Event.FLICKED) {
                this.scrollXBy(pointer.dragDeltaX());
                this.scrollYBy(pointer.dragDeltaY());
                return true;
            }

            else if (type == Event.RELEASED) {
                if (pointer.getXVel() > 0 || pointer.getYVel() > 0) {
                    scrollTask = new MoveTask(this, pointer, 0.5f);
                    Thread flick_thread = new Thread(scrollTask);
                    flick_thread.start();
                }
            }

            else if (type == Event.PRESSED) {
                cancelPointerEvents();
                
                // Does the layout want to handle it?
                already_handled = super.handlePointerEvent(type, pointer);

                // If not, go to the next state
                if (!already_handled) {
                    setPointerState(HOLDING_STATE);
                }

                // Do not block events (unless the layout does)
                return already_handled;
            }
        }

        return false;
    }
}
