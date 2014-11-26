package szoftverfolyamat.osz.game.controller;

import szoftverfolyamat.osz.model.Mission;
import szoftverfolyamat.osz.model.Game;
import szoftverfolyamat.osz.menu.GameCtrlCallsMenu;

/*
[game ctrl <--- menu]
*/
public interface MenuCallsGameCtrl {
    void exitProgram();
    
    void pause();
    
    void resume();
    
    void start(Mission m, String playerName, GameCtrlCallsMenu cbi);
    
    void start(Game m, String playerName, GameCtrlCallsMenu cbi);
}
