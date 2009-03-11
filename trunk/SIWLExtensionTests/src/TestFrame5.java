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
public class TestFrame5 extends TestFrame {
    // Create a layout for the entire frame.  In this case I want a column
    // of widgets, so I will choose a vertically oriented LinearLayout
    // as my top-level layout item.
    LinearLayout mainlayout = new LinearLayout(Orientation.VERTICAL);

    public TestFrame5(SIWLExtensionTests app, String id) {
        super(app, id);

        this.instructions = "This frame is used to test flicking.  It turns " +
                "out that widgets will block the frame from receiving " +
                "pointer pressed events, so it is my assessment that " +
                "flickable frames with widgets are not feasible in SIWL.  " +
                "Note, there are ways around this, such as drawing text and " +
                "images directly onto the frame or using a ListFrame (though " +
                "that's fraught with limitations too).";

        // First, I initialize all my Widgets
        PushButton xsbutton = new PushButton(PushButton.XSMALL_BLACK_BUTTON_TYPE,0,0);
        StaticText wtext = new StaticText(this.instructions,Font.getDefaultFont(),0x00000000,true,0,0);

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

        // Add the large button and the row layouts to the main layout
        mainlayout.manage(wtext);
        mainlayout.manage(xsbutton);

        mainlayout.setFill(wtext, true);
        mainlayout.setPadding(wtext, 10);
        wtext.setAlignment(Alignment.LEFT);
        mainlayout.setExpand(xsbutton, false);

        // Be sure to set the desired orientation, position, and size for the
        // main layout.
        mainlayout.setPosition(0, 0);
        mainlayout.setSize(this.getWidth(), this.getDrawableHeight());

        // Add all the items to the frame, including the layouts
        this.addItemWidget(mainlayout);
        this.addItemWidget(xsbutton);
        this.addItemWidget(wtext);
    }

    /**
     * Move the layout vertically by some specified amount
     * @param dy The amount to move the layout.  dy < 0 means move up, dy > 0
     *           means move down.
     */
    void moveLayout(int dy) {
        int newy = mainlayout.getYPos() + dy;
//        newy = Math.min(0, newy);
//        newy = Math.max(this.getDrawableHeight() - mainlayout.getHeight(), newy);

        mainlayout.setYPos(newy);
        this.repaint();
    }

    int oldy;
    public void pointerDown(int x, int y) {
        oldy = y;
    }

    public void pointerDrag(int x, int y) {
        moveLayout(y-oldy);
        oldy = y;
    }

    public void pointerFlick(int x, int y) {
        moveLayout(y-oldy);
        oldy = y;
    }

    protected boolean runtest() {
        return true;
    }
}
