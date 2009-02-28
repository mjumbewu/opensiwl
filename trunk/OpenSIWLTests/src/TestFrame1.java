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
    public TestFrame1(OpenSIWLTests app, String id) {
        super(app, id, Orientation.PORTRAIT);
        System.out.println("initialized base");
        Layout layout = new LinearLayout(Orientation.VERTICAL);
        System.out.println("initialized layout");
        this.setLayout(layout);
        System.out.println("set layout");
        layout.manage(new StaticText("This is a simple test.  It  "));
        System.out.println("managed first");
        layout.manage(new StaticText("consists of a Frame with a  "));
        System.out.println("managed second");
        layout.manage(new StaticText("vertical linear layout      "));
        layout.manage(new StaticText("manager.  Several StaticText"));
        layout.manage(new StaticText("objects are managed within  "));
        layout.manage(new StaticText("the layout.  They should be "));
        layout.manage(new StaticText("displayed in a column on the"));
        layout.manage(new StaticText("screen.  The default        "));
        layout.manage(new StaticText("expansion is true.  "));
        System.out.println("managed all");
    }
    
    protected boolean runtest() {
        return true;
    }
}
