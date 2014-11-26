package szoftverfolyamat.osz.model;

/*
Egy betoltheto kuldetes, amelyben az  Iterator  felsorolja az egymas utan 
kovetkezo palyakat.
*/
public interface Mission {
    /*
    A kuldetes neve.
    */    
    String getName();

    /*
    A kuldetes adatai.
    */     
    String getInfo();

    /*
    Felsorolja az egymas utan kovetkezo palyakat. Egyik palya teljesitese
    utan jon a kovetkezo.
    */   
    Iterable<Level> getLevels();
}