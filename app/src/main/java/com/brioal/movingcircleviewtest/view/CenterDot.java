package com.brioal.movingcircleviewtest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.brioal.movingcircleviewtest.R;
import com.brioal.movingcircleviewtest.view.util.SizeUtil;

/**
 * Created by Brioal on 2016/8/13.
 */

public class CenterDot extends View {
    private int mRadius;
    private int mTextColor;
    private int mTextSize;
    private Paint mPaint;
    private int mWidth;
    private int mTextYOffset;
    private int mProgress;
    private int mMaxTextOffset;

    public CenterDot(Context context, int width) {
        this(context, null, width);
    }

    public CenterDot(Context context, AttributeSet attrs, int width) {
        super(context, attrs);
        mWidth = width;
        init();
    }

    //设置动画
    public void setAnimationPogress(float progress) {
        mTextYOffset = (int) (mMaxTextOffset * (1 - progress));
        setScaleX((float) (1 - progress * 0.2));
        setScaleY((float) (1 - progress * 0.2));
        invalidate();
    }

    //设置文字
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    private void init() {
        mRadius = mWidth / 2;
        mTextColor = Color.WHITE;
        mTextSize = (int) SizeUtil.Sp2Px(getContext(), 70);
        mMaxTextOffset = (int) SizeUtil.Dp2Px(getContext(), 40);
        mTextYOffset = mMaxTextOffset;
        mProgress = 100;


        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);

        setBackgroundResource(R.drawable.ic_center_dot);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String text = mProgress + "";
        Rect textBound = new Rect();
        canvas.save();
        canvas.translate(mWidth / 2, mWidth / 2);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(Color.WHITE);
        mPaint.getTextBounds(text, 0, text.length(), textBound);

        canvas.drawText(text, -(textBound.right + textBound.left) / 2, -(textBound.top + textBound.bottom) / 2 - mTextYOffset, mPaint);


        String percentText = "%";
        mPaint.setTextSize(30);
        Rect percentBound = new Rect();
        mPaint.getTextBounds(percentText, 0, percentText.length(), percentBound);
        canvas.drawText(percentText, (textBound.right + textBound.left) / 2, -mTextYOffset, mPaint);


        Drawable btnDrawable = getResources().getDrawable(R.drawable.ic_btn_bg);
        String btnText = "加速";
        mPaint.setTextSize(mTextYOffset * 2 / 3);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        Rect btnTexBound = new Rect();
        mPaint.getTextBounds(btnText, 0, btnText.length(), btnTexBound);
        btnDrawable.setBounds(-mRadius / 2, mRadius - mMaxTextOffset - mTextYOffset, mRadius / 2, mRadius - mMaxTextOffset);
        btnDrawable.draw(canvas);
        int btnX = btnDrawable.getBounds().centerX();
        int btnY = btnDrawable.getBounds().centerY();
        canvas.save();
        canvas.translate(btnX, btnY);
        canvas.drawText(btnText, -(btnTexBound.right - btnTexBound.left) / 2, (btnTexBound.bottom - btnTexBound.top) / 2, mPaint);
        canvas.restore();

        canvas.restore();
    }
}
