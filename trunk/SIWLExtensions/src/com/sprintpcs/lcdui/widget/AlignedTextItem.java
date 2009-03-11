/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

import javax.microedition.lcdui.Font;

/**
 *
 * @author mjumbewu
 */
public class AlignedTextItem extends TextItem {
    int alignment;
    int widget_w;
    int widget_h;
    
    public AlignedTextItem(int widgetItem) {
        super(widgetItem);
        
        widget_w = 0;
        widget_h = 0;
        alignment = Alignment.TOP|Alignment.HCENTER;
    }

    public AlignedTextItem(String aLabel, Widget aWidget) {
        this(aLabel, Font.getDefaultFont(),
                aWidget.getWidth(), aWidget.getHeight(),
                Alignment.VCENTER|Alignment.HCENTER);
    }
    
    public void setWidth(int aWidth) {
        widget_w = aWidth;
    }
    
    public void setHeight(int aHeight) {
        widget_h = aHeight;
    }
    
    public void setAlignment(int aAlignment) {
        alignment = aAlignment;
    }
    
    public AlignedTextItem(String aLabel, Font aFont, int aWidth, int aHeight, int aAlignment) {
        super(aLabel, aFont);
        
        widget_w = aWidth;
        widget_h = aHeight;
        alignment = aAlignment;
        this._totallyRecalcPositions();
    }
    
    public AlignedTextItem(String aLabel, Font aFont, int aColor, int aWidth, int aHeight, int aAlignment) {
        super(aLabel, aFont, aColor);
        
        widget_w = aWidth;
        widget_h = aHeight;
        alignment = aAlignment;
        this._totallyRecalcPositions();
    }
    
    public void addLine(String aLine, Font aFont) {
        super.addLine(aLine, aFont);
        this._integrateLine();
    }
    
    public void addLine(String aLine, Font aFont, int aColor) {
        super.addLine(aLine, aFont, aColor);
        this._integrateLine();
    }
    
    public void removeLine(int index) {
        super.removeLine(index);
        
        // Lazy.
        this._totallyRecalcPositions();
    }
    
    private int _getLeft() {
        int min_l = widget_w;
        
        for (int i = 0; i < this.getTotalLine(); ++i)
            min_l = Math.min(this.getX(i), min_l);
        
        return min_l;
    }
    
    private int _getWidth() {
        int max_w = 0;
        
        for (int i = 0; i < this.getTotalLine(); ++i)
            max_w = Math.max(this.getFont(i).stringWidth(this.getText(i)), max_w);
        
        return max_w;
    }
    
    private int _getTop() {
        int min_t;
        
             if ((alignment & Alignment.TOP)     != 0) min_t = 0;
        else if ((alignment & Alignment.VCENTER) != 0) min_t = widget_h/2;
        else if ((alignment & Alignment.BOTTOM)  != 0) min_t = widget_h;
        else min_t = 0; // default is TOP.
        
        for (int i = 0; i < this.getTotalLine(); ++i)
            min_t = Math.min(this.getY(i), min_t);
        
        return min_t;
    }
    
    private int _getHeight() {
        int max_h = 0;
        
        for (int i = 0; i < this.getTotalLine(); ++i)
            max_h = Math.max(this.getY(i) + this.getFont(i).getHeight(), max_h);
        
        return max_h;
    }
    
    private void _integrateLine() {
        /*
         * Recalculate the positions of all lines assuming that all but the last
         * line were in the right places before recalculation.
         */
        
        int num_lines = this.getTotalLine();
        
        String text = this.getText(num_lines-1);
        Font font = this.getFont(num_lines-1);
        
        int line_x, line_y;
        int line_w = font.stringWidth(text);
        int line_h = font.getHeight();
        
        // Calculate the x-coordinate of the last line.
             if ((alignment & Alignment.LEFT   ) != 0) line_x = 0;
        else if ((alignment & Alignment.HCENTER) != 0) line_x = (widget_w - line_w)/2;
        else if ((alignment & Alignment.RIGHT  ) != 0) line_x = widget_w - line_w;
        else line_x = 0; // default is LEFT
        
        // Calculate the y-coordinate of the last line and move the other lines,
        // if necessary).
        int old_y;
        int old_h = 0;
        
             if ((alignment & Alignment.TOP)     != 0) old_y = 0;
        else if ((alignment & Alignment.VCENTER) != 0) old_y = widget_h/2;
        else if ((alignment & Alignment.BOTTOM)  != 0) old_y = widget_h;
        else old_y = 0; // default is TOP.
        
        for (int i = 0; i < num_lines-1; ++i)
            old_y = Math.min(this.getY(i), old_y);
        
        for (int i = 0; i < num_lines-1; ++i)
            old_h = Math.max(this.getY(i) + this.getFont(i).getHeight() - old_y, old_h);

        int new_h = old_h + line_h;
        int new_y;
        
             if ((alignment & Alignment.TOP    ) != 0) new_y = 0;
        else if ((alignment & Alignment.VCENTER) != 0) new_y = (widget_h - new_h)/2;
        else if ((alignment & Alignment.BOTTOM ) != 0) new_y = (widget_h - new_h);
        else new_y = 0; // default is TOP
        
        int dy = new_y - old_y;
        for (int i = 0; i < num_lines; ++i)
            this.setCoordinates(i, this.getX(i), this.getY(i) + dy);
        line_y = old_y + old_h + dy;
        
        this.setCoordinates(num_lines-1, line_x, line_y);
    }
    
    private void _totallyRecalcPositions() {
        /*
         * Recalculate the positions of all lines assuming that you know nothing
         * about the current positions of any of the lines.
         */
        int num_lines = this.getTotalLine();
        int start_y;
        
        // Calculate the starting vertical position.
             if ((alignment & Alignment.TOP    ) != 0) start_y = 0;
        else if ((alignment & Alignment.VCENTER) != 0) start_y = (widget_h - this._getHeight())/2;
        else if ((alignment & Alignment.BOTTOM ) != 0) start_y = widget_h - this._getHeight();
        else start_y = 0;
        
        int new_x;
        int new_y = start_y;
        for (int i = 0; i < num_lines; ++i) {
            
            // Calculate the horizontal position for each line.
            int string_w = this.getFont(i).stringWidth(this.getText(i));
            int string_h = this.getFont(i).getHeight();
                 if ((alignment & Alignment.LEFT   ) != 0) new_x = 0;
            else if ((alignment & Alignment.HCENTER) != 0) new_x = (widget_w - string_w)/2;
            else if ((alignment & Alignment.RIGHT  ) != 0) new_x = widget_w - string_w;
            else new_x = 0;
            
            this.setCoordinates(i, new_x, new_y);
            
            // Add the current line height to the next vertical position.
            new_y += string_h;
        }
    }
}

