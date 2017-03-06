package com.xolo.gzqc.ui.activity.boss;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.SumByType;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 数据分析
 */
public class DataAnalysisActivity extends BaseActivity {

    @BindView(R.id.mTitleView)
    TitleView mTitleView;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.gv)
    GridView gv;

    private String  dType = "1";

    private List<SumByType> list = new ArrayList<>();
    private int[]  list_res ;
    private String[] list_title;
    private Class[] list_class;

    private long mExitTime;

    private CommenAdapter<String> sumByTypeCommenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_analysis);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitleView();
        initData();
        initTab();
        initGv();
        getsumbytype();
    }

    private void initTitleView() {
        mTitleView.setLeftGone();
        mTitleView.setRightText("更多");
        mTitleView.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(mContext,SearchDataAnalysisActivity.class));
            }
        });
    }

    private void initData() {
        list_title = new String[]{"预约构件","预约","接车","报价","采购","维修","完工","结算","结算金额","未结金额","实收金额"};
        list_res = new int[]{R.mipmap.home01,R.mipmap.boss_yuyue,R.mipmap.boss_jieche,R.mipmap.boss_baojia,R.mipmap.home05,
                R.mipmap.boss_weixiu,R.mipmap.boss_wangong,R.mipmap.boss_jieshuang,R.mipmap.boss_jieshuang_jingen,
                R.mipmap.boss_weijie_jingen,R.mipmap.boss_shishou_jinen};
        list_class = new Class[]{PurchaseReservationDetailedActivity.class,DealAppointmentDetailedActivity.class,PickUpCarDetailActivity.class
                ,OfferDetailActivity.class,BuymDetailActivity.class,RepairdetailActivity.class
                ,FinishDetailActivity.class,OutcarDetailActivity.class,SettledDetailActivity.class
                ,UnsettledDetailActivity.class,ReceiptsDetailActivity.class};
    }

    private void initGv() {
        List<String> titles = Arrays.asList(list_title);
        sumByTypeCommenAdapter = new CommenAdapter<>(R.layout.item_boss, mContext,titles, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {

                holder.setImage(R.id.iv,list_res[position]);
                holder.setText(R.id.tv_title,list_title[position]);

                if (list.size()!= 0){
                    SumByType sumByType = list.get(position);
                    String mtype = sumByType.getMtype();
                    if (mtype.equals("1")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_gj());
                    }else if (mtype.equals("2")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_yy());
                    }else if (mtype.equals("3")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_jc());
                    }else if (mtype.equals("4")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_bj());
                    }else if (mtype.equals("5")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_cg());
                    }else if (mtype.equals("6")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_wxz());
                    }else if (mtype.equals("7")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_wg());
                    }else if (mtype.equals("8")){
                        holder.setText(R.id.tv_sum,sumByType.getCount_js());
                    }else if (mtype.equals("9")){
                        holder.setText(R.id.tv_sum,sumByType.getMoney_js());
                    }else if (mtype.equals("10")){
                        holder.setText(R.id.tv_sum,sumByType.getMoney_wjs());
                    }else if (mtype.equals("11")){
                        holder.setText(R.id.tv_sum,sumByType.getMoney_ss());
                    }
                }else
                    holder.setText(R.id.tv_sum,"0");
            }
        });

        gv.setAdapter(sumByTypeCommenAdapter);
    }

    private void initTab() {
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.DarkBlue));
        mTabLayout.setTabTextColors(ContextCompat.getColor(mContext, R.color.gray), Color.parseColor("#015cab"));

        mTabLayout.addTab(mTabLayout.newTab().setText("日"));
        mTabLayout.addTab(mTabLayout.newTab().setText("月"));
        mTabLayout.addTab(mTabLayout.newTab().setText("季"));
        mTabLayout.addTab(mTabLayout.newTab().setText("年"));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                dType = String.valueOf(position+1);
                getsumbytype();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void getsumbytype(){
        RequestParams params = creatParams("getsumbytype");
        params.addBodyParameter("dtype",dType);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.SumByTypeBean.class, new HttpUtil.HttpCallBack<ORMBean.SumByTypeBean>() {
            @Override
            public void onSuccess(ORMBean.SumByTypeBean result) {
                          list.clear();
                          list.addAll(result.getData());
                          sumByTypeCommenAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnItemClick(R.id.gv)
    void  onItemClick(int position){
        Intent intent = new Intent(mContext, list_class[position]);
        intent.putExtra(IntentConstant.TYPE,dType);
        startActivity(intent);
    }

}
