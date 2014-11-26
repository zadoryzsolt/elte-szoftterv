package szoftverfolyamat.osz.model;

public class MissionImpl implements Mission {
    private final Iterable<Level> al;
    private final String name;
    private final String info;

    public MissionImpl(Iterable<Level> l, String n, String i) {
        al = l;
        name = n;
        info = i;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public Iterable<Level> getLevels() {
        return al;
    }
}