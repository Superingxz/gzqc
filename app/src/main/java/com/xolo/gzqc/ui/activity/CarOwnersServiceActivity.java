package com.xolo.gzqc.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.bean.child.CoServiceChild;
import com.xolo.gzqc.bean.child.CompanypPhone;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.CarOwnersService;
import com.xolo.gzqc.ui.view.CoServiceChildView;
import com.xolo.gzqc.ui.view.CompanyPhoneView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.utils.L;

/**
 * 车主服务
 * Created by Administrator on 2016/9/26.
 */
public class CarOwnersServiceActivity extends BaseActivity {
    LinearLayout service_car_lv, co_server_layout;
    List<CarOwner_service> models;
    Spinner spinner;
    String[] mItems;
    TextView companyphone;
    List<CompanypPhone> companypPhones = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carowners_service);
        user = SPManager.getUser(mContext);
        initView();
        initData();
        getPhoneData();
    }

    void initSpit() {
        spinner = (Spinner) findViewById(R.id.spinner);
        companyphone.setText(companypPhones.get(0).getCompany_tel());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CarOwnersServiceActivity.this,
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
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    void initData() {
        LoadDialog.show(mContext);

        RequestParams requestParams = creatParams("getserviceinfo");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoService.class, new HttpUtil.HttpCallBack<ORMBean.CoService>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onSuccess(ORMBean.CoService result) {
                if (result.getRes().equals("1")) {
                    for (CarOwner_service service : result.getData()) {
                        CarOwnersService carOwnersService = new CarOwnersService(mContext);
                        carOwnersService.setDate(service, user);
                        service_car_lv.addView(carOwnersService);
                    }
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }

    private void initView() {
        companyphone = (TextView) findViewById(R.id.companyphone);
        co_server_layout = (LinearLayout) findViewById(R.id.co_server_layout);
        service_car_lv = (LinearLayout) findViewById(R.id.service_car_lv);
    }
}
