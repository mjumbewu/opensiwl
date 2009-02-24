/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * In this implementation of the SIWL, TextItem is an abstract class.  The 
 * PositionedTextItem class is most closely analagous to the TextItem in the
 * original SIWL.
 * 
 * @author mjumbewu
 */
public class TextItem extends DisplayItem {
    private String m_text;
    private Font m_font;
    private int m_color;

    public TextItem(String text) {
        this(text, Font.getDefaultFont());
    }
    
    public TextItem(String text, Font font) {
        this(text, font, 0x00000000);
    }
    
    public TextItem(String text, Font font, int color) {
        m_text = text;
        m_font = font;
        m_color = color;
        
        this.setSize(font.stringWidth(text), font.getHeight());
    }
    
    /**
     * Get the string of text of a Line
     * @return The text on the given line
     */
    public String getText() {
        return m_text;
    }
    
    /**
     * Get the font of a Line
     * @return The font on the given line
     */
    public Font getFont() {
        return m_font;
    }
    
    /**
     * Get the color of a Line
     * @return The color on the given line
     */
    public int getColor() {
        return m_color;
    }
    
    /**
     * Draw the specified region (given in parent coordinates, not local) of
     * the TextItem.
     * @param g The target Graphic context
     * @param x The left edge of the region
     * @param y The top of the region
     * @param width The width of the region
     * @param height The height of the region
     */
    void draw(Graphics g, int x, int y, int width, int height) {
        int xoff = this.getXPos();
        int yoff = this.getYPos();
        
        // Store the font and color so that we can come back to it later
        Font f0 = g.getFont();
        int c0 = g.getColor();
        
        String text = this.getText();
        Font font = this.getFont();
        int color = this.getColor();
            
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, xoff, yoff, Graphics.TOP|Graphics.LEFT);
        
        g.setFont(f0);
        g.setColor(c0);
    }
}
