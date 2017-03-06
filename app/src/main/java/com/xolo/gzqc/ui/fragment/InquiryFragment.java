package com.xolo.gzqc.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.xolo.gzqc.bean.child.Inquiry;
import com.xolo.gzqc.bean.child.InquriyOrder;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CarBrandActivity;
import com.xolo.gzqc.ui.activity.pickcar.AddInquiryParts;
import com.xolo.gzqc.ui.dialog.SelectYearDialog;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.OnKeyDownListener;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
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
 * 询价
 */
public class InquiryFragment extends BaseFragment implements OnKeyDownListener{

    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.brand)
    EditText brand;
    @BindView(R.id.models)
    EditText models;
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
    @BindView(R.id.vin_code)
    EditText vinCode;
    @BindView(R.id.engine)
    EditText engine;
    @BindView(R.id.login_data)
    TextView loginData;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.tr)
    TableRow tr;
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
    @BindView(R.id.vaild_time)
    TextView etVaildTime;
    @BindView(R.id.tb_vaild_time)
    TableRow tbVaildTime;
    @BindView(R.id.query_name)
    TextView queryName;
    @BindView(R.id.tb_query_name)
    TableRow tbQueryName;
    @BindView(R.id.replay_name)
    TextView replayName;
    @BindView(R.id.tb_replay_name)
    TableRow tbReplayName;
    @BindView(R.id.replay_time)
    TextView replayTime;
    @BindView(R.id.tb_replay_time)
    TableRow tbReplayTime;
    @BindView(R.id.tb_valid_time_read)
    TableRow tbValidTimeRead;
    @BindView(R.id.valid_time_read)
    TextView validTimeRead;

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
    private Dialog dialog_date_vaild;
    private Dialog dialog_year;
    private LoadDialog dialog_loding;

    private Inquiry carInfo;
    private CommenAdapter<InquiryPart> adapter;

    private LicenseKeyboardUtil keyboardUtil;

    private int type;

    private boolean isHidden = true;

    private LoadDialog loadDialog;

    private String inquiry_id;

    private Brand brand_rt;
    private TypeCode typeCode_rt;

    private InquriyOrder inquriyOrder;

    public InquiryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inquiry, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.displacement, R.id.drive, R.id.fuel, R.id.operation, R.id.btn_add, R.id.confirm, R.id.select, R.id.login_data, R.id.btn_more, R.id.vaild_time,R.id.brand,R.id.year})
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
                Intent intent = new Intent(mContext, AddInquiryParts.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(carNunber.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请输入车牌号");
                    return;
                }
                if (TextUtils.isEmpty(brand.getText().toString().trim())) {
                    ToastUtil.showShort(mContent, "请输入品牌");
                    return;
                }
                if (TextUtils.isEmpty(models.getText().toString().trim())) {
                    ToastUtil.showShort(mContent, "请输入型号");
                    return;
                }
                if (list_part.size() == 0) {
                    ToastUtil.showShort(mContent, "配件清单不能为空");
                    return;
                }

                if (is_edit) {
                    editqueryppricem();
                    return;
                }

//              0 接车员新增询价清单
//              1  购件人发送价格
                if (type == 1) {
                    sendquerypriced();
                } else {
                    addqueryppricem();
                }
                break;
            case R.id.select:
                carNunber.clearFocus();
                carnofuzzyquery();
//                listcarinfo();
                break;
            case R.id.login_data:
                dialog_date.show();
                break;
            case R.id.vaild_time:
                dialog_date_vaild.show();
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
                if (!brand.isFocusableInTouchMode()){
                    startActivityForResult(new Intent(mContext, CarBrandActivity.class),CarBrandActivity.GET_BRAND);
                }
                break;
            case R.id.year:
                dialog_year.show();
                break;
        }
    }


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


    /**
     * type: 0 接车员
     * 1采购员   能编辑配件，不能新增      改为发送价格   消息（即时通讯）
     * 采购员回复询价的时候，需要增加一个该报价的有效时间。
     * 2询价历史  只读数据
     */
    private void initIntent() {
        Intent intent = mContent.getIntent();
        if (type == 1) {
            btnAdd.setVisibility(View.GONE);
            select.setVisibility(View.GONE);
            tbVaildTime.setVisibility(View.VISIBLE);
            confirm.setText("发送价格");

            lv.setLongClickable(false);
            setEditable(new EditText[]{carNunber, brand, models ,vinCode, engine});
            setCompoundDrawables(new TextView[]{displacement, fuel, drive, loginData, operation,brand,year});
            setClickale(new TextView[]{displacement, fuel, drive, loginData, operation,brand,year});
            setHint(new TextView[]{displacement, fuel, drive, loginData, operation, carNunber, brand, models, year, vinCode, engine});
        }
        if (type == 2) {
            btnAdd.setVisibility(View.GONE);
            select.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            setVisibility(new View[]{btnAdd, select, confirm}, View.GONE);
            setVisibility(new View[]{tbQueryName, tbReplayName, tbValidTimeRead, tbReplayTime}, View.VISIBLE);

            setEditable(new EditText[]{carNunber, brand, models, vinCode, engine});
            setCompoundDrawables(new TextView[]{displacement, fuel, drive, loginData, operation,brand});
            setClickale(new TextView[]{displacement, fuel, drive, loginData, operation,brand});
            setHint(new TextView[]{displacement, fuel, drive, loginData, operation, carNunber, brand, models, year, vinCode, engine});
        }

       inquiry_id = intent.getStringExtra(IntentConstant.INQUIRY_ID);

        if (!TextUtils.isEmpty(inquiry_id)) {
            LogUtil.i(inquiry_id);
            getquerypricembypricemid();
            return;
        }

        String no = mContent.getIntent().getStringExtra(Key.CARNO);

        if (!TextUtils.isEmpty(no)) {
                getcarinfobycarno(no);
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
        dialog_date_vaild = creatDateDialog(etVaildTime);
        dialog_year = SelectYearDialog.creat(mContext,year);
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_part_inquiry, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                InquiryPart part = list_part.get(position);

                holder.setText(R.id.item1,part.getParts_name());
                holder.setText(R.id.item2,part.getPrice());
                holder.setText(R.id.item3,"x "+part.getQty()+" ( "+part.getQty_unit()+" )");
                holder.setText(R.id.item4,part.getSource());

                double total = Double.parseDouble(part.getPrice()) * Integer.parseInt(part.getQty());
                holder.setText(R.id.item5,"￥"+String.valueOf(total));
            }
        });
        lv.setAdapter(adapter);

        if (type != 2) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, AddInquiryParts.class);
                    intent.putExtra(Key.IS_EDIT, true);
                    if (type == 1) {
                        intent.putExtra("type", 1);
                    }
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
        if (requestCode == CarBrandActivity.GET_BRAND && resultCode == CarBrandActivity.RETURN_BRAND){
            brand_rt = (Brand) data.getSerializableExtra(IntentConstant.BRAND);
            typeCode_rt = (TypeCode) data.getSerializableExtra(IntentConstant.TYPE_CODE);
            brand.setText(brand_rt.getBrands());
            models.setText(typeCode_rt.getTypecode());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 2-32 新增询价单接口 addqueryppricem(userid,carno,brands,typecode,output_id,output,productyear,drive_id,drive_type,operation_id,
     * operation_type,fuel_id,fuel,vincode,reg_date,enginecode,name,mobile,json_parts,dept_id,dept_name)
     * 当前用户ID:userid,车牌号:carno,车品牌名:brands,车型代码:typecode,排量ID:output_id,排量:output,车辆生产年份:productyear,
     * 驱动类型ID:drive_id,驱动类型:drive_type,操控方式ID:operation_id,操控方式:operation_type,燃料ID:fuel_id,燃料:fuel,VIN码:vincode,
     * 注册日期:reg_date,发动机号:enginecode,询价人姓名:name,询价人手机号:mobile,询价配件清单列表:json_parts,所属维修厂ID:dept_id,所属维修厂名称:dept_name
     */
    private void addqueryppricem() {
        RequestParams params = creatParams("addqueryppricem");
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
     * 2-35 A 通过询价单ID获取询价配件清单接口 getlistquerypriced(userid,query_pricem_id)
     * 当前用户ID:userid,询价单ID:query_pricem_id
     */
    private void getlistquerypriced() {
        RequestParams params = creatParams("getlistquerypriced");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.QUERY_PRICEM_ID, carInfo == null ? inquiry_id : carInfo.getBf_query_pricem_id());
        params.addBodyParameter(Key.CARNO, getText(carNunber));

        HttpUtil.getInstance().post(params, ORMBean.InquiryPartBean.class, new HttpUtil.HttpCallBack<ORMBean.InquiryPartBean>() {
            @Override
            public void onSuccess(ORMBean.InquiryPartBean result) {
                if (result.getRes().equals("1")) {
                    List<InquiryPart> data = result.getData();
                    list_part.clear();
                    list_part.addAll(data);
                    adapter.notifyDataSetChanged();
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
     * 2-35 通过车牌号获取本维修厂该车最新的询价单接口 getnewquerypricem(userid,query_priced_id)
     * 当前用户ID:userid,询价配件清单表ID:query_priced_id
     *
     * @param carno
     */
    private void getnewquerypricem(String carno) {
        RequestParams params = creatParams("getnewquerypricem");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, carno);
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().post(params, ORMBean.InquiryBean.class, new HttpUtil.HttpCallBack<ORMBean.InquiryBean>() {
            @Override
            public void onSuccess(ORMBean.InquiryBean result) {
                if (result.getRes().equals("1")) {
                    carInfo = result.getData().get(0);
                } else {
//                    ToastUtil.showShort(mContext, result.getMsg());
                }
                getlistquerypriced();
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
        params.addBodyParameter(Key.QUERY_PRICEM_ID, carInfo.getBf_query_pricem_id());

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
        params.addBodyParameter("brands_id",brand_rt==null?"":brand_rt.getBrands_id());
        params.addBodyParameter("typecode_id",typeCode_rt == null ?"":typeCode_rt.getTypecode_id());
    }


    /**
     * 4-3 发送价格接口 sendquerypriced(userid,query_pricem_id,user_name,json_parts)
     * 当前用户ID：userid,询价单ID:query_pricem_id,当前用户姓名:user_name,配件询价清单列表:json_parts
     */
    private void sendquerypriced() {
        RequestParams params = creatParams("sendquerypriced");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("query_pricem_id",inquiry_id);
        params.addBodyParameter("user_name", user.getUser_name());
        params.addBodyParameter("valid_time", getText(etVaildTime));
        params.addBodyParameter("json_parts", "{\"data\":" + list_part.toString() + "}");

        loadDialog.show(mContent);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {

                if (result.getRes().equals("1")) {
                    String mobile = inquriyOrder.getMobile();
                    if (!TextUtils.isEmpty(mobile)) {
//                        发送融云信息
                        rongImSend(mobile);
                    } else {
                        //  构件人成功发送价格后刷新返回的列表
                        mContent.setResult(Activity.RESULT_OK);
                        loadDialog.dismiss(mContent);
                        mContent.finish();
                    }
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                    loadDialog.dismiss(mContent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
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

                    if (type == 1) {
                        tr.setVisibility(View.VISIBLE);
                        name.setText(carInfo.getName());
                    }

                    control_displacement = new Control(carInfo.getOutput_id(), carInfo.getOutput());
                    control_drive = new Control(carInfo.getDrive_id(), carInfo.getDrive_type());
                    control_fuel = new Control(carInfo.getFuel_id(), carInfo.getFuel());
                    control_operation = new Control(carInfo.getOperation_id(), carInfo.getOperation_type());

                    getnewquerypricem(carInfo.getCarno());
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
     * 2-79 通过询价单ID获取询价单信息接口 getquerypricembypricemid(userid,query_pricem_id)
     * 当前用户ID:userid,询价单ID：query_pricem_id
     */
    private void getquerypricembypricemid() {
        RequestParams params = creatParams("getquerypricembypricemid");
        params.addBodyParameter("query_pricem_id", inquiry_id);
        params.addBodyParameter(Key.USER_ID, user.getUser_id());

        dialog_loding.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.InquriyOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.InquriyOrderBean>() {
            @Override
            public void onSuccess(ORMBean.InquriyOrderBean result) {
                if (result.getRes().equals("1")) {
                     inquriyOrder = result.getData().get(0);

                    carNunber.setText(inquriyOrder.getCarno());
                    brand.setText(inquriyOrder.getBrands());
                    models.setText(inquriyOrder.getTypecode());
                    year.setText(inquriyOrder.getProductyear());
                    vinCode.setText(inquriyOrder.getVincode());

                    displacement.setText(inquriyOrder.getOutput());
                    drive.setText(inquriyOrder.getDrive_type());
                    fuel.setText(inquriyOrder.getFuel());
                    operation.setText(inquriyOrder.getOperation_type());
                    loginData.setText(inquriyOrder.getReg_date());
                    engine.setText(inquriyOrder.getEnginecode());

                    if (type == 1) {
                        tr.setVisibility(View.VISIBLE);
                        name.setText(inquriyOrder.getName());
                    }
                    if (type == 2) {
                        queryName.setText(inquriyOrder.getQuery_name());
                        replayName.setText(inquriyOrder.getReplay_name());
                        replayTime.setText(inquriyOrder.getReplay_time());
                        validTimeRead.setText(inquriyOrder.getValid_time());
                    }

                    control_displacement = new Control(inquriyOrder.getOutput_id(), inquriyOrder.getOutput());
                    control_drive = new Control(inquriyOrder.getDrive_id(), inquriyOrder.getDrive_type());
                    control_fuel = new Control(inquriyOrder.getFuel_id(), inquriyOrder.getFuel());
                    control_operation = new Control(inquriyOrder.getOperation_id(), inquriyOrder.getOperation_type());

                    getlistquerypriced();
//                    getnewquerypricem(carInfo.getCarno());
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


    private void rongImSend(String phone) {

        TextMessage myTextMessage = TextMessage.obtain(getText(carNunber) + "询价成功");

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

                //  构件人成功发送价格后刷新返回的列表
                mContent.setResult(Activity.RESULT_OK);
                ToastUtil.showShort(mContext, "发送价格成功");
                loadDialog.dismiss(mContent);
                mContent.finish();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回
                dialog_loding.dismiss(mContext);
                ToastUtil.showShort(mContext, "通知接车员失败");
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
