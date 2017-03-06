package com.xolo.gzqc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xolo.gzqc.R;
import com.xolo.gzqc.ui.view.TabView;
import com.xolo.gzqc.ui.view.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Administrator on 2016/9/26.
 */
public class CarOwnerCenterFragment extends  LazyFragment {
    TabView tabview_info;
    private Fragment[] fragments ;
    private String[] title ;
     static   CarOwnerCenterFragment fragment;
    TitleView title_view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carowerscenter,null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        title_view=(TitleView) view.findViewById(R.id.title_view);
        tabview_info=(TabView)view.findViewById(R.id.tabview_info);
        title_view.setLeftGone();
        title= getResources().getStringArray(R.array.caronerinfo);
        fragments = new Fragment[]{new CarOnwerInfoFragment(),new CarOwnersSafetyFragment(),new CarOwners_CarInfoFragment(),new CarOwners_SendinfoFragment()};
        tabview_info.setTitle(title);
        tabview_info.setFragments(fragments);
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    protected void loadData() {

    }
    public static CarOwnerCenterFragment newInstance() {
        if (fragment == null) {
            fragment = new CarOwnerCenterFragment();
        }
        return fragment;
    }
    @Override
    protected void init() {
    }
}
