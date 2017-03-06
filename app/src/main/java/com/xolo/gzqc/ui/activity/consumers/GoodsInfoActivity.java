package com.xolo.gzqc.ui.activity.consumers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.GoodInfo;
import com.xolo.gzqc.bean.child.ShoppingCart;
import com.xolo.gzqc.configuration.Constant;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.fragment.consumers.CommentFragment;
import com.xolo.gzqc.ui.fragment.consumers.GoodPhotoFragment;
import com.xolo.gzqc.ui.fragment.consumers.ProductParametersFragment;
import com.xolo.gzqc.ui.view.ExScrollView;
import com.xolo.gzqc.ui.view.ShufflingViewPager;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DensityUtils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ScreenUtils;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * 商品详情
 */
public class GoodsInfoActivity extends BaseActivity {

    @BindView(R.id.shuffViewPager)
    ShufflingViewPager shuffViewPager;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_saleNum)
    TextView tvSaleNum;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tl)
    TabLayout tl;
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.fl_contain)
    FrameLayout flContain;
    @BindView(R.id.btn_service)
    LinearLayout btnService;
    @BindView(R.id.btn_supplier)
    LinearLayout btnSupplier;
    @BindView(R.id.btn_add_shop_cart)
    TextView btnAddShopCart;
    @BindView(R.id.btn_buy)
    TextView btnBuy;
    @BindView(R.id.tl_top)
    TabLayout tlTop;
    @BindView(R.id.sv)
    ExScrollView sv;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_return_top)
    ImageView btnReturnTop;

    private GoodPhotoFragment goodPhotoFragment;
    private FragmentManager supportFragmentManager;
    private ProductParametersFragment productParametersFragment;
    private CommentFragment commentFragment;
    private GoodInfo goodInfo;

    private boolean isChaged = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTab();
        initShuffViewpager();
        initSv();
        goodsdetailinfo();
    }

    private void initSv() {
        final int screenWidth = ScreenUtils.getScreenWidth(mContext) - DensityUtils.dp2px(mContext, 50);
        final int screenHeight = ScreenUtils.getScreenHeight(mContext) - DensityUtils.dp2px(mContext, 50);
        final int screenWidthHapt = screenWidth / 2;

        LogUtil.i("heigh:"+tvGoodName.getMeasuredHeight()+","+tvGoodName.getHeight());
        //tablayout距离顶头
        final int y_tl = DensityUtils.dp2px(mContext, 110) + screenWidth;

        sv.setOnScrollChangedListener(new ExScrollView.OnScrollchangedListener() {
            @Override
            public void change(int l, int t, int oldl, int oldt) {
                if (t > y_tl) {
                    tlTop.setVisibility(View.VISIBLE);
                } else {
                    tlTop.setVisibility(View.GONE);
                }
                if (t < screenWidth) {
                    float scale = (float) t / screenWidth;
                    float alpha = (255 * scale);
                    rlTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    rlTitle.setBackgroundColor(Color.argb(255, 255, 255, 255));
                }
                if (t < screenWidthHapt) {
                    btnBack.setBackgroundResource(R.drawable.dot_back);
                    btnBack.setImageResource(R.mipmap.arrow_left_white);
                } else {
                    btnBack.setBackgroundColor(Color.argb(0, 255, 255, 255));
                    btnBack.setImageResource(R.mipmap.arrow_left);
                }
                if (t > screenHeight) {
                    btnReturnTop.setVisibility(View.VISIBLE);
                }else {
                    btnReturnTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initShuffViewpager() {
        final int height = ScreenUtils.getScreenWidth(mContext);

        ViewGroup.LayoutParams layoutParams = shuffViewPager.getLayoutParams();
        layoutParams.height = height;
        shuffViewPager.setLayoutParams(layoutParams);
    }

    private void initTab() {
        supportFragmentManager = getSupportFragmentManager();
        goodPhotoFragment = new GoodPhotoFragment();
        productParametersFragment = new ProductParametersFragment();
        commentFragment = new CommentFragment();

        supportFragmentManager.beginTransaction()
                .add(R.id.fl_contain, goodPhotoFragment)
                .add(R.id.fl_contain, productParametersFragment)
                .add(R.id.fl_contain, commentFragment)
                .commit();

        addTab(tl);
        addTab(tlTop);

        tl.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isChaged) {
                    changeTab(tab.getPosition());
                    isChaged = false;
                } else {
                    isChaged = true;
                }
                tlTop.getTabAt(tab.getPosition()).select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tlTop.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isChaged) {
                    changeTab(tab.getPosition());
                    isChaged = false;
                } else {
                    isChaged = true;
                }
                tl.getTabAt(tab.getPosition()).select();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        changeTab(0);
    }

    public void addTab(TabLayout tl) {
        TabLayout.Tab tab1 = tl.newTab().setText("图文详情");
        TabLayout.Tab tab2 = tl.newTab().setText("产品参数");
        TabLayout.Tab tab3 = tl.newTab().setText("用户评论");

        tl.addTab(tab1);
        tl.addTab(tab2);
        tl.addTab(tab3);
        tl.setSelectedTabIndicatorColor(getResources().getColor(R.color.DarkBlue));
        tl.setTabTextColors(ContextCompat.getColor(mContext, R.color.gray), Color.parseColor("#015cab"));
    }

    public void changeTab(int which) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction().hide(goodPhotoFragment).hide(productParametersFragment).hide(commentFragment);
        if (which == 0) {
            transaction.show(goodPhotoFragment).commit();
        } else if (which == 1) {
            transaction.show(productParametersFragment).commit();
        } else if (which == 2) {
            transaction.show(commentFragment).commit();
        }
    }


    /**
     * 7-6 消费者商品详情接口
     * 应用的界面：应用于用户点击某个商品图片，显示商品的详细数据。
     * goodsdetailinfo(userid,c_goodsinfor_id)
     * 当前用户ID:userid,商品ID:c_goodsinfor_id
     */

    private void goodsdetailinfo() {
        RequestParams params = new RequestParams(Constant.BASE_URL);
        params.addBodyParameter(Key.ACTION, "goodsdetailinfo");
        params.addBodyParameter(Key.USER_ID, "E501FF7C-805D-43D9-A5E8-B0BEC77CD780");
        params.addBodyParameter("c_goodsinfor_id", getIntent().getStringExtra(IntentConstant.GOODSINFOR_ID));

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.GoodInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.GoodInfoBean>() {
            @Override
            public void onSuccess(ORMBean.GoodInfoBean result) {
                goodInfo = result.getData().get(0);
                tvGoodName.setText(goodInfo.getGoods_name());
                tvComment.setText("评论：" + goodInfo.getComment_count());
                tvPrice.setText("￥" + goodInfo.getSales_price());
                tvSaleNum.setText("销量：" + goodInfo.getTotal_qty());

                List<String> list_top_photo = new ArrayList<String>();
                for (GoodInfo.TopimageBean t : goodInfo.getTopimage()) {
                    list_top_photo.add(t.getPic_path());
                }

                shuffViewPager.setDotCheck(R.drawable.dot_check);
                shuffViewPager.setDotUncheck(R.drawable.dot_uneck);
                shuffViewPager.setRun(false);
                if (list_top_photo.size() > 0) {
                    shuffViewPager.load(list_top_photo);
                }

                ((GoodInfoCallBack) goodPhotoFragment).readInfo(goodInfo);
                ((GoodInfoCallBack) productParametersFragment).readInfo(goodInfo);
                ((GoodInfoCallBack) commentFragment).readInfo(goodInfo);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    @OnClick({R.id.btn_service, R.id.btn_supplier, R.id.btn_add_shop_cart, R.id.btn_buy, R.id.btn_back,R.id.btn_return_top})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_service:
                startFriendDetailsPage();
                break;
            case R.id.btn_supplier:
                Intent intent1 = new Intent(mContext, StoreActivity.class);
                intent1.putExtra(IntentConstant.SUPPLIERINFOR_ID, goodInfo.getC_supplierinfor_id());
                startActivity(intent1);
                break;
            case R.id.btn_add_shop_cart:
                addsalecar();
                break;
            case R.id.btn_buy:
                List<ShoppingCart.SalecarGoodsBean> list_good = new ArrayList<>();
                list_good.add(new ShoppingCart.SalecarGoodsBean("", goodInfo.getGoods_name(), "1", goodInfo.getSales_price(), goodInfo.getPrice(), goodInfo.getSpecification_model(), goodInfo.getPic_1(), goodInfo.getC_goodsinfor_id()));

                ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
                shoppingCarts.add(new ShoppingCart(goodInfo.getSupplier_name(), goodInfo.getC_supplierinfor_id(), list_good));

                Intent intent = new Intent(mContext, SubmitOrderActivity.class);
                intent.putParcelableArrayListExtra(IntentConstant.LIST_SHOPPING_CART, shoppingCarts);
                intent.putExtra(IntentConstant.EDITABLE, true);
                startActivity(intent);
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_return_top:
                sv.scrollTo(0,0);
                break;
        }
    }


    public interface GoodInfoCallBack {
        void readInfo(GoodInfo goodInfo);
    }


    /**
     * 7-8 加入购物车
     * addsalecar(userid,orders_name,orders_phone)
     * 当前用户ID:userid,消费者姓名:orders_name,消费者电话:orders_phone
     */
    private void addsalecar() {
        RequestParams params = creatParams("addsalecar");
        params.addBodyParameter("orders_name", user.getName());
        params.addBodyParameter("orders_phone", user.getPhone());
        params.addBodyParameter("c_goodsinfor_id", goodInfo.getC_goodsinfor_id());

        mLoad.show(mContext);
        HttpUtil.getInstance().post(params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                if (result.getRes().equals("1")) {

                } else {
                }
                ToastUtil.showShort(mContext, result.getMsg());
                mLoad.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mLoad.dismiss(mContext);
            }
        });
    }

    private void startFriendDetailsPage() {
        if (goodInfo!=null){
            RongIM.getInstance().startPrivateChat(mContext,goodInfo.getSupplier_phone(),goodInfo.getSupplier_name());
        }
    }
}
