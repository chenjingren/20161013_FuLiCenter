package cn.ucai.fulicenter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.utils.L;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();


    RadioButton rb[];
    int index;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        rb = new RadioButton[5];
        rb[0] = rbNewGood;
        rb[1] = rbBoutique;
        rb[2] = rbCategory;
        rb[3] = rbCart;
        rb[4] = rbPersonalCenter;
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
        setRadioButtonStatus();
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
}
