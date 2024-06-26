package org.hstlp.yourmemory;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContrastEditorFragment extends Fragment {
    static ContrastEditorFragment instance = null;
    EditImage main;
    Context context = null;

    ImageButton backBtn, confirmBtn;
    SeekBar seekBar;
    TextView text;
    Bitmap contrastImageBitmap;

    public static  ContrastEditorFragment makeNewInstance() { //Hàm khởi tạo ínstance cho fragment
        if (instance == null) {
            instance = new ContrastEditorFragment();
        }
        return new ContrastEditorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //Hàm onCreate cho fragment
        super.onCreate(savedInstanceState);
        try {
            context = getContext(); // use this reference to invoke main callbacks
            main = (EditImage) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.contrast_fragment, null);

        seekBar = layout.findViewById(R.id.edit_contrast_amount);
        backBtn = layout.findViewById(R.id.contrast_back_btns);
        backBtn.setOnClickListener(view -> main.BackFragment());
        confirmBtn = layout.findViewById(R.id.contrast_confirm_btns);
        confirmBtn.setOnClickListener(view -> {
            main.ConfirmBlur(contrastImageBitmap);
            main.BackFragment();
        });
        seekBar.setMax(100);
        seekBar.setProgress(0);
        text = layout.findViewById(R.id.edit_contrast_amount_num);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text.setText(String.valueOf(i));
                contrastImageBitmap = main.contrastIMG(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return layout;
    }
}
