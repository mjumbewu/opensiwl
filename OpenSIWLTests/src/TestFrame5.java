/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import oiwl.widget.*;

/**
 *
 * @author mjumbewu
 */
public class TestFrame5 extends TestFrame implements EventListener {
    PushButton testButton = new PushButton();
    boolean testButtonPressed = false;

    public TestFrame5(OpenSIWLTests app, String id, int orient) {
        super(app, id, orient);

        this.instructions = "This is a simple test.  It consists of a Frame " +
                "with a vertical linear layout manager.  Several StaticText" +
                "objects are managed within the layout.  They should be " +
                "displayed in a column on the screen.";

        AbsoluteLayout layout = new AbsoluteLayout();
        this.setLayout(layout);

        layout.manage(testButton);
        layout.setXPos(testButton, 30);
        layout.setYPos(testButton, 70);

        testButton.setLabel(new StaticText("Testing Button"));
        testButton.addEventListener(this);
    }
    
    protected boolean runtest() {
        int devicex = this.frameToDeviceX(32, 72);
        int devicey = this.frameToDeviceY(32, 72);
        this.pointerPressed(devicex, devicey);
        this.pointerReleased(devicex, devicey);
        
        return testButtonPressed;
    }

    public void onEvent(int type, Widget sender, Object data) {
        if (sender == testButton)
            testButtonPressed = true;
    }
}
