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
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.CompleteDetailActivity;
import com.xolo.gzqc.ui.activity.team.Complete_2Activity;
import com.xolo.gzqc.ui.activity.team.MaintenanceHistroyActivity;
import com.xolo.gzqc.ui.activity.pickcar.ProgressActivity;
import com.xolo.gzqc.ui.activity.pickcar.VerhaulActivity;
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
 * 班组首页
 */
public class TeamMainFragment extends BaseFragment {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.tabview)
    TabView tabview;

    private BaseFragment[] fragments ;
    private String[] title ;

    private Finished_2Fragment finishedFragment1;

    private Dialog dialog;

    private String carno;

    public TeamMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        initTitle();
        initTab();
        initDialog();
    }

    //    <!--3、班组返回：未完工车辆、历史维修、维修接班、维修进度模块-->
    private void initDialog() {
        String[] array = getResources().getStringArray(R.array.home_team);
        List<String> strings1 = Arrays.asList(array);

        dialog = creatListDialog("",strings1,null,new ListDialogCallBack<String>() {
            @Override
            public String setText(String s) {
                return s;
            }

            @Override
            public void onClick(String s) {
                Intent  intent = null;
                if (s.equals("未完工车辆")){
                    getlistwaitreturncar();
                    return;
                }else if (s.equals("维修进度")){
                    intent = new Intent(mContext, ProgressActivity.class);
                    intent.putExtra("type",1);
                }else if (s.equals("历史维修")){
                    intent = new Intent(mContent, MaintenanceHistroyActivity.class);
                }
                else if (s.equals("拆件")){
                    intent = new Intent(mContent, MaintenanceHistroyActivity.class);
                }
//                else if (s.equals("维修接班")){
//                    intent = new Intent(mContent, Complete_2Activity.class);
//                    intent.putExtra("type",1);
//                }
                intent.putExtra(Key.CARNO,carno);
                startActivity(intent);
            }

        });
    }


    private void initTitle() {
        titleview.setLeftGone();
        titleview.setTitle(user.getDept_name()+"   班组");
        titleview.setRightBg(R.mipmap.sao);
        titleview.setRightIvClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContent, CameraActivityCapture.class),REQUEST_CODE);
            }
        });
    }

    private void initTab() {

        Bundle bundle1 = new Bundle();
        bundle1.putInt("type",2);
        finishedFragment1 = new Finished_2Fragment();
        finishedFragment1.setArguments(bundle1);

        title = getResources().getStringArray(R.array.team);
        fragments = new BaseFragment[]{new NotCompleteFragment(),finishedFragment1};
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
        if (!isLoaded) {

        }
    }


    @OnClick({R.id.btn_offer, R.id.btn_purchase})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_offer:
                intent = new Intent(mContent, MaintenanceHistroyActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_purchase:
//                intent = new Intent(mContent, Complete_2Activity.class);
                intent = new Intent(mContent, Complete_2Activity.class);
                intent.putExtra("is_team",true);
                startActivity(intent);
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


    /**
     * 5-1 本维修厂未完工车辆列表接口,如果是我的班组，则需要传班组ID。接车员的完工模块的待完工车辆也使用此接口。 getlistunfinishcar(userid,dept_id,team_id)
     当前用户ID：userid,维修厂ID:dept_id,班组ID（有则为查我的班组）：team_id
     */

    private void   getlistwaitreturncar(){
        RequestParams params = creatParams("getlistunfinishcar");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());
        params.addBodyParameter("carno",carno);

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                CarInfo carInfo = result.getData().get(0);

                Intent intent = null;

                if (carInfo.getStatus().equals("拆检中")){
                    intent = new Intent(mContent,VerhaulActivity.class);
                    intent.putExtra(Key.CARNO,carInfo.getCarno());
                }else {
                    intent = new Intent(mContent,CompleteDetailActivity.class);
                }

                intent.putExtra(Key.OBJECT,carInfo);
                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });

    }

}
