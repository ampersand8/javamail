package com.company;

import java.awt.event.*;

/**
 * Created by simon on 4/15/15.
 */
public class MyListener implements ActionListener {
    JavaMail mymail;

    MyListener(JavaMail mymail) {
        this.mymail = mymail;
    }

    public void actionPerformed(ActionEvent e) {
        mymail.fetchMail();
    }
}
