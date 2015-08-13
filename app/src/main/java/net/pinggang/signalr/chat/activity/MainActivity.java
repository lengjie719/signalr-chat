package net.pinggang.signalr.chat.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.pinggang.signalr.chat.R;
import net.pinggang.signalr.chat.service.ChatService;
import net.pinggang.signalr.chat.util.GlobalApplication;
import net.pinggang.signalr.chat.util.MediaFileUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_IMAGE_GET = 0x01;

    TextView btn_activity_back;
    TextView tv_activity_title;
    LinearLayout ll_message_container;
    EditText et_message;
    Button btn_voice_record;
    Button btn_send;

    MessageReceiver receiver;

    String buddy = "server";
    ChatService chatService;

    MediaRecorder mRecorder;
    String mFileName;
    boolean mStartRecording = false;
    MediaPlayer mPlayer;

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
                        startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        stopRecording();
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
                popupMenu(view);
                return true;
            }
        });
    }

    private void popupMenu(View view) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.MyPopupMenu);
        PopupMenu popup = new PopupMenu(wrapper, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_message_actions, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_text:
                        btn_voice_record.setVisibility(View.GONE);
                        et_message.setVisibility(View.VISIBLE);
                        break;
                    case R.id.action_image:
                        btn_voice_record.setVisibility(View.GONE);
                        et_message.setVisibility(View.VISIBLE);
                        sendImage();
                        break;
                    case R.id.action_voice:
                        btn_voice_record.setVisibility(View.VISIBLE);
                        et_message.setVisibility(View.GONE);
                        break;
                }
                return true;
            }
        });
        popup.show();
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

    private void sendImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        if (getIntent.resolveActivity(getPackageManager()) != null ||
                pickIntent.resolveActivity(getPackageManager()) != null) {

            Intent chooserIntent = Intent.createChooser(getIntent, getString(R.string.action_select_image));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

            startActivityForResult(chooserIntent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            if(data != null && data.getData() != null) {
                if(requestCode == REQUEST_IMAGE_GET) {
                    Uri uri = data.getData();
                    if(uri != null) {
                        String filePath = MediaFileUtils.getRealPathFromURI(getApplicationContext(),uri);
                        Bitmap bitmap = MediaFileUtils.decodeBitmapFromPath(filePath,
                                MediaFileUtils.dpToPx(getApplicationContext(),150),
                                MediaFileUtils.dpToPx(getApplicationContext(),150));
                        inflaterImgMessage(bitmap,uri,true,buddy);

//                        chatService.sendImage(jid,filePath);
                    } else {
                        Log.d("ChatActivity", "no data");
                    }
                }
            }
        }
    }

    private void inflaterImgMessage(Bitmap bitmap,Uri uri,boolean direction,String from) {

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(from);
        if(direction) {
            textView.setTextColor(Color.RED);
            ll.setGravity(Gravity.LEFT);
        } else {
            textView.setTextColor(Color.BLACK);
            ll.setGravity(Gravity.RIGHT);
        }


        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MediaFileUtils.dpToPx(getApplicationContext(),150),
                MediaFileUtils.dpToPx(getApplicationContext(),150));
        imageView.setLayoutParams(params);
        imageView.setPadding(MediaFileUtils.dpToPx(getApplicationContext(),10),
                MediaFileUtils.dpToPx(getApplicationContext(),10),
                MediaFileUtils.dpToPx(getApplicationContext(),10),
                MediaFileUtils.dpToPx(getApplicationContext(),10));
        imageView.setImageBitmap(bitmap);
        if(direction) {
            imageView.setBackgroundResource(R.drawable.msg_me);
        } else {
            imageView.setBackgroundResource(R.drawable.msg_buddy);
        }
        imageView.setTag(uri);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = (Uri) v.getTag();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setDataAndType(uri, "image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        ll.addView(textView);
        ll.addView(imageView);

        ll_message_container.addView(ll);
    }

    private void startRecording() {

        mFileName = MediaFileUtils.getVoiceFilePath(getApplicationContext(),"voice");

        if(!TextUtils.isEmpty(mFileName) && !mStartRecording) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("ChatActivity", "prepare() failed");
            }

            mRecorder.start();

            mStartRecording = true;
            btn_voice_record.setText(R.string.btn_voice_record_up);

        }

    }

    private void stopRecording() {
        if(mStartRecording) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            mStartRecording = false;
            btn_voice_record.setText(R.string.btn_voice_record);

            Uri uri = Uri.parse(mFileName);
            inflaterVoiceMessage(uri, true, "me");

//            chatService.sendVoice(jid,mFileName);
        }

    }

    private void inflaterVoiceMessage(Uri uri,boolean direction,String from) {

        LinearLayout ll = new LinearLayout(getApplicationContext());
        ll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(from);
        if(direction) {
            textView.setTextColor(Color.RED);
            ll.setGravity(Gravity.LEFT);
        } else {
            textView.setTextColor(Color.BLACK);
            ll.setGravity(Gravity.RIGHT);
        }

        ImageView imageView = new ImageView(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(250,250);
        if(direction) {
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.voice_me);
            imageView.setBackgroundResource(R.drawable.msg_me);

        } else {
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.voice_buddy);
            imageView.setBackgroundResource(R.drawable.msg_buddy);
        }

        imageView.setLayoutParams(params);
        imageView.setTag(uri);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri fileUri = (Uri) v.getTag();
                if(mPlayer != null) {
                    mPlayer.release();
                    mPlayer = null;
                }
                mPlayer = MediaPlayer.create(getApplicationContext(),fileUri);
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                mPlayer.start();
            }
        });

        ll.addView(textView);
        ll.addView(imageView);

        ll_message_container.addView(ll);
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
