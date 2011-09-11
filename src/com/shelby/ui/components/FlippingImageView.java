package com.shelby.ui.components;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.shelby.ui.utility.Flip3DAnimation;

public class FlippingImageView extends ImageView {
    public static final int FLAG_ROTATE_X = Flip3DAnimation.FLAG_ROTATE_X;
    public static final int FLAG_ROTATE_Y = Flip3DAnimation.FLAG_ROTATE_Y;
    public static final int FLAG_ROTATE_Z = Flip3DAnimation.FLAG_ROTATE_Z;
    public static final int FLAG_ROTATE_ALL = Flip3DAnimation.FLAG_ROTATE_ALL;
    
    protected Flip3DAnimation mFlipAnimation = null;
    protected boolean mIsAnimating = false;
    protected int mRotationFlags = FLAG_ROTATE_Y;
    

    public FlippingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlippingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlippingImageView(Context context) {
        super(context);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0)
            start();
    }
    
    public void setRotationFlags(int rotationFlags) {
        mRotationFlags = rotationFlags;
        if (mFlipAnimation != null) {
            mFlipAnimation.setRotationFlags(mRotationFlags);
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        
        if (visibility == INVISIBLE || visibility == GONE) {
            stop();
        } else {
            start();
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) { //isfroyo
            super.onVisibilityChanged(changedView, visibility);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        
        
        if (visibility == INVISIBLE || visibility == GONE) {
            stop();
        } else {
            start();
        }
        
        super.setVisibility(visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
    
    private void start() {
        if ((!mIsAnimating) && getWidth() > 0 && getVisibility() == View.VISIBLE) {
            mIsAnimating = true;
            if (mFlipAnimation == null) {
                mFlipAnimation = new Flip3DAnimation(0, 360, getWidth()/2.0f, getHeight()/2.0f, mRotationFlags);
                mFlipAnimation.setDuration(1300);
                mFlipAnimation.setInterpolator(new LinearInterpolator());
                mFlipAnimation.setRepeatCount(Animation.INFINITE);
                mFlipAnimation.setRepeatMode(Animation.RESTART);
            }
            startAnimation(mFlipAnimation);
        }
    }
    
    private void stop() {
        if (mIsAnimating) {
            mIsAnimating = false;
            setAnimation(null);
            mFlipAnimation = null;
//            mFlipAnimation.cancel();
//            mFlipAnimation.reset();
        }
    }

}
