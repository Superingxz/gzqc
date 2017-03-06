package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.ProjectChild;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CoHistorySecondLinearLayout extends LinearLayout implements View.OnClickListener {
    LinearLayout addView_layout;
    Context context;
    List<CoAccessories> projectChildren;

    public CoHistorySecondLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public CoHistorySecondLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setClickable(true);
        setOnClickListener(this);
        View.inflate(context, R.layout.item_co_project_list, this);
        addView_layout = (LinearLayout) findViewById(R.id.addView_layout);
    }

    public void setData(String project_name, String project_price) {
        ((TextView) findViewById(R.id.project_name)).setText(project_name);
        ((TextView) findViewById(R.id.project_price)).setText(project_price);
    }

    boolean ishow, isLoad;

    @Override
    public void onClick(View v) {
        if(projectChildren==null){
            return;
        }
        hideView(v);
        if (!ishow) {
            if (!isLoad) {
                for (int i=0;i<projectChildren.size();i++) {
                    CoHistoryThirdlyLinearLayout layout = new CoHistoryThirdlyLinearLayout(context);
                   layout.setData(projectChildren.get(i));
                    if(i==0){
                        layout.showTitle();
                    }
                    addView_layout.addView(layout);
                }
                isLoad = true;
            }
            for(int i=0;i<addView_layout.getChildCount();i++){
                addView_layout.getChildAt(i).setVisibility(View.VISIBLE);
            }
            ishow=true;
        } else {
            ishow=false;
            for(int i=0;i<addView_layout.getChildCount();i++){
                addView_layout.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    public List<CoAccessories> getProjectChildren() {
        return projectChildren;
    }

    public void setProjectChildren(List<CoAccessories> projectChildren) {
        this.projectChildren = projectChildren;
    }
    public void hideView(View v){
        for(int i=0;i<((LinearLayout)this.getParent()).getChildCount();i++){
            CoHistorySecondLinearLayout layout=
                    (CoHistorySecondLinearLayout) ((LinearLayout)this.getParent()).getChildAt(i);
            if(((CoHistorySecondLinearLayout)v)!=layout){
                layout.ChidlViewHide();
                layout.setIshow(false);
            }

        }
    }
    public void ChidlViewHide(){
        for(int i=0;i<addView_layout.getChildCount();i++){
            addView_layout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    public boolean ishow() {
        return ishow;
    }

    public void setIshow(boolean ishow) {
        this.ishow = ishow;
    }
}
