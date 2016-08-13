package com.brioal.movingcircleviewtest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.brioal.movingcircleviewtest.R;
import com.brioal.movingcircleviewtest.view.exceptions.SizeNotDeterminedException;
import com.brioal.movingcircleviewtest.view.listener.OnProgressChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Brioal on 2016/8/12.
 */

public class MovingDotView extends ViewGroup {
    private List<Dot> mDots;
    private int mCenterDotRadius;
    private int mCenterDotColor;
    private int mDotColor;
    private Paint mPaint;
    private Random mRandom;
    private int mBoundOffset;
    private int mMaxDotRadius;
    private int mMinDotRadius;
    private float mConverSpeed;
    private int mWidth;
    private int mProgress;
    private int mTextSize;
    private int mSpeed;
    private CenterDot mCenterDot;
    private OnProgressChangeListener mChangeListener;

    public void setChangeListener(OnProgressChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    public MovingDotView(Context context) {
        this(context, null);
    }

    public MovingDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(true);
        init();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        getChildAt(0).layout(getWidth() / 2 - mCenterDotRadius, getWidth() / 2 - mCenterDotRadius, getWidth() / 2 + mCenterDotRadius, getWidth() / 2 + mCenterDotRadius);
    }


    private void init() {
        mDots = new ArrayList<>();
        mCenterDotColor = getResources().getColor(R.color.colorPrimary);
        mDotColor = getResources().getColor(R.color.colorAccent);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mRandom = new Random();
        mBoundOffset = 50;
        mMaxDotRadius = 20;
        mMinDotRadius = 10;
        mProgress = 50;
        mTextSize = 150;
        mSpeed = 10;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //假设情况是用户至少会给宽或者高指定一个确定的值,否则抛出异常
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            try {
                throw new SizeNotDeterminedException("宽高不能都为wrap_content");
            } catch (SizeNotDeterminedException e) {
                e.printStackTrace();
            }
        }

        mWidth = Math.min(widthSize, heightSize);
        setMeasuredDimension(mWidth, mWidth);
        Dot.WIDTH = mWidth;
        Dot.SPEED = 1;
        Dot.sMaxDotRadius = 20;
        Dot.sMinDotRadius = 10;
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterDotRadius = w / 4;
        for (int i = 0; i < 10; i++) {
            mDots.add(new Dot());
        }
        mCenterDot = new CenterDot(getContext(), mCenterDotRadius * 2);
        mCenterDot.setClickable(true);
        mCenterDot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChanged) {
                    backClean();
                } else {
                    startClean();
                }
            }
        });
        addView(mCenterDot);
    }

    public void startClean() {
        startAnimation(0, 1);
        isChanged = true;
    }

    public void backClean() {
        startAnimation(1, 0);
        isChanged = false;
    }

    public void startAnimation(float from, float to) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float progress = (float) valueAnimator.getAnimatedValue();
                if (mChangeListener != null) {
                    mChangeListener.onProgressChanged(progress);
                }
                Dot.SPEED = (int) (1 + progress * 3);
                mCenterDot.setAnimationPogress(progress);
                mCenterDot.setProgress((int) (80-progress*40));
            }
        });
        valueAnimator.start();

    }

    private boolean isChanged = false;


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDotColor);
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        for (int j = 0; j < mDots.size(); j++) {
            Dot dot = mDots.get(j);
            float progress = (float) ((dot.getZ() - mCenterDotRadius) / (new Dot(-getWidth() / 2, -getWidth() / 2, 0).getZ() - mCenterDotRadius));//1~0
            if (progress > 1) {
                progress = 1;
            }
            if (progress < 0) {
                progress = 0;
            }
            Log.i("Progress", progress + "");
            int alpha = (int) ((1 - progress) * 200 + 75);
            mPaint.setAlpha(alpha > 255 ? 255 : alpha);
            canvas.drawCircle(dot.getX(), dot.getY(), dot.getRadius(), mPaint);
            dot.checkAndChange();
        }
        postInvalidateDelayed(mSpeed);
        canvas.restore();

    }

}
