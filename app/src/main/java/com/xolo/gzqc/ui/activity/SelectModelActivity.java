package com.xolo.gzqc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xolo.gzqc.App;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.LoginType;
import com.xolo.gzqc.bean.child.Role;
import com.xolo.gzqc.bean.child.User;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.administrator.AdministratorMainActivity;
import com.xolo.gzqc.ui.activity.boss.BossMainActivity;
import com.xolo.gzqc.ui.activity.consumers.ConsumersActivity;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.activity.procurement.ProcurementMainActivity;
import com.xolo.gzqc.ui.activity.supplier.SupplierActivity;
import com.xolo.gzqc.ui.activity.team.TeamMainActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.SPUtils;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class SelectModelActivity extends BaseActivity {

    @BindView(R.id.btn_carowner)
    LinearLayout btnCarowner;
    @BindView(R.id.btn_dept)
    LinearLayout btnDept;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.btn_consumers)
    LinearLayout btnConsumers;
    @BindView(R.id.btn_supplier)
    LinearLayout btnSupplier;
    private LoginType loginInfo;
    private List<LoginType.TypeBean> type;

    private String oldPhone;

    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_model);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        oldPhone = (String) SPUtils.get(this, SPManager.USER, "");

        loginInfo = getIntent().getParcelableExtra(IntentConstant.LOGINTYPE);
        type = loginInfo.getType();

        if (isAble("0")) {
          btnCarowner.setVisibility(View.VISIBLE);
        }
        if (isAble("1")) {
          btnDept.setVisibility(View.VISIBLE);
        }
        if (isAble("2")) {
          btnConsumers.setVisibility(View.VISIBLE);
        }
        if (isAble("3")) {
          btnSupplier.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.btn_carowner, R.id.btn_dept, R.id.btn_consumers, R.id.btn_supplier})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_carowner:
                getuserinfobyusertype("0");
                break;
            case R.id.btn_dept:
                getuserinfobyusertype("1");
                break;
            case R.id.btn_consumers:
                getuserinfobyusertype("2");
                break;
            case R.id.btn_supplier:
                getuserinfobyusertype("3");
                break;
        }
    }

    /**
     * 判断是否有角色权限
     *
     * @param typeSelect
     * @return
     */
    private boolean isAble(String typeSelect) {
        for (LoginType.TypeBean t : type) {
            if (typeSelect.equals(t.getUser_type())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 1-11 通过用户手机号和用户类型获取用户信息接口（用于点击模块进入时使用） getuserinfobyusertype(phone,user_type)
     * 用户手机号码：phone,用户类型（0为车主，1为维修厂员工，2为消费者，3为供应商）：user_type
     */
    private void getuserinfobyusertype(String type) {
        RequestParams params = creatParams("getuserinfobyusertype");
        params.addBodyParameter("phone", loginInfo.getPhone());
        params.addBodyParameter("user_type", type);

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.LoginBean.class, new HttpUtil.HttpCallBack<ORMBean.LoginBean>() {
            @Override
            public void onSuccess(ORMBean.LoginBean result) {
                if (result.getRes().equals("1")) {
                    User user = result.getData().get(0);

                    // 账号不变，直接登录上一次token
//                          if (loginInfo.getPhone().equals(oldPhone)) {
//                              String taken = (String) SPUtils.get(mContext, SPManager.TAKEN, "");
//                              connect(taken, user);
//                          } else
                    getusertoken(user);
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    mLoad.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });

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
                            CarOwnersActivity.isLogout = true;
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("toast", "用户账户在其他设备登录，本机被踢掉线");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            RongIM.getInstance().logout();
                            break;
                    }
                }
            });


//            RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
//                @Override
//                public void onChanged(ConnectionStatus connectionStatus) {
//                    CarOwnersActivity.isLogout=true;
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("toast", "用户账户在其他设备登录，本机被踢掉线");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//
//                }
//            });
        }
    }


    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connect(final String token, final User mUser) {

        /**
         * IMKit SDK调用第二步,建立与服务器的连接
         */
        RongIM.connect(token, new RongIMClient.ConnectCallback() {

            /**
             * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
             */
            @Override
            public void onTokenIncorrect() {
                Log.d("LoginActivity", "--onTokenIncorrect");
                getusertoken(mUser);

//                ToastUtil.showShort(mContext,"token过时");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token
             */
            @Override
            public void onSuccess(String userid) {
                addStateListener();

                LogUtil.i("123");

                SPManager.saveUser(mContext, mUser);
                SPUtils.put(SelectModelActivity.this, SPManager.TAKEN, token);

                mLoad.dismiss(mContext);

                String isfemp = mUser.getIsfemp();

//                isfemp   0为车主，1为维修厂员工，2为消费者，3为供应商
                if (isfemp.equals("2")) {
                    startActivity(new Intent(mContext, ConsumersActivity.class));
                    finish();
                    return;
                }
                if (isfemp.equals("0")) {
                    //获取车主联系人
                    getlistdeptuser(mUser, 4);
                    return;
                }
                if (isfemp.equals("3")) {
                    startActivity(new Intent(mContext, SupplierActivity.class));
                    finish();
                    return;
                }

                if (mUser.getRole_count().equals("1")) {
                    getRole(mUser);
                } else {
//                        多角色以接车人联系人为主
                    getlistdeptuser(mUser, 1);
                }
            }


            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             *                  http://www.rongcloud.cn/docs/android.html#常见错误码
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
//                ToastUtil.showLong(mContext, "登录聊天失败" + errorCode);
                LogUtil.i(errorCode.getValue() + "");
                getusertoken(mUser);
//                mLoad.dismiss(mContext);
            }
        });
    }


    /**
     * 1-5 获取用户token接口 getusertoken(userid,name,portraitUri)
     * 当前用户ID：id,用户姓名:name,头像(没有留空，系统自动读取默认头像):portraitUri
     */
    private void getusertoken(final User user) {
        RequestParams params = creatParams("getusertoken");
        params.addBodyParameter("id", loginInfo.getPhone());
        params.addBodyParameter("name", user.getUser_name() == null ? user.getName() : user.getUser_name());
        params.addBodyParameter("portraitUri", "");

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    connect(result.getMsg(), user);
                } else {
                    ToastUtil.showShort(mContext, "获取token值失败");
                    mLoad.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

    /**
     * 1-6 获取本维修厂的用户列表(含车主)接口 getlistdeptuser(userid,dept_id)
     * 当前用户ID：userid,维修厂ID：dept_id
     */
    private void getlistdeptuser(final User user, final int i) {
        RequestParams params = new RequestParams(Constant.BASE_URL);
        params.addBodyParameter(Key.ACTION, "getlistdeptuser");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("type", String.valueOf(i));

        App.setType_role_chat(i);

        HttpUtil.getInstance().post(params, ORMBean.FriendInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.FriendInfoBean>() {
            @Override
            public void onSuccess(ORMBean.FriendInfoBean result) {
                if (result.getRes().equals("1")) {

                    App.setFriendInfoList(result.getData());

                    Intent intent = null;
                    switch (i) {
                        case 1:
                            if (user.getRole_count().equals("1")) {
                                intent = new Intent(mContext, FuntionActivity.class);
                            } else
                                intent = new Intent(mContext, RoleActivity.class);
                            break;
                        case 2:
                            intent = new Intent(mContext, ProcurementMainActivity.class);
                            break;
                        case 3:
                            intent = new Intent(mContext, TeamMainActivity.class);
                            break;
                        case 7:
                            intent = new Intent(mContext, BossMainActivity.class);
                            break;
                        case 8:
                            intent = new Intent(mContext, AdministratorMainActivity.class);
                            break;
                        case 4:
                            intent = new Intent(mContext, CarOwnersActivity.class);
                            intent.putExtra("name",user.getUser_name());
                            break;
                    }
                    startActivity(intent);
                    finish();
                } else {
                    mLoad.dismiss(mContext);
                    ToastUtil.showShort(mContext, "获取联系人列表失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }


    /**
     * 根据用户的ID，获取当前用户的所有角色ID及角色名称
     *
     * @param user
     */
    private void getRole(final User user) {
        RequestParams params = new RequestParams(Constant.BASE_URL);
        params.addBodyParameter(Key.ACTION, "getListRolesByUserId");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());

        HttpUtil.getInstance().post(params, ORMBean.RoleBean.class, new HttpUtil.HttpCallBack<ORMBean.RoleBean>() {
            @Override
            public void onSuccess(ORMBean.RoleBean result) {
                if (result.getRes().equals("1")) {
                    Role role = result.getData().get(0);
//                role.getRole_id()
                    switch (role.getRole_no()) {
                        case "95":
                            getlistdeptuser(user, 1);
                            break;
                        case "96":
                            getlistdeptuser(user, 2);
                            break;
                        case "97":
                            getlistdeptuser(user, 3);
                            break;
                        case "90":
                            getlistdeptuser(user, 7);
                            break;
                        case "85":
                            getlistdeptuser(user, 8);
                            break;
                    }
                    finish();
                } else
                    ToastUtil.showShort(mContext, "本账号无权限");
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
