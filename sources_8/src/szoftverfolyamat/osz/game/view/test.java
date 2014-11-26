package szoftverfolyamat.osz.game.view;

import java.util.ArrayList;
import szoftverfolyamat.osz.model.SourceFragment;

class TestSource {
    Iterable<SourceFragment> getSource() {
        ArrayList<SourceFragment> al = new ArrayList<SourceFragment>();
        al.add(new SourceFragment() {
                   public boolean isVisible() {
                       return false;
                   }

                   public boolean isReadonly() {
                       return true;
                   }

                   public String getCode() {
                       return "Elso sor; rejtett";
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
                       return "Masodik sor; readonly";
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
                       return "Harmadik sor; irhato";
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
                       return "Negyedik sor; readonly";
                   }

                   public  int getMaxRows() {
                       return 8;
                   }
               }
              );
        return al;
    }
}
