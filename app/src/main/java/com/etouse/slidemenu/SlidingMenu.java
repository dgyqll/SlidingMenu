package com.etouse.slidemenu;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/6/13.
 */

public class SlidingMenu extends FrameLayout {

    private View menuView;
    private View mainView;
    private int scrollRange;
    private ViewDragHelper dragHelper;

    public SlidingMenu(@NonNull Context context) {
        this(context,null);
    }

    public SlidingMenu(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingMenu(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        dragHelper = ViewDragHelper.create(this, OnCallBack);
    }

    ViewDragHelper.Callback OnCallBack = new ViewDragHelper.Callback() {

        private int newLeft;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //当前View为主页或菜单页时自己消费事件
            return child == mainView || child == menuView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            //让主页随着菜单页作伴随移动，并让菜单页不移动
            if (child == menuView) {
                //限制移动范围
                newLeft = mainView.getLeft() + dx;
                if ((mainView.getLeft() + dx) < 0) {
                    newLeft = 0;
                }
                if ((mainView.getLeft() + dx) >= scrollRange) {
                    newLeft =scrollRange;
                }

                mainView.layout( newLeft,0, newLeft + mainView.getWidth(),mainView.getBottom());
                invalidate();
                return 0;
            }

            //限制主页的移动范围
            if ((child.getLeft() + dx) < 0) {
                return 0;
            }

            if ((child.getLeft() + dx) > scrollRange) {
                return scrollRange;
            }
            return left;
        }


        //垂直方向不移动
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //手指释放，菜单页滚动
            if (mainView.getLeft() >= scrollRange / 2) {
                dragHelper.smoothSlideViewTo(mainView, scrollRange, 0);
                ViewCompat.postInvalidateOnAnimation(mainView);
            } else {
                dragHelper.smoothSlideViewTo(mainView, 0, 0);
                ViewCompat.postInvalidateOnAnimation(mainView);
            }

        }

    };


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(mainView);
        }
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        menuView = getChildAt(0);
        mainView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scrollRange = menuView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = dragHelper.shouldInterceptTouchEvent(ev);
        return result;
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

}
