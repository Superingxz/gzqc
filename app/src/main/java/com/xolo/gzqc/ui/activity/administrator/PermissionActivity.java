package com.xolo.gzqc.ui.activity.administrator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.child.Employee;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.ui.fragment.DataPerssionFragment;
import com.xolo.gzqc.ui.fragment.RolePermissionFragment;
import com.xolo.gzqc.ui.view.TabView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;

public class PermissionActivity extends BaseActivity {

    @BindView(R.id.mTabview)
    TabView mTabview;

    private int currentPosition;

    private Employee employee;
    private BaseFragment[] fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
        initIntent();
        initTab();
    }

    private void initIntent() {
        employee = (Employee) getIntent().getSerializableExtra(IntentConstant.EMPLOYEE);
    }

    @OnClick(R.id.btn_add)
    public void onClick() {
        Intent intent = null;
        if (currentPosition == 0){
             intent = new Intent(mContext, AddRolePermissionActivity.class);
        }else {
            intent = new Intent(mContext, AddDataPermissionActivity.class);
        }
        intent.putExtra(IntentConstant.EMPLOYEE,employee);
        startActivityForResult(intent,99);
    }

    private void initTab() {
        mTabview.setTabMode(MODE_SCROLLABLE);

    mTabview.setTitle(new String[]{"所属角色","数据权限"});
         fragments = new BaseFragment[]{new RolePermissionFragment(), new DataPerssionFragment()};
        mTabview.setFragments(fragments);
        mTabview.setPage(2);

        mTabview.setPageChage(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
               fragments[position].load();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            fragments[currentPosition].load();
        }
    }
}
