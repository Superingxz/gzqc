package com.xolo.gzqc.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.ArrivalGoodsActivity;
import com.xolo.gzqc.ui.activity.pickcar.CarInfoActivity;
import com.xolo.gzqc.ui.activity.pickcar.CompleteActivity;
import com.xolo.gzqc.ui.activity.pickcar.DealAppointmentActivity;
import com.xolo.gzqc.ui.activity.pickcar.DispatchingActivity;
import com.xolo.gzqc.ui.activity.pickcar.InquiryActivity;
import com.xolo.gzqc.ui.activity.pickcar.NewsActivity;
import com.xolo.gzqc.ui.activity.pickcar.OfferActivity;
import com.xolo.gzqc.ui.activity.pickcar.OrderPurchaseActivity;
import com.xolo.gzqc.ui.activity.pickcar.OwnerInfoActivity;
import com.xolo.gzqc.ui.activity.pickcar.PaymentActivity;
import com.xolo.gzqc.ui.activity.pickcar.PickUpCarActivity;
import com.xolo.gzqc.ui.activity.pickcar.ProgressActivity;
import com.xolo.gzqc.ui.activity.pickcar.PurchaseReservationActivity;
import com.xolo.gzqc.ui.activity.pickcar.ReturnCarActivity;
import com.xolo.gzqc.ui.activity.pickcar.TeamActivity;
import com.xolo.gzqc.ui.activity.pickcar.VerhaulActivity;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DensityUtils;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;
import com.xolo.gzqc.zxing.camera.CameraActivityCapture;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 接车员-首页
 */
public class HomePickFragment extends BaseFragment {


    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.titleview)
    TitleView titleview;

    private int[] imgs;

    /**
     * 扫码弹窗
     */
    private Dialog  dialog;

    private String code;

    private List<String>  list = new ArrayList<>();

    /**
     * 已绑定的二维码对应的车牌号
     */
    private String carno = "";

    public HomePickFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_pick, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    protected void init() {
        titleview.setLeftGone();
        titleview.setTitle(user.getDept_name()+"   接车员");

        initGv();
        initDialog();
    }

//    预约购件、接车、询价、报价、拆检、购件开单、进度查询模块
    private void initDialog() {

        dialog = creatListDialog("", list, null, new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                Intent  intent = null;
                if (s.equals("预约购件")){
                    intent = new Intent(mContent,PurchaseReservationActivity.class);
                }else if (s.equals("接车")){
                    intent = new Intent(mContent,PickUpCarActivity.class);
                }else if (s.equals("询价")){
                    intent = new Intent(mContent,InquiryActivity.class);
                }else if (s.equals("报价")){
                    intent = new Intent(mContent,OfferActivity.class);
                }else if (s.equals("拆检")){
                    intent = new Intent(mContent,VerhaulActivity.class);
                }else if (s.equals("购件")){
                    intent = new Intent(mContent,OrderPurchaseActivity.class);
                }else if (s.equals("进度查询")){
                    intent = new Intent(mContent,ProgressActivity.class);
                }else if (s.equals("车主资料")){
                    intent = new Intent(mContent,OwnerInfoActivity.class);
                }else  if (s.equals("车辆综合信息")){
                    intent = new Intent(mContent,CarInfoActivity.class);
                }else {
                    return;
                }
                intent.putExtra(Key.CARNO,carno);
                if (TextUtils.isEmpty(carno)){
                    intent.putExtra("code",code);
                }
                startActivity(intent);
            }
        });
    }


    private void initGv() {
        final int heigh_item = (ScreenUtils.getScreenHeight(mContent)- DensityUtils.dp2px(mContent,183))/4;

        imgs = new int[]{R.mipmap.home01, R.mipmap.home02, R.mipmap.home03, R.mipmap.home04, R.mipmap.home05, R.mipmap.home06, R.mipmap.home07, R.mipmap.home08, R.mipmap.home09, R.mipmap.home10, R.mipmap.home11, R.mipmap.home12, R.mipmap.home13, R.mipmap.home14, R.mipmap.home15, R.mipmap.home16};

        Class[] classes = new Class[]{
                PurchaseReservationActivity.class, DealAppointmentActivity.class, PickUpCarActivity.class, OfferActivity.class,
                DispatchingActivity.class, VerhaulActivity.class, InquiryActivity.class, OrderPurchaseActivity.class,
                ArrivalGoodsActivity.class, CompleteActivity.class, ReturnCarActivity.class, PaymentActivity.class
                , ProgressActivity.class, TeamActivity.class, NewsActivity.class, OwnerInfoActivity.class};
        String[] array = getResources().getStringArray(R.array.function_list);
        List<String> list = Arrays.asList(array);

        if (user.getUse_version() .equals( "1")){
             classes = new Class[]{PickUpCarActivity.class, OfferActivity.class,ReturnCarActivity.class, PaymentActivity.class, ProgressActivity.class,  NewsActivity.class, OwnerInfoActivity.class};
             array = getResources().getStringArray(R.array.function_list_2);
             list = Arrays.asList(array);
            imgs = new int[]{ R.mipmap.home03, R.mipmap.home04, R.mipmap.home11, R.mipmap.home12, R.mipmap.home13, R.mipmap.home15, R.mipmap.home16};
        }

        final String[] finalArray = array;
        gv.setAdapter(new CommenAdapter<String>(R.layout.item_gv_main, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                holder.setText(R.id.tv_1, finalArray[position]);
                holder.setImage(R.id.iv_1, imgs[position]);

                View view = holder.getView(R.id.ll_contain);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = heigh_item;
                view.setLayoutParams(layoutParams);
            }
        }));

        final Class[] finalClasses = classes;
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContent, finalClasses[position]));
            }
        });

    }


    @OnClick({R.id.btn_scan, R.id.btn_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scan:
                startActivityForResult(new Intent(mContent, CameraActivityCapture.class),REQUEST_CODE);
                break;
            case R.id.btn_info:
                startActivity(new Intent(mContent, CarInfoActivity.class));
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == -1){
          code = data.getExtras().getString(IntentConstant.RESULT);

//            下一次扫码的二维码可能是未绑定车牌号的
          carno = "";
          getcarnobyqrcode(code);

        }
    }


    /**
     *     2-76 通过车牌号决定可以进入哪个模块接口，接车员扫一扫专用。 getlistmodulebyscan(userid,dept_id,carno)
     当前用户ID:userid,维修厂ID:dept_id,车牌号：carno
     */
    private   void  getlistmodulebyscan(String  no){
        RequestParams params = creatParams("getlistmodulebyscan");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());
        params.addBodyParameter("qr_code",no);

        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                  if (result.getRes().equals("1")){
                      String[] split = result.getMsg().split(",");
                      list.clear();
                      Collections.addAll(list,split);
                      dialog.show();
                  }else {
                      ToastUtil.showShort(mContent,result.getMsg());
                  }
                mLoad.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });
    }


    /**
     *    2-89 通过二维码获取车牌号接口 getcarnobyqrcode(userid,qr_code)
     当前用户ID:userid,二维码：qr_code
     */
    private void getcarnobyqrcode(final String code){
        RequestParams params = creatParams("getcarnobyqrcode");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("qr_code",code);

        mLoad.show(mContent);

        HttpUtil.getInstance().post( params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                if (result.getRes().equals("1")){
                    carno = result.getData().get(0).getCarno();
                }
                getlistmodulebyscan(code);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContent);
            }
        });
    }


}
