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
    StaticText st1 = new StaticText("This Frame starts off with two StaticText");
    StaticText st2 = new StaticText("Widgets in a vertical LinearLayout.");
    
    StaticText st_new1 = new StaticText("Then, two more StaticText Widget");
    StaticText st_new2 = new StaticText("objects are appended to the bottom");
    
    Layout hor_new = new LinearLayout(Orientation.HORIZONTAL);
    StaticText st_hor1 = new StaticText("Finally, 2 are");
    StaticText st_hor2 = new StaticText("inserted across.");
    
    public TestFrame2(OpenSIWLTests app) {
        super(app, "2", Orientation.PORTRAIT);
        this.setLayout(layout);
        layout.manage(st1);
        layout.manage(st2);
    }
    
    protected boolean runtest() {
        layout.manage(st_new1);
        layout.manage(st_new2);
        
        layout.manage(hor_new, 2);
        hor_new.manage(st_hor1);
        hor_new.manage(st_hor2);
        
        return (st1.getYPos() < st2.getYPos() &&
                st2.getYPos() < hor_new.getYPos() &&
                st2.getYPos() < st_hor1.getYPos() &&
                st_hor1.getYPos() == st_hor2.getYPos() &&
                st_hor2.getYPos() < st_new1.getYPos() &&
                hor_new.getYPos() < st_new1.getYPos() &&
                st_new1.getYPos() < st_new2.getYPos() &&
                st_hor1.getXPos() < st_hor2.getXPos());
    }
}
