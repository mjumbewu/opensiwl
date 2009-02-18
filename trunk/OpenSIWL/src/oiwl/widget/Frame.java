/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

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
 * @author mjumbewu
 */
public abstract class Frame extends GameCanvas {
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

    public Frame() {
        super(true);
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
    
    public static int PORTRAIT = 1;
    public static int LANDSCAPE = 2;
    
    private int m_orientation;
    private Image m_orientedBuffer;
    
    public int getOrientation() {
        return this.m_orientation;
    }
    
    public void setOrientation(int aOrientation) {
        if (aOrientation != LANDSCAPE &&
            aOrientation != PORTRAIT) {
            throw new IllegalArgumentException(
                    "Invalid orientation value: " + 
                    Integer.toString(aOrientation));
        }
        this.m_orientation = aOrientation;
        this.reinitOrientedBuffer();
        this.orientationChanged(aOrientation);
    }
    
    private void reinitOrientedBuffer() {
        m_orientedBuffer = Image.createImage(getWidth(), getHeight());
    }
    
    protected void orientationChanged(int orient) {}
    
    public int getWidth() {
        if (this.getOrientation() == PORTRAIT)
            return super.getWidth();
        
        else /* LANDSCAPE */
            return super.getHeight();
    }
    
    public int getHeight() {
        if (this.getOrientation() == PORTRAIT)
            return super.getHeight();
        
        else /* LANDSCAPE */
            return super.getWidth();
    }
    
    protected Graphics getGraphics() {
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
    
    private synchronized void invalidateRegion(int l, int t, int r, int b) {
        if (m_invalidated_l == m_invalidated_r ||
            m_invalidated_t == m_invalidated_b) {
            m_invalidated_l = l;
            m_invalidated_t = t;
            m_invalidated_r = r;
            m_invalidated_b = b;
        } else {
            m_invalidated_l = Math.min(l, m_invalidated_l);
            m_invalidated_t = Math.min(l, m_invalidated_t);
            m_invalidated_r = Math.max(l, m_invalidated_r);
            m_invalidated_b = Math.max(l, m_invalidated_b);
        }
    }
    
    public void flushGraphics(int x, int y, int w, int h) {
        invalidateRegion(x, y, x+w, y+h);
        this.repaint();
    }
    
    public void flushGraphics() {
        flushGraphics(0, 0, getWidth(), getHeight());
    }
    
    public void paint(Graphics g) {
        int l = m_invalidated_l;
        int t = m_invalidated_t;
        int r = m_invalidated_r;
        int b = m_invalidated_b;
        
        if (getOrientation() == PORTRAIT)
            g.drawRegion(m_orientedBuffer, l, t, r-l, b-t, 
                    Sprite.TRANS_NONE, l, t, Graphics.TOP|Graphics.LEFT);
        
        else /* LANDSCAPE, right-handed rotation */
            g.drawRegion(m_orientedBuffer, l, t, r-l, b-t, 
                    Sprite.TRANS_ROT90, b, l, Graphics.TOP|Graphics.LEFT);
        
        resetInvalidatedRegion();
    }
}
