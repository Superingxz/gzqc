package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约进度查 - 构件预约
 */
public class SearchAppointmentFragment extends BaseFragment {


    @BindView(R.id.lv)
    ListView lv;

    private List<Ordermbuyparts> list = new ArrayList<>();
    private CommenAdapter<Ordermbuyparts> adapter;

    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tabchagelistener = ((TabChangeListener) context);
    }




    public SearchAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_appointment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }



    @Override
    protected void init() {
        initLv();
    }


    @Override
    public void load() {
        super.load();
        if (!isLoaded){
            listordermbuyparts();
        }
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Ordermbuyparts ordermbuyparts = list.get(position);

                holder.setText(R.id.item1,"姓名电话："+ordermbuyparts.getName()+"/"+ordermbuyparts.getPhone());
                 holder.setText(R.id.item2,"车牌号："+ordermbuyparts.getCarno());
                   holder.setText(R.id.item3,"预约时间："+ordermbuyparts.getOperatdate());
                  holder.setText(R.id.item4,"到厂时间："+ordermbuyparts.getPlan_com_time());
                holder.setText(R.id.item5, "接车员：" + ordermbuyparts.getOperatoname());
                holder.setVisibility(R.id.item5, View.VISIBLE);


                holder.loadUrl(R.id.iv,ordermbuyparts.getBrands_path());

            }
        });
        lv.setAdapter(adapter);
    }


    /**
 *    2-10 A 本维修厂所有的预约购件进度查询接口 listordermbuyparts(userid,dept_id) 当前用户ID：userid,维修厂id：dept_id
 */
    private void  listordermbuyparts(){
        RequestParams params = creatParams("listordermbuyparts");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.OrdermbuypartsBean.class, new HttpUtil.HttpCallBack<ORMBean.OrdermbuypartsBean>() {
            @Override
            public void onSuccess(ORMBean.OrdermbuypartsBean result) {
                List<Ordermbuyparts> data = result.getData();

                list.clear();
                tabchagelistener.change(1,data.size());
                list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }

}
