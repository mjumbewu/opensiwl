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
public class TestFrame1 extends TestFrame {
    public TestFrame1(SIWLExtensionTests app, String id) {
        super(app, id);

        this.instructions = "This is a very simple frame that does nothing " +
                "but demonstrates how layouts can be used to easily position " +
                "many display items without worrying about their exact " +
                "coordinates.";

        // First, I initialize all my Widgets
        PushButton lbutton = new PushButton(PushButton.BIG_BLACK_BUTTON_TYPE,0,0);
        PushButton mbutton1 = new PushButton(PushButton.MEDIUM_BLACK_BUTTON_TYPE,0,0);
        PushButton mbutton2 = new PushButton(PushButton.MEDIUM_BLACK_BUTTON_TYPE,0,0);
        PushButton sbutton1 = new PushButton(PushButton.SMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton sbutton2 = new PushButton(PushButton.SMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton sbutton3 = new PushButton(PushButton.SMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton xsbutton1 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton xsbutton2 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton xsbutton3 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton xsbutton4 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton mixed1 = new PushButton(PushButton.MEDIUM_BUTTON_TYPE,0,0);
        PushButton mixed2 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        PushButton mixed3 = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);

        // Create a layout for the entire frame.  Be sure to set the desired
        // orientation, position, and size.  In this case I want a column of
        // rows of widgets, so I will choose a vertically oriented LinearLayout
        // as my top-level layout item.
        Layout mainlayout = new LinearLayout(Orientation.VERTICAL);
        mainlayout.setPosition(0, 0);
        mainlayout.setSize(this.getWidth(), this.getDrawableHeight());

        // Create a layout to manage the medium buttons (2 across)
        Layout row2 = new LinearLayout(Orientation.HORIZONTAL);
        row2.manage(mbutton1);
        row2.manage(mbutton2);

        // Create a layout to manage the small buttons (3 across)
        Layout row3 = new LinearLayout(Orientation.HORIZONTAL);
        row3.manage(sbutton1);
        row3.manage(sbutton2);
        row3.manage(sbutton3);

        // Create a layout to manage the extra-small buttons (4 across)
        Layout row4 = new LinearLayout(Orientation.HORIZONTAL);
        row4.manage(xsbutton1);
        row4.manage(xsbutton2);
        row4.manage(xsbutton3);
        row4.manage(xsbutton4);

        // Create a layout to manage a row with multiple types of buttons
        // (1 medium and 2 xsmall)
        Layout row5 = new LinearLayout(Orientation.HORIZONTAL);
        row5.manage(mixed1);
        row5.manage(mixed2);
        row5.manage(mixed3);

        // Add the large button and the row layouts to the main layout
        mainlayout.manage(lbutton);
        mainlayout.manage(row2);
        mainlayout.manage(row3);
        mainlayout.manage(row4);
        mainlayout.manage(row5);

        // Now I want a title bar, so lets put one on.  We can manage the
        // text item in the title bar with a layout as well.
        TitleBar tb = new TitleBar();
        TextItem cap = new TextItem("Caption", Font.getDefaultFont(), 0x00ffff00);

        LinearLayout capLayout = new LinearLayout(Orientation.VERTICAL);
        capLayout.setSizeToWidget(tb);
        capLayout.manage(cap);
        tb.setTitle(cap);

        this.setTitleBar(tb);

        // Bear in mind that the width and height of the usable portion of the
        // frame have changed by this point, owing to the late inclusion of a
        // title bar.  So, just reset the size of the main layout.
        mainlayout.setSize(this.getWidth(), this.getDrawableHeight());

        // Add all the items to the frame, including the layouts
        this.addItemWidget(mainlayout);
        this.addItemWidget(lbutton);
        this.addItemWidget(row2);
        this.addItemWidget(row3);
        this.addItemWidget(row4);
        this.addItemWidget(row5);
        this.addItemWidget(mbutton1);
        this.addItemWidget(mbutton2);
        this.addItemWidget(sbutton1);
        this.addItemWidget(sbutton2);
        this.addItemWidget(sbutton3);
        this.addItemWidget(xsbutton1);
        this.addItemWidget(xsbutton2);
        this.addItemWidget(xsbutton3);
        this.addItemWidget(xsbutton4);
        this.addItemWidget(mixed1);
        this.addItemWidget(mixed2);
        this.addItemWidget(mixed3);
    }
    
    protected boolean runtest() {
        return true;
    }
}
