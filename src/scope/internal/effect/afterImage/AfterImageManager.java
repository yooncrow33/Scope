package scope.internal.effect.afterImage;

import java.util.ArrayList;

public final class AfterImageManager implements AfterImageAccess {

    ArrayList <AfterImageRect> afterImageRects = new ArrayList<>();
    ArrayList <AfterImageOval> afterImageOvals = new ArrayList<>();

    public void update() {
        for (int i = afterImageRects.size() - 1; i >= 0; i--) {
           AfterImageRect afterImage = afterImageRects.get(i);
            afterImage.update();
            if (afterImage.isExpired()) {
                afterImageRects.remove(i);
            }
        }

        for (int i = afterImageOvals.size() - 1; i >= 0; i--) {
            AfterImageOval afterImage = afterImageOvals.get(i);
            afterImage.update();
            if (afterImage.isExpired()) {
                afterImageOvals.remove(i);
            }
        }
    }

    public void draw(java.awt.Graphics g) {
        for (AfterImageRect afterImage : afterImageRects) {
            afterImage.draw(g);
        }

        for (AfterImageOval ai : afterImageOvals) {
            ai.draw(g);
        }
    }

    @Override
    public void adeAfterImageRect(int x, int y, int r, int g, int b, int width, int height, int alpha, int alphaDegree) {
        afterImageRects.add(new AfterImageRect(x,y,r,g,b,width,height,alpha,alphaDegree));
    }

    @Override
    public void addAfterImageOval(int x, int y, int r, int g, int b, int width, int height, int alpha, int alphaDegree) {
        afterImageOvals.add(new AfterImageOval(x,y,r,g,b,width,height,alpha,alphaDegree));
    }
}
