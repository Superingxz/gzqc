package com.xolo.gzqc.ui.fragment;


import android.app.Activity;
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
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.ArrivalGoodsActivity;
import com.xolo.gzqc.ui.activity.pickcar.InquiryActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 待办事项-采购主页.
 */
public class ProcurementDealingFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;



    private List<Ordermbuyparts> list = new ArrayList<>();
    private CommenAdapter<Ordermbuyparts> adapter;

    public ProcurementDealingFragment() {
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
        getbuyermytodo();
    }

    @Override
    public void load() {
        super.load();
            getbuyermytodo();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Ordermbuyparts ordermbuyparts = list.get(position);
                String is_pay = ordermbuyparts.getIs_pay();

                holder.setText(R.id.item1,"车牌号："+ordermbuyparts.getCarno());
                holder.setText(R.id.item2,"车型："+ordermbuyparts.getBrands()+"  "+ordermbuyparts.getTypecode());
                holder.setText(R.id.item3,"时间："+ordermbuyparts.getBuy_time());
                holder.setText(R.id.item4,"状态："+ordermbuyparts.getStatus());
                holder.loadUrl(R.id.iv,ordermbuyparts.getBrands_path());

            }
        });
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ordermbuyparts ordermbuyparts = list.get(position);

                if (ordermbuyparts.getStatus().equals("询价中")){
                    Intent intent = new Intent(mContent, InquiryActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra(IntentConstant.INQUIRY_ID,ordermbuyparts.getBuy_id());
                    startActivityForResult(intent,REQUEST_CODE);
                }else {
                    Intent intent = new Intent(mContent, ArrivalGoodsActivity.class);
                    intent.putExtra("type",1);
                    intent.putExtra("buy_id",ordermbuyparts.getBuy_id());
                    startActivityForResult(intent,REQUEST_CODE);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            getbuyermytodo();
        }
    }

    /**
     *   4-1 采购员首页-我的待办事项接口 getbuyermytodo(userid,dept_id)
     当前用户ID：userid,维修厂ID：dept_id
     */
    private void  getbuyermytodo(){
        RequestParams params = creatParams("getbuyermytodo");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.OrdermbuypartsBean.class, new HttpUtil.HttpCallBack<ORMBean.OrdermbuypartsBean>() {
            @Override
            public void onSuccess(ORMBean.OrdermbuypartsBean result) {
                List<Ordermbuyparts> data = result.getData();

                list.clear();
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
