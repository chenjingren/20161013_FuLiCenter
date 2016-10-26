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

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CollectBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.holder.FooterViewHolder;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by ACherish on 2016/10/17.
 */
public class CollectsAdapter extends RecyclerView.Adapter{

    public static final String TAG = CollectsAdapter.class.getName();

    Context mContext;
    ArrayList<CollectBean> mNewGoodsList;

    boolean isMore;

    String tvFooter;

    UserAvatar userAvatar;
    
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
        final CollectBean goods = mNewGoodsList.get(position);
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

        goodsViewHolder.ivDeleteCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAvatar = FuLiCenterApplication.getUserAvatar();
                if (userAvatar!=null){
                    NetDao.reqDeleteCollect(mContext, goods.getGoodsId(), userAvatar.getMuserName(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            if (result!=null && result.isSuccess()){
                                mNewGoodsList.remove(goods);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            L.e(TAG,"deleteCollect.error===="+error);
                        }
                    });
                }

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
        ImageView ivDeleteCollect;
        public CollectViewHolder(View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_collects);
            ivGoodsThumb = (ImageView) itemView.findViewById(R.id.ivGoodsThumb);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            ivDeleteCollect = (ImageView) itemView.findViewById(R.id.iv_collect_del);
            //设置列表项单击事件监听
            //
        }
    }
}
