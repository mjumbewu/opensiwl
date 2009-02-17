/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.lang.IllegalArgumentException;
import javax.microedition.lcdui.Font;

/**
 *
 * @author mjumbewu
 */
public class AlignedTextItem extends TextItem {
    /**
     * Constant for left horizontal alignment
     */
    public static int LEFT = 1;
    
    /**
     * Constant for center horizontal alignment
     */
    public static int HCENTER = 2;
    
    /**
     * Constant for right horizontal alignment
     */
    public static int RIGHT = 4;
    
    /**
     * Constant for top vertical alignment
     */
    public static int TOP = 8;
    
    /**
     * Constant for center vertical alignment
     */
    public static int VCENTER = 16;
    
    /**
     * Constant for bottom vertical alignment
     */
    public static int BOTTOM = 32;
    
    /**
     * The alignment of the text.
     */
    private int m_alignment = AlignedTextItem.HCENTER|AlignedTextItem.VCENTER;
    
    /**
     * A line of text with a definite position.
     * @author mjumbewu
     */
    protected class AlignedLine extends Line {
        int top = 0;
    }
    
    public int getAlignment() {
        return m_alignment;
    }
    
    public void setAlignment(int aAlignment) {
        if ((aAlignment & AlignedTextItem.LEFT) == 0 &&
            (aAlignment & AlignedTextItem.HCENTER) == 0 &&
            (aAlignment & AlignedTextItem.RIGHT) == 0) {
            throw new IllegalArgumentException("No horizontal alignment specified.");
        }
        
        if ((aAlignment & AlignedTextItem.TOP) == 0 &&
            (aAlignment & AlignedTextItem.VCENTER) == 0 &&
            (aAlignment & AlignedTextItem.BOTTOM) == 0) {
            throw new IllegalArgumentException("No vertical alignment specified.");
        }
        
        this.m_alignment = aAlignment;
    }
    
    /**
     * Create a positioned line of text
     * @return A new positioned line of text
     */
    protected Line makeLine() {
        return new AlignedLine();
    }
    
    private int m_bound_width;
    private int m_bound_height;
    
    /**
     * Get the width of the bounding box.  For example, if the TextItem is 
     * contained within a Button, then this will be the width of the Button.
     * NOTE, the Button would be responsible for setting the bounds.
     * @return The width of the bounding box
     */
    int getBoundWidth() {
        return m_bound_width;
    }
    
    /**
     * Set the width of the bounding box
     * @param w The width of the bounding box
     */
    void setBoundWidth(int w) {
        m_bound_width = w;
    }
    
    /**
     * Get the height of the bounding box.  For example, if the TextItem is 
     * contained within a Button, then this will be the height of the Button.
     * NOTE, the Button would be responsible for setting the bounds.
     * @return The height of the bounding box
     */
    int getBoundHeight() {
        return m_bound_height;
    }
    
    /**
     * Set the height of the bounding box
     * @param h The height of the bounding box
     */
    void setBoundHeight(int h) {
        m_bound_height = h;
    }
    
    /**
     * Set the size of the bounding box.  For example, if the TextItem is 
     * contained within a Button, then this may be the width and height of 
     * the Button.
     * @param w The width of the bounding box
     * @param h The height of the bounding box
     */
    void setBounds(int w, int h) {
        this.setBoundWidth(w);
        this.setBoundHeight(h);
    }
    
    /**
     * Get the x-coordinate of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @return The x-coordinate of the top-left corner
     */
    public int getX(int aIndex) {
        int w = this.getLine(aIndex).font.stringWidth(this.getText(aIndex));
        int a = this.getAlignment();
        
        if ((a & AlignedTextItem.LEFT) != 0)
            return 0;
        
        else if ((a & AlignedTextItem.HCENTER) != 0)
            return (this.m_bound_width - w)/2;
        
        else /* RIGHT */
            return this.m_bound_width - w;
    }
    
    /**
     * Get the y-coordinate of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @return The y-coordinate of the top-left corner
     */
    public int getY(int aIndex) {
        // Get the height first, because the tops will be recalculated if the 
        // sizes are invalid.
        int h = this.getHeight();
        int t = ((AlignedLine)this.getLine(aIndex)).top;
        int a = this.getAlignment();
        
        if ((a & AlignedTextItem.TOP) != 0)
            return t;
        
        else if ((a & AlignedTextItem.VCENTER) != 0)
            return (this.m_bound_height - h)/2 + t;
        
        else /* BOTTOM */
            return this.m_bound_height - h + t;
    }

    synchronized void recalculateSizes() {
        int numLines = this.getTotalLine();
        
        int h = 0;
        int w = 0;
        for (int index = 0; index < numLines; ++index) {
            Font f = this.getFont(index);
            String t = this.getText(index);
            
            h += f.getHeight();
            w = Math.max(w, f.stringWidth(t));
        }
        
        this.setSizes(w, h);
        this.recalculateTops();
        this.validateSizes();
    }
    
    synchronized void recalculateTops() {
        int numLines = this.getTotalLine();
        
        int t = 0;
        for (int index = 0; index < numLines; ++index) {
            Font f = this.getFont(index);
            AlignedLine l = (AlignedLine)this.getLine(index);
            
            l.top = t;
            t += f.getHeight();
        }
    }
}
