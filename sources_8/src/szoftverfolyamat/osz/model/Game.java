package szoftverfolyamat.osz.model;

/*
Egy elmentett es betoltheto jatszma.
*/
public interface Game {
    /*
    Az hatralevo resze a kuldetesnek.
    */
    Mission getMission();

    /*
    Az aktualis Level szerializalva.
    */    
    Object getCurrentLevel();
}