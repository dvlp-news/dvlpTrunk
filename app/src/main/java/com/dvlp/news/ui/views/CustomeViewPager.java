package com.dvlp.news.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 增加禁止左右滑动开关
 * Created by zhangweiwei on 15/5/7.
 */
public class CustomeViewPager extends ViewPager {

    //是否可以左右滑动
    private boolean scrollble = false;

    public CustomeViewPager(Context context) {

        super(context);
    }

    public CustomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollble) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return scrollble;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollble) {
            return super.onTouchEvent(ev);
        } else {
            return scrollble;
        }
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
