package szoftverfolyamat.osz.game.view;

public interface Sprite {
    void moveTo(double x, double y);
    
    void moveBy(double dx, double dy);
    
    void setImage(int i);
}