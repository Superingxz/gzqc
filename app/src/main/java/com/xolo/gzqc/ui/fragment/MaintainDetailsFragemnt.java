package com.xolo.gzqc.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOwner_add_MaintainAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.CarOwner_Add_Maintain;
import com.xolo.gzqc.bean.child.CarOwner_CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆详细信息
 * Created by Administrator on 2016/9/28.
 */
public class MaintainDetailsFragemnt  extends  LazyFragment{
    List<CarOwner_Add_Maintain> list;
    ListView add_insurance_lv;
    CarOwner_add_MaintainAdapter carOwnersadapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_aintain_details,null);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        add_insurance_lv=(ListView) view.findViewById(R.id.add_insurance_lv);

        list=new ArrayList<>();
        list.add(new CarOwner_Add_Maintain("车 牌 号:",true));
        list.add(new CarOwner_Add_Maintain("品牌：",false));
        list.add(new CarOwner_Add_Maintain("排量:",false));
        list.add(new CarOwner_Add_Maintain("车型:",false));
        list.add(new CarOwner_Add_Maintain("年份:",false));
        list.add(new CarOwner_Add_Maintain("驱动:",false));
        list.add(new CarOwner_Add_Maintain("燃料:",false));
        list.add(new CarOwner_Add_Maintain("操控:",false));
        list.add(new CarOwner_Add_Maintain("VIN码:",false));
        list.add(new CarOwner_Add_Maintain("发动机号:",false));
        list.add(new CarOwner_Add_Maintain("注册日期:",false));

        carOwnersadapter=new CarOwner_add_MaintainAdapter(getActivity(),R.layout.fragment_add_maintain_tem,list);
        add_insurance_lv.setAdapter(carOwnersadapter);
        carOwnersadapter.setImaOnlickiface(new CarOwner_add_MaintainAdapter.ImaOnlickIface() {
            @Override
            public void rightImgOnlic(final int position) {
                if(position==0){
                    if (dialog == null) {
                        dialog = creatListDialog("选择车牌", liststring, new listDialogFace() {
                            @Override
                            public void retrunTest(String text) {
                                list.get(position).setEttext(text);
                                carOwnersadapter.notifyDataSetChanged();
                                getcarinfobycarno(text);
                            }
                        });

                    }
                    dialog.show();
                }
            }

            @Override
            public void changeEdit(int position, String st) {

            }
        });
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
    Dialog datedialog;
    Dialog dialog;
    List<String> liststring = new ArrayList<String>();
    List<CarOwner_CarInfo> carlist;
    void getData() {
        LoadDialog.show(getContext());
        user = SPManager.getUser(mContext);
        RequestParams requestParams = creatParams("getownercarinfo");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CoCarInfo.class, new HttpUtil.HttpCallBack<ORMBean.CoCarInfo>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);

            }

            @Override
            public void onSuccess(ORMBean.CoCarInfo result) {
                if (result.getRes().equals("1")) {
                    carlist = result.getData();
                    for (CarOwner_CarInfo code : carlist) {
                        liststring.add(code.getCarno());
                    }

                } else {
                    ToastUtil.showLong(getContext(), result.getMsg());
                    mHasLoadedOnce = false;
                }

                LoadDialog.dismiss(mContext);
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

        LoadDialog.show(mContext);

        HttpUtil.getInstance().post( params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                if (result.getRes().equals("1")){
                    list.get(0).setEttext(result.getData().get(0).getCarno());
                    list.get(1).setEttext(result.getData().get(0).getBrands());  //品牌
                    list.get(2).setEttext(result.getData().get(0).getTypecode());//型号
                    list.get(3).setEttext(result.getData().get(0).getOutput());//排量
                    list.get(4).setEttext(result.getData().get(0).getProductyear());//年份
                    list.get(5).setEttext(result.getData().get(0).getDrive_type());//驱动
                    list.get(6).setEttext(result.getData().get(0).getFuel());//燃料
                    list.get(7).setEttext(result.getData().get(0).getOperation_type());//手动
                    list.get(8).setEttext(result.getData().get(0).getVincode());//    vincode
                    list.get(9).setEttext(result.getData().get(0).getEnginecode());//   //发动机号
                    list.get(10).setEttext(result.getData().get(0).getReg_date());////注册日期
                }
                else{
                    for(CarOwner_Add_Maintain carOwner_add_maintain:list){
                        carOwner_add_maintain.setEttext("");
                    }
                    ToastUtil.showShort(mContext,result.getMsg());

                }
                carOwnersadapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
        });
    }
}
