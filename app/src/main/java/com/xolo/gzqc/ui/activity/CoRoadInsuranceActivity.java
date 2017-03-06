package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.ui.view.CarOwnersService;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/10/11.
 */
public class CoRoadInsuranceActivity extends BaseActivity implements View.OnClickListener{
    LinearLayout top_layout;
    TextView alarm_tel_text,dp_id,trailer,road_guiide,insurance_text;
    Button send_bt_depot,tow_truck_bt;
    TitleView titleview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_insurance);
        initView();
        initData2();
    }

    private void initView() {
        titleview=(TitleView)findViewById(R.id.titleview);
        titleview.setTitle("道路救援");
        send_bt_depot=(Button)findViewById(R.id.send_bt_depot);
        tow_truck_bt=(Button)findViewById(R.id.tow_truck_bt);
        trailer=(TextView) findViewById(R.id.trailer);
        dp_id=(TextView) findViewById(R.id.dp_id);
        top_layout = (LinearLayout) findViewById(R.id.top_layout);
       // dp_id_text=(TextView) findViewById(R.id.dp_id_text);
        //trailer_text=(TextView) findViewById(R.id.trailer_text);
        alarm_tel_text=(TextView) findViewById(R.id.alarm_tel_text);
        insurance_text=(TextView) findViewById(R.id.insurance_text);
        dp_id.setOnClickListener(this);
        trailer.setOnClickListener(this);
        road_guiide=(TextView) findViewById(R.id.road_guiide);
        insurance_text.setText("道路救援指南");

        top_layout.setVisibility(View.GONE);
        //dp_id_text.setOnClickListener(this);
        alarm_tel_text.setOnClickListener(this);
       // trailer_text.setOnClickListener(this);
        tow_truck_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startFriendDetailsPage(trailer_text.getText().toString());

            }
        });
        send_bt_depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(dp_id.getText().toString())){
                    startFriendDetailsPage(dp_id.getText().toString());
                }else{
                    ToastUtil.showShort(mContext,"没有数据");
                }

            }
        });
    }
    private void startFriendDetailsPage(String id) {
        RongIM.getInstance().startPrivateChat(CoRoadInsuranceActivity.this,id, "");
    }
    String phone,phone1;
    void initData2() {
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("roadsideservice");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid",user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoRescueBean.class, new HttpUtil.HttpCallBack<ORMBean.CoRescueBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoRescueBean result) {
                if (result.getRes().equals("1")) {
                    //dp_id_text.setText();
                    dp_id.setText(user.getDept_name()+": "+result.getData().get(0).getPhone());
                    phone=result.getData().get(0).getPhone();
                    phone1=result.getData().get(0).getTrailer_tel();
                    alarm_tel_text.setText(result.getData().get(0).getAlarm_tel());
                    //trailer_text.setText();//拖车电话
                    trailer.setText(result.getData().get(0).getTrailer_name()+": "+result.getData().get(0).getTrailer_tel());//拖车名字
                    road_guiide.setText(result.getData().get(0).getRoad_guiide());//道路出险指南
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dp_id:
                if (!TextUtils.isEmpty(phone)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    startActivity(intent);
                }
                break;
            case R.id.trailer:
                if (!TextUtils.isEmpty(phone1)) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone1));
                    startActivity(intent);
                }
                break;
        }


    }
}
