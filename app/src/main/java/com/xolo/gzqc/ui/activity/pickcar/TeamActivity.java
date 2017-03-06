package com.xolo.gzqc.ui.activity.pickcar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Team;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.TitleView;
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
 * 班组情况
 */
public class TeamActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.lv)
    ListView lv;

    private List<Team>  list_team = new ArrayList<>();
    private List<List<CarInfo>>  list_car = new ArrayList<>();
    private CommenAdapter<Team> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initTitle();
        initLv();
        getlistfactoryteam();
    }

    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlistfactoryteam();
            }
        });
    }

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_team, mContext, list_team, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                          holder.setText(R.id.item1,list_team.get(position).getTeam_name());

                final List<CarInfo> carInfos = list_car.get(position);
                ListView itemLv = (ListView) holder.getView(R.id.item2);
                itemLv.setAdapter(new CommenAdapter<CarInfo>(R.layout.item_info_2, mContext, carInfos, new CommenAdapter.AdapterCallback() {
                    @Override
                    public void setView(ViewHolder holder, int position) {
                        CarInfo carInfo = carInfos.get(position);
                        holder.loadUrl(R.id.iv,carInfo.getBrands_path());
                        holder.setText(R.id.item1,"车牌号："+carInfo.getCarno());
                        holder.setText(R.id.item2,"车型："+carInfo.getBrands()+"  "+carInfo.getTypecode());
                        holder.setText(R.id.item3,"当前状态："+carInfo.getStatus());
                        holder.getView(R.id.item4).setVisibility(View.GONE);
                    }
                }));
            }
        });

        lv.setAdapter(adapter);

//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                new AlertDialog.Builder(mContext).setMessage("是否删除").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        list_team.remove(position);
//                        list_car.remove(position);
//                        adapter.notifyDataSetChanged();
//                    }
//                }).show();
//
//                return true;
//            }
//        });
//
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                ToastUtil.showShort(mContext,"123");
//            }
//        });


    }


    /**
     *    2-59 获取本维修厂对应的班组列表接口 getlistfactoryteam(userid,dept_id)
     当前用户ID:userid,维修厂ID:dept_id
     */
    private  void getlistfactoryteam(){
        RequestParams params = creatParams("getlistfactoryteam");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());


        HttpUtil.getInstance().postLoading(mContext,params, ORMBean.TeamBean.class, new HttpUtil.HttpCallBack<ORMBean.TeamBean>() {
            @Override

            public void onSuccess(ORMBean.TeamBean result) {

                    List<Team> data = result.getData();

                   list_team.clear();
                   list_car.clear();
                   list_team.addAll(data);

                for (int i = 0; i < data.size(); i++) {
                    Team team = data.get(i);
                    String team_id = team.getTeam_id();

                    ArrayList<CarInfo> carInfos = new ArrayList<>();
                    list_car.add(carInfos);

                    getlistrepaircarbyteamid(i,team_id);
                }


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }


    /**
     * 2-60 根据班组ID获取该班组对应的维修车辆列表接口 getlistrepaircarbyteamid(userid,team_id)
     当前用户ID:userid,班组ID:team_id
     */
    private  void getlistrepaircarbyteamid(final int position, String teamID){
        RequestParams params = creatParams("getlistrepaircarbyteamid");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("team_id",teamID);

        HttpUtil.getInstance().post(params, ORMBean.CarInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CarInfoBean>() {
            @Override

            public void onSuccess(ORMBean.CarInfoBean result) {

                if (result.getRes().equals("1")){
                    List<CarInfo> data = result.getData();
                    List<CarInfo> carInfos = list_car.get(position);
                    carInfos.addAll(data);

                    adapter.notifyDataSetChanged();
                }
                else{
                    ToastUtil.showShort(mContext,result.getMsg());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
        });
    }

}
