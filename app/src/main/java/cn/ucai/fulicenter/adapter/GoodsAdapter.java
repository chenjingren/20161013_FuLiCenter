package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
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
import cn.ucai.fulicenter.activity.GoodDetailsActivity;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.bean.NewGoodsBean;
import cn.ucai.fulicenter.holder.FooterViewHolder;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by ACherish on 2016/10/17.
 */
public class GoodsAdapter extends RecyclerView.Adapter{

    public static final String TAG = GoodsAdapter.class.getName();

    Context mContext;
    ArrayList<NewGoodsBean> mNewGoodsList;

    boolean isMore;

    String tvFooter;

    int sortBy = I.SORT_BY_ADDTIME_ASC;

    public GoodsAdapter(Context mContext, ArrayList<NewGoodsBean> mNewGoodsList) {
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

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
        sortBy();
        notifyDataSetChanged();
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
        L.e(TAG,"onBindViewHolder position======="+position);
        if (getItemViewType(position)== I.TYPE_FOOTER){
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            footerViewHolder.tvFooter.setText(getFooterText());
            return;
        }
        //L.e(TAG,"onBindViewHolder position======="+position);
        NewGoodsBean goods = mNewGoodsList.get(position);
        NewGoodsViewHolder goodsViewHolder = (NewGoodsViewHolder) holder;
        goodsViewHolder.tvGoodsPrice.setText(goods.getCurrencyPrice());
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

    public void initList(ArrayList<NewGoodsBean> goodsList) {
        if (mNewGoodsList!=null){
            mNewGoodsList.clear();
        }
        mNewGoodsList.addAll(goodsList);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<NewGoodsBean> goodsList) {
        mNewGoodsList.addAll(goodsList);
        notifyDataSetChanged();
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
            //
        }
    }

    public  void sortBy(){
        Collections.sort(mNewGoodsList, new Comparator<NewGoodsBean>() {
            @Override
            public int compare(NewGoodsBean o1, NewGoodsBean o2) {
                int result = 0;
                switch (sortBy){
                    case I.SORT_BY_ADDTIME_ASC:
                        result = (int) (o1.getAddTime()-o2.getAddTime());
                        break;
                    case I.SORT_BY_ADDTIME_DESC:
                        result = (int) (o2.getAddTime()-o1.getAddTime());
                        break;
                    case I.SORT_BY_PRICE_ASC:
                        result = getPrice(o1.getCurrencyPrice())-getPrice(o2.getCurrencyPrice());
                        break;
                    case I.SORT_BY_PRICE_DESC:
                        result = getPrice(o2.getCurrencyPrice())-getPrice(o1.getCurrencyPrice());
                        break;
                }
                return result;
            }

            public  int getPrice(String price){
                int p;
                price = price.substring(price.indexOf("￥")+1);
                p = Integer.valueOf(price);
                return p;
            }
        });
    }
}
