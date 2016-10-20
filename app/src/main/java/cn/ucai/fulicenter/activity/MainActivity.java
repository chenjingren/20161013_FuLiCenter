package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.fragment.CategoryFragment;
import cn.ucai.fulicenter.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getName();

    RadioButton rb[];
    Fragment[] mFragments;
    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        mFragments = new Fragment[5];
        mFragments[0] = newGoodsFragment;
        mFragments[1] = boutiqueFragment;
        mFragments[2] = categoryFragment;
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
                index = 4;
                break;
        }
        setFragment();
        setRadioButtonStatus();
        currentIndex = index;
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (index!=currentIndex){
            ft.hide(mFragments[currentIndex]);
            if (!mFragments[index].isAdded()){
                ft.add(R.id.fragment_container,mFragments[index]);
            }
            ft.show(mFragments[index]).commit();
        }
    }

    private void setRadioButtonStatus() {
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
}
