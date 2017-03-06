package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.xolo.gzqc.bean.postJson.Part;
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

public class AddofferParts extends BaseActivity {

    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.requery)
    Button requery;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.et3)
    EditText et3;
    @BindView(R.id.btn_count)
    Button btnCount;
    @BindView(R.id.et4)
    EditText et4;
    @BindView(R.id.et5)
    EditText et5;
    @BindView(R.id.et7)
    EditText et7;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.et8)
    TextView et8;

    private boolean isEdit;

    private int position;

    private String carno;

    private List<InquiryPart> list = new ArrayList<>();
    private List<Control> list_control = new ArrayList<>();

    private Dialog dialog;
    private Dialog dialog_control;

    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer_parts);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra(Key.IS_EDIT, false);
        position = intent.getIntExtra(Key.POSITION, 0);
        Serializable serializable = intent.getSerializableExtra(Key.OBJECT);

        if (serializable != null) {
            Part part = (Part) serializable;
            et1.setText(part.getParts_name());
            et2.setText(part.getCostprice());
            et4.setText(part.getSaleprice());
            et5.setText(part.getQty());
            et7.setText(part.getQty_unit());
            et8.setText(part.getSource());

            if (part.getSource()!=null){
                control = new Control(part.getSource_id(), part.getSource());
            }
        }

//        2-87 获取本维修厂该车已询价的询价单上配件清单接口   用到
            carno = getIntent().getStringExtra(Key.CARNO);

            dialog = creatListLeftDialog("", list, et1, new ListDialogCallBack<InquiryPart>() {
                @Override
                public String setText(InquiryPart part) {
                    return part.getParts_name();
                }

                @Override
                public void onClick(InquiryPart part) {
                    et2.setText(part.getPrice());
                    et5.setText(part.getQty());
                    et7.setText(part.getQty_unit());
                }
            });

            dialog_control = creatListDialog("", list_control, et8, new ListDialogCallBack<Control>() {
                @Override
                public String setText(Control control) {
                    return control.getDisplay_data();
                }

                @Override
                public void onClick(Control control1) {
                    control = control1;
                }
            });

        et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (TextUtils.isEmpty(getText(et2))) {
//                        ToastUtil.showShort(mContext, "请填写成本单价");
                        return;
                    }
                    if (TextUtils.isEmpty(getText(et3))) {
//                        ToastUtil.showShort(mContext, "请填写百分比");
                        return;
                    }

                    String t = getText(et3);
                    int i = Integer.parseInt(t);

                    String text = getText(et2);
                    double v1 = Double.parseDouble(text);

                    double v2 = v1 * i / 100;

                    et4.setText(v2 + "");
                }
            }
        });

    }


    @OnClick({R.id.btn_count, R.id.confirm, R.id.requery,R.id.et8})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_count:
                if (TextUtils.isEmpty(getText(et2))) {
                    ToastUtil.showShort(mContext, "请填写成本单价");
                    break;
                }
                if (TextUtils.isEmpty(getText(et3))) {
                    ToastUtil.showShort(mContext, "请填写百分比");
                    break;
                }

                String t = getText(et3);
                int i = Integer.parseInt(t);

                String text = getText(et2);
                double v = Double.parseDouble(text);

                double v1 = v * i / 100;

                et4.setText(v1 + "");

                break;
            case R.id.confirm:
                final String name = et1.getText().toString().trim();
                final String costPrice = et2.getText().toString().trim();
                final String salePrice = et4.getText().toString().trim();
                final String qty = et5.getText().toString().trim();
                final String qty_util = et7.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showShort(mContext, "请先选择配件");
                    return;
                }
                if (TextUtils.isEmpty(qty)) {
                    ToastUtil.showShort(mContext, "请先输入数量");
                    return;
                }
//                if (TextUtils.isEmpty(costPrice)) {
//                    ToastUtil.showShort(mContext, "请先输入成本单价");
//                    return;
//                }
                if (TextUtils.isEmpty(salePrice)) {
                    ToastUtil.showShort(mContext, "请先输入销售单价");
                    return;
                }

                if (!TextUtils.isEmpty(salePrice) && !TextUtils.isEmpty(costPrice)) {
                    if (Float.parseFloat(costPrice) > Float.parseFloat(salePrice)) {
                        new AlertDialog.Builder(mContext).setMessage("销售单价小于成本单价？").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirm(name, qty, costPrice, salePrice, qty_util);
                            }
                        }).setPositiveButton("取消", null).show();
                    } else
                        confirm(name, qty, costPrice, salePrice, qty_util);
                } else
                    confirm(name, qty, costPrice, salePrice, qty_util);

                break;
            case R.id.requery:
                if (list.size() > 0) {
                    dialog.show();
                } else
                    getquerypriceparts();
                break;
            case R.id.et8:
                if (list_control.size() == 0) {
                    getcontrolbytype("9");
                } else
                    dialog_control.show();
                break;

        }
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
                list_control.addAll(data);
                dialog_control.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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
                list.addAll(data);

                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-86 获取本维修厂该车已确认的报价单上配件清单接口,用于购件模块。 getquotedpriceparts(userid,carno,dept_id)
     * 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
//private void  getquerypriceparts(){
//    RequestParams params = creatParams("getquotedpriceparts");
//    params.addBodyParameter(Key.USER_ID,user.getUser_id());
//    params.addBodyParameter("carno",carno);
//    params.addBodyParameter(Key.DEPT_ID,user.getDept_id());
//
//    HttpUtil.getInstance().postLoading(mContext, params, ORMBean.PartBean.class, new HttpUtil.HttpCallBack<ORMBean.PartBean>() {
//        @Override
//        public void onSuccess(ORMBean.PartBean result) {
//            List<Part> data = result.getData();
//            list.addAll(data);
//
//            dialog.show();
//        }
//
//        @Override
//        public void onError(Throwable ex, boolean isOnCallback) {
//
//        }
//    });
//}
    private void confirm(String name, String qty, String costPrice, String salePrice, String qty_util) {
        Part part = new Part(name, qty, costPrice, salePrice, Float.parseFloat(salePrice) * Integer.parseInt(qty), qty_util);

        if (!TextUtils.isEmpty(getText(et8))){
            part.setSource(getText(et8));
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
}
