package net.pingfang.signalr.chat.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.pingfang.signalr.chat.R;
import net.pingfang.signalr.chat.util.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ChatHistoryFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ChatListAdapter mAdapter;

    private List<MessageHolder> listMessage = new ArrayList<>();

    public static ChatHistoryFragment newInstance() {
        ChatHistoryFragment fragment = new ChatHistoryFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatHistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChatListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mListView = (ListView) view.findViewById(android.R.id.list);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListener.loadMessage();
    }

    public void updateMessage(String name,String uid, String body) {
        MessageHolder holder = null;
        for(int i = 0; i < listMessage.size(); i++) {
            MessageHolder tmp = listMessage.get(i);
            if(tmp.uid.equals(uid)) {
                holder = tmp;
                holder.body = body;
                break;
            }
        }

        if(holder == null) {
            holder = new MessageHolder();
            holder.name = name;
            holder.uid = uid;
            holder.body = body;
            listMessage.add(holder);
        }

        if(mAdapter == null) {
            mAdapter = new ChatListAdapter(getActivity());
            mListView.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            MessageHolder holder = listMessage.get(position);
            mListener.onFragmentInteraction(holder.name,holder.uid);
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    private class ChatListAdapter extends BaseAdapter {

        Context context;

        public ChatListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return listMessage.size();
        }

        @Override
        public Object getItem(int position) {
            return listMessage.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.list_item_message, null);
                MessageHolder holder = listMessage.get(position);
                TextView tv_friends_name = (TextView) view.findViewById(R.id.tv_friends_name);
                tv_friends_name.setText(holder.name);
                TextView tv_message_update = (TextView) view.findViewById(R.id.tv_message_update);
                tv_message_update.setText(holder.body);
                view.setTag(holder.uid);
                convertView = view;
            } else {
                MessageHolder holder = listMessage.get(position);
                TextView tv_friends_name = (TextView) convertView.findViewById(R.id.tv_friends_name);
                tv_friends_name.setText(holder.name);
                TextView tv_message_update = (TextView) convertView.findViewById(R.id.tv_message_update);
                tv_message_update.setText(holder.body);
                convertView.setTag(holder.uid);
            }
            return convertView;
        }
    }

    public static class MessageHolder {
        private String name;
        private String uid;
        private String body;
    }

}
