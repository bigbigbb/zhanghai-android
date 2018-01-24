package com.caishi.zhanghai.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.caishi.zhanghai.im.App;
import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.SealConst;
import com.caishi.zhanghai.im.SealUserInfoManager;
import com.caishi.zhanghai.im.bean.BaseReturnBean;
import com.caishi.zhanghai.im.bean.BeanBean;
import com.caishi.zhanghai.im.bean.GroupListReturnBean;
import com.caishi.zhanghai.im.db.Groups;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.SocketClient;
import com.caishi.zhanghai.im.server.broadcast.BroadcastManager;
import com.caishi.zhanghai.im.server.utils.NToast;
import com.caishi.zhanghai.im.server.widget.SelectableRoundedImageView;
import com.google.gson.Gson;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/3/8.
 * Company RongCloud
 */
public class GroupListActivity extends BaseActivity {

    private ListView mGroupListView;
    private GroupAdapter adapter;
    private TextView mNoGroups;
    private EditText mSearch;
    private List<Groups> mList = new ArrayList<>();
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_group_list);
        setTitle(R.string.my_groups);
        mGroupListView = (ListView) findViewById(R.id.group_listview);
        mNoGroups = (TextView) findViewById(R.id.show_no_group);
        mSearch = (EditText) findViewById(R.id.group_search);
        mTextView = (TextView)findViewById(R.id.foot_group_size);
        initData();
        getAllGroup();
        BroadcastManager.getInstance(mContext).addAction(SealConst.GROUP_LIST_UPDATE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        });
    }

    private void initData() {
        SealUserInfoManager.getInstance().getGroups(new SealUserInfoManager.ResultCallback<List<Groups>>() {
            @Override
            public void onSuccess(List<Groups> groupsList) {
                mList = groupsList;
                if (mList != null && mList.size() > 0) {
                    adapter = new GroupAdapter(mContext, mList);
                    mGroupListView.setAdapter(adapter);
                    mNoGroups.setVisibility(View.GONE);
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText(getString(R.string.ac_group_list_group_number, mList.size()));
                    mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Groups bean = (Groups) adapter.getItem(position);
                            RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupsId(), bean.getName());
                        }
                    });
                    mSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            filterData(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    mNoGroups.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String errString) {

            }
        });
    }


    private void getAllGroup(){
        BeanBean friendAllBean = new BeanBean();
        friendAllBean.setK("all");
        friendAllBean.setM("group");
        friendAllBean.setRid(String.valueOf(System.currentTimeMillis()));
        String msg = new Gson().toJson(friendAllBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("msg1111", json);
                GroupListReturnBean baseReturnBean = new Gson().fromJson(json, GroupListReturnBean.class);
                if (null != baseReturnBean) {
                    Message message = new Message();
                    message.obj = baseReturnBean;
                    handler.sendMessage(message);
                }


            }
        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GroupListReturnBean groupListReturnBean   =  (GroupListReturnBean) msg.obj;
            if(null!=groupListReturnBean){
                List<GroupListReturnBean.DataBean> listReturnBeans = groupListReturnBean.getData();
                if(listReturnBeans.size()>0){
                    Groups groups = null;
                    for (GroupListReturnBean.DataBean dataBean:listReturnBeans){
                        groups = new Groups();
                        groups.setGroupsId(dataBean.getId());
                        groups.setName(dataBean.getName());
                        if(null!=groups.getPortraitUri()){
                            groups.setPortraitUri(dataBean.getPortraitUri());
                        }
                        mList.add(groups);

                    }
                }
                if (mList != null && mList.size() > 0) {
                    adapter = new GroupAdapter(mContext, mList);
                    mGroupListView.setAdapter(adapter);
                    mNoGroups.setVisibility(View.GONE);
                    mTextView.setVisibility(View.VISIBLE);
                    mTextView.setText(getString(R.string.ac_group_list_group_number, mList.size()));
                    mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Groups bean = (Groups) adapter.getItem(position);
                            RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupsId(), bean.getName());
                        }
                    });
                    mSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            filterData(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    mNoGroups.setVisibility(View.VISIBLE);
                }
            }

        }
    };
    private void filterData(String s) {
        List<Groups> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            filterDataList = mList;
        } else {
            for (Groups groups : mList) {
                if (groups.getName().contains(s)) {
                    filterDataList.add(groups);
                }
            }
        }
        adapter.updateListView(filterDataList);
        mTextView.setText(getString(R.string.ac_group_list_group_number, filterDataList.size()));
    }


    class GroupAdapter extends BaseAdapter {

        private Context context;

        private List<Groups> list;

        public GroupAdapter(Context context, List<Groups> list) {
            this.context = context;
            this.list = list;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<Groups> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (list != null) return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list == null)
                return null;

            if (position >= list.size())
                return null;

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final Groups mContent = list.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.group_item_new, parent, false);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.groupname);
                viewHolder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.groupuri);
                viewHolder.groupId = (TextView) convertView.findViewById(R.id.group_id);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(mContent.getName());
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mContent);
            ImageLoader.getInstance().displayImage(portraitUri, viewHolder.mImageView, App.getOptions());
            if (context.getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false)) {
                viewHolder.groupId.setVisibility(View.VISIBLE);
                viewHolder.groupId.setText(mContent.getGroupsId());
            }
            return convertView;
        }


        class ViewHolder {
            /**
             * 昵称
             */
            TextView tvTitle;
            /**
             * 头像
             */
            SelectableRoundedImageView mImageView;
            /**
             * userId
             */
            TextView groupId;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).destroy(SealConst.GROUP_LIST_UPDATE);
    }


}
