package cn.ucai.fulicenter.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.nick)
    EditText nick;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;

    RegisterActivity mContext;

    String userName;
    String userNick;
    String passWord;
    String confirmPwd;

    public static final String TAG = RegisterActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,"用户注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_register)
    public void onClick() {
        userName = username.getText().toString().trim();
        userNick = nick.getText().toString().trim();
        passWord = password.getText().toString().trim();
        confirmPwd = confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)){
            CommonUtils.showLongToast(getResources().getString(R.string.user_name_connot_be_empty));
            username.requestFocus();
            return;
        }else if (!userName.matches("[a-zA-Z]\\w{5,15}")){
            CommonUtils.showLongToast(getResources().getString(R.string.illegal_user_name));
            username.requestFocus();
            return;
        }else if (TextUtils.isEmpty(userNick)){
            CommonUtils.showLongToast(getResources().getString(R.string.nick_name_connot_be_empty));
            nick.requestFocus();
            return;
        }else if (TextUtils.isEmpty(passWord)){
            CommonUtils.showLongToast(getResources().getString(R.string.password_connot_be_empty));
            password.requestFocus();
            return;
        }else if (TextUtils.isEmpty(confirmPwd)){
            CommonUtils.showLongToast(getResources().getString(R.string.confirm_password_connot_be_empty));
            confirmPassword.requestFocus();
            return;
        }else if (!passWord.equals(confirmPwd)){
            CommonUtils.showLongToast(getResources().getString(R.string.two_input_password));
            confirmPassword.requestFocus();
            return;
        }
        register();
    }

    private void register() {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.registering));
        pd.show();
        NetDao.reqRegister(mContext, userName, userNick, passWord, new OkHttpUtils.OnCompleteListener<Result>() {
            @Override
            public void onSuccess(Result result) {
                pd.dismiss();
                if (result == null){
                    CommonUtils.showLongToast(R.string.register_fail);
                }else {
                    if (result.isRetMsg()){
                        CommonUtils.showLongToast(R.string.register_success);
                        MFGT.finish(mContext);
                    }else {
                        CommonUtils.showLongToast(R.string.register_fail);
                        username.requestFocus();
                    }
                }
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(error);
                L.e(TAG,"error===="+error);
            }
        });
    }
}
