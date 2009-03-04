/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import java.util.Vector;

/**
 * In this implementation of the SIWL, StaticText is an abstract class.  The 
 * PositionedTextItem class is most closely analagous to the StaticText in the
 * original SIWL.
 * 
 * @author mjumbewu
 */
public class StaticText extends StaticWidget {
    private String m_text;
    private Font m_font;
    private int m_color;

    // TODO: implement line wrapping (StaticText ignores newlines).
    private boolean m_wrap = false;
    private Vector m_lines = null;

    public StaticText(String text) {
        this(text, Font.getDefaultFont());
    }
    
    public StaticText(String text, Font font) {
        this(text, font, 0x00000000);
    }
    
    public StaticText(String text, Font font, int color) {
        m_text = text;
        m_font = font;
        m_color = color;
        
        this.setSize(font.stringWidth(text), font.getHeight());
    }
    
    /**
     * Get the string of text
     * @return The text
     */
    public String getText() {
        return m_text;
    }
    
    /**
     * Get the font
     * @return The font
     */
    public Font getFont() {
        return m_font;
    }
    
    /**
     * Get the color
     * @return The color
     */
    public int getColor() {
        return m_color;
    }
    
    /**
     * Draw the specified region (given in parent coordinates, not local) of
     * the StaticText.
     * @param g The target Graphic context
     * @param x The left edge of the region
     * @param y The top of the region
     * @param width The width of the region
     * @param height The height of the region
     */
    void draw(Graphics g, int xoff, int yoff, int x, int y, int width, int height) {
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
