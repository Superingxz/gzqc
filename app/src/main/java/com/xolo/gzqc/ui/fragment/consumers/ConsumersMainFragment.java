package com.xolo.gzqc.ui.fragment.consumers;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ConsumerMain;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.activity.consumers.GoodsInfoActivity;
import com.xolo.gzqc.ui.activity.consumers.MoreActivity;
import com.xolo.gzqc.ui.activity.consumers.SearchActivity;
import com.xolo.gzqc.ui.view.ScrollChangeListView;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.ui.view.ShufflingViewPager;
import com.xolo.gzqc.utils.DensityUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.CommenRecycleAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 消费者首页
 */
public class ConsumersMainFragment extends BaseFragment implements View.OnClickListener {

    //    @BindView(R.id.shuffViewPager)
    ShufflingViewPager shuffViewPager;
    //    @BindView(R.id.ll_car_accessory)
    LinearLayout llCarAccessory;
    //    @BindView(R.id.ll_second_hand_car)
    LinearLayout llSecondHandCar;
    //    @BindView(R.id.ll_special_goods)
    LinearLayout llSpecialGoods;
    //    @BindView(R.id.ll_else)
    LinearLayout llElse;
    //    @BindView(R.id.slv)
    ScrollListView slv;
        @BindView(R.id.btn_search)
    LinearLayout btnSearch;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.btn_return_top)
    ImageView btnReturnTop;
    @BindView(R.id.lv)
    ScrollChangeListView listView;
//    @BindView(R.id.srv)
//    ScrollRecycleView srv;

    private List<ConsumerMain.TypeBean> list_type = new ArrayList<>();
    private CommenAdapter<ConsumerMain.TypeBean> adapter;
    private PopupWindow dialog_search;
    private CommenRecycleAdapter<ConsumerMain.TypeBean> typeBeanCommenRecycleAdapter;
    private Drawable drawable;

    public ConsumersMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consumers_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            shuffViewPager.stop();
        } else
            shuffViewPager.start();
    }

    /**
     * 7-1 消费者首页接口 clienthome(userid)
     * 当前用户ID:userid
     */
    private void clienthome() {
        RequestParams params = creatParams("clienthome");
        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.ConsumerMainBean.class, new HttpUtil.HttpCallBack<ORMBean.ConsumerMainBean>() {
            @Override
            public void onSuccess(ORMBean.ConsumerMainBean result) {
                ConsumerMain consumerMain = result.getData().get(0);
                List<ConsumerMain.HomeimgBean> homeimg = consumerMain.getHomeimg();

                List<String> list_url = new ArrayList<String>();
                for (ConsumerMain.HomeimgBean h : homeimg) {
                    list_url.add(h.getPic_path());
                }
//                list_url.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3608429800,3146691015&fm=111&gp=0.jpg");
//                list_url.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4165520467,4140249363&fm=116&gp=0.jpg");
//                list_url.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3637379975,3338374522&fm=21&gp=0.jpg");
                shuffViewPager.load(list_url);

                List<ConsumerMain.TypeBean> type = consumerMain.getType();
                if (type != null) {
                    list_type.clear();
                    list_type.addAll(type);
                }
                adapter.notifyDataSetChanged();

                listView.setFocusable(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @Override
    protected void init() {
//        slv.setFocusable(false);
        initHeadView();
        initlv();
//        initRcv();
        initShuffViewpager();
        clienthome();
    }


    /**
     * 初始化头布局
     */
    private void initHeadView() {
        drawable = ContextCompat.getDrawable(mContent, R.drawable.seach_bg);
        drawable.setAlpha(180);
        btnSearch.setBackgroundDrawable(drawable);

        View inflate = LayoutInflater.from(mContent).inflate(R.layout.merge_consumers_main, null);
        shuffViewPager = (ShufflingViewPager) inflate.findViewById(R.id.shuffViewPager);
        llCarAccessory = (LinearLayout) inflate.findViewById(R.id.ll_car_accessory);
        llElse = (LinearLayout) inflate.findViewById(R.id.ll_else);
        llSecondHandCar = (LinearLayout) inflate.findViewById(R.id.ll_second_hand_car);
        llSpecialGoods = (LinearLayout) inflate.findViewById(R.id.ll_special_goods);


        btnReturnTop.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        shuffViewPager.setOnClickListener(this);
        llCarAccessory.setOnClickListener(this);
        llElse.setOnClickListener(this);
        llSpecialGoods.setOnClickListener(this);
        llSecondHandCar.setOnClickListener(this);

        listView.addHeaderView(inflate);
    }


    private void initShuffViewpager() {
        final int height = ScreenUtils.getScreenWidth(mContext) / 2;

        ViewGroup.LayoutParams layoutParams = shuffViewPager.getLayoutParams();
        layoutParams.height = height;
        shuffViewPager.setLayoutParams(layoutParams);

        shuffViewPager.setDotCheck(R.drawable.dot_check);
        shuffViewPager.setDotUncheck(R.drawable.dot_uneck);
    }


    public void startMore(String value_data, String title) {
        Intent intent = new Intent(mContext, MoreActivity.class);
        intent.putExtra(IntentConstant.VALUE_DATA, value_data);
        intent.putExtra(IntentConstant.TITLE, title);
        startActivity(intent);
    }

    private void initlv() {
        final int ivHeight = ScreenUtils.getScreenWidth(mContext) / 2 - DensityUtils.dp2px(mContext, 30);

        adapter = new CommenAdapter<>(R.layout.item_type_good, mContent, list_type, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                final ConsumerMain.TypeBean typeBean = list_type.get(position);
                final List<ConsumerMain.TypeBean.GoodsBean> goods = typeBean.getGoods();

                holder.setText(R.id.tv_title, typeBean.getDisplay_data());
                holder.setOnClicklistener(R.id.btn_more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startMore(typeBean.getValue_data(), typeBean.getDisplay_data());
                    }
                });

                if (goods.size() > 0) {
                    GridView gv = (GridView) holder.getView(R.id.sgv);

                    gv.setAdapter(new CommenAdapter<ConsumerMain.TypeBean.GoodsBean>(R.layout.item_consumer_maingood, mContent, goods, new CommenAdapter.AdapterCallback() {
                        @Override
                        public void setView(ViewHolder holder, int position) {
                            ConsumerMain.TypeBean.GoodsBean goodsBean = goods.get(position);

                            holder.loadUrl(R.id.iv_good, goodsBean.getPic_path());
                            holder.setText(R.id.tv_price, "￥" + goodsBean.getSales_price());
                            holder.setText(R.id.tv_qty, "销量：" + goodsBean.getQty());
                            holder.setText(R.id.tv_title, goodsBean.getGoods_name());

                            ImageView iv = holder.getView(R.id.iv_good);
                            ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                            layoutParams.height = ivHeight;
                            iv.setLayoutParams(layoutParams);
                        }
                    }));

                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ConsumerMain.TypeBean.GoodsBean goodsBean = goods.get(position);

                            Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                            intent.putExtra(IntentConstant.GOODSINFOR_ID, goodsBean.getC_goodsinfor_id());
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        final int screenWidth = ScreenUtils.getScreenWidth(mContent)/2-DensityUtils.dp2px(mContent,50);
        final int screenHeight = ScreenUtils.getScreenHeight(mContent)-DensityUtils.dp2px(mContent,53);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                int scrollY = listView.getScrollY();
//                LogUtil.i(getScrollY()+"");
                int t = getScrollY();
                if (t<screenWidth){
                    float scale = (float) t / screenWidth;
                    float alphaRl = (255 * scale);
                    rlTitle.setBackgroundColor(Color.argb((int) alphaRl, 255, 255, 255));

                    float alphaBtn= (75 * scale)+180;
                    drawable.setAlpha((int) alphaBtn);
                    btnSearch.setBackgroundDrawable(drawable);


                } else  {
                    rlTitle.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    drawable.setAlpha(255);
                    btnSearch.setBackgroundDrawable(drawable);
                }
                if (t>screenHeight){
                    btnReturnTop.setVisibility(View.VISIBLE);
                }else {
                    btnReturnTop.setVisibility(View.GONE);
                }
            }
        });

//        listView.setOnScrollListener(new ScrollChangeListView.OnScrollChangeListener() {
//             @Override
//             public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                       if (t<screenWidth){
//                           float scale = (float) t / screenWidth;
//                           float alphaRl = (255 * scale);
//                           rlTitle.setBackgroundColor(Color.argb((int) alphaRl, 255, 255, 255));
//
//                           float alphaBtn= (75 * scale)+180;
//                           drawable.setAlpha((int) alphaBtn);
//                           btnSearch.setBackgroundDrawable(drawable);
//
//                           LogUtil.i("ar:"+alphaRl);
//                           LogUtil.i("ab:"+alphaBtn);
//                           LogUtil.i("ab:"+t);
//
//                       }else {
//                           rlTitle.setBackgroundColor(Color.argb(255, 255, 255, 255));
//                           drawable.setAlpha(255);
//                           btnSearch.setBackgroundDrawable(drawable);
//                       }
//                 if (t>screenHeight){
//                     btnReturnTop.setVisibility(View.VISIBLE);
//                 }else {
//                     btnReturnTop.setVisibility(View.GONE);
//                 }
//             }
//         });

        listView.setAdapter(adapter);
    }

    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }


    //    @OnClick({R.id.btn_search, R.id.ll_car_accessory, R.id.ll_second_hand_car, R.id.ll_special_goods, R.id.ll_else})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_car_accessory:
                startMore("f_qcyp", "汽车用品");
                break;
            case R.id.ll_second_hand_car:
                startMore("f_esc", "二手车");
                break;
            case R.id.ll_special_goods:
                startMore("f_tssp", "汽车用品");
                break;
            case R.id.ll_else:
                startMore("f_qt", "其他");
                break;
            case R.id.btn_return_top:
                listView.setSelection(0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shuffViewPager.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        shuffViewPager.stop();
    }

}
