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

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {

    public static final String TAG = NewGoodsFragment.class.getName();

    @BindView(R.id.tvRefresh)
    TextView tvRefresh;
    @BindView(R.id.rl)
    RecyclerView rl;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;

    public static final int PAGE_ID = 1;

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
        initView();
        initData();
        return layout;
    }

    private void initView() {
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

    }

    private void initData() {
        NetDao.downloadNewGoods(context, PAGE_ID, new OkHttpUtils.OnCompleteListener<NewGoodsBean[]>() {
            @Override
            public void onSuccess(NewGoodsBean[] result) {
                tvRefresh.setVisibility(View.GONE);
                srl.setRefreshing(false);
                if (result!=null &&result.length>0){
                    ArrayList<NewGoodsBean> goodsList = ConvertUtils.array2List(result);
                    mAdapter.initList(goodsList);
                    if (goodsList.size()<I.PAGE_SIZE_DEFAULT){
                        mAdapter.setMore(false);
                    }
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
}
