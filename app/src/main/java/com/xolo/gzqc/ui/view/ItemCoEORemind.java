package com.xolo.gzqc.ui.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.child.CoEngineOilRemind;
import com.xolo.gzqc.bean.child.CoRemind;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.MaintainSubscribeActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * 提醒换机油
 * Created by Administrator on 2016/9/28.
 */
public class ItemCoEORemind extends LinearLayout {
    TextView remind_text_Num, remind_text_title, message;
    Context context;
    Button remind_subscribe_bt, remind_maintain_bt;

    public ItemCoEORemind(Context context) {
        super(context);
        init(context);
    }

    public ItemCoEORemind(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.item_co_eo_remind, this);
        initView();
    }

    String phone;

    public RequestParams creatParams(String action) {
        RequestParams requestParams = new RequestParams(Constant.BASE_URL);
        requestParams.addBodyParameter(Key.ACTION, action);
        return requestParams;
    }


    void initView() {
        remind_text_Num = (TextView) findViewById(R.id.remind_text_num);
        remind_text_title = (TextView) findViewById(R.id.remind_text_title);
        message = (TextView) findViewById(R.id.message);
        remind_subscribe_bt = (Button) findViewById(R.id.remind_subscribe_bt);
        remind_maintain_bt = (Button) findViewById(R.id.remind_maintain_bt);
        remind_maintain_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phone != null) {
                    return;
                }
                readMsg(0, data.getBf_warn_id());

            }
        });

        remind_subscribe_bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                readMsg(1, data.getBf_warn_id());
                //一键预约
            }
        });
    }

    CoEngineOilRemind data;

    public void setData(int num, CoEngineOilRemind data) {
        remind_text_Num.setText(String.valueOf(num));
        remind_text_title.setText(data.getTitle());
        message.setText(data.getContent2());
        this.data = data;
    }

    void readMsg(final int index, String bf_warn_id) {
        LoadDialog.show(context);
        User user = SPManager.getUser(context);
        RequestParams requestParams = creatParams("updateownerwarnstatus");
        requestParams.addBodyParameter("bf_warn_id", bf_warn_id);
        requestParams.addBodyParameter("userid", user.getUser_id());
        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                LoadDialog.dismiss(context);
                if (result.getRes().equals("1")) {
                    if (index == 0) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + phone);
                        intent.setData(data);
                        //联系维修厂
                        if (ActivityCompat.
                                checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {
                            context.startActivity(intent);
                        } else {
                            context.startActivity(intent);
                        }
                    } else {
                        context.startActivity(new Intent(context, MaintainSubscribeActivity.class));
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });

    }


    public void setPhone(String phone) {
        phone = phone;
    }
}
