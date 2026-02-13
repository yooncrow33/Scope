package test;

import java.awt.*;
import scope.Base;

class Test extends Base {

    Key key;


    public Test(String title) {
        super(title);
    }

    @Override
    protected void init() {
        String root = System.getProperty("user.home") + "/SC/sound";
        System.out.println("[DEBUG] sound root = " + root);
        scopeEngine().sound().setExternalRoot(root);
        key = new Key(this,this);

        System.out.println(getTitle());
    }

    @Override
    protected void update(double dt) {
        int i = scopeEngine().system().getCpuPercentage();
        int s = (int)scopeEngine().system().getFreeMemory();
        scopeEngine().effect().AfterImage().addAfterImageOval(0,0,0,0,0,0,0,0,0);
    }

     @Override
     protected void render(Graphics g) {
     }

     public void addPopup() {
         scopeEngine().sound().play("sound.wav");
    }

    public static void main(String[] args) {
        new Test("test app");
    }
}
