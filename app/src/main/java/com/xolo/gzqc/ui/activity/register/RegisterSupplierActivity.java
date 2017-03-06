package com.xolo.gzqc.ui.activity.register;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xolo.gzqc.BaseActivity;
import com.xolo.gzqc.R;
import com.xolo.gzqc.bean.BaseBean;
import com.xolo.gzqc.bean.ORMBean;
import com.xolo.gzqc.bean.child.Area;
import com.xolo.gzqc.bean.child.Control;
import com.xolo.gzqc.configuration.Key;
import com.xolo.gzqc.ui.dialog.BottomMenuDialog;
import com.xolo.gzqc.ui.view.ExEditText;
import com.xolo.gzqc.ui.view.TitleView;
import com.xolo.gzqc.utils.AMUtils;
import com.xolo.gzqc.utils.Base64Utils;
import com.xolo.gzqc.utils.Interface.ListDialogCallBack;
import com.xolo.gzqc.utils.HttpUtil;
import com.xolo.gzqc.utils.PhotoUtils;
import com.xolo.gzqc.utils.SPUtils;
import com.xolo.gzqc.utils.ToastUtil;
import com.xolo.gzqc.utils.downtime.DownTimer;
import com.xolo.gzqc.utils.downtime.DownTimerListener;

import org.xutils.http.RequestParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 供应商注册
 */
public class RegisterSupplierActivity extends BaseActivity {

    @BindView(R.id.eet_company)
    ExEditText eetCompany;
    @BindView(R.id.eet_name)
    ExEditText eetName;
    @BindView(R.id.eet_phone)
    ExEditText eetPhone;
    @BindView(R.id.eet_code)
    ExEditText eetCode;
    @BindView(R.id.eet_province)
    ExEditText eetProvince;
    @BindView(R.id.eet_city)
    ExEditText eetCity;
    @BindView(R.id.eet_country)
    ExEditText eetCountry;
    @BindView(R.id.eet_business)
    ExEditText eetBusiness;
    @BindView(R.id.eet_service)
    ExEditText eetService;
    @BindView(R.id.btn_add_legal_photo)
    ImageView btnAddLegalPhoto;
    @BindView(R.id.iv_legal)
    ImageView ivLegal;
    @BindView(R.id.btn_add_company_photo)
    ImageView btnAddCompanyPhoto;
    @BindView(R.id.iv_company)
    ImageView ivCompany;
    @BindView(R.id.btn_add_else_photo)
    ImageView btnAddElsePhoto;
    @BindView(R.id.iv_else_photo_1)
    ImageView ivElsePhoto1;
    @BindView(R.id.iv_else_photo_2)
    ImageView ivElsePhoto2;
    @BindView(R.id.iv_else_photo_3)
    ImageView ivElsePhoto3;
    @BindView(R.id.titleview)
    TitleView titleview;
    @BindView(R.id.eet_identity)
    ExEditText eetIdentity;
    @BindView(R.id.eet_password)
    ExEditText eetPassword;
    @BindView(R.id.eet_stree)
    ExEditText eetStree;
    @BindView(R.id.btn_commin)
    Button btnCommin;

    private Button getCode;

    private List<Area> list_province = new ArrayList<>();
    private List<Area> list_city = new ArrayList<>();
    private List<Area> list_county = new ArrayList<>();

    private Dialog dialog_province;
    private Dialog dialog_city;
    private Dialog dialog_county;
    private Dialog dialog_business;
    private BottomMenuDialog dialog_select_photo;

    private String provinceid;
    private String cityid;
    private String areaid;

    private int position_select_photo;

    private PhotoUtils photoUtils;

    private Bitmap bitmap_legal;
    private Bitmap bitmap_company;
    private Bitmap[] bitmaps_else = new Bitmap[3];


    /**
     * 经营范围
     */
    private String   is_car_article = "0";
    private String   is_second_car = "0";
    private String   is_goods = "0";
    private String   is_other = "0";

    /**
     * 客服
     */
    private Dialog  dialog_sevice;
    private Control control_sevice;
    private List<Control>  list_sevice = new ArrayList<>();

    /**
     * 倒计时
     */
    private DownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_supplier);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        eetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);

        getCode = eetCode.getButton();
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendverificationcode();
            }
        });
        initTitle();
        initDialog();
        initDownTime();
        initPhotoUtils();
    }

    private void initPhotoUtils() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
//                Bitmap bitmap = PhotoUtils.decodeSampledBitmapFromResource(mContext, uri.getPath(), 60, 60);
                Bitmap bitmap = null;
                try {
                    bitmap = PhotoUtils.getBitmapFormUri(mContext,uri);
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(mContext,"获取图片文件失败");
                }
                switch (position_select_photo) {
                    case 0:
                        bitmap_legal = bitmap;
                        ivLegal.setImageBitmap(bitmap_legal);
                        break;
                    case 1:
                        bitmap_company = bitmap;
                        ivCompany.setImageBitmap(bitmap_company);
                        break;
                    case 2:
                        for (int i = 0; i < 3; i++) {
                            if (bitmaps_else[i] == null) {
                                bitmaps_else[i] = bitmap;
                                if (i == 0) {
                                    ivElsePhoto1.setImageBitmap(bitmaps_else[i]);
                                }
                                if (i == 1) {
                                    ivElsePhoto2.setImageBitmap(bitmaps_else[i]);
                                }
                                if (i == 2) {
                                    ivElsePhoto3.setImageBitmap(bitmaps_else[i]);
                                }
                                return;
                            }
                        }
                        break;
                }

            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    private void initDownTime() {
        downTimer = new DownTimer();
        downTimer.setListener(new DownTimerListener() {
            @Override
            public void onTick(long millisUntilFinished) {
                getCode.setText("重新发送(" + (millisUntilFinished / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                getCode.setClickable(true);
                getCode.setText("获取验证码");
                getCode.setBackgroundColor(getResources().getColor(R.color.DarkBlue));
            }
        });
    }

    private void initTitle() {
        titleview.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initDialog() {
        dialog_province = createAreaDialog("省", list_province, eetProvince);
        dialog_city = createAreaDialog("市", list_city, eetCity);
        dialog_county = createAreaDialog("县区", list_county, eetCountry);

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
        dialog_sevice = creatListDialog("", list_sevice, eetService.getEt_contetn(), new ListDialogCallBack<Control>() {
            @Override
            public String setText(Control control) {
                return control.getDisplay_data();
            }

            @Override
            public void onClick(Control control) {
              control_sevice = control;
            }
        });

        dialog_business = new AlertDialog.Builder(mContext)
                .setTitle("选择经营范围")
                .setMultiChoiceItems(new String[]{"汽车用品","二手车","特色产品","其他"},new boolean[]{false,false,false,false},new DialogInterface.OnMultiChoiceClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (which == 0){
                            if (isChecked){is_car_article = "1";} else  is_car_article = "0";
                        }else  if (which == 1){
                            if (isChecked){is_second_car = "1";} else  is_second_car = "0";
                        }else  if (which == 2){
                            if (isChecked){is_goods = "1";} else  is_goods = "0";
                        }else
                        if (isChecked){is_other = "1";} else  is_other = "0";
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuffer buffer = null;
                        if (is_car_article == "1"){
                            buffer = new StringBuffer("汽车用品");
                        }
                        if (is_second_car == "1"){
                            if (buffer == null){
                                buffer = new StringBuffer("二手车");
                            }else  buffer.append("/二手车");
                        }
                        if (is_goods == "1"){
                            if (buffer == null){
                                buffer = new StringBuffer("特色产品");
                            }else  buffer.append("/特色产品");
                        }
                        if (is_other == "1"){
                            if (buffer == null){
                                buffer = new StringBuffer("其他");
                            }else  buffer.append("/其他");
                        }

                        eetBusiness.setText(buffer == null?"":buffer.toString());
                    }
                })
                .create();
    }

    @OnClick({R.id.eet_province, R.id.eet_city, R.id.eet_country, R.id.eet_business, R.id.eet_service, R.id.btn_add_legal_photo, R.id.btn_add_company_photo, R.id.btn_add_else_photo, R.id.btn_commin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eet_province:
                if (list_province.size() > 0) {
                    dialog_province.show();
                    return;
                }
                getArea(list_province, dialog_province, "");
                break;
            case R.id.eet_city:
                if (TextUtils.isEmpty(eetProvince.getText())) {
                    ToastUtil.showLong(mContext, "请先选择省");
                    return;
                }
                getArea(list_city, dialog_city, provinceid);
                break;
            case R.id.eet_country:
                if (TextUtils.isEmpty(eetCity.getText())) {
                    ToastUtil.showLong(mContext, "请先选择市");
                    return;
                }
                getArea(list_county, dialog_county, cityid);
                break;
            case R.id.eet_business:
                dialog_business.show();
                break;
            case R.id.eet_service:
                if (list_sevice.size()>0){
                    dialog_sevice.show();
                }else
                getcontrolbytype();
                break;
            case R.id.btn_add_legal_photo:
                position_select_photo = 0;
                dialog_select_photo.show();
                break;
            case R.id.btn_add_company_photo:
                position_select_photo = 1;
                dialog_select_photo.show();
                break;
            case R.id.btn_add_else_photo:
                position_select_photo = 2;
                dialog_select_photo.show();
                break;
            case R.id.btn_commin:
                supplierapply();
                break;
        }
    }


    private Dialog createAreaDialog(String title, List<Area> list, final ExEditText targer) {
        return creatListDialog(title, list, targer.getEt_contetn(), new ListDialogCallBack<Area>() {
            @Override
            public String setText(Area area) {
                return area.getDistname();
            }

            @Override
            public void onClick(Area area) {

                if (targer == eetProvince) {
                    eetCity.setText("");
                    eetCountry.setText("");
                    provinceid = area.getDistid();
                    cityid = null;
                } else if (targer == eetCity) {
                    eetCountry.setText("");
                    cityid = area.getDistid();
                } else {
                    areaid = area.getDistid();
                }
            }
        });
    }


    /**
     * 接口获取省，市，县的信息
     *
     * @param list
     * @param dialog
     */
    private void getArea(final List list, final Dialog dialog, String id) {
        RequestParams params = creatParams("getregionbydistid");
        params.addBodyParameter(Key.USER_ID, user.getUser_id());
        params.addBodyParameter("distid", id);

        HttpUtil.getInstance().postLoading(mContext, params, ORMBean.AreaBean.class, new HttpUtil.HttpCallBack<ORMBean.AreaBean>() {
            @Override
            public void onSuccess(ORMBean.AreaBean result) {
                List<Area> data = result.getData();

                list.clear();
                list.addAll(data);
                dialog.show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    /**
     * 1-4 发送验证码短信接口 sendverificationcode(mobile)
     * 手机号码：mobile
     */
    private void sendverificationcode() {

        final String phone = eetPhone.getText();
        if (!AMUtils.isMobile(phone)) {
            ToastUtil.showShort(mContext, "请先填写正确的手机号码");
            return;
        }

        RequestParams params = creatParams("sendverificationcode");
        params.addBodyParameter("mobile", phone);

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                String msg = result.getMsg();
                SPUtils.put(mContext, "massageCode", msg);
                SPUtils.put(mContext, "massagePhone", phone);

                downTimer.startDown(60000);
                getCode.setClickable(false);
                getCode.setBackgroundColor(getResources().getColor(R.color.gray1));
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoUtils.onActivityResult(mContext, requestCode, resultCode, data);
    }


    /**
     * 1-8 供应商申请入驻接口 supplierapply(company_name,legal_name,card,phone,password,province_id,province,city_id,city,area_id,area,street,
     * coustomer_type_id,coustomer_type,car_articles,second_car,goods,others,card_pic,sz_pic,qt_pic)
     * 公司名称:company_name,法人姓名:legal_name,法人身份证号:card,联系电话:phone,登录密码:password,省份ID:province_id,省份:province,城市ID:city_id,城市:city,区县ID:area_id,
     * 区县:area,乡镇街道:street,客服类别ID:coustomer_type_id,客服类别:coustomer_type,汽车用品:car_articles,二手车:second_car,特色商品:goods,其他:others,法人身份证图片:card_pic,
     * 公司三证图片:sz_pic,其他证书（多个证书，使用英文逗号隔开）:qt_pic
     */
    private void supplierapply() {
        if (TextUtils.isEmpty(eetCompany.getText())) {
            ToastUtil.showShort(mContext, "请输入公司名称");
            return;
        }
        if (TextUtils.isEmpty(eetName.getText())) {
            ToastUtil.showShort(mContext, "请输入法人姓名");
            return;
        }
        if (TextUtils.isEmpty(eetIdentity.getText())) {
            ToastUtil.showShort(mContext, "请输入身份证号");
            return;
        }
        if (TextUtils.isEmpty(eetPhone.getText())) {
            ToastUtil.showShort(mContext, "请输入联系电话");
            return;
        }
        if (TextUtils.isEmpty(eetCode.getText())) {
            ToastUtil.showShort(mContext, "请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(eetPassword.getText())) {
            ToastUtil.showShort(mContext, "请输入登录密码");
            return;
        }
        if (TextUtils.isEmpty(eetProvince.getText())) {
            ToastUtil.showShort(mContext, "请选择省份");
            return;
        }
        if (TextUtils.isEmpty(eetCity.getText())) {
            ToastUtil.showShort(mContext, "请选择市区");
            return;
        }
        if (TextUtils.isEmpty(eetCountry.getText())) {
            ToastUtil.showShort(mContext, "请选择区县");
            return;
        }
        if (TextUtils.isEmpty(eetStree.getText())) {
            ToastUtil.showShort(mContext, "请输入乡镇街道");
            return;
        }
        if (TextUtils.isEmpty(eetBusiness.getText())) {
            ToastUtil.showShort(mContext, "请选择经营范围");
            return;
        }
        if (control_sevice == null) {
            ToastUtil.showShort(mContext, "请选择客服类型");
            return;
        }
        if (bitmap_legal == null){
            ToastUtil.showShort(mContext, "请上传法人身份证照片");
            return;
        }
        if (bitmap_company == null){
            ToastUtil.showShort(mContext, "请上传公司三证照片");
            return;
        }

        final String massageCode = (String) SPUtils.get(mContext, "massageCode", "");
        if (!eetCode.getText().equals(massageCode)) {
            ToastUtil.showShort(mContext, "输入验证码错误");
            return;
        }

        String massagePhone = (String) SPUtils.get(mContext, "massagePhone", "");
        if (!eetPhone.getText().equals(massagePhone)) {
            ToastUtil.showShort(mContext, "输入验证码错误");
            return;
        }

        RequestParams params = creatParams("supplierapply");

     params.addBodyParameter("company_name",eetCompany.getText());
     params.addBodyParameter("legal_name",eetName.getText());
     params.addBodyParameter("card",eetIdentity.getText());
     params.addBodyParameter("phone",eetPhone.getText());
     params.addBodyParameter("password",eetPassword.getText());
     params.addBodyParameter("province_id",provinceid);
     params.addBodyParameter("province",eetProvince.getText());
     params.addBodyParameter("city_id",cityid);
     params.addBodyParameter("city",eetCity.getText());
     params.addBodyParameter("area_id",areaid);
     params.addBodyParameter("area",eetCountry.getText());
     params.addBodyParameter("street",eetStree.getText());
     params.addBodyParameter("coustomer_type_id",control_sevice.getControl_id());
     params.addBodyParameter("coustomer_type",control_sevice.getDisplay_data());
     params.addBodyParameter("car_articles",is_car_article);
     params.addBodyParameter("second_car",is_second_car);
     params.addBodyParameter("goods",is_goods);
     params.addBodyParameter("others",is_other);
     params.addBodyParameter("card_pic", Base64Utils.Bitmap2StrByBase64(bitmap_legal));
     params.addBodyParameter("sz_pic",Base64Utils.Bitmap2StrByBase64(bitmap_company));
     params.addBodyParameter("qt_pic",Base64Utils.Bitmap2StrByBase64(bitmaps_else));

        HttpUtil.getInstance().postLoading(mContext, params, BaseBean.class, new HttpUtil.HttpCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean result) {
                ToastUtil.showShort(mContext,"提交成功");
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }
        });

    }


    /**
     *     2-23 根据类别获取对应的数据字典接口。1为燃料，2为操控，3为驱动，4为排量 ,5为油量,6为车况记录，7为随车附件,8为班组,9为配件来源,10为进度查询的当前状态,11为客服类别
     *     getcontrolbytype(userid,type,dept_id) 当前用户ID:userid,类型:type,维修厂ID:dept_id
     */

       private void  getcontrolbytype(){
           RequestParams params = creatParams("getcontrolbytype");
           params.addBodyParameter("type","11");

           HttpUtil.getInstance().postLoading(mContext, params, ORMBean.ControlBean.class, new HttpUtil.HttpCallBack<ORMBean.ControlBean>() {
               @Override
               public void onSuccess(ORMBean.ControlBean result) {
                   List<Control> data = result.getData();
                   list_sevice.addAll(data);
                   dialog_sevice.show();
               }

               @Override
               public void onError(Throwable ex, boolean isOnCallback) {

               }
           });
       }



}
