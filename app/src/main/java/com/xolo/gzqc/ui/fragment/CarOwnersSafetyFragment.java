package com.xolo.gzqc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.ui.activity.CoPasswrodActivity;
import com.xolo.gzqc.ui.activity.RoleActivity;
import com.xolo.gzqc.ui.view.CoSafetyCompanyView;
import com.xolo.gzqc.ui.view.CompanyPhoneView;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import io.rong.imageloader.utils.L;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CarOwnersSafetyFragment extends LazyFragment {
    Button carowner_pwssword_bt,carowner_ohter_bt;//修改密码 切换角色
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view= inflater.inflate(R.layout.fragment_carwners_safety,null);
            initView(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void loadData() {
        if(needLoad()){
            mHasLoadedOnce=true;
        }
    }

    void initView(View view) {
        //co_server_layout=(LinearLayout) view.findViewById(R.id.co_server_layout);
        carowner_ohter_bt=(Button)view.findViewById(R.id.carowner_ohter_bt);
        carowner_ohter_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RoleActivity.class));
                getActivity().finish();
            }
        });
        carowner_pwssword_bt=(Button) view.findViewById(R.id.carowner_pwssword_bt);




        carowner_pwssword_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CoPasswrodActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void init() {

    }

}
