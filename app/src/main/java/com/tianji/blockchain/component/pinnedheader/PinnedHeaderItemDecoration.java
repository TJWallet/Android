package com.tianji.blockchain.component.pinnedheader;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class PinnedHeaderItemDecoration extends RecyclerView.ItemDecoration implements IPinnedHeaderDecoration {
    private Rect mPinnedHeaderRect = null;
    private int  mPinnedHeaderPosition = -1;

    @Override
    public void onDrawOver(Canvas mCanvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(mCanvas, parent, state);
        if (parent.getAdapter() instanceof PinnedHeaderAdapter && parent.getChildCount() > 0) {
            PinnedHeaderAdapter adapter = (PinnedHeaderAdapter) parent.getAdapter();
            View firstView = parent.getChildAt(0);
            int firstAdapterPosition = parent.getChildAdapterPosition(firstView);
            int pinnedHeaderPosition = getPinnedHeaderViewPosition(firstAdapterPosition, adapter);
            mPinnedHeaderPosition = pinnedHeaderPosition;
            if (pinnedHeaderPosition != -1) {
                RecyclerView.ViewHolder pinnedHeaderViewHolder = adapter.onCreateViewHolder(parent, adapter.getItemViewType(pinnedHeaderPosition));
                adapter.onBindViewHolder(pinnedHeaderViewHolder, pinnedHeaderPosition);
                View pinnedHeaderView = pinnedHeaderViewHolder.itemView;
                ensurePinnedHeaderViewLayout(pinnedHeaderView, parent);
                int sectionPinOffset = 0;
                for (int index = 0; index < parent.getChildCount(); index++) {
                    if (adapter.isPinnedPosition(parent.getChildAdapterPosition(parent.getChildAt(index)))) {
                        View sectionView = parent.getChildAt(index);
                        int sectionTop = sectionView.getTop();
                        int pinViewHeight = pinnedHeaderView.getHeight();
                        if (sectionTop < pinViewHeight && sectionTop > 0) {
                            sectionPinOffset = sectionTop - pinViewHeight;
                        }
                    }
                }
                int saveCount = mCanvas.save();
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) pinnedHeaderView.getLayoutParams();
                if (layoutParams == null) {
                    throw new NullPointerException("PinnedHeaderItemDecoration");
                }
                mCanvas.translate(layoutParams.leftMargin, sectionPinOffset);
                mCanvas.clipRect(0, 0, parent.getWidth(), pinnedHeaderView.getMeasuredHeight());
                pinnedHeaderView.draw(mCanvas);
                mCanvas.restoreToCount(saveCount);
                if (mPinnedHeaderRect == null) {
                    mPinnedHeaderRect = new Rect();
                }
                mPinnedHeaderRect.set(0, 0, parent.getWidth(), pinnedHeaderView.getMeasuredHeight() + sectionPinOffset);
            } else {
                mPinnedHeaderRect = null;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    }

    private int getPinnedHeaderViewPosition(int adapterFirstVisible, PinnedHeaderAdapter adapter) {
        for (int index = adapterFirstVisible; index >= 0; index--) {
            if (adapter.isPinnedPosition(index)) {
                return index;
            }
        }
        return -1;
    }

    private void ensurePinnedHeaderViewLayout(View pinView, RecyclerView recyclerView) {
        if (pinView.isLayoutRequested()) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) pinView.getLayoutParams();
            if (layoutParams == null) {
                throw new NullPointerException("PinnedHeaderItemDecoration");
            }
            int widthSpec = View.MeasureSpec.makeMeasureSpec(recyclerView.getMeasuredWidth() - layoutParams.leftMargin - layoutParams.rightMargin, View.MeasureSpec.EXACTLY);
            int heightSpec;
            if (layoutParams.height > 0) {
                heightSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY);
            } else {
                heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            }
            pinView.measure(widthSpec, heightSpec);
            pinView.layout(0, 0, pinView.getMeasuredWidth(), pinView.getMeasuredHeight());
        }
    }

    @Override
    public Rect getPinnedHeaderRect() {
        return mPinnedHeaderRect;
    }

    @Override
    public int getPinnedHeaderPosition() {
        return mPinnedHeaderPosition;
    }
}
