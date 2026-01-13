package test;

import scope.KeyBindingBase;

import javax.swing.*;

public class Key extends KeyBindingBase {
    Test test;
    protected Key(JComponent comp, Test test) {
        super(comp);
        this.test = test;
    }

    @Override
    protected void onKeyPPress() {
        test.addPopup();
    }
}
