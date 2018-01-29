package com.caishi.zhanghai.im.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.caishi.zhanghai.im.App;
import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.bean.AgreeGroupBean;
import com.caishi.zhanghai.im.bean.BaseReturnBean;
import com.caishi.zhanghai.im.bean.GroupApplyListReturnBean;
import com.caishi.zhanghai.im.bean.GroupInfoReturnBean;
import com.caishi.zhanghai.im.bean.QuitGroupBean;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.SocketClient;
import com.caishi.zhanghai.im.server.utils.NToast;
import com.caishi.zhanghai.im.server.widget.SelectableRoundedImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;

/**
 * Created by shihui on 2018/1/29.
 */

public class GroupApplyListAc extends BaseActivity implements View.OnClickListener {
    private ListView mLvApplyList;
    private RadioButton rb_select_all;
    private Button btn_group_agree;
    private String fromConversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_group_apply);
        setTitle(R.string.group_apply);
        initData();
    }

    private void initData() {
        mLvApplyList = (ListView) findViewById(R.id.lv_group_apply_list);
        rb_select_all = (RadioButton) findViewById(R.id.rb_select_all);
        btn_group_agree = (Button) findViewById(R.id.btn_group_agree);
        fromConversationId = getIntent().getStringExtra("fromConversationId");
        getGroupApply();
        boolean isCheckedAll = false;
        rb_select_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    for (GroupApplyListReturnBean.DataBean dataBean : dataBeanList) {
                        dataBean.setCheck(true);
                    }
                    b = !b;
                } else {
                    for (GroupApplyListReturnBean.DataBean dataBean : dataBeanList) {
                        dataBean.setCheck(false);
                    }
                    b = !b;
                }
                myAdapter.notifyDataSetChanged();

            }
        });

        btn_group_agree.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_group_agree:
                for (GroupApplyListReturnBean.DataBean dataBean : dataBeanList) {
                    if (dataBean.isCheck()) {
                        stringAccounts.add(dataBean.getAccount());
                    }
                }
                agreeGroup();
                break;
        }
    }


    List<String> stringAccounts = new ArrayList<>();

    private void agreeGroup() {
        AgreeGroupBean agreeGroupBean = new AgreeGroupBean();
        agreeGroupBean.setK("agree");
        agreeGroupBean.setM("group");
        agreeGroupBean.setRid(String.valueOf(System.currentTimeMillis()));
        AgreeGroupBean.VBean vBean = new AgreeGroupBean.VBean();
        vBean.setGroupId(fromConversationId);
        vBean.setApplyer_accounts(stringAccounts);
        agreeGroupBean.setV(vBean);
        String msg = new Gson().toJson(agreeGroupBean);

        SocketClient.getInstance().sendMsg(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("json", json);
                BaseReturnBean baseReturnBean = new Gson().fromJson(json, BaseReturnBean.class);
                if (null != baseReturnBean) {
                    Message message = new Message();
                    message.what = 2;
                    message.obj = baseReturnBean;
                    handler.sendMessage(message);
                }

            }
        });

    }

    private void getGroupApply() {
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
                GroupApplyListReturnBean groupInfoReturnBean = new Gson().fromJson(json, GroupApplyListReturnBean.class);
                if (null != groupInfoReturnBean) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = groupInfoReturnBean;
                    handler.sendMessage(message);
                }

            }
        });
    }

    private MyAdapter myAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    GroupApplyListReturnBean groupInfoReturnBean = (GroupApplyListReturnBean) msg.obj;
                    NToast.longToast(getApplication(), groupInfoReturnBean.getDesc());
                    if (null != groupInfoReturnBean.getData()) {
                        dataBeanList = groupInfoReturnBean.getData();
                        myAdapter = new MyAdapter();
                        mLvApplyList.setAdapter(myAdapter);
                    }
                    break;

                case 2://同意入群
                    BaseReturnBean baseReturnBean = (BaseReturnBean) msg.obj;
                    NToast.longToast(getApplication(), baseReturnBean.getDesc());


                    break;
            }
        }
    };
    private List<GroupApplyListReturnBean.DataBean> dataBeanList;

    private class MyAdapter extends BaseAdapter {

        GroupApplyListReturnBean.DataBean dataBean = null;


        @Override
        public int getCount() {
            return dataBeanList.size();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return dataBeanList.get(i);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.acitivity_group_apply_item, viewGroup, false);
            SelectableRoundedImageView search_item_header = (SelectableRoundedImageView) view.findViewById(R.id.search_item_header);
            TextView search_item_name = (TextView) view.findViewById(R.id.search_item_name);
            TextView search_item_time = (TextView) view.findViewById(R.id.search_item_time);
            RadioButton rb_select_one = (RadioButton) view.findViewById(R.id.rb_select_one);

            if (null != dataBeanList && dataBeanList.size() > 0) {
                dataBean = dataBeanList.get(i);
                if (null != dataBean) {
                    ImageLoader.getInstance().displayImage(dataBean.getPortraitUri(), search_item_header, App.getOptions());
                    search_item_name.setText(dataBean.getNickname());
                    search_item_time.setText(dataBean.getApply_time());
                }

            }

            rb_select_one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        dataBean.setCheck(true);
                        b = !b;
                    } else {
                        dataBean.setCheck(false);
                        b = !b;
                    }
                }
            });


            return view;
        }
    }
}
