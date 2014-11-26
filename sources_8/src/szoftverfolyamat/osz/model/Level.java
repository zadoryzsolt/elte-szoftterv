package szoftverfolyamat.osz.model;

/*
Egy palyat reprezentalo interface; a tenyleges palyat a palya-programozo kesziti el.
*/
public interface Level {
    /*
    A palya neve.
    */
    String getName();

    /*
    A palya adatai.
    */    
    String getInfo();

    /*
    A forraskod-toredekek sorozata.
    */      
    Iterable<SourceFragment> getFragments();
}