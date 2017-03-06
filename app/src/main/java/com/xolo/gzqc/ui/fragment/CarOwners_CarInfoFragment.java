package com.xolo.gzqc.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_CarInfoAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.pickcar.AddCarInfoActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 车主车辆信息
 * Created by Administrator on 2016/9/24.
 */
public class CarOwners_CarInfoFragment extends LazyFragment {
    List<CarInfo> list;
    ListView owner_car_info_lv;
    CarOwner_CarInfo carOwners_carInfoAdapter;
    View view;
    EditText maintain_et;
    Button add_car;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_ownercarinfo, null);
            initView();
        }
        return  view;
    }
    CarOwner_CarInfoAdapter carOwner_carInfoAdapter;
    private void initView() {
        //initDialog();
        add_car=(Button)view.findViewById(R.id.add_car);
        list=new ArrayList<>();
        owner_car_info_lv=(ListView)view.findViewById(R.id.owner_car_info_lv);
        carOwner_carInfoAdapter=new CarOwner_CarInfoAdapter(getContext()
                ,R.layout.item_ownercarinfo,list);
        owner_car_info_lv.setAdapter(carOwner_carInfoAdapter);
        owner_car_info_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                carInfo=list.get(position);
                Intent  intent=new  Intent(mContext,AddCarInfoActivity.class);
                intent.putExtra("isEdit",true);
                intent.putExtra(Key.CAR_INFO_ID,list.get(position).getBc_car_info_id());
                intent.putExtra(Key.CAR_OWNER_ID,user.getUser_id());
                startActivityForResult(intent,111);
                //dialog.show();
            }
        });

        add_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),AddCarInfoActivity.class);
                intent.putExtra(Key.CAR_OWNER_ID,user.getUser_id());
                startActivityForResult(intent,111);
            }
        });
    }
    CarInfo carInfo;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK&&requestCode==111){
            getData();
        }
    }
   // private Dialog dialog;
    private void initDialog() {

//        final String[] strings = {"修改车辆信息"};
//       // final String[] strings = {"接车单信息","修改车辆信息"};
//        final View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_list, null);
//        dialog = new AlertDialog.Builder(mContext).setView(inflate)
//                .create();
//        ListView lv = (ListView) inflate.findViewById(R.id.lv_dilog);
//
//        lv.setAdapter(new CommenAdapter<String>(R.layout.item_dialog_list, mContext, Arrays.asList(strings), new CommenAdapter.AdapterCallback() {
//            @Override
//            public void setView(ViewHolder holder, int position) {
//                holder.setText(R.id.item2, strings[position]);
//            }
//        }));

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                switch (position){
//                    case 0:
//                        intent = new Intent(mContext,ReadPickUpActivity.class);
//                        intent.putExtra(Key.OBJECT,carInfo);
//                        startActivity(intent);
//                        break;
//                    case 0:

//                        break;

//                }
 //               dialog.dismiss();
 //           }
 //       });
    }
    void  getData(){
        LoadDialog.show(getContext());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getownercarinfo");
        requestParams.addBodyParameter("bc_car_owner_id",user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                if(result.getRes().equals("1")){
                    list.clear();
                    list.addAll(result.getData());
                    carOwner_carInfoAdapter.notifyDataSetChanged();
                }else{
                    ToastUtil.showLong(getContext(),result.getMsg());
                }

                LoadDialog.dismiss(mContext);
            }
        });

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    protected void loadData() {
        if(needLoad()){
            getData();
            mHasLoadedOnce=true;
        }
    }

    @Override
    protected void init() {

    }
}
