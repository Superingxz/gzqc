package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;

/**
 * Created by Administrator on 2016/10/12.
 */
public class CoSafetyCompanyView extends LinearLayout {

    public CoSafetyCompanyView(Context context) {
        super(context);
        initView(context);
    }

    public CoSafetyCompanyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        View.inflate(context, R.layout.item_co_safety_view, this);

    }

    public void setData(String name, String phone) {

        TextView name_text = (TextView) findViewById(R.id.name);
        TextView phone_text = (TextView) findViewById(R.id.phone);
        name_text.setText(name+":");
        phone_text.setText(phone);
    }


}
