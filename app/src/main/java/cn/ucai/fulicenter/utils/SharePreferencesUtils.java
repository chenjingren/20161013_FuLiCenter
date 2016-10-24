package cn.ucai.fulicenter.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ACherish on 2016/10/24.
 */
public class SharePreferencesUtils {
    private static final String SHARE_NAME = "saveUserInfo";
    private static SharePreferencesUtils instance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String SHARE_KEY_USER_NAME = "share_key_user_name";

    public SharePreferencesUtils(Context context){
        mSharedPreferences = context.getSharedPreferences(SHARE_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharePreferencesUtils getInstance(Context context){
        if (instance == null){
            instance = new SharePreferencesUtils(context);
        }
        return instance;
    }

    public void saveUser(String username){
        mEditor.putString(SHARE_KEY_USER_NAME,username);
        mEditor.commit();
    }
    public String getUser(){
        String username = mSharedPreferences.getString(SHARE_KEY_USER_NAME, null);
        return username;
    }
    public void removeUser(){
        mEditor.remove(SHARE_KEY_USER_NAME);
        mEditor.commit();
    }
}
