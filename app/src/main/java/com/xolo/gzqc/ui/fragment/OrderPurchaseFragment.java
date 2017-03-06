package com.xolo.gzqc.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.PurchaseOrder;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CarBrandActivity;
import com.xolo.gzqc.ui.activity.pickcar.AddOrderPurchaseParts;
import com.xolo.gzqc.ui.dialog.SelectYearDialog;
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
 * A simple {@link Fragment} subclass.
 */
public class OrderPurchaseFragment extends BaseFragment implements OnKeyDownListener {
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.brand)
    EditText brand;
    @BindView(R.id.models)
    EditText models;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.tb_name)
    TableRow tbName;
    @BindView(R.id.replay_name)
    TextView replayName;
    @BindView(R.id.tb_replay_name)
    TableRow tbReplayName;
    @BindView(R.id.replay_time)
    TextView replayTime;
    @BindView(R.id.plandate)
    TextView plandate;
    @BindView(R.id.urgent_parts)
    CheckBox urgentParts;
    @BindView(R.id.btn_more)
    TextView btnMore;
    @BindView(R.id.displacement)
    TextView displacement;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.drive)
    TextView drive;
    @BindView(R.id.fuel)
    TextView fuel;
    @BindView(R.id.operation)
    TextView operation;
    @BindView(R.id.mileage)
    EditText mileage;
    @BindView(R.id.vin_code)
    EditText vinCode;
    @BindView(R.id.engine)
    EditText engine;
    @BindView(R.id.login_data)
    TextView loginData;
    @BindView(R.id.tl_hidden)
    TableLayout tlHidden;
    @BindView(R.id.btn_add)
    ImageButton btnAdd;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;
    @BindView(R.id.tb_replay_time)
    TableRow tbReplayTime;
    @BindView(R.id.tb_plan_time)
    TableRow tbPlanTime;
    private boolean is_edit;

    private List<Control> list_displacement = new ArrayList<>();
    private List<Control> list_drive = new ArrayList<>();
    private List<Control> list_fuel = new ArrayList<>();
    private List<Control> list_operation = new ArrayList<>();
    private List<CarInfo> list_carInfo = new ArrayList<>();
    private List<InquiryPart> list_part = new ArrayList<>();

    private Control control_displacement;
    private Control control_drive;
    private Control control_fuel;
    private Control control_operation;

    private Dialog dialog_carno;
    private Dialog dialog_displacement;
    private Dialog dialog_drive;
    private Dialog dialog_fuel;
    private Dialog dialog_operation;
    private Dialog dialog_date;
    private Dialog dialog_date_2;
    private Dialog dialog_year;
    private LoadDialog dialog_loding;

    private PurchaseOrder carInfo;
    private CommenAdapter<InquiryPart> adapter;

    private LicenseKeyboardUtil keyboardUtil;

    private boolean isHidden = true;

    private int type;

    private Brand brand_rt;
    private TypeCode typeCode_rt;

    public OrderPurchaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_purchase, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        brand.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                brand.setFocusable(true);
                brand.setFocusableInTouchMode(true);
                brand.setClickable(false);
                return true;
            }
        });
        models.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                models.setFocusable(true);
                models.setFocusableInTouchMode(true);
                return true;
            }
        });

        type = mContent.getIntent().getIntExtra("type", 0);
        initLv();
        initDialog();
        initIntent();

        keyboardUtil = new LicenseKeyboardUtil(mContent, carNunber, keyboardView);
    }


    @OnClick({R.id.displacement, R.id.drive, R.id.fuel, R.id.operation, R.id.btn_add, R.id.confirm, R.id.select, R.id.login_data, R.id.plandate, R.id.btn_more, R.id.brand, R.id.year})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.displacement:
                if (list_displacement.size() > 0) {
                    dialog_displacement.show();
                    return;
                }
                getcontrolbytype("4");
                break;
            case R.id.drive:
                if (list_drive.size() > 0) {
                    dialog_drive.show();
                    return;
                }
                getcontrolbytype("3");
                break;
            case R.id.fuel:
                if (list_fuel.size() > 0) {
                    dialog_fuel.show();
                    return;
                }
                getcontrolbytype("1");
                break;
            case R.id.operation:
                if (list_operation.size() > 0) {
                    dialog_operation.show();
                    return;
                }
                getcontrolbytype("2");
                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(getText(carNunber))) {
                    ToastUtil.showShort(mContext, "请选择车辆");
                    return;
                }

                Intent intent = new Intent(mContext, AddOrderPurchaseParts.class);
                intent.putExtra(Key.CARNO, getText(carNunber));
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.confirm:
                if (list_part.size() == 0) {
                    ToastUtil.showShort(mContent, "购件清单不能为空");
                    return;
                }
                if (TextUtils.isEmpty(carNunber.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请输入车牌号");
                    return;
                }
                if (TextUtils.isEmpty(brand.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请输入品牌");
                    return;
                }

//                if (is_edit) {
//                    editqueryppricem();
//                    return;
//                }
                addorderbuym();
                break;
            case R.id.select:
                carnofuzzyquery();
//                listcarinfo();
                break;
            case R.id.login_data:
                dialog_date.show();
                break;
            case R.id.plandate:
                dialog_date_2.show();
                break;
            case R.id.btn_more:
                if (isHidden) {
                    btnMore.setText("隐藏更多");
                    tlHidden.setVisibility(View.VISIBLE);
                    isHidden = false;
                } else {
                    btnMore.setText("显示更多");
                    tlHidden.setVisibility(View.GONE);
                    isHidden = true;
                }
                break;
            case R.id.brand:
                if (!brand.isFocusableInTouchMode()) {
                    startActivityForResult(new Intent(mContext, CarBrandActivity.class), CarBrandActivity.GET_BRAND);
                }
                break;
            case R.id.year:
                dialog_year.show();
                break;
        }
    }


    private void initIntent() {
        String no = mContent.getIntent().getStringExtra(Key.CARNO);
        if (!TextUtils.isEmpty(no)) {
            getcarinfobycarno(no);
        }

//        tyep:1.历史构件
        if (type == 1) {
            select.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            tbPlanTime.setVisibility(View.VISIBLE);
            setVisibility(new View[]{select, btnAdd, confirm}, View.GONE);
            setVisibility(new View[]{tbName, tbReplayName, tbReplayTime}, View.VISIBLE);
            setEditable(new EditText[]{carNunber, brand, models, vinCode, engine, mileage});
            setCompoundDrawables(new TextView[]{displacement, fuel, drive, loginData, operation, brand, year});
            setClickale(new TextView[]{displacement, fuel, drive, loginData, operation, brand, year});
            setHint(new TextView[]{displacement, fuel, drive, loginData, operation, carNunber, brand, models, year, vinCode, engine, mileage, replayTime});

            String id = mContent.getIntent().getStringExtra(IntentConstant.BUY_ID);
            getbuyminfobybuymid(id);
        }

    }

    private void initDialog() {
        dialog_displacement = createControlDialog("排量", list_displacement, displacement);
        dialog_drive = createControlDialog("驱动", list_drive, drive);
        dialog_fuel = createControlDialog("燃料", list_fuel, fuel);
        dialog_operation = createControlDialog("操作", list_operation, operation);

        dialog_carno = creatListDialog("车牌号", list_carInfo, carNunber, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getCarno();
            }

            @Override
            public void onClick(CarInfo carInfo) {
                clearDate();
                getcarinfobycarno(carInfo.getCarno());
            }
        });

        dialog_date = creatDateDialog(loginData);
        dialog_date_2 = creatDateDialog(plandate);
        dialog_year = SelectYearDialog.creat(mContext, year);
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_part_orderpurchase, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                InquiryPart part = list_part.get(position);

                holder.setText(R.id.item1,part.getParts_name());
                holder.setText(R.id.item2,part.getCostprice());
                holder.setText(R.id.item3,"x "+part.getQty()+" ( "+part.getQty_unit()+" )");
                holder.setText(R.id.item4,part.getSource());

                double total = Double.parseDouble(part.getCostprice()) * Integer.parseInt(part.getQty());
                holder.setText(R.id.item5,"￥"+String.valueOf(total));
            }
        });
        lv.setAdapter(adapter);

        if (type == 0) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, AddOrderPurchaseParts.class);
                    intent.putExtra(Key.IS_EDIT, true);
                    intent.putExtra(Key.OBJECT, list_part.get(position));
                    intent.putExtra(Key.POSITION, position);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    final InquiryPart car = list_part.get(position);
                    new AlertDialog.Builder(mContext).setMessage("是否删除").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list_part.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }).show();
                    return true;
                }
            });
        }


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

        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
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


    private Dialog createControlDialog(String title, List<Control> list, final TextView targer) {
        return creatListDialog(title, list, targer, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control) {
                if (targer == displacement) {
                    control_displacement = control;
                } else if (targer == drive) {
                    control_drive = control;
                } else if (targer == fuel) {
                    control_fuel = control;
                } else {
                    control_operation = control;
                }
            }
        });
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

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
            @Override
            public void onSuccess(ORMBean.ControlBean result) {
                List<Control> data = result.getData();
                if (type.equals("1")) {
                    list_fuel.addAll(data);
                    dialog_fuel.show();
                } else if (type.equals("2")) {
                    list_operation.addAll(data);
                    dialog_operation.show();
                } else if (type.equals("3")) {
                    list_drive.addAll(data);
                    dialog_drive.show();
                } else if (type.equals("4")) {
                    list_displacement.addAll(data);
                    dialog_displacement.show();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                InquiryPart part = (InquiryPart) data.getSerializableExtra(Key.OBJECT);
                list_part.add(part);
                adapter.notifyDataSetChanged();

            } else if (resultCode == 9) {
                InquiryPart part = (InquiryPart) data.getSerializableExtra(Key.OBJECT);
                int position = data.getIntExtra(Key.POSITION, 0);
                list_part.remove(position);
                list_part.add(position, part);

                adapter.notifyDataSetChanged();
            }
        }

        //车辆品牌型号
        if (requestCode == CarBrandActivity.GET_BRAND && resultCode == CarBrandActivity.RETURN_BRAND) {
            brand_rt = (Brand) data.getSerializableExtra(IntentConstant.BRAND);
            typeCode_rt = (TypeCode) data.getSerializableExtra(IntentConstant.TYPE_CODE);
            brand.setText(brand_rt.getBrands());
            models.setText(typeCode_rt.getTypecode());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 2-42 购件开单下单采购接口 addorderbuym(userid,dept_id,dept_name,receiver_id,name,mobile,plandate,carno,brands,typecode,output_id,
     * productyear,drive_id,drive_type,operation_id,operation_type,fuel_id,fuel,vincode,reg_date,enginecode,mileage,urgent_parts,json_parts)
     * 当前用户ID:userid,维修厂ID:dept_id,所属维修厂名称:dept_name,接车单ID,有就传，没有就留空:receiver_id,购件人姓名:name,购件人手机号:mobile,
     * 要求到货时间:plandate,车牌号:carno,车品牌名:brands,车型代码:typecode,排量ID:output_id,排量:output,车辆生产年份:productyear,驱动类型ID:drive_id,
     * 驱动类型:drive_type,操控方式ID:operation_id,操控方式:operation_type,燃料ID:fuel_id,燃料:fuel,VIN码:vincode,注册日期:reg_date,发动机号:enginecode,
     * 行驶里程:mileage,紧急件:urgent_parts,配件清单列表:json_parts
     */
    private void addorderbuym() {
        RequestParams params = creatParams("addorderbuym");
        params.addBodyParameter(Key.RECRIVE_ID, "");
        params.addBodyParameter(Key.PLANDATE, plandate.getText().toString().trim());
        params.addBodyParameter(Key.MILEAGE, mileage.getText().toString().trim());
        params.addBodyParameter(Key.URGENT_PARTS, urgentParts.isChecked() ? "1" : "0");
        params.addBodyParameter("brands_id", brand_rt == null ? "" : brand_rt.getBrands_id());
        params.addBodyParameter("typecode_id", typeCode_rt == null ? "" : typeCode_rt.getTypecode_id());
        addParems(params);

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                mContent.finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-45 根据购件单ID获取配件清单接口 getlistrepairparts(userid,buym_id)
     * 当前用户ID:userid,购件单ID:buym_id
     */
    private void getlistrepairparts() {
        RequestParams params = creatParams("getlistrepairparts");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.BUYM_ID, carInfo.getBf_buym_id());

        HttpUtil.getInstance().post(params, ORMBean.InquiryPartBean.class, new HttpUtil.HttpCallBack<ORMBean.InquiryPartBean>() {
            @Override
            public void onSuccess(ORMBean.InquiryPartBean result) {
                if (result.getRes().equals("1")) {
                    List<InquiryPart> data = result.getData();
                    list_part.clear();
                    list_part.addAll(data);
                    adapter.notifyDataSetChanged();

//                    is_edit = true;
//                    confirm.setText("修改");
                } else {
//                    is_edit = false;
//                    confirm.setText("配件询价");
//                    ToastUtil.showShort(mContext, result.getMsg());
                }
                dialog_loding.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    /**
     * 2-41 根据车牌号获取本维修厂该车最新的购件单接口 getnewbuym(userid,dept_id,carno)
     * 当前用户ID:userid,维修厂ID:dept_id,车牌号:carno
     * 2-83 通过购件单ID获取购件单信息接口 getbuyminfobybuymid(userid,buym_id)
     * 当前用户ID:userid,购件单ID：buym_id
     */
    private void getnewbuym(String carno) {
        RequestParams params = null;

        params = creatParams("getnewbuym");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, carno);
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());


        HttpUtil.getInstance().post(params, ORMBean.PurchaseOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PurchaseOrderBean>() {
            @Override
            public void onSuccess(ORMBean.PurchaseOrderBean result) {
                if (result.getRes().equals("1")) {
                    PurchaseOrder carInfo1 = result.getData().get(0);
                    carInfo = carInfo1;

                    plandate.setText(carInfo.getBuy_time());
                    mileage.setText(carInfo.getMileage());

                    String urgent = carInfo.getUrgent_parts();
                    if (urgent.equals("1")) {
                        urgentParts.setChecked(true);
                    }

//                    getquotedpriceparts();
                    getlistrepairparts();
                } else {
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
     * 2-33 修改询价单接口，仅能修改采购员未回复的询价单 editqueryppricem(userid,query_pricem_id,carno,brands,typecode,output_id,output,
     * productyear,drive_id,drive_type,operation_id,operation_type,fuel_id,fuel,vincode,reg_date,enginecode,name,mobile,json_parts)
     * 当前用户ID:userid,询价单ID:query_pricem_id,车牌号:carno,车品牌名:brands,车型代码:typecode,排量ID:output_id,排量:output,
     * 车辆生产年份:productyear,驱动类型ID:drive_id,驱动类型:drive_type,操控方式ID:operation_id,操控方式:operation_type,燃料ID:fuel_id,
     * 燃料:fuel,VIN码:vincode,注册日期:reg_date,发动机号:enginecode,询价人姓名:name,询价人手机号:mobile,询价配件清单列表:json_parts,
     * 所属维修厂ID:dept_id,所属维修厂名称:dept_name
     */
    private void editqueryppricem() {
        RequestParams params = creatParams("editqueryppricem");
        addParems(params);
//        params.addBodyParameter(Key.QUERY_PRICEM_ID,carInfo.getBf_query_pricem_id());

        HttpUtil.getInstance().postLoading(mContent, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-46 删除购件配件清单接口 delrepairparts(userid,buyd_id)
     * 当前用户ID:userid,配件请单ID:buyd_id
     */
    private void delrepairparts() {

    }


    private void addParems(RequestParams params) {
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, carNunber.getText().toString().trim());
        params.addBodyParameter(Key.BRANDS, brand.getText().toString().trim());
        params.addBodyParameter(Key.TYPECODE, models.getText().toString().trim());
        params.addBodyParameter(Key.OUTPUT_ID, control_displacement.getControl_id());
        params.addBodyParameter(Key.OUTPUT, control_displacement.getDisplay_data());
        params.addBodyParameter(Key.PRODUCTYEAR, year.getText().toString().trim());
        params.addBodyParameter(Key.DRIVE_ID, control_drive.getControl_id());
        params.addBodyParameter(Key.DRIVE_TYPE, control_drive.getDisplay_data());
        params.addBodyParameter(Key.OPERATION_ID, control_operation.getControl_id());
        params.addBodyParameter(Key.OPERATION_TYPE, control_operation.getDisplay_data());
        params.addBodyParameter(Key.FUEL_ID, control_fuel.getControl_id());
        params.addBodyParameter(Key.FUEL, control_fuel.getDisplay_data());
        params.addBodyParameter(Key.VINCODE, vinCode.getText().toString().trim());
        params.addBodyParameter(Key.REG_DATE, loginData.getText().toString());
        params.addBodyParameter(Key.ENGINECODE, engine.getText().toString().trim());
        params.addBodyParameter(Key.NAME, user.getUser_name());
        params.addBodyParameter(Key.MOBILE, user.getPhone_code());
        params.addBodyParameter("json_parts", "{\"data\":" + list_part.toString() + "}");
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.DEPT_NAME, user.getDept_name());
    }


    private void clearDate() {
        carNunber.setText("");
        brand.setText("");
        models.setText("");
        year.setText("");
        vinCode.setText("");

        displacement.setText("");
        drive.setText("");
        fuel.setText("");
        operation.setText("");
        loginData.setText("");
        engine.setText("");

        control_displacement = null;
        control_drive = null;
        control_fuel = null;
        control_operation = null;

        carInfo = null;

        list_part.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 2-86 获取本维修厂该车已确认的报价单上配件清单接口,用于购件模块。 getquotedpriceparts(userid,carno,dept_id)
     * 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
    private void getquotedpriceparts() {
        RequestParams params = creatParams("getquotedpriceparts");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("carno", getText(carNunber));
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().post(params, ORMBean.PartBean.class, new HttpUtil.HttpCallBack<ORMBean.PartBean>() {
            @Override
            public void onSuccess(ORMBean.PartBean result) {
                if (result.getRes().equals("1")) {

                    List<Part> data = result.getData();

                    list_part.clear();

                    for (Part p : data) {
                        list_part.add(new InquiryPart(p.getParts_name(), p.getQty(), p.getCostprice(), p.getCostprice(), p.getQty_unit()));
                    }

                    adapter.notifyDataSetChanged();
                }
                dialog_loding.dismiss(mContent);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContent);
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
                    CarInfo carInfo = result.getData().get(0);

                    carNunber.setText(carInfo.getCarno());
                    brand.setText(carInfo.getBrands());
                    models.setText(carInfo.getTypecode());
                    year.setText(carInfo.getProductyear());
                    vinCode.setText(carInfo.getVincode());

                    displacement.setText(carInfo.getOutput());
                    drive.setText(carInfo.getDrive_type());
                    fuel.setText(carInfo.getFuel());
                    operation.setText(carInfo.getOperation_type());
                    loginData.setText(carInfo.getReg_date());
                    engine.setText(carInfo.getEnginecode());

                    control_displacement = new Control(carInfo.getOutput_id(), carInfo.getOutput());
                    control_drive = new Control(carInfo.getDrive_id(), carInfo.getDrive_type());
                    control_fuel = new Control(carInfo.getFuel_id(), carInfo.getFuel());
                    control_operation = new Control(carInfo.getOperation_id(), carInfo.getOperation_type());

                    if (type == 0) {
                        getquotedpriceparts();
                    } else
                        getnewbuym(carInfo.getCarno());
                } else {
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
     * 2-85 本维修厂车牌号模糊查询 接口 carnofuzzyquery(userid,carno,dept_id)
     * 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
    private void carnofuzzyquery() {
        RequestParams params = creatParams("carnofuzzyquery");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("carno", TextUtils.isEmpty(getText(carNunber)) ? "" : getText(carNunber));
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carInfo.clear();
                list_carInfo.addAll(data);

                dialog_carno.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    /**
     * 2-83 通过购件单ID获取购件单信息接口 getbuyminfobybuymid(userid,buym_id)
     * 当前用户ID:userid,购件单ID：buym_id
     */
    private void getbuyminfobybuymid(final String id) {
        RequestParams params = creatParams("getbuyminfobybuymid");
        params.addBodyParameter("buym_id", id);

        dialog_loding.show(mContent);
        HttpUtil.getInstance().post(params, ORMBean.PurchaseOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PurchaseOrderBean>() {
            @Override
            public void onSuccess(ORMBean.PurchaseOrderBean result) {
                if (result.getRes().equals("1")) {
                    PurchaseOrder purchaseOrder = result.getData().get(0);

                    carInfo = new PurchaseOrder();
                    carInfo.setBf_buym_id(id);

                    carNunber.setText(purchaseOrder.getCarno());
                    brand.setText(purchaseOrder.getBrands());
                    models.setText(purchaseOrder.getTypecode());
                    year.setText(purchaseOrder.getProductyear());
                    vinCode.setText(purchaseOrder.getVincode());

                    displacement.setText(purchaseOrder.getOutput());
                    drive.setText(purchaseOrder.getDrive_type());
                    fuel.setText(purchaseOrder.getFuel());
                    operation.setText(purchaseOrder.getOperation_type());
                    loginData.setText(purchaseOrder.getReg_date());
                    engine.setText(purchaseOrder.getEnginecode());

                    plandate.setText(purchaseOrder.getPlandate());
                    name.setText(purchaseOrder.getName());
                    replayName.setText(purchaseOrder.getReplay_name());
                    replayTime.setText(purchaseOrder.getReplay_time());

                    getlistrepairparts();
                } else {
                    ToastUtil.showShort(mContent, result.getMsg());
                    dialog_loding.dismiss(mContent);
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContent);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
                return true;
            } else
                return false;
        }
        return false;
    }
}
