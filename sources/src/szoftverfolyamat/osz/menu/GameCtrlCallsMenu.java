package szoftverfolyamat.osz.menu;

/*
[menu <--- game ctrl] 
*/
public interface GameCtrlCallsMenu {
    void backToMainMenu();
    
    void loadGame();
    
    void startMission();
    
    void error(String s);
}