package com.caishi.zhanghai.im.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.bean.GroupInfoReturnBean;
import com.caishi.zhanghai.im.bean.QuitGroupBean;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.SocketClient;
import com.google.gson.Gson;

/**
 * Created by shihui on 2018/1/29.
 */

public class GroupApplyListAc extends BaseActivity{
    private ListView mLvApplyList;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.acitivity_group_apply);
        setTitle(R.string.group_apply);
        initData();
    }

    private void initData(){
        mLvApplyList = (ListView) findViewById(R.id.lv_group_apply_list);

        String  fromConversationId = getIntent().getStringExtra("fromConversationId");
        getGroupApply(fromConversationId);

    }


    private void getGroupApply(String fromConversationId) {
        QuitGroupBean groupBean = new QuitGroupBean();
        groupBean.setK("applies");
        groupBean.setM("group");
        groupBean.setRid(String.valueOf(System.currentTimeMillis()));
        QuitGroupBean.VBean vBean = new QuitGroupBean.VBean();
        vBean.setGroupId(fromConversationId);
        groupBean.setV(vBean);
        String msg = new Gson().toJson(groupBean);

        SocketClient.getInstance().sendMsg(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("json", json);
                GroupInfoReturnBean groupInfoReturnBean = new Gson().fromJson(json, GroupInfoReturnBean.class);
                if (null != groupInfoReturnBean) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = groupInfoReturnBean;
                    handler.sendMessage(message);
                }

            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.acitivity_group_apply_item,viewGroup,false);
            return view;
        }
    }
}
