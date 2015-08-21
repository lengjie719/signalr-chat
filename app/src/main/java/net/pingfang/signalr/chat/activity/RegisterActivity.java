package net.pingfang.signalr.chat.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.pingfang.signalr.chat.R;
import net.pingfang.signalr.chat.fragment.InfoRegFragment;
import net.pingfang.signalr.chat.fragment.PhoneFragment;
import net.pingfang.signalr.chat.util.OnRegisterInteractionListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,OnRegisterInteractionListener{

    public static final int REGEST_STEP_1 = 1;
    public static final int REGEST_STEP_2 = 2;

    TextView btn_step_previous;
    TextView btn_step_next;

    FrameLayout fl_container_reg;

    int requestStep = REGEST_STEP_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initFragment();
    }

    private void initView() {
        btn_step_previous = (TextView) findViewById(R.id.btn_step_previous);
        btn_step_previous.setOnClickListener(this);
        btn_step_next = (TextView) findViewById(R.id.btn_step_next);
        btn_step_next.setOnClickListener(this);

        fl_container_reg = (FrameLayout) findViewById(R.id.fl_container_reg);
    }

    private void initFragment() {
        if(requestStep == REGEST_STEP_1) {
            PhoneFragment phoneFragment = PhoneFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fl_container_reg, phoneFragment, "PhoneFragment");
            ft.commit();
        } else if(requestStep == REGEST_STEP_2) {
            InfoRegFragment infoFragment = InfoRegFragment.newInstance();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fl_container_reg,infoFragment,"InfoRegFragment");
            ft.commit();
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 实现获取验证码功能
     * @param phoneNo 注册使用的手机号码
     */
    @Override
    public void loadVC(String phoneNo) {
        // 待实现
    }

    /**
     *
     * @param phoneNo 手机号码
     * @param vc  通过短信获取的验证码
     */
    @Override
    public void submitC(String phoneNo, String vc) {
        // 待实现

        // 跳转
        requestStep = REGEST_STEP_2;
        initFragment();
    }

    @Override
    public void submitInfo(String nick, String password, String qq) {
        // 待实现

        // 跳转
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
