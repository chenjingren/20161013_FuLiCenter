package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumsBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
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

    public static final String TAG = GoodDetailsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_good_details);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
        L.e(TAG, "goodsId=====" + goodsId);
        if (goodsId==0){
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

    @Override
    protected void initData() {
        NetDao.downloadGoodDetails(mContext, goodsId, new OkHttpUtils.OnCompleteListener<GoodsDetailsBean>() {
            @Override
            public void onSuccess(GoodsDetailsBean result) {
                L.e(TAG,"RESULT==="+result);
                if (result!=null){
                    showGoodDetails(result);
                }else {
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG,"ERROR===="+error);
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

        salv.startPlayLoop(indicator,getAlbumUrl(goodsDetails),getAlbumCount(goodsDetails));
        wvGoodBrief.loadDataWithBaseURL(null,goodsDetails.getGoodsBrief(),I.TEXT_HTML,I.UTF_8,null);
    }

    private String[] getAlbumUrl(GoodsDetailsBean goodsDetails) {
        String[] url = new String[]{};
        if (goodsDetails.getPropertiesBean()!=null && goodsDetails.getPropertiesBean().length>0){
            AlbumsBean[] albumsBean =
                    goodsDetails.getPropertiesBean()[0].getAlbumsBean();
            for (int i = 0;i<albumsBean.length;i++){
                url = new String[albumsBean.length];
                url[i] = albumsBean[i].getImgUrl();
            }
        }
        return url;
    }

    private int getAlbumCount(GoodsDetailsBean goodsDetails) {
        if (goodsDetails.getPropertiesBean()!=null && goodsDetails.getPropertiesBean().length>0) {
                    return goodsDetails.getPropertiesBean()[0].getAlbumsBean().length;
        }
        return 0;
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.backClickArea)
    public void onBackClick(){
        MFGT.finish(mContext);
    }

}
