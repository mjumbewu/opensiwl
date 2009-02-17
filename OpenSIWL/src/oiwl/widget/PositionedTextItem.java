/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Font;

/**
 *
 * @author mjumbewu
 */
public class PositionedTextItem extends TextItem {
    /**
     * The default x-coordinate of the top-left corner of a line of text
     */
    static int DEFAULT_LINE_XPOS = 0;
    
    /**
     * The default y-coordinate of the top-left corner of a line of text
     */
    static int DEFAULT_LINE_YPOS = 0;
    
    /**
     * A line of text with a definite position.
     * @author mjumbewu
     */
    protected class PositionedLine extends Line {
        int xpos = PositionedTextItem.DEFAULT_LINE_XPOS;
        int ypos = PositionedTextItem.DEFAULT_LINE_YPOS;
    }
    
    /**
     * Create a positioned line of text
     * @return A new positioned line of text
     */
    protected Line makeLine() {
        return new PositionedLine();
    }
    
    /**
     * Add the line of text to the TextItem
     * @param text The text string
     * @param font The font of the text
     * @param color The color of the text
     * @param xpos The x-coordinate of the top-left corner
     * @param ypos The y-coordinate of the top-left corner
     */
    public void addLine(String text, Font font, int color, int xpos, int ypos) {
        this.addLine((PositionedLine)this.makeLine(), text, font, color, xpos, ypos);
    }
    
    /**
     * Internal function to initialize the given line with the given values and
     * add the line to this TextItem.
     * @param newLine The Line object to initialize and add
     * @param text The text string
     * @param font The font of the text
     * @param color The color of the text
     * @param xpos The x-coordinate of the top-left corner
     * @param ypos The y-coordinate of the top-left corner
     */
    protected void addLine(PositionedLine newLine, String text, Font font, int color, int xpos, int ypos) {
        newLine.xpos = xpos;
        newLine.ypos = ypos;
        this.addLine(newLine, text, font, color);
    }
    
    /**
     * Get the x-coordinate of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @return The x-coordinate of the top-left corner
     */
    public int getX(int aIndex) {
        return ((PositionedLine)this.getLine(aIndex)).xpos;
    }
    
    /**
     * Set the x-coordinate of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @param aPos The x-coordinate of the top-left corner
     */
    public void setX(int aIndex, int aPos) {
        ((PositionedLine)this.getLine(aIndex)).xpos = aPos;
    }
    
    /**
     * Get the y-coordinate of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @return The y-coordinate of the top-left corner
     */
    public int getY(int aIndex) {
        return ((PositionedLine)this.getLine(aIndex)).ypos;
    }
    
    /**
     * Set the y-coordinate of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @param aPos The y-coordinate of the top-left corner
     */
    public void setY(int aIndex, int aPos) {
        ((PositionedLine)this.getLine(aIndex)).ypos = aPos;
    }
    
    /**
     * Set the coordinates of the top-left corner of the given line.
     * @param aIndex The line number/index
     * @param x The x-coordinate of the top-left corner
     * @param y The y-coordinate of the top-left corner
     */
    public void setCoordinates(int aIndex, int x, int y) {
        ((PositionedLine)this.getLine(aIndex)).xpos = x;
        ((PositionedLine)this.getLine(aIndex)).ypos = y;
    }
    
}
