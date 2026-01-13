package scope.internal.effect.afterImage;

public interface AfterImageAccess {
    void adeAfterImageRect(int x, int y, int r, int g, int b, int width, int height, int alpha, int alphaDegree);
    void addAfterImageOval(int x, int y, int r, int g, int b, int width, int height, int alpha, int alphaDegree);
}
