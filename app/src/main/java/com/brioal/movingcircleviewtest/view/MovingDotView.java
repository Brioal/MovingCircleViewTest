package com.brioal.movingcircleviewtest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.brioal.movingcircleviewtest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Brioal on 2016/8/12.
 */

public class MovingDotView extends View {
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


    public MovingDotView(Context context) {
        this(context, null);
    }

    public MovingDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

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
    }

    @Override
    protected void onSizeChanged(final int w, final int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterDotRadius = w / 4;
        for (int i = 0; i < 10; i++) {
            mDots.add(setRightPosition());
        }
        mConverSpeed = 1; //每毫秒多少像素
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    for (int i = 0; i < mDots.size(); i++) {
                        Dot dot = mDots.get(i);
                        double x = getWidth() / 2 - dot.getX();
                        double y = getHeight() / 2 - dot.getY();
                        double z = Math.sqrt(x * x + y * y);
                        if (z + dot.getRadius() < mCenterDotRadius / 4) {
                            mDots.set(i, setRightPosition());
                        } else {
                            double xoffset = x * mConverSpeed / z;
                            double yoffset = y * mConverSpeed / z;
                            dot.setX((float) (dot.getX() + xoffset));
                            dot.setY((float) (dot.getY() + yoffset));
                        }
                    }
                    mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public double getZ(Dot dot) {
        double x = getWidth() / 2 - dot.getX();
        double y = getHeight() / 2 - dot.getY();
        double z = Math.sqrt(x * x + y * y);
        return z;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDotColor);
        for (int j = 0; j < mDots.size(); j++) {
            Dot dot = mDots.get(j);
            float progress = (float) ((getZ(dot) - mCenterDotRadius) / mCenterDotRadius);//1~0
            Log.i("Progress", progress + "");
            if (progress > 1) {
                progress = 1;
            }
            if (progress <0) {
                progress = 0;
            }
            mPaint.setAlpha((int) ((1 - progress) * 205 + 50));
            canvas.drawCircle(dot.getX(), dot.getY(), dot.getRadius(), mPaint);
        }
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_cneter_dot);
        drawable.setBounds(-mCenterDotRadius, -mCenterDotRadius, mCenterDotRadius, mCenterDotRadius);
        drawable.draw(canvas);
        canvas.restore();

    }

    public Dot setRightPosition() {
        int radius = (int) (mRandom.nextFloat() * (mMaxDotRadius - mMinDotRadius) + mMinDotRadius);
        int x = (mRandom.nextInt((getWidth() - 10 - 2 * radius)) + 5 + radius);
        int y = mRandom.nextInt((getHeight() - 10 - 2 * radius)) + 5 + radius;
        if (mBoundOffset - radius < x && x < getWidth() - mBoundOffset + radius && y > mBoundOffset - radius && y < getHeight() - mBoundOffset + radius) {
            return setRightPosition();
        } else {
            return new Dot(x, y, radius);
        }
    }
}
