/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import oiwl.widget.*;

/**
 * @author mjumbewu
 */
public class OpenSIWLTests extends MIDlet {
    Display display;
    TestFrame[] testFrames;
    int currentTestIndex;
    boolean isInitialized = false;
    
    public void startApp() {
        if (!isInitialized) {
            System.out.println("Creating the frames");
            testFrames = new TestFrame[2];
            testFrames[0] = new TestFrame1(this);
            testFrames[1] = new TestFrame2(this);
            currentTestIndex = 0;
            isInitialized = true;

            display = Display.getDisplay(this);
            nextTest();
        }
    }
    
    public void nextTest() {
        if (currentTestIndex < testFrames.length) {
            System.out.println("Displaying the frame.");
            TestFrame currentTestFrame = testFrames[currentTestIndex++];
            display.setCurrent(currentTestFrame);
            currentTestFrame.start();
        } else {
            this.notifyDestroyed();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
