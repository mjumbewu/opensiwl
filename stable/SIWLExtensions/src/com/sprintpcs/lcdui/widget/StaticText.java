/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Font;
import java.util.Vector;
import java.util.Enumeration;


/**
 * A class used to "look like" a TextItem (so that it can be used in layouts),
 * but that refers to a preexisting font and substring, so as to save space;
 * don't want to duplicate the storage of elements.
 * @author mjumbewu
 */
class SubstringTextItem extends TextItem {
    StaticText m_parent;
    int m_begin, m_end;
    public SubstringTextItem(StaticText parent, int begin, int end) {
        super(null, null);
    }

    public int getColor(int lineNumber) {
        return m_parent.getColor();
    }

    public Font getFont(int lineNumber) {
        return m_parent.getFont();
    }

    public String getText(int lineNumber) {
        return m_parent.getText().substring(m_begin, m_end);
    }

    public int getTotalLine() {
        return 1;
    }
}

/**
 *
 * @author mjumbewu
 */
public class StaticText extends ItemWidget implements DynamicallySizedWidget {
    
    private String m_text = null;
    private Font m_font = null;
    private int m_color = 0x00000000;
    
    private boolean m_wrap = false;

    private Layout m_layout = new LinearLayout(Orientation.VERTICAL, false, false, Alignment.HCENTER|Alignment.VCENTER);
    private Vector m_textItems = new Vector();
    
    public StaticText() {
        super();
    }
    
    public StaticText(String text, Font font, int color, boolean wrap, int x, int y) {
        super();
        
        this.setColor(color);
        this.setWrap(wrap);
        this.setFont(font);
        this.setText(text);
        this.setPosition(x, y);
    }

    public String getText() {
        return m_text;
    }

    public Font getFont() {
        return m_font;
    }

    public int getColor() {
        return m_color;
    }

    public boolean getWrap() {
        return m_wrap;
    }

    public void setText(String text) {
        m_text = text;
        updateText();
    }
    
    public void setFont(Font font) {
        m_font = font;
        updateText();
    }
    
    public void setColor(int color) {
        m_color = color;
        updateText();
    }
    
    public void setWrap(boolean wrap) {
        m_wrap = wrap;
        updateText();
    }

    public void setAlignment(int alignment) {
        int num_lines = m_layout.getItemCount();
        for (int i = 0; i < num_lines; ++i)
            ((LinearLayout)m_layout).setHAlignment(i, alignment);
        ((LinearLayout)m_layout).setDefaultAlignment(alignment|Alignment.VCENTER);
    }

    public int getNumLines() {
        return m_textItems.size();
    }
    
    protected void updateText() {
        if (getText() != null && getFont() != null) {
            m_layout.unmanageAll();
            m_textItems.removeAllElements();

            if (!getWrap()) {
                TextItem ti = new TextItem(getText(), getFont(), getColor(), 0, 0);
                m_textItems.addElement(ti);
                m_layout.manage(ti);
            } else {
                Vector lines = breakIntoLines(getText(), getFont(), getWidth());
                for (int i = 0; i < lines.size(); ++i) {
                    String line = (String)lines.elementAt(i);
                    TextItem ti = new TextItem(line, getFont(), getColor());

                    m_textItems.addElement(ti);
                    m_layout.manage(ti);
                }
            }
        }
    }

    // breaks a string into a Vector of Strings based on Font size and availble width
    // note if you have a work longer than the screen width this method will fail.
    private Vector breakIntoLines(String text, Font textFont, int availablePixels)
    {
        Vector lines = new Vector(); // temp storage of the line
        Vector parsed = tokenize(text, " ");
        Enumeration wordEnum = parsed.elements();

        String word = null;
        StringBuffer candidateLineBuff = new StringBuffer(); // candidate next line

        while (wordEnum.hasMoreElements())
        {
            word = (String) wordEnum.nextElement();

            if (candidateLineBuff.length() == 0) {
                candidateLineBuff.append(word); // there will be at least one word on each line.
            }
            else if (textFont.stringWidth(candidateLineBuff.toString() + " " + word) <= availablePixels) // it will fit, so prepare for the next word
            {
                candidateLineBuff.append(' ');
                candidateLineBuff.append(word);
            }
            else  // won't fit, so save line and start a new one
            {
                lines.addElement(candidateLineBuff.toString().trim());
                candidateLineBuff = new StringBuffer();
                candidateLineBuff.append(word);
            }
        } // while

        if (candidateLineBuff.length() > 0)
        { // add the last line
            lines.addElement(candidateLineBuff.toString());
        }
        return lines;
    }

    private Vector tokenize(String parseString, String delimiter)
    {
        Vector v = new Vector();
        int pos = 0;
        int curPos = 0;
        while (parseString.indexOf(delimiter, pos) != -1)
        {
            curPos = parseString.indexOf(delimiter, pos);
            v.addElement(parseString.substring(pos, curPos + delimiter.length()));
            pos = curPos + delimiter.length();
        }
        //add what is left at the end
        v.addElement(parseString.substring(pos));
        return v;
    }

    public void setWidth(int w) {
        m_layout.setWidth(w);
        updateText();
    }

    public void setHeight(int h) {
        m_layout.setHeight(h);
    }

    public int getMinWidth() {
        return 0;
//        return m_layout.getMinWidth();
    }

    public int getMinHeight() {
        return m_layout.getMinHeight();
    }

    public int getWidth() {
        return m_layout.getWidth();
    }

    public int getHeight() {
//        return m_layout.getHeight();
        return m_textItems.size()*getFont().getHeight();
    }

    public void setXPos(int x) {
        super.setXPos(x);
        m_layout.setXPos(x);
    }

    public void setYPos(int y) {
        super.setYPos(y);
        m_layout.setYPos(y);
    }
    
    public void draw(Graphics g) {
        if (!m_textItems.isEmpty()) {
            // Calling draw on the layout will ensure that its size and
            // positionsare correct.
            m_layout.draw(g);
            
            // We could pre-render this to an image each time setText is called.
            int x = this.getXPos();
            int y = this.getYPos();

            Font origFont = g.getFont();
            int origColor = g.getColor();

            g.setFont(getFont());
            g.setColor(getColor());

            for (int i = 0; i < m_textItems.size(); ++i) {
                TextItem ti = (TextItem)m_textItems.elementAt(i);
                String lineText = ti.getText(0);
                int lineX = ti.getX(0);
                int lineY = ti.getY(0);
                g.drawString(lineText, lineX, lineY, Graphics.TOP|Graphics.LEFT);
            }

            g.setFont(origFont);
            g.setColor(origColor);
        }
    }
}
