package com.xolo.gzqc.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.utils.AMUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.SPUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.downtime.DownTimer;
import com.xolo.gzqc.utils.downtime.DownTimerListener;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 忘记密码
 */
public class PasswordActivity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verification_code)
    EditText etVerificationCode;
    @BindView(R.id.getCode)
    Button getCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.confirm)
    Button confirm;


    /**
     * 倒计时
     */
    private DownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        downTimer = new DownTimer();
        downTimer.setListener(new DownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                getCode.setText("重新发送(" + (millisUntilFinished / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                getCode.setClickable(true);
                getCode.setText("获取验证码");
                getCode.setBackgroundColor(getResources().getColor(R.color.DarkBlue));
            }
        });
    }


    /**
     * 1-3 忘记密码接口，通过短信验证码、手机号及新密码，将密码重置 forgetpassword( phone,pws )
     */
    private void postPassWord() {
        String phone = getText(etPhone);
        String password = getText(etPassword);
        String code = getText(etVerificationCode);
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShort(mContext, "请先填写手机号码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showShort(mContext, "请先填写新密码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.showShort(mContext, "请先获取验证码");
            return;
        }

        String massageCode = (String) SPUtils.get(mContext, "massageCode", "");
        if (!code.equals(massageCode)) {
            ToastUtil.showShort(mContext, "输入验证码错误");
            return;
        }

        String massagePhone = (String) SPUtils.get(mContext, "massagePhone", "");
        if (!getText(etPhone).equals(massagePhone)) {
            ToastUtil.showShort(mContext, "输入验证码错误");
            return;
        }

        RequestParams params = creatParams("forgetpassword");
        params.addBodyParameter("phone", etPhone.getText().toString().trim());
        params.addBodyParameter("pws", etPassword.getText().toString().trim());

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

    @OnClick({R.id.getCode, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getCode:
                sendverificationcode();
                break;
            case R.id.confirm:
                postPassWord();
                break;
        }
    }

    /**
     * 1-4 发送验证码短信接口 sendverificationcode(mobile)
     * 手机号码：mobile
     */
    private void sendverificationcode() {

        final String phone = getText(etPhone);
        if (!AMUtils.isMobile(phone)) {
            ToastUtil.showShort(mContext, "请先填写正确的手机号码");
            return;
        }

        RequestParams params = creatParams("sendverificationcode");
        params.addBodyParameter("mobile", phone);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                String msg = result.getMsg();
                SPUtils.put(mContext, "massageCode", msg);
                SPUtils.put(mContext, "massagePhone",phone);

                downTimer.startDown(60000);
                getCode.setClickable(false);
                getCode.setBackgroundColor(getResources().getColor(R.color.activity_bg));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


}
