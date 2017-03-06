package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ScrollChangeListView extends ListView{
    private OnScrollChangeListener  listener;

    public ScrollChangeListView(Context context) {
        super(context);
    }

    public ScrollChangeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollChangeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener!=null){
            listener.onScrollChanged(l, t, oldl, oldt);
        }
    }


    public  interface   OnScrollChangeListener{
        void   onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void setOnScrollListener(OnScrollChangeListener listener) {
        this.listener = listener;
    }
}
