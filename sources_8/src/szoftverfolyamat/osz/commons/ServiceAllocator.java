package szoftverfolyamat.osz.commons;

import java.util.Iterator;
import java.util.ServiceLoader;

/*
A  META-INF/services bejegyzesben szereplo szolgaltatast keres meg.
*/
public class ServiceAllocator {
    /*
    Adott service specification osztaly/interfesz implementaciojanak lekerese.
    */
    public static <T> T allocateService(Class<T> klass) {
        T ret = null;
        ServiceLoader<T> loader = ServiceLoader.load(klass);
        Iterator<T> i = loader.iterator();
        try {
            ret = i.next();
        } catch(Exception e) {}
        return ret;
    }
}
