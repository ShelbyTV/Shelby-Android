package com.shelby.ui.utility;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Flip3DAnimation extends Animation {
    
    public static final int FLAG_ROTATE_X = 0x1;
    public static final int FLAG_ROTATE_Y = 0x2;
    public static final int FLAG_ROTATE_Z = 0x4;
    public static final int FLAG_ROTATE_ALL = FLAG_ROTATE_X | FLAG_ROTATE_Y | FLAG_ROTATE_Z;
    
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private Camera mCamera;
    private int mRotationFlags;
    
    
    public Flip3DAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, int rotationFlags) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mRotationFlags = rotationFlags;
    }
    
    public void setRotationFlags(int rotationFlags) {
        mRotationFlags = rotationFlags;
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
            int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        camera.save();

        if ((mRotationFlags & FLAG_ROTATE_X) == FLAG_ROTATE_X) {
            camera.rotateX(degrees);
        }
        if ((mRotationFlags & FLAG_ROTATE_Y) == FLAG_ROTATE_Y) {
            camera.rotateY(degrees);
        }
        if ((mRotationFlags & FLAG_ROTATE_Z) == FLAG_ROTATE_Z) {
            camera.rotateZ(degrees);
        }

        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

}
