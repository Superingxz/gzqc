package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CarBrandActivity;
import com.xolo.gzqc.ui.dialog.SelectYearDialog;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.AMUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.zxing.camera.CameraActivityCapture;
import com.xolo.gzqc.zxing.encode.QRCodeEncoder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCarInfoActivity extends BaseActivity {

    @BindView(R.id.displacement)
    TextView displacement;
    @BindView(R.id.drive)
    TextView drive;
    @BindView(R.id.fuel)
    TextView fuel;
    @BindView(R.id.operation)
    TextView operation;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.brand)
    EditText brand;
    @BindView(R.id.models)
    EditText models;
    @BindView(R.id.year)
    TextView year;
    @BindView(R.id.vin_code)
    EditText vinCode;
    @BindView(R.id.engine)
    EditText engine;
    @BindView(R.id.login_data)
    TextView loginData;
    @BindView(R.id.btn_determine)
    Button btnDetermine;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;
    @BindView(R.id.iv_code)
    ImageView ivCode;
    @BindView(R.id.qr_code)
    TextView qrCode;
    @BindView(R.id.old_carno)
    TextView oldCarno;
    @BindView(R.id.tr_old)
    TableRow trOld;

    private Dialog dialog_date;
    private LoadDialog loadDialog;
    private Dialog dialog_years;

    private boolean isEdit;

    private String ownerid;

    /**
     * 排量弹出框
     */
    private Dialog dialog_displacement;

    /**
     * 驱动弹出框
     */
    private Dialog dialog_drive;

    /**
     * 燃料弹出框
     */
    private Dialog dialog_fuel;

    /**
     * 操作弹出框
     */
    private Dialog dialog_operation;

    private String carInfo_id;


    private List<Control> list_displacement = new ArrayList<>();
    private List<Control> list_drive = new ArrayList<>();
    private List<Control> list_fuel = new ArrayList<>();
    private List<Control> list_operation = new ArrayList<>();

    private Control control_displacement;
    private Control control_drive;
    private Control control_fuel;
    private Control control_operation;

    private LicenseKeyboardUtil keyboardUtil;

    private String qr_code;

    private Brand brand_rt;
    private TypeCode typeCode_rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_info);
        ButterKnife.bind(this);
        init();
        try {
            Bitmap bitmap = new QRCodeEncoder(mContext, "粤A23498", 50, true).encodeAsBitmap();
            ivCode.setImageBitmap(bitmap);
        } catch (WriterException e) {

        }
    }

    private void init() {
        brand.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                brand.setFocusable(true);
                brand.setFocusableInTouchMode(true);
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


        initIntent();

        keyboardUtil = new LicenseKeyboardUtil(mContext, carNunber, keyboardView);

        if (!TextUtils.isEmpty(carInfo_id)) {
            getcarinfobycarinfoid();
        }

    }

    private void initIntent() {
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("isEdit", false);
        ownerid = intent.getStringExtra(Key.CAR_OWNER_ID);
        carInfo_id = intent.getStringExtra(Key.CAR_INFO_ID);

        /**
         * 只有接车员可以修改车牌号
         */
        boolean pickcar = intent.getBooleanExtra("pickcar", false);

        /**
         * 车主资料点进来修改    显示曾用车牌
         */
        if (isEdit) {
            titleview.setTitle("修改车辆");
            btnDetermine.setText("修改");
            if (!pickcar) {
                carNunber.setEnabled(false);
            }
            //显示曾用车牌
            trOld.setVisibility(View.VISIBLE);
        }

        /**
         * 读取查询信息
         */
        CarInfo carInfo = (CarInfo) intent.getSerializableExtra(Key.OBJECT);

        if (carInfo != null) {
           //显示曾用车牌
            trOld.setVisibility(View.VISIBLE);
            titleview.setTitle("车辆信息");

            carInfo_id = carInfo.getBc_car_info_id();

            btnDetermine.setVisibility(View.GONE);
            btnScan.setVisibility(View.GONE);

            setEditable(new EditText[]{carNunber, models, vinCode, engine});
            setCompoundDrawables(new TextView[]{displacement, fuel, drive, loginData, operation, brand, year});
            setClickale(new TextView[]{displacement, fuel, drive, loginData, operation, brand, year});
            setHint(new TextView[]{displacement, fuel, drive, loginData, operation, carNunber, models, year, vinCode, engine});
        } else {
            initDialog();
        }
    }

    private void initDialog() {
        dialog_displacement = createControlDialog("排量", list_displacement, displacement);
        dialog_drive = createControlDialog("驱动", list_drive, drive);
        dialog_fuel = createControlDialog("燃料", list_fuel, fuel);
        dialog_operation = createControlDialog("操作", list_operation, operation);

        dialog_date = creatDateDialog(loginData);
        dialog_years = SelectYearDialog.creat(mContext, year);
    }

    @OnClick({R.id.displacement, R.id.drive, R.id.fuel, R.id.operation, R.id.btn_determine, R.id.login_data, R.id.btn_scan, R.id.brand, R.id.year})
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
            case R.id.login_data:
                dialog_date.show();
                break;
            case R.id.btn_determine:
                if (TextUtils.isEmpty(carNunber.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请输入车牌号");
                    return;
                }
                if (TextUtils.isEmpty(brand.getText().toString().trim())) {
                    ToastUtil.showShort(mContext, "请输入品牌");
                    return;
                }
                if (!AMUtils.validateCarNum(getText(carNunber))) {
                    ToastUtil.showShort(mContext, "车牌号格式不正确");
                    return;
                }
                if (isEdit) {
                    geteditcarinfo();
                    return;
                }
                getaddcarinfo();
                break;
            case R.id.btn_scan:
                startActivityForResult(new Intent(mContext, CameraActivityCapture.class), REQUEST_CODE);
                break;
            case R.id.brand:
                if (!brand.isFocusableInTouchMode()) {
                    startActivityForResult(new Intent(mContext, CarBrandActivity.class), CarBrandActivity.GET_BRAND);
                }
                break;
            case R.id.year:
                dialog_years.show();
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
     * 新增车辆接口
     * getaddcarinfo(userid,car_owner_id,carno,brands,brands,typecode,output_id,output,productyear,drive_id,drive_type,operation_id,operation_type,fuel_id,fuel,vincode,reg_date,enginecode,photo)
     * 当前用户ID:userid,车主ID:car_owner_id,车牌号:carno,车品牌名:brands,车型代码:typecode,排量ID:output_id,排量:output,车辆生产年份:productyear,驱动类型ID:drive_id,驱动类型:drive_type,操控方式ID:operation_id,操控方式:operation_type,燃料ID:fuel_id,燃料:fuel,VIN码:vincode,注册日期:reg_date,发动机号:enginecode,车辆图标（缩略图）,没有就留空:photo
     */
    private void getaddcarinfo() {
        RequestParams params = creatParams("getaddcarinfo");
        addBodyParam(params);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
//                Intent intent = new Intent();
//                intent.putExtra(Key.CARNO,carNunber.getText().toString().trim());
//                intent.putExtra(Key.TYPECODE,brand.getText().toString().trim());
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 修改车辆接口
     */
    private void geteditcarinfo() {
        RequestParams params = creatParams("geteditcarinfo");
        params.addBodyParameter(Key.CAR_INFO_ID, carInfo_id);
        addBodyParam(params);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, "修改车辆信息成功");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 通过车辆信息ID得到该车的车辆详细信息接口
     */
    private void getcarinfobycarinfoid() {
        RequestParams params = creatParams("getcarinfobycarinfoid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_INFO_ID, carInfo_id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                CarInfo carInfo = result.getData().get(0);
                carNunber.setText(carInfo.getCarno());
                if (!TextUtils.isEmpty(carInfo_id)) {
                    carNunber.setClickable(false);
                    carNunber.setFocusable(false);
                }
                brand.setText(carInfo.getBrands());
                models.setText(carInfo.getTypecode());
                year.setText(carInfo.getProductyear());
                vinCode.setText(carInfo.getVincode());
                engine.setText(carInfo.getEnginecode());
                loginData.setText(carInfo.getReg_date());
                qrCode.setText(carInfo.getQr_code());
                qr_code = carInfo.getQr_code();
                oldCarno.setText(carInfo.getOld_carno());

                displacement.setText(carInfo.getOutput());
                drive.setText(carInfo.getDrive_type());
                fuel.setText(carInfo.getFuel());
                operation.setText(carInfo.getOperation_type());

                control_displacement = new Control(carInfo.getOutput_id(), carInfo.getOutput());
                control_drive = new Control(carInfo.getDrive_id(), carInfo.getDrive_type());
                control_fuel = new Control(carInfo.getFuel_id(), carInfo.getFuel());
                control_operation = new Control(carInfo.getOperation_id(), carInfo.getOperation_type());

                if (!TextUtils.isEmpty(carInfo.getBrands_id())) {
                    brand_rt = new Brand(carInfo.getBrands(), carInfo.getBrands_id());
                    typeCode_rt = new TypeCode(carInfo.getTypecode(), carInfo.getTypecode_id());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    private void addBodyParam(RequestParams params) {
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_OWNER_ID, ownerid);
        params.addBodyParameter(Key.CARNO, carNunber.getText().toString().trim());
        params.addBodyParameter(Key.BRANDS, brand.getText().toString().trim());
        params.addBodyParameter(Key.TYPECODE, models.getText().toString().trim());
        params.addBodyParameter(Key.OUTPUT_ID, control_displacement == null ? "" : control_displacement.getControl_id());
        params.addBodyParameter(Key.OUTPUT, control_displacement == null ? "" : control_displacement.getDisplay_data());
        params.addBodyParameter(Key.PRODUCTYEAR, year.getText().toString().trim());
        params.addBodyParameter(Key.DRIVE_ID, control_drive == null ? "" : control_drive.getControl_id());
        params.addBodyParameter(Key.DRIVE_TYPE, control_drive == null ? "" : control_drive.getDisplay_data());
        params.addBodyParameter(Key.OPERATION_ID, control_operation == null ? "" : control_operation.getControl_id());
        params.addBodyParameter(Key.OPERATION_TYPE, control_operation == null ? "" : control_operation.getDisplay_data());
        params.addBodyParameter(Key.FUEL, control_fuel == null ? "" : control_fuel.getDisplay_data());
        params.addBodyParameter(Key.FUEL_ID, control_fuel == null ? "" : control_fuel.getControl_id());
        params.addBodyParameter(Key.VINCODE, vinCode.getText().toString().trim());
        params.addBodyParameter(Key.REG_DATE, loginData.getText().toString().trim());
        params.addBodyParameter(Key.ENGINECODE, engine.getText().toString().trim());
        params.addBodyParameter(Key.PHOTO, "");
        params.addBodyParameter("qr_code", getText(qrCode));
        params.addBodyParameter("brands_id", brand_rt == null ? "" : brand_rt.getBrands_id());
        params.addBodyParameter("typecode_id", typeCode_rt == null ? "" : typeCode_rt.getTypecode_id());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getcarnobyqrcode(data.getExtras().getString("result"));
        }
        if (requestCode == CarBrandActivity.GET_BRAND && resultCode == CarBrandActivity.RETURN_BRAND) {
            brand_rt = (Brand) data.getSerializableExtra(IntentConstant.BRAND);
            typeCode_rt = (TypeCode) data.getSerializableExtra(IntentConstant.TYPE_CODE);
            brand.setText(brand_rt.getBrands());
            models.setText(typeCode_rt.getTypecode());
        }
        super.onActivityResult(requestCode, resultCode, data);
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
     * 2-89 通过二维码获取车牌号接口 getcarnobyqrcode(userid,qr_code)
     * 当前用户ID:userid,二维码：qr_code
     */
    private void getcarnobyqrcode(final String code) {
        RequestParams params = creatParams("getcarnobyqrcode");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("qr_code", code);

        loadDialog.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {

                if (result.getRes().equals("1")) {
//                    carNunber.setText(result.getData().get(0).getCarno());
                    String carno = result.getData().get(0).getCarno();
                    ToastUtil.showShort(mContext, "改二维码已绑定过");
                } else {
                    if (!TextUtils.isEmpty(getText(qrCode))) {
                        new AlertDialog.Builder(mContext)
                                .setMessage("该车辆已经绑定了二维码，需要重新绑定吗？")
                                .setPositiveButton("取消", null)
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        qrCode.setText(code);
                                    }
                                }).show();
                    } else {
                        qrCode.setText(qr_code);
                    }
                }
                loadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


}
