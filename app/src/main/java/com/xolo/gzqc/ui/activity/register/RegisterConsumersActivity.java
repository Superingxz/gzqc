package com.xolo.gzqc.ui.activity.register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.AMUtils;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.SPUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.downtime.DownTimer;
import com.xolo.gzqc.utils.downtime.DownTimerListener;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消费者注册
 */
public class RegisterConsumersActivity extends BaseActivity {

    @BindView(R.id.eet_name)
    ExEditText eetName;
    @BindView(R.id.eet_sex)
    ExEditText eetSex;
    @BindView(R.id.eet_phone)
    ExEditText eetPhone;
    @BindView(R.id.eet_code)
    ExEditText eetCode;
    @BindView(R.id.eet_password)
    ExEditText eetPassword;
    @BindView(R.id.eet_province)
    ExEditText eetProvince;
    @BindView(R.id.eet_city)
    ExEditText eetCity;
    @BindView(R.id.eet_country)
    ExEditText eetCountry;
    @BindView(R.id.eet_postal)
    ExEditText eetPostal;
    @BindView(R.id.btn_registered)
    Button btnRegistered;
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.eet_stree)
    ExEditText eetStree;

    private Button getCode;

    private List<Area> list_province = new ArrayList<>();
    private List<Area> list_city = new ArrayList<>();
    private List<Area> list_county = new ArrayList<>();

    private Dialog dialog_province;
    private Dialog dialog_city;
    private Dialog dialog_county;
    private Dialog dialog_sex;

    private String provinceid;
    private String cityid;
    private String areaid;

    private String select_sex;

    /**
     * 倒计时
     */
    private DownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_consumers);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        eetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        getCode = eetCode.getButton();
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverificationcode();
            }
        });
        initDialog();
        initDownTime();
    }

    @OnClick({R.id.eet_sex, R.id.eet_province, R.id.eet_city, R.id.eet_country, R.id.btn_registered})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eet_sex:
                dialog_sex.show();
                break;
            case R.id.eet_province:
                if (list_province.size() > 0) {
                    dialog_province.show();
                    return;
                }
                getArea(list_province, dialog_province, "");
                break;
            case R.id.eet_city:
                if (TextUtils.isEmpty(eetProvince.getText())) {
                    ToastUtil.showLong(mContext, "请先选择省");
                    return;
                }
                getArea(list_city, dialog_city, provinceid);
                break;
            case R.id.eet_country:
                if (TextUtils.isEmpty(eetCity.getText())) {
                    ToastUtil.showLong(mContext, "请先选择市");
                    return;
                }
                getArea(list_county, dialog_county, cityid);
                break;
            case R.id.btn_registered:
                clientapply();
                break;
        }
    }

    private void initDialog() {
        dialog_province = createAreaDialog("省", list_province, eetProvince);
        dialog_city = createAreaDialog("市", list_city, eetCity);
        dialog_county = createAreaDialog("县区", list_county, eetCountry);

        dialog_sex = new AlertDialog.Builder(mContext)
                .setTitle("选择性别")
                .setSingleChoiceItems(new String[]{"男", "女"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            select_sex = "男";
                        } else select_sex = "女";
                    }
                })
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           eetSex.setText(select_sex);
                    }
                })
                .create();
    }

    private void initDownTime() {
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


    private Dialog createAreaDialog(String title, List<Area> list, final ExEditText targer) {
        return creatListDialog(title, list, targer.getEt_contetn(), new ListDialogCallBack<Area>() {
            @Override
            public String setText(Area area) {
                return area.getDistname();
            }

            @Override
            public void onClick(Area area) {

                if (targer == eetProvince) {
                    eetCity.setText("");
                    eetCountry.setText("");
                    provinceid = area.getDistid();
                    cityid = null;
                } else if (targer == eetCity) {
                    eetCountry.setText("");
                    cityid = area.getDistid();
                } else {
                    areaid = area.getDistid();
                }
            }
        });
    }

    /**
     * 接口获取省，市，县的信息
     *
     * @param list
     * @param dialog
     */
    private void getArea(final List list, final Dialog dialog, String id) {
        RequestParams params = creatParams("getregionbydistid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("distid", id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.AreaBean.class, new HttpUtil.HttpCallBack<ORMBean.AreaBean>() {
            @Override
            public void onSuccess(ORMBean.AreaBean result) {
                List<Area> data = result.getData();

                list.clear();
                list.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 1-4 发送验证码短信接口 sendverificationcode(mobile)
     * 手机号码：mobile
     */
    private void sendverificationcode() {

        final String phone = eetPhone.getText();
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
                SPUtils.put(mContext, "massagePhone", phone);

                downTimer.startDown(60000);
                getCode.setClickable(false);
                getCode.setBackgroundColor(getResources().getColor(R.color.gray1));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 1-10 消费者注册接口 clientapply(name,sex,phone,password,province_id,province,city_id,city,area_id,area,street,zip_code)
     * 姓名:name,性别:sex,联系电话:phone,登录密码:password,省份ID:province_id,省份:province,城市ID:city_id,城市:city,区县ID:area_id,区县:area,乡镇街道:street,邮编:zip_code
     */
    private void clientapply() {
        if (TextUtils.isEmpty(eetName.getText())) {
            ToastUtil.showShort(mContext, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(eetPassword.getText())) {
            ToastUtil.showShort(mContext, "请输入登录密码");
            return;
        }
        if (TextUtils.isEmpty(eetPhone.getText())) {
            ToastUtil.showShort(mContext, "请输入联系电话");
            return;
        }
        if (TextUtils.isEmpty(eetCode.getText())) {
            ToastUtil.showShort(mContext, "请输入验证码");
            return;
        }

        final String massageCode = (String) SPUtils.get(mContext, "massageCode", "");
        if (!eetCode.getText().equals(massageCode)) {
            ToastUtil.showShort(mContext, "输入验证码错误");
            return;
        }

        String massagePhone = (String) SPUtils.get(mContext, "massagePhone", "");
        if (!eetPhone.getText().equals(massagePhone)) {
            ToastUtil.showShort(mContext, "输入验证码错误");
            return;
        }

        RequestParams params = creatParams("clientapply");
        params.addBodyParameter("name",eetName.getText());
        params.addBodyParameter("sex",eetSex.getText().equals("男")?"0":"1");
        params.addBodyParameter("phone",eetPhone.getText());
        params.addBodyParameter("password",eetPassword.getText());
        params.addBodyParameter("province_id",provinceid);
        params.addBodyParameter("province",eetProvince.getText());
        params.addBodyParameter("city_id",cityid);
        params.addBodyParameter("city",eetCity.getText());
        params.addBodyParameter("area_id",areaid);
        params.addBodyParameter("area",eetCountry.getText());
        params.addBodyParameter("street",eetStree.getText());
        params.addBodyParameter("zip_code",eetPostal.getText());

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext,"提交成功");
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
