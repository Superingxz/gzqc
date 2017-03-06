package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarState;
import com.xolo.gzqc.bean.child.Comfault;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.OwnerInfo;
import com.xolo.gzqc.bean.child.PickUpOrder;
import com.xolo.gzqc.bean.child.Receiveannex;
import com.xolo.gzqc.bean.child.SendMan;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CarBrandActivity;
import com.xolo.gzqc.ui.activity.PhotoActivity;
import com.xolo.gzqc.ui.dialog.BottomMenuDialog;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Base64Utils;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 接车
 */
public class PickUpCarActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.carno)
    EditText carno;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.name_send)
    EditText nameSend;
    @BindView(R.id.phone_send)
    EditText phoneSend;
    @BindView(R.id.link_name)
    EditText linkName;
    @BindView(R.id.link_phone)
    EditText linkPhone;
    @BindView(R.id.get_car_name)
    EditText getCarName;
    @BindView(R.id.get_car_phone)
    EditText getCarPhone;
    @BindView(R.id.brand)
    EditText brand;
    @BindView(R.id.mileage)
    EditText mileage;
    @BindView(R.id.btn_yiBiao)
    Button btnYiBiao;
    @BindView(R.id.oil)
    TextView oil;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.in_time)
    TextView inTime;
    @BindView(R.id.book_time)
    TextView bookTime;
    @BindView(R.id.btn_part)
    Button btnPart;
    @BindView(R.id.btn_histroy)
    Button btnHistroy;
    @BindView(R.id.btn_add)
    ImageButton btnAdd;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.btn_guzhang)
    Button btnGuzhang;
    @BindView(R.id.is_save_des)
    CheckBox isSaveDes;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.is_rework)
    CheckBox isRework;
    @BindView(R.id.is_check)
    CheckBox isCheck;
    @BindView(R.id.is_print)
    CheckBox isPrint;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.requery)
    Button requery;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;
    @BindView(R.id.typecode)
    EditText typecode;

    private Dialog dialog_oil;
    private Dialog dialog_owner;
    private Dialog dialog_sendName;
    private Dialog dialog_sendPhone;
    private Dialog dialog_comfault;
    private Dialog dialog_carno;
    private Dialog dialog_data1;
    private Dialog dialog_data2;
    private Dialog dialog_select;
    private Dialog dialog_query;
    private LoadDialog dialog_loding;
    //选择图片
    private BottomMenuDialog dialog_select_photo;

    private List<Control> list_oil = new ArrayList<>();
    private List<String> list_owner = new ArrayList<>();
    private List<SendMan> list_sendMan = new ArrayList<>();
    private List<String> list_sendPhone = new ArrayList<>();
    private List<Comfault> list_comfault = new ArrayList<>();
    private List<CarState> list_carstate = new ArrayList<>();
    private List<CarInfo> list_carInfo = new ArrayList<>();
    private List<CarInfo> list_carInfo2 = new ArrayList<>();
    private ArrayList<Receiveannex> list_receiveannex = new ArrayList<>();

    private Control control_oil;

    private CommenAdapter<CarState> adapter;

    private CarInfo carInfo;
    private OwnerInfo ownerInfo;
    private SendMan sendMan;

    private Bitmap bitmap;

    private int select_position;

    private PhotoUtils photoUtils;

    private LicenseKeyboardUtil keyboardUtil;

    private Brand brand_rt;
    private TypeCode typeCode_rt;

    /**
     * 二维码返回
     */
    private String qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up_car);
        ButterKnife.bind(this);
        init();
        initDialog();
        initLv();
        initIntent();
        setPortraitChangeListener();
    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                bitmap = PhotoUtils.getZoomImage(uri,100);
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }


    private void initIntent() {
        String no = getIntent().getStringExtra(Key.CARNO);
        qr_code = getIntent().getStringExtra("code");
        if (!TextUtils.isEmpty(no)) {
            carno.setText(no);
            getcarinfobycarno(no);
        }
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_car_state, mContext, list_carstate, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarState carState = list_carstate.get(position);
                holder.setText(R.id.item1, carState.getType_name());
                holder.setText(R.id.item2, carState.getDescription());

                if (!TextUtils.isEmpty(carState.getPhoto())) {
                    holder.setImage(R.id.item3, Base64Utils.Base64StrByBitmap(carState.getPhoto()));
                }

            }
        });
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final CarState car = list_carstate.get(position);
                new AlertDialog.Builder(mContext).setMessage("是否删除").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list_carstate.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_position = position;

                Intent intent = new Intent(mContext, AddCarStateActivity.class);
                intent.putExtra("carstate", list_carstate.get(position));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    private void init() {

        carno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   if (s.length() == 7){
                       getcarinfobycarno(s.toString());
                   }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        brand.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                brand.setFocusable(true);
                brand.setFocusableInTouchMode(true);
                brand.setClickable(false);
                return true;
            }
        });
        typecode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                typecode.setFocusable(true);
                typecode.setFocusableInTouchMode(true);
                return true;
            }
        });

        keyboardUtil = new LicenseKeyboardUtil(mContext, carno, keyboardView);

        initDate();
    }

    private void initDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        inTime.setText(simpleDateFormat.format(new Date()));
    }

    private void initDialog() {
        dialog_oil = creatListDialog("油量", list_oil, oil, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control) {
                control_oil = control;
            }
        });


        dialog_owner = creatListDialog("车主手机", list_owner, phone);

        dialog_sendName = creatListDialog("送车人姓名", list_sendMan, nameSend, new ListDialogCallBack<SendMan>() {
            @Override
            public String setText(SendMan sendMan) {
                return sendMan.getSendmanname();
            }

            @Override
            public void onClick(SendMan sendMan2) {
                sendMan = sendMan2;

                list_sendPhone.clear();
                list_sendPhone.add(sendMan2.getPhone());
                list_sendPhone.add(sendMan2.getPhone2());
            }

        });

        dialog_sendPhone = creatListDialog("送车人手机号", list_sendPhone, phoneSend);

        dialog_comfault = creatListDialog("常见故障描述", list_comfault, description, new ListDialogCallBack<Comfault>() {
            @Override
            public String setText(Comfault comfault) {
                return comfault.getContent();
            }

            @Override
            public void onClick(Comfault comfault) {
            }
        });


        dialog_carno = creatListDialog("车牌号", list_carInfo, carno, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getCarno();
            }

            @Override
            public void onClick(CarInfo carInfo) {
                list_owner.clear();
                list_sendPhone.clear();
                list_sendMan.clear();

                nameSend.setText("");
                phoneSend.setText("");

                getcarinfobycarno(carInfo.getCarno());
            }
        });


        dialog_query = creatListDialog("车牌号", list_carInfo2, carno, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getCarno();
            }

            @Override
            public void onClick(CarInfo carInfo) {
                list_owner.clear();
                list_sendPhone.clear();
                list_sendMan.clear();

                nameSend.setText("");
                phoneSend.setText("");

                getcarinfobycarno(carInfo.getCarno());
            }
        });


        dialog_data1 = creatDateDialog(inTime);

        dialog_data2 = creatDateDialog(bookTime);


//        报价、拆检、询价、返回首页
        String[] array = new String[]{"报价", "拆检", "询价", "返回首页"};
        List<String> strings1 = Arrays.asList(array);
        dialog_select = creatListDialog("", strings1, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                Intent intent = null;
                if (s.equals("报价")) {
                    intent = new Intent(mContext, OfferActivity.class);
                } else if (s.equals("拆检")) {
                    intent = new Intent(mContext, VerhaulActivity.class);
                } else if (s.equals("询价")) {
                    intent = new Intent(mContext, InquiryActivity.class);
                } else if (s.equals("返回首页")) {
                    finish();
                    return;
                }
                intent.putExtra(Key.CARNO, getText(carno));
                startActivity(intent);
                finish();
            }
        });

//选择图片
        dialog_select_photo = new BottomMenuDialog(mContext, "拍照", "从本地导入");
        dialog_select_photo.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.takePicture(mContext);
                dialog_select_photo.dismiss();
            }
        });
        dialog_select_photo.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.selectPicture(mContext);
                dialog_select_photo.dismiss();
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

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
            @Override
            public void onSuccess(ORMBean.ControlBean result) {
                List<Control> data = result.getData();
                list_oil.addAll(data);
                dialog_oil.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @OnClick({R.id.oil, R.id.btn_guzhang, R.id.confirm, R.id.phone, R.id.name_send, R.id.phone_send, R.id.btn_add, R.id.btn_part, R.id.in_time, R.id.book_time, R.id.select, R.id.iv, R.id.btn_yiBiao, R.id.requery,R.id.brand})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.oil:
                if (list_oil.size() > 0) {
                    dialog_oil.show();
                    return;
                }
                getcontrolbytype("5");
                break;
            case R.id.btn_guzhang:
                if (list_comfault.size() > 0) {
                    dialog_comfault.show();
                    return;
                }
                listcomfault();
                break;
            case R.id.confirm:
                if (TextUtils.isEmpty(carno.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请填写车牌号");
                    return;
                }
                if (TextUtils.isEmpty(mileage.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请填写里程");
                    return;
                }
                receivecar();
                break;
            case R.id.phone:
                if (list_owner.size() > 0) {
                    dialog_owner.show();
                }
                break;
            case R.id.name_send:
                if (list_sendMan.size() > 0) {
                    dialog_sendName.show();
                }
                break;
            case R.id.phone_send:
                if (list_sendPhone.size() > 0) {
                    dialog_sendPhone.show();
                }
                break;
            case R.id.btn_add:
                startActivityForResult(new Intent(mContext, AddCarStateActivity.class), REQUEST_CODE);
                break;
            case R.id.btn_part:
                Intent intent = new Intent(mContext, ReceiveannexActivity.class);
                if (list_receiveannex.size() != 0) {
                    intent.putParcelableArrayListExtra(Key.OBJECT, list_receiveannex);
                }
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.book_time:
                dialog_data2.show();
                break;
            case R.id.in_time:
                dialog_data1.show();
                break;
            case R.id.select:
                if (list_carInfo.size() > 0) {
                    dialog_carno.show();
                    return;
                }
                listcarinfo();
                break;
            case R.id.iv:
                if (bitmap != null) {
                    Intent intent1 = new Intent(mContext, PhotoActivity.class);
                    intent1.putExtra("bitmap", bitmap);
                    startActivity(intent1);
                }
                break;
            case R.id.btn_yiBiao:
                dialog_select_photo.show();
                break;
            case R.id.requery:
                carnofuzzyquery();
                break;
            case R.id.brand:
                if (!brand.isFocusableInTouchMode()){
                    startActivityForResult(new Intent(mContext, CarBrandActivity.class),CarBrandActivity.GET_BRAND);
                }
                break;
        }
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
                    brand.setText(carInfo.getBrands());
                    typecode.setText(carInfo.getTypecode());

                    getcarownerbycarno(no);
                } else {
//                    startActivity(new Intent(mContext, OwnerInfoActivity.class));
//
//                    ToastUtil.showShort(mContext, "无该车车辆信息，请添加车辆");
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

                    list_owner.clear();
                    list_owner.add(ownerInfo1.getPhone());

                    String phone2 = ownerInfo1.getPhone2();
                    if (!TextUtils.isEmpty(phone2)) {
                        list_owner.add(phone2);
                    }

                    getsendmanbycarno(no);
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
     * 通过车牌号获取该车的送修人信息接口 getsendmanbycarno(userid,carno) 当前用户ID:userid,车牌号:carno
     */
    private void getsendmanbycarno(String no) {
        RequestParams params = creatParams("getsendmanbycarno");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, no);

        HttpUtil.getInstance().post(params, ORMBean.SendManBean.class, new HttpUtil.HttpCallBack<ORMBean.SendManBean>() {
            @Override
            public void onSuccess(ORMBean.SendManBean result) {

                if (result.getRes().equals("1")) {
                    List<SendMan> data = result.getData();
                    list_sendMan.addAll(data);

                    SendMan sendMan1 = data.get(0);
                    sendMan = sendMan1;

                    nameSend.setText(sendMan1.getSendmanname());
                    phoneSend.setText(sendMan1.getPhone());

                    list_sendPhone.clear();
                    list_sendPhone.add(sendMan1.getPhone());

                    String phone2 = sendMan1.getPhone2();
                    if (!TextUtils.isEmpty(phone2)) {
                        list_sendPhone.add(phone2);
                    }

                } else {
                    if (TextUtils.isEmpty(getText(nameSend))) {
                        nameSend.setText(name.getText());
                    }
                    if (TextUtils.isEmpty(getText(phoneSend))) {
                        phoneSend.setText(phone.getText());
                    }
                }

                getnewreceiveinfo();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    /**
     * 获取常见故障列表接口 listcomfault(userid,dept_id) 当前用户ID:userid,维修厂ID:dept_id
     */
    private void listcomfault() {
        RequestParams params = creatParams("listcomfault");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ComfaultBean.class, new HttpUtil.HttpCallBack<ORMBean.ComfaultBean>() {
            @Override
            public void onSuccess(ORMBean.ComfaultBean result) {
                List<Comfault> data = result.getData();
                list_comfault.addAll(data);

                dialog_comfault.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-15 接车接口
     * receivecar(userid,car_info_id,carno,enginecode,oil_id,oil,mileage,in_time,book_time,description,car_photo,is_rework,is_check,is_print,is_save_des,
     * json_car_status,follow_car_goods,operatorid,operatoname,dept_id,dept_name,car_owner_id,car_owner_name,car_owner_sex,car_owner_mobile,sendmanid,sendmanname,
     * sendmansex,sendmanphone)
     * 当前用户ID:userid,车辆信息表ID:car_info_id,车牌号:carno,发动机号:enginecode,油量ID:oil_id,油量:oil,行驶里程:mileage,进厂时间:in_time,
     * 预交车时间:book_time,维修描述:description,仪表盘照片:car_photo,属于返修:is_rework,同意拆检:is_check,打印接车单:is_print,
     * 保存描述:is_save_des,车况记录列表:json_car_status,随车附件记录:follow_car_goods,接车人ID:operatorid,接车人姓名:operatoname,
     * 维修厂ID:dept_id,维修厂名称:dept_name,车主ID:car_owner_id,车主姓名:car_owner_name,车主性别:car_owner_sex,车主手机号:car_owner_mobile,
     * 送修人ID:sendmanid,送修人姓名:sendmanname,送修人性别:sendmansex,送修人手机号:sendmanphone,
     * 联系人姓名:link_name,联系人手机号:link_phone,提车人姓名:get_car_name,提车人手机号:get_car_phone
     */

    private void receivecar() {
        if (TextUtils.isEmpty(carno.getText().toString().trim())) {
            ToastUtil.showShort(mContext, "请先选择或填写车牌号");
            return;
        }
        if (TextUtils.isEmpty(brand.getText().toString().trim())) {
            ToastUtil.showShort(mContext, "请填写车辆品牌");
            return;
        }
        if (TextUtils.isEmpty(phone.getText().toString().trim())) {
            ToastUtil.showShort(mContext, "请填写手机号");
            return;
        }


        iv.setImageBitmap(bitmap);

        StringBuffer buffer = new StringBuffer("");
        for (int i = 0; i < list_carstate.size(); i++) {
            CarState carState = list_carstate.get(i);

            if (i == 0) {
                buffer.append(carState.getPhotolist());
            } else {
                buffer.append("#").append(carState.getPhotolist());
            }
        }


        RequestParams params = creatParams("receivecar");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_INFO_ID, carInfo == null ? "" : carInfo.getBc_car_info_id());
        params.addBodyParameter(Key.CARNO, carno.getText().toString().trim());
        params.addBodyParameter(Key.ENGINECODE, carInfo == null ? "" : carInfo.getEnginecode());
        params.addBodyParameter(Key.OIL, control_oil == null ? "" : control_oil.getDisplay_data());
        params.addBodyParameter(Key.OIL_ID, control_oil == null ? "" : control_oil.getControl_id());
        params.addBodyParameter(Key.MILEAGE, mileage.getText().toString().trim());
        params.addBodyParameter(Key.IN_TIME, inTime.getText().toString().trim());
        params.addBodyParameter(Key.BOOK_TIME, bookTime.getText().toString().trim());
        params.addBodyParameter("description", description.getText().toString().trim());
        params.addBodyParameter(Key.CAR_PHOTO, bitmap == null ? "" : Base64Utils.Bitmap2StrByBase64(bitmap));
        params.addBodyParameter(Key.IS_REWORK, isRework.isChecked() ? "1" : "0");
        params.addBodyParameter(Key.IS_CHECK, isCheck.isChecked() ? "1" : "0");
        params.addBodyParameter(Key.IS_PRINT, isPrint.isChecked() ? "1" : "0");
        params.addBodyParameter(Key.IS_SAVE_DES, isSaveDes.isChecked() ? "1" : "0");
        params.addBodyParameter(Key.FOLLOW_CAR_GOODS, "{\"data\":" + list_receiveannex.toString() + "}");
        params.addBodyParameter(Key.jSON_CAR_STATUS, "{\"data\":" + list_carstate.toString() + "}");
        params.addBodyParameter(Key.OPERATION_ID, user.getUser_id());
        params.addBodyParameter(Key.OPERATONAME, user.getUser_name());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.DEPT_NAME, user.getDept_name());
        params.addBodyParameter(Key.CAR_OWNER_ID, ownerInfo == null ? "" : ownerInfo.getCar_owner_id());
        params.addBodyParameter(Key.CAR_OWNER_NAME, getText(name));
        params.addBodyParameter(Key.CAR_OWNER_SEX, ownerInfo == null ? "" : ownerInfo.getSex());
        params.addBodyParameter(Key.CAR_OWNER_MOBILE, phone.getText().toString().trim());
        params.addBodyParameter(Key.SENDMANID, sendMan == null ? ownerInfo == null ? "" : ownerInfo.getCar_owner_id() : sendMan.getSendmanid());
        params.addBodyParameter("sendmanname", getText(nameSend));
        params.addBodyParameter(Key.SENDMANSEX, sendMan == null ? ownerInfo == null ? "" : ownerInfo.getSex() : sendMan.getSex());
        params.addBodyParameter(Key.SENDMANPHONE, phoneSend.getText().toString().trim());
        params.addBodyParameter("photo", buffer.toString());
//        联系人姓名:link_name,联系人手机号:link_phone,提车人姓名:get_car_name,提车人手机号:get_car_phone
        params.addBodyParameter("link_name", getText(linkName));
        params.addBodyParameter("link_phone", getText(linkPhone));
        params.addBodyParameter("get_car_name", getText(getCarName));
        params.addBodyParameter("get_car_phone", getText(getCarPhone));
        params.addBodyParameter("brands", getText(brand));
        params.addBodyParameter("qr_code", qr_code);
        params.addBodyParameter(Key.TYPECODE,getText(typecode));
        params.addBodyParameter("brands_id",brand_rt==null?"":brand_rt.getBrands_id());
        params.addBodyParameter("typecode_id",typeCode_rt == null ?"":typeCode_rt.getTypecode_id());


        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
                dialog_select.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        qr_code = "";

        if (requestCode == REQUEST_CODE) {
            if (resultCode == 1) {
                CarState carState = (CarState) data.getParcelableExtra(Key.OBJECT);

                list_carstate.add(carState);
                adapter.notifyDataSetChanged();
            } else if (resultCode == 2) {
                list_receiveannex = data.getParcelableArrayListExtra(Key.JSON_PATRS);
            } else if (resultCode == 3) {
                CarState carState = (CarState) data.getParcelableExtra(Key.OBJECT);
                list_carstate.remove(select_position);
                list_carstate.add(select_position,carState);
                adapter.notifyDataSetChanged();
            } else if (resultCode == RESULT_OK) {
                getcarnobyqrcode(data.getExtras().getString("result"));
            }
        }

        if (requestCode == CarBrandActivity.GET_BRAND && resultCode == CarBrandActivity.RETURN_BRAND){
            brand_rt = (Brand) data.getSerializableExtra(IntentConstant.BRAND);
            typeCode_rt = (TypeCode) data.getSerializableExtra(IntentConstant.TYPE_CODE);
            brand.setText(brand_rt.getBrands());
            typecode.setText(typeCode_rt.getTypecode());
        }

        photoUtils.onActivityResult(PickUpCarActivity.this, requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
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

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
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

    /**
     * 2-30 通过接车单ID获取车况列表记录接口 getcarstatusinfo(userid,receive_id) 当前用户ID:userid,接车单ID:receive_id
     */
    private void getcarstatusinfo() {
        RequestParams params = creatParams("getcarstatusinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
    }

    /**
     * 2-29 通过车牌号获取本维修厂最新的接车信息接口 getnewreceiveinfo(userid,carno,dept_id) 当前用户ID:userid,
     * 车牌号:carno,维修厂ID:dept_id
     */
    private void getnewreceiveinfo() {
        RequestParams params = creatParams("getnewreceiveinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("carno", carInfo.getCarno());


        HttpUtil.getInstance().post(params, ORMBean.PickUpOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PickUpOrderBean>() {
            @Override
            public void onSuccess(ORMBean.PickUpOrderBean result) {
                if (result.getRes().equals("1")) {
                    PickUpOrder pickUpOrder = result.getData().get(0);

                    if (!TextUtils.isEmpty(pickUpOrder.getLink_phone())) {
                        linkPhone.setText(pickUpOrder.getLink_phone());
                    } else {
                        linkPhone.setText(getText(phone));
                    }
                    if (!TextUtils.isEmpty(pickUpOrder.getLink_name())) {
                        linkName.setText(pickUpOrder.getLink_name());
                    } else {
                        linkName.setText(getText(name));
                    }
                    if (!TextUtils.isEmpty(pickUpOrder.getGet_car_name())) {
                        getCarName.setText(pickUpOrder.getGet_car_name());
                    } else {
                        getCarName.setText(getText(name));
                    }
                    if (!TextUtils.isEmpty(pickUpOrder.getGet_car_phone())) {
                        getCarPhone.setText(pickUpOrder.getGet_car_phone());
                    } else {
                        getCarPhone.setText(getText(phone));
                    }

                } else {
                    linkPhone.setText(getText(phone));
                    linkName.setText(getText(name));
                    getCarName.setText(getText(name));
                    getCarPhone.setText(getText(phone));
                }
                dialog_loding.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 2-85 本维修厂车牌号模糊查询 接口 carnofuzzyquery(userid,carno,dept_id)
     * 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
    private void carnofuzzyquery() {
        RequestParams params = creatParams("carnofuzzyquery");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("carno", TextUtils.isEmpty(getText(carno)) ? "" : getText(carno));
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_carInfo2.clear();
                list_carInfo2.addAll(data);

                dialog_query.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    /**
     * 2-89 通过二维码获取车牌号接口 getcarnobyqrcode(userid,qr_code)
     * 当前用户ID:userid,二维码：qr_code
     */
    private void getcarnobyqrcode(final String code) {
        RequestParams params = creatParams("getcarnobyqrcode");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("qr_code", code);

        dialog_loding.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {

                if (result.getRes().equals("1")) {
                    String carno = result.getData().get(0).getCarno();
                    ToastUtil.showShort(mContext, "本二维码已绑定过");
                } else {
                    ToastUtil.showShort(mContext, "本二维码未绑定过");
                    qr_code = code;
                }
                dialog_loding.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                dialog_loding.dismiss(mContext);
            }
        });
    }


}
