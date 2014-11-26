package szoftverfolyamat.osz.model;

/*
Egy elmentett jatek bejegyzese.  Eloszor csak ezek lesznek betoltve, a jatekos
ezek kozul valaszt, aztan az  id  alapjan betolti a tenyleges  Game -et.
*/
public interface GameStub {
    /*
    Az elmento jatekos neve.
    */ 
    String getPlayerName();

    /*
    Az elmento jatekos aktualis pontszama.
    */     
    int getScore();

    /*
    Opcionalis megjegyzesek.
    */     
    String getNote();

    /*
    A mentes datuma.
    */     
    String getDate();

    /*
    Az elmento jatekos neve.
    */     
    long getId();
}