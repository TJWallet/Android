package com.tianji.blockchain.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.tianji.blockchain.R;

/** author:jason **/
public class RoundImageView extends AppCompatImageView {
    float width, height;
    int radius;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.analyzeAttrs(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        this.analyzeAttrs(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    private int dp2px(Context context, int dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void analyzeAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        if (typedArray != null) {
            int radius = typedArray.getInteger(R.styleable.RoundImageView_border_radius, 16);
            this.radius = this.dp2px(getContext(), radius);
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int angleRadius = this.radius;

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
