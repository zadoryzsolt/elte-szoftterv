package szoftverfolyamat.osz.menu;

import szoftverfolyamat.osz.model.Level;
import szoftverfolyamat.osz.model.SourceFragment;
import szoftverfolyamat.osz.model.Mission;

import java.util.Vector;

public class Tester implements GameCtrlCallsMenu {
    @Override
    public void backToMainMenu() {
        System.exit(0);
    }

    @Override
    public void loadGame() {
        System.err.println("Exiting instead of loading a game.");
        System.exit(0);
    }

    @Override
    public void startMission() {
        System.err.println("Exiting instead of starting new game.");
        System.exit(0);
    }

    @Override
    public void error(String s) {
        System.err.println(s);
        System.exit(-1);
    }

    public static class TestMission implements Mission {
        private Vector<Level> al;

        public TestMission() {
            al = new Vector<Level>();
            al.add(new TestLevel_1());
            al.add(new TestLevel_2());
        }

        @Override
        public String getName() {
            return "Test mission";
        }

        @Override
        public String getInfo() {
            return "Test mission info";
        }

        @Override
        public Iterable<Level> getLevels() {
            return al;
        }
    }

    public static class TestLevel_1 implements Level {
        @Override
        public String getName() {
            return "Test level #1";
        }

        @Override
        public String getInfo() {
            return "Test level #1 info";
        }

        @Override
        public Iterable<SourceFragment> getFragments() {
            Vector<SourceFragment> al = new Vector<SourceFragment>();
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return false;
                       }

                       public boolean isReadonly() {
                           return true;
                       }

                       public String getCode() {
                           return "// test Elso sor; rejtett\n" +
                                  "level.initialize(700, 500, 0x383330);\n";
                       }

                       public  int getMaxRows() {
                           return 2;
                       }
                   }
                  );
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return true;
                       }

                       public boolean isReadonly() {
                           return false;
                       }

                       public String getCode() {
                           return "// test Masodik sor; irhato\n" +
                                  "var w = 40; \nvar h = 40;\n" +
                                  "for(var i=0; i<6; i++) { \n" +
                                  "   level.createWall(\"RED_BRICK\", \n" +
                                  "                    180+i*w, 200, w, h);\n" +
                                  "}\n" +
                                  "for(var i=0; i<6; i++) { \n" +
                                  "   level.createWall(\"RED_BRICK\", \n" +
                                  "                    180, 200 + h + i*h, w, h);\n" +
                                  "}\n";
                       }

                       public  int getMaxRows() {
                           return 10;
                       }
                   }
                  );
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return true;
                       }

                       public boolean isReadonly() {
                           return true;
                       }

                       public String getCode() {
                           return "// test Harmadik sor; nem irhato\n" +
                                  "level.setPlayer(450, 200, 50, 72);\n" +
                                  "level.createComputer(250, 250, 55, 55);\n" +
                                  "level.createSavePoint(400, 400, 55, 55);\n" +
                                  "level.createGhost(500, 400, 75, 75);\n" +
                                  "level.createExit(40, 40, 100, 50);\n";
                       }

                       public  int getMaxRows() {
                           return 4;
                       }
                   }
                  );
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return true;
                       }

                       public boolean isReadonly() {
                           return true;
                       }

                       public String getCode() {
                           return "// test Negyedik sor; new irhato \n" +
                                  "for(var i=0; i<35; i++) {\n" +
                                  "    level.createWall(\"STONE_BRICK\", \n" +
                                  "                     i*20, 0, 20, 20); \n" +
                                  "    level.createWall(\"STONE_BRICK\", \n" +
                                  "                     i*20, 480, 20, 20); \n" +
                                  "} \n" +
                                  "for(var i=1; i<24; i++) {\n" +
                                  "    level.createWall(\"STONE_BRICK\", \n" +
                                  "                     0, i*20, 20, 20); \n" +
                                  "    level.createWall(\"STONE_BRICK\", \n" +
                                  "                     680, i*20, 20, 20); \n" +
                                  "} \n";
                       }

                       public  int getMaxRows() {
                           return 15;
                       }
                   }
                  );
            return al;
        }
    }

    public static class TestLevel_2 implements Level {
        @Override
        public String getName() {
            return "Test level #2";
        }

        @Override
        public String getInfo() {
            return "Test level #2 info";
        }

        @Override
        public Iterable<SourceFragment> getFragments() {
            Vector<SourceFragment> al = new Vector<SourceFragment>();
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return false;
                       }

                       public boolean isReadonly() {
                           return true;
                       }

                       public String getCode() {
                           return "// test Elso sor; rejtett\n" +
                                  "level.initialize(700, 500, 0x383330);\n";
                       }

                       public  int getMaxRows() {
                           return 2;
                       }
                   }
                  );
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return true;
                       }

                       public boolean isReadonly() {
                           return true;
                       }

                       public String getCode() {
                           return  "//test2 Masodik sor; irhato";
                       }

                       public  int getMaxRows() {
                           return 7;
                       }
                   }
                  );
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return true;
                       }

                       public boolean isReadonly() {
                           return false;
                       }

                       public String getCode() {
                           return "// test Harmadik sor; nem irhato\n" +
                                  "level.setPlayer(450, 200, 50, 72);\n" +
                                  "level.createComputer(250, 250, 55, 55);\n" +
                                  "level.createSavePoint(400, 400, 55, 55);\n" +
                                  "level.createGhost(500, 400, 75, 75);\n" +
                                  "level.createExit(40, 40, 100, 50);\n";
                       }

                       public  int getMaxRows() {
                           return 3;
                       }
                   }
                  );
            al.add(new SourceFragment() {
                       public boolean isVisible() {
                           return true;
                       }

                       public boolean isReadonly() {
                           return true;
                       }

                       public String getCode() {
                           return "//test 2 Negyedik sor; readonly";
                       }

                       public  int getMaxRows() {
                           return 8;
                       }
                   }
                  );
            return al;
        }
    }
}