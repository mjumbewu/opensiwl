/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

/**
 * The stuff widgets store should be minimal.  The interface only needs to
 * provide methods for getting position/size, drawing, event sending/listening,
 * and getting/setting parentage.  The Widget class should not explicitly store
 * width, height, x-pos, or y-pos.  X- and y-position are layout-determined, so
 * should belong to layout.  Width and height gets calculated differently for
 * different Widget objects.  Some may store the values, some calculate on the
 * fly.
 * 
 * Some Widget objects store state/progress.  Different events move them from
 * one state to the next.  For example, a PushButton click is implemented in
 * three states: INACTIVE, HOLDING, and ACCEPT.
 * <ul>
 * <li>From the START state, a PRESSED event in the sending Widget will move the
 * click Event object to the HOLDING state</li>
 * <li>From the HOLDING state, a DRAGGED event outside of the sending Widget
 * will move the click Event back to the START state.</li>
 * <li>From the HOLDING state, a RELEASED event inside of the sending Widget
 * will move the click Event to the ACCEPT state.</li>
 * </ul>
 * Double click would be implemented in much the same way.
 *
 * @author mjumbewu
 */
public abstract class Widget {
    private WidgetParent m_parent;
    private EventSender m_eventSender = new EventSender();
    
    /**
     * The constant for the MOVED_EVENT event type.  The auxiliary data for this
     * type of event will be a Point object representing the previous position
     * of the moved Item.
     */
    public static final int MOVED_EVENT = Event.NewEventType();

    /**
     * The constant for the RESIZED_EVENT event type.  The auxiliary data for this
     * type of event will be a Size object representing the previous size
     * of the resized Item.
     */
    public static final int RESIZED_EVENT = Event.NewEventType();

    protected EventSender getEventSender() {
        return m_eventSender;
    }
    
    /**
     * Set the WidgetParent for this Widget.  The application should have no
     * need to call this method; only the Widget or its new WidgetParent need.
     * @param ip The new WidgetParent of this Widget
     */
    void setParent(WidgetParent ip) {
        this.m_parent = ip;
    }

    /**
     * Get the WidgetParent of this Widget.
     * @return This Widget object's WidgetParent
     */
    public WidgetParent getParent() {
        return this.m_parent;
    }

    /**
     * Add an object to the set of this Widget object's listeners.
     * @param evl The EventListener that receives events for this Widget.
     */
    public void addEventListener(EventListener evl) {
        m_eventSender.addListener(evl);
    }

    public void removeEventListener(EventListener evl) {
        m_eventSender.removeListener(evl);
    }

    /**
     * Get the height of this Widget.
     * @return The height of this widget
     */
    public int getHeight() {
        return Math.min(Math.max(this.getParent().getChildHeight(this), this.getMinHeight()), this.getMaxHeight());
    }

    /**
     * Get the minimum height that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual height.  For some (e.g. Layout)
     * it may be smaller.
     * @return The minimum height that this Widget desires to be
     */
    abstract public int getMinHeight();

    /**
     * Get the maximum height that this Widget desires to be.
     * @return The maximum height that this Widget desires to be
     */
    public int getMaxHeight() {
        return Integer.MAX_VALUE;
    }

    /**
     * Get the width of this Widget.
     * @return The width of this widget
     */
    public int getWidth() {
        return Math.min(Math.max(this.getParent().getChildWidth(this), this.getMinWidth()), this.getMaxWidth());
    }
    
    /**
     * Get the minimum width that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual width.  For some (e.g. Layout)
     * it may be smaller.
     */
    abstract public int getMinWidth();

    /**
     * Get the maximum width that this Widget desires to be.
     * @return The maximum width that this Widget desires to be
     */
    public int getMaxWidth() {
        return Integer.MAX_VALUE;
    }

    /**
     * Send a resized event if the size has actually changed
     * @param oldw The previous width
     * @param oldh The previous height
     */
    protected void sendSizeChange(int oldw, int oldh) {
        if (oldw != this.getWidth() || oldh != this.getHeight())
            this.m_eventSender.sendEvent(RESIZED_EVENT, this, new SizeData(oldw,oldh));
    }

    public int getXPos() {
        return getParent().getXPos() + getParent().getChildXPos(this);
    }

    public int getYPos() {
        return getParent().getYPos() + getParent().getChildYPos(this);
    }

    /**
     * Send a moved event if the size has actually changed
     * @param oldx The previous x-position
     * @param oldy The previous y-position
     */
    protected void sendPosChange(int oldx, int oldy) {
        if (oldx != this.getXPos() || oldy != this.getYPos())
            this.m_eventSender.sendEvent(MOVED_EVENT, this, new LocationData(oldx,oldy));
    }
    

    /**
     * Determine whether the point specified by the coordinates are within the
     * bounding area of the Widget
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return True if the point is within the bounds of the widget, false
     *         otherwise
     */
    public boolean contains(int x, int y) {
        x -= this.getXPos();
        y -= this.getYPos();
        return this.containsLocal(x, y);
    }
    
    public boolean containsLocal(int x, int y) {
        return ((x >= 0) &&
                (y >= 0) &&
                (x < this.getWidth()) &&
                (y < this.getHeight()));
    }

    public boolean isBelowLocal(int y) {
        return y < 0;
    }

    public boolean isAboveLocal(int y) {
        return y >= this.getHeight();
    }

    public boolean isRightOfLocal(int x) {
        return x < 0;
    }

    public boolean isLeftOfLocal(int x) {
        return x >= this.getWidth();
    }
    
    public boolean intersectsLocal(int l, int t, int w, int h) {
        int r = l + w;
        int b = t + h;
        
        return ((r > 0) &&
                (b > 0) &&
                (l < this.getWidth()) &&
                (t < this.getHeight()));
    }
    
    private boolean m_canDraw = true;

    /**
     * Disallow the passing along of redraw requests.
     */
    public void supressDraw() {
        this.m_canDraw = false;
    }

    /**
     * Allow the passing along of redraw requests.
     */
    public void allowDraw() {
        this.m_canDraw = true;
    }

    /**
     * Check whether redraw requests get passed along.
     * @return True if redraw requests are passed on.
     */
    public boolean canDraw() {
        return this.m_canDraw;
    }
    
    /**
     * Request a redraw of this Widget.  The WidgetParent is responsible for
     * figuring out what to do with the request.
     */
    public void redraw() {
        if (this.canDraw())
            this.getParent().handleChildRedraw(this, 
                    0, 0, this.getWidth(), this.getHeight());
    }
    
    /**
     * Update the specified section of the widget.  The given bounds are given
     * in local coordinates
     * @param g The Graphics context to draw to.
     * @param xoff The global (i.e. device coordinated) x offset where the
     *             Widget will be drawn
     * @param yoff The global y offset where the Widget will be drawn
     * @param x The local x-coordinate of the top-left corner
     * @param y The local y-coordinate of the top-left corner
     * @param width The width of the section
     * @param height The height of the section
     */
    abstract void draw(Graphics g, int xoff, int yoff,
            int x, int y, int width, int height);
    
    /**
     * This is equivalent to calling render with <code>x=0</code>,
     * <code>y=0</code>, <code>width=getWidth()</code>, and
     * <code>height=getHeight()</code>.
     * @param g The Graphics context to draw to.
     * @param xoff The global (i.e. device coordinated) x offset where the
     *             Widget will be drawn
     * @param yoff The global y offset where the Widget will be drawn
     */
    void draw(Graphics g, int xoff, int yoff) {
        this.draw(g, xoff, yoff, 0, 0, this.getWidth(), this.getHeight());
    }
    
    /**
     * Handle the given event.
     * @param type The type of event
     * @param data The data relevant for the event
     * @return True if the Widget handles the event (and should block other
     *         Widget objects from handling it as well).
     */
    public abstract boolean handlePointerEvent(int type, PointerTracker pointer);

    private static int st_id = 0;
    protected static int NewWidgetState() {
        return st_id++;
    }

    static int INACTIVE_STATE = Widget.NewWidgetState();

    private int m_state = INACTIVE_STATE;
    
    protected void resetPointerState() {
        setPointerState(INACTIVE_STATE);
    }

    void cancelPointerEvents() {
        this.resetPointerState();
    }

    public int getPointerState() {
        return m_state;
    }

    protected void setPointerState(int state) {
        m_state = state;
    }

    /**
     * Get the activity state for this Widget.  The Widget is active if and
     * only if it is in some orig_pointer state other than INACTIVE_STATE.
     * @return
     */
    public boolean isActive() {
        return (m_state != INACTIVE_STATE);
    }
}
