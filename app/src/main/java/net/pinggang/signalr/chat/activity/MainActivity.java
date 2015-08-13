package net.pinggang.signalr.chat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.pinggang.signalr.chat.R;
import net.pinggang.signalr.chat.service.ChatService;
import net.pinggang.signalr.chat.util.GlobalApplication;
import net.pinggang.signalr.chat.util.MediaFileUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView btn_activity_back;
    TextView tv_activity_title;
    LinearLayout ll_message_container;
    EditText et_message;
    Button btn_voice_record;
    Button btn_send;

    MessageReceiver receiver;

    String buddy = "server";
    ChatService chatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatService = ChatService.newInstance(getApplicationContext());

        initView();
    }

    private void initView() {
        btn_activity_back = (TextView) findViewById(R.id.btn_activity_back);
        btn_activity_back.setOnClickListener(this);

        tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
        tv_activity_title.setText(getString(R.string.title_activity_chat, buddy));

        ll_message_container = (LinearLayout) findViewById(R.id.ll_message_container);

        et_message = (EditText) findViewById(R.id.et_message);
        et_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendMessage(buddy);
                    handled = true;
                }
                return handled;
            }
        });

        btn_voice_record = (Button) findViewById(R.id.btn_voice_record);
        btn_voice_record.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventCode = event.getAction();
                switch (eventCode) {
                    case MotionEvent.ACTION_DOWN:
                        //                        startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //                        stopRecording();
                        return true;
                }
                return false;
            }
        });

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        btn_send.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //                popupMenu(view);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver();
    }

    public void registerReceiver() {
        receiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalApplication.ACTION_INTENT_TEXT_MESSAGE_INCOMING);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch(viewId) {
            case R.id.btn_activity_back:
//                navigateUp();
                break;
            case R.id.btn_send:
                sendMessage(buddy);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        chatService.destroy();
    }

    private void sendMessage(String buddy) {
        if(!TextUtils.isEmpty(et_message.getText().toString().trim())) {
            chatService.sendMessage(buddy, et_message.getText().toString().trim());

            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setGravity(Gravity.LEFT);

            TextView textView = new TextView(getApplicationContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText("me");
            textView.setTextColor(Color.RED);

            TextView tv_msg = new TextView(getApplicationContext());
            tv_msg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_msg.setTextColor(Color.RED);
            tv_msg.setPadding(20, 0, 0, 0);
            tv_msg.setGravity(Gravity.CENTER_VERTICAL);
            tv_msg.setText(et_message.getText().toString().trim());
            tv_msg.setBackgroundResource(R.drawable.msg_me);


            ll.addView(textView);
            ll.addView(tv_msg);

            ll_message_container.addView(ll);
            et_message.setText("");
        }
    }

    private class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(GlobalApplication.ACTION_INTENT_TEXT_MESSAGE_INCOMING)) {
                String nameFrom= intent.getStringExtra("buddy");
                String body = intent.getStringExtra("body");
                if(nameFrom.equals(buddy)) {
                    LinearLayout ll = new LinearLayout(getApplicationContext());
                    ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.setGravity(Gravity.RIGHT);

                    TextView textView = new TextView(getApplicationContext());
                    textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    textView.setText(nameFrom);
                    textView.setTextColor(Color.BLACK);

                    TextView tv_msg = new TextView(getApplicationContext());
                    tv_msg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
                    tv_msg.setTextColor(Color.BLACK);
                    tv_msg.setPadding(0, 0, MediaFileUtils.dpToPx(getApplicationContext(), 20), 0);
                    tv_msg.setGravity(Gravity.CENTER_VERTICAL);
                    tv_msg.setText(body);
                    tv_msg.setBackgroundResource(R.drawable.msg_buddy);

                    ll.addView(textView);
                    ll.addView(tv_msg);

                    ll_message_container.addView(ll);
                } else {

                }
            }
        }
    }

}
