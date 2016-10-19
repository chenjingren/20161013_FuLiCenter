package cn.ucai.fulicenter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by ACherish on 2016/10/17.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.tvFooter)
    public TextView tvFooter;

    public FooterViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
