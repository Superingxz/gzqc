package com.xolo.gzqc.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.ReceiveInfo;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.bean.postJson.Repairlist;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 配件清单 - 报价
 */
public class PartListFragment extends BaseFragment {


    @BindView(R.id.lv)
    ListView lv;
    private CommenAdapter<Part> adapter;

    private ReceiveInfo receiveInfo;

    public PartListFragment() {
        // Required empty public constructor
    }

    private LoadDialog  loadDialog;

    private List<Part> repairlists = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_part_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
           initLv();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_part_offer, mContent, repairlists, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Part repairlist = repairlists.get(position);
                holder.setText(R.id.item1,repairlist.getParts_name());
                holder.setText(R.id.item2,repairlist.getSaleprice());
                holder.setText(R.id.item3,"x "+repairlist.getQty()+" ( "+repairlist.getQty_unit()+" )");
                holder.setText(R.id.item4,repairlist.getSource());

                double total = Double.parseDouble(repairlist.getSaleprice()) * Integer.parseInt(repairlist.getQty());
                holder.setText(R.id.item5,"￥"+String.valueOf(total));
            }
        });

        lv.setAdapter(adapter);
    }




/**
 * 2-55 报价接口--根据接车表id查询出所有的报价配件清单 quoterepariorderm,bf_receive_car_id,is_deleted)
 当前用户ID:userid,接车表ID:bf_receive_car_id,是否报废：is_deleted
 */
    private void  quoterepariorderm(){
        if (receiveInfo == null){
            ToastUtil.showShort(mContent,"请选择车辆");
            return;
        }
        user = SPManager.getUser(mContext);
        RequestParams params = creatParams("quoterepariorderm");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("bf_receive_car_id",receiveInfo.getBf_receive_id());
        params.addBodyParameter("is_deleted","0");
        params.addBodyParameter("owen_id",user.getUser_id());

        loadDialog.show(mContent);

        HttpUtil.getInstance().post( params, ORMBean.PartBean.class, new HttpUtil.HttpCallBack<ORMBean.PartBean>() {
            @Override
            public void onSuccess(ORMBean.PartBean result) {
                repairlists.clear();
                if (result.getRes().equals("1")){
                    repairlists.addAll(result.getData());
                }
                else{
                    ToastUtil.showShort(mContext,result.getMsg());
                }
                adapter.notifyDataSetChanged();
                loadDialog.dismiss(mContent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
               loadDialog.dismiss(mContent);
            }
        });
    }


    public void setReceiveInfo(ReceiveInfo receiveInfo) {
        this.receiveInfo = receiveInfo;
    }

//    @Override
//    public void load() {
//        super.load();
//        quoterepariorderm();
//    }

    public void  add(List<Part> list){
        repairlists.addAll(list);
        adapter.notifyDataSetChanged();
    }


    public void  clear(){
        repairlists.clear();
        adapter.notifyDataSetChanged();
    }

}
