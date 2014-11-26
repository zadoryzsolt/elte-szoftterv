package szoftverfolyamat.osz.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

class MissionsReader extends PropertiesReader {
    private final Vector<Mission> missions;

    public MissionsReader() {
        super();
        missions = new Vector<Mission>();
        path = USER_DIR + SEPARATOR + "data" +
               SEPARATOR + "missions.properties";
        try {
            final Iterable<String> mna = loadProperties().stringPropertyNames();
            if(errno!=OK) {
                throw new Exception("error parsing missions");
            }
            LevelsReader levelsReader = new LevelsReader();
            if(levelsReader.errno!=0) {
                throw new Exception("error parsing levels");
            }
            for(String mn : mna) {
                final String ls = getStringProperty(mn);
                String[] lna = ls.split(",");
                Vector<Level> levels = new Vector<Level>();
                for(String s : lna) {
                    s = s.trim();
                    levels.add(levelsReader.getLevel(s));
                }
                missions.add(new MissionImpl(levels, "", ""));
            }
        } catch(Exception e) {
            e.printStackTrace();
            errno = -1;
        }
    }

    Iterable<Mission> getMissions() {
        return missions;
    }
}

//------------------------------------------------------------------------------

class LevelsReader {
    public int errno;
    private DocumentBuilderFactory dbf;

    public LevelsReader() {
        errno = 0;
        dbf = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setCoalescing(false);
        } catch(Exception e) {
            errno = -1;
            e.printStackTrace();
        }
    }

    public Level getLevel(String id) {
        Level ret = null;
        try {
            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document doc =
                db.parse(new File(PropertiesReader.USER_DIR +
                                  PropertiesReader.SEPARATOR + "data" +
                                  PropertiesReader.SEPARATOR + id + ".xml"));
            Element root = doc.getDocumentElement();
            if(!(root.getTagName().equals("level"))) {
                throw new Exception("no <level> root node");
            }
            final String levelName = root.getAttribute("name");
            final Vector<SourceFragment> fragments = new Vector<SourceFragment>();
            Node node = root.getFirstChild().getNextSibling();
            while(node!=null) {
                final String nn = node.getNodeName();
                final String tc =
                    stripLeadingAndTrailingNewlines(node.getTextContent());
                final int lines =
                    tc.split(System.getProperty("line.separator")).length;
                if(nn.equals("invisible")) {
                    fragments.add(
                        new SourceFragment() {
                            @Override
                            public  boolean isVisible() {
                                return false;
                            }

                            @Override
                            public boolean isReadonly() {
                                return true;
                            }

                            @Override
                            public String getCode() {
                                return tc;
                            }

                            @Override
                            public int getMaxRows() {
                                return lines;
                            }
                        }
                    );
                } else if(nn.equals("readonly")) {
                    fragments.add(
                        new SourceFragment() {
                            @Override
                            public  boolean isVisible() {
                                return true;
                            }

                            @Override
                            public boolean isReadonly() {
                                return true;
                            }

                            @Override
                            public String getCode() {
                                return tc;
                            }

                            @Override
                            public int getMaxRows() {
                                return lines;
                            }
                        }
                    );
                } else if(nn.equals("editable")) {
                    fragments.add(
                        new SourceFragment() {
                            @Override
                            public  boolean isVisible() {
                                return true;
                            }

                            @Override
                            public boolean isReadonly() {
                                return false;
                            }

                            @Override
                            public String getCode() {
                                return tc;
                            }

                            @Override
                            public int getMaxRows() {
                                return lines;
                            }
                        }
                    );
                } else {
                    throw new Exception("bad children of <level> root node");
                }
                node = node.getNextSibling();
                if(node!=null) {
                    node = node.getNextSibling();
                }
            }
            ret = new LevelImpl(levelName, "", fragments);
        } catch(Exception e) {
            errno = -2;
            e.printStackTrace();
        }
        return ret;
    }

    private String stripLeadingAndTrailingNewlines(String s) {
        return s;
    }
}

//------------------------------------------------------------------------------

class PropertiesReader {
    public static final int OK = 0;
    public static final int FILE_NOT_FOUND = -1;
    public static final int FILE_NOT_WRITABLE = -2;
    public static final int FILE_NOT_READABLE = -3;
    public static final int UNSPECIFIED_ERROR = -111;
    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String USER_DIR = System.getProperty("user.dir");

    public int errno;
    protected String path;

    public PropertiesReader() {
        errno = OK;
    }

    protected Properties loadProperties() {
        Properties ret = null;
        try {
            ret = new Properties();
            ret.load(new FileInputStream(path));
        } catch (Exception e) {
            errno = UNSPECIFIED_ERROR;
        }
        return ret;
    }

    protected void writeProperties(Properties prop) {
        // in ISO 8859-1
        try {
            prop.store(new FileOutputStream(path), "");
        } catch (Exception e) {
            errno = UNSPECIFIED_ERROR;
        }
    }

    protected String getStringProperty(String key) {
        String s = null;
        try {
            Properties prop = loadProperties();
            s = prop.getProperty(key);
            errno = OK;
        } catch (Exception e) {
            errno = UNSPECIFIED_ERROR;
        }
        return s;
    }

    protected void writeStringProperty(String key, String s) {
        Properties prop = loadProperties();
        try {
            prop.setProperty(key, s);
            writeProperties(prop);
        } catch (Exception e) {
            errno = UNSPECIFIED_ERROR;
        }
    }

    protected int getIntProperty(String key) {
        int ret = UNSPECIFIED_ERROR;
        String s = getStringProperty(key);
        if(s!=null) {
            ret  = new Integer(s).intValue();
        } else {
            errno = UNSPECIFIED_ERROR;
        }
        return ret;
    }

    protected void writeIntProperty(String key, int t) {
        writeStringProperty(key, String.valueOf(t));
    }
}
