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
        layout.manage(new StaticText("Then two more are added at the bottom."));
        layout.manage(new StaticText("Finally, two more are inserted within"));
        layout.manage(new StaticText("a horizontally oriented LinearLayout"));
        layout.manage(new StaticText("across all of these."));
    }
    
    protected boolean runtest() {
        layout.manage(st_new1);
        layout.manage(st_new2);
        
        layout.manage(hor_new, 2);
        hor_new.manage(st_hor1);
        hor_new.manage(st_hor2);
        
        return (st1.getLocalYPos() < st2.getLocalYPos() &&
                st2.getLocalYPos() < hor_new.getLocalYPos() &&
                st2.getLocalYPos() < st_hor1.getLocalYPos() &&
                st_hor1.getLocalYPos() == st_hor2.getLocalYPos() &&
                st_hor2.getLocalYPos() < st_new1.getLocalYPos() &&
                hor_new.getLocalYPos() < st_new1.getLocalYPos() &&
                st_new1.getLocalYPos() < st_new2.getLocalYPos() &&
                st_hor1.getLocalXPos() < st_hor2.getLocalXPos());
    }
}
