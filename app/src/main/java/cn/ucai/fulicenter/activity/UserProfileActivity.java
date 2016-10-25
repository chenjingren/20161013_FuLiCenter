package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_use_profile);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        tvCommonTitle.setText("个人资料");
    }

    @Override
    protected void initData() {
        UserAvatar userAvatar = FuLiCenterApplication.getUserAvatar();
        tvUserName.setText(userAvatar.getMuserName());
        tvUserNick.setText(userAvatar.getMuserNick());
        ImageLoader.setUserAvatar(
                ImageLoader.getAvatarUrl(userAvatar),mContext,ivUserAvatar);
    }

    @Override
    protected void setListener() {

    }

    @OnClick({R.id.backClickArea,R.id.iv_jt1, R.id.iv_jt2, R.id.iv_jt3, R.id.btn_exit_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(mContext);
                break;
            case R.id.iv_jt1:
                break;
            case R.id.iv_jt2:
                break;
            case R.id.iv_jt3:
                break;
            case R.id.btn_exit_login:
                break;
        }
    }
}
