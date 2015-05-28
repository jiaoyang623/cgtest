package org.daniel.android.cgtest.widgets;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import org.daniel.android.cgtest.utils.TU;

/**
 * 实现栈顶滑动退出
 *
 * @author jiaoyang<br>
 *         email: jiaoyang623@qq.com
 * @version 1.0
 * @date May 27 2015 10:21 AM
 */
public class SweepStackLayout extends ViewGroup {
    //图片长宽比
    private static final float WPH = 3f / 4f;
    // 中心图片占整个View宽度的大小
    private static final float CENTER_SCALE = 0.7f;
    //纵向便宜
    private static final float VERTICAL_CENTER_PERCENT = 0.4F;
    //后面图片的缩放
    private static final float BACK_SCALE = 0.95f;
    //选中区域
    private static final float UNSELECT_SCALE = 0.2f;
    //手势判定移动的最小位移平方
    private static final int MOVE_THRESHOLD = 5 * 5;
    private static final int DURATION = 300;

    //监听器
    private Callback mCallback;
    //容器的长宽
    private int mHeight, mWidth;
    //是否点击到图片上
    private boolean mIsTouchIn = false;
    //ACTION_DOWN事件的x和y
    private float mX0, mY0;
    //原始中心位置
    private int mCX0, mCY0;
    //移动中心位置
    private int mCX1, mCY1;

    public SweepStackLayout(Context context) {
        super(context);
        init(context);
    }

    public SweepStackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SweepStackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        TU.j("onlayout", left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        int inWidth = (int) (mWidth * CENTER_SCALE);
        int inHeight = (int) (inWidth / WPH);

        int centerY = (int) (mHeight * VERTICAL_CENTER_PERCENT);

        mCX0 = mWidth / 2;
        mCY0 = centerY;

        //先画后面，后画前面
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            float d = (1 - BACK_SCALE) * (count - i - 1);
            int dx = (int) (inWidth * d);
            int dy = (int) (inHeight * d);

            int l = (mWidth - inWidth) / 2 + dx;
            int r = l + inWidth - dx * 2;
            int t = centerY - inHeight / 2 - dx;
            int b = t + inHeight - dx - dy;

            if (l >= r || t >= b) {
                break;
            }
            getChildAt(i).layout(l, t, r, b);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mX0 = ev.getX();
                mY0 = ev.getY();
                mCX1 = mCX0;
                mCY1 = mCY0;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - mX0;
                float dy = ev.getY() - mY0;
                return (dx * dx + dy * dy > MOVE_THRESHOLD);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - mX0);
                int dy = (int) (event.getY() - mY0);
                mCX1 = mCX0 + dx;
                mCY1 = mCY0 + dy;
                move();

                break;
            case MotionEvent.ACTION_CANCEL:
                mCX1 = mCX0;
                mCY1 = mCY0;
                move();
                //取消
                break;
            case MotionEvent.ACTION_UP:
                View v = getActView();
                if (v != null) {
                    float scale = (mCX1 - mCX0) / (float) mWidth;
                    Log.i("jy", "animate: " + v.getTranslationY() + ", " + v.getTop());
                    int top = (int) v.getTranslationY();
                    if (scale > UNSELECT_SCALE / 2) {
                        //to right
                        go(v, mWidth, top, v.getRotation());
                    } else if (scale < -UNSELECT_SCALE / 2) {
                        // to left
                        go(v, -mWidth, top, v.getRotation());
                    } else {
                        // to center
                        go(v, 0, 0, 0);
                    }
                }
                //结束
                break;
        }

        return true;
    }

    private View getActView() {
        int count = getChildCount();
        if (count == 0) {
            return null;
        } else {
            return getChildAt(count - 1);
        }
    }


    private void go(View v, int x, int y, float rotation) {
        if (v == null) {
            return;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float x) {
                return (float) (x + Math.sin(x * Math.PI) * 0.5);
            }
        });
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(v, "rotation", rotation),
                ObjectAnimator.ofFloat(v, "translationX", x),
                ObjectAnimator.ofFloat(v, "translationY", y));
        animatorSet.setDuration(DURATION);
        animatorSet.start();
    }

    private void move() {
        View v = getActView();
        if (v == null) {
            return;
        }

        int w = v.getWidth();
        int h = v.getHeight();
        // move
        int dx = mCX1 - w / 2 - v.getLeft();
        int dy = mCY1 - h / 2 - v.getTop();
        v.setTranslationX(dx);
        v.setTranslationY(dy);

        // rotate
        double degree = Math.toDegrees(Math.atan2(mCY1 + mHeight, mCX1 - mCX0)) - 90;
        v.setRotation((float) degree);
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        /**
         * @param v         poped view
         * @param direction 0: left
         *                  1: right
         */
        void onPop(View v, int direction);

        void onClick();
    }
}
