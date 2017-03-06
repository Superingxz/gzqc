package com.xolo.gzqc.ui.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.ArrivalGoodsActivity;
import com.xolo.gzqc.ui.activity.pickcar.InquiryActivity;
import com.xolo.gzqc.ui.activity.pickcar.ProgressActivity;
import com.xolo.gzqc.ui.activity.procurement.QueryOfferActivity;
import com.xolo.gzqc.ui.activity.procurement.QueryPairActivity;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.zxing.camera.CameraActivityCapture;

import org.xutils.http.RequestParams;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 采购主页面
 */
public class ProcurementMainFragment extends BaseFragment {


    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.tabview)
    TabView tabview;

    private BaseFragment[] fragments ;
    private String[] title ;

    private Dialog dialog;

    private String carno;

    public ProcurementMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_procurement_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        initTitle();
        initTab();
        initDialog();
    }

    private void initTitle() {
        titleview.setLeftGone();
        titleview.setTitle(user.getDept_name()+"   采购员");
        titleview.setRightBg(R.mipmap.sao);
        titleview.setRightIvClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivityForResult(new Intent(mContent, CameraActivityCapture.class),REQUEST_CODE);
            }
        });
    }

    //    <!--2、采购员返回：我的待办事项、维修进度、历史报价、历史购件模块-->
    private void initDialog() {
        String[] array = getResources().getStringArray(R.array.home_procurement);
        List<String> strings1 = Arrays.asList(array);

        dialog = creatListDialog("",strings1,null,new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                Intent  intent = null;
                if (s.equals("我的待办事项")){
                    getbuyermytodo();
                    return;
                }else if (s.equals("维修进度")){
                    intent = new Intent(mContext, ProgressActivity.class);
                    intent.putExtra("type",1);
                }else if (s.equals("历史报价")){
                    intent = new Intent(mContent, QueryOfferActivity.class);
                }else if (s.equals("历史购件模块")){
                    intent = new Intent(mContent, QueryOfferActivity.class);
                    intent.putExtra("type",1);
                }
                intent.putExtra(Key.CARNO,carno);
                startActivity(intent);
            }

        });
    }


    private void initTab() {

        title = getResources().getStringArray(R.array.procurementDeal);
        fragments = new BaseFragment[]{new ProcurementDealingFragment(),new ProcurementDealedFragment()};
        tabview.setTitle(title);
        tabview.setFragments(fragments);

        tabview.setPageChage(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragments[position].load();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void load() {
        super.load();

    }


    @OnClick({R.id.btn_offer, R.id.btn_purchase})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_offer:
                Intent intent = new Intent(mContent, QueryOfferActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_purchase:
                Intent intent1 = new Intent(mContent, QueryPairActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == -1){
            carno = data.getExtras().getString("result");

            if (!TextUtils.isEmpty(carno)){
                getcarnobyqrcode(carno);
            }

        }
    }


    /**
     * -1 采购员首页-我的待办事项接口 getbuyermytodo(userid,dept_id,carno)
     当前用户ID：userid,维修厂ID：dept_id,车牌号(扫一扫的时候使用):carno
     */
      private   void   getbuyermytodo(){
          RequestParams params = creatParams("getbuyermytodo");
          params.addBodyParameter(Key.USER_ID,user.getUser_id());
          params.addBodyParameter("dept_id",user.getDept_id());
          params.addBodyParameter("carno",carno);

          HttpUtil.getInstance().postLoading(mContent, params, ORMBean.OrdermbuypartsBean.class, new HttpUtil.HttpCallBack<ORMBean.OrdermbuypartsBean>() {
              @Override
              public void onSuccess(ORMBean.OrdermbuypartsBean result) {
                  Ordermbuyparts ordermbuyparts = result.getData().get(0);
                  if (ordermbuyparts.getStatus().equals("询价中")){
                      Intent intent = new Intent(mContent, InquiryActivity.class);
                      intent.putExtra("type",1);
                      intent.putExtra(Key.CARNO,ordermbuyparts.getCarno());
                      startActivity(intent);
                  }else {
                      Intent intent = new Intent(mContent, ArrivalGoodsActivity.class);
                      intent.putExtra("type",1);
                      intent.putExtra(Key.CARNO,ordermbuyparts.getCarno());
                      startActivity(intent);
                  }
              }

              @Override
              public void onError(Throwable ex, boolean isOnCallback) {

              }
          });
      }


    /**
     *    2-89 通过二维码获取车牌号接口 getcarnobyqrcode(userid,qr_code)
     当前用户ID:userid,二维码：qr_code
     */
    private void getcarnobyqrcode(String code){
        RequestParams params = creatParams("getcarnobyqrcode");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("qr_code",code);

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                carno = result.getData().get(0).getCarno();
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

}
