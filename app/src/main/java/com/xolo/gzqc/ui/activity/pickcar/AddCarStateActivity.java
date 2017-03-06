package com.xolo.gzqc.ui.activity.pickcar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.CarState;
import com.xolo.gzqc.bean.child.CarStateRead;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.activity.PhotoActivity;
import com.xolo.gzqc.ui.dialog.BottomMenuDialog;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.Base64Utils;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.adapterUtils.CommenAdapter;
import com.xolo.gzqc.utils.adapterUtils.ViewHolder;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCarStateActivity extends BaseActivity {

    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.gv)
    GridView gv;
    @BindView(R.id.rb_wg)
    RadioButton rbWg;
    @BindView(R.id.rb_ns)
    RadioButton rbNs;
    @BindView(R.id.rg)
    RadioGroup rg;

    private Dialog dialog;
    //选择图片
    private BottomMenuDialog dialog_select_photo;

    private List<Control> list = new ArrayList<>();
    private Control control;

    private int typeCode;

    private PhotoUtils photoUtils;

    Bitmap bitmap;

    private boolean is_edit;


    private List<CarStateRead.PhotosBean> urlList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private CommenAdapter<Bitmap> gvAdapter;
    private CommenAdapter<CarStateRead.PhotosBean> photoCommenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_state);
        ButterKnife.bind(this);
        initView();
        init();
        initGv();
        initDialog();
        setPortraitChangeListener();
    }

    private void initView() {
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbWg.getId()){
                    control = new Control("jxstar0687731","外观");
                }else {
                    control = new Control("jxstar8497732","内饰");
                }
            }
        });

        rbWg.setChecked(true);
    }

    private void init() {

        Intent intent = getIntent();

        //                typeCode == 1为读数据
        typeCode = getIntent().getIntExtra("type", 0);

        /**
         * typeCode==1  只读信息   车辆综合信息进来
         */
        if (typeCode == 0) {
            initTitle();

            CarState carState = (CarState) intent.getParcelableExtra("carstate");

            if (carState != null) {
                is_edit = true;

//                type.setText(carState.getType_name());
                //                type.setText(carState.getType_name());
                if (carState.getType_name().equals("外观")){
                    rbWg.setChecked(true);
                }else
                    rbNs.setChecked(true);

                description.setText(carState.getDescription());

                control = new Control(carState.getType_id(), carState.getType_name());

                String[] split = carState.getPhotolist().split(",");
                for (int i = 0; i < split.length; i++) {
                    bitmapList.add(Base64Utils.Base64StrByBitmap(split[i]));
                }
            }
        } else {
            titleview.setTitle("车况记录详情");
            confirm.setVisibility(View.GONE);
            type.setCompoundDrawables(null, null, null, null);
            type.setClickable(false);
            description.setEnabled(false);

            CarStateRead carState = (CarStateRead) intent.getParcelableExtra(Key.OBJECT);

            if (carState != null) {

//                type.setText(carState.getType_name());
                if (carState.getType_name().equals("外观")){
                    rbWg.setChecked(true);
                }else
                rbNs.setChecked(true);

                description.setText(carState.getDescription());

                if (carState.getPhotos() != null) {

                    urlList = carState.getPhotos();

                }
            }
        }

    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
//                  bitmap  =   PhotoUtils.compressFile(uri.getPath(),6);
                Bitmap bitmap = null;
                if (bitmapList.size() < 7) {
                    //                        bitmap = PhotoUtils.getBitmapFormUri(mContext,uri);
                    bitmap = PhotoUtils.getZoomImage(uri, 100);
                    bitmapList.add(bitmap);
                    gvAdapter.notifyDataSetChanged();
                    iv.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    private void initTitle() {
        titleview.setRightText("拍照");
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_select_photo.show();
            }
        });
    }

    private void initDialog() {
        dialog = creatListDialog("选择类型", list, type, new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control2) {
                control = control2;
            }
        });

        //选择图片
        dialog_select_photo = new BottomMenuDialog(mContext, "拍照", "从本地导入");
        dialog_select_photo.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.takePicture(mContext);
                dialog_select_photo.dismiss();
            }
        });
        dialog_select_photo.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUtils.selectPicture(mContext);
                dialog_select_photo.dismiss();
            }
        });
    }


    @OnClick({R.id.type, R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.type:
                if (list.size() > 0) {
                    dialog.show();
                    return;
                }
                getcontrolbytype("6");
                break;
            case R.id.confirm:
                if (control == null) {
                    ToastUtil.showShort(mContext, "请选择类型");
                    return;
                }
//                if (TextUtils.isEmpty(description.getText().toString())) {
//                    ToastUtil.showShort(mContext, "请描述故障");
//                    return;
//                }
                Intent intent = new Intent();

                CarState carState = new CarState(getText(description), control.getDisplay_data(), "", "", control.getControl_id());
                if (bitmapList.size() > 0) {
                    carState.setPhoto(Base64Utils.Bitmap2StrByBase64(bitmapList.get(0)));
                    carState.setPhotolist(Base64Utils.Bitmap2StrByBase64(bitmapList, ","));
                }

                intent.putExtra(Key.OBJECT, carState);

                if (is_edit) {
                    setResult(3, intent);
                } else {
                    setResult(1, intent);
                }

                finish();
                break;
        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photoUtils.onActivityResult(mContext, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initGv() {
        if (typeCode == 1) {
            photoCommenAdapter = new CommenAdapter<>(R.layout.item_photo, mContext, urlList, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    ImageView view = holder.getView(R.id.item1);
                    Glide.with(mContext).load(urlList.get(position).getPhoto()).into(view);
                }
            });

            gv.setAdapter(photoCommenAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String photo_path = urlList.get(position).getPhoto();

                    if (!TextUtils.isEmpty(photo_path)) {
                        Intent intent1 = new Intent(mContext, PhotoActivity.class);
                        intent1.putExtra("url", photo_path);
                        startActivity(intent1);
                    }
                }
            });

        } else {
            gvAdapter = new CommenAdapter<>(R.layout.item_photo, mContext, bitmapList, new CommenAdapter.AdapterCallback() {
                @Override
                public void setView(ViewHolder holder, int position) {
                    holder.setImage(R.id.item1, bitmapList.get(position));
                }
            });

            gv.setAdapter(gvAdapter);

            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent1 = new Intent(mContext, PhotoActivity.class);
                    intent1.putExtra("bitmap", bitmapList.get(position));

                    startActivity(intent1);
                }
            });
        }

    }

}
