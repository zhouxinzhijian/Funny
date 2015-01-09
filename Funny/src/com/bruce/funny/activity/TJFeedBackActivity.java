package com.bruce.funny.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.funny.R;
import com.bruce.funny.util.DensityUtil;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouxinzhijian on 2015/1/8.
 */
public class TJFeedBackActivity extends TJSwipeBackFragmentActivity implements View.OnClickListener{
    private static final String TAG = "ConversationActivity";
    private FeedbackAgent agent;
    private Conversation defaultConversation;
    private ReplyListAdapter adapter;
    private ListView replyListView;
    private EditText userReplyContentEdit;
    private SharedPreferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.umeng_fb_activity_conversation);

        setIncludeFragment(false);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.action_bar_feedback_titile);

        userReplyContentEdit = (EditText) findViewById(R.id.umeng_fb_reply_content);
        findViewById(R.id.umeng_fb_send).setOnClickListener(this);
        saveTimeWhenFirstUsingFeedback(); // 尝试记录下，用户第一次使用用户反馈的时间。当作第一条反馈的时间

        try {
            agent = new FeedbackAgent(this);
            defaultConversation = agent.getDefaultConversation();
            replyListView = (ListView) findViewById(R.id.umeng_fb_reply_list);
            adapter = new ReplyListAdapter(this);
            replyListView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            this.finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sync();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void sendFeedback() {
        try {
            String content = userReplyContentEdit.getEditableText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(this, R.string.feedback_reply_empty_prompt, Toast.LENGTH_SHORT).show();
                return;
            }
            userReplyContentEdit.getEditableText().clear();
            defaultConversation.addUserReply(content);
            sync();
            // hide soft input window after sending.
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(userReplyContentEdit.getWindowToken(), 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "sendFeedback , error");
            e.printStackTrace();
        }
    }

    // 记录用户第一次使用用户反馈的时间
    private void saveTimeWhenFirstUsingFeedback() {
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        if (preference.getLong("firstTimeUsingFeedback", 0) == 0) {
            preference.edit().putLong("firstTimeUsingFeedback", System.currentTimeMillis()).commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.umeng_fb_send) {
            sendFeedback();
        }
    }

    void sync() {
        Conversation.SyncListener listener = new Conversation.SyncListener()
        {
            @Override
            public void onSendUserReply(List<Reply> replyList) {
                Log.v(TAG, "onSendUserReply");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onReceiveDevReply(List<DevReply> replyList) {
                Log.v(TAG, "onReceiveDevReply");
                adapter.notifyDataSetChanged();
            }
        };
        defaultConversation.sync(listener);
    }

    class ReplyListAdapter extends BaseAdapter {
        Context mContext;
        LayoutInflater mInflater;

        public ReplyListAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            List<Reply> replyList = defaultConversation.getReplyList();
            return (replyList == null) ? 1 : replyList.size() + 1; // 默认情况下，第一次进入时，就会有一条回复。
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.umeng_fb_list_item, null);
                holder = new ViewHolder();
                holder.replyDate = (TextView) convertView.findViewById(R.id.umeng_fb_reply_date);
                holder.replyContent = (TextView) convertView.findViewById(R.id.umeng_fb_reply_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 默认情况下，第一次进入时，就会有一条回复。下面就是这第一个View。
            if (position == 0) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = DensityUtil.dip2px(mContext, 8);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT); // ALIGN_PARENT_RIGHT
                holder.replyContent.setLayoutParams(layoutParams);

                // set bg after layout
                holder.replyContent.setBackgroundResource(R.drawable.umeng_fb_reply_left_bg);
                long firstTimeUsingFeedback = preference.getLong("firstTimeUsingFeedback", 0);
                if (firstTimeUsingFeedback == 0) {
                    firstTimeUsingFeedback = System.currentTimeMillis();
                }
                holder.replyDate.setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(firstTimeUsingFeedback)));
                holder.replyContent.setText(R.string.feedback_reply_date_default);
                return convertView;
            }

            position--;

            Reply reply = defaultConversation.getReplyList().get(position);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            if (reply instanceof DevReply) {
                layoutParams.leftMargin = DensityUtil.dip2px(mContext, 8);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT); // ALIGN_PARENT_RIGHT
                holder.replyContent.setLayoutParams(layoutParams);

                // set bg after layout
                holder.replyContent.setBackgroundResource(R.drawable.umeng_fb_reply_left_bg);
            } else {
                layoutParams.rightMargin = DensityUtil.dip2px(mContext, 8);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); // ALIGN_PARENT_RIGHT
                // layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.replyContent.setLayoutParams(layoutParams);
                holder.replyContent.setBackgroundResource(R.drawable.umeng_fb_reply_right_bg);
            }

            holder.replyDate.setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(reply.getDatetime()));
            holder.replyContent.setText(reply.getContent());
            return convertView;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position) {
            return defaultConversation.getReplyList().get(position);
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView replyDate;
            TextView replyContent;
        }
    }
}
