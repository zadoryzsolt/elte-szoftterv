package szoftverfolyamat.osz.game.view;

import szoftverfolyamat.osz.model.SourceFragment;

/*
[view::lifecycle <--- game ctrl] 
*/
public interface GameCtrlCallsViewLifecycle {
    void enterTerminal();
    
    void exitTerminal();
    
    void exit();
    
    void pause();
    
    void resume();
    
    void defeat(String s);
    
    void victory(String s);
    
    void levelCompleted(String s);
    
    void gameAborted();
    
    void setSource(Iterable<SourceFragment> src);
    
    void playerDied(String s);
}