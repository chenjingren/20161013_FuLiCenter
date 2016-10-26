package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CollectsAdapter;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.DisplayUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class CollectsActivity extends BaseActivity {

    public static final String TAG = CollectsActivity.class.getName();


    public static int pageId = 1;

    GridLayoutManager mGridLayoutManager;

    CollectsActivity context;

    CollectsAdapter mAdapter;

    UserAvatar userAvatar;

    ArrayList<CollectBean> mList;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rl)
    RecyclerView rl;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collects);
        ButterKnife.bind(this);
        context =this;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setListener() {
        setPullDownloadListener();
        setPullUpListener();
    }

    private void setPullUpListener() {
        rl.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mGridLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == mAdapter.getItemCount() - 1
                        && mAdapter.isMore()) {
                    pageId++;
                    downLoadCollectsGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = mGridLayoutManager.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition == 0);
            }
        });
    }

    private void setPullDownloadListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId = 1;
                downLoadCollectsGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downLoadCollectsGoods(final int action) {
        userAvatar = FuLiCenterApplication.getUserAvatar();
        if (userAvatar!=null){
            NetDao.reqFindCollects(context, userAvatar.getMuserName(), pageId, new OkHttpUtils.OnCompleteListener<CollectBean[]>() {
                @Override
                public void onSuccess(CollectBean[] result) {
                    tvRefresh.setVisibility(View.GONE);
                    srl.setRefreshing(false);
                    mAdapter.setMore(true);
                    if (result != null && result.length > 0) {
                        ArrayList<CollectBean> goodsList = ConvertUtils.array2List(result);
                        if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                            mAdapter.initList(goodsList);
                        } else {
                            mAdapter.addList(goodsList);
                        }
                        if (goodsList.size() < I.PAGE_SIZE_DEFAULT) {
                            mAdapter.setMore(false);
                        }
                    } else {
                        tvRefresh.setVisibility(View.GONE);
                        srl.setRefreshing(false);
                        mAdapter.setMore(false);
                    }
                }

                @Override
                public void onError(String error) {
                    tvRefresh.setVisibility(View.GONE);
                    srl.setRefreshing(false);
                    mAdapter.setMore(false);
                    CommonUtils.showLongToast(error);
                    L.e(TAG, "error==" + error);
                }
            });
        }
    }

    @Override
    protected void initView() {
        DisplayUtils.initBackWithTitle(context,"收藏商品");
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue)
                , getResources().getColor(R.color.google_green)
                , getResources().getColor(R.color.google_red)
                , getResources().getColor(R.color.google_yellow)
        );

        mGridLayoutManager = new GridLayoutManager(context, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl.setLayoutManager(mGridLayoutManager);
        rl.setHasFixedSize(true);
        mList = new ArrayList<CollectBean>();
        mAdapter = new CollectsAdapter(context, mList);
        rl.setAdapter(mAdapter);
        rl.addItemDecoration(new SpaceItemDecoration(20));
    }

    @Override
    protected void initData() {
        downLoadCollectsGoods(I.ACTION_DOWNLOAD);
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
    }
}
