package szoftverfolyamat.osz.model;

public class DaoForMenu implements MenuCallsDao {
    private int _errno;

    public DaoForMenu() {
        _errno = 0;
    }

    @Override
    public Iterable<Mission> getMissions() {
        MissionsReader mr = new MissionsReader();
        _errno = mr.errno;
        return mr.getMissions();
    }

    @Override
    public String[][] getHighScores() {
        return null;
    }

    @Override
    public <Iterable>GameStub getSavedGames(String player) {
        return null;
    }

    @Override
    public Game loadGame(long gameId) {
        return null;
    }

    @Override
    public int errno() {
        return _errno;
    }
}