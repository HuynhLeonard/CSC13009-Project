package org.hstlp.yourmemory.Ultilities;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecoration extends RecyclerView.ItemDecoration {
    private final int space;
    private int spanColumns;
    private boolean isUsed;

    public ItemDecoration(int space, int spanColumns) {
        this.spanColumns = spanColumns;
        this.space = space;
        isUsed = true;
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = space;
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) < spanColumns) {
            outRect.top = space;
        }
        if (parent.getChildLayoutPosition(view) % spanColumns == 0) {
            outRect.left = space;
        }
    }

    public void setSpanColumns(int columns) {
        this.spanColumns = columns;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (isUsed) {
            super.onDraw(c, parent, state);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (isUsed) {
            super.onDrawOver(c, parent, state);
        }
    }

}