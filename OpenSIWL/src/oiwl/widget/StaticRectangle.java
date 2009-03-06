/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Graphics;

/**
 *
 * @author mjumbewu
 */
public class StaticRectangle extends StaticWidget {
    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        g.fillRect(xoff, yoff, this.getWidth(), this.getHeight());
    }
}
