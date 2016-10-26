package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.SharePreferencesUtils;

public class UserProfileActivity extends BaseActivity {

    Activity mContext;

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_nick)
    TextView tvUserNick;

    UserAvatar userAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_use_profile);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(mContext, "个人资料");
    }

    @Override
    protected void initData() {
        userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar==null){
            finish();
        }else {
           showUserProfile();
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.layout_user_avatar, R.id.layout_user_name, R.id.layout_user_nick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_user_avatar:
                break;
            case R.id.layout_user_name:
                CommonUtils.showLongToast("用户名不能修改");
                break;
            case R.id.layout_user_nick:
                MFGT.gotoUpdateNickActivity(mContext);
                break;
        }
    }

    @OnClick(R.id.btn_exit_login)
    public void onClick() {
        if (userAvatar!=null){
            SharePreferencesUtils.getInstance(mContext).removeUser();
            FuLiCenterApplication.setUserAvatar(null);
            MFGT.gotoLoginActivity(mContext);
        }
        finish();
    }

    private void showUserProfile(){
        userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar!=null){
            tvUserName.setText(userAvatar.getMuserName());
            tvUserNick.setText(userAvatar.getMuserNick());
            ImageLoader.setUserAvatar(
                    ImageLoader.getAvatarUrl(userAvatar), mContext, ivUserAvatar);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == I.REQUEST_CODE_UPDATE_NICK && resultCode == RESULT_OK){
            CommonUtils.showLongToast(R.string.update_user_nick_success);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserProfile();
    }
}
