/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sprintpcs.lcdui.widget;

/**
 *
 * @author mjumbewu
 */
public class Alignment {
    /**
     * Constant for left horizontal alignment
     */
    public static int LEFT = 1;
    
    /**
     * Constant for center horizontal alignment
     */
    public static int HCENTER = 2;
    
    /**
     * Constant for right horizontal alignment
     */
    public static int RIGHT = 4;

    /**
     * Constant for top vertical alignment
     */
    public static int TOP = 8;
    
    /**
     * Constant for center vertical alignment
     */
    public static int VCENTER = 16;
    
    /**
     * Constant for bottom vertical alignment
     */
    public static int BOTTOM = 32;
    
    /**
     * Orientation-agnostic minimum alignment
     */
    public static int MIN = 64;
    
    /**
     * Orientation-agnostic center alignment
     */
    public static int CENTER = 128;
    
    /**
     * Orientation-agnostic maximum alignment
     */
    public static int MAX = 256;

    static int HCOMPONENT = 
            Alignment.LEFT|Alignment.HCENTER|Alignment.RIGHT;
    static int VCOMPONENT = 
            Alignment.TOP|Alignment.VCENTER|Alignment.BOTTOM;
}
