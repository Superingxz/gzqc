package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.ChildHistroyPrice;
import com.xolo.gzqc.bean.child.CoHistroyPrice;
import com.xolo.gzqc.bean.child.CoProject;
import com.xolo.gzqc.utils.NumberFormatTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoHistoryLinearLayout extends LinearLayout implements View.OnClickListener {
    List<ChildHistroyPrice> childHistroyPrices= new ArrayList<>();
    LinearLayout co_second_view;
    View co_project_title_line;
    public  void cleanData(){
        childHistroyPrices= new ArrayList<>();
    }


    public List<ChildHistroyPrice> getChildHistroyPrices() {
        return childHistroyPrices;
    }

    public void setChildHistroyPrices(List<ChildHistroyPrice> childHistroyPrices) {
        this.childHistroyPrices = childHistroyPrices;
    }

    //co_second_view
    Context context;

    public CoHistoryLinearLayout(Context context) {
        super(context);
        this.context = context;
        init(context);
    }
    public CoHistoryLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context);
    }
    public  void  setLineViewHide(){
        co_second_view.setVisibility(View.GONE);
    }
    private void init(Context context) {
        View.inflate(context, R.layout.item_co_history_title, this);
        co_project_title_line=findViewById(R.id.co_project_title_line);
        setClickable(true);
        setOnClickListener(this);
        co_second_view = (LinearLayout) findViewById(R.id.co_second_view);
    }


    public void initData(CoHistroyPrice coHistroyPrice) {
       ((TextView) findViewById(R.id.frist_time)).setText("第"+NumberFormatTest.foematInteger
                (Integer.valueOf(coHistroyPrice.getTimes_cnt()))+"次报价");
        ((TextView) findViewById(R.id.history_time)).setText(coHistroyPrice.getOperatdate());
        ((TextView) findViewById(R.id.history_time)).getPaint().setFakeBoldText(true);

    }

    boolean ishow, isLoad;

    public void hideView(View v) {
        for (int i = 0; i < ((LinearLayout) this.getParent()).getChildCount(); i++) {
            if(((LinearLayout)this.getParent()).getChildAt(i) instanceof  CoHistoryLinearLayout){
                CoHistoryLinearLayout layout =
                        (CoHistoryLinearLayout) ((LinearLayout) this.getParent()).getChildAt(i);
                if (((CoHistoryLinearLayout) v) != layout) {
                    layout.ChidlViewHide();
                    layout.hideTitle();
                    layout.setIshow(false);
                }
            }
        }
    }

    public void ChidlViewHide() {
        for (int i = 0; i < co_second_view.getChildCount(); i++) {
            co_second_view.getChildAt(i).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (childHistroyPrices==null||childHistroyPrices.size()==0) {
            return;
        }
        //隐藏第三级目录
        LinearLayout layout1 = (LinearLayout) this.findViewById(R.id.co_second_view);
        for (int j = 0; j < layout1.getChildCount(); j++) {
            CoHistorySecondLinearLayout layout2 = ((CoHistorySecondLinearLayout) layout1.getChildAt(j));
            if(layout2.ishow()){
                layout2.ChidlViewHide();
                layout2.setIshow(false);
            }
        }
        //隐藏掉其他同级控件
        hideView(v);
        if (ishow) {
            ((LinearLayout) (findViewById(R.id.co_hi_title))).setVisibility(View.GONE);
            ishow = false;
            for (int i = 0; i < ((LinearLayout) findViewById(R.id.co_second_view)).getChildCount(); i++) {
                co_second_view.getChildAt(i).setVisibility(View.GONE);
            }
        } else {
            ((LinearLayout) (findViewById(R.id.co_hi_title))).setVisibility(View.VISIBLE);
            ishow = true;
            //已加载一次数据
            if (!isLoad) {
                for (int i = 0; i < childHistroyPrices.size(); i++) {
                    CoHistorySecondLinearLayout secondView = new CoHistorySecondLinearLayout(context);
                    secondView.setData(childHistroyPrices.get(i).getItemt_name(), childHistroyPrices.get(i).getWorkamt());
                    secondView.setProjectChildren(childHistroyPrices.get(i).getCoAccessories());
                    co_second_view.addView(secondView);
                }
                isLoad = true;
            }

            for (int i = 0; i < ((LinearLayout) findViewById(R.id.co_second_view)).getChildCount(); i++) {
                co_second_view.getChildAt(i).setVisibility(View.VISIBLE);
            }
        }

    }

    public void setIshow(boolean ishow) {
        this.ishow = ishow;
    }

    public void hideTitle() {
        ((LinearLayout) (findViewById(R.id.co_hi_title))).setVisibility(View.GONE);
    }
}
