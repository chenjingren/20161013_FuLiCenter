package cn.ucai.fulicenter.fragment;


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
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.dao.UserDao;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.utils.ResultUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends BaseFragment {

    public static final String TAG = PersonalCenterFragment.class.getName();

    @BindView(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    MainActivity mContext;

    UserAvatar userAvatar;
    @BindView(R.id.tv_collect_count)
    TextView tvCollectCount;

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
        userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar == null) {
            MFGT.gotoLoginActivity(mContext);
        } else {
            ImageLoader.setUserAvatar(ImageLoader.getAvatarUrl(userAvatar), mContext, ivUserAvatar);
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

    @Override
    public void onResume() {
        super.onResume();
        userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar != null) {
            ImageLoader.setUserAvatar(ImageLoader.getAvatarUrl(userAvatar), mContext, ivUserAvatar);
            tvUserName.setText(userAvatar.getMuserNick());
            syncUserInfo();
            showCollectCounts();
        }
    }

    public void syncUserInfo() {
        NetDao.reqFindUserByUsername(mContext, userAvatar.getMuserName(), new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                Result result = ResultUtils.getResultFromJson(s, UserAvatar.class);
                L.e(TAG, "findUser.result====" + result);
                if (result.isRetMsg()) {
                    UserAvatar user = (UserAvatar) result.getRetData();
                    if (!user.equals(userAvatar)) {
                        UserDao dao = new UserDao(mContext);
                        boolean b = dao.saveUser(user);
                        if (b) {
                            FuLiCenterApplication.setUserAvatar(user);
                            userAvatar = user;
                            ImageLoader.setUserAvatar(ImageLoader.getAvatarUrl(userAvatar), mContext, ivUserAvatar);
                            tvUserName.setText(userAvatar.getMuserName());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "syncUserInfo.error=====" + error);
            }
        });
    }

    public void showCollectCounts() {
        NetDao.reqFindUserCollectCounts(mContext, userAvatar.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
            @Override
            public void onSuccess(MessageBean mes) {
                if (mes != null && mes.isSuccess()) {
                    L.e(TAG, "count====" + mes.getMsg());
                    tvCollectCount.setText(mes.getMsg());
                } else {
                    tvCollectCount.setText(String.valueOf(0));
                }
            }

            @Override
            public void onError(String error) {
                tvCollectCount.setText(String.valueOf(0));
                L.e(TAG, "showCollectCounts.error=====" + error);
            }
        });
    }

    @OnClick(R.id.layout_center_collect)
    public void gotoCollect() {
        MFGT.gotoCollectsActivity(mContext);
    }
}
