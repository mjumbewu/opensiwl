/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import com.sprintpcs.lcdui.widget.*;

import java.util.Vector;

/**
 * @author mjumbewu
 */
public class SIWLExtensionTests extends MIDlet {
    FrameManager frameManager;
    Vector testFrames;
    int currentTestIndex;
    boolean isInitialized = false;

    class MyFrame extends BasicFrame {

    }

    public void startApp() {
        if (!isInitialized) {
            System.out.println("Creating the frames");
            testFrames = new Vector();
            testFrames.addElement(new TestFrame1(this, "1"));
            testFrames.addElement(new TestFrame2(this, "2"));
            testFrames.addElement(new TestFrame3(this, "3"));
            testFrames.addElement(new TestFrame4(this, "4"));
            testFrames.addElement(new TestFrame5(this, "5"));
            currentTestIndex = 0;
            isInitialized = true;

            frameManager = new FrameManager(Display.getDisplay(this));
            nextTest();
        }
    }

    public void nextTest() {
        if (currentTestIndex < testFrames.size()) {
            System.out.println("Displaying the frame.");
            TestFrame currentTestFrame = (TestFrame)testFrames.elementAt(currentTestIndex++);
            frameManager.setCurrent(currentTestFrame);
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
