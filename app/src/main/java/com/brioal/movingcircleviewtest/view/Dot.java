package com.brioal.movingcircleviewtest.view;

import java.util.Random;

/**
 * Created by Brioal on 2016/8/12.
 */

public class Dot {
    private float mX;
    private float mY;
    private float mRadius;

    public static int WIDTH = 0;
    public static int SPEED = 0;
    public  Random sRandom;
    public static int sMaxDotRadius;
    public static int sMinDotRadius;

    public Dot() {
        setRightPosition();
    }

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

    public double getZ() {
        return Math.sqrt(mX * mX + mY * mY);
    }

    public void checkAndChange() {
        if (getZ() + getRadius() < WIDTH / 4) {
            setRightPosition();
        } else {
            adjustPosition();
        }
    }

    public void adjustPosition() {
        float xOffset = (float) (mX * SPEED / getZ());
        mX -= xOffset;
        float yOffset = (float) (mY * SPEED / getZ());
        mY -= yOffset;
    }

    public void setRightPosition() {
        if (sRandom == null) {
            sRandom = new Random();
        }
        mRadius = (int) (sRandom.nextFloat() * (sMaxDotRadius - sMinDotRadius) + sMinDotRadius);
        int angle = sRandom.nextInt(360); //获取角度值
        if (angle < 90) {
            mY = -WIDTH / 2 - mRadius-sRandom.nextInt(50);
            angle = angle - 45;
            mX = (int) (angle < 0 ? (-Math.tan(angle * Math.PI / 360) * WIDTH / 2) : (Math.tan(angle * Math.PI / 180) * WIDTH / 2));
        } else if (angle < 180) {
            mX = WIDTH / 2 + mRadius+sRandom.nextInt(50);
            angle = angle - 135;
            mY = (int) (angle < 0 ? (-Math.tan(angle * Math.PI / 180) * WIDTH / 2) : (Math.tan(angle * Math.PI / 180) * WIDTH / 2));
        } else if (angle < 270) {
            mY = WIDTH / 2 + mRadius+sRandom.nextInt(50);
            angle = angle - 225;
            mX = (int) (angle < 0 ? (Math.tan(angle * Math.PI / 180) * WIDTH / 2) : (-Math.tan(angle * Math.PI / 180) * WIDTH / 2));
        } else {
            mX = -WIDTH / 2 - mRadius-sRandom.nextInt(50);
            angle = angle - 315;
            mY = (int) (angle > 0 ? (-Math.tan(angle * Math.PI / 180) * WIDTH / 2) : (Math.tan(angle * Math.PI / 180) * WIDTH / 2));
        }
    }
}
