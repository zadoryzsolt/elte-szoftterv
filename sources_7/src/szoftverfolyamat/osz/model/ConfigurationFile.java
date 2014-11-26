package szoftverfolyamat.osz.model;

import java.io.File;

public class ConfigurationFile extends PropertiesReader
    implements Configuration {

    public ConfigurationFile() {
        super();
        path = USER_DIR + SEPARATOR + "data" + SEPARATOR +
               "config.properties";
        checkConfigurationFile(path);
        if(errno!=OK) {
            System.out.println("cfg file err");
        }
    }

    protected void checkConfigurationFile(String p) {
        int e = OK;
        try {
            File file = new File(path);
            if(!file.exists()) {
                errno = FILE_NOT_FOUND;
            } else if(!file.canWrite()) {
                errno = FILE_NOT_WRITABLE;
            } else if(!file.canRead()) {
                errno = FILE_NOT_READABLE;
            }
        } catch (Exception ex) {
            e = UNSPECIFIED_ERROR;
        }
        errno = e;
    }

    // locale
    public String getLanguage() {
        return getStringProperty("locale");
    }

    public void setLanguage(String l) {
        writeStringProperty("locale", l);
    }

    // widow width
    public int getWindowWidth() {
        return getIntProperty("window.width");
    }

    public void setWindowWidth(int w) {
        writeIntProperty("window.width", w);
    }

    // window height
    public int getWindowHeight() {
        return getIntProperty("window.height");
    }

    public void setWindowHeight(int h) {
        writeIntProperty("window.height", h);
    }
}
