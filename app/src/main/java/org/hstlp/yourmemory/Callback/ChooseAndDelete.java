package org.hstlp.yourmemory.Callback;

public interface ChooseAndDelete {
    void deleteClicked();
    void deleteClicked(String file);
    void clearClicked();
    void selectAllClicked();
    void renameClicked(String file, String newFile);
}
