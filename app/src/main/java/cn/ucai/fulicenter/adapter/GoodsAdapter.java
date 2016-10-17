package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.holder.FooterViewHolder;
import cn.ucai.fulicenter.utils.ImageLoader;

/**
 * Created by ACherish on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter{
    Context mContext;
    ArrayList<NewGoodsBean> mNewGoodsList;

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> mNewGoodsList) {
        this.mContext = mContext;
        this.mNewGoodsList = mNewGoodsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType==I.TYPE_FOOTER){
            holder = new FooterViewHolder(View.inflate(mContext,R.layout.item_footer,null));
        }else {
            holder = new NewGoodsViewHolder(View.inflate(mContext,R.layout.item_goods,null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)== I.TYPE_FOOTER){

            return;
        }
        NewGoodsBean goods = mNewGoodsList.get(position);
        NewGoodsViewHolder goodsViewHolder = (NewGoodsViewHolder) holder;
        goodsViewHolder.tvGoodsPrice.setText(goods.getCurrencyPrice());
        goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
        ImageLoader.setImage(I.IMAGE_URL,mContext,goodsViewHolder.ivGoodsThumb,false);
    }

    @Override
    public int getItemCount() {
        return mNewGoodsList!=null?mNewGoodsList.size()+1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1){
            return I.TYPE_FOOTER;
        }
            return I.TYPE_ITEM;

    }

    class NewGoodsViewHolder extends RecyclerView.ViewHolder{
        View layoutItem;
        ImageView ivGoodsThumb;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        public NewGoodsViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_goods);
            ivGoodsThumb = (ImageView) itemView.findViewById(R.id.ivGoodsThumb);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tvGoodsPrice);
            //设置列表项单击事件监听
            //layoutItem.setOnClickListener(mOnItemClickListener);
        }
    }
}
