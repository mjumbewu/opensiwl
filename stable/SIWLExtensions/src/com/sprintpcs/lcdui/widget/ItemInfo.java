/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

/**
 * The abstract class for position and size information about an item.
 * @author mjumbewu
 */
abstract class ItemInfo {
    public static ItemInfo create(Object item) {
        ItemInfo info;

        // ::NOTE:: I don't want to hard code the item types, but I'm going to
        //          until I can think of a better solution.
        if (DynamicallySizedWidget.class.isInstance(item))
            info = new ResizableWidgetInfo();
        else if (TextItem.class.isInstance(item))
            info = new TextItemInfo();
        else if (ItemWidget.class.isInstance(item))
            info = new ItemWidgetInfo();
        else {
            System.out.println("Cannot get info from a " + item.getClass().toString());
            throw new IllegalArgumentException("Cannot get info from a " + item.getClass().toString());
        }
        info.setItem(item);
        return info;
    }

    abstract public void setItem(Object o);
    abstract public Object getItem();

    abstract public int getXPos();
    abstract public int getYPos();
    abstract public void setXPos(int x);
    abstract public void setYPos(int y);

    abstract public int getWidth();
    abstract public int getHeight();
    abstract public void setWidth(int w);
    abstract public void setHeight(int h);

    abstract public int getMinWidth();
    abstract public int getMinHeight();
}

/**
 * The class for providing size and position information about ItemWidget
 * objects.
 * @author mjumbewu
 */
class ItemWidgetInfo extends ItemInfo {
    ItemWidget item;

    public void setItem(Object o) {
        if (ItemWidget.class.isInstance(o))
            item = (ItemWidget)o;
        else
            throw new IllegalArgumentException();
    }

    public Object getItem() { return item; }

    public int getXPos() { return item.getXPos(); }
    public int getYPos() { return item.getYPos(); }
    public void setXPos(int x) { item.setXPos(x); }
    public void setYPos(int y) { item.setYPos(y); }

    public int getWidth() { return item.getWidth(); }
    public int getHeight() { return item.getHeight(); }
    public void setWidth(int w) {}
    public void setHeight(int h) {}

    public int getMinWidth() { return item.getWidth(); }
    public int getMinHeight() { return item.getHeight(); }
}


/**
 * The class for providing size and position information about resizable
 * objects.
 * @author mjumbewu
 */
class ResizableWidgetInfo extends ItemInfo {
    DynamicallySizedWidget item;

    public void setItem(Object o) {
        if (DynamicallySizedWidget.class.isInstance(o))
            item = (DynamicallySizedWidget)o;
        else
            throw new IllegalArgumentException();
    }

    public Object getItem() { return item; }

    public int getXPos() { return item.getXPos(); }
    public int getYPos() { return item.getYPos(); }
    public void setXPos(int x) { item.setXPos(x); }
    public void setYPos(int y) { item.setYPos(y); }

    public int getWidth() { return item.getWidth(); }
    public int getHeight() { return item.getHeight(); }
    public void setWidth(int w) { item.setWidth(w); }
    public void setHeight(int h) { item.setHeight(h); }

    public int getMinWidth() { return item.getMinWidth(); }
    public int getMinHeight() { return item.getMinHeight(); }
}

/**
 * The class for providing size and position information about TextItem
 * objects.
 * @author mjumbewu
 */
class TextItemInfo extends ItemInfo {
    TextItem item;

    public void setItem(Object o) {
        if (TextItem.class.isInstance(o))
            item = (TextItem)o;
        else
            throw new IllegalArgumentException();
    }

    public Object getItem() { return item; }

    public int getXPos() { return item.getX(0); }
    public int getYPos() { return item.getY(0); }
    public void setXPos(int x) { item.setCoordinates(0, x, item.getY(0)); }
    public void setYPos(int y) { item.setCoordinates(0, item.getX(0), y); }

    public int getWidth() { return item.getFont(0).stringWidth(item.getText(0)); }
    public int getHeight() { return item.getFont(0).getHeight(); }
    public void setWidth(int w) {}
    public void setHeight(int h) {}

    public int getMinWidth() { return getWidth(); }
    public int getMinHeight() { return getHeight(); }
}

