package com.xolo.gzqc.ui.activity.procurement;

import android.app.Dialog;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.LicenseKeyboardUtil;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Brand;
import com.xolo.gzqc.bean.child.TypeCode;
import com.xolo.gzqc.bean.postJson.PartVerhaul;
import com.xolo.gzqc.configuration.IntentConstant;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.CarBrandActivity;
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
 * 历史报价
 */
public class QueryOfferActivity extends BaseActivity {

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
    @BindView(R.id.btn_return_top)
    ImageView btnReturnTop;
    @BindView(R.id.esv)
    ExScrollView esv;
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
        setContentView(R.layout.activity_query_offer);
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

            getlisthistoryquerypriced();
        }
    }

    private void initDialog() {
        dialog_start = DataPopUtil.creatStartDateDialog(mContext,dateStart);
        dialog_end = DataPopUtil.creatEndDateDialog(mContext,dateEnd);
    }


    private void initTitle() {
        titleview.setRightText("查询");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlisthistoryquerypriced();
            }
        });
    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_info_1, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                PartVerhaul carInfo = list_part.get(position);

                holder.setText(R.id.item1, "车牌号：" + carInfo.getCarno());
                holder.setText(R.id.item2, "时间：" + carInfo.getOperatdate());
                holder.setText(R.id.item3, "配件： " + carInfo.getParts_name());
                holder.setText(R.id.item4, "单价： " + carInfo.getPrice());
                holder.loadUrl(R.id.iv, carInfo.getBrands_path());


            }
        });

        lv.setAdapter(adapter);
    }


    /**
     * 4-5 本维修厂的历史报价查询（指回复询价）接口 getlisthistoryquerypriced(userid,dept_id,start_date,end_date,carno,
     * brands,typecode,output_id,productyear,parts_name)
     * 当前用户ID：userid,维修厂ID:dept_id,给接车员报价开始时间:start_date,给接车员报价结束时间:end_date,车牌号:carno,品牌:brands,
     * 车型:typecode,排量ID:output_id,生产年份:productyear,配件名称:parts_name
     */
    private void getlisthistoryquerypriced() {
        RequestParams params = creatParams("getlisthistoryquerypriced");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());
        params.addBodyParameter("start_date", getText(dateStart));
        params.addBodyParameter("end_date", getText(dateEnd));
        params.addBodyParameter("carno", getText(carNunber));
        params.addBodyParameter("brands", getText(brand));
        params.addBodyParameter("typecode", getText(type));
        params.addBodyParameter("parts_name", getText(part));

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


    @OnClick({R.id.date_start, R.id.date_end, R.id.brand})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_start:
                dialog_start.show();
                break;
            case R.id.date_end:
                dialog_end.show();
                break;
            case R.id.brand:
                if (!brand.isFocusableInTouchMode()) {
                    startActivityForResult(new Intent(mContext, CarBrandActivity.class), CarBrandActivity.GET_BRAND);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CarBrandActivity.GET_BRAND && resultCode == CarBrandActivity.RETURN_BRAND) {
            Brand brand_rt = (Brand) data.getSerializableExtra(IntentConstant.BRAND);
            TypeCode typeCode_rt = (TypeCode) data.getSerializableExtra(IntentConstant.TYPE_CODE);
            brand.setText(brand_rt.getBrands());
            type.setText(typeCode_rt.getTypecode());
        }
    }

}
