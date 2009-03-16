/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

/**
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
 * @author mjumbewu
 */
public abstract class Widget {
    private int m_height;
    private int m_width;
    private int m_xpos;
    private int m_ypos;

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
     * Get the minimum width of the Layout
     * @return The width of the Layout
     */
    public int getSuggestedWidth() {
        return this.m_width;
    }

    /**
     * Set the width minimum of the Layout
     * @param aWidth The width of the Layout
     */
    protected void setSuggestedWidth(int width) {
        if (m_width != width) {
            int w = this.getWidth();
            int h = this.getHeight();

            this.m_width = width;
            this.sendSizeChange(w, h);
        }
    }

    /**
     * Get the minimum height of the Layout
     * @return The height of the Layout
     */
    public int getSuggestedHeight() {
        return this.m_height;
    }

    /**
     * Set the minimum height of the Layout
     * @param aHeight The height of the Layout
     */
    protected void setSuggestedHeight(int height) {
        if (m_height != height) {
            int w = this.getWidth();
            int h = this.getHeight();

            this.m_height = height;
            this.sendSizeChange(w, h);
        }
    }

    /**
     * Set the minimum width and height of the Layout
     * @param w The width of the Layout
     * @param h The height of the Layout
     */
    protected void setSuggestedSize(int w, int h) {
        this.setSuggestedWidth(w);
        this.setSuggestedHeight(h);
    }
    
    /**
     * Get the height of this Widget.
     * @return The height of this widget
     */
    public int getHeight() {
        return Math.max(this.getMinHeight(), this.getSuggestedHeight());
    }

    /**
     * Get the minimum height that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual height.  For some (e.g. Layout)
     * it may be smaller.
     * @return The minimum height that this Widget desires to be
     */
    int getMinHeight() {
        return this.m_height;
    }

    /**
     * Suggest a height for this Widget.
     * @param height The height suggestion
     */
    void setHeight(int height) {
        this.setSuggestedHeight(height);
    }

    /**
     * Get the width of this Widget.
     * @return The width of this widget
     */
    public int getWidth() {
        return Math.max(this.getMinWidth(), this.getSuggestedWidth());
    }
    
    /**
     * Get the minimum width that this Widget desires to be.  For most Widget
     * objects, this corresponds to its actual width.  For some (e.g. Layout)
     * it may be smaller.
     */
    int getMinWidth() {
        return this.m_width;
    }

    /**
     * Suggest a width for this Widget.
     * @param width The width suggestion
     */
    void setWidth(int width) {
        this.setSuggestedWidth(width);
    }
    
    /**
     * Set the width and height of the Widget.  In some cases, this is just a
     * suggestion for the width and the height (e.g. in the case of a Layout,
     * when this sets the minimum width and height).
     * @param w The suggested width
     * @param h The suggested height
     */
    void setSize(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
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

    /**
     * Set the x-coordinate of this Widget object's top-left corner
     * @param aPos The  x-coordinate of this Widget object's top-left corner
     */
    void setLocalXPos(int aPos) {
        if (m_xpos != aPos) {
            int x = this.getLocalXPos();
            int y = this.getLocalYPos();

            m_xpos = aPos;
            this.sendPosChange(x, y);
        }
    }

    /**
     * Get the x-coordinate of this Widget object's top-left corner
     * @return The x-coordinate of this Widget object's top-left corner
     */
    public int getLocalXPos() {
        return m_xpos;
    }

    public int getGlobalXPos() {
        return getLocalXPos() + getParent().getGlobalXPos();
    }

    /**
     * Set the y-coordinate of this Widget object's top-left corner
     * @param aPos The y-coordinate of this Widget object's top-left corner
     */
    void setLocalYPos(int aPos) {
        if (m_ypos != aPos) {
            int x = this.getLocalXPos();
            int y = this.getLocalYPos();

            m_ypos = aPos;
            this.sendPosChange(x, y);
        }
    }
    
    /**
     * Get the y-coordinate of this widget's top-left corner
     * @return The y-coordinate of this widget's top-left corner
     */
    public int getLocalYPos() {
        return m_ypos;
    }

    public int getGlobalYPos() {
        return getLocalYPos() + getParent().getGlobalYPos();
    }

    /**
     * Set the x- and y-coordinates of this Widget object's top-left corner
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    void setLocalPos(int x, int y) {
        this.setLocalXPos(x);
        this.setLocalYPos(y);
    }

    /**
     * Offset the x- and y-coordinates of this Widget object's top-left corner
     * @param dx The x displacement
     * @param dy The y displacement
     */
    void offsetBy(int dx, int dy) {
        this.setLocalPos(this.getLocalXPos() + dx, this.getLocalYPos() + dy);
    }

    /**
     * Send a moved event if the size has actually changed
     * @param oldx The previous x-position
     * @param oldy The previous y-position
     */
    protected void sendPosChange(int oldx, int oldy) {
        if (oldx != this.getLocalXPos() || oldy != this.getLocalYPos())
            this.m_eventSender.sendEvent(MOVED_EVENT, this, new LocationData(oldx,oldy));
    }

    /**
     * Get the x-value of the left-most edge of the Widget
     * @return The left-most edge
     */
    public int getLeft() {
        return this.getGlobalXPos();
    }
    
    /**
     * Get the y-value of the top-most edge of the Widget
     * @return The top-most edge
     */
    public int getTop() {
        return this.getGlobalYPos();
    }
    
    /** 
     * Get the x-value of the right-most edge of the Widget
     * @return The right-most edge
     */
    public int getRight() {
        return this.getLeft() + this.getWidth();
    }
    
    /**
     * Get the y-value of the bottom-most edge of the Widget
     * @return The bottom-most edge
     */
    public int getBottom() {
        return this.getTop() + this.getHeight();
    }
    
    /**
     * Determine whether the point specified by the coordinates are within the
     * bounding area of the Widget
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return True if the point is within the bounds of the widget, false
     *         otherwise
     */
    public boolean containsGlobal(int x, int y) {
        x -= this.getGlobalXPos();
        y -= this.getGlobalYPos();
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
    
    /**
     * Determine whether the rectangular region specified by the bounds 
     * intersectsGlobal with this Widget
     * @param l The left edge of the region
     * @param t The top of the region
     * @param w The width of the region
     * @param h The height of the region
     * @return True if the region intersect this Widget.  False otherwise.
     */
    public boolean intersectsGlobal(int l, int t, int w, int h) {
        int r = l + w;
        int b = t + h;
        
        return ((l < this.getRight()) &&
                (r > this.getLeft()) &&
                (t < this.getBottom()) &&
                (b > this.getTop()));
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
