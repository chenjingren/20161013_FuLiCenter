package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class BoutiqueChildActivity extends AppCompatActivity {

    public static final String TAG = BoutiqueChildActivity.class.getName();

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boutique_child);
        ButterKnife.bind(this);
        int catId = getIntent().getIntExtra(I.Boutique.CAT_ID, 0);
        L.e(TAG,"catId====="+catId);
        initView();
        initData();
        setListener();
    }

    private void initView() {

    }

    private void initData() {

    }

    private void setListener() {

    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
    }
}
