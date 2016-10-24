package cn.ucai.fulicenter.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.fulicenter.bean.UserAvatar;

/**
 * Created by ACherish on 2016/10/24.
 */
public class DBManager {
    private static DBManager mDbManager = new DBManager();
    private DBOpenHelper mOpenHelper;

    void onInit(Context context){
        mOpenHelper = new DBOpenHelper(context);
    }
    public static synchronized DBManager getInstance(){
        return mDbManager;
    }

    public synchronized void closeDB(){
        if (mOpenHelper !=null){
            mOpenHelper.closeDB();
        }
    }

    public synchronized boolean saveUser(UserAvatar userAvatar){
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,userAvatar.getMuserName());
        values.put(UserDao.USER_COLUMN_NICK,userAvatar.getMuserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,userAvatar.getMavatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,userAvatar.getMavatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,userAvatar.getMavatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,userAvatar.getMavatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,userAvatar.getMavatarLastUpdateTime());
        if (db.isOpen()){
            return db.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public synchronized UserAvatar getUser(String username){
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String sql = "select * from " + UserDao.USER_TABLE_NAME + " where " +
                UserDao.USER_COLUMN_NAME + " =?";
        UserAvatar userAvatar =null;
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToNext()){
            userAvatar = new UserAvatar();
            userAvatar.setMuserName(username);
            userAvatar.setMuserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            userAvatar.setMavatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            userAvatar.setMavatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            userAvatar.setMavatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            userAvatar.setMavatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            userAvatar.setMavatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return userAvatar;
        }
        return userAvatar;
    }

    public synchronized boolean updateUser(UserAvatar userAvatar){
        int result =-1;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        String sql = UserDao.USER_COLUMN_NAME +"=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK,userAvatar.getMuserNick());
        if (db.isOpen()){
            result = db.update(UserDao.USER_TABLE_NAME,values,sql,new String[]{userAvatar.getMuserName()});

        }
        
        return result>0;
    }
}
