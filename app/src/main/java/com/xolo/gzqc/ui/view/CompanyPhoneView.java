package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;

/**
 * Created by Administrator on 2016/10/10.
 */
public class CompanyPhoneView extends LinearLayout{

    public CompanyPhoneView(Context context) {
        super(context);
        initView(context);
    }

    public CompanyPhoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    void  initView(Context context){
        View.inflate(context, R.layout.item_company,this);

    }

    public  void setDate(String name,String phone){
        ((TextView)findViewById(R.id.companyname)).setText(name);
        ((TextView)findViewById(R.id.companyphone)).setText(phone);

    }

}
