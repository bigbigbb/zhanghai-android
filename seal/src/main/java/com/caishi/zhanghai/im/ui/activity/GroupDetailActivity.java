package com.caishi.zhanghai.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caishi.zhanghai.im.bean.BaseReturnBean;
import com.caishi.zhanghai.im.bean.BeanBean;
import com.caishi.zhanghai.im.bean.ChangeGUserNameBean;
import com.caishi.zhanghai.im.bean.ChangeGroupNameBean;
import com.caishi.zhanghai.im.bean.ChangeGroupSettingBean;
import com.caishi.zhanghai.im.bean.CreateGroupBean;
import com.caishi.zhanghai.im.bean.GroupInfoReturnBean;
import com.caishi.zhanghai.im.bean.GroupListReturnBean;
import com.caishi.zhanghai.im.bean.GroupMembersReturnBean;
import com.caishi.zhanghai.im.bean.QuitGroupBean;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.SocketClient;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.caishi.zhanghai.im.App;
import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.SealAppContext;
import com.caishi.zhanghai.im.SealConst;
import com.caishi.zhanghai.im.SealUserInfoManager;
import com.caishi.zhanghai.im.db.DBManager;
import com.caishi.zhanghai.im.db.Friend;
import com.caishi.zhanghai.im.db.GroupMember;
import com.caishi.zhanghai.im.db.Groups;
import com.caishi.zhanghai.im.db.GroupsDao;
import com.caishi.zhanghai.im.model.SealSearchConversationResult;
import com.caishi.zhanghai.im.server.broadcast.BroadcastManager;
import com.caishi.zhanghai.im.server.network.http.HttpException;
import com.caishi.zhanghai.im.server.pinyin.CharacterParser;
import com.caishi.zhanghai.im.server.response.DismissGroupResponse;
import com.caishi.zhanghai.im.server.response.GetGroupInfoResponse;
import com.caishi.zhanghai.im.server.response.QiNiuTokenResponse;
import com.caishi.zhanghai.im.server.response.QuitGroupResponse;
import com.caishi.zhanghai.im.server.response.SetGroupDisplayNameResponse;
import com.caishi.zhanghai.im.server.response.SetGroupNameResponse;
import com.caishi.zhanghai.im.server.response.SetGroupPortraitResponse;
import com.caishi.zhanghai.im.server.utils.CommonUtils;
import com.caishi.zhanghai.im.server.utils.NToast;
import com.caishi.zhanghai.im.server.utils.OperationRong;
import com.caishi.zhanghai.im.server.utils.RongGenerate;
import com.caishi.zhanghai.im.server.utils.json.JsonMananger;
import com.caishi.zhanghai.im.server.utils.photo.PhotoUtils;
import com.caishi.zhanghai.im.server.widget.BottomMenuDialog;
import com.caishi.zhanghai.im.server.widget.DialogWithYesOrNoUtils;
import com.caishi.zhanghai.im.server.widget.LoadDialog;
import com.caishi.zhanghai.im.server.widget.SelectableRoundedImageView;
import com.caishi.zhanghai.im.ui.widget.DemoGridView;
import com.caishi.zhanghai.im.ui.widget.switchbutton.SwitchButton;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/1/27.
 * Company RongCloud
 */
public class
GroupDetailActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;

    private static final int DISMISS_GROUP = 26;
    private static final int QUIT_GROUP = 27;
    private static final int SET_GROUP_NAME = 29;
    private static final int GET_GROUP_INFO = 30;
    private static final int UPDATE_GROUP_NAME = 32;
    private static final int GET_QI_NIU_TOKEN = 133;
    private static final int UPDATE_GROUP_HEADER = 25;
    private static final int SEARCH_TYPE_FLAG = 1;
    private static final int CHECKGROUPURL = 39;


    private boolean isCreated = false;
    private DemoGridView mGridView;
    private List<GroupMember> mGroupMember;
    private LinearLayout ll_group_two_two;
    private TextView mTextViewMemberSize, mGroupDisplayNameText;
    private SelectableRoundedImageView mGroupHeader;
    private SwitchButton messageTop, messageNotification;
    private SwitchButton sw_group_one, sw_group_two, sw_group_three, sw_group_four;
    private Groups mGroup;
    private String fromConversationId;
    private Conversation.ConversationType mConversationType;
    private boolean isFromConversation;
    private LinearLayout mGroupAnnouncementDividerLinearLayout, detail_group_lly_setting;
    private RelativeLayout group_member_apply_lly;
    private TextView mGroupName, mTvName, tv_group_two_two;
    private PhotoUtils photoUtils;
    private BottomMenuDialog dialog;
    private UploadManager uploadManager;
    private String imageUrl;
    private Uri selectUri;
    private String newGroupName, cacheAccount, newNickName;
    private LinearLayout mGroupNotice;
    private LinearLayout mSearchMessagesLinearLayout;
    private Button mDismissBtn;
    private Button mQuitBtn;
    private SealSearchConversationResult mResult;
    private boolean isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);
        initViews();
        setTitle(R.string.group_info);

        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        cacheAccount = sp.getString("loginAccount", "");
        //群组会话界面点进群组详情
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");

        if (!TextUtils.isEmpty(fromConversationId)) {
            isFromConversation = true;
        }

        if (isFromConversation) {//群组会话页进入
            LoadDialog.show(mContext);
//            getGroups();
//            getGroupMembers();
        }
        setPortraitChangeListener();

        SealAppContext.getInstance().pushActivity(this);

        setGroupsInfoChangeListener();
        getGroupInfo();

    }

    private void getGroupInfo() {
        QuitGroupBean groupBean = new QuitGroupBean();
        groupBean.setK("info");
        groupBean.setM("group");
        groupBean.setRid(String.valueOf(System.currentTimeMillis()));
        QuitGroupBean.VBean vBean = new QuitGroupBean.VBean();
        vBean.setGroupId(fromConversationId);
        groupBean.setV(vBean);
        String msg = new Gson().toJson(groupBean);

        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
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

    private void getGroupMember() {
        QuitGroupBean groupBean = new QuitGroupBean();
        groupBean.setK("members");
        groupBean.setM("group");
        groupBean.setRid(String.valueOf(System.currentTimeMillis()));
        QuitGroupBean.VBean vBean = new QuitGroupBean.VBean();
        vBean.setGroupId(fromConversationId);
        groupBean.setV(vBean);
        String msg = new Gson().toJson(groupBean);

        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("json", json);
                GroupMembersReturnBean groupMembersReturnBean = new Gson().fromJson(json, GroupMembersReturnBean.class);
                if (null != groupMembersReturnBean) {
                    Message message = new Message();
                    message.obj = groupMembersReturnBean;
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void changeGroupName() {
        ChangeGroupNameBean groupBean = new ChangeGroupNameBean();
        groupBean.setK("setname");
        groupBean.setM("group");
        groupBean.setRid(String.valueOf(System.currentTimeMillis()));
        ChangeGroupNameBean.VBean vBean = new ChangeGroupNameBean.VBean();
        vBean.setGroupId(fromConversationId);
        vBean.setGroupName(newGroupName);
        groupBean.setV(vBean);
        String msg = new Gson().toJson(groupBean);

        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("json", json);
                BaseReturnBean baseReturnBean = new Gson().fromJson(json, BaseReturnBean.class);
                if (null != baseReturnBean) {
                    Message message = new Message();
                    message.obj = baseReturnBean;
                    message.what = 4;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void changeUserName() {
        ChangeGUserNameBean gUserNameBean = new ChangeGUserNameBean();
        gUserNameBean.setK("setnick");
        gUserNameBean.setM("group");
        gUserNameBean.setRid(String.valueOf(System.currentTimeMillis()));
        ChangeGUserNameBean.VBean vBean = new ChangeGUserNameBean.VBean();
        vBean.setGroupId(fromConversationId);
        vBean.setNickName(newNickName);
        gUserNameBean.setV(vBean);
        String msg = new Gson().toJson(gUserNameBean);

        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("json", json);
                BaseReturnBean baseReturnBean = new Gson().fromJson(json, BaseReturnBean.class);
                if (null != baseReturnBean) {
                    Message message = new Message();
                    message.obj = baseReturnBean;
                    message.what = 5;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void getGroups() {
        SealUserInfoManager.getInstance().getGroupsByID(fromConversationId, new SealUserInfoManager.ResultCallback<Groups>() {

            @Override
            public void onSuccess(Groups groups) {
                if (groups != null) {
                    mGroup = groups;
                    initGroupData();
                }
            }

            @Override
            public void onError(String errString) {

            }
        });
    }

    private void getGroupMembers() {
        SealUserInfoManager.getInstance().getGroupMembers(fromConversationId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                LoadDialog.dismiss(mContext);
                if (groupMembers != null && groupMembers.size() > 0) {
                    mGroupMember = groupMembers;
                    initGroupMemberData();
                }
            }

            @Override
            public void onError(String errString) {
                LoadDialog.dismiss(mContext);
            }
        });
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_GROUP_NAME);
        BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_GROUP_MEMBER);
        BroadcastManager.getInstance(this).destroy(SealAppContext.GROUP_DISMISS);
        super.onDestroy();
    }

    private void initGroupData() {
        String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mGroup);
        ImageLoader.getInstance().displayImage(portraitUri, mGroupHeader, App.getOptions());
        mGroupName.setText(mGroup.getDisplayName());

        /**
         * join_direct : 0
         * is_join_via_pay : 0.00
         * join_amount : 0.00
         * is_view_each : 0
         * is_invite_each : 0
         */
        int join_direct = groupInfoBean.getJoin_direct();
        String join_amount = groupInfoBean.getJoin_amount();
        int is_view_each = groupInfoBean.getIs_view_each();
        int is_invite_each = groupInfoBean.getIs_invite_each();
        String is_join_via_pay = groupInfoBean.getIs_join_via_pay();
        if (join_direct == 1) {
            sw_group_one.setChecked(true);
        } else {
            sw_group_one.setChecked(false);
        }
        if (is_view_each == 1) {
            sw_group_three.setChecked(true);
        } else {
            sw_group_three.setChecked(false);
        }
        if (is_invite_each == 1) {
            sw_group_four.setChecked(true);
        } else {
            sw_group_four.setChecked(false);
        }
        if (is_join_via_pay.equals("0.00")) {
            sw_group_two.setChecked(false);
            ll_group_two_two.setVisibility(View.GONE);
        } else {
            sw_group_two.setChecked(true);
            ll_group_two_two.setVisibility(View.VISIBLE);
        }
        tv_group_two_two.setText(join_amount);
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, mGroup.getGroupsId(), new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        messageTop.setChecked(true);
                    } else {
                        messageTop.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, mGroup.getGroupsId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                    if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                        messageNotification.setChecked(true);
                    } else {
                        messageNotification.setChecked(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }

        if (mGroup.getRole().equals(cacheAccount)) {
            isCreated = true;
            detail_group_lly_setting.setVisibility(View.VISIBLE);
            group_member_apply_lly.setVisibility(View.VISIBLE);


        }
        if (!isCreated) {
            mGroupAnnouncementDividerLinearLayout.setVisibility(View.VISIBLE);
            mGroupNotice.setVisibility(View.VISIBLE);
        } else {
            mGroupAnnouncementDividerLinearLayout.setVisibility(View.VISIBLE);
            mDismissBtn.setVisibility(View.VISIBLE);
            mQuitBtn.setVisibility(View.GONE);
            mGroupNotice.setVisibility(View.VISIBLE);
        }
        if (CommonUtils.isNetworkConnected(mContext)) {
            request(CHECKGROUPURL);
        }
    }

    private void initGroupMemberData() {
        if (mGroupMember != null && mGroupMember.size() > 0) {
            setTitle(getString(R.string.group_info) + "(" + mGroupMember.size() + ")");
            mTextViewMemberSize.setText(getString(R.string.group_member_size) + "(" + mGroupMember.size() + ")");
            mGridView.setAdapter(new GridAdapter(mContext, mGroupMember));
        } else {
            return;
        }

        for (GroupMember member : mGroupMember) {
            if (member.getUserId().equals(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""))) {
                if (!TextUtils.isEmpty(member.getDisplayName())) {
                    mGroupDisplayNameText.setText(member.getDisplayName());
                } else {
                    mGroupDisplayNameText.setText("无");
                }
            }
        }
    }

    /**
     * 删除并退出群、解散并退出群
     */
    private void quitOrDissGroup(final String type) {
        QuitGroupBean quitGroupBean = new QuitGroupBean();
        quitGroupBean.setK(type);
        quitGroupBean.setM("group");
        quitGroupBean.setRid(String.valueOf(System.currentTimeMillis()));
        QuitGroupBean.VBean vBean = new QuitGroupBean.VBean();
        vBean.setGroupId(fromConversationId);
        quitGroupBean.setV(vBean);
        String msg = new Gson().toJson(quitGroupBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("msg1111", json);
                BaseReturnBean baseReturnBean = new Gson().fromJson(json, BaseReturnBean.class);
                if (null != baseReturnBean) {
                    Message message = new Message();
                    message.obj = baseReturnBean;
                    if (type.equals("quit")) {
                        message.what = 0;
                    } else if (type.equals("dismiss")) {
                        message.what = 3;
                    }

                    handler.sendMessage(message);
                }


            }
        });

    }

    private boolean is_join_direct = false;
    private boolean is_join_via_pay = false;
    private boolean is_view_each = false;
    private boolean is_invite_each = false;

    private void changeSetting(String key, String value) {
        String joinAmount = tv_group_two_two.getText().toString();
        ChangeGroupSettingBean changeGroupSettingBean = new ChangeGroupSettingBean();
        changeGroupSettingBean.setK("config");
        changeGroupSettingBean.setM("group");
        changeGroupSettingBean.setRid(String.valueOf(System.currentTimeMillis()));
        ChangeGroupSettingBean.VBean vBean = new ChangeGroupSettingBean.VBean();
        vBean.setGroupId(fromConversationId);
        vBean.setKey(key);
        vBean.setValue(value);
//        vBean.setGroupName(mGroupName);
//        if (is_join_direct) {
//            vBean.setJoin_direct("1");
//        } else {
//            vBean.setJoin_direct("0");
//        }
//        if (is_join_via_pay) {
//            vBean.setIs_join_via_pay("1");
//            vBean.setJoin_amount(joinAmount);
//        } else {
//            vBean.setIs_join_via_pay("0");
//            vBean.setJoin_amount("0");
//        }
//
//
//        if (is_view_each) {
//            vBean.setIs_view_each("1");
//        } else {
//            vBean.setIs_view_each("0");
//        }
//
//        if (is_invite_each) {
//            vBean.setIs_invite_each("1");
//        } else {
//            vBean.setIs_invite_each("0");
//        }

        changeGroupSettingBean.setV(vBean);

        String msg = new Gson().toJson(changeGroupSettingBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("msg1111", json);
                BaseReturnBean baseReturnBean = new Gson().fromJson(json, BaseReturnBean.class);
                if (null != baseReturnBean) {
                    Message message = new Message();
                    message.obj = baseReturnBean;
                    message.what = 6;

                    handler.sendMessage(message);
                }
            }
        });
    }


    private GroupInfoReturnBean.DataBean groupInfoBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://退出群组
                    BaseReturnBean baseReturnBean = (BaseReturnBean) msg.obj;
                    NToast.longToast(getApplication(), baseReturnBean.getDesc());
                    if (baseReturnBean.getV().equals("ok")) {
                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                            @Override
                            public void onSuccess(Conversation conversation) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode e) {

                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                        SealUserInfoManager.getInstance().deleteGroups(new Groups(fromConversationId));
                        SealUserInfoManager.getInstance().deleteGroupMembers(fromConversationId);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
                        setResult(501, new Intent());
                        LoadDialog.dismiss(mContext);
                        finish();
                    }
                    break;
                case 1://获取群组消息
                    GroupInfoReturnBean groupInfoReturnBean = (GroupInfoReturnBean) msg.obj;
                    NToast.shortToast(getApplication(), groupInfoReturnBean.getDesc());
                    if (groupInfoReturnBean.getV().equals("ok")) {
                        groupInfoBean = groupInfoReturnBean.getData();
                        mGroup = new Groups();
                        if (!TextUtils.isEmpty(groupInfoBean.getPortraitUri())) {
                            mGroup.setPortraitUri(groupInfoBean.getPortraitUri());
                        }
                        mGroup.setDisplayName(groupInfoBean.getName());
                        mGroup.setGroupsId(groupInfoBean.getId());
                        mGroup.setRole(groupInfoBean.getCreatorId());

                        initGroupData();
                    }
                    getGroupMember();
                    break;

                case 2://获取群成员消息
                    GroupMembersReturnBean groupMembersReturnBean = (GroupMembersReturnBean) msg.obj;
                    NToast.longToast(getApplication(), groupMembersReturnBean.getDesc());
                    if (groupMembersReturnBean.getV().equals("ok")) {
                        LoadDialog.dismiss(mContext);
                        List<GroupMembersReturnBean.DataBean> dataBeanList = groupMembersReturnBean.getData();
                        if (dataBeanList != null && dataBeanList.size() > 0) {
                            mGroupMember = new ArrayList<>();
                            GroupMember groupMember = null;
                            for (GroupMembersReturnBean.DataBean dataBean : dataBeanList) {
                                groupMember = new GroupMember(dataBean.getUser().getId(), dataBean.getUser().getNickname(), Uri.parse((String) dataBean.getUser().getPortraitUri()));

                                mGroupMember.add(groupMember);
                                if (groupMember.getUserId().equals(cacheAccount)) {
                                    String nickName = groupMember.getName();
                                    mTvName.setText(nickName);
                                }
                            }

                            initGroupMemberData();
                        }
                    }

                    break;

                case 3://解散群
                    BaseReturnBean baseReturnBean1 = (BaseReturnBean) msg.obj;
                    NToast.longToast(getApplication(), baseReturnBean1.getDesc());
                    if (baseReturnBean1.getV().equals("ok")) {
                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                            @Override
                            public void onSuccess(Conversation conversation) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode e) {

                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                        SealUserInfoManager.getInstance().deleteGroups(new Groups(fromConversationId));
                        SealUserInfoManager.getInstance().deleteGroupMembers(fromConversationId);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
                        setResult(501, new Intent());
                        NToast.shortToast(mContext, getString(R.string.dismiss_success));
                        LoadDialog.dismiss(mContext);
                        finish();
                    }
                    break;

                case 4://修改群名称
                    BaseReturnBean baseReturnBean4 = (BaseReturnBean) msg.obj;
                    NToast.longToast(getApplication(), baseReturnBean4.getDesc());
                    if (baseReturnBean4.getV().equals("ok")) {
                        SealUserInfoManager.getInstance().addGroup(
                                new Groups(mGroup.getGroupsId(), newGroupName, mGroup.getPortraitUri(), mGroup.getRole())
                        );
                        mGroupName.setText(newGroupName);
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, TextUtils.isEmpty(mGroup.getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(newGroupName, mGroup.getGroupsId())) : Uri.parse(mGroup.getPortraitUri())));
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }
                    break;

                case 5://修改群昵稱
                    BaseReturnBean baseReturnBean5 = (BaseReturnBean) msg.obj;
                    NToast.longToast(getApplication(), baseReturnBean5.getDesc());
                    if (baseReturnBean5.getV().equals("ok")) {
                        mTvName.setText(newNickName);
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }
                    break;
                case 6:
                    BaseReturnBean baseReturnBean6 = (BaseReturnBean) msg.obj;
                    NToast.longToast(getApplication(), baseReturnBean6.getDesc());
                    LoadDialog.dismiss(mContext);
                    if (baseReturnBean6.getV().equals("ok")) {

                    }
                    break;
            }
        }
    };

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case QUIT_GROUP:
                return action.quitGroup(fromConversationId);
            case DISMISS_GROUP:
                return action.dissmissGroup(fromConversationId);
            case SET_GROUP_NAME:
                return action.setGroupDisplayName(fromConversationId, newGroupName);
            case GET_GROUP_INFO:
                return action.getGroupInfo(fromConversationId);
            case UPDATE_GROUP_HEADER:
                return action.setGroupPortrait(fromConversationId, imageUrl);
            case GET_QI_NIU_TOKEN:
                return action.getQiNiuToken();
            case UPDATE_GROUP_NAME:
                return action.setGroupName(fromConversationId, newGroupName);
            case CHECKGROUPURL:
                return action.getGroupInfo(fromConversationId);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case QUIT_GROUP:
                    QuitGroupResponse response = (QuitGroupResponse) result;
                    if (response.getCode() == 200) {

                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                            @Override
                            public void onSuccess(Conversation conversation) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode e) {

                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                        SealUserInfoManager.getInstance().deleteGroups(new Groups(fromConversationId));
                        SealUserInfoManager.getInstance().deleteGroupMembers(fromConversationId);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
                        setResult(501, new Intent());
                        NToast.shortToast(mContext, getString(R.string.quit_success));
                        LoadDialog.dismiss(mContext);
                        finish();
                    }
                    break;

                case DISMISS_GROUP:
                    DismissGroupResponse response1 = (DismissGroupResponse) result;
                    if (response1.getCode() == 200) {
                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                            @Override
                            public void onSuccess(Conversation conversation) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode e) {

                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                        SealUserInfoManager.getInstance().deleteGroups(new Groups(fromConversationId));
                        SealUserInfoManager.getInstance().deleteGroupMembers(fromConversationId);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
                        setResult(501, new Intent());
                        NToast.shortToast(mContext, getString(R.string.dismiss_success));
                        LoadDialog.dismiss(mContext);
                        finish();
                    }
                    break;

                case SET_GROUP_NAME:
                    SetGroupDisplayNameResponse response2 = (SetGroupDisplayNameResponse) result;
                    if (response2.getCode() == 200) {
                        request(GET_GROUP_INFO);
                    }
                    break;
                case GET_GROUP_INFO:
                    GetGroupInfoResponse response3 = (GetGroupInfoResponse) result;
                    if (response3.getCode() == 200) {
                        int i;
                        if (isCreated) {
                            i = 0;
                        } else {
                            i = 1;
                        }
                        GetGroupInfoResponse.ResultEntity bean = response3.getResult();
                        SealUserInfoManager.getInstance().addGroup(
                                new Groups(bean.getId(), bean.getName(), bean.getPortraitUri(), newGroupName, String.valueOf(i), null)
                        );
                        mGroupName.setText(newGroupName);
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, Uri.parse(bean.getPortraitUri())));
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }
                    break;
                case UPDATE_GROUP_HEADER:
                    SetGroupPortraitResponse response5 = (SetGroupPortraitResponse) result;
                    if (response5.getCode() == 200) {
                        ImageLoader.getInstance().displayImage(imageUrl, mGroupHeader, App.getOptions());
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, mGroup.getName(), Uri.parse(imageUrl)));
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }

                    break;
                case GET_QI_NIU_TOKEN:
                    QiNiuTokenResponse response6 = (QiNiuTokenResponse) result;
                    if (response6.getCode() == 200) {
                        uploadImage(response6.getResult().getDomain(), response6.getResult().getToken(), selectUri);
                    }
                    break;
                case UPDATE_GROUP_NAME:
                    SetGroupNameResponse response7 = (SetGroupNameResponse) result;
                    if (response7.getCode() == 200) {
                        SealUserInfoManager.getInstance().addGroup(
                                new Groups(mGroup.getGroupsId(), newGroupName, mGroup.getPortraitUri(), mGroup.getRole())
                        );
                        mGroupName.setText(newGroupName);
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, TextUtils.isEmpty(mGroup.getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(newGroupName, mGroup.getGroupsId())) : Uri.parse(mGroup.getPortraitUri())));
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }
                    break;
                case CHECKGROUPURL:
                    GetGroupInfoResponse groupInfoResponse = (GetGroupInfoResponse) result;
                    if (groupInfoResponse.getCode() == 200) {
                        if (groupInfoResponse.getResult() != null) {
                            mGroupName.setText(groupInfoResponse.getResult().getName());
                            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(groupInfoResponse);
                            ImageLoader.getInstance().displayImage(portraitUri, mGroupHeader, App.getOptions());
                            RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, groupInfoResponse.getResult().getName(), TextUtils.isEmpty(groupInfoResponse.getResult().getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(groupInfoResponse.getResult().getName(), groupInfoResponse.getResult().getId())) : Uri.parse(groupInfoResponse.getResult().getPortraitUri())));
                        }
                    }
                    break;
            }
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case QUIT_GROUP:
                NToast.shortToast(mContext, "退出群组请求失败");
                LoadDialog.dismiss(mContext);
                break;
            case DISMISS_GROUP:
                NToast.shortToast(mContext, "解散群组请求失败");
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_quit:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, getString(R.string.confirm_quit_group), new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        LoadDialog.show(mContext);
//                        request(QUIT_GROUP);
                        quitOrDissGroup("quit");
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.group_dismiss:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, getString(R.string.confirm_dismiss_group), new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        LoadDialog.show(mContext);
//                        request(DISMISS_GROUP);
                        quitOrDissGroup("dismiss");
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.ac_ll_search_chatting_records:
                Intent searchIntent = new Intent(GroupDetailActivity.this, SealSearchChattingDetailActivity.class);
                searchIntent.putExtra("filterString", "");
                ArrayList<io.rong.imlib.model.Message> arrayList = new ArrayList<>();
                searchIntent.putParcelableArrayListExtra("filterMessages", arrayList);
                mResult = new SealSearchConversationResult();
                Conversation conversation = new Conversation();
                conversation.setTargetId(fromConversationId);
                conversation.setConversationType(mConversationType);
                mResult.setConversation(conversation);
                Groups groupInfo = DBManager.getInstance().getDaoSession().getGroupsDao().queryBuilder().where(GroupsDao.Properties.GroupsId.eq(fromConversationId)).unique();
                if (groupInfo != null) {
                    String portraitUri = groupInfo.getPortraitUri();
                    mResult.setId(groupInfo.getGroupsId());

                    if (!TextUtils.isEmpty(portraitUri)) {
                        mResult.setPortraitUri(portraitUri);
                    }
                    if (!TextUtils.isEmpty(groupInfo.getName())) {
                        mResult.setTitle(groupInfo.getName());
                    } else {
                        mResult.setTitle(groupInfo.getGroupsId());
                    }

                    searchIntent.putExtra("searchConversationResult", mResult);
                    searchIntent.putExtra("flag", SEARCH_TYPE_FLAG);
                    startActivity(searchIntent);
                }
                break;
            case R.id.group_clean:
                PromptPopupDialog.newInstance(mContext,
                        getString(R.string.clean_group_chat_history)).setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt_warning)
                        .setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                if (RongIM.getInstance() != null) {
                                    if (mGroup != null) {
                                        RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, mGroup.getGroupsId(), new RongIMClient.ResultCallback<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                                NToast.shortToast(mContext, getString(R.string.clear_success));
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {
                                                NToast.shortToast(mContext, getString(R.string.clear_failure));
                                            }
                                        });
                                        RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP, mGroup.getGroupsId(), System.currentTimeMillis(), null);
                                    }
                                }
                            }
                        }).show();

                break;
            case R.id.group_member_size_item:
                Intent intent = new Intent(mContext, TotalGroupMemberActivity.class);
                intent.putExtra("targetId", fromConversationId);
                startActivity(intent);
                break;
            case R.id.group_member_online_status:
                intent = new Intent(mContext, MembersOnlineStatusActivity.class);
                intent.putExtra("targetId", fromConversationId);
                startActivity(intent);
                break;
            case R.id.ll_group_port:
                if (isCreated) {
                    showPhotoDialog();
                }
                break;
            case R.id.ll_group_name:
                if (isCreated) {
                    DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.new_group_name), getString(R.string.confirm), new DialogWithYesOrNoUtils.DialogCallBack() {
                        @Override
                        public void executeEvent() {

                        }

                        @Override
                        public void executeEditEvent(String editText) {
                            if (TextUtils.isEmpty(editText)) {
                                return;
                            }
                            if (editText.length() < 2 && editText.length() > 10) {
                                NToast.shortToast(mContext, "群名称应为 2-10 字");
                                return;
                            }

                            if (AndroidEmoji.isEmoji(editText) && editText.length() < 4) {
                                NToast.shortToast(mContext, "群名称表情过短");
                                return;
                            }
                            newGroupName = editText;
                            LoadDialog.show(mContext);
//                            request(UPDATE_GROUP_NAME);
                            changeGroupName();
                        }

                        @Override
                        public void updatePassword(String oldPassword, String newPassword) {

                        }
                    });
                } else {
                    NToast.longToast(getApplication(), "只有群主才能修改群名称");
                }
                break;
            case R.id.group_announcement:
                Intent tempIntent = new Intent(mContext, GroupNoticeActivity.class);
                tempIntent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
                tempIntent.putExtra("targetId", fromConversationId);
                startActivity(tempIntent);
                break;

            case R.id.ll_user_name://修改群名片
                DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.new_group_nike), getString(R.string.confirm), new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {

                    }

                    @Override
                    public void executeEditEvent(String editText) {
                        if (TextUtils.isEmpty(editText)) {
                            return;
                        }
                        if (editText.length() < 2 && editText.length() > 10) {
                            NToast.shortToast(mContext, "群昵称应为 2-10 字");
                            return;
                        }

                        if (AndroidEmoji.isEmoji(editText) && editText.length() < 4) {
                            NToast.shortToast(mContext, "群名称表情过短");
                            return;
                        }
                        newNickName = editText;
                        LoadDialog.show(mContext);
//                            request(UPDATE_GROUP_NAME);
                        changeUserName();
                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;

            case R.id.group_member_apply_lly:
                Intent intent1 = new Intent(GroupDetailActivity.this, GroupApplyListAc.class);
                intent1.putExtra("fromConversationId", fromConversationId);
                startActivity(intent1);
                break;
        }
    }

    private boolean isOpen1, isOpen2, isOpen3, isOpen4 = false;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_group_top:
                if (isChecked) {
                    if (mGroup != null) {
                        OperationRong.setConversationTop(mContext, Conversation.ConversationType.GROUP, mGroup.getGroupsId(), true);
                    }
                } else {
                    if (mGroup != null) {
                        OperationRong.setConversationTop(mContext, Conversation.ConversationType.GROUP, mGroup.getGroupsId(), false);
                    }
                }
                break;
            case R.id.sw_group_notfaction:
                if (isChecked) {
                    if (mGroup != null) {
                        OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, mGroup.getGroupsId(), true);
                    }
                } else {
                    if (mGroup != null) {
                        OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, mGroup.getGroupsId(), false);
                    }
                }

                break;
            case R.id.sw_group_one:


                if (isChecked) {
                    is_join_direct = true;
                    if (isOpen1) {
                        changeSetting("join_direct", "1");
                    }

                } else {
                    is_join_direct = false;
                    if (isOpen1) {
                        changeSetting("join_direct", "0");
                    }

                }
                isOpen1 = true;

                break;
            case R.id.sw_group_two:
                if (isChecked) {
                    is_join_via_pay = true;
                    ll_group_two_two.setVisibility(View.VISIBLE);
                    if (isOpen2) {
                        changeSetting("is_join_via_pay", "1");
                    }

                } else {
                    is_join_via_pay = false;
                    ll_group_two_two.setVisibility(View.GONE);
                    if (isOpen2) {
                        changeSetting("is_join_via_pay", "0");
                    }

                }
                isOpen2 = true;
                break;
            case R.id.sw_group_three:
                if (isChecked) {
                    is_view_each = true;
                    if (isOpen3) {
                        changeSetting("is_view_each", "1");
                    }

                } else {
                    is_view_each = false;
                    if (isOpen3) {
                        changeSetting("is_view_each", "0");
                    }

                }
                isOpen3 = true;
                break;
            case R.id.sw_group_four:
                if (isChecked) {
                    is_invite_each = true;
                    if (isOpen4) {
                        changeSetting("is_invite_each", "1");
                    }

                } else {
                    is_invite_each = false;
                    if (isOpen4) {
                        changeSetting("is_invite_each", "0");
                    }

                }
                isOpen4 = true;
                break;
        }
    }


    private class GridAdapter extends BaseAdapter {

        private List<GroupMember> list;
        Context context;


        public GridAdapter(Context context, List<GroupMember> list) {
            if (list.size() >= 31) {
                this.list = list.subList(0, 30);
            } else {
                this.list = list;
            }

            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.social_chatsetting_gridview_item, parent, false);
            }
            SelectableRoundedImageView iv_avatar = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_avatar);
            TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            ImageView badge_delete = (ImageView) convertView.findViewById(R.id.badge_delete);

            // 最后一个item，减人按钮
            if (position == getCount() - 1 && isCreated) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.icon_btn_deleteperson);

                iv_avatar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
                        intent.putExtra("isDeleteGroupMember", true);
                        intent.putExtra("GroupId", mGroup.getGroupsId());
                        startActivityForResult(intent, 101);
                    }

                });
            } else if ((isCreated && position == getCount() - 2) || (!isCreated && position == getCount() - 1)) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);

                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
                        intent.putExtra("isAddGroupMember", true);
                        intent.putExtra("GroupId", mGroup.getGroupsId());
                        startActivityForResult(intent, 100);

                    }
                });
            } else { // 普通成员
                final GroupMember bean = list.get(position);
                Friend friend = SealUserInfoManager.getInstance().getFriendByID(bean.getUserId());
                if (friend != null && !TextUtils.isEmpty(friend.getDisplayName())) {
                    tv_username.setText(friend.getDisplayName());
                } else {
                    tv_username.setText(bean.getName());
                }

                String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(bean);
                ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, App.getOptions());
                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo userInfo = new UserInfo(bean.getUserId(), bean.getName(), TextUtils.isEmpty(bean.getPortraitUri().toString()) ? Uri.parse(RongGenerate.generateDefaultAvatar(bean.getName(), bean.getUserId())) : bean.getPortraitUri());
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
                        intent.putExtra("friend", friend);
                        intent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
                        //Groups not Serializable,just need group name
                        intent.putExtra("groupName", mGroup.getName());
                        intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                        context.startActivity(intent);
                    }

                });

            }

            return convertView;
        }

        @Override
        public int getCount() {
            if (isCreated) {
                return list.size() + 2;
            } else {
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<GroupMember> list) {
            this.list = list;
            notifyDataSetChanged();
        }

    }


    // 拿到新增的成员刷新adapter
    @Override
    @SuppressWarnings("unchecked")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            List<Friend> newMemberData = (List<Friend>) data.getSerializableExtra("newAddMember");
            List<Friend> deleMember = (List<Friend>) data.getSerializableExtra("deleteMember");
            if (newMemberData != null && newMemberData.size() > 0) {
                for (Friend friend : newMemberData) {
                    GroupMember member = new GroupMember(fromConversationId,
                            friend.getUserId(),
                            friend.getName(),
                            friend.getPortraitUri(),
                            null);
                    mGroupMember.add(1, member);
                }
                initGroupMemberData();
            } else if (deleMember != null && deleMember.size() > 0) {
                for (Friend friend : deleMember) {
                    for (GroupMember member : mGroupMember) {
                        if (member.getUserId().equals(friend.getUserId())) {
                            mGroupMember.remove(member);
                            break;
                        }
                    }
                }
                initGroupMemberData();
            }

        }
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(GroupDetailActivity.this, requestCode, resultCode, data);
                break;
        }

    }

    private void setGroupsInfoChangeListener() {
        //有些权限只有群主有,比如修改群名称等,已经更新UI不需要再更新
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_GROUP_NAME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String result = intent.getStringExtra("result");
                    if (result != null) {
                        try {
                            List<String> nameList = JsonMananger.jsonToBean(result, List.class);
                            if (nameList.size() != 3)
                                return;
                            String groupID = nameList.get(0);
                            if (groupID != null && !groupID.equals(fromConversationId))
                                return;
                            if (mGroup != null && mGroup.getRole().equals("0"))
                                return;
                            String groupName = nameList.get(1);
                            String operationName = nameList.get(2);
                            mGroupName.setText(groupName);
                            newGroupName = groupName;
                            NToast.shortToast(mContext, operationName + context.getString(R.string.rc_item_change_group_name)
                                    + "\"" + groupName + "\"");
                            RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, TextUtils.isEmpty(mGroup.getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(newGroupName, mGroup.getGroupsId())) : Uri.parse(mGroup.getPortraitUri())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_GROUP_MEMBER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String groupID = intent.getStringExtra("String");
                    if (groupID != null && groupID.equals(fromConversationId))
                        getGroupMembers();
                }
            }
        });
        BroadcastManager.getInstance(this).addAction(SealAppContext.GROUP_DISMISS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String groupID = intent.getStringExtra("String");
                    if (groupID != null && groupID.equals(fromConversationId)) {
                        if (mGroup.getRole().equals("1"))
                            backAsGroupDismiss();
                    }
                }
            }
        });
    }

    private void backAsGroupDismiss() {
        this.setResult(501, new Intent());
        finish();
    }

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                    LoadDialog.show(mContext);
                    request(GET_QI_NIU_TOKEN);
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }


    /**
     * 弹出底部框
     */
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                photoUtils.takePicture(GroupDetailActivity.this);
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                photoUtils.selectPicture(GroupDetailActivity.this);
            }
        });
        dialog.show();
    }


    public void uploadImage(final String domain, String imageToken, Uri imagePath) {
        if (TextUtils.isEmpty(domain) && TextUtils.isEmpty(imageToken) && TextUtils.isEmpty(imagePath.toString())) {
            throw new RuntimeException("upload parameter is null!");
        }
        File imageFile = new File(imagePath.getPath());

        if (this.uploadManager == null) {
            this.uploadManager = new UploadManager();
        }
        this.uploadManager.put(imageFile, null, imageToken, new UpCompletionHandler() {

            @Override
            public void complete(String s, ResponseInfo responseInfo, JSONObject jsonObject) {
                if (responseInfo.isOK()) {
                    try {
                        String key = (String) jsonObject.get("key");
                        imageUrl = "http://" + domain + "/" + key;
                        Log.e("uploadImage", imageUrl);
                        if (!TextUtils.isEmpty(imageUrl)) {
                            request(UPDATE_GROUP_HEADER);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, null);
    }


    private void initViews() {
        tv_group_two_two = (TextView) findViewById(R.id.tv_group_two_two);
        messageTop = (SwitchButton) findViewById(R.id.sw_group_top);
        messageNotification = (SwitchButton) findViewById(R.id.sw_group_notfaction);
        messageTop.setOnCheckedChangeListener(this);
        messageNotification.setOnCheckedChangeListener(this);
        LinearLayout groupClean = (LinearLayout) findViewById(R.id.group_clean);
        mGridView = (DemoGridView) findViewById(R.id.gridview);
        mTextViewMemberSize = (TextView) findViewById(R.id.group_member_size);
        mGroupHeader = (SelectableRoundedImageView) findViewById(R.id.group_header);
        LinearLayout mGroupDisplayName = (LinearLayout) findViewById(R.id.group_displayname);
        mGroupDisplayNameText = (TextView) findViewById(R.id.group_displayname_text);
        mGroupName = (TextView) findViewById(R.id.group_name);
        mTvName = (TextView) findViewById(R.id.user_tv_name);
        mQuitBtn = (Button) findViewById(R.id.group_quit);
        mDismissBtn = (Button) findViewById(R.id.group_dismiss);
        RelativeLayout totalGroupMember = (RelativeLayout) findViewById(R.id.group_member_size_item);
        RelativeLayout memberOnlineStatus = (RelativeLayout) findViewById(R.id.group_member_online_status);
        sw_group_one = (SwitchButton) findViewById(R.id.sw_group_one);
        sw_group_two = (SwitchButton) findViewById(R.id.sw_group_two);
        sw_group_three = (SwitchButton) findViewById(R.id.sw_group_three);
        sw_group_four = (SwitchButton) findViewById(R.id.sw_group_four);
        ll_group_two_two = (LinearLayout) findViewById(R.id.ll_group_two_two);
        LinearLayout mGroupPortL = (LinearLayout) findViewById(R.id.ll_group_port);
        LinearLayout mGroupNameL = (LinearLayout) findViewById(R.id.ll_group_name);
        LinearLayout ll_user_name = (LinearLayout) findViewById(R.id.ll_user_name);
        detail_group_lly_setting = (LinearLayout) findViewById(R.id.detail_group_lly_setting);
        group_member_apply_lly = (RelativeLayout) findViewById(R.id.group_member_apply_lly);
        mGroupAnnouncementDividerLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_group_announcement_divider);
        mGroupNotice = (LinearLayout) findViewById(R.id.group_announcement);
        mSearchMessagesLinearLayout = (LinearLayout) findViewById(R.id.ac_ll_search_chatting_records);
        mGroupPortL.setOnClickListener(this);
        mGroupNameL.setOnClickListener(this);
        totalGroupMember.setOnClickListener(this);
        mGroupDisplayName.setOnClickListener(this);
        memberOnlineStatus.setOnClickListener(this);
        if (getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isDebug", false)) {
            memberOnlineStatus.setVisibility(View.VISIBLE);
        }
        mQuitBtn.setOnClickListener(this);
        ll_user_name.setOnClickListener(this);
        mDismissBtn.setOnClickListener(this);
        groupClean.setOnClickListener(this);
        mGroupNotice.setOnClickListener(this);
        group_member_apply_lly.setOnClickListener(this);
        mSearchMessagesLinearLayout.setOnClickListener(this);
//        if(isOpen){
        sw_group_one.setOnCheckedChangeListener(this);
        sw_group_two.setOnCheckedChangeListener(this);
        sw_group_three.setOnCheckedChangeListener(this);
        sw_group_four.setOnCheckedChangeListener(this);
//        }


        tv_group_two_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String e = editable.toString();
                Log.e("editable", e);
            }
        });
    }

    @Override
    public void onBackPressed() {
        SealAppContext.getInstance().popActivity(this);
        super.onBackPressed();
    }
}
