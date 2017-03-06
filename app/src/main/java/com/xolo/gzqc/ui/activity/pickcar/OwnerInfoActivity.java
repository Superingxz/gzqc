package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.bean.child.Car;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarOwner;
import com.xolo.gzqc.bean.child.OwnerInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
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
 * 车主资料
 */
public class OwnerInfoActivity extends BaseActivity {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone_1)
    EditText phone1;
    @BindView(R.id.phone_2)
    EditText phone2;
    @BindView(R.id.province)
    TextView province;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.county)
    TextView county;
    @BindView(R.id.street)
    EditText street;
    @BindView(R.id.note)
    EditText note;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rb_boy)
    RadioButton rbBoy;
    @BindView(R.id.rb_gird)
    RadioButton rbGird;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.drivingno)
    EditText drivingno;
    @BindView(R.id.lv_car)
    ListView lvCar;
    @BindView(R.id.link_name)
    EditText linkName;
    @BindView(R.id.link_phone)
    EditText linkPhone;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.requery)
    Button requery;


    private List<Area> list_province;
    private List<Area> list_city;
    private List<Area> list_county;

    private Dialog dialog_province;
    private Dialog dialog_city;
    private Dialog dialog_county;
    private Dialog dialog_query;

    private String owner_id;

    private List<Car> list_car;
    private CommenAdapter<Car> carAdapter;

    private boolean is_Read;

    private String provinceid;
    private String cityid;
    private String areaid;

    private LoadDialog loadDialog;

    private List<CarInfo> list_carInfo2 = new ArrayList<>();

    private boolean is_addDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_info);
        ButterKnife.bind(this);
        initView();
        initDialog();
        initList();
    }


    private void initView() {
        Intent intent = getIntent();
        is_Read = intent.getBooleanExtra(Key.IS_READ, false);
        owner_id = intent.getStringExtra(Key.CAR_OWNER_ID);
        String phone3 = intent.getStringExtra("phone");

        /**
         * 只读数据
         */
        if (is_Read) {
//            getcarownerinfo();
            getOwnerInfo(phone3);
            btnSave.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            requery.setVisibility(View.GONE);
            setEditable(new EditText[]{name,phone1,phone2,drivingno,street,linkName,linkPhone,note});
            setClickale(new TextView[]{province,city,county,rbBoy,rbGird});
            setCompoundDrawables(new TextView[]{province,city,county});
            setHint(new TextView[]{name,phone1,phone2,drivingno,street,linkName,linkPhone,note,province,city,county});
        } else {
            rbBoy.setChecked(true);
        }

        phone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() == 11 && before == 0){
                            getOwnerInfo(s.toString());
                        }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void initList() {
        list_car = new ArrayList<>();
        carAdapter = new CommenAdapter<>(R.layout.item_owner_car, mContext, list_car, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Car car = list_car.get(position);
                holder.setText(R.id.item1, car.getCarno());
                holder.setText(R.id.item2, car.getBrands());
                holder.setText(R.id.item3, car.getQr_code());
            }
        });
        lvCar.setAdapter(carAdapter);

        lvCar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final Car car = list_car.get(position);
                String carno = car.getCarno();
                new AlertDialog.Builder(mContext).setMessage("是否删除" + carno).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delcarinfo(car.getBc_car_info_id(), position);
                    }
                }).show();
                return true;
            }
        });

        lvCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, AddCarInfoActivity.class);
                if (is_Read){
                    CarInfo carInfo = new CarInfo();
                    carInfo.setBc_car_info_id(list_car.get(position).getBc_car_info_id());
                    intent.putExtra(Key.OBJECT,carInfo);
                }
                intent.putExtra("isEdit", true);
                intent.putExtra("pickcar", true);
                intent.putExtra(Key.CAR_OWNER_ID, owner_id);
                intent.putExtra(Key.CAR_INFO_ID, list_car.get(position).getBc_car_info_id());
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void initDialog() {
        list_city = new ArrayList<>();
        list_county = new ArrayList<>();
        list_province = new ArrayList<>();

        dialog_province = createAreaDialog("省", list_province, province);
        dialog_city = createAreaDialog("市", list_city, city);
        dialog_county = createAreaDialog("县区", list_county, county);

        dialog_query = creatListDialog("车牌号", list_carInfo2, name, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getName();
            }

            @Override
            public void onClick(CarInfo carInfo) {
                phone1.setText(carInfo.getPhone());
                clear();
                getOwnerInfo(carInfo.getPhone());
            }
        });
    }


    /**
     * 2-84 输入姓名时，模糊查询，自动弹出对应的姓名+手机号1 接口 getlistcarownername(userid,car_owner_name)
     * 当前用户ID:userid,车主姓名：car_owner_name
     */
    private void getlistcarownername() {
        RequestParams params = creatParams("getlistcarownername");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("car_owner_name", TextUtils.isEmpty(getText(name))?"":getText(name));

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

    @OnClick({R.id.province, R.id.city, R.id.county, R.id.btn_add, R.id.btn_save, R.id.requery})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.province:
                if (list_province.size() > 0) {
                    dialog_province.show();
                    return;
                }
                getArea(list_province, dialog_province, "");
                break;
            case R.id.city:
                if (TextUtils.isEmpty(province.getText().toString().trim())) {
                    ToastUtil.showLong(mContext, "请先选择省");
                    return;
                }
                getArea(list_city, dialog_city, provinceid);
                break;
            case R.id.county:
                if (TextUtils.isEmpty(city.getText().toString().trim())) {
                    ToastUtil.showLong(mContext, "请先选择市");
                    return;
                }
                getArea(list_county, dialog_county, cityid);
                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(owner_id)) {
                    ToastUtil.showShort(mContext, "请先注册再添加车辆");
                    return;
                }
                Intent intent = new Intent(mContext, AddCarInfoActivity.class);
                intent.putExtra(Key.CAR_OWNER_ID, owner_id);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_save:

                if (is_addDept) {

                    addfactory();

                } else {
                    if (TextUtils.isEmpty(name.getText().toString().trim())) {
                        ToastUtil.showShort(mContext, "请输入姓名");
                        return;
                    }
                    if (TextUtils.isEmpty(phone1.getText().toString().trim())) {
                        ToastUtil.showShort(mContext, "请输入手机号1");
                        return;
                    }
                    if (TextUtils.isEmpty(province.getText().toString().trim())) {
                        ToastUtil.showShort(mContext, "请选择省");
                        return;
                    }
                    if (TextUtils.isEmpty(city.getText().toString().trim())) {
                        ToastUtil.showShort(mContext, "请选择市");
                        return;
                    }
                    if (TextUtils.isEmpty(county.getText().toString().trim())) {
                        ToastUtil.showShort(mContext, "请选择县");
                        return;
                    }

                    login();
                }
                break;
            case R.id.requery:
                getlistcarownername();
                break;
        }
    }


    /**
     * 2-18 接车员注册接口 getregcarowner(userid,name,sex,phone,phone2,drivingno,province,city,area,street,photo,remark,dept_id,dept_name,
     * provinceid,cityid,areaid) 当前用户ID:userid,车主姓名:name,车主性别:sex,手机号1:phone,手机号2:phone2,行驶证号:drivingno,省:province,市:city,
     * 区县:area,街道:street,车主头像:photo,备注:remark,维修厂ID:dept_id,维修厂名称:dept_name,省id:provinceid,市id:cityid,区县id:areaid
     * 联系人姓名:link_name,联系人电话:link_phone
     */
    private void login() {

        if (TextUtils.isEmpty(name.getText().toString())) {
            ToastUtil.showShort(mContext, "请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(phone1.getText().toString())) {
            ToastUtil.showShort(mContext, "请输入手机号码");
            return;
        }

        RequestParams params = creatParams("getregcarowner");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.NAME, name.getText().toString().trim());
        params.addBodyParameter(Key.SEX, rbBoy.isChecked()?"1":"0");
        params.addBodyParameter(Key.PHONE, phone1.getText().toString().trim());
        params.addBodyParameter(Key.PHONE_2, phone2.getText().toString().trim());
        params.addBodyParameter(Key.DRIVINGNO, drivingno.getText().toString().trim());
        params.addBodyParameter(Key.PROVINCE, province.getText().toString().trim());
        params.addBodyParameter(Key.CITY, city.getText().toString().trim());
        params.addBodyParameter(Key.AREA, county.getText().toString().trim());
        params.addBodyParameter(Key.STREET, street.getText().toString().trim());
        params.addBodyParameter(Key.PHOTO, "");
        params.addBodyParameter(Key.REMARK, note.getText().toString().trim());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.DEPT_NAME, user.getDept_name());
        params.addBodyParameter("provinceid", provinceid);
        params.addBodyParameter("cityid", cityid);
        params.addBodyParameter("areaid", areaid);
//        联系人姓名:link_name,联系人电话:link_phone
        params.addBodyParameter("link_name", getText(linkName));
        params.addBodyParameter("link_phone", getText(linkPhone));


        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                getOwnerInfo(phone1.getText().toString().trim());
                ToastUtil.showShort(mContext, result.getMsg());

                rl.setVisibility(View.VISIBLE);
                ll.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });

    }


    /**
     * 接口获取省，市，县的信息
     *
     * @param list
     * @param dialog
     */
    private void getArea(final List list, final Dialog dialog, String id) {
        RequestParams params = creatParams("getregionbydistid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("distid", id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.AreaBean.class, new HttpUtil.HttpCallBack<ORMBean.AreaBean>() {
            @Override
            public void onSuccess(ORMBean.AreaBean result) {
                List<Area> data = result.getData();

                list.clear();
                list.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    private Dialog createAreaDialog(String title, List<Area> list, final TextView targer) {
        return creatListDialog(title, list, targer, new ListDialogCallBack<Area>() {
            @Override
            public String setText(Area area) {
                return area.getDistname();
            }

            @Override
            public void onClick(Area area) {

                if (targer == province) {
                    city.setText("");
                    county.setText("");
                    provinceid = area.getDistid();
                    cityid = null;
                } else if (targer == city) {
                    county.setText("");
                    cityid = area.getDistid();
                } else {
                    areaid = area.getDistid();
                }
            }
        });
    }


    /**
     * 获取车主信息
     *
     * @param phone
     */
    private void getOwnerInfo(final String phone) {
        RequestParams params = creatParams("getcarownerinfobyphone");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.PHONE, phone);

        loadDialog.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.CarOwnerBean.class, new HttpUtil.HttpCallBack<ORMBean.CarOwnerBean>() {
            @Override
            public void onSuccess(ORMBean.CarOwnerBean result) {
                if (result.getRes().equals("1")) {
                    CarOwner carOwner = result.getData().get(0);
                    String id = carOwner.getBc_car_owner_id();
                    owner_id = id;
                    name.setText(carOwner.getName());
                    phone1.setText(carOwner.getPhone());
                    phone2.setText(carOwner.getPhone2());
                    drivingno.setText(carOwner.getDrivingno());
                    province.setText(carOwner.getProvince());
                    city.setText(carOwner.getCity());
                    county.setText(carOwner.getArea());
                    street.setText(carOwner.getStreet());
                    note.setText(carOwner.getRemark());

                    linkName.setText(carOwner.getLink_name());
                    linkPhone.setText(carOwner.getLink_phone());

                    provinceid = carOwner.getProvinceid();
                    cityid = carOwner.getCityid();
                    areaid = carOwner.getAreaid();

                    if (carOwner.getSex().equals("0")) {
                        rbGird.setChecked(true);
                    } else
                        rbBoy.setChecked(true);

                    ll.setVisibility(View.VISIBLE);
                    rl.setVisibility(View.VISIBLE);

//                    btnSave.setText("增加维修厂");
                    is_addDept = true;

                    getlistcarbycarownerid();
                } else {
                    loadDialog.dismiss(mContext);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


    /**
     * 获取车辆列表
     */
    private void getlistcarbycarownerid() {
        RequestParams params = creatParams("getlistcarbycarownerid");
        params.addBodyParameter(Key.CAR_OWNER_ID, owner_id);
        params.addBodyParameter(Key.USER_ID, user.getUser_id());

        HttpUtil.getInstance().post(params, ORMBean.CarBean.class, new HttpUtil.HttpCallBack<ORMBean.CarBean>() {
            @Override
            public void onSuccess(ORMBean.CarBean result) {
                if (result.getRes().equals("1")) {
                    List<Car> data = result.getData();
                    if (list_car.size() > 0) {
                        list_car.clear();
                    }
                    list_car.addAll(data);
                    carAdapter.notifyDataSetChanged();
                }
                loadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            getlistcarbycarownerid();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 删除车辆接口 delcarinfo(userid,car_info_id) 当前用户ID:userid,车辆信息ID:car_info_id
     */
    private void delcarinfo(String id, final int position) {
        RequestParams params = creatParams("delcarinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_INFO_ID, id);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                list_car.remove(position);
                carAdapter.notifyDataSetChanged();
                ToastUtil.showShort(mContext, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /**
     * 2-13 查看客户信息接口 getcarownerinfo(userid,car_owner_id) 当前用户ID:userid,车主ID:car_owner_id
     */
    private void getcarownerinfo() {
        RequestParams params = creatParams("getcarownerinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CAR_OWNER_ID, owner_id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.OwnerInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.OwnerInfoBean>() {
            @Override
            public void onSuccess(ORMBean.OwnerInfoBean result) {
                OwnerInfo ownerInfo = result.getData().get(0);

                name.setText(ownerInfo.getName());
                phone1.setText(ownerInfo.getPhone());
                phone2.setText(ownerInfo.getPhone2());
                if (ownerInfo.getSex().equals("0")) {
                    rbBoy.setChecked(true);
                } else {
                    rbGird.setChecked(true);
                }
                drivingno.setText(ownerInfo.getDrivingno());
                province.setText(ownerInfo.getProvince());
                city.setText(ownerInfo.getCity());
                county.setText(ownerInfo.getArea());
                street.setText(ownerInfo.getStreet());
                note.setText(ownerInfo.getRemark());

                getOwnerInfo(ownerInfo.getPhone());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 2-75 接车员增加维修厂接口 addfactory(userid,dept_id,dept_name,car_owner_id)
     * 当前用户ID:userid,维修厂ID:dept_id,维修厂名称:dept_name,车主ID:car_owner_id
     */
    private void addfactory() {
        RequestParams params = creatParams("addfactory");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("dept_name", user.getDept_name());
        params.addBodyParameter("car_owner_id", owner_id);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext, result.getMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @Override
    public void clear() {
        super.clear();
        phone2.setText("");
        drivingno.setText("");
        province.setText("");
        city.setText("");
        county.setText("");
        street.setText("");
        linkName.setText("");
        linkPhone.setText("");
        note.setText("");

        rl.setVisibility(View.GONE);
        ll.setVisibility(View.GONE);

        btnSave.setText("保存");
    }
}
