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
import com.xolo.gzqc.bean.child.Inquiry;
import com.xolo.gzqc.bean.postJson.InquiryPart;
import com.xolo.gzqc.ui.fragment.InquiryFragment;
import com.xolo.gzqc.ui.fragment.InquiryHistroyFragment;
import com.xolo.gzqc.ui.fragment.VerhaulFragment;
import com.xolo.gzqc.ui.fragment.VerhaulHistroyFragment;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 询价
 * 0:接车员  1：采购员
 */
public class InquiryActivity extends BaseActivity implements TabChangeListener {


    @BindView(R.id.tabview)
    TabView tabview;
    private boolean is_edit;

    private List<Control> list_displacement = new ArrayList<>();
    private List<Control> list_drive = new ArrayList<>();
    private List<Control> list_fuel = new ArrayList<>();
    private List<Control> list_operation = new ArrayList<>();
    private List<CarInfo> list_carInfo = new ArrayList<>();
    private List<InquiryPart> list_part = new ArrayList<>();

    private Control control_displacement;
    private Control control_drive;
    private Control control_fuel;
    private Control control_operation;

    private Dialog dialog_carno;
    private Dialog dialog_displacement;
    private Dialog dialog_drive;
    private Dialog dialog_fuel;
    private Dialog dialog_operation;
    private Dialog dialog_date;
    private LoadDialog dialog_loding;

    private Inquiry carInfo;
    private CommenAdapter<InquiryPart> adapter;

    private int type;

    private boolean isHidden = true;

    private String[] title;

    private LoadDialog loadDialog;
    private InquiryFragment inquiryFragment;
    private InquiryHistroyFragment inquiryHistroyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);
        ButterKnife.bind(this);
        init();
    }

    protected void init() {
        initTab();
    }

    private void initTab() {
     title = new String[]{"询价", "询价历史"};

        inquiryFragment = new InquiryFragment();
        inquiryHistroyFragment = new InquiryHistroyFragment();
        final BaseFragment[] fragments = new BaseFragment[]{inquiryFragment,inquiryHistroyFragment};

        tabview.setTitle(title);
        tabview.setFragments(fragments);

        tabview.setPageChage(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

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
        if (inquiryFragment.onKeyDown(keyCode,event) || inquiryHistroyFragment.onKeyDown(keyCode,event)){
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void change(int position, int count) {
        tabview.getTab(position).setText(title[position]+"("+count+")");
    }
}
