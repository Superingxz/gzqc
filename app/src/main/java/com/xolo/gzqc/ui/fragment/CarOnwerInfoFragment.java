package com.xolo.gzqc.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.xolo.gzqc.R;
import com.xolo.gzqc.adapter.CarOnwerInfoAdapter;
import com.xolo.gzqc.adapter.Co_deNameAdapter;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.bean.child.CarOwnerPersonalCenter;
import com.xolo.gzqc.bean.child.CoInfo;
import com.xolo.gzqc.bean.child.Dept;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.configuration.SPManager;
import com.xolo.gzqc.ui.activity.HelpActivity;
import com.xolo.gzqc.ui.activity.UpdateDepActivity;
import com.xolo.gzqc.ui.view.LoadDialog;
import com.xolo.gzqc.ui.view.ScrollGridView;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.ImageSelectUtils;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.PhoneUtil;
import com.xolo.gzqc.utils.ToastUtil;

import org.xutils.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * 车主信息
 * Created by Administrator on 2016/9/24.
 */
public class CarOnwerInfoFragment extends LazyFragment {
    //    String[] textArry = {"姓名:", "性别:", "手机号:", "手机号2:", "省份:", "市级:", "区域:", "街道:", "所属汽修厂:"};
//    ListView owner_listview;
    private static final int PICK_PHOTO = 0x00002;// PhotoPicker
    private static final int MSG_HTTP_RESULT = 001;
    private static final String TAG = "test";
    List<CarOwnerPersonalCenter> list;
    Button update_user, delete_dept;
    List<Area> list_province = new ArrayList<>();
    List<Area> list_city = new ArrayList<>();
    List<Area> list_county = new ArrayList<>();
    String provinceId, cityId, countyId, provinceName, cityName, counName;
    EditText name_text, phone_2, street;
    ImageView man_img, female_img;
    LinearLayout layout_female, layout_man;
    TextView phone_1, dep_name, area_text, province, city;
    // Spinner spinner, spinner1, spinner2, spinner3;
    ImageView choose_img;
    LinearLayout spinner_layout, spinner_layout1, spinner_layout2;
    RelativeLayout help_layout;

    ArrayList<String> imgpath = new ArrayList<>();
    int UPLOAD = 12;
    private CarOnwerInfoAdapter carOnwerInfoAdapter;
    private StringBuffer imgString;
    private Button share;
    private ScrollGridView sgv;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    protected void loadData() {
        if (needLoad()) {
            getDate();
            getArea("", null, 4);
            mHasLoadedOnce = true;
        }
    }


    View view;

    @Override
    public void onResume() {
        super.onResume();
        if (dep_name != null) {
            dep_name.setText(user.getDept_name());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            user = SPManager.getUser(mContext);
            view = inflater.inflate(R.layout.fragment_ownerinfo2, null);
            initView(view);

            //初始化ShareSDK
            ShareSDK.initSDK(mContext);


        }

        return view;
    }

    CoInfo info;

    List<Dept> deptList = new ArrayList<>();

    void getDate() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getownerinfo");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.CoInfoBean.class, new HttpUtil.HttpCallBack<ORMBean.CoInfoBean>() {
            @Override
            public void onSuccess(ORMBean.CoInfoBean result) {
                if (result.getRes().equals("1")) {
                    info = result.getData().get(0);
                    if (!TextUtils.isEmpty(info.getPhoto())) {
                        Glide.with(getActivity()).load(info.getPhoto()).asBitmap().centerCrop().into(new BitmapImageViewTarget(choose_img) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                choose_img.setImageDrawable(circularBitmapDrawable);
                                userImag = resource;
                            }
                        });
                    }

/*
                    imgpath.remove("default");
                    for (int i = 0; i < info.getPic().size(); i++) {
                        if (i < 12) {
                            imgpath.add(info.getPic().get(i).getPhoto_path());
                        }
                    }
                    if (imgpath.size() < UPLOAD && !imgpath.contains("default")) {
                        imgpath.add("default");
                    }
                    carOnwerInfoAdapter.notifyDataSetChanged();
*/

                    imgpath.remove("default");
                    if (info.getPic()!=null){
                        for (int i = 0; i < info.getPic().size(); i++) {
                            if(info.getPic().get(i).getPhoto_path()!=null){
                                imgpath.add(info.getPic().get(i).getPhoto_path());
                            }
                        }
                    }
                    if (imgpath.size() < UPLOAD && !imgpath.contains("default")) {
                        imgpath.add("default");
                    }

                    Log.e(TAG, "imgpath: "+imgpath.toString() );
                    carOnwerInfoAdapter.notifyDataSetChanged();

                    setListData();
                    getDeptData();
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());
                }
                LoadDialog.dismiss(mContext);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
        });
    }


    void getDeptData() {
        LoadDialog.show(getContext());
        RequestParams requestParams = creatParams("getrepairdeptbyownerid");
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("userid", user.getUser_id());

        HttpUtil.getInstance().post(requestParams, ORMBean.DeptBean.class, new HttpUtil.HttpCallBack<ORMBean.DeptBean>() {
            @Override
            public void onSuccess(ORMBean.DeptBean result) {
                if (result.getRes().equals("1")) {
                    deptList.clear();
                    deptList.addAll(result.getData());
                } else {
                    ToastUtil.showShort(mContext, result.getMsg());

                }
                LoadDialog.dismiss(mContext);
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
        });
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        int lengths = bos.toByteArray().length;
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    Co_deNameAdapter spinner3adapter;
    int sex = 0;

    private void setListData() {
        if (!TextUtils.isEmpty(info.getSex().trim())) {
            sex = Integer.valueOf(info.getSex());
            if (sex == 0) {
                man_img.setBackgroundResource(R.mipmap.sure);
                female_img.setBackgroundResource(R.mipmap.nil);
            } else {
                man_img.setBackgroundResource(R.mipmap.nil);
                female_img.setBackgroundResource(R.mipmap.sure);
            }
        }
        name_text.setText(info.getName());
        phone_1.setText(info.getPhone());
        phone_2.setText(info.getPhone2());
        street.setText(info.getStreet());
        cityName = info.getCity();
        provinceName = info.getProvince();
        counName = info.getArea();
        area_text.setText(counName);
        city.setText(cityName);
        province.setText(provinceName);
        provinceId = info.getProvinceid();
        cityId = info.getCityid();
        countyId = info.getAreaid();
    }

    Bitmap userImag;
    ImageSelectUtils imageSelectUtils;
    ImageSelectUtils imageSelectUtilsGv;

    protected void initView(View view) {

        help_layout = (RelativeLayout) view.findViewById(R.id.help_layout);
        help_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpActivity.class);
                intent.putExtra("role", "carowners");
                startActivity(intent);
            }
        });
        area_text = (TextView) view.findViewById(R.id.area);
        province = (TextView) view.findViewById(R.id.province);
        city = (TextView) view.findViewById(R.id.city);
        spinner_layout = (LinearLayout) view.findViewById(R.id.spinner_layout);
        spinner_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list_province.size() > 0) {
                    creatListDialog("", list_province, province, new ListDialogCallBack<Area>() {
                        @Override
                        public String setText(Area area) {
                            return area.getDistname();
                        }

                        @Override
                        public void onClick(Area area) {
                            provinceId = area.getDistid();
                            provinceName = area.getDistname();
                            province.setText(area.getDistname());
                        }
                    }).show();
                } else {
                    getArea("", province, 0);
                }
            }
        });
        spinner_layout1 = (LinearLayout) view.findViewById(R.id.spinner_layout1);
        spinner_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArea(provinceId, city, 1);
            }
        });
        spinner_layout2 = (LinearLayout) view.findViewById(R.id.spinner_layout2);
        spinner_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getArea(cityId, area_text, 2);
            }
        });

        //选择头像
        imageSelectUtils = new ImageSelectUtils(getActivity());
        imageSelectUtils.setResultCallBack(new ImageSelectUtils.ResultCallBack() {
            @Override
            public void onHanlderSuccess(List<PhotoInfo> resultList) {
                Glide.with(getActivity()).load(resultList.get(0).getPhotoPath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(choose_img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        choose_img.setImageDrawable(circularBitmapDrawable);
                        userImag = resource;
                    }
                });
            }

            @Override
            public void onHanlderFailure(String errorMsg) {

            }
        });

        choose_img = (ImageView) view.findViewById(R.id.choose_img);
        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelectUtils.openGallerySingle();
            }
        });


        //选择gv证件片
        sgv = ((ScrollGridView) view.findViewById(R.id.sgv));
        imageSelectUtilsGv = new ImageSelectUtils(getActivity());
        imgpath.add("default");
        carOnwerInfoAdapter = new CarOnwerInfoAdapter(getActivity(), R.layout.item_owner_car_info_img, imgpath);
        sgv.setAdapter(carOnwerInfoAdapter);

        sgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                ToastUtil.showShort(getActivity(),"onItemClick");
                if (carOnwerInfoAdapter.getData().get(position).equals("default")) {
                    imageSelectUtilsGv.openGalleryMuti((ArrayList<String>) imgpath, UPLOAD + 1);
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("是否删除？").setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imgpath.remove(position);
                            dialog.dismiss();
                            if (!carOnwerInfoAdapter.getData().contains("default") && carOnwerInfoAdapter.getData().size() < 12) {
                                imgpath.add("default");
                            }
                            carOnwerInfoAdapter.notifyDataSetChanged();
                        }
                    }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });

        imageSelectUtilsGv.setResultCallBack(new ImageSelectUtils.ResultCallBack() {
            @Override
            public void onHanlderSuccess(List<PhotoInfo> resultList) {
                imgpath.remove("default");
                for (PhotoInfo photoInfo : resultList) {
                    imgpath.add(photoInfo.getPhotoPath());
                }
                if (carOnwerInfoAdapter.getData().size() < 12) {
                    imgpath.add("default");
                }
                carOnwerInfoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onHanlderFailure(String errorMsg) {
                ToastUtil.showShort(getActivity(), errorMsg);
            }
        });

        /**
         * 长按图片进行分享
         */
        sgv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtil.showShort(getActivity(),"onItemLongClick");
                String imgPath = imgpath.get(position);
                if (imgPath.equals("default")){
                    return true;
                }

                final String photo_path = info.getPic().get(position).getPhoto_path();
                final String imgName = photo_path.substring(photo_path.lastIndexOf("/") + 1);

                if (IsExists(imgName)) {
                    shareSdk(photo_path, imgName);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bitmap = getImageBitmap(photo_path);
                                savePicture(bitmap, imgName);
                                if (IsExists(imgName)) {
                                    shareSdk(photo_path, imgName);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                return true;
            }
        });


        dep_name = (TextView) view.findViewById(R.id.dep_name);
        dep_name.setText(user.getDept_name());
        delete_dept = (Button) view.findViewById(R.id.delete_dept);
        delete_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Dept de : deptList) {
                    if (de.getDept_name().equals(user.getDept_name())) {
                        de.setChecck(true);
                    } else {
                        de.setChecck(false);
                    }
                }
                Intent intent = new Intent(getActivity(), UpdateDepActivity.class);
                intent.putExtra("deptList", (Serializable) deptList);
                startActivity(intent);
            }
        });
        street = (EditText) view.findViewById(R.id.street);
        man_img = (ImageView) view.findViewById(R.id.man_img);
        female_img = (ImageView) view.findViewById(R.id.female_img);
        name_text = (EditText) view.findViewById(R.id.name_text);
        phone_2 = (EditText) view.findViewById(R.id.phone_2);
        layout_female = (LinearLayout) view.findViewById(R.id.layout_female);
        layout_man = (LinearLayout) view.findViewById(R.id.layout_man);
        phone_1 = (TextView) view.findViewById(R.id.phone_1);
        layout_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man_img.setBackgroundResource(R.mipmap.nil);
                female_img.setBackgroundResource(R.mipmap.sure);
                sex = 1;
            }
        });
        layout_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man_img.setBackgroundResource(R.mipmap.sure);
                female_img.setBackgroundResource(R.mipmap.nil);
                sex = 0;
            }
        });


        update_user = (Button) view.findViewById(R.id.update_user);
        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });

    }

    private boolean IsExists( String imgName) {
        String absolutePath = mContext.getExternalFilesDir(null).getAbsolutePath();
        File file = new File(absolutePath + File.separator + imgName);
        if (file.exists()) {
            return true;
        }
        return false;
    }


    private void getArea(final String id, final TextView textView, final int type) {
        RequestParams params = creatParams("getregionbydistid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("distid", id);
        HttpUtil.getInstance().postLoading(getActivity(), params, ORMBean.AreaBean.class, new HttpUtil.HttpCallBack<ORMBean.AreaBean>() {
            @Override
            public void onSuccess(ORMBean.AreaBean result) {
                if (type == 0) {
                    list_province.clear();
                    list_province.addAll(result.getData());
                    creatListDialog("", list_province, province, new ListDialogCallBack<Area>() {
                        @Override
                        public String setText(Area area) {
                            return area.getDistname();
                        }

                        @Override
                        public void onClick(Area area) {
                            provinceId = area.getDistid();
                            provinceName = area.getDistname();
                        }
                    }).show();
                } else if (type == 1) {
                    list_city.clear();
                    list_city.addAll(result.getData());
                    creatListDialog("", list_city, city, new ListDialogCallBack<Area>() {
                        @Override
                        public String setText(Area area) {
                            return area.getDistname();
                        }

                        @Override
                        public void onClick(Area area) {
                            cityId = area.getDistid();
                            cityName = area.getDistname();
                        }
                    }).show();
                } else if (type == 2) {
                    list_county.clear();
                    list_county.addAll(result.getData());
                    creatListDialog("", list_county, area_text, new ListDialogCallBack<Area>() {
                        @Override
                        public String setText(Area area) {
                            return area.getDistname();
                        }

                        @Override
                        public void onClick(Area area) {
                            countyId = area.getDistid();
                            counName = area.getDistname();
                        }
                    }).show();
                }
            }


            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    void updateInfo() {
        if (!TextUtils.isEmpty(phone_2.getText().toString())) {
            if (!PhoneUtil.isMobileNO(phone_2.getText().toString())) {
                ToastUtil.showShort(getActivity(), "请输入正确的手机号码");
                return;
            }

        }

/*        if (carOnwerInfoAdapter.getData().size() == 1) {
            ToastUtil.showShort(getContext(), "至少上传一张证件照");
            return;
        }*/

        LoadDialog.show(getContext());
        final RequestParams requestParams = creatParams("updateownerinfo");
        requestParams.addBodyParameter("userid", user.getUser_id());
        requestParams.addBodyParameter("bc_car_owner_id", user.getUser_id());
        requestParams.addBodyParameter("name", name_text.getText().toString());
        requestParams.addBodyParameter("sex", String.valueOf(sex));
        requestParams.addBodyParameter("phone2", phone_2.getText().toString());
        requestParams.addBodyParameter("province", provinceName);
        requestParams.addBodyParameter("provinceid", provinceId);
        requestParams.addBodyParameter("city", cityName);
        requestParams.addBodyParameter("cityid", cityId);
        requestParams.addBodyParameter("area", counName);
        requestParams.addBodyParameter("areaid", countyId);
        requestParams.addBodyParameter("street", street.getText().toString());
        requestParams.addBodyParameter("factory_name", user.getDept_name());
        requestParams.addBodyParameter("factory_id", user.getDept_id());
        if (userImag != null) {
            requestParams.addBodyParameter("photo", Bitmap2StrByBase64(userImag));
        } else {
            requestParams.addBodyParameter("photo", "");
        }

        imgString = new StringBuffer();
        for (int i = 0; i < carOnwerInfoAdapter.getData().size(); i++) {
            if (!carOnwerInfoAdapter.getData().get(i).equals("default")) {
                String item = carOnwerInfoAdapter.getItem(i);
                Bitmap bitmap = carOnwerInfoAdapter.getHashMap().get(item);
                imgString.append(Bitmap2StrByBase64(bitmap) + ",");
            }
        }
        if (imgString.length() > 0) {
            imgString.deleteCharAt(imgString.length() - 1);
            requestParams.addBodyParameter("pic", imgString.toString());
        }else {
            requestParams.addBodyParameter("pic", "");
        }

        HttpUtil.getInstance().post(requestParams, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                imgpath.clear();
                carOnwerInfoAdapter.notifyDataSetChanged();
                getDate();

                ToastUtil.showShort(mContext, result.getMsg());
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoadDialog.dismiss(mContext);
            }
        });


    }

    @Override
    protected void init() {

    }


    private void shareSdk(String shareUrl, String imgName) {
        //初始化ShareSDK
        ShareSDK.initSDK(mContext);

        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
//        oks.setTitle("证件照");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
//        oks.setTitleUrl(shareUrl);
// text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        String imagePath = mContext.getExternalFilesDir(null).getAbsolutePath() + File.separator + imgName;
        oks.setImagePath(imagePath);//确保SDcard下面存在此张图片

// url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(shareUrl);
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(mContext.getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(shareUrl);

// 启动分享GUI
        oks.show(mContext);
    }

    /**
     * 获取网络图片
     * @param path 图片路径
     * @return
     */
    public Bitmap getImageBitmap(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(20000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
            return bitmap;
        }
        return null;
    }

    /**
     * 将图片保存到私有目录
     *
     * @param bitmap
     * @param pictureName
     */
    public void savePicture(Bitmap bitmap, String pictureName) {
        String absolutePath = mContext.getExternalFilesDir(null).getAbsolutePath();
        File file = new File(absolutePath + File.separator + pictureName);
        FileOutputStream fos = null;
        if(file.exists()){
            file.delete();
        }
        try {
            fos = new FileOutputStream(file,false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.flush();
                fos.close();
                Log.i(TAG, "图片已经保存");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
