package org.daniel.android.cgtest.widgets;

import android.animation.Animator;
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

import java.util.LinkedList;
import java.util.List;

/**
 * 实现栈顶滑动退出
 *
 * @author jiaoyang<br>
 *         email: jiaoyang623@qq.com
 * @version 1.0
 * @date May 27 2015 10:21 AM
 */
public class SweepStackLayout extends ViewGroup {
    // 显示最多的页数
    private static final int MAX_COUNT = 4;
    //图片长宽比
    private static final float WPH = 1;
    // 中心图片占整个View宽度的大小
    private static final float CENTER_SCALE = 0.85f;
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
    // 内容宽高
    private int mInWidth, mInHeight;
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
        mInWidth = (int) (mWidth * CENTER_SCALE);
        mInHeight = (int) (mInWidth / WPH);

        int centerY = (int) (mHeight * VERTICAL_CENTER_PERCENT);

        mCX0 = mWidth / 2;
        mCY0 = centerY;

        refillContent();
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
                View v = getActView();
                if (v != null) {
                    mIsTouchIn = mX0 > v.getLeft() && mX0 < v.getRight() && mY0 > v.getTop() && mY0 < v.getBottom();
                    TU.j(v.getLeft(), mX0, v.getRight(), v.getTop(), mY0, v.getBottom());
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - mX0;
                float dy = ev.getY() - mY0;
                return mIsTouchIn && (dx * dx + dy * dy > MOVE_THRESHOLD);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsTouchIn) {
            return true;
        }
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
                        go(v, 1);
                    } else if (scale < -UNSELECT_SCALE / 2) {
                        // to left
                        go(v, -1);
                    } else {
                        // to center
                        go(v, 0);
                    }
                }
                //结束
                break;
        }

        return true;
    }

    /**
     * 获取需要运动的View
     */
    private View getActView() {
        int count = getChildCount();
        if (count == 0) {
            return null;
        } else {
            return getChildAt(count - 1);
        }
    }


    /**
     * 去哪？
     */
    private void go(final View v, final int direction) {
        if (v == null) {
            return;
        }

        LinkedList<Animator> animList = new LinkedList<Animator>();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float x) {
                return (float) (x + Math.sin(x * Math.PI) * 0.5);
            }
        });

        if (direction > 0) {
            animList.add(ObjectAnimator.ofFloat(v, "rotation", v.getRotation()));
            animList.add(ObjectAnimator.ofFloat(v, "translationX", mWidth));
            animList.add(ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY()));

            returnAnimate(animList, 1);
        } else if (direction < 0) {
            animList.add(ObjectAnimator.ofFloat(v, "rotation", v.getRotation()));
            animList.add(ObjectAnimator.ofFloat(v, "translationX", -mWidth));
            animList.add(ObjectAnimator.ofFloat(v, "translationY", v.getTranslationY()));

            returnAnimate(animList, 1);
        } else {
            animList.add(ObjectAnimator.ofFloat(v, "rotation", 0));
            animList.add(ObjectAnimator.ofFloat(v, "translationX", 0f));
            animList.add(ObjectAnimator.ofFloat(v, "translationY", 0f));

            returnAnimate(animList, BACK_SCALE);
        }

        animatorSet.playTogether(animList);

        animatorSet.setDuration(DURATION);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCallback != null) {
                    if (direction != 0) {
                        removeView(v);
                        mCallback.onPop(v, direction);
                        refillContent();
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.start();


    }

    private void returnAnimate(List<Animator> animList, float toScale) {
        float scale = toScale;
        for (int i = getChildCount() - 2; i >= 0; i--) {
            View v = getChildAt(i);
            int dy = (int) (mInHeight * (1 - scale) / 1.5);

            animList.add(ObjectAnimator.ofFloat(v, "translationY", -dy));
            animList.add(ObjectAnimator.ofFloat(v, "scaleX", scale));
            animList.add(ObjectAnimator.ofFloat(v, "scaleY", scale));
            animList.add(ObjectAnimator.ofFloat(v, "alpha", (float) Math.pow(scale, 4)));

            scale -= (1 - BACK_SCALE);
        }
        progressActView(0);
    }

    /**
     * 移动？
     */
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
        float progress = (mCX1 - mCX0) / (float) mWidth / (UNSELECT_SCALE / 2);

        childAnimate(progress);
    }

    /**
     * @param progress 0: center
     *                 positive: right
     *                 negtive: left
     *                 [-1,1]: unselected
     *                 else: selected
     */
    private void childAnimate(float progress) {
        setContentEffect(BACK_SCALE + (1 - BACK_SCALE) * Math.min(1, Math.abs(progress)));
        progressActView(progress);
    }

    private void progressActView(float progress) {
        View v = getActView();
        if (v != null && v instanceof Progressable) {
            ((Progressable) v).changeProgress(progress);
        }
    }

    /**
     * 填装内容
     */
    private void refillContent() {
        if (mCallback != null) {
            while (getChildCount() < MAX_COUNT) {
                View v = mCallback.getNext();
                if (v == null) {
                    break;
                } else {
                    addView(v, 0);
                    layoutView(v);
                    if (v instanceof Progressable) {
                        ((Progressable) v).changeProgress(0);
                    }
                }
            }
            setContentEffect(BACK_SCALE);
        }
    }

    private void layoutView(View v) {
        v.measure(MeasureSpec.makeMeasureSpec(mInWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mInHeight, MeasureSpec.EXACTLY));
        int l = (mWidth - mInWidth) / 2;
        int r = l + mInWidth;
        int t = mCY0 - mInHeight / 2;
        int b = t + mInHeight;
        v.layout(l, t, r, b);
    }

    private void setContentEffect(float startScale) {
        float scale = startScale;
        for (int i = getChildCount() - 2; i >= 0; i--) {
            View v = getChildAt(i);
            int dy = (int) (mInHeight * (1 - scale) / 1.5);
            v.setRotation(0);
            v.setTranslationX(0);
            v.setTranslationY(-dy);

            v.setScaleX(scale);
            v.setScaleY(scale);
            v.setAlpha((float) Math.pow(scale, 4));

            scale -= (1 - BACK_SCALE);
        }
    }

    /**
     * @param direction 方向
     *                  {-1: left, 1:right}
     */
    public void sweep(int direction) {
        if (direction * direction != 1) {
            throw new IllegalArgumentException("direction need to be 1 or -1");
        }

        go(getActView(), direction);
    }


    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        View getNext();

        /**
         * @param v         poped view
         * @param direction -1: left
         *                  1: right
         */
        void onPop(View v, int direction);

        void onClick();
    }

    public interface Progressable {
        /**
         * @param progress 0: center
         *                 positive: right
         *                 negtive: left
         *                 [-1,1]: unselected
         *                 else: selected
         */
        void changeProgress(float progress);
    }
}
