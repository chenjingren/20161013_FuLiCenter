package cn.ucai.fulicenter.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.MainActivity;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.net.NetDao;
import cn.ucai.fulicenter.utils.CommonUtils;
import cn.ucai.fulicenter.utils.ConvertUtils;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.OkHttpUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends BaseFragment {

    public static final String TAG = CategoryFragment.class.getName();

    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;

    CategoryAdapter mAdapter;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<ArrayList<CategoryChildBean>> mChildList;
    MainActivity mContext;

    int groupCount;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, layout);
        mContext = (MainActivity) getContext();
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mAdapter = new CategoryAdapter(mContext,mGroupList,mChildList);
        super.onCreateView(inflater, container, savedInstanceState);
        return layout;
    }

    @Override
    protected void initView() {
        elvCategory.setGroupIndicator(null);
        elvCategory.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        showCategoryGroup();
    }

    private void showCategoryGroup() {
        NetDao.downloadCategoryGroup(mContext, new OkHttpUtils.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                //L.e(TAG,"result==="+result);
                if (result!=null&& result.length>0){
                    ArrayList<CategoryGroupBean> groupList = ConvertUtils.array2List(result);
                    L.e(TAG,"showCategoryGroup.groupList.size==="+groupList.size());
                    mGroupList.addAll(groupList);
                    int i =0;
                    for (CategoryGroupBean groupBean:groupList){
                        mChildList.add(new ArrayList<CategoryChildBean>());
                        showCategoryChild(groupBean,i);
                        i++;
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG,"error====="+error);
            }
        });
    }

    private void showCategoryChild(CategoryGroupBean groupBean, final int index) {
        NetDao.downloadCategoryChild(mContext, groupBean.getId(), new OkHttpUtils.OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                groupCount++;
                if (result!=null && result.length>0){
                    ArrayList<CategoryChildBean> childList = ConvertUtils.array2List(result);
                    L.e(TAG,"showCategoryChild.childList.size==="+childList.size());
                    mChildList.set(index,childList);
                }
                if (groupCount == mGroupList.size()){
                    mAdapter.initList(mGroupList,mChildList);
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG,"error====="+error);
            }
        });
    }

    @Override
    protected void setListener() {

    }

}
