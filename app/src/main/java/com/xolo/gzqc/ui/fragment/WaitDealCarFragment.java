package com.xolo.gzqc.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Ordermbuyparts;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.BookingDetailsActivity;
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
 * 待处理预约车辆 - 预约处理
 */
public class WaitDealCarFragment extends BaseFragment {


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



    public WaitDealCarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wait_deal_car, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        initLv();
        listwaitdealorderm();
    }

    @Override
    public void load() {
        super.load();
        listwaitdealorderm();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Ordermbuyparts ordermbuyparts = list.get(position);
                String is_pay = ordermbuyparts.getIs_pay();

                holder.setText(R.id.item1,"姓名电话："+ordermbuyparts.getName()+"/"+ordermbuyparts.getPhone());
                holder.setText(R.id.item2,"车牌号："+ordermbuyparts.getCarno());
                holder.setText(R.id.item3,"预约时间："+ordermbuyparts.getOperatdate());
                holder.setText(R.id.item4,"到厂时间："+ordermbuyparts.getPlan_com_time());
                holder.setText(R.id.item8,"接车员："+ordermbuyparts.getOperatoname());
                holder.getView(R.id.item8).setVisibility(View.VISIBLE);

                holder.loadUrl(R.id.iv,ordermbuyparts.getBrands_path());

                holder.getView(R.id.item5).setVisibility(View.VISIBLE);
                if (is_pay.equals("1")) {
                    holder.setText(R.id.item5, "购件付款：" + "已付款");
                } else {
                    holder.setText(R.id.item5, "购件付款：" + "未付款");
                }

            }
        });
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ordermbuyparts ordermbuyparts = list.get(position);
                Intent intent = new Intent(mContent, BookingDetailsActivity.class);
                intent.putExtra(Key.OBJECT,ordermbuyparts);
                startActivity(intent);
            }
        });
    }



    /**
     *   2-11 本维修厂所有的待处理预约车辆接口 listwaitdealorderm(userid,dept_id) 当前用户ID：userid,维修厂id：dept_id
     */
    private void  listwaitdealorderm(){
        RequestParams params = creatParams("listwaitdealorderm");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.OrdermbuypartsBean.class, new HttpUtil.HttpCallBack<ORMBean.OrdermbuypartsBean>() {
            @Override
            public void onSuccess(ORMBean.OrdermbuypartsBean result) {
                List<Ordermbuyparts> data = result.getData();

                list.clear();
                tabchagelistener.change(0,data.size());
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
