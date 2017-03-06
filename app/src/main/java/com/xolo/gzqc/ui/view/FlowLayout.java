package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/24.
 * 流布局
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    List<List<View>>  totalList=new ArrayList<>();
    List<Integer>    heightList=new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
         totalList.clear();
         heightList.clear();


        List<View>  lineView=new ArrayList<>();

        int childCount = getChildCount();

        int maxHeight=0;
        int totalWidth=0;

        int width = getWidth();

        for (int i=0;i<childCount;i++){
            View childAt = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
            int currentWith=childAt.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            int currentHeight=childAt.getMeasuredHeight()+layoutParams.topMargin+layoutParams.bottomMargin;

            if (totalWidth+currentWith>width){
                    totalList.add(lineView);
                    heightList.add(maxHeight);
                    lineView=new ArrayList<>();
                    lineView.add(childAt);
                    maxHeight=currentHeight;
                     totalWidth=currentWith;
            }else {
                  if (currentHeight>maxHeight){maxHeight=currentHeight;}
                  lineView.add(childAt);
                totalWidth+=currentWith;
            }

        }
        totalList.add(lineView);
        heightList.add(maxHeight);


        int size = heightList.size();
        int tolHeight=0;
        int tolWidth=0;

        for (int i=0;i<size;i++){

            List<View> views = totalList.get(i);
            int size1 = views.size();

            tolWidth=0;

            for (int k=0;k<size1;k++){

                View view = views.get(k);
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                int left=tolWidth+layoutParams.leftMargin;
                int right=tolWidth+layoutParams.leftMargin+view.getMeasuredWidth();
                int top=tolHeight+layoutParams.topMargin;
                int bottom=tolHeight+layoutParams.topMargin+view.getMeasuredHeight();

                view.layout(left,top,right,bottom);

                tolWidth+=layoutParams.leftMargin+layoutParams.rightMargin+view.getMeasuredWidth();
            }

            tolHeight+=heightList.get(i);

        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 获取父容件为它设置的测量模式和大小
         */
        int meaWidth = MeasureSpec.getSize(widthMeasureSpec);
        int meaHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //子组件的数量
        int childCount = getChildCount();

        int lineWidth=0;

        int lineHeight=0;

        int totalHeight=0;

        //遍历组件
        for (int i=0;i<childCount;i++){
            //获取子组件
            View childAt = getChildAt(i);
            //测量子组件
            measureChild(childAt,widthMeasureSpec,heightMeasureSpec);

            MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
            int childWidth=childAt.getMeasuredWidth()+layoutParams.leftMargin+layoutParams.rightMargin;
            int childHeight=childAt.getMeasuredHeight()+layoutParams.topMargin+layoutParams.bottomMargin;

            if (childWidth+lineWidth>meaWidth){
                     totalHeight+=lineHeight;
                      lineHeight=childHeight;
                      lineWidth=childWidth;
            }else {
                if (childHeight>lineHeight){
                    lineHeight=childHeight;
                }
                lineWidth+=childWidth;
            }

        }
        //添加最后一行的高度
        totalHeight+=lineHeight;

        setMeasuredDimension(meaWidth,modeHeight==MeasureSpec.EXACTLY ? meaHeight : totalHeight);

    }

}
