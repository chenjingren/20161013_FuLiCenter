package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class UpdateNickActivity extends BaseActivity {

    public static final String TAG = UpdateNickActivity.class.getName();

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_user_nick)
    EditText etUserNick;

    Activity mContext;

    UserAvatar userAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_update_nick);
        ButterKnife.bind(this);
        mContext =this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext,"修改昵称");
    }

    @Override
    protected void initData() {
        userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar!=null){
            etUserNick.setText(userAvatar.getMuserNick());
            etUserNick.setSelectAllOnFocus(true);
        }else {
            finish();
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.btn_save)
    public void onClick(View view) {
        if (userAvatar!=null){
            String nick = etUserNick.getText().toString().trim();
            if (nick.equals(userAvatar.getMuserNick())){
                CommonUtils.showLongToast(R.string.update_user_nick_is_same);
            }else if (TextUtils.isEmpty(nick)){
                CommonUtils.showLongToast(R.string.nick_name_connot_be_empty);
            }else {
                updateUserNick(nick);
            }
        }
    }

    private void updateUserNick(String nick) {
        final ProgressDialog pd = new ProgressDialog(mContext);
        pd.setMessage(getResources().getString(R.string.updating_user_nick));
        pd.show();
        NetDao.reqUpdateUserNick(mContext, userAvatar.getMuserName(), nick, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String str) {
                Result result = ResultUtils.getResultFromJson(str, UserAvatar.class);
                L.e(TAG,"result===="+result);
                if (result == null){
                    CommonUtils.showLongToast(R.string.update_user_nick_fail);
                }else {
                    if (result.isRetMsg()){

                        UserAvatar user = (UserAvatar) result.getRetData();

                        L.e(TAG,"user===="+user);
                        UserDao dao = new UserDao(mContext);
                        boolean isSuccess = dao.saveUser(user);
                        if (isSuccess){
                            FuLiCenterApplication.setUserAvatar(user);
                            setResult(RESULT_OK);
                            MFGT.finish(mContext);
                        }else {
                            CommonUtils.showLongToast(R.string.user_database_error);
                        }
                    }else {
                        if (result.getRetCode() == I.MSG_USER_SAME_NICK){
                            CommonUtils.showLongToast(R.string.update_user_nick_is_same);
                        }else if(result.getRetCode() == I.MSG_USER_UPDATE_NICK_FAIL){
                            CommonUtils.showLongToast(R.string.update_user_nick_fail);
                        }else {
                            CommonUtils.showLongToast(R.string.update_user_nick_fail);
                        }
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onError(String error) {
                pd.dismiss();
                CommonUtils.showLongToast(R.string.update_user_nick_fail);
                L.e(TAG,"error====="+error);
            }
        });
    }
}
