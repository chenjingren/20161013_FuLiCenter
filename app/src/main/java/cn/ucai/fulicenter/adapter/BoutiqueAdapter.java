package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.holder.FooterViewHolder;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by ACherish on 2016/10/19.
 */
public class BoutiqueAdapter extends Adapter {
    Context mContext;
    ArrayList<BoutiqueBean> mList;

    boolean isMore;

    String tvFooterText;

    public BoutiqueAdapter(Context context, ArrayList<BoutiqueBean> list) {
        this.mContext = context;
        mList = new ArrayList<>();
        mList = list;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public String getTvFooterText() {
        return tvFooterText;
    }

    public void setTvFooterText(String tvFooterText) {
        this.tvFooterText = tvFooterText;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == I.TYPE_FOOTER) {
            holder = new BoutiqueViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_boutique, parent, false));
        } else {
            holder = new FooterViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).tvFooter.setText(getTvFooterText());
        }
        if (holder instanceof BoutiqueViewHolder) {
            BoutiqueBean boutiqueBean = mList.get(position);
            ((BoutiqueViewHolder) holder).tvTitle.setText(boutiqueBean.getTitle());
            ((BoutiqueViewHolder) holder).tv.setText(boutiqueBean.getName());
            ((BoutiqueViewHolder) holder).tvDes.setText(boutiqueBean.getDescription());
            ImageLoader.downloadImg(mContext,((BoutiqueViewHolder) holder).ivBoutique,boutiqueBean.getImageurl());
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_boutique)
        ImageView ivBoutique;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_name)
        TextView tv;
        @BindView(R.id.tv_desc)
        TextView tvDes;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tvFooter)
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
