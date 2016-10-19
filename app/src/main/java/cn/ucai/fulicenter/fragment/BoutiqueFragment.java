package cn.ucai.fulicenter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.BoutiqueAdapter;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;
import cn.ucai.fulicenter.views.SpaceItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoutiqueFragment extends Fragment {

    public static final String TAG = BoutiqueFragment.class.getName();

    BoutiqueAdapter mAdapter;
    ArrayList<BoutiqueBean> mList;

    MainActivity mContext;

    LinearLayoutManager mLayoutManager;

    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rl)
    RecyclerView rl;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    public BoutiqueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_boutique, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        initView();
        initData();
        setListener();
        return layout;
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_yellow),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_green)
        );

        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mList = new ArrayList<>();
        mAdapter = new BoutiqueAdapter(mContext,mList);
        rl.setLayoutManager(mLayoutManager);
        rl.setHasFixedSize(true);
        rl.setAdapter(mAdapter);
        rl.addItemDecoration(new SpaceItemDecoration(20));
    }

    private void initData() {
        showBoutique(I.ACTION_DOWNLOAD);
    }

    private void showBoutique(final int action) {
        NetDao.downloadBoutique(mContext, new OkHttpUtils.OnCompleteListener<BoutiqueBean[]>() {
            @Override
            public void onSuccess(BoutiqueBean[] result) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(true);
                mAdapter.setTvFooterText(String.valueOf(R.string.load_more));
                if (result!=null && result.length>0){
                    ArrayList<BoutiqueBean> list = ConvertUtils.array2List(result);
                    if (action== I.ACTION_DOWNLOAD|| action == I.ACTION_PULL_DOWN){
                        mAdapter.initList(list);
                    }else {
                        mAdapter.addList(list);
                    }
                    if (list.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                        mAdapter.setTvFooterText(String.valueOf(R.string.no_more));
                    }
                }else {
                    srl.setRefreshing(false);
                    tvRefresh.setVisibility(View.GONE);
                    mAdapter.setMore(false);
                    mAdapter.setTvFooterText(String.valueOf(R.string.no_more));
                }
            }

            @Override
            public void onError(String error) {
                srl.setRefreshing(false);
                tvRefresh.setVisibility(View.GONE);
                mAdapter.setMore(false);
                L.e(TAG,"error====="+error);
                CommonUtils.showLongToast(error);
            }
        });
    }


    private void setListener() {
        setPullDownloadListener();
        setPullUpListener();
    }

    private void setPullDownloadListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showBoutique(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        rl.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = mLayoutManager.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastPosition == mAdapter.getItemCount()-1 &&
                        mAdapter.isMore()){

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition==0);
            }
        });
    }

}
