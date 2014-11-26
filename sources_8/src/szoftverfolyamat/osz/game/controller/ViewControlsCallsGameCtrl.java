package szoftverfolyamat.osz.game.controller;

/*
[game ctrl <--- view::controls]
*/

public interface ViewControlsCallsGameCtrl {
    enum Direction {
        NIL, N, S, W, E, NW, NE, SW, SE
    }

    final static int BACK_TO_MAIN_MENU = 0;
    final static int LOAD_GAME = 1;
    final static int SELECT_MISSION = 2;
   
    void abortGame();
    
    void movePlayer(Direction d);
    
    void activate();
    
    void pause();
    
    void levelCompletionConfirmed();
    
    void gameoverConfirmed(int furtherAction); 
    
    void restartLevel();
}
