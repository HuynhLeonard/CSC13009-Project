package org.hstlp.yourmemory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class EditFilterFragment extends Fragment{
    @SuppressLint("StaticFieldLeak")
    static EditFilterFragment instance = null;
    EditImage main;
    Context context = null;
    String[] listName;
    ArrayList<Bitmap> listImage;
    LinearLayout filterList;
    ImageButton backBtn, confirmBtn;
    Bitmap chooseFilter = null;

    EditFilterFragment(String[] listName, ArrayList<Bitmap> listImage){
        this.listName = listName;
        this.listImage = listImage;
    }

    public static EditFilterFragment newInstance(String[] listName, ArrayList<Bitmap> listImage){
        if(instance == null){
            instance = new EditFilterFragment(listName, listImage);
        }
        return new EditFilterFragment(listName, listImage);
        //Tạo một đối tượng mới nhưng chỉ trả về đối tượng mới nếu không có đối tượng nào tồn tại trước đó
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getContext(); // để context có thể truy cập Activity mà Fragment đang gắn vào
            main = (EditImage) getActivity();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") LinearLayout layout =(LinearLayout) inflater.inflate(R.layout.edit_filter_fragment, null);
        //Tạo giao diện từ layout này vào các biến
        filterList = layout.findViewById(R.id.filterList);
        backBtn = layout.findViewById(R.id.backBtns);
        confirmBtn = layout.findViewById((R.id.confirmBtns));

        backBtn.setOnClickListener(view -> main.BackFragment());

        confirmBtn.setOnClickListener(view ->{
            if(chooseFilter != null)
                main.ConfirmBlur(chooseFilter);
            main.BackFragment();
        });

        for(int i = 0; i < listImage.size(); i++) { //Duyệt qua các listImage và thực hiện gán tên và hình ảnh vào các phần tử và cập nhật theo bộ lọc filter
            @SuppressLint("InlfateParams") View frame = getLayoutInflater().inflate(R.layout.edit_filter_list_item, null);
            TextView caption = frame.findViewById(R.id.filter_name);
            ImageView icon = frame.findViewById(R.id.filter_view);
            caption.setText(listName[i]);
            icon.setImageBitmap(listImage.get(i));
            int finalI = i;
            frame.setOnClickListener(view -> {
                main.BitmapFilterChoose(listImage.get(finalI), listName[finalI]);
                chooseFilter = listImage.get(finalI);
            });
            filterList.addView(frame);
        }

        return layout;
    }
}
