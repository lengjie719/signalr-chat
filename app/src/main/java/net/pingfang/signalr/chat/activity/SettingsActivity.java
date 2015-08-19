package net.pingfang.signalr.chat.activity;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import net.pingfang.signalr.chat.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    TextView btn_activity_back;
    TextView tv_settings_item_about;
    TextView tv_settings_item_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        btn_activity_back = (TextView) findViewById(R.id.btn_activity_back);
        btn_activity_back.setOnClickListener(this);
        tv_settings_item_about = (TextView) findViewById(R.id.tv_settings_item_about);
        tv_settings_item_about.setOnClickListener(this);
        tv_settings_item_exit = (TextView) findViewById(R.id.tv_settings_item_exit);
        tv_settings_item_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_activity_back:
                navigateUp();
                break;
            case R.id.tv_settings_item_about:
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),AppAboutActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_settings_item_exit:
                Intent exitIntent = new Intent();
                exitIntent.setClass(getApplicationContext(),LoginActivity.class);
                exitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(exitIntent);
                finish();
                break;
        }
    }

    public void navigateUp() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if(NavUtils.shouldUpRecreateTask(this, upIntent)) {
            TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(upIntent)
                    .startActivities();
        } else {
            onBackPressed();
        }
    }
}
