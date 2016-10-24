package cn.ucai.fulicenter;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.bean.UserAvatar;

/**
 * Created by ACherish on 2016/10/17.
 */
public class FuLiCenterApplication extends Application{
    //public static Context applicationContext;
    public static FuLiCenterApplication application;
    private static FuLiCenterApplication instance;

    private static  String userName;
    private static UserAvatar userAvatar;

    public static UserAvatar getUserAvatar() {
        return userAvatar;
    }

    public static void setUserAvatar(UserAvatar userAvatar) {
        FuLiCenterApplication.userAvatar = userAvatar;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        instance = this;
    }

    public static FuLiCenterApplication getInstance(){
        if (instance==null){
            instance = new FuLiCenterApplication();
        }
        return instance;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        FuLiCenterApplication.userName = userName;
    }
}
