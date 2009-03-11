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
public class Padding extends ItemWidget implements DynamicallySizedWidget {

    private ItemInfo m_itemInfo;

    public Padding(Object item, int l, int t, int r, int b) {
        m_itemInfo = ItemInfo.create(item);
        m_left = l;
        m_top = t;
        m_right = r;
        m_bottom = b;
    }

    public Padding(Object item, int horz, int vert) {
        this(item, horz, vert, horz, vert);
    }

    public Padding(Object item, int pad) {
        this(item, pad, pad);
    }

    public Padding(Object item) {
        this(item, 0);
    }

    public int m_left;
    public int m_right;
    public int m_bottom;
    public int m_top;

    public int getLeft() {
        return m_left;
    }

    public int getRight() {
        return m_right;
    }

    public int getTop() {
        return m_top;
    }

    public int getBottom() {
        return m_bottom;
    }

    public void setLeft(int l) {
        m_left = l;
        updateInfo();
    }

    public void setRight(int r) {
        m_right = r;
        updateInfo();
    }

    public void setTop(int t) {
        m_top = t;
        updateInfo();
    }

    public void setBottom(int b) {
        m_bottom = b;
        updateInfo();
    }

    public void draw(Graphics g) {}

    private void updateInfo() {
        m_itemInfo.setXPos(getXPos() + getLeft());
        m_itemInfo.setYPos(getYPos() + getTop());
    }

    public void setXPos(int x) {
        super.setXPos(x);
        updateInfo();
    }

    public void setYPos(int y) {
        super.setYPos(y);
        updateInfo();
    }

    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        m_itemInfo.setXPos(x + getLeft());
        m_itemInfo.setYPos(y + getTop());
    }

    public void setHeight(int aSize) {
        m_itemInfo.setHeight(aSize - getTop() - getBottom());
    }

    public void setWidth(int aSize) {
        m_itemInfo.setWidth(aSize - getLeft() - getRight());
    }

    public int getHeight() {
        return m_itemInfo.getHeight() + getTop() + getBottom();
    }

    public int getWidth() {
        return m_itemInfo.getWidth() + getLeft() + getRight();
    }

    public int getMinWidth() {
        return m_itemInfo.getMinWidth() + getLeft() + getRight();
    }

    public int getMinHeight() {
        return m_itemInfo.getMinHeight() + getTop() + getBottom();
    }

}
