package net.pingfang.signalr.chat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import net.pingfang.signalr.chat.R;

public class LoginActivity extends AppCompatActivity {

    EditText et_login_pwd;
    CheckBox cb_show_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);

        cb_show_pwd = (CheckBox) findViewById(R.id.cb_show_pwd);
        cb_show_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    et_login_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_login_pwd.setSelection(et_login_pwd.getText().length());
                } else {
                    et_login_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_login_pwd.setSelection(et_login_pwd.getText().length());
                }
            }
        });
    }

    public void login(View view) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
