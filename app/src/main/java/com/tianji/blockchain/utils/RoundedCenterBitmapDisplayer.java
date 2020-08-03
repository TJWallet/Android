package com.tianji.blockchain.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class RoundedCenterBitmapDisplayer implements BitmapDisplayer {
    // 半径
    private int radius;
    // 边框 高度
    private int borderWidth;
    // 边框 颜色
    private int borderColor;
    // 容器ImageView 高度
    private int imageViewHeight;
    // 容器ImageView 宽度
    private int imageViewWidth;

    // 四个角
    private boolean leftTop, rightTop, leftBottom, rightBottom;

    public RoundedCenterBitmapDisplayer(
            int radius,
            int borderWidth,
            int borderColor,
            int imageViewHeight,
            int imageViewWidth,
            boolean leftTop,
            boolean rightTop,
            boolean leftBottom,
            boolean rightBottom) {

        this.radius = radius;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.imageViewHeight = imageViewHeight;
        this.imageViewWidth = imageViewWidth;
        this.leftTop = leftTop;
        this.rightTop = rightTop;
        this.leftBottom = leftBottom;
        this.rightBottom = rightBottom;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new RoundedDrawable(bitmap, radius, borderWidth, borderColor, imageViewHeight, imageViewWidth, leftTop, rightTop, leftBottom, rightBottom));
    }

    public static class RoundedDrawable extends Drawable {

        private final RectF mRect = new RectF(), mBitmapRect;
        private final BitmapShader bitmapShader;
        private final Paint paint;

        // 边框 颜色
        private int borderColor;
        // 边框 宽度
        private int borderWidth;
        // 半径
        private int radius;

        private boolean leftTop, rightTop, leftBottom, rightBottom;

        protected RoundedDrawable(Bitmap bitmap,
                                  int radius,
                                  int borderWidth,
                                  int borderColor,
                                  int imageViewHeight,
                                  int imageViewWidth,
                                  boolean leftTop,
                                  boolean rightTop,
                                  boolean leftBottom,
                                  boolean rightBottom) {

            this.radius = radius;
            this.borderWidth = borderWidth;
            this.borderColor = borderColor;
            this.leftTop = leftTop;
            this.rightTop = rightTop;
            this.leftBottom = leftBottom;
            this.rightBottom = rightBottom;

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            Bitmap targetBitmap;

            if (imageViewHeight == 0 || imageViewWidth == 0) {
                targetBitmap = bitmap;
            } else {
                // 图框长宽比
                double scaleImageView = (double) imageViewHeight / imageViewWidth;
                // 图片长宽比
                double scaleBitmap = (double) bitmapHeight / bitmapWidth;

                // 对比图框的长宽比和图片的长宽比，谁的长宽比更小就取谁的，这样才能在范围能截图
                if (scaleImageView < scaleBitmap) {
                    // 图框比例比图像大
                    int bitmapHeightTemp = (int) (scaleImageView * bitmapWidth);

                    if (bitmapHeightTemp > imageViewHeight) {
                        Matrix matrix = new Matrix();
                        float scale = (float) imageViewHeight / bitmapHeightTemp;
                        matrix.setScale(scale, scale);

                        targetBitmap = Bitmap.createBitmap(bitmap, 0, (bitmapHeight - bitmapHeightTemp) / 2, bitmapWidth, bitmapHeightTemp, matrix, true);
                    } else {
                        targetBitmap = Bitmap.createBitmap(bitmap, 0, (bitmapHeight - bitmapHeightTemp) / 2, bitmapWidth, bitmapHeightTemp, null, false);
                    }

                } else {
                    // 图像比例比图框小
                    int bitmapWidthTemp = (int) (bitmapHeight / scaleImageView);

                    if (bitmapWidthTemp > imageViewWidth) {
                        Matrix matrix = new Matrix();
                        float scale = (float) imageViewWidth / bitmapWidthTemp;
                        matrix.setScale(scale, scale);

                        targetBitmap = Bitmap.createBitmap(bitmap, (bitmapWidth - bitmapWidthTemp) / 2, 0, bitmapWidthTemp, bitmapHeight, matrix, true);
                    } else {
                        targetBitmap = Bitmap.createBitmap(bitmap, (bitmapWidth - bitmapWidthTemp) / 2, 0, bitmapWidthTemp, bitmapHeight, null, false);
                    }


                }
            }

            bitmapShader = new BitmapShader(targetBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBitmapRect = new RectF(borderWidth / 2, borderWidth / 2, targetBitmap.getWidth() - borderWidth / 2, targetBitmap.getHeight() - borderWidth / 2);

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
            paint.setFilterBitmap(true);
            paint.setDither(true);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRect.set(borderWidth / 2, borderWidth / 2, bounds.width() - borderWidth / 2, bounds.height() - borderWidth / 2);

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
            bitmapShader.setLocalMatrix(shaderMatrix);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRoundRect(mRect, radius, radius, paint);

            if (leftTop) { //左上角不为圆角
                canvas.drawRect(0, 0, radius, radius, paint);
            }
            if (rightTop) {//右上角不为圆角
                canvas.drawRect(canvas.getWidth() - radius, 0, canvas.getWidth(), radius, paint);
            }

            if (leftBottom) {//左下角不为圆角
                canvas.drawRect(0, canvas.getHeight() - radius, radius, canvas.getHeight(), paint);
            }

            if (rightBottom) {//右下角不为圆角
                canvas.drawRect(canvas.getWidth() - radius, canvas.getHeight() - radius, canvas.getWidth(), canvas.getHeight(), paint);
            }

            if (borderWidth > 0) {
                drawBorder(canvas);
            }
        }

        /**
         * 绘制自定义控件边框
         *
         * @param canvas
         */
        private void drawBorder(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(borderColor);
            paint.setAntiAlias(true);

            canvas.drawRoundRect(mRect, radius, radius, paint);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void setAlpha(int alpha) {
            paint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            paint.setColorFilter(cf);
        }
    }
}
