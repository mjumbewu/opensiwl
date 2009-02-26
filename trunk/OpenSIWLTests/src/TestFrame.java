/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import oiwl.widget.*;
import javax.microedition.lcdui.Font;

/**
 *
 * @author mjumbewu
 */
public abstract class TestFrame extends Frame implements Runnable {
    Panel testPanel = new Panel(Panel.BOTTOM);
    String testID;
    
    OpenSIWLTests app;
    
    public TestFrame(OpenSIWLTests App, String TestID, int Orient) {
        super(Orient);
        testID = TestID;
        app = App;
        this.addPanel(testPanel);
        testPanel.getLayout().manage(new StaticText(testID + ": Performing test...", Font.getDefaultFont(), 0x000000ff));
    }
    
    protected void start() {
        Thread runner = new Thread(this);
        runner.start();
    }
    
    abstract protected boolean runtest();
    
    public void run() {
        boolean success = runtest();
        testPanel.getLayout().unmanage(0);
        if (success)
            testPanel.getLayout().manage(new StaticText(testID + ": Success!",
                    Font.getDefaultFont(), 0x0000ff00));
        else
            testPanel.getLayout().manage(new StaticText(testID + ": Failure!",
                    Font.getDefaultFont(), 0x00ff0000));
    }
    
    public void keyPressed(int key) {
        app.nextTest();
    }
}
