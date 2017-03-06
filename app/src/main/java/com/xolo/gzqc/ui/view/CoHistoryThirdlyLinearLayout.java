package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoAccessories;
import com.xolo.gzqc.bean.child.ProjectChild;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class CoHistoryThirdlyLinearLayout extends LinearLayout {


    public void showTitle(){

    this.findViewById(R.id.project_child_title).setVisibility(View.VISIBLE);

    }
    public void setData(CoAccessories projectChild){
        ((TextView)   findViewById(R.id.accessories_name))
                .setText(projectChild.getParts_name());

        ((TextView)   findViewById(R.id.accessories_util_price))
                .setText(projectChild.getSaleprice());

        ((TextView)   findViewById(R.id.accessories_num))
                .setText(projectChild.getQty());

        ((TextView)   findViewById(R.id.accessories_price))
                .setText(projectChild.getTotalprice());
    }
    public CoHistoryThirdlyLinearLayout(Context context) {
        super(context);
        init(context);
    }
    public CoHistoryThirdlyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    void  init(Context context){
        View.inflate(context, R.layout.item_co_project_child,this);
   }
    
    
}
