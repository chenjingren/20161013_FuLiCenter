package cn.ucai.fulicenter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.ucai.fulicenter.R;

/**
 * Created by ACherish on 2016/10/17.
 */
public class FooterViewHolder extends RecyclerView.ViewHolder{
    TextView mtvFooter;
    public FooterViewHolder(View itemView) {
        super(itemView);
        mtvFooter = (TextView) itemView.findViewById(R.id.tvFooter);
    }
}
