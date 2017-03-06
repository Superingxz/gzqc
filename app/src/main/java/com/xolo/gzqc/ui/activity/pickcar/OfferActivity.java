package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.OwnerInfo;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.fragment.MaintenanceListFragment;
import com.xolo.gzqc.ui.fragment.OfferHistroyFragment;
import com.xolo.gzqc.ui.fragment.PartListFragment;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 报价
 */
public class OfferActivity extends BaseActivity {

    @BindView(R.id.tabview)
    TabView tabview;
    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.brand)
    TextView brand;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.titleview)
    TitleView titleview;


    private BaseFragment[] fragments;
    private String[] title;

    private Dialog dialog_carno;
    private LoadDialog dialog_loding;

    private List<CarInfo> list_carInfo = new ArrayList<>();

    private CarInfo carInfo;

    private boolean isRead;

    private List<Offer> offers = new ArrayList<>();

    private ReceiveInfo receiveInfo;
    private MaintenanceListFragment maintenanceListFragment;
    private PartListFragment partListFragment;
    private OfferHistroyFragment offerHistroyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTab();
        initDialog();
        initIntent();
    }

    private void initIntent() {

        carInfo = (CarInfo) getIntent().getSerializableExtra(Key.OBJECT);

        /**
         * 扫二维码进来
         */
        String no = getIntent().getStringExtra(Key.CARNO);
        if (!TextUtils.isEmpty(no)){
            getcarownerbycarno(no);
            return;
        }

        /**
         * 车辆综合信息读数据
         */
        if (carInfo != null) {

            isRead = true;

            titleview.setTitle("报价详情");

            brand.setText(carInfo.getBrands()+"  "+carInfo.getTypecode());

            btnAdd.setVisibility(View.GONE);
            select.setVisibility(View.GONE);

            getcarownerbycarno(carInfo.getCarno());
        }

    }


    private void initTab() {
        tabview.setTabMode(TabLayout.MODE_SCROLLABLE);

        title = getResources().getStringArray(R.array.offer);

        maintenanceListFragment = new MaintenanceListFragment();
        //车主隐打印按钮
        if(!TextUtils.isEmpty(getIntent().getStringExtra("carwoner"))){
            Bundle bundle=new Bundle();
            bundle.putString("carwoner","carwoner");
            maintenanceListFragment.setArguments(bundle);
        }
        partListFragment = new PartListFragment();
        offerHistroyFragment = new OfferHistroyFragment();

        fragments = new BaseFragment[]{maintenanceListFragment, partListFragment, offerHistroyFragment};
        tabview.setTitle(title);
        tabview.setFragments(fragments);
        tabview.setPage(3);

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


    private void initDialog() {
        dialog_carno = creatListDialog("车牌号", list_carInfo, carno, new ListDialogCallBack<CarInfo>() {
            @Override
            public String setText(CarInfo carInfo) {
                return carInfo.getCarno();
            }

            @Override
            public void onClick(CarInfo carInfo1) {
                carno.setText(carInfo1.getCarno());
                brand.setText(carInfo1.getBrands() + "  " + carInfo1.getTypecode());

                clear();
                getcarownerbycarno(carInfo1.getCarno());
            }
        });
    }


    /**
     *    2-52-B 根据接车当前状态以及维修厂id查询接车车辆列表 getcarlistbydeptidandstatus(userid,dept_id)
     当前用户ID:userid,维修厂ID:dept_id
     */
    private void  getcarlistbydeptidandstatus(){
        RequestParams params = creatParams("getcarlistbydeptidandstatus");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

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
     * 通过车牌号获取该车的车主信息接口 getcarownerbycarno(userid,carno) 当前用户ID:userid,车牌号:carno
     */
    private void getcarownerbycarno(final String no) {
        RequestParams params = creatParams("getcarownerbycarno");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.CARNO, no);

        HttpUtil.getInstance().post(params, ORMBean.OwnerInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.OwnerInfoBean>() {
            @Override
            public void onSuccess(ORMBean.OwnerInfoBean result) {
                carno.setText(no);

                if (result.getRes().equals("1")) {
                    OwnerInfo ownerInfo1 = result.getData().get(0);

                    name.setText(ownerInfo1.getCar_owner_name());
                    phone.setText(ownerInfo1.getPhone());

                    offerHistroyFragment.setCarno(no);
                    maintenanceListFragment.setPhone(ownerInfo1.getPhone());

                    getnewreceiveinfo();
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
     * 2-29 通过车牌号获取本维修厂最新的接车信息接口 getnewreceiveinfo(userid,carno,dept_id) 当前用户ID:userid,车牌号:carno,维修厂ID:dept_id
     */
    private void getnewreceiveinfo() {
        RequestParams params = creatParams("getnewreceiveinfo");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter(Key.CARNO, carno.getText().toString().trim());

        HttpUtil.getInstance().post(params, ORMBean.ReceiveInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.ReceiveInfoBean>() {
            @Override
            public void onSuccess(ORMBean.ReceiveInfoBean result) {
                if (result.getRes().equals("1")) {
                    receiveInfo = result.getData().get(0);
                    maintenanceListFragment.setReceiveInfo(receiveInfo);

                    brand.setText(receiveInfo.getBrands() +"   "+receiveInfo.getTypecode());
//                    partListFragment.setReceiveInfo(receiveInfo);
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
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
     * 2-52-B 根据接车当前状态以及维修厂id查询接车车辆列表 getcarlistbydeptidandstatus(userid,dept_id)
     当前用户ID:userid,维修厂ID:dept_id
     */
    private void listcarinfo() {
        RequestParams params = creatParams("getcarlistbydeptidandstatus");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

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


    @OnClick({R.id.select, R.id.btn_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select:
                if (list_carInfo.size() > 0) {
                    dialog_carno.show();
                } else
                    getcarlistbydeptidandstatus();
                break;
            case R.id.btn_add:
                if (TextUtils.isEmpty(getText(carno))){
                    ToastUtil.showShort(mContext,"请选择车牌号");
                    return;
                }
                Intent intent = new Intent(mContext, AddOfferItemActivity.class);
                intent.putExtra(Key.CARNO,getText(carno));
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Offer offer = (Offer) data.getSerializableExtra(Key.OBJECT);
            maintenanceListFragment.addOffer(offer);
            partListFragment.add(offer.getRepairlist());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isRead() {
        return isRead;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void  addRepairList(List<Part>  list){
        partListFragment.add(list);
    }

    public void  updateRepairList(List<Part>  list){
        partListFragment.clear();
        partListFragment.add(list);
    }

    @Override
    public void clear() {
        super.clear();
        tabview.setCurrentPage(0);
        maintenanceListFragment.clear();
        partListFragment.clear();
        btnAdd.setVisibility(View.VISIBLE);
    }

    public void setbtnVIsible(int visible){
        btnAdd.setVisibility(visible);
    }
}
