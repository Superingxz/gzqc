package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarOwner_service;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.CarOwnersService;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/10/8.
 */
public class CoServiceChildActivity  extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coservice_child);
        initView();

    }

    private void initView() {
        String title= getIntent().getExtras().getString("title");
        TitleView titleView=(TitleView) findViewById(R.id.service_title);
        titleView.setTitle(title);
        service_content=(TextView)findViewById(R.id.service_content);

        String bf_service_m_id= getIntent().getExtras().getString("bf_service_m_id");

        initData(bf_service_m_id);


    }
    TextView service_content;
    void initData(String bf_service_m_id){
        LoadDialog.show(mContext);
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getservicecontent");
        requestParams.addBodyParameter("bf_service_m_id",bf_service_m_id);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoServiceContentBean.class, new HttpUtil.HttpCallBack<ORMBean.CoServiceContentBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(ORMBean.CoServiceContentBean result) {
                if(result.getRes().equals("1")){
                    service_content.setText(result.getData().get(0).getContent());
                }else{
                    ToastUtil.showShort(mContext,result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });

    }
}
