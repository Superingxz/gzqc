package com.xolo.gzqc.ui.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 注册引导页面
 */
public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_register_supplier, R.id.btn_register_dept, R.id.btn_register_consumers})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_supplier:
                startActivity(new Intent(mContext,RegisterSupplierActivity.class));
                break;
            case R.id.btn_register_dept:
                startActivity(new Intent(mContext,RegisterDeftActivity.class));
                break;
            case R.id.btn_register_consumers:
                startActivity(new Intent(mContext,RegisterConsumersActivity.class));
                break;
        }
    }

}
