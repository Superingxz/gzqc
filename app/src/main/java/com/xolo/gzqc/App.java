package com.xolo.gzqc;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.xolo.gzqc.bean.child.CarOwner;
import com.xolo.gzqc.rong.FriendInfo;
import com.xolo.gzqc.utils.LogUtil;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.List;

import io.rong.imageloader.cache.disc.naming.Md5FileNameGenerator;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imageloader.core.ImageLoaderConfiguration;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/9/12.
 */
public  class App extends MultiDexApplication {

    /**
     * 是否是第一次登录
     */
    public static boolean isFirstLogin =  true;

    private static DisplayImageOptions options;

    private static List<FriendInfo>  friendInfoList;

    private static int type_role_chat;

    public static boolean isFirstLogin() {
        return isFirstLogin;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        RongIM.init(this);
        AppTool.init(this);

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.img_error)
                .showImageOnFail(R.mipmap.img_error)
                .showImageOnLoading(R.mipmap.img_error)
                .displayer(new FadeInBitmapDisplayer(300))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        //初始化图片下载组件
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(200)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(options)
                .build();

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    @Override
    public void onTerminate() {
        RongIM.getInstance().logout();
        LogUtil.i("terminate");
        super.onTerminate();
    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    public static void setIsFirstLogin(boolean isFirstLogin) {
        App.isFirstLogin = isFirstLogin;
    }


    public static List<FriendInfo> getFriendInfoList() {
        return friendInfoList;
    }

    public static void setFriendInfoList(List<FriendInfo> friendInfoList) {
        App.friendInfoList = friendInfoList;
    }

    public static int getType_role_chat() {
        return type_role_chat;
    }

    public static void setType_role_chat(int type_role_chat) {
        App.type_role_chat = type_role_chat;
    }
}
