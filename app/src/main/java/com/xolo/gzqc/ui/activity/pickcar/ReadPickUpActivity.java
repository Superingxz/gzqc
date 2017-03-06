package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.ReceiveannexBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarState;
import com.xolo.gzqc.bean.child.CarStateRead;
import com.xolo.gzqc.bean.child.PickUpOrder;
import com.xolo.gzqc.bean.child.Receiveannex;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.PhotoActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 读取接车信息
 */
public class ReadPickUpActivity extends BaseActivity {


    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.link_name)
    TextView linkName;
    @BindView(R.id.link_phone)
    TextView linkPhone;
    @BindView(R.id.name_send)
    TextView nameSend;
    @BindView(R.id.phone_send)
    TextView phoneSend;
    @BindView(R.id.get_car_name)
    TextView getCarName;
    @BindView(R.id.get_car_phone)
    TextView getCarPhone;
    @BindView(R.id.brand)
    TextView brand;
    @BindView(R.id.mileage)
    TextView mileage;
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
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.is_rework)
    CheckBox isRework;
    @BindView(R.id.is_check)
    CheckBox isCheck;
    @BindView(R.id.confirm)
    Button confirm;

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.typecode)
    TextView typecode;
    @BindView(R.id.is_save_des)
    CheckBox isSaveDes;
    @BindView(R.id.receive_name)
    TextView receiveName;

//    @BindView(R.id.is_print)
//    CheckBox is_print;

    private CommenAdapter<CarStateRead> adapter;

    private List<CarStateRead> list_carstate = new ArrayList<>();
    private ArrayList<Receiveannex> list_receiveannex = new ArrayList<>();

    private CarInfo carInfo;

    private LoadDialog loadDialog;
    private Dialog dialog_select;

    private int type;

    PickUpOrder pickUpOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_pick_up);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))) {
//            is_print.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
        }
        carInfo = (CarInfo) getIntent().getSerializableExtra(Key.OBJECT);
        type = getIntent().getIntExtra("type", 0);


        /**
         * 工作台进来,不打印，弹窗
         */
        if (type == 1) {
            confirm.setText("下一步");
        }

        init();
    }

    protected void init() {
        initLv();
        initDialog();
        getnewreceiveinfo();
    }

    private void initDialog() {
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
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_car_state, mContext, list_carstate, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarStateRead carState = list_carstate.get(position);
                List<CarStateRead.PhotosBean> photos = carState.getPhotos();

                holder.setText(R.id.item1, carState.getType_name());
                holder.setText(R.id.item2, carState.getDescription());

                ImageView i = holder.getView(R.id.item3);
                Glide.with(mContext).load(photos.get(0).getPhoto()).into(i);
            }
        });
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddCarStateActivity.class);
                intent.putExtra(Key.OBJECT, list_carstate.get(position));
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }


    @OnClick({R.id.btn_part, R.id.confirm, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_part:
                Intent intent = new Intent(mContext, ReceiveannexActivity.class);
                if (list_receiveannex.size() != 0) {
                    intent.putParcelableArrayListExtra(Key.OBJECT, list_receiveannex);
                }
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.confirm:
                if (type == 1) {
                    dialog_select.show();
                } else
                    setprintmark();
                break;
            case R.id.iv:
                if (!TextUtils.isEmpty(pickUpOrder.getCar_photo())) {
                    Intent intent1 = new Intent(mContext, PhotoActivity.class);
                    intent1.putExtra("url", pickUpOrder.getCar_photo());
                    startActivity(intent1);
                }
                break;
        }
    }

    /**
     * 2-95 通过接车单ID获取的接车信息接口 getreceiveinfobyid(userid,bf_receive_id)
     * 当前用户ID:userid,接车单ID：bf_receive_id
     */
    private void getnewreceiveinfo() {
        RequestParams params = creatParams("getreceiveinfobyid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("bf_receive_id", carInfo.getBf_receive_id());

        loadDialog.show(mContext);

        HttpUtil.getInstance().post(params, ORMBean.PickUpOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PickUpOrderBean>() {
            @Override
            public void onSuccess(ORMBean.PickUpOrderBean result) {
                if (result.getRes().equals("1")) {
                    pickUpOrder = result.getData().get(0);

//                  填写数据
                    carno.setText(pickUpOrder.getCarno());
                    name.setText(pickUpOrder.getName());
                    phone.setText(pickUpOrder.getMobile());
                    phoneSend.setText(pickUpOrder.getSendmanphone());
                    brand.setText(pickUpOrder.getBrands());
                    mileage.setText(pickUpOrder.getMileage());
                    oil.setText(pickUpOrder.getOil());
                    inTime.setText(pickUpOrder.getIn_time());
                    bookTime.setText(pickUpOrder.getBook_time());
                    description.setText(pickUpOrder.getDescription());
                    linkPhone.setText(pickUpOrder.getLink_phone());
                    linkName.setText(pickUpOrder.getLink_name());
                    getCarName.setText(pickUpOrder.getGet_car_name());
                    getCarPhone.setText(pickUpOrder.getGet_car_phone());
                    nameSend.setText(pickUpOrder.getSendmanname());
                    typecode.setText(pickUpOrder.getTypecode());
                    receiveName.setText(pickUpOrder.getOperatoname());

                    Glide.with(mContext).load(pickUpOrder.getCar_photo()).into(iv);

                    if (pickUpOrder.getIs_rework().equals("1")) {
                        isRework.setChecked(true);
                    }
                    if (pickUpOrder.getIs_check().equals("1")) {
                        isCheck.setChecked(true);
                    }

//                       2-30 通过接车单ID获取车况列表记录接口
                    getcarstatusinfo();

                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
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
     * 2-30 通过接车单ID获取车况列表记录接口 getcarstatusinfo(userid,receive_id) 当前用户ID:userid,接车单ID:receive_id
     */
    private void getcarstatusinfo() {
        RequestParams params = creatParams("getcarstatusinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receive_id", carInfo.getBf_receive_id());

        HttpUtil.getInstance().post(params, ORMBean.CarStateReadBean.class, new HttpUtil.HttpCallBack<ORMBean.CarStateReadBean>() {
            @Override
            public void onSuccess(ORMBean.CarStateReadBean result) {
                if (result.getRes().equals("1")) {
                    List<CarStateRead> data = result.getData();

                    list_carstate.clear();
                    list_carstate.addAll(data);
                    adapter.notifyDataSetChanged();

                } else {
                    ToastUtil.showShort(mContext, "车况记录为空");
                    loadDialog.dismiss(mContext);
                }
                listreceiveannex();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }


    /**
     * 2-25 获取随车附件列表接口 listreceiveannex(userid,receive_id) 当前用户ID:userid,接车表ID：receive_id
     */
    private void listreceiveannex() {
        RequestParams params = creatParams("listreceiveannex");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receive_id", carInfo.getBf_receive_id());

        HttpUtil.getInstance().post(params, ReceiveannexBean.class, new HttpUtil.HttpCallBack<ReceiveannexBean>() {
            @Override
            public void onSuccess(ReceiveannexBean result) {
                if (result.getRes().equals("1")) {
                    list_receiveannex = (ArrayList<Receiveannex>) result.getData();
                } else {
                    ToastUtil.showShort(mContext, "随车附件为空");
                }
                loadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
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
        params.addBodyParameter("page_id", carInfo.getBf_receive_id());
        params.addBodyParameter("type", "1");

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

}
