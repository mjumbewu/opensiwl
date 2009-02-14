/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.open.widget;

import javax.microedition.lcdui.Graphics;
import java.util.Vector;

/**
 *
 * @author mjumbewu
 */
public abstract class Widget {
    private int m_height;
    private int m_width;
    private int m_xpos;
    private int m_ypos;

    private Vector m_listeners;

    public void addWidgetEventListener(WidgetEventListener evl) {
        m_listeners.addElement(evl);
    }

    public int getHeight() {
        return m_height;
    }

    public int getWidth() {
        return m_width;
    }

    public void setXPos(int aPos) {
        m_xpos = aPos;
    }

    public int getXPos() {
        return m_xpos;
    }

    public void setYPos(int aPos) {
        m_ypos = aPos;
    }
    
    public int getYPos() {
        return m_ypos;
    }

    public abstract void draw(Graphics g);
}
