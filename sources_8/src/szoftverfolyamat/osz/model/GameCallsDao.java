package szoftverfolyamat.osz.model;

/*
[dao <--- game ctrl] 
*/
public interface GameCallsDao {
    /*
    Folyamatban levo jatszma elmentese.
    */
    void saveGame(Game game);

    /*
    Jatek vegen a pontszam es a jatekos nevenek mentese.
    */    
    void saveScore(int score, String player);

    /*
    IO error, 0 ha OK.
    */
    int errno();
}