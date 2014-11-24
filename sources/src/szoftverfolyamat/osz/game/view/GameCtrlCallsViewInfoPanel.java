package szoftverfolyamat.osz.game.view;

/*
[view::info panel <--- game ctrl] 
*/
public interface GameCtrlCallsViewInfoPanel {
    void setPlayerInfo(String s);
    
    void setLevelInfo(String s);
    
    void setTime(String hour, String min, String sec);
    
    void setScore(String s);
    
    void gameSaved(String s);
    
    void runtimeError(String s);
    
    void ping();
}
