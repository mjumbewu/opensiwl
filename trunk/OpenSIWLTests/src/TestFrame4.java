/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import oiwl.widget.*;

/**
 *
 * @author mjumbewu
 */
public class TestFrame4 extends TestFrame implements EventListener {
    LinearLayout layout = new LinearLayout(Orientation.VERTICAL);
    StaticText prompt = new StaticText("Tap the button!");

    PushButton button = new PushButton();
    
    public TestFrame4(OpenSIWLTests app, String id, int orient) {
        super(app, id, orient);
        this.setLayout(layout);
        
        this.instructions = "This test ensures that the PushButton object will" +
                "correctly send tap events.  It consists of a StaticText item " +
                "prompting to push the button, and a button to push.  When the" +
                "button is tapped, the prompt should change.";

        layout.manage(prompt);

        button.setLabel(new StaticText("Press Me!"));
        button.addEventListener(this);
        layout.manage(button);
    }
    
    protected boolean runtest() {
        button.tap();
        String text = prompt.getText();
        
        // Check that we couldn't paint when we weren't supposed to and we
        // could when we were.
        return (text.equals("You tapped the button!"));
    }

    public void onEvent(int type, Widget sender, Object data) {
        if (type == PushButton.TAPPED_EVENT && sender == this.button) {
            int prompt_index = layout.getIndexOf(prompt);
            layout.unmanage(prompt);
            prompt = new StaticText("You tapped the button!");
            layout.manage(prompt, prompt_index, 0, true, false);
        }
        
        else if (type == PushButton.DTAPPED_EVENT && sender == this.button) {
            int prompt_index = layout.getIndexOf(prompt);
            layout.unmanage(prompt);
            String str = prompt.getText();
            prompt = new StaticText(str + "!");
            layout.manage(prompt, prompt_index, 0, true, false);
        }
    }
}
