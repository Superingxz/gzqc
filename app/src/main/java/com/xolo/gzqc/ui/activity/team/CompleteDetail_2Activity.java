package com.xolo.gzqc.ui.activity.team;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarInfo;
import com.xolo.gzqc.bean.child.Complete;
import com.xolo.gzqc.bean.postJson.Photo;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.PhotoActivity;
import com.xolo.gzqc.ui.activity.pickcar.FuntionActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollGridView;
import com.xolo.gzqc.ui.view.ScrollListView;
import com.xolo.gzqc.utils.Base64Utils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.LogUtil;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 完工详情
 */
public class CompleteDetail_2Activity extends BaseActivity {

    @BindView(R.id.carno)
    TextView carno;
    @BindView(R.id.brand)
    TextView brand;
    @BindView(R.id.lv)
    ScrollListView lv;
    @BindView(R.id.btn_1)
    Button btn1;
    @BindView(R.id.btn_2)
    Button btn2;
    @BindView(R.id.gv)
    ScrollGridView gv;

    private CarInfo carInfo;

    private List<Complete> list = new ArrayList<>();

    private CommenAdapter<Complete> adapter;

    private Dialog dialog;

    private int position_targer;

    private List<Photo>  urlList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private CommenAdapter<Bitmap> gvAdapter;

    private LoadDialog loadDialog;

    private CommenAdapter<Photo> photoCommenAdapter;

    private PhotoUtils photoUtils;

    /**
     * type:1 班组，已完工
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_list);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        carInfo = (CarInfo) intent.getSerializableExtra(Key.OBJECT);
        type = intent.getIntExtra("type",0);

        if (type == 1){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
        }

        btn1.setText("拍照");
        btn2.setText("发送给接车员");

        carno.setText(carInfo.getCarno());
        brand.setText(carInfo.getBrands() + "  " + carInfo.getTypecode());

LogUtil.i(carInfo.getBrands() + "  " + carInfo.getTypecode());

        initLv();
        initGv();
        initPhotoUtils();

        getlistrepairprojectbyreceiveid();
        if (type == 1 ){
            getlistfinishedphoto();
        }

    }


    private void initPhotoUtils() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                Bitmap bitmap = PhotoUtils.compressFile(uri.getPath(),6);
                bitmapList.add(bitmap);
                gvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }


    private void initGv() {
        if (type == 1 ){
            photoCommenAdapter = new CommenAdapter<>(R.layout.item_photo, mContext, urlList, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    ImageView view = holder.getView(R.id.item1);
                    Glide.with(mContext).load(urlList.get(position).getPhoto_path()).into(view);
                }
            });

            gv.setAdapter(photoCommenAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String photo_path = urlList.get(position).getPhoto_path();

                    if (!TextUtils.isEmpty(photo_path)){
                        Intent intent1 = new Intent(mContext, PhotoActivity.class);
                        intent1.putExtra("url",photo_path);
                        startActivity(intent1);
                    }
                }
            });

        }else {
            gvAdapter = new CommenAdapter<>(R.layout.item_photo, mContext, bitmapList, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    holder.setImage(R.id.item1,bitmapList.get(position));
                }
            });

            gv.setAdapter(gvAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bitmap bitmap =bitmapList.get(position);

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
                    byte[] bytes = bos.toByteArray();

                    Intent intent1 = new Intent(mContext, PhotoActivity.class);
                    intent1.putExtra(Key.OBJECT, bytes);

                    startActivity(intent1);
                }
            });
        }

    }


    private void initLv() {
        adapter = new CommenAdapter<>(R.layout.item_complete, mContext, list, new CommenAdapter.AdapterCallback() {
            @Override
            public void setView(ViewHolder holder, int position) {
                Complete complete1 = list.get(position);

                final Complete complete = complete1;

                holder.setText(R.id.item1, "项目：" + complete.getItemt_name());
                holder.setText(R.id.item2, "班组：" + complete.getTeam_name());

                RadioGroup rg = (RadioGroup) holder.getView(R.id.rg);

                RadioButton rb1 = (RadioButton) holder.getView(R.id.item3);
                RadioButton rb2 = (RadioButton) holder.getView(R.id.item4);
                RadioButton rb3 = (RadioButton) holder.getView(R.id.item5);

                final TextView tv = (TextView) holder.getView(R.id.item6);
                tv.setText( "操作人：" + complete1.getUser_name());

                if (type !=1){
                    rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                complete.setIs_preparing("1");
                                complete.setIs_repairing("0");
                                complete.setIs_completed("0");
                            }
                        }
                    });
                    rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                complete.setIs_preparing("0");
                                complete.setIs_repairing("1");
                                complete.setIs_completed("0");
                            }
                        }
                    });
                    rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                complete.setIs_preparing("0");
                                complete.setIs_repairing("0");
                                complete.setIs_completed("1");
                            }
                        }
                    });
                }else {
                    rb1.setClickable(false);
                    rb2.setClickable(false);
                    rb3.setClickable(false);
                }

                rg.clearCheck();

                if (complete.getIs_preparing().equals("1")) {
                    rb1.setChecked(true);
                }

                if (complete.getIs_repairing().equals("1")) {
                    rb2.setChecked(true);
                }


                if (complete.getIs_completed().equals("1")) {
                    rb3.setChecked(true);
                }

            }
        });

        lv.setAdapter(adapter);
    }

    @OnClick({R.id.btn_1, R.id.btn_2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                photoUtils.takePicture(mContext);
                break;
            case R.id.btn_2:
                if(type==3){
                    setcompleted();
                }else
                sendscheduletoclient();
                break;
        }
    }


    /**
     * 2-57 根据接车单ID获取该车对应的维修项目清单接口 getlistrepairprojectbyreceiveid(userid,receiver_id)
     * 当前用户ID:userid,接车单ID:receiver_id
     */
    private void getlistrepairprojectbyreceiveid() {
        RequestParams params = creatParams("getlistrepairprojectbyreceiveid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("receiver_id", carInfo.getBf_receiver_id());
        params.addBodyParameter("team_id",user.getRole_id());

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.CompleteBean.class, new HttpUtil.HttpCallBack<ORMBean.CompleteBean>() {
            @Override
            public void onSuccess(ORMBean.CompleteBean result) {
                List<Complete> data = result.getData();

                list.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoUtils.onActivityResult(mContext,requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     *       5-4 维修厂完工设置接口 setcompleted(userid,receiver_id,json_photo,json_projets,dept_id)
     当前用户ID:userid,接车单ID:receiver_id,照片列表:json_photo,维修项目清单:json_projets,维修厂ID:dept_id
     */
     private   void   setcompleted(){
         StringBuffer  buffer = new StringBuffer("");
         for (int i = 0; i <bitmapList.size() ; i++) {
             Bitmap bitmap = bitmapList.get(i);
             if (i==0){
                 buffer.append(Base64Utils.Bitmap2StrByBase64(bitmap));
             }else {
                 buffer.append(","+Base64Utils.Bitmap2StrByBase64(bitmap));
             }
         }

         RequestParams params = creatParams("setcompleted");
         params.addBodyParameter(Key.USER_ID,user.getUser_id());
         params.addBodyParameter(Key.DEPT_ID,user.getDept_id());
         params.addBodyParameter("receiver_id",carInfo.getBf_receiver_id());
         params.addBodyParameter("json_photo",buffer.toString());
         params.addBodyParameter("json_projets","{\"data\":" + list.toString() + "}");


         HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
             @Override
             public void onSuccess(BaseBean result) {
                 ToastUtil.showShort(mContext,result.getMsg());
                 setResult(RESULT_OK);
                 finish();
             }

             @Override
             public void onError(Throwable ex, boolean isOnCallback) {

             }
         });

     }


    /**
     * 2-58 发送给客户接口 sendscheduletoclient(userid,receiver_id,json_photo,json_projets,dept_id)
     当前用户ID:userid,接车单ID:receiver_id,照片列表:json_photo,维修项目清单:json_projets,维修厂ID:dept_id
     */
    private   void   sendscheduletoclient(){
        StringBuffer  buffer = new StringBuffer("");
        for (int i = 0; i <bitmapList.size() ; i++) {
            Bitmap bitmap = bitmapList.get(i);
            if (i==0){
                buffer.append(Base64Utils.Bitmap2StrByBase64(bitmap));
            }else {
                buffer.append(","+Base64Utils.Bitmap2StrByBase64(bitmap));
            }
        }

        RequestParams params = creatParams("sendscheduletoclient");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter(Key.DEPT_ID,user.getDept_id());
        params.addBodyParameter("receiver_id",carInfo.getBf_receiver_id());
        params.addBodyParameter("json_photo",buffer.toString());
        params.addBodyParameter("json_projets","{\"data\":" + list.toString() + "}");

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext,result.getMsg());
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });

    }


    /**
     *    5-7 根据接车单ID获取该车对应的完工设置照片列表单接口 getlistfinishedphoto(userid,receiver_id)
     当前用户ID:userid,接车单ID:receiver_id
     */
    private  void   getlistfinishedphoto(){
        RequestParams params = creatParams("getlistfinishedphoto");
        params.addBodyParameter(Key.USER_ID,user.getUser_id());
        params.addBodyParameter("receiver_id",carInfo.getBf_receiver_id());

        loadDialog.show(mContext);
        HttpUtil.getInstance().post(params, ORMBean.PhotoBean.class, new HttpUtil.HttpCallBack<ORMBean.PhotoBean>() {
            @Override
            public void onSuccess(ORMBean.PhotoBean result) {
                if (result.getRes().equals("1")){
                    List<Photo> data = result.getData();
                    urlList.addAll(data);
                    photoCommenAdapter.notifyDataSetChanged();
                }
                loadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                loadDialog.dismiss(mContext);
            }
        });
    }

}
