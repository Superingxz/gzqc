package com.xolo.gzqc.ui.activity.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 店铺公告
 * Created by Administrator on 2016/12/9.
 */
public class NoticeActivity extends BaseActivity{
    @BindView(R.id.notice_centre_et)
    EditText notice_centre_et;
    @BindView(R.id.shart_time)
    TextView shart_time;
    @BindView(R.id.end_time)
    TextView end_time;
    String announceid;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.et_img_layout)
    RelativeLayout et_img_layout;
    @BindView(R.id.et_img_layout2)
    RelativeLayout et_img_layout2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_notice);
        ButterKnife.bind(this);
        getNotice();
    }
    @OnClick({R.id.submit,R.id.et_img_layout,R.id.et_img_layout2})
    void  OnClick(View view){
        switch (view.getId()){
            case  R.id.submit:
                submitNotice();
                break;
            case R.id.et_img_layout2:
                creatDateDialog(end_time).show();
                break;
            case R.id.et_img_layout:
                creatDateDialog(shart_time).show();
                break;
        }
    }

    void submitNotice(){
        if(TextUtils.isEmpty(shart_time.getText().toString().trim())){
            ToastUtil.showShort(NoticeActivity.this,"请填写公告开始时间");
            return;
        }
        if(TextUtils.isEmpty(end_time.getText().toString().trim())){
            ToastUtil.showShort(NoticeActivity.this,"请填写公告结束时间");
            return;
        }
        if(TextUtils.isEmpty(notice_centre_et.getText().toString().trim())){
            ToastUtil.showShort(NoticeActivity.this,"请填写公告");
            return;
        }
        LoadDialog.show(NoticeActivity.this);
        RequestParams requestParams = creatParams("saveshopnotice");
        requestParams.addBodyParameter("noticeid",announceid);
        requestParams.addBodyParameter("notice",notice_centre_et.getText().toString());
        requestParams.addBodyParameter("starttime",shart_time.getText().toString());
        requestParams.addBodyParameter("endtime",end_time.getText().toString());
        HttpUtil.getInstance().post(requestParams, ORMBean.NoticeBean.class, new HttpUtil.HttpCallBack<ORMBean.NoticeBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(NoticeActivity.this);
            }

            @Override
            public void onSuccess(ORMBean.NoticeBean result) {
                LoadDialog.dismiss(NoticeActivity.this);
                if(result.getRes().equals("1")){
                    ToastUtil.showShort(NoticeActivity.this,result.getMsg());
                    finish();
                }else{
                    ToastUtil.showShort(NoticeActivity.this,result.getMsg());
                }

            }
        });
    }



    void getNotice(){
        LoadDialog.show(NoticeActivity.this);
        user = SPManager.getUser(NoticeActivity.this);
        RequestParams requestParams = creatParams("getshopnotice");
        HttpUtil.getInstance().post(requestParams, ORMBean.NoticeBean.class, new HttpUtil.HttpCallBack<ORMBean.NoticeBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(NoticeActivity.this);
            }

            @Override
            public void onSuccess(ORMBean.NoticeBean result) {
                if(result.getRes().equals("1")){
                    end_time.setText(result.getData().get(0).getEndtime());
                    shart_time.setText(result.getData().get(0).getStartime());
                    notice_centre_et.setText(result.getData().get(0).getAnnouncement());
                    announceid=result.getData().get(0).getAnnounceid();
                }
                LoadDialog.dismiss(NoticeActivity.this);
            }
        });
    }
}
