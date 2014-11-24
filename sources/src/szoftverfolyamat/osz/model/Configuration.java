package szoftverfolyamat.osz.model;

/*
A konfiguracios beallitasokat jelkepezo interface.
Jelenleg a nyelvet, az ablak szelesseget es magassagat lehet 
megadni a getterekkel es a setterekkel. Ezeket az implementacio 
perzisztensen kezeli.
*/
public interface Configuration {    
    public String getLanguage();    
    
    public void setLanguage(String l);  // nyelv megadasa es lekerdezese

    public int getWindowWidth();        // ablak szelesseg megadasa es lekerdezese
    
    public void setWindowWidth(int w);

    public int getWindowHeight();       // ablak magassag megadasa es lekerdezese
    
    public void setWindowHeight(int h);
}
