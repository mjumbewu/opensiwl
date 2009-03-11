/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sprintpcs.lcdui.widget.*;
import javax.microedition.lcdui.Font;

/**
 *
 * @author mjumbewu
 */
public class TestFrame2 extends TestFrame {
    public TestFrame2(SIWLExtensionTests app, String id) {
        super(app, id);

        this.instructions = "This is also a very simple frame.  It consists " +
                "of a textual description with a large button below it.  The " +
                "textual description is wrapped and centered using the " +
                "StaticText widget.  Press a phone button (e.g. the voice " +
                "command button on the side of the phone) to continue.";

        // First, I initialize all my Widgets
        PushButton lbutton = new PushButton(PushButton.BIG_BLACK_BUTTON_TYPE,0,0);
        StaticText wtext = new StaticText(this.instructions, Font.getDefaultFont(), 0x00000000, true, 0, 0);

        // Now put a title bar at the top of the Frame.  We can manage the
        // text item in the title bar with a layout.
        TitleBar tb = new TitleBar();
        TextItem cap = new TextItem("Caption", Font.getDefaultFont(), 0x00ffff00);

        LinearLayout capLayout = new LinearLayout(Orientation.VERTICAL);
        capLayout.setSizeToWidget(tb);
        capLayout.manage(cap);
        tb.setTitle(cap);

        this.setTitleBar(tb);

        // Create a layout for the entire frame.  In this case I want a column 
        // of widgets, so I will choose a vertically oriented LinearLayout
        // as my top-level layout item.
        Layout mainlayout = new LinearLayout(Orientation.VERTICAL);

        // Add the large button and the row layouts to the main layout
        mainlayout.manage(wtext);
        mainlayout.manage(lbutton);

        // Make the wrapped text take up as much of the width of the frame as
        // possible (i.e. have it "fill" the layout).  A layout item's fill
        // property is set to false by default.
        ((LinearLayout)mainlayout).setFill(mainlayout.getIndexOf(wtext), true);
        ((LinearLayout)mainlayout).setPadding(mainlayout.getIndexOf(wtext), 5);

        // Make the button's box take up as little room as possible (i.e. have
        // the box not "expand" in the layout).  A layout item's expand property
        // is set to true by default.
        ((LinearLayout)mainlayout).setExpand(mainlayout.getIndexOf(lbutton), false);
        ((LinearLayout)mainlayout).setPadding(mainlayout.getIndexOf(lbutton), 5);

        // Be sure to set the desired orientation, position, and size.
        mainlayout.setPosition(0, 0);
        mainlayout.setSize(this.getWidth(), this.getDrawableHeight());

        // Add all the items to the frame, including the layouts
        this.addItemWidget(mainlayout);
        this.addItemWidget(lbutton);
        this.addItemWidget(wtext);
    }
    
    protected boolean runtest() {
        return true;
    }
}
