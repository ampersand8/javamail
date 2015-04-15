package com.company;

import java.awt.event.*;

/**
 * Created by simon on 4/15/15.
 */
public class MyListener implements ActionListener {
    Javamail mymail;

    MyListener(Javamail mymail) {
        this.mymail = mymail;
    }

    public void actionPerformed(ActionEvent e) {
        mymail.fetch_mail();
    }
}
