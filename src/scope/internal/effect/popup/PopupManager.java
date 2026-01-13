package scope.internal.effect.popup;

import scope.internal.tick.ITick;

import java.util.ArrayList;

public final class PopupManager implements PopupAccess {

    ITick iTick;

    public PopupManager(ITick iTick) {
        this.iTick = iTick;
    }

    ArrayList<Popup> popups = new ArrayList<>();

    public void update() {
        for (int i = popups.size() - 1; i >= 0; i--) {
            Popup afterImage = popups.get(i);
            afterImage.update();
            if (afterImage.isExpired()) {
                popups.remove(i);
            }
        }
    }

    public void draw(java.awt.Graphics g) {
        for (Popup afterImage : popups) {
            afterImage.draw(g);
        }
    }

    @Override
    public void addPopup(String firstLine, String secondLine, String thirdLine,String title) {
        popups.add(new Popup(firstLine,secondLine,thirdLine,title, (int)iTick.getTick()));
    }
}