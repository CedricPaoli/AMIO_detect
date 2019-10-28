package com.example.amio_detect.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amio_detect.R;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    SimpleDividerItemDecoration(Context context) {
        mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left + 30, top, right - 30, bottom);
            mDivider.draw(c);
        }
    }

    public void getItemOffsets(Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = 50;
        outRect.right = 50;
    }
}