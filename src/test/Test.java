package test;

import java.awt.*;
import scope.Base;

public class Test extends Base {

    Key key;

    public Test(String title) {
        super(title);
    }

    @Override
    protected void init() {
        key = new Key(this,this);
    }

    @Override
    protected void update(double dt) {
    }

     @Override
     protected void render(Graphics g) {

     }

     public void addPopup() {
         getPopupManager().addPopup("1","2","3","title");
     }

    public static void main(String[] args) {
        new Test("test app");
    }
}
