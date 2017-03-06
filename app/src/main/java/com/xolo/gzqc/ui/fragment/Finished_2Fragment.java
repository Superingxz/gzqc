package com.xolo.gzqc.ui.fragment;

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
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.CompleteDetailActivity;
import com.xolo.gzqc.ui.activity.team.CompleteDetail_2Activity;
import com.xolo.gzqc.ui.activity.pickcar.VerhaulActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 已完工-完工
 */
public class Finished_2Fragment extends BaseFragment{
    @BindView(R.id.lv)
    ListView lv;

    private List<CarInfo> list_info = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;


    /**
     * 1 接车员  2班组首页
     */
    private  int type;

    public Finished_2Fragment() {
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
        type  = getArguments().getInt("type",0);

        initLv();
        if (this.type ==1){
            getlistwaitreturncar();
        }
    }


    @Override
    public void load() {
        super.load();
        getlistwaitreturncar();
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_maintenance_list, mContext, list_info, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                CarInfo carInfo = list_info.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "车型：" + carInfo.getBrands() + "  " + carInfo.getTypecode());
                holder.setText(R.id.item4, "来车时间： " + carInfo.getIn_time());
                holder.loadUrl(R.id.iv,carInfo.getBrands_path());
                holder.getView(R.id.item3).setVisibility(View.GONE);
                holder.getView(R.id.item5).setVisibility(View.GONE);
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarInfo carInfo = list_info.get(position);
                if(type == 1){

                    if (!carInfo.getStatus().equals("拆检中")){
                        Intent intent = new Intent(mContext, CompleteDetail_2Activity.class);
                        intent.putExtra(Key.OBJECT,carInfo);
                        intent.putExtra("type",1);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContext, VerhaulActivity.class);
                        intent.putExtra(Key.CARNO,carInfo.getCarno());
                        intent.putExtra("type",2);
                        startActivity(intent);
                    }

                }else {
                    Intent intent = new Intent(mContext, CompleteDetailActivity.class);
                    intent.putExtra(Key.OBJECT,carInfo);
                    intent.putExtra("type",2);
                    startActivity(intent);
                }

            }
        });

    }


    /**
     * 5-1 本维修厂未完工车辆列表接口,如果是我的班组，则需要传班组ID。接车员的完工模块的待完工车辆也使用此接口。 getlistunfinishcar(userid,dept_id,team_id)
     当前用户ID：userid,维修厂ID:dept_id,班组ID（有则为查我的班组）：team_id
     */
    /**
     * 5-2 本维修厂最近3天已完工车辆接口,接车员的完工模块的最近3天已完工单也用此接口 getlistfinishcar(userid,dept_id)
     当前用户ID：userid,维修厂ID:dept_id
     */
    private void   getlistwaitreturncar(){
        RequestParams params =null;
        if (type == 1) {
            params = creatParams("getlistunfinishcar");
        }else {
            params = creatParams("getlistfinishcar");
        }
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CarInfoBean result) {
                List<CarInfo> data = result.getData();
                list_info.clear();
                list_info.addAll(data);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_info.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }
}
