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
public class TestFrame extends Frame implements Runnable {
    Panel testPanel = new Panel(Panel.BOTTOM);
    
    public TestFrame(int orient) {
        super(orient);
        this.addPanel(testPanel);
        testPanel.getLayout().manage(new StaticText("Performing test...", Font.getDefaultFont(), 0x000000ff));
    }
    
    protected void start() {
        Thread runner = new Thread(this);
        runner.start();
    }
    
    protected boolean runtest() {
        return true;
    }
    
    public void run() {
        boolean success = runtest();
        testPanel.getLayout().unmanage(0);
        if (success)
            testPanel.getLayout().manage(new StaticText("Success!",
                    Font.getDefaultFont(), 0x0000ff00));
        else
            testPanel.getLayout().manage(new StaticText("Failure!",
                    Font.getDefaultFont(), 0x00ff0000));
    }
}
