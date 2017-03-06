package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.PayOrder;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.PayDetailsActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 交车信息-未付结账
 * 0-未付结账，1-综合信息，2-交车
 */
public class InTheCarInfoFragment extends BaseFragment {

    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.tv_pay_1)
    TextView tvPay1;
    @BindView(R.id.tv_pay_2)
    TextView tvPay2;
    @BindView(R.id.tv_pay_3)
    TextView tvPay3;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.ration)
    EditText ration;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.lessmoney)
    EditText lessmoney;
    @BindView(R.id.tv_pay_4)
    EditText tvPay4;
    @BindView(R.id.tax_point)
    EditText taxPoint;
    @BindView(R.id.taxes)
    TextView taxes;
    @BindView(R.id.tv_pay_5)
    EditText tvPay5;
    @BindView(R.id.receive_name)
    EditText receiveName;
    @BindView(R.id.receive_phone)
    EditText receivePhone;
    @BindView(R.id.cb_1)
    CheckBox cb1;
    @BindView(R.id.cb_2)
    CheckBox cb2;
    @BindView(R.id.cb_3)
    CheckBox cb3;
    @BindView(R.id.btn_count)
    Button btnCount;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.holder)
    TextView holder;
    @BindView(R.id.holder_tel)
    TextView holderTel;
    @BindView(R.id.et_remark)
    EditText etRemark;

    private CarInfo carInfo;

    private PayOrder payOrder;

    private int type;

    PayDetailsActivity activity;

    LoadDialog loadDialog;

    public InTheCarInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        activity = (PayDetailsActivity) context;

        type = activity.getIntent().getIntExtra("type", 0);
        carInfo = activity.getCarInfo();

        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_in_the_car_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        rb1.setChecked(true);
//  1  接车员  车辆综合信息进来只读数据  改为打印功能
//   0     未付结账    隐藏未结算选项
//        2   结算交车
        if (type == 1) {
            btnPay.setText("打印");
            btnCount.setVisibility(View.GONE);
            cb2.setVisibility(View.GONE);
            cb3.setVisibility(View.GONE);
            setEditable(new EditText[]{ration, lessmoney, tvPay4, taxPoint, tvPay5, receiveName, receivePhone});
            setClickale(new View[]{cb1, cb2, cb3, rb1, rb2});
        }
        if (type == 0) {
            cb2.setVisibility(View.GONE);
        }
        getreturncarinfobybillid();

        ration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rb1.setChecked(true);
                }
            }
        });

        lessmoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rb2.setChecked(true);
                }
            }
        });

        //车主隐打印按钮
        if (getArguments() != null) {
            if (!TextUtils.isEmpty(getArguments().getString("carwoner"))) {
                cb3.setVisibility(View.GONE);
                cb1.setVisibility(View.GONE);
                cb2.setVisibility(View.GONE);
                btnPay.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void load() {
        super.load();
        getreturncarinfobybillid();
    }

    @OnClick({R.id.btn_count, R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_count:
                count();
                break;
            case R.id.btn_pay:
                if (type == 1) {
                    setprintmark();
                } else
                    unpaysettled();
                break;
        }
    }


    /**
     * 换算方法
     */
    private void count() {
        //                实际金额
        float pay1 = 0;
//                应付金额
        float pay2 = 0;
//                税金
        float pay3 = 0;

        if (payOrder == null) {
            ToastUtil.showShort(mContent, "获取交车详情失败");
        }

        String total_money = payOrder.getTotal_money();
        float i = Float.parseFloat(total_money);

        if (rb1.isChecked()) {
            String trim = ration.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                float i1 = Float.parseFloat(trim);
                pay1 = i * i1 / 100;
            } else {
                pay1 = i;
            }
        } else {
            String trim = lessmoney.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                float i1 = Float.parseFloat(trim);
                pay1 = i - i1;
            } else {
                pay1 = i;
            }
        }


        String trim = taxPoint.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            float i1 = Float.parseFloat(trim);
            pay3 = i * i1 / 100;
        }

        pay2 = pay1 + pay3;

        tvPay4.setText(String.valueOf(pay1));
        tvPay5.setText(String.valueOf(pay2));
        taxes.setText(String.valueOf(pay3));
    }


    /**
     * 2-65 通过结算单ID获取交车信息接口 getreturncarinfobybillid(userid,finished_id,receive_id)
     * 当前用户ID:userid,结算单ID(即：交车ID)：finished_id，接车单ID：receive_id
     */

    /**
     * 2-73 通过接车单ID获取交车信息接口 getreturncarinfobyreceiveid(userid,receive_id)
     * 当前用户ID:userid,接车单ID：receive_id
     */
    private void getreturncarinfobybillid() {
        RequestParams params = null;
        if (type == 1 || type == 0) {
            params = creatParams("getreturncarinfobybillid");
            params.addBodyParameter("finished_id", carInfo.getBf_finished_id());
        } else if (type == 2) {
            params = creatParams("getreturncarinfobyreceiveid");
        }
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receive_id", carInfo.getBf_receive_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.PayOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PayOrderBean>() {
            @Override
            public void onSuccess(ORMBean.PayOrderBean result) {
                payOrder = result.getData().get(0);
                carno.setText(payOrder.getCarno());
                name.setText(payOrder.getName());
                phone.setText(payOrder.getPhone());
                holder.setText(payOrder.getHolder());
                holderTel.setText(payOrder.getHolder_tel());
                tvPay1.setText(payOrder.getSaleprice());
                tvPay2.setText(payOrder.getWorkamt());
                tvPay3.setText(payOrder.getTotal_money());
                tvPay4.setText(payOrder.getTotal_money());
                tvPay5.setText(payOrder.getTotal_money());
                etRemark.setText(payOrder.getRemark());

                activity.setPhone(payOrder.getPhone());

                if (type == 1) {
                    ration.setText(payOrder.getRation());
                    lessmoney.setText(payOrder.getLessmoney());
                    taxes.setText(payOrder.getTaxes());
                    taxPoint.setText(payOrder.getTax_point());

                    if (TextUtils.isEmpty(payOrder.getLessmoney())) {
                        rb1.setChecked(true);
                    } else {
                        rb2.setChecked(true);
                    }

                    count();
                }

                if (type == 2) {
                    receiveName.setText(payOrder.getGet_car_name());
                    receivePhone.setText(payOrder.getGet_car_phone());
                } else {
                    receiveName.setText(payOrder.getReceive_name());
                    receivePhone.setText(payOrder.getReceive_phone());
                }


                activity.setmilleage(payOrder.getMileage());

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-66 未付结账模块结算接口 unpaysettled(userid,finished_id,receive_id,total_money,ration,lessmoney,tax_point,taxes,
     * is_invoicing,is_print,receive_name,receive_phone)
     * 当前用户ID:userid,结算单ID(即：交车ID):finished_id,接车单ID:receive_id,结算总金额:total_money,折扣比例:ration,
     * 优惠金额:lessmoney,税点:tax_point,税金:taxes,是否开发票:is_invoicing,是否打印:is_print,领车人姓名:receive_name,
     * 领车人手机号:receive_phone
     */
    /**
     * 2-74 交车模块结算接口 returncarsettled(userid,receive_id,total_money,ration,lessmoney,tax_point,taxes,is_invoicing,is_unsettled,
     * is_print,receive_name,receive_phone)
     * 当前用户ID:userid,接车单ID：receive_id,结算总金额:total_money,折扣比例:ration,优惠金额:lessmoney,税点:tax_point,税金:taxes,
     * 是否开发票:is_invoicing,未结算:is_unsettled,是否打印:is_print,领车人姓名:receive_name,领车人手机号:receive_phone
     */
    private void unpaysettled() {
        RequestParams params = null;
        if (type == 0) {
            params = creatParams("unpaysettled");
            params.addBodyParameter("finished_id", carInfo.getBf_finished_id());
        } else if (type == 2) {
            params = creatParams("returncarsettled");
            params.addBodyParameter("is_unsettled", cb2.isChecked() ? "1" : "0");
        }
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receive_id", carInfo.getBf_receive_id());
        params.addBodyParameter("total_money", tvPay3.getText().toString().trim());
        params.addBodyParameter("ration", ration.getText().toString().trim());
        params.addBodyParameter("lessmoney", lessmoney.getText().toString().trim());
        params.addBodyParameter("tax_point", taxPoint.getText().toString().trim());
        params.addBodyParameter("taxes", taxes.getText().toString().trim());
        params.addBodyParameter("is_invoicing", cb1.isChecked() ? "1" : "0");
        params.addBodyParameter("is_print", cb3.isChecked() ? "1" : "0");
        params.addBodyParameter("receive_name", receiveName.getText().toString().trim());
        params.addBodyParameter("receive_phone", receivePhone.getText().toString().trim());
        params.addBodyParameter("real_money", getText(tvPay4));
        params.addBodyParameter("must_money", getText(tvPay5));
        params.addBodyParameter("remark",getText(etRemark));

        loadDialog.show(mContent);

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {
                    if (type == 2) {
                        String[] split = result.getMsg().split("，");
                        ToastUtil.showShort(mContent, split[0]);

                        activity.setFinishId(split[1]);
                    } else
                        ToastUtil.showShort(mContent, result.getMsg());

//                    切换到保养提醒页
                    activity.setTabview();
                } else {
                    ToastUtil.showShort(mContent, result.getMsg());
                }

                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });

    }


    /**
     * 2-63 车辆综合信息记录设置打印标记接口 setprintmark(userid,page_id,type)
     * 当前用户ID:userid,页签来源ID：page_id, 类别（1为接车单，2为报价单，3为结算单）:type
     */
    private void setprintmark() {
        RequestParams params = creatParams("setprintmark");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("page_id", carInfo.getBf_finished_id());
        params.addBodyParameter("type", "3");

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContent, result.getMsg());
                mContent.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
