package szoftverfolyamat.osz.game.controller.sprites;

public interface GlyphSprite {
    String getFontName();
    
    String getText();
    
    boolean isBackgroundTransparent();
    
    int getBackgroundColor();
}
