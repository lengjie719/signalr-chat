package net.pingfang.signalr.chat.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import net.pingfang.signalr.chat.R;


public class SettingsActivity extends AppCompatActivity {

    TextView tv_app_name_version_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        tv_app_name_version_code = (TextView) findViewById(R.id.tv_app_name_version_code);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);
            tv_app_name_version_code.setText(getString(R.string.tv_app_name_version_code,info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
}
