package szoftverfolyamat.osz.model;

public class LevelImpl implements Level {
    private final String info;
    private final String name;
    private final Iterable<SourceFragment> fragments;
    
    public LevelImpl(String n, String i, Iterable<SourceFragment> f) {
        name = n;
        info = i;
        fragments = f;
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
    public Iterable<SourceFragment> getFragments() {
        return fragments;
    }
}