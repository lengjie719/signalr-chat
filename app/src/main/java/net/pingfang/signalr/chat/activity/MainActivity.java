package net.pingfang.signalr.chat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.pingfang.signalr.chat.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),LoginActivity.class);
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
