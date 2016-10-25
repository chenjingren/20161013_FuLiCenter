package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.I;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.fragment.PersonalCenterFragment;
import cn.ucai.fulicenter.utils.L;
import cn.ucai.fulicenter.utils.MFGT;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getName();

    RadioButton rb[];
    Fragment[] mFragments;
    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
    PersonalCenterFragment personalCenterFragment;
    int index;
    int currentIndex;

    @BindView(R.id.rb_new_good)
    RadioButton rbNewGood;
    @BindView(R.id.rb_boutique)
    RadioButton rbBoutique;
    @BindView(R.id.rb_category)
    RadioButton rbCategory;
    @BindView(R.id.rb_cart)
    RadioButton rbCart;
    @BindView(R.id.tvCartHint)
    TextView tvCartHint;
    @BindView(R.id.rb_personal_center)
    RadioButton rbPersonalCenter;

    Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        super.onCreate(savedInstanceState);
        /*initView();
        initFragment();*/
    }

    @Override
    protected void initView() {
        rb = new RadioButton[5];
        rb[0] = rbNewGood;
        rb[1] = rbBoutique;
        rb[2] = rbCategory;
        rb[3] = rbCart;
        rb[4] = rbPersonalCenter;
    }

    @Override
    protected void initData() {
        initFragment();
    }

    @Override
    protected void setListener() {

    }

    private void initFragment() {
        newGoodsFragment = new NewGoodsFragment();
        boutiqueFragment = new BoutiqueFragment();
        categoryFragment = new CategoryFragment();
        personalCenterFragment = new PersonalCenterFragment();
        mFragments = new Fragment[5];
        mFragments[0] = newGoodsFragment;
        mFragments[1] = boutiqueFragment;
        mFragments[2] = categoryFragment;
        mFragments[4] = personalCenterFragment;

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container,newGoodsFragment)
                .add(R.id.fragment_container,boutiqueFragment)
                .add(R.id.fragment_container,categoryFragment)
                .hide(categoryFragment)
                .hide(boutiqueFragment)
                .show(newGoodsFragment)
                .commit();
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.rb_new_good:
                index = 0;
                break;
            case R.id.rb_boutique:
                index = 1;
                break;
            case R.id.rb_category:
                index = 2;
                break;
            case R.id.rb_cart:
                index = 3;
                break;
            case R.id.rb_personal_center:
                if (FuLiCenterApplication.getUserAvatar()==null){
                    MFGT.gotoLoginActivity(mContext);
                }else {
                    index = 4;
                }
                break;
        }
        setFragment();
        //setRadioButtonStatus();
        //currentIndex = index;
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (index!=currentIndex){
            ft.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()){
                ft.add(R.id.fragment_container,mFragments[index]);
            }
            ft.show(mFragments[index]).commit();
            setRadioButtonStatus(index);
            currentIndex = index;
        }
    }

    private void setRadioButtonStatus(int index) {
        L.e(TAG, "index===" + index);
        for (int i = 0; i < rb.length; i++) {
            if (index == i) {
                rb[i].setChecked(true);
            } else {
                rb[i].setChecked(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG,"onResume( )......");
        if (index == 4 && FuLiCenterApplication.getUserAvatar()!=null){
            index =0;
        }
        setFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG,"onActivityResult.requestCode======"+requestCode);
        if (requestCode == I.REQUEST_CODE_LOGIN && FuLiCenterApplication.getUserAvatar()!=null){
            index = 4;
        }
    }
}
