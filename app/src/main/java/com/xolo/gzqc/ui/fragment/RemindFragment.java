package com.xolo.gzqc.ui.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Maintainwarn;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.PayDetailsActivity;
import com.xolo.gzqc.ui.activity.pickcar.ReturnCarActivity;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.DataPopUtil;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 保养提醒-未付结账
 */
public class RemindFragment extends BaseFragment {

    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.data)
    TextView data;
    @BindView(R.id.mileage)
    TextView mileage;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.btn_commit)
    Button btn;
    @BindView(R.id.mileage_last)
    TextView mileageLast;
    @BindView(R.id.eet_che_jian)
    ExEditText eetCheJian;
    @BindView(R.id.eet_bao_xian)
    ExEditText eetBaoXian;

    private CarInfo carInfo;

    private int type;

    private int milleage;

    private String phone;

    private Dialog dialog_day;
    private Dialog dialog_milleage;
    private Dialog dialog_che_jian;
    private Dialog dialog_bao_xian;

    private LoadDialog loadDialog;

    PayDetailsActivity activity;

    public RemindFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        activity = (PayDetailsActivity) context;
        carInfo = activity.getCarInfo();
        type = activity.getIntent().getIntExtra("type", 0);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_remind, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        rb1.setChecked(true);
        /**
         * 车辆综合信息不给于编辑
         */
        if (type == 1) {
            btn.setVisibility(View.GONE);
            day.setClickable(false);
            mileage.setClickable(false);
            remark.setEnabled(false);
            eetCheJian.setClickable(false);
            eetBaoXian.setClickable(false);
        }
        initDialog();
    }

    private void initDialog() {
        String[] array = new String[]{"30", "60", "90", "120", "150", "180", "210", "240", "270", "300", "330", "360"};
        List<String> strings1 = Arrays.asList(array);
        dialog_day = creatListDialog("", strings1, day, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                int i = 0;

                String trim = day.getText().toString().trim();
                if (!TextUtils.isEmpty(trim)) {
                    i = Integer.parseInt(trim);
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i);

                data.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
            }
        });

        String[] array2 = new String[]{"1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000", "9000", "10000"};
        List<String> strings2 = Arrays.asList(array2);
        dialog_milleage = creatListDialog("", strings2, mileage, new ListDialogCallBack<String>() {
            private int milleage_total;

            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                int i = Integer.parseInt(getText(mileage));
                mileageLast.setText(i + milleage + "");
            }
        });
        dialog_bao_xian = creatDateDialog(eetBaoXian.getEt_contetn());
        dialog_che_jian = creatDateDialog(eetCheJian.getEt_contetn());
    }

    @OnClick({R.id.btn_commit, R.id.day, R.id.mileage,R.id.eet_che_jian,R.id.eet_bao_xian})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                savemaintainwarn();
                break;
            case R.id.day:
                if (type != 1) {
                    dialog_day.show();
                }
                break;
            case R.id.mileage:
                if (type != 1) {
                    dialog_milleage.show();
                }
                break;
            case R.id.eet_che_jian:
                dialog_che_jian.show();
                break;
            case R.id.eet_bao_xian:
                dialog_bao_xian.show();
                break;
        }
    }

    @Override
    public void load() {
        super.load();
        carInfo = activity.getCarInfo();
        /**
         * 只有交车不用读数据
         */
        if (type == 1 || type == 0) {
            getmaintainwarnbybillid();
        }
    }

    /**
     * 2-67 保存保养提醒接口 savemaintainwarn(userid,finished_id,receive_id,mileage,mcontent,warn_content,dept_id,dept_name)
     * 当前用户ID:userid,结算单ID(即：交车ID):finished_id,接车单ID:receive_id,行驶里程保养：mileage,保养内容(天数):mcontent,
     * 提醒内容:warn_content,维修厂ID:dept_id,维修厂名称:dept_name
     *       annual_check_date 	是 	string 	车辆年检提醒日期
            insurance_date 	是 	string 	保险购买提醒日期
     */
    private void savemaintainwarn() {
        if (TextUtils.isEmpty(carInfo.getBf_finished_id())) {
            ToastUtil.showShort(mContent, "本车还未结算");
            return;
        }

        if (TextUtils.isEmpty(getText(day))) {
            ToastUtil.showShort(mContent, "保养日期必填");
            return;
        }

        if (TextUtils.isEmpty(getText(mileage))) {
            ToastUtil.showShort(mContent, "行驶里程必填");
            return;
        }

        if (TextUtils.isEmpty(getText(remark))) {
            ToastUtil.showShort(mContent, "提醒事项必填");
            return;
        }

//        String type = null;
//        String mcontent = null;

//        if (rb1.isChecked()) {
//            type = "1";
//            mcontent = day.getText().toString().trim();
//        } else {
//            type = "2";
//            mcontent = mileageLast.getText().toString().trim();
//        }

        RequestParams params = creatParams("savemaintainwarn");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("finished_id", carInfo.getBf_finished_id());
        params.addBodyParameter("receive_id", carInfo.getBf_receive_id());
        params.addBodyParameter("mcontent", getText(day));
        params.addBodyParameter("mileage", getText(mileageLast));
        params.addBodyParameter("warn_content", remark.getText().toString().trim());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("dept_name", user.getDept_name());
        params.addBodyParameter("mileage_add", getText(mileage));
        params.addBodyParameter("annual_check_date", eetCheJian.getText());
        params.addBodyParameter("insurance_date", eetBaoXian.getText());

        loadDialog.show(mContent);

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    rongImSend();
                } else {
                    loadDialog.dismiss(mContent);
                }
                ToastUtil.showShort(mContent, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }


    /**
     * 2-68 通过结算单ID获取保养提醒信息接口 getmaintainwarnbybillid(userid,finished_id)
     * 当前用户ID:userid,结算单ID(即：交车ID):finished_id
     */
    private void getmaintainwarnbybillid() {
        RequestParams params = creatParams("getmaintainwarnbybillid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("finished_id", carInfo.getBf_finished_id());

        loadDialog.show(mContent);

        HttpUtil.getInstance().post(params, ORMBean.MaintainwarnBean.class, new HttpUtil.HttpCallBack<ORMBean.MaintainwarnBean>() {
            @Override
            public void onSuccess(ORMBean.MaintainwarnBean result) {
                if (result.getRes().equals("1")) {
                    Maintainwarn maintainwarn = result.getData().get(0);
                    remark.setText(maintainwarn.getContent2());
                    day.setText(maintainwarn.getContent());
                    mileageLast.setText(maintainwarn.getMileage());
                    mileage.setText(maintainwarn.getMileage_add());
                    eetCheJian.setText(maintainwarn.getAnnual_check_date());
                    eetBaoXian.setText(maintainwarn.getInsurance_date());

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(maintainwarn.getContent()));

                    data.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
                }
                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }

    private void rongImSend() {

        TextMessage myTextMessage = TextMessage.obtain("您好，" + user.getDept_name() + "维修厂温馨提醒您，请在本次维修" + getText(day) + "天后，即" + getText(data) + "或者行驶到达" + getText(mileageLast) + "公里后,来厂进行保养,保养内容：" + getText(remark));

        if (TextUtils.isEmpty(phone)) {
            loadDialog.dismiss(mContent);
            return;
        }


//"7127" 为目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
//Conversation.ConversationType.PRIVATE 为会话类型。
        Message myMessage = Message.obtain(phone, Conversation.ConversationType.PRIVATE, myTextMessage);

/**
 * <p>发送消息。
 * 通过 {@link IRongCallback.ISendMessageCallback}
 * 中的方法回调发送的消息状态及消息体。</p>
 *
 * @param message     将要发送的消息体。
 * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
 *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
 *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
 * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link PushNotificationMessage#getPushData()} 方法获取。
 * @param callback    发送消息的回调，参考 {@link IRongCallback.ISendMessageCallback}。
 */
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
                loadDialog.dismiss(mContext);
                Intent intent = new Intent(mContent, ReturnCarActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回
                loadDialog.dismiss(mContext);
                ToastUtil.showShort(mContext, "提醒车主失败");
            }

        });
    }


    public int getMilleage() {
        return milleage;
    }

    public void setMilleage(int milleage) {
        this.milleage = milleage;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
