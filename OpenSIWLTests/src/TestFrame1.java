/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import oiwl.widget.*;

/**
 *
 * @author mjumbewu
 */
public class TestFrame1 extends TestFrame {
    public TestFrame1(OpenSIWLTests app) {
        super(app, "1", Orientation.PORTRAIT);
        Layout layout = new LinearLayout(Orientation.PORTRAIT);
        this.setLayout(layout);
        layout.manage(new StaticText("This is a simple test.  It  "));
        layout.manage(new StaticText("consists of a Frame with a  "));
        layout.manage(new StaticText("vertical linear layout      "));
        layout.manage(new StaticText("manager.  Several StaticText"));
        layout.manage(new StaticText("objects are managed within  "));
        layout.manage(new StaticText("the layout.  They should be "));
        layout.manage(new StaticText("displayed in a column on the"));
        layout.manage(new StaticText("screen.  The default        "));
        layout.manage(new StaticText("expansion is true.  "));
    }
    
    protected boolean runtest() {
        return true;
    }
}
