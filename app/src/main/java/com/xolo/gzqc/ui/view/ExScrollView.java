package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.xolo.gzqc.utils.ScreenUtils;

/**
 * Created by Administrator on 2016/12/2.
 */
public class ExScrollView extends ScrollView {
    private Context context;

    public ExScrollView(Context context) {
        super(context);
        this.context = context;
    }

    public ExScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public ExScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private OnScrollchangedListener   listener;

    private OnScrollchangedListener   listenerReturnTop;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener!=null){
            listener.change(l, t, oldl, oldt);
        }
        if (listenerReturnTop!=null){
            listenerReturnTop.change(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangedListener(OnScrollchangedListener listener) {
        this.listener = listener;
    }

    public interface  OnScrollchangedListener{
        void  change(int l, int t, int oldl, int oldt);
    }

    public void setTopReturn(final View  v){
        final int limitHeight = ScreenUtils.getScreenHeight(context) / 2;

        listenerReturnTop = new OnScrollchangedListener() {
               @Override
               public void change(int l, int t, int oldl, int oldt) {
                      if (t>limitHeight){
                          v.setVisibility(VISIBLE);
                      }else {
                          v.setVisibility(GONE);
                      }
               }
           };

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ExScrollView.this.scrollTo(0,0);
            }
        });
    }

}
