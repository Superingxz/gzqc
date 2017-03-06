package com.xolo.gzqc.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.xolo.gzqc.R;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by Administrator on 2016/12/1.
 */
public  class ImageSelectUtils {
    Context context;
    public  static int MaxNum=8;
    public ImageSelectUtils(Context context) {
        this.context = context;
        initTheme(context);



    }
    FunctionConfig.Builder functionConfigBuilder;
    FunctionConfig functionConfig;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initTheme(Context context) {
        functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setMutiSelectMaxSize(MaxNum);
        functionConfigBuilder.setEnableCamera(true);
        //functionConfigBuilder.setEnableRotate(true);
        //functionConfigBuilder.setEnableCrop(true);
        //functionConfigBuilder.setEnableEdit(true);
       // functionConfigBuilder.setEnablePreview(true);
        functionConfig = functionConfigBuilder.build();
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#015cab"))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(R.color.DarkBlue)
                .setFabPressedColor(Color.parseColor("#015cab"))
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#015cab"))
                .setCheckNornalColor(Color.TRANSPARENT)
//                .setIconBack(R.mipmap.ic_action_previous_item)
//                .setIconRotate(R.mipmap.ic_action_repeat)
//                .setIconCrop(R.mipmap.ic_action_crop)
                .setIconCamera(R.mipmap.camera_2)
                .setPreviewBg(ContextCompat.getDrawable(context,R.drawable.bg_bleak))
                .setEditPhotoBgTexture(ContextCompat.getDrawable(context,R.drawable.bg_bleak))
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(context, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }

    void initConfig(Context context) {
        //setRotateReplaceSource(boolean)//配置选择图片时是否替换原始图片，默认不替换
        //setCropReplaceSource(boolean)//配置裁剪图片时是否替换原始图片，默认不替换
        //setFilter(List list)//添加图片过滤，也就是不在GalleryFinal中显示
        //setSelected(List)//添加已选列表,只是在列表中默认呗选中不会过滤图片
        //takePhotoFolter(File file)//配置拍照保存目录，不做配置的话默认是/sdcard/DCIM/GalleryFinal/
        //setForceCropEdit(boolean)//在开启强制裁剪功能时是否可以对图片进行编辑（也就是是否显示旋转图标和拍照图标）
        //setForceCrop(boolean)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
        //functionConfigBuilder.setEnableEdit(true);
        // functionConfigBuilder.setEnableRotate(true);
        // functionConfigBuilder.setEnablePreview(true);//是否开启预览功能
        // functionConfigBuilder.setRotateReplaceSource(true);
        // functionConfigBuilder.setCropSquare(true);

    }

    /**
     * 单选
     */
    public void openGallerySingle() {
        GalleryFinal.openGallerySingle(0x22, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if(resultCallBack!=null){
                    resultCallBack.onHanlderSuccess(resultList);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                if(resultCallBack!=null){
                    resultCallBack.onHanlderFailure(errorMsg);
                }
            }
        });
    }

    /**
     * 多选
     */
    public void openGalleryMuti(int num) {
        functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setMutiSelectMaxSize(num);
        functionConfig = functionConfigBuilder.build();
        GalleryFinal.openGalleryMuti(0x22, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if(resultCallBack!=null){
                    resultCallBack.onHanlderSuccess(resultList);
                }

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                if(resultCallBack!=null){
                    resultCallBack.onHanlderFailure(errorMsg);
                }

            }
        });
    }

    /**
     * 多选
     */
    public void openGalleryMuti(ArrayList<String> list, int num) {
        functionConfigBuilder = new FunctionConfig.Builder();
        functionConfigBuilder.setMutiSelectMaxSize(num-list.size());
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setFilter(list);
        functionConfig = functionConfigBuilder.build();
        GalleryFinal.openGalleryMuti(0x22, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if(resultCallBack!=null){
                    resultCallBack.onHanlderSuccess(resultList);
                }

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                if(resultCallBack!=null){
                    resultCallBack.onHanlderFailure(errorMsg);
                }

            }
        });
    }

    public ResultCallBack resultCallBack;

    public ResultCallBack getResultCallBack() {
        return resultCallBack;
    }

    public void setResultCallBack(ResultCallBack resultCallBack) {
        this.resultCallBack = resultCallBack;
    }

    public    interface ResultCallBack {
        void onHanlderSuccess(List<PhotoInfo> resultList);

        void onHanlderFailure(String errorMsg);
    }

}
