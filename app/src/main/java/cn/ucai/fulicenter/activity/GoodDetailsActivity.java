package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.FlowIndicator;
import cn.ucai.fulicenter.views.SlideAutoLoopView;

public class GoodDetailsActivity extends BaseActivity {

    @BindView(R.id.backClickArea)
    LinearLayout backClickArea;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;

    int goodsId;

    GoodDetailsActivity mContext;

    boolean isCollected;

    public static final String TAG = GoodDetailsActivity.class.getName();
    @BindView(R.id.iv_good_collect)
    ImageView ivGoodCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_good_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e(TAG, "goodsId=====" + goodsId);
        if (goodsId == 0) {
            finish();
        }
        mContext = this;
        super.onCreate(savedInstanceState);
        /*initView();
        initData();
        setListener();*/
    }

    @Override
    protected void initView() {

    }

    @OnClick(R.id.iv_good_collect)
    public void onAddCollect(){
        UserAvatar userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar!=null){
            if (isCollected){
                NetDao.reqAddCollect(mContext, goodsId, userAvatar.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess()){
                            showBitmap();
                            isCollected = !isCollected;
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                NetDao.reqDeleteCollect(mContext, goodsId, userAvatar.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess()){
                            showBitmap();
                            isCollected = !isCollected;
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }else {
            MFGT.gotoLoginActivity(mContext);
        }
    }

    @Override
    protected void initData() {
        NetDao.downloadGoodDetails(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.e(TAG, "RESULT===" + result);
                if (result != null) {
                    showGoodDetails(result);
                } else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "ERROR====" + error);
                CommonUtils.showLongToast(error);
                finish();
            }
        });
    }

    private void showGoodDetails(GoodsDetailsBean goodsDetails) {
        tvGoodName.setText(goodsDetails.getGoodsName());
        tvGoodNameEnglish.setText(goodsDetails.getGoodsEnglishName());
        tvGoodPriceCurrent.setText(goodsDetails.getCurrencyPrice());
        tvGoodPriceShop.setText(goodsDetails.getShopPrice());

        salv.startPlayLoop(indicator, getAlbumUrl(goodsDetails), getAlbumCount(goodsDetails));
        wvGoodBrief.loadDataWithBaseURL(null, goodsDetails.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);
    }

    private String[] getAlbumUrl(GoodsDetailsBean goodsDetails) {
        String[] url = new String[]{};
        if (goodsDetails.getPropertiesBean() != null && goodsDetails.getPropertiesBean().length > 0) {
            AlbumsBean[] albumsBean =
                    goodsDetails.getPropertiesBean()[0].getAlbumsBean();
            for (int i = 0; i < albumsBean.length; i++) {
                url = new String[albumsBean.length];
                url[i] = albumsBean[i].getImgUrl();
            }
        }
        return url;
    }

    private int getAlbumCount(GoodsDetailsBean goodsDetails) {
        if (goodsDetails.getPropertiesBean() != null && goodsDetails.getPropertiesBean().length > 0) {
            return goodsDetails.getPropertiesBean()[0].getAlbumsBean().length;
        }
        return 0;
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.backClickArea)
    public void onBackClick() {
        MFGT.finish(mContext);
    }

    public void isCollect() {
        UserAvatar userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar != null) {
            NetDao.reqIsCollect(mContext, goodsId, userAvatar.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result != null && result.isSuccess()) {
                        isCollected = true;
                        showBitmap();
                    }
                }

                @Override
                public void onError(String error) {
                    ivGoodCollect.setImageResource(R.mipmap.bg_collect_in);
                    L.e(TAG,"isCollect.error===="+error);
                }
            });
        }
    }

    public void showBitmap(){
        if (isCollected){
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_out);
        }else {
            ivGoodCollect.setImageResource(R.mipmap.bg_collect_in);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        isCollect();
    }

    @OnClick(R.id.iv_good_share)
        public void showShare() {
            ShareSDK.initSDK(this);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();

            // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            oks.setTitle("标题");
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl("http://sharesdk.cn");
            // text是分享文本，所有平台都需要这个字段
            oks.setText("我是分享文本");
            //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
            oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl("http://sharesdk.cn");
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite("ShareSDK");
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl("http://sharesdk.cn");

            // 启动分享GUI
            oks.show(this);
        }
}
