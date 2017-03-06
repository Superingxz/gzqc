package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.PurchaseOrder;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.ui.fragment.InquiryHistroyFragment;
import com.xolo.gzqc.ui.fragment.OrderPurchaseFragment;
import com.xolo.gzqc.ui.fragment.OrderPurchseHistroyFragment;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 构件开单
 */
public class OrderPurchaseActivity extends BaseActivity implements TabChangeListener {


    @BindView(R.id.tabview)
    TabView tabview;
    private OrderPurchaseFragment orderPurchaseFragment;
    private OrderPurchseHistroyFragment orderPurchseHistroyFragment;

    private  String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_purchase);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        initTab();
    }

    private void initTab() {
        title = new String[]{"购件", "购件历史"};

        orderPurchaseFragment = new OrderPurchaseFragment();
        orderPurchseHistroyFragment = new OrderPurchseHistroyFragment();
        final BaseFragment[] fragments = new BaseFragment[]{orderPurchaseFragment,orderPurchseHistroyFragment};
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (orderPurchseHistroyFragment.onKeyDown(keyCode,event) || orderPurchaseFragment.onKeyDown(keyCode,event)){
            return false;
        }

        return super.onKeyDown(keyCode,event);
    }


    @Override
    public void change(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
