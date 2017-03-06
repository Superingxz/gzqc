package com.xolo.gzqc.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CoServiceChild;
import com.xolo.gzqc.ui.activity.CoServiceChildActivity;

/**
 * Created by Administrator on 2016/10/8.
 */
public class CoServiceChildView extends LinearLayout implements View.OnClickListener {
    CoServiceChild coServiceChild;
    Context context;
    public CoServiceChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init( context);

    }

    public CoServiceChildView(Context context) {
        super(context);
        init( context);
    }

    void init(Context context) {
        this.context=context;
        setOnClickListener(this);
        View.inflate(context, R.layout.item_co_server_child, this);
    }

    public void setData(CoServiceChild coServiceChild) {
        this.coServiceChild = coServiceChild;
        TextView textView=(TextView) findViewById(R.id.service_child_name);
        textView.setText(coServiceChild.getTitle_name());

    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context, CoServiceChildActivity.class);
        intent.putExtra("title",coServiceChild.getTitle_name());
        intent.putExtra("bf_service_m_id",coServiceChild.getBf_service_m_id());
        ((Activity)context).startActivity(intent);
    }
}
