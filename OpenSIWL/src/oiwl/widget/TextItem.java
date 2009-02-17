/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * In this implementation of the SIWL, TextItem is an abstract class.  The 
 * PositionedTextItem class is most closely analagous to the TextItem in the
 * original SIWL.
 * 
 * @author mjumbewu
 */
public abstract class TextItem {
    /**
     * The default string for a line of text
     */
    static String DEFAULT_LINE_TEXT = "";
    
    /**
     * The default font for a line of text
     */
    static Font DEFAULT_LINE_FONT = Font.getDefaultFont();
    
    /**
     * The default color for a line of text
     */
    static int DEFAULT_LINE_COLOR = 0x00000000;
    
    /**
     * The internal representation of a line of text
     * @author mjumbewu
     */
    protected class Line {
        String text = TextItem.DEFAULT_LINE_TEXT;
        Font font = TextItem.DEFAULT_LINE_FONT;
        int color = TextItem.DEFAULT_LINE_COLOR;
    }
    
    private Vector m_lines;
    
    /**
     * The method used to create a new line of text
     * @return A new Line object
     */
    protected abstract Line makeLine();
    
    /**
     * Return the Vector of Lines in the TextItem
     * @return The Vector of Lines in the TextItem
     */
    protected Vector getLines() {
        return m_lines;
    }
    
    /**
     * Return the line indexed by aLineNumber
     * @param aLineNumber
     * @return The Line indexed by aLineNumber
     */
    protected Line getLine(int aLineNumber) {
        return (Line)m_lines.elementAt(aLineNumber);
    }
    
    /**
     * Add a line of text to the TextItem.  All unspecified parameters are set
     * to the default.
     * @param text The string of text
     * @param font The font for the line
     */
    public void addLine(String text, Font font) {
        this.addLine(text, font, DEFAULT_LINE_COLOR);
    }
    
    /**
     * Add aline of text to the TextItem
     * @param text The string of text
     * @param font The font for the line
     * @param color The color of the line
     */
    public void addLine(String text, Font font, int color) {
        this.addLine(this.makeLine(), text, font, color);
    }
    
    /**
     * An internal method to initialized a given Line with the given values and
     * insert it into the set of Lines
     * @param newLine The Line to initialize
     * @param text The string of text
     * @param font The font for the line
     * @param color The color of the line
     */
    protected void addLine(Line newLine, String text, Font font, int color) {
        newLine.text = text;
        newLine.font = font;
        newLine.color = color;
        
        m_lines.addElement(newLine);
        this.invalidateSizes();
    }
    
    /**
     * Get the string of text of a Line
     * @param aLineNumber The line number
     * @return The text on the given line
     */
    public String getText(int aLineNumber) {
        return ((Line)m_lines.elementAt(aLineNumber)).text;
    }
    
    /**
     * Get the font of a Line
     * @param aLineNumber The line number
     * @return The font on the given line
     */
    public Font getFont(int aLineNumber) {
        return ((Line)m_lines.elementAt(aLineNumber)).font;
    }
    
    /**
     * Get the color of a Line
     * @param aLineNumber The line number
     * @return The color on the given line
     */
    public int getColor(int aLineNumber) {
        return ((Line)m_lines.elementAt(aLineNumber)).color;
    }
    
    /**
     * Get the number of lines in the TextItem
     * @return The number of lines in the TextItem
     */
    public int getTotalLine() {
        return m_lines.size();
    }
    
    /**
     * Remove the line from the TextItem
     * @param aLineNumber The line number of the line to be removed
     */
    public void removeLine(int aLineNumber) {
        m_lines.removeElementAt(aLineNumber);
    }
    
    /**
     * Get the x-coordinate of the top-left corner of the given line.
     * @param aIndex The line number
     * @return The x-coordinate of the top-left corner
     */
    public abstract int getX(int aIndex);
    
    /**
     * Get the y-coordinate of the top-left corner of the given line.
     * @param aIndex The line number
     * @return The y-coordinate of the top-left corner
     */
    public abstract int getY(int aIndex);
    
    private boolean m_areSizesValid = false;
    private int m_width = 0;
    private int m_height = 0;
    
    synchronized void recalculateSizes() {
        int numLines = this.getTotalLine();
        
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        for (int index = 0; index < numLines; ++index) {
            int x = this.getX(index);
            int y = this.getY(index);
            Font f = this.getFont(index);
            String t = this.getText(index);
            
            minx = Math.min(minx, x);
            maxx = Math.max(maxx, x + f.stringWidth(t));
            miny = Math.min(miny, y);
            maxy = Math.max(maxy, y + f.getHeight());
        }
        this.setSizes(maxx-minx, maxy-miny);
        this.validateSizes();
    }
    
    protected boolean areSizesValid() {
        return this.m_areSizesValid;
    }
    
    protected void invalidateSizes() {
        this.m_areSizesValid = false;
    }
    
    protected void validateSizes() {
        this.m_areSizesValid = true;
    }
    
    int getWidth() {
        if (!m_areSizesValid) this.recalculateSizes();
        return this.m_width;
    }
    
    protected void setWidth(int aWidth) {
        this.m_width = aWidth;
    }
    
    int getHeight() {
        if (!m_areSizesValid) this.recalculateSizes();
        return this.m_height;
    }
    
    protected void setHeight(int aHeight) {
        this.m_height = aHeight;
    }
    
    protected void setSizes(int w, int h) {
        this.setWidth(w);
        this.setHeight(h);
    }
    
    /**
     * Draw all lines to the given Graphics context
     * @param g The graphics context
     * @param xoff The horizontal offset where drawing should begin
     * @param yoff The vertical offset where drawing should begin
     */
    void render(Graphics g, int xoff, int yoff) {
        int numLines = this.getTotalLine();
        
        // Store the font and color so that we can come back to it later
        Font f0 = g.getFont();
        int c0 = g.getColor();
        
        // NOTE: Should we declare t, f, c, x, and y outside of the loop, or
        //       should we trust the compiler to optimize this for us?  The
        //       same question could be asked of the bitwise or.
        for (int i = 0; i < numLines; ++i) {
            String t = this.getText(i);
            Font f = this.getFont(i);
            int c = this.getColor(i);
            
            int x = this.getX(i);
            int y = this.getY(i);
            
            g.setFont(f);
            g.setColor(c);
            g.drawString(t, x+xoff, y+yoff, Graphics.TOP|Graphics.LEFT);
        }
        
        g.setFont(f0);
        g.setColor(c0);
    }
}
