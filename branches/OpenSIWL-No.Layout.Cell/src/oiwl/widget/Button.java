/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

/**
 * If we think about it, a button is just a Widget with state.  Push buttons,
 * toggle buttons, check buttons, radio buttons, safety buttons, switch 
 * buttons, etc.
 * 
 * Some other possible states that certain buttons might want to take into 
 * account are:
 *  - UNKNOWN/NEBULOUS/MULTI (e.g. for tri-state checkboxes)
 *  - CHANGING(?) (for safety/switch buttons; not sure if it's useful)
 * 
 * And radio buttons would have to have access to the group to which they are
 * a part so that just one can have an ACTIVE state at a time.
 * @author mjumbewu
 */
public abstract class Button extends ItemWidget implements WidgetParent {
    public Button() {
        super();
    }
    
    public boolean isValidChild(Widget item) {
        return (StaticWidget.class.isInstance(item) ||
                Layout.class.isInstance(item));
    }
}
