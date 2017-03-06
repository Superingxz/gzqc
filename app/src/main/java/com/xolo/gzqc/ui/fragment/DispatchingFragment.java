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
import com.xolo.gzqc.ui.activity.pickcar.MaintenanceListActivity;
import com.xolo.gzqc.ui.view.TitleView;
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
 * 待派工
 */
public class DispatchingFragment extends BaseFragment {


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

    public DispatchingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispatching, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void init() {
        initLv();
        getlistwaitassignwork();
    }

    @Override
    public void load() {
        super.load();
        getlistwaitassignwork();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContent, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Ordermbuyparts ordermbuyparts = list.get(position);
                String is_pay = ordermbuyparts.getIs_pay();
                holder.setText(R.id.item1, "车牌号：" + ordermbuyparts.getCarno() );
                holder.setText(R.id.item2, "品牌：" + ordermbuyparts.getBrands());
                holder.setText(R.id.item3, "型号：" + ordermbuyparts.getTypecode());
                holder.setText(R.id.item4, "到厂时间：" + ordermbuyparts.getIn_time());
                holder.setText(R.id.item5, "接车员：" + ordermbuyparts.getOperatoname());
                holder.setVisibility(R.id.item5, View.VISIBLE);
                holder.loadUrl(R.id.iv,ordermbuyparts.getBrands_path());
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ordermbuyparts ordermbuyparts = list.get(position);
                Intent intent = new Intent(mContent, MaintenanceListActivity.class);
                intent.putExtra(Key.OBJECT, ordermbuyparts);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取本维修厂的待派工车辆列表接口 getlistwaitassignwork(userid,dept_id)
     当前用户ID:userid,维修厂ID:dept_id
     */
    private void getlistwaitassignwork() {
        RequestParams params = creatParams("getlistwaitassignwork");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

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
