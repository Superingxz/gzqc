package com.xolo.gzqc.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.xolo.gzqc.BaseFragment;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.WorkbenchExAdapter;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.WordBeachDealed;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.TabChangeListener;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 待办事项 - 工作台
 */
public class DealedThingFragment extends BaseFragment{

    private ListView lv;

    private List<CarInfo> list_info = new ArrayList<>();
    private CommenAdapter<CarInfo> adapter;

    private ExpandableListView expandableListView;

    private List<WordBeachDealed>  list_dealed = new ArrayList<>();
    private WorkbenchExAdapter workbenchExAdapter;


    private TabChangeListener tabchagelistener;

    @Override
    public void onAttach(Context context) {
        tabchagelistener = (TabChangeListener)context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        expandableListView = new ExpandableListView(mContent);
        expandableListView.setBackgroundColor(ContextCompat.getColor(mContent,R.color.activity_bg));

        lv = new ListView(mContext);
        lv.setDivider(null);
        lv.setBackgroundColor(getResources().getColor(R.color.activity_bg));

        return expandableListView;
    }

    @Override
    protected void init() {
        initLv();
    }

    @Override
    public void load() {
        getrecentalreadymatter();
    }

    private void initLv() {
        workbenchExAdapter = new WorkbenchExAdapter(mContent,list_dealed);
        expandableListView.setAdapter(workbenchExAdapter);

//        只能打开一个
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = expandableListView.getExpandableListAdapter().getGroupCount();
                for(int j = 0; j < count; j++){
                    if(j != groupPosition){
                        expandableListView.collapseGroup(j);
                    }
                }
            }
        });
    }


    /**
     *       2-2 工作台-接车员工作台最近3天已办事项接口 getrecentalreadymatter(userid,dept_id)
     当前用户ID:userid,维修厂ID：dept_id
     */
    private  void getrecentalreadymatter(){
        RequestParams params = creatParams("getrecentalreadymatter");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContent, params, ORMBean.WordBeachDealedBean.class, new HttpUtil.HttpCallBack<ORMBean.WordBeachDealedBean>() {
            @Override
            public void onSuccess(ORMBean.WordBeachDealedBean result) {
                List<WordBeachDealed> data = result.getData();

                list_dealed.clear();
                tabchagelistener.change(1,data.size());
                list_dealed.addAll(data);
                workbenchExAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_dealed.clear();
                workbenchExAdapter.notifyDataSetChanged();
            }
        });

    }

}
