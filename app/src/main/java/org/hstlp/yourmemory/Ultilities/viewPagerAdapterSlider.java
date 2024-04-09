package org.hstlp.yourmemory.Ultilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.hstlp.yourmemory.R;
import org.hstlp.yourmemory.SelectedViewPagerItem;

import java.util.ArrayList;
public class viewPagerAdapterSlider extends RecyclerView.Adapter<viewPagerAdapterSlider.ViewHolder> {
    ArrayList<SelectedViewPagerItem> arrayItems;
    SlideShow main;

    public viewPagerAdapterSlider(ArrayList<SelectedViewPagerItem> arrayItems, SlideShow main) {
        this.main = main;
        this.arrayItems = arrayItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_picture, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SelectedViewPagerItem item = arrayItems.get(position);
        holder.img.setImageBitmap(item.getItemBitmap());
    }


    @Override
    public int getItemCount() {
        return arrayItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.sliderImage);
        }
    }
}
