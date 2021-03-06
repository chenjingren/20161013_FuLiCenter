package cn.ucai.fulicenter.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueChildActivity;
import cn.ucai.fulicenter.activity.CategoryChildActivity;
import cn.ucai.fulicenter.activity.CollectsActivity;
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.activity.LoginActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.RegisterActivity;
import cn.ucai.fulicenter.activity.UpdateNickActivity;
import cn.ucai.fulicenter.activity.UserProfileActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.bean.CategoryChildBean;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }

    public static void startActivity(Context context,Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoGoodDetailsActivity(Context context, int goodsId){
        Intent intent = new Intent();
        intent.setClass(context, GoodDetailsActivity.class);
        intent.putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }

    public static void gotoBoutiqueChildActivity(Context context, BoutiqueBean bean){
        Intent intent = new Intent();
        intent.setClass(context, BoutiqueChildActivity.class);
        intent.putExtra(I.Boutique.CAT_ID,bean);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }

    public static void gotoCategoryChildActivity(Context context, CategoryChildBean bean){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,bean);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }

    public static void gotoCategoryChildActivity(Context context, int catId){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }

    public static void gotoCategoryChildActivity(Context context, int catId,
                                                 String groupName, ArrayList<CategoryChildBean> list){
        Intent intent = new Intent();
        intent.setClass(context, CategoryChildActivity.class);
        intent.putExtra(I.CategoryChild.CAT_ID,catId);
        intent.putExtra(I.CategoryGroup.NAME,groupName);
        intent.putExtra(I.CategoryChild.ID,list);
        startActivity(context,intent);
        /*context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);*/
    }

    public static void gotoLoginActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_LOGIN);
    }

    public static void gotoLoginActivityFromCart(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_LOGIN_FROM_CART);
    }

    public static void gotoRegisterActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, RegisterActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_REGISTER);
    }

    public static void startActivityForResult(Activity context,Intent intent,int requestCode){
        context.startActivityForResult(intent,requestCode);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoUserProfileActivity(Activity context){
        startActivity(context, UserProfileActivity.class);
    }

    public static void gotoUpdateNickActivity(Activity context){
        Intent intent = new Intent();
        intent.setClass(context, UpdateNickActivity.class);
        startActivityForResult(context,intent,I.REQUEST_CODE_UPDATE_NICK);
    }

    public static void gotoCollectsActivity(Activity context){
        startActivity(context, CollectsActivity.class);
    }
}
