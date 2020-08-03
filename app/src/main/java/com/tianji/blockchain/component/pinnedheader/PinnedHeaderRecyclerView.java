package com.tianji.blockchain.component.pinnedheader;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PinnedHeaderRecyclerView extends RecyclerView {
    public interface OnPinnedHeaderClickListener {
        void onPinnedHeaderClick(int adapterPosition);
    }

    public PinnedHeaderRecyclerView(Context context) {
        super(context);
    }

    public PinnedHeaderRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedHeaderRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private OnPinnedHeaderClickListener mPinnedHeaderClickListener;

    public void setOnPinnedHeaderClickListener(OnPinnedHeaderClickListener listener) {
        mPinnedHeaderClickListener = listener;
    }

    private boolean mPinnedHeaderHandle;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mPinnedHeaderClickListener == null) {
            return super.onInterceptTouchEvent(event);
        }
        IPinnedHeaderDecoration pinnedHeaderInterface = getPinnedHeaderDecoration();
        if (pinnedHeaderInterface == null) {
            return super.onInterceptTouchEvent(event);
        }
        Rect pinnedHeaderRect = pinnedHeaderInterface.getPinnedHeaderRect();
        int pinnedHeaderPosition = pinnedHeaderInterface.getPinnedHeaderPosition();
        if (pinnedHeaderRect == null || pinnedHeaderPosition == -1) {
            return super.onInterceptTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (pinnedHeaderRect.contains((int) event.getX(), (int) event.getY())) {
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPinnedHeaderClickListener == null) {
            return super.onTouchEvent(event);
        }
        IPinnedHeaderDecoration pinnedHeaderInterface = getPinnedHeaderDecoration();
        if (pinnedHeaderInterface == null) {
            return super.onTouchEvent(event);
        }
        Rect pinnedHeaderRect = pinnedHeaderInterface.getPinnedHeaderRect();
        int pinnedHeaderPosition = pinnedHeaderInterface.getPinnedHeaderPosition();
        if (pinnedHeaderRect == null || pinnedHeaderPosition == -1) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPinnedHeaderHandle = false;
                if (pinnedHeaderRect.contains((int) event.getX(), (int) event.getY())) {
                    mPinnedHeaderHandle = true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPinnedHeaderHandle) {
                    if (!pinnedHeaderRect.contains((int) event.getX(), (int) event.getY())) {
                        MotionEvent cancel = MotionEvent.obtain(event);
                        cancel.setAction(MotionEvent.ACTION_CANCEL);
                        super.dispatchTouchEvent(cancel);
                        MotionEvent down = MotionEvent.obtain(event);
                        down.setAction(MotionEvent.ACTION_DOWN);
                        return super.dispatchTouchEvent(down);
                    } else {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();
                if (mPinnedHeaderHandle && pinnedHeaderRect.contains((int) x, (int) y)) {
                    mPinnedHeaderClickListener.onPinnedHeaderClick(pinnedHeaderPosition);
                    mPinnedHeaderHandle = false;
                    return true;
                }
                mPinnedHeaderHandle = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public IPinnedHeaderDecoration getPinnedHeaderDecoration() {
        int decorationIndex = 0;
        ItemDecoration itemDecoration;
        do {
            itemDecoration = getItemDecorationAt(decorationIndex);
            if (itemDecoration instanceof IPinnedHeaderDecoration) {
                return (IPinnedHeaderDecoration) itemDecoration;
            }
            decorationIndex++;
        } while (itemDecoration != null);
        return null;
    }
}
