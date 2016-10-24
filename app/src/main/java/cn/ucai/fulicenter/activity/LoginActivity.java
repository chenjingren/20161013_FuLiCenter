package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;
import cn.ucai.fulicenter.utils.SharePreferencesUtils;

public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getName();

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;

    LoginActivity mContext;

    String userName;
    String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        DisplayUtils.initBack(mContext);
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
                MFGT.gotoRegisterActivity(mContext);
                break;
        }
    }

    private void login() {
        userName = username.getText().toString().trim();
        passWord = password.getText().toString().trim();
        if (TextUtils.isEmpty(userName)){
            CommonUtils.showLongToast(R.string.user_name_connot_be_empty);
            username.requestFocus();
            return;
        }else  if (TextUtils.isEmpty(passWord)){
            CommonUtils.showLongToast(R.string.password_connot_be_empty);
            username.requestFocus();
            return;
        }
        L.e(TAG,"userName==="+userName+"password===="+passWord);
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.logining));
        pd.show();
        NetDao.reqLogin(mContext, userName, passWord, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str, UserAvatar.class);
                L.e(TAG,"result===="+result);
                if (result == null){
                    CommonUtils.showLongToast(R.string.login_fail);
                }else {
                    if (result.isRetMsg()){

                        UserAvatar user = (UserAvatar) result.getRetData();

                        L.e(TAG,"user===="+user);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.saveUser(user);
                        if (isSuccess){
                            SharePreferencesUtils.getInstance(mContext).saveUser(user.getMuserName());
                            FuLiCenterApplication.setUserAvatar(user);
                            MFGT.finish(mContext);
                        }else {
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }

                    }else {
                        if (result.getRetCode() == I.MSG_LOGIN_UNKNOW_USER){
                            CommonUtils.showLongToast(R.string.login_fail_unknow_username);
                        }else if(result.getRetCode() == I.MSG_LOGIN_ERROR_PASSWORD){
                            CommonUtils.showLongToast(R.string.login_fail_error_password);
                        }else {
                            CommonUtils.showLongToast(R.string.login_fail);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(R.string.login_fail);
                L.e(TAG,"error====="+error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_REGISTER){
            String name = data.getStringExtra(I.User.USER_NAME);
            username.setText(name);
        }
    }
}
