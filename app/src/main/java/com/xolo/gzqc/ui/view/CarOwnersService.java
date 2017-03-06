package com.xolo.gzqc.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.bean.child.CoServiceChild;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/10/8.
 */
public class CarOwnersService extends LinearLayout {
    User user;
    CarOwner_service carOwner_services;
    Context context;

    public CarOwnersService(Context context) {
        super(context);
        init(context);
    }

    public CarOwnersService(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    LinearLayout linearLayout;
    void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.item_carowners_service, this);
        linearLayout = (LinearLayout) findViewById(R.id.child_layout);
    }

    public void setDate(CarOwner_service carOwner_services, User user) {
        this.carOwner_services = carOwner_services;
        this.user = user;
        TextView textView = (TextView) findViewById(R.id.co_server_title_namae);
        textView.setText(carOwner_services.getService_name());
        initData();
    }

    public RequestParams creatParams(String action) {
        RequestParams requestParams = new RequestParams(Constant.BASE_URL);
        requestParams.addBodyParameter(Key.ACTION, action);
        return requestParams;
    }
    void initData() {
        LoadDialog.show(context);
        RequestParams requestParams = creatParams("getservicetitle");
        requestParams.addBodyParameter("bigcode",carOwner_services.getBigcode());
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoSvChild.class, new HttpUtil.HttpCallBack<ORMBean.CoSvChild>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(context);
            }
            @Override
            public void onSuccess(ORMBean.CoSvChild result) {
                if (result.getRes().equals("1")) {
                    for(CoServiceChild child:result.getData()){
                        CoServiceChildView coServiceChildView=new CoServiceChildView(context);
                        coServiceChildView.setData(child);
                        linearLayout.addView(coServiceChildView);
                    }
                } else {
                    ToastUtil.showShort(context, result.getMsg());
                }
                LoadDialog.dismiss(context);
            }
        });

    }

}
