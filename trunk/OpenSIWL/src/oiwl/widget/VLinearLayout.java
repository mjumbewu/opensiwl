/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 *
 * @author mjumbewu
 */
public class VLinearLayout extends LinearLayout {
    protected int getAxialPos() { return this.getYPos(); }
    protected int getOrthoPos() { return this.getXPos(); }
    
    protected int getAxialSize() { return this.getHeight(); }
    protected int getOrthoSize() { return this.getWidth(); }
    protected void setAxialSize(int size) { this.setHeight(size); }
    protected void setOrthoSize(int size) { this.setWidth(size); }
    
    protected int getStretchedAxialSize() { return this.getStretchedHeight(); }
    protected int getStretchedOrthoSize() { return this.getStretchedWidth(); }
    protected void setStretchedAxialSize(int size) { this.setStretchedHeight(size); }
    protected void setStretchedOrthoSize(int size) {this.setStretchedWidth(size); }
    
    protected int getAxialPos(Item item) { return item.getYPos(); }
    protected int getOrthoPos(Item item) { return item.getXPos(); }
    protected void setAxialPos(Item item, int pos) { item.setYPos(pos); }
    protected void setOrthoPos(Item item, int pos) { item.setXPos(pos); }
    
    protected int getAxialSize(Item item) { return item.getHeight(); }
    protected int getOrthoSize(Item item) { return item.getWidth(); }
    protected void setAxialSize(Item item, int size) { item.setHeight(size); }
    protected void setOrthoSize(Item item, int size) { item.setWidth(size); }
    
    protected int getMinAxialSize(Item item) { return item.getMinHeight(); }
    protected int getMinOrthoSize(Item item) { return item.getMinWidth(); }
    
    protected int getAxialAlign(int index) { return this.getVAlignment(index) << 3; }
    protected int getOrthoAlign(int index) { return this.getHAlignment(index) << 6; }
}
