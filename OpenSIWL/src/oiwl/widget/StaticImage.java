/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oiwl.widget;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

/**
 *
 * @author mjumbewu
 */
public class StaticImage extends StaticWidget {
    private Image m_image;

    public StaticImage(Image img) {
        this.m_image = img;
    }

    public Image getImage() {
        return this.m_image;
    }

    public void draw(Graphics g, int xoff, int yoff, int x, int y, int w, int h) {
        g.drawRegion(m_image, x, y, w, h,
                javax.microedition.lcdui.game.Sprite.TRANS_NONE,
                xoff, yoff, Graphics.TOP | Graphics.LEFT);
    }
}
