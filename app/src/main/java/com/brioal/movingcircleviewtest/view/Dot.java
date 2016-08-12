package com.brioal.movingcircleviewtest.view;

/**
 * Created by Brioal on 2016/8/12.
 */

public class Dot {
    private float mX;
    private float mY;
    private float mRadius;

    public Dot(float x, float y, float radius) {
        mX = x;
        mY = y;
        mRadius = radius;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }
}
