package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.Itemt;
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

/**
 * 添加询价配件
 */

public class AddInquiryParts extends BaseActivity {


    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et5)
    EditText et5;
    @BindView(R.id.et7)
    EditText et7;
    @BindView(R.id.et6)
    EditText et6;
    @BindView(R.id.tr_4)
    TableRow tr4;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.tb_itmet)
    TableRow tbItmet;
    @BindView(R.id.et3)
    TextView et3;

    private boolean isEdit;

    private int position;

    private List<Itemt> list_itemt = new ArrayList<>();

    private Dialog dialog;
    private Dialog dialog_control;

    private Control control;

    private Itemt itemt;

    InquiryPart inquiryPart;

    private List<Control> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parts);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra(Key.IS_EDIT, false);
        position = intent.getIntExtra(Key.POSITION, 0);
        Serializable serializable = intent.getSerializableExtra(Key.OBJECT);

        int type = intent.getIntExtra("type", 0);


        if (serializable != null) {
            inquiryPart = (InquiryPart) serializable;
            et1.setText(inquiryPart.getParts_name());
            et5.setText(inquiryPart.getQty());
            et6.setText(inquiryPart.getPrice());
            et7.setText(inquiryPart.getQty_unit());
            et2.setText(inquiryPart.getItemt_name());
            et3.setText(inquiryPart.getSource());

            control = new Control(inquiryPart.getSource_id(), inquiryPart.getSource());
            itemt = new Itemt(inquiryPart.getItemt_name(), inquiryPart.getItemt_id());
        }

        initDialog();
    }


    private void initDialog() {
        dialog = creatListLeftDialog("", list_itemt, et2, new ListDialogCallBack<Itemt>() {
            @Override
            public String setText(Itemt itemt) {
                return itemt.getItemt_name();
            }

            @Override
            public void onClick(Itemt itemt1) {
                itemt = itemt1;
            }
        });
        dialog_control = creatListDialog("", list, et3, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control1) {
                control = control1;
            }
        });

    }


    @OnClick({R.id.confirm, R.id.select, R.id.et3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                final String name = et1.getText().toString().trim();
                final String qty = et5.getText().toString().trim();
                final String price = et6.getText().toString().trim();
                final String qty_util = et7.getText().toString().trim();
                String itemt_name = getText(et2);

                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showShort(mContext, "请先选择配件");
                    return;
                }
                if (TextUtils.isEmpty(qty)) {
                    ToastUtil.showShort(mContext, "请先输入数量");
                    return;
                }
                confirm(name, qty, price, qty_util, itemt_name);
                break;
            case R.id.select:
                getlistrepairitemt();
                break;
            case R.id.et3:
                if (list.size() == 0) {
                    getcontrolbytype("9");
                } else
                    dialog_control.show();
                break;
        }
    }

    /**
     * 2-88 模糊查询获取本维修厂维修项目列表接口,用于报价、询价、拆检模块的维修项目录入。 getlistrepairitemt(userid,itemt_name,dept_id)
     * 当前用户ID:userid,维修项目名称：itemt_name,维修厂ID:dept_id
     */
    private void getlistrepairitemt() {
        RequestParams params = creatParams("getlistrepairitemt");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("itemt_name", TextUtils.isEmpty(getText(et2)) ? "" : getText(et2));
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ItemtBean.class, new HttpUtil.HttpCallBack<ORMBean.ItemtBean>() {
            @Override
            public void onSuccess(ORMBean.ItemtBean result) {
                List<Itemt> data = result.getData();
                list_itemt.clear();
                list_itemt.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    private void confirm(String name, String qty, String price, String qty_util, String itemt_name) {
//        if (control == null) {
//            ToastUtil.showShort(mContext, "请选择来源");
//            return;
//        }

        InquiryPart part = new InquiryPart(name, qty, price, price, qty_util);
        if (control !=null){
            part.setSource(control.getDisplay_data());
            part.setSource_id(control.getControl_id());
        }

        part.setItemt_name(itemt_name);
        if (!(itemt == null)) {
            part.setItemt_id(itemt.getBf_repair_item_id());
        }
        Intent intent = new Intent();
        intent.putExtra(Key.OBJECT, part);

        if (isEdit) {
            part.setBf_query_priced_id(inquiryPart.getBf_query_priced_id());
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
                dialog_control.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


}
