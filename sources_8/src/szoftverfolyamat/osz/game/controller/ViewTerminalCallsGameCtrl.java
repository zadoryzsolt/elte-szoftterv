package szoftverfolyamat.osz.game.controller;

/*
[game ctrl <--- view::terminal]
*/

public interface ViewTerminalCallsGameCtrl {
    void executeCode(String code); 
    
     void exitTerminal();
}
