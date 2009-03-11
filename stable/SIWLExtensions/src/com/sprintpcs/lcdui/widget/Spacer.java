/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author mjumbewu
 */
public class Spacer extends ItemWidget implements DynamicallySizedWidget {

    public void draw(Graphics g) {}

    private int m_width;
    private int m_height;

    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }
    
    public void setHeight(int aSize) {
        m_height = aSize;
    }

    public void setWidth(int aSize) {
        m_width = aSize;
    }

    public int getMinWidth() {
        return 0;
    }

    public int getMinHeight() {
        return 0;
    }
}
