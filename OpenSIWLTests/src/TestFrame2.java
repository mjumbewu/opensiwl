/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import oiwl.widget.*;

/**
 *
 * @author mjumbewu
 */
public class TestFrame2 extends TestFrame {
    Layout layout = new LinearLayout(Orientation.VERTICAL);
    StaticText st1 = new StaticText("This one starts off with two");
    StaticText st2 = new StaticText("text items arranged vertically.");
    
    StaticText st_new1 = new StaticText("Bottom1");
    StaticText st_new2 = new StaticText("Bottom2");
    
    Layout hor_new = new LinearLayout(Orientation.HORIZONTAL);
    StaticText st_hor1 = new StaticText("Hor1");
    StaticText st_hor2 = new StaticText("Hor2");
    
    public TestFrame2(OpenSIWLTests app) {
        super(app, "2", Orientation.PORTRAIT);
        this.setLayout(layout);
        layout.manage(st1);
        layout.manage(st2);
        layout.manage(new StaticText("Then two more are added at the"));
        layout.manage(new StaticText(" bottom.  Finally, two more are "));
        layout.manage(new StaticText("inserted within a horizontally "));
        layout.manage(new StaticText("oriented LinearLayout across "));
        layout.manage(new StaticText("all of these."));
    }
    
    protected boolean runtest() {
        layout.manage(st_new1);
        layout.manage(st_new2);
        
        layout.manage(hor_new, 2);
        hor_new.manage(st_hor1);
        hor_new.manage(st_hor2);
        
        return (st1.getGlobalYPos() < st2.getGlobalYPos() &&
                st2.getGlobalYPos() < hor_new.getGlobalYPos() &&
                st2.getGlobalYPos() < st_hor1.getGlobalYPos() &&
                st_hor1.getGlobalYPos() == st_hor2.getGlobalYPos() &&
                st_hor2.getGlobalYPos() < st_new1.getGlobalYPos() &&
                hor_new.getGlobalYPos() < st_new1.getGlobalYPos() &&
                st_new1.getGlobalYPos() < st_new2.getGlobalYPos() &&
                st_hor1.getGlobalXPos() < st_hor2.getGlobalXPos());
    }
}
