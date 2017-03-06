package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Itemt;
import com.xolo.gzqc.bean.postJson.Offer;
import com.xolo.gzqc.bean.postJson.Part;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
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
 * 添加报价项目
 */
public class AddOfferItemActivity extends BaseActivity {

    @BindView(R.id.et1)
    EditText et1;
    @BindView(R.id.et2)
    EditText et2;
    @BindView(R.id.lv)
    ScrollListView lv;

    private CommenAdapter<Part> adapter;

    private List<Part> list_part;

    private Offer offer = new Offer();

    private String carno;

    private List<Itemt>  list_itemt = new ArrayList<>();

    private Dialog dialog ;
    private Dialog dialog_control;

    private Itemt itemt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer_item);
        ButterKnife.bind(this);

        Offer offer1 = (Offer) getIntent().getSerializableExtra(Key.OBJECT);
        carno = getIntent().getStringExtra(Key.CARNO);
        if (offer1!=null){
            offer = offer1;
            et1.setText(offer.getItemt_name());
            et2.setText(offer.getWorkamt());
            if (!TextUtils.isEmpty(offer.getItemt_id())){
                itemt = new Itemt(offer.getItemt_name(),offer.getItemt_id());
            }
        }

        init();
    }

    private void init() {
        list_part = offer.getRepairlist();
        initLv();
        initDialog();
    }

    private void initDialog() {
        dialog = creatListLeftDialog("", list_itemt, et1, new ListDialogCallBack<Itemt>() {
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

    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_offer_part, mContext, list_part, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Part part = list_part.get(position);

                holder.setText(R.id.item_1, part.getParts_name());
                holder.setText(R.id.item_2, part.getQty()+part.getQty_unit());
                holder.setText(R.id.item_3, part.getSaleprice());
                holder.setText(R.id.item_4, part.getTotalPrice() + "");
                holder.setText(R.id.item_5, part.getSource());
            }
        });

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, AddofferParts.class);
                intent.putExtra(Key.IS_EDIT, true);
                intent.putExtra(Key.OBJECT, list_part.get(position));
                intent.putExtra(Key.POSITION, position);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Part car = list_part.get(position);
                new AlertDialog.Builder(mContext).setMessage("是否删除").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list_part.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                return true;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Part part = (Part) data.getSerializableExtra(Key.OBJECT);

                list_part.add(part);
                adapter.notifyDataSetChanged();

            } else if (resultCode == 9) {
                Part part = (Part) data.getSerializableExtra(Key.OBJECT);
                int position = data.getIntExtra(Key.POSITION, 0);
                list_part.remove(position);
                list_part.add(position,part);
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.btn_add, R.id.confirm,R.id.select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent1 = new Intent(mContext, AddofferParts.class);
                intent1.putExtra(Key.CARNO,carno);
                startActivityForResult(intent1, REQUEST_CODE);
                break;
            case R.id.confirm:
                String text1 = getText(et1);
                String text2 = getText(et2);

                if (TextUtils.isEmpty(text1)) {
                    ToastUtil.showShort(mContext, "请填写维修项目");
                    return;
                }
                if (TextUtils.isEmpty(text2)) {
                    ToastUtil.showShort(mContext, "请填写工时费");
                    return;
                }

                offer.setItemt_name(text1);
                if (!(itemt == null)){
                    offer.setItemt_id(itemt.getBf_repair_item_id());
                }
                offer.setWorkamt(text2);

                Intent intent = new Intent();
                intent.putExtra(Key.OBJECT, offer);
                setResult(RESULT_OK, intent);

                finish();
                break;
            case  R.id.select:
                getlistrepairitemt();
                break;
        }
    }



    /**
     * 2-88 模糊查询获取本维修厂维修项目列表接口,用于报价、询价、拆检模块的维修项目录入。 getlistrepairitemt(userid,itemt_name,dept_id)
     当前用户ID:userid,维修项目名称：itemt_name,维修厂ID:dept_id
     */
      private  void  getlistrepairitemt(){
          RequestParams params = creatParams("getlistrepairitemt");
          params.addBodyParameter(Key.USER_ID,user.getUser_id());
          params.addBodyParameter("itemt_name",TextUtils.isEmpty(getText(et1))?"":getText(et1));
          params.addBodyParameter(Key.DEPT_ID,user.getDept_id());

          HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ItemtBean.class, new HttpUtil.HttpCallBack<ORMBean.ItemtBean>() {
              @Override
              public void onSuccess(ORMBean.ItemtBean result) {
                  List<Itemt> data = result.getData();
                  list_itemt.clear();
                  list_itemt.addAll(data);
                  dialog.show();
              }

              @Override
              public void onError(Throwable ex, boolean isOnCallback) {

              }
          });
      }


}
