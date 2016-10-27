package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.holder.FooterViewHolder;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * Created by ACherish on 2016/10/19.
 */
public class CartAdapter extends Adapter {

    public static final String TAG = CartAdapter.class.getName();

    Context mContext;
    ArrayList<CartBean> mList;

    boolean isMore;

    String tvFooterText;

    //int catId;

    CartBean bean;

    public CartAdapter(Context context, ArrayList<CartBean> list) {
        this.mContext = context;
        this.mList = list;
        //this.mList = new ArrayList<CartBean>();
        //this.mList.addAll(list);
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
            holder = new FooterViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_footer, parent, false));
        } else {
            holder = new CartViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_cart, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        L.e(TAG, "onBindViewHolder position=======" + position);
        if (holder instanceof FooterViewHolder) {
            ((FooterViewHolder) holder).tvFooter.setText(getTvFooterText());
        }
        if (holder instanceof CartViewHolder) {
            CartBean cartBean = mList.get(position);
            GoodsDetailsBean goods = cartBean.getGoods();
            if (goods!=null){
                ImageLoader.downloadImg(mContext,((CartViewHolder) holder).ivGoodsThumb,goods.getGoodsThumb());
                ((CartViewHolder) holder).tvGoodsName.setText(goods.getGoodsName());
                ((CartViewHolder) holder).tvGoodsPrice.setText(goods.getCurrencyPrice());
            }
            ((CartViewHolder) holder).tvGoodsCount.setText(String.valueOf(cartBean.getCount()));
            ((CartViewHolder) holder).cbCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((CartViewHolder) holder).cbCart.setChecked(isChecked);
                    mContext.sendBroadcast(new Intent(I.BRAODCASE_UPDATE_GOODS_PRICE));
                }
            });

            ((CartViewHolder) holder).layoutItemCart.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    public void initList(ArrayList<CartBean> list) {
       /* if (mList != null) {
            mList.clear();
        }*/
       // mList.addAll(list);
        mList = list;
        notifyDataSetChanged();
    }

    public void addList(ArrayList<CartBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.cb_cart)
        CheckBox cbCart;
        @BindView(R.id.iv_goods_thumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.iv_add_goods)
        ImageView ivAddGoods;
        @BindView(R.id.tv_goods_count)
        TextView tvGoodsCount;
        @BindView(R.id.iv_del_goods)
        ImageView ivDelGoods;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_item_cart)
        RelativeLayout layoutItemCart;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.iv_add_goods)
        public void addGoods(){
            final int position = (int) layoutItemCart.getTag();
            CartBean cartBean = mList.get(position);
            NetDao.updateGoodsCount(mContext, cartBean.getId(), cartBean.getCount() + 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if (result!=null && result.isSuccess()){
                        mList.get(position).setCount(mList.get(position).getCount()+1);
                        mContext.sendBroadcast(new Intent(I.BRAODCASE_UPDATE_GOODS_PRICE));
                        tvGoodsCount.setText(String.valueOf(mList.get(position).getCount()));
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }

        @OnClick(R.id.iv_del_goods)
        public void delGoods(){
            final int position = (int) layoutItemCart.getTag();
            CartBean cartBean = mList.get(position);
            if (cartBean.getCount()>1){
                NetDao.updateGoodsCount(mContext, cartBean.getId(), cartBean.getCount() - 1, new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess()){
                            mList.get(position).setCount(mList.get(position).getCount()-1);
                            mContext.sendBroadcast(new Intent(I.BRAODCASE_UPDATE_GOODS_PRICE));
                            tvGoodsCount.setText(String.valueOf(mList.get(position).getCount()));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                NetDao.delGoodsCount(mContext, cartBean.getId(), new OkHttpUtils.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result!=null && result.isSuccess()){

                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }
    }
}
