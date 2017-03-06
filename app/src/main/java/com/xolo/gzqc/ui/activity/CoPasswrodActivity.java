package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2016/9/28.
 */
public class CoPasswrodActivity extends BaseActivity {
    EditText maintain_et1,maintain_et;
    Button submit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_password);
        initView();
    }

    private void initView() {
        submit=(Button)findViewById(R.id.submit);
        maintain_et1=(EditText)findViewById(R.id.maintain_et1);
        maintain_et=(EditText)findViewById(R.id.maintain_et);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(maintain_et1.getText())&&TextUtils.isEmpty(maintain_et.getText())){
                    ToastUtil.showShort(CoPasswrodActivity.this,"请输入密码");
                    return;
                }else if(TextUtils.isEmpty(maintain_et1.getText())){
                    ToastUtil.showShort(CoPasswrodActivity.this,"请输入确认密码");
                    return;
                }
                if(maintain_et.getText().toString().equals(maintain_et1.getText().toString())){
                    LoadDialog.show(mContext);
                    user = SPManager.getUser(mContext);
                    RequestParams requestParams = creatParams("updateownerpwd");
                    requestParams.addBodyParameter("bc_car_owner_id",user.getUser_id());
                    requestParams.addBodyParameter("password", maintain_et.getText().toString());
                    requestParams.addBodyParameter("userid", user.getUser_id());

                    HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            LoadDialog.dismiss(mContext);
                        }

                        @Override
                        public void onSuccess(BaseBean result) {
                            ToastUtil.showShort(mContext,result.getMsg());
                            LoadDialog.dismiss(mContext);
                            finish();
                        }
                        });

                }else{
                    ToastUtil.showShort(CoPasswrodActivity.this,"2次输入的密码不相同");
                }

            }
        });
    }
}
