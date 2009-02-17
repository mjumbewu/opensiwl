/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Font;

/**
 * An AlignedTextItem is a TextItem that is aligned along some edges or in the
 * center of a bounding box.
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

    private static int HCOMPONENT = LEFT|HCENTER|RIGHT;
    
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

    private static int VCOMPONENT = TOP|VCENTER|BOTTOM;
    
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

    /**
     * Get the vertical and horizontal alignment of the TextItem
     * @return The vertical and horizontal alignment of the TextItem
     */
    public int getAlignment() {
        return m_alignment;
    }

    /**
     * Get the horizontal component of the TextItem's alignment
     * @return The horizontal component of the alignment
     */
    public int getHAlignment() {
        return (this.getAlignment() & AlignedTextItem.HCOMPONENT);
    }

    /**
     * Get the vertical component of the TextItem's alignment
     * @return The vertical component of the alignment
     */
    public int getVAlignment() {
        return (this.getAlignment() & AlignedTextItem.VCOMPONENT);
    }

    /**
     * Set the horizontal and vertical alignment of the TextItem
     * @param aAlignment The alignment flags
     * @throws IllegalArgumentException if either a vertical of horizontal
     *         alignment is invalid
     * @see setHAlignment
     * @see setVAlignment
     */
    public void setAlignment(int aAlignment) {
        this.setHAlignment(aAlignment & AlignedTextItem.HCOMPONENT);
        this.setVAlignment(aAlignment & AlignedTextItem.VCOMPONENT);
    }

    /**
     * Set the horizontal component of the TextItem's alignment
     * @param aAlignment The horizontal component of the alignment
     * @throws IllegalArgumentException if the alignment does not match exactly
     *         one of the horizontal alignment flags
     */
    public void setHAlignment(int aAlignment) {
        if ((aAlignment != AlignedTextItem.LEFT) &&
            (aAlignment != AlignedTextItem.HCENTER) &&
            (aAlignment != AlignedTextItem.RIGHT)) {
            throw new IllegalArgumentException(
                    "Illegal horizontal alignment specified: " +
                    Integer.toString(aAlignment));
        }
        this.m_alignment = aAlignment | this.getVAlignment();
    }

    /**
     * Set the vertical component of the TextItem's alignment
     * @param aAlignment The vertical component of the alignment
     * @throws IllegalArgumentException if the alignment does not match exactly
     *         one of the vertical alignment flags
     */
    public void setVAlignment(int aAlignment) {
        if ((aAlignment != AlignedTextItem.TOP) &&
            (aAlignment != AlignedTextItem.VCENTER) &&
            (aAlignment != AlignedTextItem.BOTTOM)) {
            throw new IllegalArgumentException(
                    "Illegal vertical alignment specified: " +
                    Integer.toString(aAlignment));
        }
        this.m_alignment = aAlignment | this.getHAlignment();
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

    /**
     * Calculate the width and the height as the width of the widest line and
     * the sum of the heights of the lines, respectively, in this TextItem.
     */
    protected synchronized void recalculateSizes() {
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

    /**
     * Calculate the relative positions of the tops of the lines in this
     * TextItem.  The top-most line always has a top=0.  Every top from there
     * on is the top of the previous line offset by the height of the previous
     * line.
     */
    protected synchronized void recalculateTops() {
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
