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
    
    public TestFrame2(OpenSIWLTests app, String id, int orient) {
        super(app, id, orient);

        this.instructions = "This test starts off with two text items arranged" +
                "vertically.  Then several more are added below.  Finally, two" +
                "more are inserted within a horizontally oriented LinearLayout" +
                "across all of these.";
        
        this.setLayout(layout);
        layout.manage(st1);
        layout.manage(st2);
    }
    
    protected boolean runtest() {
        layout.manage(new StaticText("Then two more are added at the"));
        layout.manage(new StaticText(" bottom.  Finally, two more are "));
        layout.manage(new StaticText("inserted within a horizontally "));
        layout.manage(new StaticText("oriented LinearLayout across "));
        layout.manage(new StaticText("all of these."));
        layout.manage(st_new1);
        layout.manage(st_new2);
        
        ((LinearLayout)layout).manage(hor_new, 2, 0, true, true);
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
