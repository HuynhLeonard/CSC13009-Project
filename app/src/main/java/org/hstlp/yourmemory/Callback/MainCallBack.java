package org.hstlp.yourmemory.Callback;

import java.util.ArrayList;

public interface MainCallBack {
    void setCurrentDirectory(String Dir);
    String getSDDirectory();
    String getCurrentDirectory();
    void pushFolderPath (String inp );
    void popFolderPath();
    ArrayList<String> getFolderPath();
    String getDCIMDirectory();
    String getPictureDirectory();
    ArrayList<String> getFileinDir();
    void removeImageUpdate(String[] input);
}
