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
        System.out.println("initialized super-base");
        this.addPanel(testPanel);
        System.out.println("added panel");
        testPanel.getLayout().manage(new StaticText(testID + ": Performing test...", Font.getDefaultFont(), 0x000000ff));
        System.out.println("managed first in panel");
    }
    
    protected void start() {
        Thread runner = new Thread(this);
        runner.start();
    }
    
    abstract protected boolean runtest();
    
    public void run() {
        System.out.print("testing...");
        boolean success = runtest();
        System.out.println("done.");
        testPanel.getLayout().unmanage(0);
        if (success) {
            System.out.println("success!");
            testPanel.getLayout().manage(new StaticText(testID + ": Success!",
                    Font.getDefaultFont(), 0x0000ff00));
        }

        else {
            System.out.println("failure!");
            testPanel.getLayout().manage(new StaticText(testID + ": Failure!",
                    Font.getDefaultFont(), 0x00ff0000));
        }
        System.out.println("test over.");
    }
    
    public void keyPressed(int key) {
        app.nextTest();
    }
}