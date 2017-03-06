package com.xolo.gzqc.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.LoginType;
import com.xolo.gzqc.bean.child.UpDate;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.runtimepermissions.PermissionsManager;
import com.xolo.gzqc.runtimepermissions.PermissionsResultAction;
import com.xolo.gzqc.service.UpdateService;
import com.xolo.gzqc.ui.activity.register.RegisterActivity;
import com.xolo.gzqc.utils.AppUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.SPUtils;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_remenber)
    CheckBox cbRemenber;
    @BindView(R.id.cb_autologin)
    CheckBox cbAutologin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.version)
    TextView version;

    private String name;
    private String password;

    private String oldPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        LogUtil.d(Build.MODEL.toLowerCase());

        version.setText("当前版本：v" + AppUtils.getVersionName(mContext));

        RongIM.getInstance().logout();

//        判断账号是否在别的机子登录
        Intent intent = getIntent();
        String toast = intent.getStringExtra("toast");
        if (!TextUtils.isEmpty(toast)) {
            Toast.makeText(LoginActivity.this, toast, Toast.LENGTH_LONG).show();
        }

        oldPhone = (String) SPUtils.get(this, SPManager.USER, "");
        boolean isRemenber = (boolean) SPUtils.get(this, SPManager.IS_REMENBER, false);
        boolean isAuto = (boolean) SPUtils.get(mContext, SPManager.IS_AUTO_LOGIN, false);
        cbAutologin.setChecked(isAuto);
        cbRemenber.setChecked(isRemenber);

        if (isRemenber) {
            readSP();
        }

        if (App.isFirstLogin) {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions();
                int checkPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (checkPermission == PackageManager.PERMISSION_GRANTED) {
                    getnewversionno();
                }
            } else {
                getnewversionno();
            }
        }
    }

    private void readSP() {
//        name = (String) SPUtils.get(this, SPManager.USER, "");
        password = (String) SPUtils.get(this, SPManager.PASSWORD_2, "");
        etName.setText(oldPhone);
        etPassword.setText(password);
    }

    @OnClick({R.id.login, R.id.loss_passWord,R.id.btn_register,R.id.tv_invitation})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                login();
                break;
            case R.id.loss_passWord:
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(mContext,RegisterActivity.class));
                break;
            case R.id.tv_invitation:
                startActivity(new Intent(mContext,CheckVersionActivity.class));
                break;
        }
    }

    /**
     * 登录
     */
    public void login() {

        App.setIsFirstLogin(false);

        name = etName.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showLong(mContext, "账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.showLong(mContext, "密码不能为空");
            return;
        }

        postLogin(name, password);
    }


    private void addStateListener() {
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            /**
             * 设置连接状态变化的监听器.
             */
            RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                @Override
                public void onChanged(ConnectionStatus connectionStatus) {

                    switch (connectionStatus) {

                        case CONNECTED://连接成功。
                            break;
                        case DISCONNECTED://断开连接。

                            break;
                        case CONNECTING://连接中。
                            break;
                        case NETWORK_UNAVAILABLE://网络不可用。
                            break;
                        case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                            CarOwnersActivity.isLogout=true;
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("toast", "用户账户在其他设备登录，本机被踢掉线");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            break;
                    }
                }
            });

        }
    }


    private void postLogin(String phone, String password) {
//        保存登录账户和密码
        if (cbRemenber.isChecked()) {
            SPUtils.put(LoginActivity.this, SPManager.USER, name);
            SPUtils.put(LoginActivity.this, SPManager.PASSWORD_2, password);
            SPUtils.put(LoginActivity.this, SPManager.IS_REMENBER, true);
        } else {
            SPUtils.put(LoginActivity.this, SPManager.IS_REMENBER, false);
        }
        SPUtils.put(mContext, SPManager.IS_AUTO_LOGIN, cbAutologin.isChecked());


        mLoad.show(mContext);
        RequestParams requestParams = creatParams("getloginuserinfor");
        requestParams.addBodyParameter(Key.PHONE, phone);
        requestParams.addBodyParameter(Key.PASSWORD, password);

        HttpUtil.getInstance().post(requestParams, ORMBean.LoginTypeBean.class, new HttpUtil.HttpCallBack<ORMBean.LoginTypeBean>() {
            @Override
            public void onSuccess(ORMBean.LoginTypeBean result) {

                if (result.getRes().equals("1")) {
                    LoginType user = result.getData().get(0);

                    Intent intent = new Intent(mContext, SelectModelActivity.class);
                    intent.putExtra(IntentConstant.LOGINTYPE,user);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }



    /**
     * 1-7 版本升级接口,获取当前最新版本号 getnewversionno(userid,app_type)
     * 当前用户ID：userid,app类别（类别分“ANDROID”,“IOS”）：app_type
     */
    private void getnewversionno() {
        RequestParams params = creatParams("getnewversionno");
        params.addBodyParameter("app_type", "ANDROID");

        mLoad.show(mContext);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.UpDateBean.class, new HttpUtil.HttpCallBack<ORMBean.UpDateBean>() {
            @Override
            public void onSuccess(ORMBean.UpDateBean result) {
                if (result.getRes().equals("1")) {

                    final UpDate upDate = result.getData().get(0);
                    int newVerson = Integer.parseInt(upDate.getVersionno());

                    int versionCode = AppUtils.getVersionCode(mContext);

                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_updata, null);
                    TextView textView = (TextView) inflate.findViewById(R.id.tv_message);
                    textView.setText(upDate.getUpdate_content());

//                     判断是否须有更新
                    if (versionCode < newVerson) {
                        new AlertDialog.Builder(mContext).setView(inflate).setPositiveButton("下载安装", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ToastUtil.showShort(mContext, getString(R.string.downloading_apk));
                                UpdateService.Builder.create(upDate.getDownload_url())
                                        .setStoreDir("update/flag")
                                        .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                                        .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                                        .build(mContext);
                            }
                        }).setNegativeButton("取消", null).show();
                    } else {
//                         自动登录
                        if (App.isFirstLogin) {
                            boolean isAuto = (boolean) SPUtils.get(mContext, SPManager.IS_AUTO_LOGIN, false);

                            if (isAuto) {
                                readSP();
                                login();
                                return;
                            }
                        }
                    }

                }
                mLoad.dismiss(mContext);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }



    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
                        getnewversionno();
            }

            @Override
            public void onDenied(String permission) {
//                Toast.makeText(mContext, "缺少" + permission + "会导致部分功能无法使用", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }



}
