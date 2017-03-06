package com.xolo.gzqc.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.OwnerInfo;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.AddPurchaseReservationParts;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.Interface.OnKeyDownListener;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新增预约 - 构件预约
 */
public class AddAppointmentFragment extends BaseFragment implements OnKeyDownListener {

    @BindView(R.id.carno)
    EditText carno;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.fault_des)
    EditText faultDes;
    @BindView(R.id.btn_add)
    ImageButton btnAdd;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    private LoadDialog dialog_loding;

    private Dialog dialog_carno;
    private Dialog dialog_data;

    private CarInfo carInfo;
    private OwnerInfo ownerInfo;

    private List<CarInfo> list_carInfo = new ArrayList<>();
    private List<Part> list = new ArrayList<>();

    private CommenAdapter<Part> adapter;
    private LicenseKeyboardUtil keyboardUtil;

    public AddAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_appointment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        initLv();
        initDialog();
        initIntent();

        keyboardUtil = new LicenseKeyboardUtil(mContent, carno,keyboardView);

        carno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clear();
                if (start == 6 && before == 0) {
                    getcarinfobycarno(carno.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initIntent() {
        String no = getActivity().getIntent().getStringExtra(Key.CARNO);
        carno.setText(no);
        if (!TextUtils.isEmpty(no)) {
            getcarinfobycarno(no);
        }
    }

    private void initDialog() {
        dialog_data = creatDateDialog(date);
        dialog_carno = creatListDialog("车牌号", list_carInfo, carno, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getCarno();
            }

            @Override
            public void onClick(CarInfo carInfo) {
                getcarinfobycarno(carInfo.getCarno());
            }
        });
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_part, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Part part = list.get(position);

                holder.setText(R.id.item_1, part.getParts_name());
                holder.setText(R.id.item_2, part.getQty() + part.getQty_unit());
                holder.setText(R.id.item_3, part.getSaleprice());
                holder.setText(R.id.item_4, part.getTotalPrice() + "");
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContent, AddPurchaseReservationParts.class);
                intent.putExtra(Key.IS_EDIT, true);
                intent.putExtra(Key.OBJECT, list.get(position));
                intent.putExtra(Key.POSITION, position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Part car = list.get(position);
                new AlertDialog.Builder(mContext).setMessage("是否删除").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                return true;
            }
        });

    }

    /**
     * 2-7 预约维修接口（含购件、非购件） ordermrepair(userid,car_info_id,plan_com_time,fault_des,is_buy_fitting,userid,
     * json_patrs,dept_id,dept_name,is_pay,car_owner_id)
     * 当前用户ID：userid,车辆信息ID：car_info_id,
     * 计划来车时间：plan_com_time,故障描述：fault_des,是否购件：is_buy_fitting,
     * 配件清单：json_patrs,维修厂ID：dept_id,维修厂名称：dept_name,是否付款:is_pay,车主ID:car_owner_id
     * <p>
     * {"data":[{"parts_name":"过滤器","qty":"1","costprice":"1300","saleprice":"2300"},
     * {"parts_name":"喷油嘴","qty":"1","costprice":"300","saleprice":"500"}]}
     */
    private void ordermrepair(String isBuy) {
        if (TextUtils.isEmpty(getText(carno))) {
            ToastUtil.showShort(mContent, "请选择车辆");
            return;
        }
        if (TextUtils.isEmpty(getText(date))) {
            ToastUtil.showShort(mContent, "请选择到厂时间");
            return;
        }
        if (list.size() == 0) {
            ToastUtil.showShort(mContent, "请添加配件");
            return;
        }

        RequestParams params = creatParams("ordermrepair");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_INFO_ID, carInfo.getBc_car_info_id());
        params.addBodyParameter(Key.PLAN_COM_TIME, date.getText().toString().trim());
        params.addBodyParameter(Key.FAULT_DES, faultDes.getText().toString().trim());
        params.addBodyParameter(Key.IS_BUY_FITTING, "1");
        params.addBodyParameter(Key.JSON_PATRS, "{\"data\":" + list.toString() + "}");
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.DEPT_NAME, user.getDept_name());
        params.addBodyParameter(Key.IS_PAY, isBuy);
        params.addBodyParameter(Key.CAR_OWNER_ID, ownerInfo.getCar_owner_id());

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


    /**
     * 通过车牌号获取该车的车辆信息接口 getcarownerbycarno(userid,carno) 当前用户ID:userid,车牌号:carno
     */
    private void getcarinfobycarno(final String no) {
        RequestParams params = creatParams("getcarinfobycarno");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, no);

        dialog_loding.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                if (result.getRes().equals("1")) {
                    CarInfo carInfo1 = result.getData().get(0);
                    carInfo = carInfo1;
                    getcarownerbycarno(no);
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    dialog_loding.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    /**
     * 通过车牌号获取该车的车主信息接口 getcarownerbycarno(userid,carno) 当前用户ID:userid,车牌号:carno
     */
    private void getcarownerbycarno(final String no) {
        RequestParams params = creatParams("getcarownerbycarno");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, no);

        HttpUtil.getInstance().post(params, ORMBean.OwnerInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.OwnerInfoBean>() {
            @Override
            public void onSuccess(ORMBean.OwnerInfoBean result) {

                if (result.getRes().equals("1")) {
                    OwnerInfo ownerInfo1 = result.getData().get(0);
                    ownerInfo = ownerInfo1;

                    name.setText(ownerInfo1.getCar_owner_name());
                    phone.setText(ownerInfo1.getPhone());

                    dialog_loding.dismiss(mContent);
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    dialog_loding.dismiss(mContext);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });

    }

    @OnClick({R.id.date, R.id.btn_add, R.id.btn_pay, R.id.btn_payed, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date:
                dialog_data.show();
                break;
            case R.id.btn_add:
                startActivityForResult(new Intent(mContent, AddPurchaseReservationParts.class), REQUEST_CODE);
                break;
            case R.id.btn_pay:
                ordermrepair("0");
                break;
            case R.id.btn_payed:
                ordermrepair("1");
                break;
            case R.id.select:
                if (list_carInfo.size() > 0) {
                    dialog_carno.show();
                    return;
                }
                listcarinfo();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Part part = (Part) data.getSerializableExtra(Key.OBJECT);
                list.add(part);
                adapter.notifyDataSetChanged();

                count();
            } else if (resultCode == 9) {
                Part part = (Part) data.getSerializableExtra(Key.OBJECT);
                int position = data.getIntExtra(Key.POSITION, 0);
                Part part1 = list.get(position);
                part1.setParts_name(part.getParts_name());
                part1.setTotalPrice(part.getTotalPrice());
                part1.setSaleprice(part.getSaleprice());
                part1.setQty(part.getQty());
                part1.setCostprice(part.getCostprice());
                part1.setQty_unit(part.getQty_unit());

                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 计算总金额
     */
    private void count() {
        int total = 0;
        for (Part part : list) {
            total += part.getTotalPrice();
        }
        totalAmount.setText(String.valueOf(total));
    }

    /**
     * 2-9 本维修厂车辆列表接口,含查询 listcarinfo(userid,car_no,name,phone,dept_id,is_query) 当前用户ID：userid,车牌号：car_no,
     * 车主姓名：name,车主手机号：phone,维修厂id：dept_id,是否查询.0 为不是查询，1为查询：is_query
     */
    private void listcarinfo() {
        RequestParams params = creatParams("listcarinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, "");
        params.addBodyParameter(Key.NAME, "");
        params.addBodyParameter(Key.PHONE, "");
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("is_query", "0");

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carInfo.addAll(data);

                dialog_carno.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardUtil.hideKeyboard();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
                return true;
            }else
                return false;
        }
        return false;
    }

}
