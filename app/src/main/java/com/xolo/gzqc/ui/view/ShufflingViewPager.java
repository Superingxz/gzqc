package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.bumptech.glide.Glide;
import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.fragment.InquiryFragment;
import com.xolo.gzqc.utils.LogUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/7/6.
 * 轮间播放类     使用glide加载图片
 *
 * 使用方法：
 *      shuffling.load(list);
 */

public class ShufflingViewPager extends RelativeLayout {
    private static final String TAG = "qwer";

    private ViewPager viewPager;
    private LinearLayout dotContain;
    private int size;
    private Context context;
    private int delay = 5000;
    private int dotBottomMargin = 30;
    private int dotCheck ;
    private int dotUncheck ;
    private int dotSize = 15;
    private int dotRightMargin = 15;
    private int selectPosition = 0;
    private boolean isRunning;
    private boolean isRun =true;

    private Handler  handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (viewPager!=null){
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                sendEmptyMessageDelayed(1,delay);
            }
        }

    };

    private PagerAdapter adapter;


    public ShufflingViewPager(Context context) {
        super(context);
        init(context);
    }


    public ShufflingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        viewPager = new ViewPager(context);
        LayoutParams   param = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(param);
        addView(viewPager);

        dotContain = new LinearLayout(context);
        param = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        param.bottomMargin = dotBottomMargin;
        dotContain.setLayoutParams(param);
        dotContain.setGravity(Gravity.CENTER);
        addView(dotContain);
    }

    public void  load(final List<String> urls){
        size = urls.size();

        final ImageView[]  ivs = new ImageView[size];

        if (size == 0){
            return;
        }

        initDot();

        adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return  size <2?size: Integer.MAX_VALUE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = new ImageView(context);

//                if (ivs[position%size] == null){
//                        ivs[position%size] = new ImageView(context);
//                }else
//                       view = ivs[position%size];

                Glide.with(context).load(urls.get(position%size))
//                        .placeholder(R.mipmap.default_product_bg_nano)
                        .error(R.mipmap.img_error)
                        .into(view);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

        };
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View childAt = dotContain.getChildAt(selectPosition);
                childAt.setBackgroundResource(dotUncheck);

                int i = position % size;
                View childAt1 = dotContain.getChildAt(i);
                childAt1.setBackgroundResource(dotCheck);

                selectPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (isRun){
            start();
        }

    }

    private  void  initDot(){
        dotContain.removeAllViews();

        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(dotSize,dotSize);

        params.rightMargin = dotRightMargin;
        for (int i = 0; i <size; i++) {
            View view = new View(context);
            if (i == 0){
             view.setBackgroundResource(dotCheck);
            }else {
                view.setBackgroundResource(dotUncheck);
            }
            view.setLayoutParams(params);
            dotContain.addView(view);
        }

    }



    public void start(){
            if (isRunning == false && size>1){
                handler.sendEmptyMessageDelayed(1,delay);
                isRunning = true;
            }
    }

    public  void stop(){
        if (isRunning == true){
            handler.removeCallbacksAndMessages(null);
            isRunning = false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isRun){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    stop();
                    break;
                case MotionEvent.ACTION_UP:
                    start();
                    break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public void setDotCheck(int dotCheck) {
        this.dotCheck = dotCheck;
    }

    public void setDotUncheck(int dotUncheck) {
        this.dotUncheck = dotUncheck;
    }
}
