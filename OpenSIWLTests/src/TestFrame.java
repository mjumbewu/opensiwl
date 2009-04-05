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
    String instructions = "";

    StaticText statusText;
    
    OpenSIWLTests app;
    
    public TestFrame(OpenSIWLTests App, String TestID, int Orient) {
        super(Orient);
        testID = TestID;
        app = App;
        this.addPanel(testPanel);
        statusText = new StaticText(testID + ": Performing test...", Font.getDefaultFont(), 0x000000ff);
        testPanel.getLayout().manage(statusText);
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
        testPanel.getLayout().unmanage(statusText);
        if (success) {
            System.out.println(testID + ": success!");
            statusText = new StaticText(testID + ": Success!",
                    Font.getDefaultFont(), 0x0000ff00);
            
        }

        else {
            System.out.println(testID + ": failure!");
            statusText = new StaticText(testID + ": Failure!",
                    Font.getDefaultFont(), 0x00ff0000);
        }
        testPanel.getLayout().manage(statusText);
        System.out.println("test over.");
        this.invalidate();
    }
    
    public void keyPressed(int key) {
        app.nextTest();
    }
}
