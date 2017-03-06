package com.xolo.gzqc.ui.activity.team;

import android.app.Dialog;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.postJson.PartVerhaul;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ExScrollView;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.DataPopUtil;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 历史维修
 */
public class MaintenanceHistroyActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.car_nunber)
    EditText carNunber;
    @BindView(R.id.brand)
    EditText brand;
    @BindView(R.id.type)
    EditText type;
    @BindView(R.id.date_start)
    TextView dateStart;
    @BindView(R.id.date_end)
    TextView dateEnd;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.part)
    EditText part;
    @BindView(R.id.part_name)
    TextView partName;
    @BindView(R.id.esv)
    ExScrollView esv;
    @BindView(R.id.btn_return_top)
    ImageView btnReturnTop;
    @BindView(R.id.keyboard_view)
    KeyboardView keyboardView;

    private int typeCode;

    private List<PartVerhaul> list_part = new ArrayList<>();
    private CommenAdapter<PartVerhaul> adapter;

    private Dialog dialog_start;
    private Dialog dialog_end;
    private LicenseKeyboardUtil licenseKeyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_histroy);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        brand.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                brand.setFocusable(true);
                brand.setFocusableInTouchMode(true);
                brand.setClickable(false);
                return true;
            }
        });
        licenseKeyboardUtil = new LicenseKeyboardUtil(mContext,carNunber,keyboardView);
        esv.setTopReturn(btnReturnTop);

        initTitle();
        initLv();
        initDialog();
        initIntent();
    }

    private void initIntent() {
        typeCode = getIntent().getIntExtra("type", 0);

        String no = getIntent().getStringExtra(Key.CARNO);

        if (!TextUtils.isEmpty(no)) {
            carNunber.setText(no);
            getlisthistoryquotedpriced();
        }
    }

    private void initDialog() {
        dialog_start = DataPopUtil.creatStartDateDialog(mContext,dateStart);
        dialog_end = DataPopUtil.creatEndDateDialog(mContext,dateEnd);
    }


    private void initTitle() {
        titleview.setTitle("历史维修查询");
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlisthistoryquotedpriced();
            }
        });
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                PartVerhaul carInfo = list_part.get(position);

                holder.setText(R.id.item2, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item1, "时间：" + carInfo.getIn_time());
                holder.setText(R.id.item3, "维修项目： " + carInfo.getItemt_name());
                holder.setText(R.id.item4, "单价： " + carInfo.getWorkamt());
                holder.loadUrl(R.id.iv, carInfo.getBrands_path());

                if (typeCode == 1) {
                    holder.getView(R.id.item5).setVisibility(View.VISIBLE);
                    holder.setText(R.id.item5, "数量： " + carInfo.getQty());
                }

            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PartVerhaul partVerhaul = list_part.get(position);

                Intent intent = new Intent(mContext, MaintenanceList_3Activity.class);
                intent.putExtra("id", partVerhaul.getBf_receive_id());
                startActivity(intent);
            }
        });
    }


    /**
     * 5-3 本维修厂的历史维修查询接口 getlisthistoryquotedpriced(userid,dept_id,start_date,end_date,carno,brands,typecode,
     * output_id,productyear,project_name)
     * 当前用户ID：userid,维修厂ID:dept_id，维修开始时间:start_date,维修结束时间:end_date,车牌号:carno,品牌:brands,车型:typecode,
     * 排量ID:output_id,生产年份:productyear,维修项目名称：project_name
     */
    private void getlisthistoryquotedpriced() {
        RequestParams params = creatParams("getlisthistoryquotedpriced");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("start_date", getText(dateStart));
        params.addBodyParameter("end_date", getText(dateEnd));
        params.addBodyParameter("carno", getText(carNunber));
        params.addBodyParameter("brands", getText(brand));
        params.addBodyParameter("typecode", getText(type));
        params.addBodyParameter("project_name", getText(part));

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.PartVerhaulBean.class, new HttpUtil.HttpCallBack<ORMBean.PartVerhaulBean>() {
            @Override
            public void onSuccess(ORMBean.PartVerhaulBean result) {
                List<PartVerhaul> data = result.getData();
                list_part.clear();
                list_part.addAll(data);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                list_part.clear();
                adapter.notifyDataSetChanged();
            }
        });
    }


    @OnClick({R.id.date_start, R.id.date_end})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_start:
                dialog_start.show();
                break;
            case R.id.date_end:
                dialog_end.show();
                break;
        }
    }
}
