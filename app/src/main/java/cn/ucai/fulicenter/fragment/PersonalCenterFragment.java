package cn.ucai.fulicenter.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.activity.UserProfileActivity;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends BaseFragment {


    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    MainActivity mContext;

    public PersonalCenterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_personal_center, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        UserAvatar userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar == null){
            MFGT.gotoLoginActivity(mContext);
        }else {
            ImageLoader.setUserAvatar(ImageLoader.getAvatarUrl(userAvatar),mContext,ivUserAvatar);
            tvUserName.setText(userAvatar.getMuserNick());
        }
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.tv_center_settings)
    public void onClick() {
       MFGT.gotoUserProfileActivity(mContext);
    }
}
