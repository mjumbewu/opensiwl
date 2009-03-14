/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import java.util.Vector;
import java.util.Timer;
import java.util.Date;

/**
 * A Frame is the canvas for an OIWL-base UI.
 * <h3>Display</h3>
 * <p>The content of a frame is rendered on top of a background.  The 
 * background has a color and an image.  In the absensece of an image, the 
 * background will be flooded with the background color.  If the image has
 * transparency, the background color will show through.  If the image is 
 * present and occupies the entire canvas, then the background color could
 * be fuscia for all the user knows.</p>
 * 
 * <h3>Orientation</h3>
 * <p>When the orientation changes, we issue an orientationChange event.</p>
 * 
 * <h3>Pointer Events</h3>
 * <ul>
 * <li>pointerDown</li>
 * <li>pointerDrag</li>
 * <li>pointerFlick</li>
 * <li>pointerUp</li>
 * </ul>
 * 
 * <h3>Layout Management</h3>
 * @author mjumbewu
 */
public class Frame extends Canvas implements WidgetParent {
    /**
     * Constant for the stock light background image
     */
    public static final int BACKGROUND_LIGHT = 0;

    /**
     * Constant for the stock dark background image
     */
    public static final int BACKGROUND_DARK = 1;

    /**
     * Constant for white-filled background
     */
    public static final int BACKGROUND_NONE = 2;

    private int m_backgroundColor = 0x00ffffff;
    private Image m_backgroundImage = null;
    
    private Layout m_layout = null;
    private Vector m_panels = new Vector();

    /**
     * Default constructor for a Frame.  Initializes the orientation to 
     * Orientation.PORTRAIT
     */
    public Frame() {
        this(Orientation.PORTRAIT);
    }
    
    /**
     * Constructor that initializes the orientation of the Frame.
     * @param orient The desired Frame orientation
     */
    public Frame(int orient) {
        super();
        this.setOrientation(orient);
        this.invalidate();
    }
    
    /**
     * Set the background color for the frame.
     * @param aColor The new background color of the frame in the form
     *               0x00rrggbb.
     */
    public void setBackgroundColor(int aColor) {
        m_backgroundColor = aColor;
    }

    /**
     * Get the background color of the frame.  By default, this value is
     * 0x00ffffff (white)
     * @return The background color of the frame in the format 0x00rrggbb
     */
    public int getBackgroundColor() {
        return m_backgroundColor;
    }

    /**
     * Set the background image for the frame.
     * @param aImage The new image.  If <code>null</code>, this will clear the
     *               background image.
     * @see clearBackgroundImage()
     */
    public void setBackgroundImage(Image aImage) {
        m_backgroundImage = aImage;
    }

    /**
     * Get the background image for the frame.  By default, this value is null.
     * @return The background image of the frame.
     */
    public Image getBackgroundImage() {
        return m_backgroundImage;
    }

    /**
     * Clears the background image from the frame.  The background of the frame
     * will be flooded with the background color.
     */
    public void clearBackgroundImage() {
        m_backgroundImage = null;
    }
    
    private int m_orientation;
    private Image m_orientedBuffer;
    
    /**
     * Get the orientation of the Frame.
     * @return The orientation of the Frame
     */
    public int getOrientation() {
        return this.m_orientation;
    }
    
    /**
     * Set the orientation of the Frame.
     * @param aOrientation The desired orientation of the frame
     */
    public void setOrientation(int aOrientation) {
        if (aOrientation != Orientation.LANDSCAPE &&
            aOrientation != Orientation.PORTRAIT) {
            throw new IllegalArgumentException(
                    "Invalid orientation value: " + 
                    Integer.toString(aOrientation));
        }
        
        if (this.getOrientation() != aOrientation) {
            this.m_orientation = aOrientation;
            this.freeOrientedBuffer();
            this.reinitLayoutSize();

            // call the orientation changed callback method
            this.orientationChanged(aOrientation);
        }
    }
    
    /**
     * Free the image that acts as the offscreen buffer for this Frame.  This
     * should be done when the frame is not visible so that its buffer does not
     * take up an unnecessary amount of space in memory.
     */
    private void freeOrientedBuffer() {
        m_orientedBuffer = null;
        System.out.println("Freed Buffer Image");
    }

    /**
     * Reset the image that acts as the offscreen buffer for this Frame.  This
     * needs to be done when the Frame orientation changes.
     */
    private void reinitOrientedBuffer() {
        m_orientedBuffer = Image.createImage(getWidth(), getHeight());
        System.out.println("Created Buffer Image");
    }

    /**
     * Handler that is called when the Frame orientation changes.
     * @param orient The new Frame orientation
     */
    protected void orientationChanged(int orient) {}
    
    /**
     * Check that the Widget is appropriate to be added to a Frame.  Appropriate
     * Widget objects include Layout objects, ItemWidget objects, and
     * StaticWidget objects.
     * @param item The Widget to check
     * @return Whether the Widget is an appropriate child
     */
    public boolean isValidChild(Widget item) {
        if (Layout.class.isInstance(item) ||
                ItemWidget.class.isInstance(item) ||
                StaticWidget.class.isInstance(item))
            return true;
        else
            return false;
    }
    
    public void handleChildRedraw(Widget item, int x, int y, int w, int h) {
        this.invalidate(
                item.getGlobalXPos() + x, item.getGlobalYPos() + y, w, h);
    }

    public int getGlobalXPos() {
        return 0;
    }

    public int getGlobalYPos() {
        return 0;
    }

    /**
     * Set this Frame object's Layout
     * @param aLayout The layout
     */
    public void setLayout(Layout aLayout) {
        aLayout.setParent(this);
        this.m_layout = aLayout;
        this.reinitLayoutSize();
    }
    
    /**
     * Get this Frame object's Layout
     * @return The layout
     */
    public Layout getLayout() {
        return this.m_layout;
    }
    
    private void reinitLayoutSize() {
        if (this.getLayout() != null) {
            getLayout().setSize(this.getLayoutWidth(), this.getLayoutHeight());
        }
    }
    
    public void addPanel(Panel panel) {
        if (panel.getAttachment() == Panel.TOP) {
            panel.setLocalPos(this.getLayoutXPos(), this.getLayoutYPos());
            panel.setWidth(this.getLayoutWidth());
        }
        else if (panel.getAttachment() == Panel.LEFT) {
            panel.setLocalPos(this.getLayoutXPos(), this.getLayoutYPos());
            panel.setHeight(this.getLayoutHeight());
        }
        else if (panel.getAttachment() == Panel.RIGHT) {
            panel.setLocalPos(this.getLayoutXPos()+this.getLayoutWidth()-panel.getWidth(), this.getLayoutYPos());
            panel.setHeight(this.getLayoutHeight());
        }
        else if (panel.getAttachment() == Panel.BOTTOM) {
            panel.setLocalPos(this.getLayoutXPos(), this.getLayoutYPos()+this.getLayoutHeight()-panel.getHeight());
            panel.setWidth(this.getLayoutWidth());
        }
        panel.setParent(this);
        
//        System.out.print("layout size: ");
//        System.out.print(this.getLayoutWidth());
//        System.out.print(",");
//        System.out.println(this.getLayoutHeight());
//        
//        System.out.print("panel pos: ");
//        System.out.print(panel.getLocalXPos());
//        System.out.print(",");
//        System.out.println(panel.getLocalYPos());
        m_panels.addElement(panel);
    }
    
    public int getNumPanels() {
        return m_panels.size();
    }
    
    public Panel getPanel(int index) {
        return (Panel)m_panels.elementAt(index);
    }
    
    /**
     * Return the width of the Frame.  The width is the shorter dimension in
     * PORTRAIT mode and the longer dimension in LANDSCAPE mode.
     * @return The width of the Frame
     */
    public int getWidth() {
        if (this.getOrientation() == Orientation.PORTRAIT)
            return super.getWidth();
        
        else /* LANDSCAPE */
            return super.getHeight();
    }
    
    /**
     * Return the height of the Frame.  The height is the longer dimension in
     * PORTRAIT mode and the shorter dimension in LANDSCAPE mode.
     * @return The height of the Frame
     */
    public int getHeight() {
        if (this.getOrientation() == Orientation.PORTRAIT)
            return super.getHeight();
        
        else /* LANDSCAPE */
            return super.getWidth();
    }
    
    public int getLayoutXPos() {
        int pos = 0;
        int num_panels = this.getNumPanels();
        
        for (int i = 0; i < num_panels; ++i) {
            Panel panel = this.getPanel(i);
            if (panel.getAttachment() == Panel.LEFT)
                pos += panel.getWidth();
        }
        return pos;
    }
    
    public int getLayoutYPos() {
        int pos = 0;
        int num_panels = this.getNumPanels();
        
        for (int i = 0; i < num_panels; ++i) {
            Panel panel = this.getPanel(i);
            if (panel.getAttachment() == Panel.TOP)
                pos += panel.getHeight();
        }
        return pos;
    }
    
    public int getLayoutWidth() {
        int total = this.getWidth();
        int num_panels = this.getNumPanels();
        
        for (int i = 0; i < num_panels; ++i) {
            Panel panel = this.getPanel(i);
            if (panel.getAttachment() == Panel.RIGHT ||
                    panel.getAttachment() == Panel.LEFT)
                total -= panel.getWidth();
        }
        return total;
    }
    
    public int getLayoutHeight() {
        int total = this.getHeight();
        int num_panels = this.getNumPanels();
        
        for (int i = 0; i < num_panels; ++i) {
            Panel panel = this.getPanel(i);
            if (panel.getAttachment() == Panel.BOTTOM ||
                    panel.getAttachment() == Panel.TOP)
                total -= panel.getHeight();
        }
        return total;
    }
    
    protected Graphics getGraphics() {
        if (this.m_orientedBuffer == null) {
            this.reinitOrientedBuffer();
        }
        return this.m_orientedBuffer.getGraphics();
    }
    
    private int m_invalidated_l = 0;
    private int m_invalidated_t = 0;
    private int m_invalidated_r = 0;
    private int m_invalidated_b = 0;
    
    private void resetInvalidatedRegion() {
        m_invalidated_l = 0;
        m_invalidated_t = 0;
        m_invalidated_r = 0;
        m_invalidated_b = 0;
    }
    
    public synchronized void invalidate(int x, int y, int w, int h) {
//        System.out.println("-invalidating the region x="
//                + Integer.toString(l) + ", y="
//                + Integer.toString(t) + ", w="
//                + Integer.toString(r-l) + ", h="
//                + Integer.toString(b-t));
        // If the invalidated region is empty, then set to the parameters
        if (m_invalidated_l == m_invalidated_r ||
            m_invalidated_t == m_invalidated_b) {
            m_invalidated_l = x;
            m_invalidated_t = y;
            m_invalidated_r = x+w;
            m_invalidated_b = y+h;
        } 
        
        // If it's not empty, then set to the bounding box
        else {
            m_invalidated_l = Math.min(x, m_invalidated_l);
            m_invalidated_t = Math.min(y, m_invalidated_t);
            m_invalidated_r = Math.max(x+w, m_invalidated_r);
            m_invalidated_b = Math.max(y+h, m_invalidated_b);
        }
        
        // Request a repaint
        this.repaint(x,y,w,h);
    }
    
    public void invalidate() {
        this.invalidate(0, 0, this.getWidth(), this.getHeight());
    }
    
    private void drawBackground(Graphics g, int x, int y, int w, int h) {
        int c0 = g.getColor();
        
        // Draw the bg color
        g.setColor(this.getBackgroundColor());
        g.fillRect(x, y, w, h);
        
        // Draw the bg image (if it exists)
        Image bgimage = this.getBackgroundImage();
        if (bgimage != null)
            g.drawRegion(bgimage, x, y, w, h, Sprite.TRANS_NONE, x, y, Graphics.TOP|Graphics.LEFT);
        
        // Reset the color to what it originally was
        g.setColor(c0);
    }
    
    private boolean m_paintAllowed = true;
    
    public void suppressPaint() {
        this.m_paintAllowed = false;
    }
    
    public void allowPaint() {
        this.m_paintAllowed = true;
    }
    
    public boolean canPaint() {
        return this.m_paintAllowed;
    }
    
    public void paint(Graphics g) {
        int x = m_invalidated_l;
        int y = m_invalidated_t;
        int r = m_invalidated_r;
        int b = m_invalidated_b;
        int w = r-x;
        int h = b-y;

        if (this.canPaint()) {
//            System.out.println("painting the region x="
//                    + Integer.toString(x) + ", y="
//                    + Integer.toString(y) + ", w="
//                    + Integer.toString(w) + ", h="
//                    + Integer.toString(h));

            // Draw the layout and the panels to the oriented buffer
            Graphics buffer = this.getGraphics();

            this.drawBackground(buffer, x, y, w, h);
            Layout layout = this.getLayout();
            int layoutx = layout.getLocalXPos();
            int layouty = layout.getLocalYPos();
            layout.draw(buffer, layoutx, layouty, 
                    x - layoutx, y - layouty, w, h);
            for (int i = 0; i < this.getNumPanels(); ++i) {
                Panel panel = this.getPanel(i);
                int panelx = panel.getLocalXPos();
                int panely = panel.getLocalYPos();
                panel.draw(buffer, panelx, panely,
                        x - panelx, y - panely, w, h);
            }
            resetInvalidatedRegion();
        }

        if (this.m_orientedBuffer != null) {
            // Draw the oriented buffer to the screen
            if (getOrientation() == Orientation.PORTRAIT)
                g.drawRegion(m_orientedBuffer, x, y, w, h,
                        Sprite.TRANS_NONE, x, y, Graphics.TOP|Graphics.LEFT);

            else /* LANDSCAPE, right-handed rotation */
                g.drawRegion(m_orientedBuffer, x, y, w, h,
                        Sprite.TRANS_ROT90, x, this.getHeight()-b, Graphics.TOP|Graphics.LEFT);
        }
    }
    
    public void pointerDown (int x, int y) {}
    public void pointerDrag (int x, int y) {}
    public void pointerUp   (int x, int y) {}
    public void pointerFlick(int x, int y) {}
    
    protected void hideNotify() {
        this.freeOrientedBuffer();
    }

    protected void showNotify() {
        this.reinitOrientedBuffer();
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    // Flicking
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    // If this object has not had a moving event in longer than
    // 'stillness_duration_threshold', it is not considered to be moving (i.e.
    // it has a zero velocity).
    long stillness_duration_threshold = 100;
    
    // If this object has not been dragged more than 'drag_movement_threshold'
    // pixels/second, it is not considered to be moving (i.e. it has a zero 
    // velocity).  This is like the stillness escape velocity.
    int drag_movement_threshold = 10;
    
    /*
     * The following are variables and methods for recording the position of the
     * user's pointing device and the time of the recording.
     */
    
    // prev_pos and curr_pos both refer to the position of the pointer.
    private int prev_pointer_x = 0;
    private int prev_pointer_y = 0;
    private int curr_pointer_x = 0;
    private int curr_pointer_y = 0;
    
    // last_move_time and current_time  refer to the time at which the screen 
    // was polled for the pointer position on a press or drag.
    private Date last_drag_time;
    private Date current_time;
    
    private void resetPointerPosition(int x, int y)
    {
        prev_pointer_x = curr_pointer_x = x;
        prev_pointer_y = curr_pointer_y = y;
        
        has_updated_pointer = true;
    }
    
    private void resetDragTime()
    {
        last_drag_time = new Date(0);
        current_time = new Date();
        
        has_updated_pointer = true;
    }
    
    private void updatePointerPosition(int x, int y)
    {
        prev_pointer_x = curr_pointer_x;
        prev_pointer_y = curr_pointer_y;
        
        curr_pointer_x = x;
        curr_pointer_y = y;
        
        has_updated_pointer = true;
    }
    
    void updateDragTime()
    {
        last_drag_time = current_time;
        current_time = new Date();
        
        has_updated_pointer = true;
    }
    
    public void pointerPressed(int x, int y) {
        this.getLayout().handleEvent(Event.PRESSED, new LocationData(x,y));
        
        System.out.print("down  ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        // Reset the velocity and time.
        resetPointerPosition(x,y);
        resetDragTime();
    }
    
    public void pointerDragged(int x, int y) {
        this.getLayout().handleEvent(Event.DRAGGED, new LocationData(x,y));
        
        System.out.print("drag  ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        updatePointerPosition(x,y);
        updateDragTime();
    }
    
    public void pointerReleased(int x, int y) {
        this.getLayout().handleEvent(Event.RELEASED, new LocationData(x,y));
        
        System.out.print("up    ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        updateDragTime();
        calcVelocity();
        
        MovementData flickData = new MovementData(x, y, 
                this.current_time.getTime(), this.velocity_x, this.velocity_y);
        this.getLayout().handleEvent(Event.FLICKED, flickData);
    }

    /*
     * The following are tools to determine the current velocity of the frame,
     * and keep track of whether the frame has moved since the last calculation.
     * If the frame has not moved, then the functions will return their stored
     * values, so as not to do unnecessary floating-point calculations and 
     * function calls.float
     */
    private boolean has_updated_pointer = false;
    private float velocity_x = 0f;
    private float velocity_y = 0f;
    
    private int dragDeltaX() { return curr_pointer_x - prev_pointer_x; }
    private int dragDeltaY() { return curr_pointer_y - prev_pointer_y; }
    private long dragDeltaTime() { return current_time.getTime() - last_drag_time.getTime(); }
    
    private synchronized void calcVelocity()
    {
        // Only update the velocity if we have to (in response to a press or 
        // drag).
        if (has_updated_pointer)
        {
            // If we have been still too long, no movement.
            if (dragDeltaTime() > stillness_duration_threshold) {
                velocity_x = 0f;
                velocity_y = 0f;
            }
            else
            {
                float dx = dragDeltaX();
                float dy = dragDeltaY();
                long  dt = dragDeltaTime();

                velocity_x = (float) dx / dt * 1000;
                velocity_y = (float) dy / dt * 1000;
            }
        }
        
        // ::NOTE:: This 'has_updated_pointer' way of keeping track of state is vulnerable
        //          to race conditions.  I just hope it won't be noticed too 
        //          much.  And hopefully the 'synchronized' will solve it.
        has_updated_pointer = false;
    }
    
}
