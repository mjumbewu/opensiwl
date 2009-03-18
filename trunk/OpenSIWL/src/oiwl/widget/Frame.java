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
     * Get the value needed when transforming the buffer to the device space.
     * @return The transformation needed to get the buffer to the device
     */
    private int getTransformation() {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            return Sprite.TRANS_ROT90;
        else
            return Sprite.TRANS_NONE;
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
//        System.out.print(this.getDefaultLayoutWidth());
//        System.out.print(",");
//        System.out.println(this.getDefaultLayoutHeight());
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
        int untransformed_x = frameToDeviceX(x,y,w,h);
        int untransformed_y = frameToDeviceY(x,y,w,h);
        int untransformed_w = frameToDeviceW(x,y,w,h);
        int untransformed_h = frameToDeviceH(x,y,w,h);
        this.repaint(untransformed_x,untransformed_y,
                untransformed_w,untransformed_h);
    }
    
    public void invalidate() {
        this.invalidate(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Draw the specified portion of the Frame's background to the graphics
     * context.
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h
     */
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
            System.out.println("painting the region x="
                    + Integer.toString(x) + ", y="
                    + Integer.toString(y) + ", w="
                    + Integer.toString(w) + ", h="
                    + Integer.toString(h));

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

            buffer.drawRect(x, y, w-1, h-1);
            resetInvalidatedRegion();
        }

        if (this.m_orientedBuffer != null) {
            // Draw the oriented buffer to the screen
            int untransformed_x = this.frameToDeviceX(x, y, w, h);
            int untransformed_y = this.frameToDeviceY(x, y, w, h);
            int transformation = this.getTransformation();
            System.out.println(transformation);

            g.drawRegion(m_orientedBuffer, x, y, w, h, transformation,
                    untransformed_x, untransformed_y, Graphics.TOP|Graphics.LEFT);
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
    // Pointer Events
    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    PointerTracker pointer = new PointerTracker();

    public int deviceToFrameX(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            x = y;

        return x;
    }

    public int deviceToFrameY(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            y = this.getHeight() - x - w;

        return y;
    }

    public int deviceToFrameW(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            w = h;

        return w;
    }

    public int deviceToFrameH(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            h = w;

        return h;
    }

    /**
     * A function that derives a translated x-coordinate from coordinates
     * specified in the device reference space.
     * @param x The x-coordinate of the point in device space
     * @param y The y-coordinate of the point in device space
     * @return The x-coordinate of the point in the Frame's reference space
     */
    public int deviceToFrameX(int x, int y) {
        return this.deviceToFrameX(x, y, 0, 0);
    }

    /**
     * A function that derives a translated y-coordinate from coordinates
     * specified in the device reference space.
     * @param x The x-coordinate of the point in device space
     * @param y The y-coordinate of the point in device space
     * @return The y-coordinate of the point in the Frame's reference space
     */
    public int deviceToFrameY(int x, int y) {
        return this.deviceToFrameY(x, y, 0, 0);
    }

    public int frameToDeviceX(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            x = this.getHeight() - y - h;

        return x;
    }

    public int frameToDeviceY(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            y = x;

        return y;
    }

    public int frameToDeviceW(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            w = h;

        return w;
    }

    public int frameToDeviceH(int x, int y, int w, int h) {
        if (this.getOrientation() == Orientation.LANDSCAPE)
            h = w;

        return h;
    }

    /**
     * A function that derives a device x-coordinate from coordinates
     * specified in the Frame's reference space.
     * @param x The x-coordinate of the point in the Frame's reference space
     * @param y The y-coordinate of the point in the Frame's reference space
     * @return The x-coordinate of the point in device space
     */
    public int frameToDeviceX(int x, int y) {
        return this.frameToDeviceX(x, y, 0, 0);
    }

    /**
     * A function that derives a device y-coordinate from coordinates
     * specified in the Frame's reference space.
     * @param x The x-coordinate of the point in the Frame's reference space
     * @param y The y-coordinate of the point in the Frame's reference space
     * @return The y-coordinate of the point in device space
     */
    public int frameToDeviceY(int x, int y) {
        return this.frameToDeviceY(x, y, 0, 0);
    }

    public void pointerPressed(int untransformed_x, int untransformed_y) {

        int x = deviceToFrameX(untransformed_x, untransformed_y);
        int y = deviceToFrameY(untransformed_x, untransformed_y);
        
        System.out.print("down  ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        // Reset the velocity and time.
        pointer.resetPointerPosition(x,y);
        pointer.resetDragTime();
        
        this.getLayout().handlePointerEvent(Event.PRESSED, pointer);
    }
    
    public void pointerDragged(int untransformed_x, int untransformed_y) {
        
        int x = deviceToFrameX(untransformed_x, untransformed_y);
        int y = deviceToFrameY(untransformed_x, untransformed_y);

        System.out.print("drag  ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        pointer.updatePointerPosition(x,y);
        pointer.updateDragTime();

        this.getLayout().handlePointerEvent(Event.DRAGGED, pointer);
    }
    
    public void pointerReleased(int untransformed_x, int untransformed_y) {
        
        int x = deviceToFrameX(untransformed_x, untransformed_y);
        int y = deviceToFrameY(untransformed_x, untransformed_y);

        System.out.print("up    ");
        System.out.print(x);
        System.out.print(", ");
        System.out.println(y);
        
        pointer.updateDragTime();
        
        this.getLayout().handlePointerEvent(Event.RELEASED, pointer);
    }
}
