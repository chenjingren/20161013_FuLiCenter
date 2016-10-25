package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;

public class UserProfileActivity extends BaseActivity {

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

    @OnClick({R.id.iv_jt1, R.id.iv_jt2, R.id.iv_jt3, R.id.btn_exit_login})
    public void onClick(View view) {
        switch (view.getId()) {
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
