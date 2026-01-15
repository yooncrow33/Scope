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
        int i = scopeEngine().system().getCpuPercentage();
        int s = (int)scopeEngine().system().getFreeMemory();
    }

     @Override
     protected void render(Graphics g) {

     }

     public void addPopup() {

    }

    public static void main(String[] args) {
        new Test("test app");
    }
}
