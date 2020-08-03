package com.tianji.blockchain.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.tianji.blockchain.R;

/** author:jason **/
public class RoundAngleLayout extends RelativeLayout {
    float width, height;
    int radius = 10;

    public RoundAngleLayout(Context context) {
        super(context);
    }

    public RoundAngleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundAngleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoundAngleLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int angleRadius = this.dp2px(this.getContext(), this.radius);

        if (width > angleRadius && height > angleRadius) {
            Path path = new Path();
            path.moveTo(angleRadius, 0);
            path.lineTo(width - angleRadius, 0);
            path.quadTo(width, 0, width, angleRadius);
            path.lineTo(width, height - angleRadius);
            path.quadTo(width, height, width - angleRadius, height);
            path.lineTo(angleRadius, height);
            path.quadTo(0, height, 0, height - angleRadius);
            path.lineTo(0, angleRadius);
            path.quadTo(0, 0, angleRadius, 0);
            canvas.clipPath(path);
        }
        super.onDraw(canvas);
    }
}
