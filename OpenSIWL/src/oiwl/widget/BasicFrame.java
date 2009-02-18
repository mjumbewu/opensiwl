/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import java.util.Vector;
/**
 *
 * @author mjumbewu
 */
public class BasicFrame extends Frame {
    private Vector m_widgets;
    
    public void addWidget(ItemWidget aWidget) {
        if (this.getIndex(aWidget) == -1)
            m_widgets.addElement(aWidget);
    }
    
    public int getIndex(ItemWidget aWidget) {
        return this.m_widgets.indexOf(aWidget);
    }
    
    public ItemWidget getItemAt(int index) {
        return (ItemWidget)this.m_widgets.elementAt(index);
    }
    
    public void removeItemWidget(ItemWidget aWidget) {
        this.m_widgets.removeElement(aWidget);
    }
    
    public void pointerDown(int x, int y) {}
    public void pointerDrag(int x, int y) {}
    public void pointerUp  (int x, int y) {}
    
    
}
