package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.bean.child.CompanypPhone;
import com.xolo.gzqc.ui.view.CarOwnersService;
import com.xolo.gzqc.ui.view.CompanyPhoneView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/10/11.
 */
public class CoRescueInsuranceActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout co_server_layout;
    Spinner spinner;
    String[] mItems;
    List<CompanypPhone> companypPhones = new ArrayList<>();
    TextView alarm_tel_text, dp_id, trailer, road_guiide, companyphone;
    Button send_bt_depot, tow_truck_bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rescue_insurance);
        initView();
        getPhoneData();
        initData2();

    }


    private void initView() {
        send_bt_depot = (Button) findViewById(R.id.send_bt_depot);
        tow_truck_bt = (Button) findViewById(R.id.tow_truck_bt);
        spinner = (Spinner) findViewById(R.id.spinner);
        companyphone = (TextView) findViewById(R.id.companyphone);

        trailer = (TextView) findViewById(R.id.trailer);
        dp_id = (TextView) findViewById(R.id.dp_id);
        co_server_layout = (LinearLayout) findViewById(R.id.co_server_layout);
//        dp_id_text = (TextView) findViewById(R.id.dp_id_text);
//        trailer_text = (TextView) findViewById(R.id.trailer_text);
        alarm_tel_text = (TextView) findViewById(R.id.alarm_tel_text);
        road_guiide = (TextView) findViewById(R.id.road_guiide);
        //   dp_id_text.setOnClickListener(this);
        companyphone.setOnClickListener(this);
        dp_id.setOnClickListener(this);
        trailer.setOnClickListener(this);
        //  alarm_tel_text.setOnClickListener(this);
        // trailer_text.setOnClickListener(this);
        tow_truck_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        send_bt_depot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(dp_id.getText().toString())) {
                    startFriendDetailsPage(dp_id.getText().toString());
                } else {
                    ToastUtil.showShort(mContext, "没有数据");
                }
            }
        });
    }

    private void startFriendDetailsPage(String id) {
        RongIM.getInstance().startPrivateChat(CoRescueInsuranceActivity.this, id, "");
    }

    String phone, phone1;

    void initData2() {
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("roadsideservice");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoRescueBean.class, new HttpUtil.HttpCallBack<ORMBean.CoRescueBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoRescueBean result) {
                if (result.getRes().equals("1")) {
                    //  dp_id_text.setText(result.getData().get(0).getPhone());
                    dp_id.setText(user.getDept_name() + ": " + result.getData().get(0).getPhone());
                    alarm_tel_text.setText(result.getData().get(0).getAlarm_tel());
                    //  trailer_text.setText(result.getData().get(0).getTrailer_tel());//拖车电话
                    trailer.setText(result.getData().get(0).getTrailer_name() + ": " + result.getData().get(0).getTrailer_tel());//拖车名字
                    road_guiide.setText(result.getData().get(0).getCar_guide());//车辆出险指南
                    phone = result.getData().get(0).getPhone();
                    phone1 = result.getData().get(0).getTrailer_tel();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    void getPhoneData() {
        LoadDialog.show(mContext);
        RequestParams requestParams = creatParams("getinsurancecompany");
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoCompanyPhoneBean.class, new HttpUtil.HttpCallBack<ORMBean.CoCompanyPhoneBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoCompanyPhoneBean result) {
                if (result.getRes().equals("1")) {
                    companypPhones.clear();
                    companypPhones = result.getData();
                    mItems = new String[companypPhones.size()];
                    for (int i = 0; i < result.getData().size(); i++) {
                        mItems[i] = companypPhones.get(i).getCompany_name();
                    }
                    initSpit();
//                    for (int i = 0; i < result.getData().size(); i++) {
                    CompanyPhoneView companypPhone = new CompanyPhoneView(CoRescueInsuranceActivity.this);
//                        companypPhone.setDate(result.getData().get(i).getCompany_name(),
//                                result.getData().get(i).getCompany_tel());
//                        co_server_layout.addView(companypPhone);
//                    }
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    void initSpit() {
        companyphone.setText(companypPhones.get(0).getCompany_tel());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CoRescueInsuranceActivity.this,
                android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                companyphone.setText(companypPhones.get(pos).getCompany_tel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
//测试测试测试22222222222222222222222
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
            case R.id.companyphone:
                if(!TextUtils.isEmpty(companyphone.getText().toString())){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + companyphone.getText().toString()));
                    startActivity(intent);
                }
                break;
        }
    }
}
