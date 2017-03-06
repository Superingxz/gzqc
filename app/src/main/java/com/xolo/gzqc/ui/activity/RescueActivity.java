package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.CarOwnersService;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/10/8.
 */
public class RescueActivity extends BaseActivity

{
    RelativeLayout rescue_road,rescue_car;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue);
        rescue_road=(RelativeLayout)findViewById(R.id.rescue_road);
        rescue_car=(RelativeLayout)findViewById(R.id.rescue_car);
        rescue_road.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RescueActivity.this,CoRoadInsuranceActivity.class);
                startActivity(intent);
            }
        });
        rescue_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RescueActivity.this,CoRescueInsuranceActivity.class);
                startActivity(intent);
            }
        });
    }

}
