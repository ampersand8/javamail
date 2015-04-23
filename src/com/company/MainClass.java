package com.company;

import javax.swing.JFrame;

/**
 * Created by simon on 4/6/15.
 */

public class MainClass {
    private static final JFrame frame = new JFrame("xMail");

    public static void main(String[] args) {
        frame.addWindowListener(new JavaMail(frame));
    }
}