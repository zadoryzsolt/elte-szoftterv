package szoftverfolyamat.osz.model;

/*
Forraskod toredek. Minden palya forrasa ilyen toredekekbol all.
*/
public interface SourceFragment {
    /*
    Ha rejtett, akkor a jatekos nem latja.
    */ 
    boolean isVisible();

    /*
    Irasvedett-e.
    */    
    boolean isReadonly();

    /*
    Maga a kod szovege.
    */ 
    String getCode();
    
    int getMaxRows();
}