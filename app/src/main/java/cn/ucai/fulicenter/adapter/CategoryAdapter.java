package cn.ucai.fulicenter.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.utils.ImageLoader;
import cn.ucai.fulicenter.utils.MFGT;

/**
 * Created by ACherish on 2016/10/20.
 */
public class CategoryAdapter extends BaseExpandableListAdapter {
    Context mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mGroupList,
                           ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        this.mContext = mContext;
        this.mGroupList = new ArrayList<>();
        this.mGroupList.addAll(mGroupList);
        this.mChildList = new ArrayList<>();
        this.mChildList.addAll(mChildList);
    }

    @Override
    public int getGroupCount() {
        return mGroupList != null ? mGroupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList != null && mChildList.get(groupPosition) != null ?
                mChildList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return mGroupList != null ? mGroupList.get(groupPosition) : null;
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition) != null &&
                mChildList.get(groupPosition).get(childPosition) != null ?
                mChildList.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_category_group, null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        CategoryGroupBean group = getGroup(groupPosition);
        if (group != null) {
            ImageLoader.downloadImg(mContext, holder.ivGroupThumb, group.getImageUrl());
            holder.tvGroupName.setText(group.getName());
            holder.ivIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_category_child, null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        final CategoryChildBean child = getChild(groupPosition, childPosition);
        if (child!=null){
            ImageLoader.downloadImg(mContext,holder.ivCategoryChildThumb,child.getImageUrl());
            holder.tvCategoryChildName.setText(child.getName());
            holder.layoutCategoryChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoCategoryChildActivity(mContext,child.getId());
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void initList(ArrayList<CategoryGroupBean> mGroupList,
                         ArrayList<ArrayList<CategoryChildBean>> mChildList) {
        if (this.mGroupList!=null){
            this.mGroupList.clear();
        }
        this.mGroupList.addAll(mGroupList);
        if (this.mChildList!=null){
            this.mChildList.clear();
        }
        this.mChildList.addAll(mChildList);
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView ivGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView ivIndicator;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ChildViewHolder {
        @BindView(R.id.iv_category_child_thumb)
        ImageView ivCategoryChildThumb;
        @BindView(R.id.tv_category_child_name)
        TextView tvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
