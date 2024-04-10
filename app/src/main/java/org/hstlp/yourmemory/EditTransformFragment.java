package org.hstlp.yourmemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditTransformFragment {
    Context context = null;
    ImageButton verticalBtn;
    ImageButton horizontalBtn;

    public static EditTransformFragment newInstance() {return new EditTransformFragment();}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // check later
        super.onCreate(savedInstanceState);
        try {
            context = getContext(); // use this reference to invoke the onAttachMethod
            main = (EditImage) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }
}
