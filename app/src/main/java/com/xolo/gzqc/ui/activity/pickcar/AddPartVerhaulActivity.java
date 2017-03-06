package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.bean.child.ItemVerhaul;
import com.xolo.gzqc.bean.child.Itemt;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 拆检添加配件
 */

public class AddPartVerhaulActivity extends BaseActivity {


    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.team)
    TextView team;
    @BindView(R.id.btn_add)
    ImageView btnAdd;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.confirm)
    Button confirm;

    private boolean isEdit;

    private Control control;

    private Dialog dialog;

    private List<Control> list = new ArrayList<>();
    private List<Itemt> list_itemt = new ArrayList<>();
    private List<ItemVerhaul.PartsBean> list_itemVerhaul = new ArrayList<>();

    private Dialog dialog_itemt;

    private Itemt itemt;
    private CommenAdapter<ItemVerhaul.PartsBean> itemVerhaulCommenAdapter;

    private  int position_select ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_part_verhaul);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        initLv();

        Intent intent = getIntent();

        ItemVerhaul part = (ItemVerhaul) intent.getSerializableExtra(Key.OBJECT);

        if (part != null) {
            isEdit = true;

            et1.setText(part.getItemt_name());
            itemt = new Itemt(part.getItemt_name(), part.getItemt_id());

                team.setText(part.getTeam_name());
                control = new Control(part.getTeam_id(), part.getTeam_name());

            if (part.getParts()!=null&&part.getParts().size()>0){
                list_itemVerhaul.addAll(part.getParts());
                itemVerhaulCommenAdapter.notifyDataSetChanged();
            }
        }

        initDialog();

    }

    private void initLv() {
        itemVerhaulCommenAdapter = new CommenAdapter<>(R.layout.item_car, mContext, list_itemVerhaul, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                ItemVerhaul.PartsBean partsBean = list_itemVerhaul.get(position);
                holder.setText(R.id.item1,partsBean.getParts_name());
                holder.setText(R.id.item2,partsBean.getQty()+"("+partsBean.getQty_unit()+")");
            }
        });

        lv.setAdapter(itemVerhaulCommenAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position_select = position;

                ItemVerhaul.PartsBean partsBean = list_itemVerhaul.get(position);
                Intent intent = new Intent(mContext, AddPart3.class);
                intent.putExtra(Key.OBJECT,partsBean);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void initDialog() {
        dialog = creatListDialog("", list, team, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control1) {
                control = control1;
            }
        });

        dialog_itemt = creatListLeftDialog("", list_itemt, et1, new ListDialogCallBack<Itemt>() {
            @Override
            public String setText(Itemt itemt) {
                return itemt.getItemt_name();
            }

            @Override
            public void onClick(Itemt itemt1) {
                itemt = itemt1;
            }
        });
    }

    /**
     * 根据类别获取对应的数据字典接口。1为燃料，2为操控，3为驱动，4为排量 ,5为油量
     *
     * @param type
     */
    private void getcontrolbytype(final String type) {
        RequestParams params = creatParams("getcontrolbytype");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("type", type);
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
            @Override
            public void onSuccess(ORMBean.ControlBean result) {
                List<Control> data = result.getData();
                list.addAll(data);
                dialog.show();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @OnClick({R.id.btn_add, R.id.confirm, R.id.team, R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                startActivityForResult(new Intent(mContext,AddPart3.class),REQUEST_CODE);
                break;
            case R.id.confirm:
                if (control == null) {
                    ToastUtil.showShort(mContext, "请选择班组");
                    return;
                }
                if (TextUtils.isEmpty(getText(et1))) {
                    ToastUtil.showShort(mContext, "请选择维修项目");
                    return;
                }
                ItemVerhaul itemVerhaul = new ItemVerhaul(getText(et1), getText(team), "",control.getControl_id(), list_itemVerhaul);
                Intent intent = new Intent();
                if (itemt != null){
                    itemVerhaul.setItemt_id(itemt.getBf_repair_item_id());
                }
                intent.putExtra(Key.OBJECT,itemVerhaul);
                if (isEdit){
                    setResult(2,intent);
                }else
                setResult(RESULT_OK,intent);
                if (itemVerhaul.getParts() == null){
                    LogUtil.i("addverhaul == null");
                }
                finish();
                break;
            case R.id.team:
                if (list.size() > 0) {
                    dialog.show();
                } else {
                    getcontrolbytype("8");
                }
                break;
            case R.id.select:
                getlistrepairitemt();
                break;
        }
    }

    /**
     * 2-88 模糊查询获取本维修厂维修项目列表接口,用于报价、询价、拆检模块的维修项目录入。 getlistrepairitemt(userid,itemt_name,dept_id)
     * 当前用户ID:userid,维修项目名称：itemt_name,维修厂ID:dept_id
     */
    private void getlistrepairitemt() {
        RequestParams params = creatParams("getlistrepairitemt");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("itemt_name", TextUtils.isEmpty(getText(et1)) ? "" : getText(et1));
        params.addBodyParameter(Key.DEPT_ID, user.getDept_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ItemtBean.class, new HttpUtil.HttpCallBack<ORMBean.ItemtBean>() {
            @Override
            public void onSuccess(ORMBean.ItemtBean result) {
                List<Itemt> data = result.getData();
                list_itemt.clear();
                list_itemt.addAll(data);
                dialog_itemt.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE ){
            if (resultCode == RESULT_OK){
                ItemVerhaul.PartsBean  partsBean  = (ItemVerhaul.PartsBean) data.getSerializableExtra(Key.OBJECT);
                list_itemVerhaul.add(partsBean);
            }else  if(resultCode == 2){
                ItemVerhaul.PartsBean  partsBean  = (ItemVerhaul.PartsBean) data.getSerializableExtra(Key.OBJECT);
                list_itemVerhaul.remove(position_select);
                list_itemVerhaul.add(position_select,partsBean);
            }
            itemVerhaulCommenAdapter.notifyDataSetChanged();
        }
    }
}
