package com.xolo.gzqc.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.ViewPagerAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CoMainImg;
import com.xolo.gzqc.bean.child.CoMaintainInfo;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.CarOwnerseMaintainActivity;
import com.xolo.gzqc.ui.activity.MaintainSubscribeActivity;
import com.xolo.gzqc.ui.activity.CarOwnersServiceActivity;
import com.xolo.gzqc.ui.activity.RescueActivity;
import com.xolo.gzqc.ui.activity.consumers.ConsumersActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.MyImageView;
import com.xolo.gzqc.ui.view.ScrollViewCustom;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 车主首页.
 */
public class MainCarOwnerFragment extends  LazyFragment{
    RelativeLayout carwoner_service_layout,subscribe_layout,rescue_layout,shop_layout;
    TitleView title_view;
    private ViewPager mVPActivity;
    private ViewPagerAdapter mPgAdapter;
    LinearLayout id_gallery;
    TextView de_name;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private ScrollViewCustom scrollViewCustom;
    private List<String> imageIdList = new ArrayList<String>();
    static MainCarOwnerFragment fragment;
    TextView maintain_info;
    public static CoMaintainInfo coMaintainInfo;
    public static MainCarOwnerFragment newInstance() {
        if (fragment == null) {
            fragment = new MainCarOwnerFragment();
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maincarowner,null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(de_name!=null&&title_view!=null){
            user = SPManager.getUser(mContext);
            de_name.setText(user.getDept_name());
            title_view.setTitle(user.getDept_name());
            if(!TextUtils.isEmpty(oldde_name)&&!oldde_name.equals(user.getDept_name())){
                getAdImg();
                // getB2CImg();
                getInside();
                getInfo();
            }
        }
    }

    void initView(View view){
        shop_layout=(RelativeLayout)view.findViewById(R.id.shop_layout);
        id_gallery=(LinearLayout)view.findViewById(R.id.id_gallery);
        scrollViewCustom=(ScrollViewCustom)view.findViewById(R.id.scrollViewCustom);
        de_name=(TextView)view.findViewById(R.id.dep_name);
        maintain_info=(TextView)view.findViewById(R.id.maintain_info);
        user = SPManager.getUser(mContext);
        rescue_layout=(RelativeLayout)view.findViewById(R.id.rescue_layout);
        subscribe_layout=(RelativeLayout)view.findViewById(R.id.subscribe_layout);
        carwoner_service_layout=(RelativeLayout)view.findViewById(R.id.carwoner_service_layout);
//MaintainSubscribeActivity
        subscribe_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(), CarOwnerseMaintainActivity.class);
                startActivity(intent);
            }
        });
        carwoner_service_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CarOwnersServiceActivity.class);
                startActivity(intent);
            }
        });
        rescue_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), RescueActivity.class);
                startActivity(intent);
            }
        });
        shop_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ConsumersActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        title_view=(TitleView) view.findViewById(R.id.title_view);
        title_view.setLeftGone();
        mVPActivity=(ViewPager)view.findViewById(R.id.vp_activity);

//        imageIdList.add("http://img1.imgtn.bdimg.com/it/u=176745999,516786760&fm=21&gp=0.jpg");
//        imageIdList.add("http://attachments.gfan.com/attachments2/day_111015/1110151302d4c8bc6ff8174287.jpg");
//        imageIdList.add("http://attachments.gfan.com/attachments2/day_111015/1110151301de443bf3f6c9fedf.jpg");





    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);


    }
    //初始化完毕
    @Override
        protected void loadData() {
            if(needLoad()){
                getAdImg();
               // getB2CImg();
                getInside();
                getInfo();
                mHasLoadedOnce=true;
            }
            //已加载数据
        }



    int num = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (num == mListFragment.size() - 1) {
                    num=0;
                    mVPActivity.setCurrentItem(num);
                } else {
                    num++;
                    mVPActivity.setCurrentItem(num);
                }
                handler.sendEmptyMessageDelayed(1, 3000);

            }
        }
    };


    @Override
    protected void init() {

    }

    //滚动广告图
    void getAdImg() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getownerfirstpic");
        requestParams.addBodyParameter("use_type", "1");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid",user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMainImgBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMainImgBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(ORMBean.CoMainImgBean result) {
                if (result.getRes().equals("1")) {
                    initViewPage(result);
                } else {
                    initViewPage(result);
                    handler.removeMessages(1);
                    ToastUtil.showLong(getActivity(),result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    private void initViewPage(ORMBean.CoMainImgBean result) {
        imageIdList.clear();
        mListFragment.clear();
        handler.removeMessages(1);
        for(CoMainImg coMainImg:result.getData()){
            imageIdList.add(coMainImg.getAttach_path());
        }
        for (int i = 0; i < imageIdList.size(); i++) {
            SplashPageFragment viewPagerFragment = new SplashPageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("upImageId", imageIdList.get(i));
            viewPagerFragment.setArguments(bundle);
            mListFragment.add(viewPagerFragment);
        }
        mPgAdapter = new ViewPagerAdapter(getChildFragmentManager(), mListFragment);
        mVPActivity.setAdapter(mPgAdapter);
        mVPActivity.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                num = position;
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        handler.sendEmptyMessageDelayed(1, 3000);
        mVPActivity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.removeMessages(1);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeMessages(1);
                    handler.sendEmptyMessageDelayed(1, 3000);
                }
                return false;
            }
        });
    }

    //b2c图
    void getB2CImg() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getownerfirstpic");
        requestParams.addBodyParameter("use_type", "3");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid",user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMainImgBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMainImgBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(ORMBean.CoMainImgBean result) {
                if (result.getRes().equals("1")) {
                } else {
                    ToastUtil.showLong(getActivity(),result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

    //内部图
    void getInside() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getownerfirstpic");
        requestParams.addBodyParameter("use_type", "2");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid",user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMainImgBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMainImgBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(ORMBean.CoMainImgBean result) {
                if (result.getRes().equals("1")) {
                    id_gallery.removeAllViews();
                    LinearLayout linearLayout = (LinearLayout) View.inflate(getActivity(),
                            R.layout.fragment_clubviewpage, null);
                    MyImageView img = (MyImageView) linearLayout.findViewById(R.id.club_viewpage_img);
                    for (int i = 0; i < result.getData().size(); i++) {
                        MyImageView imageView = new MyImageView(getActivity(),
                                result.getData().get(i).getAttach_path());
                        imageView.setLayoutParams(img.getLayoutParams());
                        id_gallery.addView(imageView);
                    }
                    scrollViewCustom.RecheckTotalWidth();
                } else {
                    id_gallery.removeAllViews();
                    ToastUtil.showLong(getActivity(),result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }

     String oldde_name;
    //维修厂信息
    void getInfo() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getownerfirst");
        requestParams.addBodyParameter("dept_id", user.getDept_id());
        requestParams.addBodyParameter("userid",user.getUser_id());
        HttpUtil.getInstance().post(requestParams, ORMBean.CoMaintainInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CoMaintainInfoBean>() {
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
            @Override
            public void onSuccess(ORMBean.CoMaintainInfoBean result) {
                if (result.getRes().equals("1")) {
                    coMaintainInfo=result.getData().get(0);
                    maintain_info.setText(result.getData().get(0).getIntrodction());
                    de_name.setText(result.getData().get(0).getDept_name());
                    title_view.setTitle(result.getData().get(0).getDept_name());
                    oldde_name=result.getData().get(0).getDept_name();
                } else {
                    ToastUtil.showLong(getActivity(),result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }
        });
    }
}
