package com.tianji.blockchain.adapter.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.recyclerview.widget.RecyclerView.*;

/** author:jason **/
public class SpaceItemDecoration extends ItemDecoration {
    public static final int LINEARLAYOUT = 0;
    public static final int GRIDLAYOUT = 1;
    public static final int STAGGEREDGRIDLAYOUT = 2;

    @IntDef({LINEARLAYOUT, GRIDLAYOUT,STAGGEREDGRIDLAYOUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LayoutManager {
        public int type() default LINEARLAYOUT;
    }


    private int leftRight;
    private int topBottom;
    private int headItemCount;
    private int space;
    private boolean includeEdge;
    private int spanCount;

    private @LayoutManager int layoutManager;

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param leftRight
     * @param topBottom
     * @param headItemCount
     * @param layoutManager
     */
    public SpaceItemDecoration(int leftRight, int topBottom, int headItemCount, @LayoutManager int layoutManager) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        this.headItemCount = headItemCount;
        this.layoutManager = layoutManager;
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param includeEdge
     * @param layoutManager
     */
    public SpaceItemDecoration(int space,boolean includeEdge, @LayoutManager int layoutManager) {
        this(space, 0, includeEdge, layoutManager);
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param headItemCount
     * @param includeEdge
     * @param layoutManager
     */
    public SpaceItemDecoration(int space,int headItemCount,boolean includeEdge, @LayoutManager int layoutManager) {
        this.space = space;
        this.headItemCount = headItemCount;
        this.includeEdge = includeEdge;
        this.layoutManager = layoutManager;
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param headItemCount
     * @param layoutManager
     */
    public SpaceItemDecoration(int space,int headItemCount, @LayoutManager int layoutManager) {
        this(space, headItemCount, true, layoutManager);
    }

    /**
     * LinearLayoutManager or GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param space
     * @param layoutManager
     */
    public SpaceItemDecoration(int space, @LayoutManager int layoutManager) {
        this(space, 0, true, layoutManager);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        switch (layoutManager) {
            case LINEARLAYOUT:
                setLinearLayoutSpaceItemDecoration(outRect,view,parent,state);
                break;
            case GRIDLAYOUT:
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
                spanCount = gridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect,view,parent,state);
                break;
            case STAGGEREDGRIDLAYOUT:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) parent.getLayoutManager();
                spanCount = staggeredGridLayoutManager.getSpanCount();
                setNGridLayoutSpaceItemDecoration(outRect,view,parent,state);
                break;
            default:
                break;
        }
    }

    /**
     * LinearLayoutManager spacing
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setLinearLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = space;
        } else {
            outRect.top = 0;
        }
    }

    /**
     * GridLayoutManager or StaggeredGridLayoutManager spacing
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setNGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, State state){
        int position = parent.getChildAdapterPosition(view) - headItemCount;
        if (headItemCount != 0 && position ==  - headItemCount){
            return;
        }
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = space - column * space / spanCount;
            outRect.right = (column + 1) * space / spanCount;
            if (position < spanCount) {
                outRect.top = space;
            }
            outRect.bottom = space;
        } else {
            outRect.left = column * space / spanCount;
            outRect.right = space - (column + 1) * space / spanCount;
            if (position >= spanCount) {
                outRect.top = space;
            }
        }
    }

    /**
     * GridLayoutManager设置间距（此方法最左边和最右边间距为设置的一半）
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    private void setGridLayoutSpaceItemDecoration(Rect outRect, View view, RecyclerView parent, State state) {
        GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
        int totalCount = layoutManager.getItemCount();
        int surplusCount = totalCount % layoutManager.getSpanCount();
        int childPosition = parent.getChildAdapterPosition(view);
        if (layoutManager.getOrientation() == GridLayoutManager.VERTICAL) {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                outRect.bottom = topBottom;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.bottom = topBottom;
            }
            if ((childPosition + 1 - headItemCount) % layoutManager.getSpanCount() == 0) {
                //加了右边后最后一列的图就非宽度少一个右边距
                //outRect.right = leftRight;
            }
            outRect.top = topBottom;
            outRect.left = leftRight / 2;
            outRect.right = leftRight / 2;
        } else {
            if (surplusCount == 0 && childPosition > totalCount - layoutManager.getSpanCount() - 1) {
                outRect.right = leftRight;
            } else if (surplusCount != 0 && childPosition > totalCount - surplusCount - 1) {
                outRect.right = leftRight;
            }
            if ((childPosition + 1) % layoutManager.getSpanCount() == 0) {
                outRect.bottom = topBottom;
            }
            outRect.top = topBottom;
            outRect.left = leftRight;
        }
    }
}
