package net.pingfang.signalr.chat.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.pingfang.signalr.chat.R;
import net.pingfang.signalr.chat.adapter.CollectionPagerAdapter;
import net.pingfang.signalr.chat.fragment.AccountFragment;
import net.pingfang.signalr.chat.fragment.BuddyFragment;
import net.pingfang.signalr.chat.fragment.MessageFragment;
import net.pingfang.signalr.chat.util.OnFragmentInteractionListener;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener,OnFragmentInteractionListener {

    TextView tv_activity_title;
    TextView tv_menu_drop_down;
    FrameLayout fl_container;
    ViewPager pager;

    MessageFragment messageFragment;
    BuddyFragment buddyFragment;
    AccountFragment accountFragment;


    Button btn_list_chat;
    Button btn_list_friend;
    Button btn_account_management;

    CollectionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initAdapter();
    }

    private void initView() {
        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_menu_drop_down = (TextView) findViewById(R.id.tv_menu_drop_down);
        tv_menu_drop_down.setOnClickListener(this);
        fl_container = (FrameLayout) findViewById(R.id.fl_container);
        fl_container = (FrameLayout) findViewById(R.id.fl_container);
        pager = (ViewPager) findViewById(R.id.pager);
        btn_list_chat = (Button) findViewById(R.id.btn_list_chat);
        btn_list_chat.setOnClickListener(this);
        btn_list_friend = (Button) findViewById(R.id.btn_list_friend);
        btn_list_friend.setOnClickListener(this);
        btn_account_management = (Button) findViewById(R.id.btn_account_management);
        btn_account_management.setOnClickListener(this);
    }

    private void initAdapter() {
        messageFragment = MessageFragment.newInstance();
        buddyFragment = BuddyFragment.newInstance();
        accountFragment = AccountFragment.newInstance();
        adapter = new CollectionPagerAdapter(getSupportFragmentManager());
        adapter.add(messageFragment);
        adapter.add(buddyFragment);
        adapter.add(accountFragment);
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        tv_activity_title.setText(R.string.tv_activity_title_roster);
                        break;
                    case 2:
                        tv_activity_title.setText(R.string.tv_activity_title_account);
                        break;
                    case 0:
                        tv_activity_title.setText(R.string.tv_activity_title_message);
                        break;
                }
            }
        });
    }

    @Override
    public void loadMessage() {
        MessageFragment fragment = (MessageFragment) adapter.getItem(0);
        fragment.updateMessage("server", "0001", "");
    }

    @Override
    public void onFragmentInteraction(String name, String uid) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(),ChatActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("uid",uid);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch(viewId) {
            case R.id.tv_menu_drop_down:
                popupMenu(view);
                break;
            case R.id.btn_list_chat:
                pager.setCurrentItem(0);
                break;
            case R.id.btn_list_friend:
                pager.setCurrentItem(1);
                break;
            case R.id.btn_account_management:
                pager.setCurrentItem(2);
                break;
        }
    }

    private void popupMenu(View view) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme);
        PopupMenu popup = new PopupMenu(wrapper, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_home, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:

                        break;
                    case R.id.action_resource:

                        break;
                    case R.id.action_maintain:

                        break;
                }
                return true;
            }
        });
        popup.show();
    }
}
