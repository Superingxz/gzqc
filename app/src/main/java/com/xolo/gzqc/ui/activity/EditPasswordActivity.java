package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPasswordActivity extends BaseActivity {

    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_edit_password)
    public void onClick() {
        setrepairmodifypassword();
    }

    /**
     * 2-6 维修厂员工修改密码接口 setrepairmodifypassword(userid,pws) 当前用户ID:userid 密码:pws
     */
    private void setrepairmodifypassword() {
        String s = etPassword.getText().toString();
        if (s.length() < 6) {
            ToastUtil.showShort(mContext, "请输入6位新密码");
            return;
        }

        RequestParams params = creatParams("setrepairmodifypassword");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("pws", s);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
