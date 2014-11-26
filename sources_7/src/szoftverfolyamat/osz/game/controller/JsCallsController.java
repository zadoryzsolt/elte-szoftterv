package szoftverfolyamat.osz.game.controller;

/*
A  binaries/data/level*.xml  fajlokban vannak a palyak javascript kodjai; ezeket
tolti be a program inditaskor. Alapertelmezetten az elso Mission indul el, lasd
missions.properties fajl. Ezek a fajlok szerkeszthetok.
*/
public interface JsCallsController {
    /*
    Inicializalja a palyat. legeloszor ezt kell meghivni.
    */
    void initialize(int width, int height, int bgColor);
    
    /*
    Elhelyezi a jatekost a palyan.  
    w: objektum szelesseg
    h: objektum magassaga
    x: objektum bal felso sarkanak  x  koordinataja
    y: objektum bal felso sarkanak  y  koordinataja
    */
    void setPlayer(int x, int y, int w, int h);
    
    /*
    Fal-elemet (~teglat) rak ki a palyara.  
    klass: YELLOW_BRICK, RED_BRICK, DARK_BRICK, STONE_BRICK  valamelyike
    w: objektum szelesseg
    h: objektum magassaga
    x: objektum bal felso sarkanak  x  koordinataja
    y: objektum bal felso sarkanak  y  koordinataja
    */  
    void createWall(String klass, int x, int y, int w, int h);
    
    /*
    Szamitogepet hoz letre a palyan.  
    w: objektum szelesseg
    h: objektum magassaga
    x: objektum bal felso sarkanak  x  koordinataja
    y: objektum bal felso sarkanak  y  koordinataja
    */
    void createComputer(int x, int y, int w, int h);
    
    /*
    Kijaratot hoz letre a palyan.  
    w: objektum szelesseg
    h: objektum magassaga
    x: objektum bal felso sarkanak  x  koordinataja
    y: objektum bal felso sarkanak  y  koordinataja
    */
    void createExit(int x, int y, int w, int h);
    
    /*
    Mentesi pontot hoz letre a palyan. A mentes meg nem elerheto.
    w: objektum szelesseg
    h: objektum magassaga
    x: objektum bal felso sarkanak  x  koordinataja
    y: objektum bal felso sarkanak  y  koordinataja
    */
    void createSavePoint(int x, int y, int w, int h);
    
    /*
    Mozgo szellemet hoz letre a palyan.  
    w: objektum szelesseg
    h: objektum magassaga
    psx: a szellem altal bejart pontok  x  koordinatai
    psy: a szellem altal bejart pontok  y  koordinatai
    speed: mozgas gyorsasaga (1 gyors, 10 lassu) 
    */
    void createGhost(int w, int h, int pxs[], int pys[], int speed);
    
    /*
    Csapdat hoz letre a palyan. Ha ralep a jatekos, akkor meghal.
    w: objektum szelesseg
    h: objektum magassaga
    x: objektum bal felso sarkanak  x  koordinataja
    y: objektum bal felso sarkanak  y  koordinataja
    */
    void createTrap(int x, int y, int w, int h);
    
    /*
    Nincs implementalva.
    */
    void registerTickHandler(Object o);
    
    /*
    Nincs implementalva.
    */
    void registerCollisionHandler(Object o);
    
}


