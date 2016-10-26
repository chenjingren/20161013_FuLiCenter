package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.holder.FooterViewHolder;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by ACherish on 2016/10/17.
 */
public class CollectsAdapter extends RecyclerView.Adapter{

    public static final String TAG = CollectsAdapter.class.getName();

    Context mContext;
    ArrayList<CollectBean> mNewGoodsList;

    boolean isMore;

    String tvFooter;
    
    public CollectsAdapter(Context mContext, ArrayList<CollectBean> mNewGoodsList) {
        this.mContext = mContext;
        this.mNewGoodsList = new ArrayList<>();
        this.mNewGoodsList.addAll(mNewGoodsList);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public void setTvFooter(String tvFooter) {
        this.tvFooter = tvFooter;
        notifyDataSetChanged();
    }
    

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType==I.TYPE_FOOTER){
            holder = new FooterViewHolder(View.inflate(mContext,R.layout.item_footer,null));
        }else {
            holder = new CollectViewHolder(View.inflate(mContext,R.layout.item_collect,null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        L.e(TAG,"onBindViewHolder position======="+position);
        if (getItemViewType(position)== I.TYPE_FOOTER){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooterText());
            return;
        }
        //L.e(TAG,"onBindViewHolder position======="+position);
        CollectBean goods = mNewGoodsList.get(position);
        CollectViewHolder goodsViewHolder = (CollectViewHolder) holder;
        goodsViewHolder.tvGoodsName.setText(goods.getGoodsName());
        ImageLoader.downloadImg(mContext,goodsViewHolder.ivGoodsThumb,goods.getGoodsThumb());

        final int goodsId = goods.getGoodsId();
        goodsViewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mContext.startActivity(new Intent(mContext, GoodDetailsActivity.class)
                        .putExtra(I.GoodsDetails.KEY_GOODS_ID,goodsId));*/
                MFGT.gotoGoodDetailsActivity(mContext,goodsId);
            }
        });
    }

    private int getFooterText() {
        return isMore?R.string.load_more:R.string.no_more;
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

    public void initList(ArrayList<CollectBean> goodsList) {
        if (mNewGoodsList!=null){
            mNewGoodsList.clear();
        }
        mNewGoodsList.addAll(goodsList);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<CollectBean> goodsList) {
        mNewGoodsList.addAll(goodsList);
        notifyDataSetChanged();
    }

    class CollectViewHolder extends RecyclerView.ViewHolder{
        View layoutItem;
        ImageView ivGoodsThumb;
        TextView tvGoodsName;
        ImageView tvGoodsPrice;
        public CollectViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_collects);
            ivGoodsThumb = (ImageView) itemView.findViewById(R.id.ivGoodsThumb);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = (ImageView) itemView.findViewById(R.id.iv_collect_del);
            //设置列表项单击事件监听
            //
        }
    }
}
