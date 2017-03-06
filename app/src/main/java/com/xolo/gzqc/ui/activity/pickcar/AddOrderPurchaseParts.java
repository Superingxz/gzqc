package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddOrderPurchaseParts extends BaseActivity {


    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.et5)
    EditText et5;
    @BindView(R.id.et7)
    EditText et7;
    @BindView(R.id.et6)
    EditText et6;
    @BindView(R.id.et3)
    TextView et3;
    @BindView(R.id.confirm)
    Button confirm;
    private boolean isEdit;

    private int position;

    private Dialog dialog;

    private Control control;

    private List<Control> list = new ArrayList<>();

    private String carno;

    private List<InquiryPart> list_part = new ArrayList<>();
    private Dialog dialog_part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parts2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra(Key.IS_EDIT, false);
        position = intent.getIntExtra(Key.POSITION, 0);
        Serializable serializable = intent.getSerializableExtra(Key.OBJECT);

        carno = intent.getStringExtra(Key.CARNO);

        if (serializable != null) {
            InquiryPart inquiryPart = (InquiryPart) serializable;
            et1.setText(inquiryPart.getParts_name());
            et5.setText(inquiryPart.getQty());
            et6.setText(inquiryPart.getPrice());
            et7.setText(inquiryPart.getQty_unit());
            et3.setText(inquiryPart.getSource());

            control = new Control(inquiryPart.getSource_id(), inquiryPart.getSource());
        }

        initDialog();
    }

    private void initDialog() {
        dialog = creatListDialog("", list, et3, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control1) {
                control = control1;
            }
        });

        dialog_part = creatListDialog("", list_part, et1, new ListDialogCallBack<InquiryPart>() {
            @Override
            public String setText(InquiryPart part) {
                return part.getParts_name();
            }

            @Override
            public void onClick(InquiryPart part) {
                et5.setText(part.getQty());
                et7.setText(part.getQty_unit());
                et6.setText(part.getPrice());
            }
        });
    }


    /**
     * 2-87 获取本维修厂该车已询价的询价单上配件清单接口,用于报价模块。 getquerypriceparts(userid,carno,dept_id)
     * 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
    private void getquerypriceparts() {
        RequestParams params = creatParams("getquerypriceparts");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("carno", carno);
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.InquiryPartBean.class, new HttpUtil.HttpCallBack<ORMBean.InquiryPartBean>() {
            @Override
            public void onSuccess(ORMBean.InquiryPartBean result) {
                List<InquiryPart> data = result.getData();
                list_part.addAll(data);

                dialog_part.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @OnClick({R.id.confirm, R.id.et3, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                final String name = et1.getText().toString().trim();
                final String qty = et5.getText().toString().trim();
                final String price = et6.getText().toString().trim();
                final String qty_util = et7.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showShort(mContext, "请先选择配件");
                    return;
                }
                if (TextUtils.isEmpty(qty)) {
                    ToastUtil.showShort(mContext, "请先输入数量");
                    return;
                }
                confirm(name, qty, price, qty_util);
                break;

            case R.id.et3:
                if (list.size() == 0) {
                    getcontrolbytype("9");
                } else
                    dialog.show();
                break;
            case R.id.select:
                if (list_part.size() > 0) {
                    dialog_part.show();
                } else
                    getquerypriceparts();
                break;
        }
    }


    private void confirm(String name, String qty, String price, String qty_util) {
//        if (control == null) {
//            ToastUtil.showShort(mContext, "请选择来源");
//            return;
//        }

        InquiryPart part = new InquiryPart(name, qty, price, price, qty_util);
        if (control!=null){
            part.setSource(control.getDisplay_data());
            part.setSource_id(control.getControl_id());
        }
        Intent intent = new Intent();
        intent.putExtra(Key.OBJECT, part);

        if (isEdit) {
            intent.putExtra(Key.POSITION, position);
            setResult(9, intent);
        } else
            setResult(RESULT_OK, intent);

        finish();
    }


    /**
     * 根据类别获取对应的数据字典接口。1为燃料，2为操控，3为驱动，4为排量 ,5为油量
     *
     * @param type
     */
    private void getcontrolbytype(final String type) {
        RequestParams params = creatParams("getcontrolbytype");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("type", type);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
            @Override
            public void onSuccess(ORMBean.ControlBean result) {
                List<Control> data = result.getData();
                list.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
