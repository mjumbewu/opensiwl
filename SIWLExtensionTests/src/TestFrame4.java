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
public class TestFrame4 extends TestFrame {
    public TestFrame4(SIWLExtensionTests app, String id) {
        super(app, id);

        this.instructions = "This frame is used to test the Spacer widget.  " +
                "The two extra-small buttons on the bottom should be pushed " +
                "to opposite edges of the screen.  This is because there are " +
                "three widgets, the middle one being a spacer.  A spacer " +
                "is a dynamically resizing widget that will expand to take " +
                "as much space as it is allowed.";

        // First, I initialize all my Widgets
        PushButton xsbutton1 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton xsbutton2 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        StaticText wtext = new StaticText(this.instructions,Font.getDefaultFont(),0x00000000,true,0,0);
        Spacer spacer = new Spacer();

        // Now put a title bar at the top of the Frame.  We can manage the
        // text item in the title bar with a layout.
        TitleBar tb = new TitleBar();
        TextItem cap = new TextItem("Caption", Font.getDefaultFont(), 0x00ffff00);

        LinearLayout capLayout = new LinearLayout(Orientation.VERTICAL);
        capLayout.setSizeToWidget(tb);
        capLayout.manage(cap);
        // Force a layout recalculation
        capLayout.recalculateSizes();
        tb.setTitle(cap);

        this.setTitleBar(tb);

        // Create a layout for the entire frame.  In this case I want a column 
        // of widgets, so I will choose a vertically oriented LinearLayout
        // as my top-level layout item.
        LinearLayout mainlayout = new LinearLayout(Orientation.VERTICAL);

        // Create a layout for the buttons and spacer at the bottom.
        LinearLayout bottomlayout = new LinearLayout(Orientation.HORIZONTAL);
        bottomlayout.manage(xsbutton1);
        bottomlayout.manage(spacer);
        bottomlayout.manage(xsbutton2);

        // Add the large button and the row layouts to the main layout
        mainlayout.manage(wtext);
        mainlayout.manage(bottomlayout);

        // Make the wrapped text take up as much of the width of the frame as
        // possible (i.e. have it "fill" the layout).  A layout item's fill
        // property is set to false by default.  Also, let's left-align the
        // text this time.
        mainlayout.setFill(wtext, true);
        mainlayout.setPadding(wtext, 10);
        wtext.setAlignment(Alignment.LEFT);

        // Make the bottom layout fill the bottom cell, but not expand to take
        // up more space.
        mainlayout.setFill(bottomlayout, true);
        mainlayout.setExpand(bottomlayout, false);

        // Make sure the button boxes aren't expanding and pad the buttons from
        // the edges a bit.  Note that these properties get set within the
        // bottomlayout instead of mainlayout.
        bottomlayout.setExpand(xsbutton1, false);
        bottomlayout.setExpand(xsbutton2, false);

        // Be sure to set the desired orientation, position, and size for the
        // main layout.
        mainlayout.setPosition(0, 0);
        mainlayout.setSize(this.getWidth(), this.getDrawableHeight());

        // Add all the items to the frame, including the layouts
        this.addItemWidget(mainlayout);
        this.addItemWidget(bottomlayout);
        this.addItemWidget(xsbutton1);
        this.addItemWidget(spacer);
        this.addItemWidget(xsbutton2);
        this.addItemWidget(wtext);
    }
    
    protected boolean runtest() {
        return true;
    }
}
