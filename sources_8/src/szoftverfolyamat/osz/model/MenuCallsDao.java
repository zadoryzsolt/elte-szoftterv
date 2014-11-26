package szoftverfolyamat.osz.model;

/*
[dao <--- menu]
*/
public interface MenuCallsDao {
    /*
    Az elerheto Mission-ok listazasa.
    */
    Iterable<Mission> getMissions();

    /*
    Legmagasabb pontszamokat listazza ki.
    */
    String[][] getHighScores();

    /*
    Mentett jatszmak listajanak lekerese; ha a  player==null, akkor az osszeset
    kilistazza, kulonben csak az adott nevu jatekoset.
    */
    <Iterable>GameStub getSavedGames(String player);

    /*
    Adott azonositoju jatszma betoltese.
    */    
    Game loadGame(long gameId);

    /*
    IO error, 0 ha OK.
    */
    int errno();
}