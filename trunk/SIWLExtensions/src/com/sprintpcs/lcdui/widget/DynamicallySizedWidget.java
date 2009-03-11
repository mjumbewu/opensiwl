/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

/**
 *
 * @author mjumbewu
 */
interface DynamicallySizedWidget {
    int getXPos();
    int getYPos();
    
    void setXPos(int x);
    void setYPos(int y);
    
    int getWidth();
    int getHeight();

    void setHeight(int aSize);
    void setWidth(int aSize);

    int getMinWidth();
    int getMinHeight();
}
