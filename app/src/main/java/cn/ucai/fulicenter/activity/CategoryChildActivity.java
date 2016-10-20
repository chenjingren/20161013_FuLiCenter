package cn.ucai.fulicenter.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodsAdapter;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

public class CategoryChildActivity extends BaseActivity {

    public static final String TAG = CategoryChildActivity.class.getName();

    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rl)
    RecyclerView rl;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    //CategoryChildBean childBean;

    GoodsAdapter mAdapter;
    ArrayList<NewGoodsBean> mList;
    GridLayoutManager mGridLayoutManager;

    CategoryChildActivity mContext;

    int pageId = 1;

    int catId;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;

    boolean isSortByPriceAsc;
    boolean isSortByAddTimeAsc;

    int sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
        catId = getIntent().getIntExtra(I.CategoryChild.CAT_ID, 0);
        //childBean = (CategoryChildBean) getIntent().getSerializableExtra(I.CategoryChild.CAT_ID);
        //L.e(TAG,childBean.toString());
        L.e(TAG, "CATiD=====" + catId);
    }

    @Override
    protected void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_yellow)
        );

        mGridLayoutManager = new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mList = new ArrayList<>();
        mAdapter = new GoodsAdapter(mContext, mList);

        rl.setHasFixedSize(true);
        rl.setLayoutManager(mGridLayoutManager);
        rl.setAdapter(mAdapter);
        rl.addItemDecoration(new SpaceItemDecoration(12));

    }

    @Override
    protected void initData() {
        showCategoryChild(I.ACTION_DOWNLOAD);

    }

    private void showCategoryChild(final int action) {
        NetDao.downloadCategoryChild(mContext, catId, pageId, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                if (result != null && result.length > 0) {
                    ArrayList<NewGoodsBean> list = ConvertUtils.array2List(result);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initList(list);
                    } else {
                        mAdapter.addList(list);
                    }
                    if (list.size() < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                    }
                } else {
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    mAdapter.setMore(false);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    protected void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl.setRefreshing(true);
                tvRefresh.setVisibility(View.GONE);
                pageId = 1;
                showCategoryChild(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        rl.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mGridLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        mAdapter.getItemCount() - 1 == lastPosition &&
                        mAdapter.isMore()) {
                    pageId++;
                    showCategoryChild(I.ACTION_PULL_UP);
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

    @OnClick(R.id.backClickArea)
    public void onClick() {
        MFGT.finish(mContext);
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void onClick(View view) {
        Drawable drawable;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                if (isSortByPriceAsc){
                    sortBy = I.SORT_BY_PRICE_ASC;
                    drawable = getResources().getDrawable(R.mipmap.arrow_order_up);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    btnSortPrice.setCompoundDrawables(null,null,drawable,null);
                }else {
                    sortBy = I.SORT_BY_PRICE_DESC;
                    drawable = getResources().getDrawable(R.mipmap.arrow_order_down);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    btnSortPrice.setCompoundDrawables(null,null,drawable,null);
                }
                isSortByPriceAsc = !isSortByPriceAsc;
                break;
            case R.id.btn_sort_addtime:
                if (isSortByAddTimeAsc){
                    sortBy = I.SORT_BY_ADDTIME_ASC;
                    drawable = getResources().getDrawable(R.mipmap.arrow_order_up);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    btnSortAddtime.setCompoundDrawables(null,null,drawable,null);
                }else {
                    sortBy = I.SORT_BY_ADDTIME_DESC;
                    drawable = getResources().getDrawable(R.mipmap.arrow_order_down);
                    drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
                    btnSortAddtime.setCompoundDrawables(null,null,drawable,null);
                }
                isSortByAddTimeAsc = !isSortByAddTimeAsc;
                break;
        }
        mAdapter.setSortBy(sortBy);
    }
}
