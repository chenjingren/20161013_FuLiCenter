package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends BaseFragment {

    public static final String TAG = NewGoodsFragment.class.getName();

    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rl)
    RecyclerView rl;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    public static int pageId = 1;

    GridLayoutManager mGridLayoutManager;

    MainActivity context;

    GoodsAdapter mAdapter;

    ArrayList<NewGoodsBean> mList;
    public NewGoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, layout);
        context = (MainActivity) getContext();
        super.onCreateView(inflater,container,savedInstanceState);
        /*initView();
        initData();
        setListener();*/
        return layout;
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
                        && lastPosition==mAdapter.getItemCount()-1
                        && mAdapter.isMore()){
                    pageId++;
                    downLoadNewGoods(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = mGridLayoutManager.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition==0);
            }
        });
    }

    private void setPullDownloadListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.VISIBLE);
                pageId=1;
                downLoadNewGoods(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void downLoadNewGoods(final int action) {
        NetDao.downloadNewGoods(context,I.CAT_ID ,pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                tvRefresh.setVisibility(View.GONE);
                srl.setRefreshing(false);
                mAdapter.setMore(true);
                if (result!=null &&result.length>0){
                    ArrayList<NewGoodsBean> goodsList = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN){
                        mAdapter.initList(goodsList);
                    }else {
                        mAdapter.addList(goodsList);
                    }
                    if (goodsList.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
                }else {
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
                L.e(TAG,"error=="+error);
            }
        });
    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue)
                ,getResources().getColor(R.color.google_green)
                ,getResources().getColor(R.color.google_red)
                ,getResources().getColor(R.color.google_yellow)
        );

        mGridLayoutManager = new GridLayoutManager(context, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rl.setLayoutManager(mGridLayoutManager);
        rl.setHasFixedSize(true);
        mList = new ArrayList<NewGoodsBean>();
        mAdapter = new GoodsAdapter(context,mList);
        rl.setAdapter(mAdapter);
        rl.addItemDecoration(new SpaceItemDecoration(20));
    }

    @Override
    protected void initData() {
        downLoadNewGoods(I.ACTION_DOWNLOAD);
    }
}
