/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import oiwl.widget.*;

/**
 *
 * @author mjumbewu
 */
public class TestFrame3 extends TestFrame {
    Layout layout = new LinearLayout(Orientation.VERTICAL);
    StaticText st1 = new StaticText("This one starts off with two");
    StaticText st2 = new StaticText("text items arranged vertically.");
    
    StaticText st_new1 = new StaticText("Bottom1");
    StaticText st_new2 = new StaticText("Bottom2");
    
    Layout hor_new = new LinearLayout(Orientation.HORIZONTAL);
    StaticText st_hor1 = new StaticText("Hor1");
    StaticText st_hor2 = new StaticText("Hor2");
    
    public TestFrame3(OpenSIWLTests app, String id) {
        super(app, id, Orientation.PORTRAIT);
        
        this.instructions = "This test is very similar to the previous one.  " +
                "The main difference is that painting of the frame is suppressed" +
                "when the new Widget objects are added to the layout.  So, where" +
                "before the frame may have been visibly updating after each " +
                "additional managed Widget, now the Frame should update all at" +
                "once at the end.";

        this.setLayout(layout);
        layout.manage(st1);
        layout.manage(st2);
    }
    
    protected boolean runtest() {
        this.suppressPaint();
            layout.manage(new StaticText("This test is a lot like the one"));
            layout.manage(new StaticText("before it except painting is "));
            layout.manage(new StaticText("suppressed until the new Widget"));
            layout.manage(new StaticText("objects are added."));
            
            boolean cannot_paint = !this.canPaint();
            layout.manage(st_new1);
            layout.manage(st_new2);

            ((LinearLayout)layout).manage(hor_new, 2, 0, true, true);
            hor_new.manage(st_hor1);
            hor_new.manage(st_hor2);
        this.allowPaint();
        boolean can_paint = this.canPaint();
        this.repaint();
        
        // Check that we couldn't paint when we weren't supposed to and we
        // could when we were.
        return (cannot_paint && can_paint);
    }
}
