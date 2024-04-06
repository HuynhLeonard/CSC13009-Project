package org.hstlp.yourmemory.Callback;

public interface IsSelectedPicture {
    void preventSwipe();
    void allowSwipe();
    void setCurrentSelectedName(String name);
    void setCurrentPosition(int pos);
    void removeImageUpdate(String input);
    void showNav();
    void hiddenNav();
    void notifyChanged();
}
