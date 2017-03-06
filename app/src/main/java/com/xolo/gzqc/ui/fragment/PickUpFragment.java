package com.xolo.gzqc.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.ReceiveannexBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarState;
import com.xolo.gzqc.bean.child.PickUpOrder;
import com.xolo.gzqc.bean.child.Receiveannex;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.AddCarStateActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollListView;
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
 * A simple {@link Fragment} subclass.
 */
public class PickUpFragment extends BaseFragment {



    @BindView(R.id.carno)
    EditText carno;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.name_send)
    TextView nameSend;
    @BindView(R.id.phone_send)
    TextView phoneSend;
    @BindView(R.id.brand)
    TextView brand;
    @BindView(R.id.mileage)
    EditText mileage;
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
    EditText description;
    @BindView(R.id.is_rework)
    CheckBox isRework;
    @BindView(R.id.is_check)
    CheckBox isCheck;
    @BindView(R.id.confirm)
    Button confirm;

    private CommenAdapter<CarState> adapter;

    private List<CarState> list_carstate = new ArrayList<>();
    private ArrayList<Receiveannex> list_receiveannex = new ArrayList<>();

    private CarInfo  carInfo;

    private LoadDialog  loadDialog;

    public PickUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_up, container, false);
        ButterKnife.bind(this, view);
        carInfo = (CarInfo) getArguments().getSerializable(Key.OBJECT);
        return view;
    }

    @Override
    protected void init() {
          initLv();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_car_state, mContext, list_carstate, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarState carState = list_carstate.get(position);
                holder.setText(R.id.item1, carState.getType_name());
                holder.setText(R.id.item2, carState.getDescription());
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
                Intent intent = new Intent(mContext, AddCarStateActivity.class);
                intent.putExtra(Key.OBJECT, list_carstate.get(position));
                startActivity(intent);
            }
        });

    }


    @OnClick({R.id.btn_part, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_part:

                break;
            case R.id.confirm:

                break;
        }
    }

    /**
     * 2-29 通过车牌号获取本维修厂最新的接车信息接口 getnewreceiveinfo(userid,carno,dept_id) 当前用户ID:userid,
     * 车牌号:carno,维修厂ID:dept_id
     */
       private  void  getnewreceiveinfo(){
           RequestParams params = creatParams("getnewreceiveinfo");
           params.addBodyParameter(Key.USER_ID,user.getUser_id());
           params.addBodyParameter(Key.DEPT_ID,user.getDept_id());
           params.addBodyParameter("carno",carInfo.getCarno());

           loadDialog.show(mContent);

           HttpUtil.getInstance().post(params, ORMBean.PickUpOrderBean.class, new HttpUtil.HttpCallBack<ORMBean.PickUpOrderBean>() {
               @Override
               public void onSuccess(ORMBean.PickUpOrderBean result) {
                   if (result.getRes().equals("1")){
                       PickUpOrder pickUpOrder = result.getData().get(0);

//                       填写数据
                       carno.setText(pickUpOrder.getCarno());
                       name.setText(pickUpOrder.getName());
                       phone.setText(pickUpOrder.getMobile());
                       phoneSend.setText(pickUpOrder.getSendmanphone());
//                       brand.setText(pickUpOrder.get);
                       mileage.setText(pickUpOrder.getMileage());
                       oil.setText(pickUpOrder.getOil());
                       inTime.setText(pickUpOrder.getIn_time());
                       bookTime.setText(pickUpOrder.getBook_time());
                       description.setText(pickUpOrder.getDescription());

                       if (pickUpOrder.getIs_rework().equals("1")){
                           isRework.setChecked(true);
                       }
                       if (pickUpOrder.getIs_check().equals("1")){
                           isCheck.setChecked(true);
                       }

//                       2-30 通过接车单ID获取车况列表记录接口
                      getcarstatusinfo();

                   }else {
                       ToastUtil.showShort(mContent,result.getMsg());
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
     *    2-30 通过接车单ID获取车况列表记录接口 getcarstatusinfo(userid,receive_id) 当前用户ID:userid,接车单ID:receive_id
     */
     private  void  getcarstatusinfo(){
         RequestParams params = creatParams("getcarstatusinfo");
         params.addBodyParameter(Key.USER_ID,user.getUser_id());
         params.addBodyParameter("receive_id",carInfo.getBf_receive_id());

         HttpUtil.getInstance().post(params, ORMBean.CarStateBean.class, new HttpUtil.HttpCallBack<ORMBean.CarStateBean>() {
             @Override
             public void onSuccess(ORMBean.CarStateBean result) {
                 if (result.getRes().equals("1")){
                     List<CarState> data = result.getData();
                     list_carstate.clear();
                     adapter.notifyDataSetChanged();


                 }else {
                     ToastUtil.showShort(mContent,result.getMsg());
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
     * 2-25 获取随车附件列表接口 listreceiveannex(userid,receive_id) 当前用户ID:userid,接车表ID：receive_id
     */
    private  void  listreceiveannex(){
        RequestParams params = creatParams("listreceiveannex");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("receive_id",carInfo.getBf_receive_id());

        HttpUtil.getInstance().post(params, ReceiveannexBean.class, new HttpUtil.HttpCallBack<ReceiveannexBean>() {
            @Override
            public void onSuccess(ReceiveannexBean result) {
                if (result.getRes().equals("1")){
                   list_receiveannex = (ArrayList<Receiveannex>) result.getData();
                }else {
                    ToastUtil.showShort(mContent,result.getMsg());
                }
                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContent);
            }
        });
    }


    @Override
    public void load() {
        super.load();
        getnewreceiveinfo();
    }
}
