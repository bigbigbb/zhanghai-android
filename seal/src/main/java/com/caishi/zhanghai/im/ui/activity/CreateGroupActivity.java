package com.caishi.zhanghai.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caishi.zhanghai.im.bean.BaseReturnBean;
import com.caishi.zhanghai.im.bean.CreateGroupBean;
import com.caishi.zhanghai.im.bean.CreateGroupReturnBean;
import com.caishi.zhanghai.im.bean.UpLoadPicReBean;
import com.caishi.zhanghai.im.bean.UpLoadPictureBean;
import com.caishi.zhanghai.im.bean.UpLoadPictureReturnBean;
import com.caishi.zhanghai.im.bean.UploadGroupPicBean;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.ReqSSl;
import com.caishi.zhanghai.im.net.SocketClient;
import com.caishi.zhanghai.im.ui.widget.DemoGridView;
import com.caishi.zhanghai.im.ui.widget.switchbutton.SwitchButton;
import com.caishi.zhanghai.im.utils.CommonUtils;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.SealConst;
import com.caishi.zhanghai.im.SealUserInfoManager;
import com.caishi.zhanghai.im.db.Friend;
import com.caishi.zhanghai.im.db.Groups;
import com.caishi.zhanghai.im.server.broadcast.BroadcastManager;
import com.caishi.zhanghai.im.server.network.http.HttpException;
import com.caishi.zhanghai.im.server.response.CreateGroupResponse;
import com.caishi.zhanghai.im.server.response.QiNiuTokenResponse;
import com.caishi.zhanghai.im.server.response.SetGroupPortraitResponse;
import com.caishi.zhanghai.im.server.utils.NToast;
import com.caishi.zhanghai.im.server.utils.photo.PhotoUtils;
import com.caishi.zhanghai.im.server.widget.BottomMenuDialog;
import com.caishi.zhanghai.im.server.widget.ClearWriteEditText;
import com.caishi.zhanghai.im.server.widget.LoadDialog;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateGroupActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private static final int GET_QI_NIU_TOKEN = 131;
    private static final int CREATE_GROUP = 16;
    private static final int SET_GROUP_PORTRAIT_URI = 17;
    public static final String REFRESH_GROUP_UI = "REFRESH_GROUP_UI";
    private AsyncImageView asyncImageView;
    private LinearLayout ll_group_two_two;
    private TextView create_group_tv_show,create_group_tv_look,tv_group_two_two;
    private DemoGridView ac_gd_crate_group;
    private PhotoUtils photoUtils;
    private BottomMenuDialog dialog;
    private String mGroupName, mGroupId;
    private ClearWriteEditText mGroupNameEdit;
    private List<String> groupIds = new ArrayList<>();
    private Uri selectUri;
    private UploadManager uploadManager;
    private String portraitUri;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private MyAdpter myAdpter;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle(R.string.rc_item_create_group);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        List<Friend> memberList = (List<Friend>) getIntent().getSerializableExtra("GroupMember");
        initView();
        setPortraitChangeListener();
        List<String> list = new ArrayList<>();
        if (memberList != null && memberList.size() > 0) {
            groupIds.add(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""));
            for (Friend f : memberList) {
                groupIds.add(f.getUserId());
                list.add(f.getName());
            }
        }

//        for (int i =0 ;i<100;i++){
//            list.add(""+i);
//        }
        create_group_tv_show.setText("您已选择了"+list.size()+"位联系人创建群组");

        if(list.size()>10){
            create_group_tv_look.setVisibility(View.VISIBLE);
        }else {
            create_group_tv_look.setVisibility(View.GONE);
        }
        myAdpter = new MyAdpter(list);
        ac_gd_crate_group.setAdapter(myAdpter);
    }

    private String baseString;

    private void setPortraitChangeListener() {
        photoUtils = new PhotoUtils(new PhotoUtils.OnPhotoResultListener() {
            @Override
            public void onPhotoResult(Uri uri) {
                if (uri != null && !TextUtils.isEmpty(uri.getPath())) {
                    selectUri = uri;
                    asyncImageView.setImageBitmap(CommonUtils.getBitmapFromUri(uri, getApplication()));
                    baseString = CommonUtils.convertIconToString(CommonUtils.getBitmapFromUri(uri, getApplication()));
//                    LoadDialog.show(mContext);
//                    request(GET_QI_NIU_TOKEN);
                }
            }

            @Override
            public void onPhotoCancel() {

            }
        });
    }

    private void initView() {
        asyncImageView = (AsyncImageView) findViewById(R.id.img_Group_portrait);
        asyncImageView.setOnClickListener(this);
        Button mButton = (Button) findViewById(R.id.create_ok);
        SwitchButton sw_group_one = (SwitchButton) findViewById(R.id.sw_group_one);
        SwitchButton sw_group_two = (SwitchButton) findViewById(R.id.sw_group_two);
        SwitchButton sw_group_three = (SwitchButton) findViewById(R.id.sw_group_three);
        SwitchButton sw_group_four = (SwitchButton) findViewById(R.id.sw_group_four);
        ll_group_two_two = (LinearLayout) findViewById(R.id.ll_group_two_two);
        create_group_tv_look = (TextView) findViewById(R.id.create_group_tv_look);
        tv_group_two_two = (TextView) findViewById(R.id.tv_group_two_two);
        create_group_tv_show = (TextView) findViewById(R.id.create_group_tv_show);
        ac_gd_crate_group = (DemoGridView) findViewById(R.id.ac_gd_crate_group);
        mButton.setOnClickListener(this);
        create_group_tv_look.setOnClickListener(this);
        sw_group_one.setOnCheckedChangeListener(this);
        sw_group_two.setOnCheckedChangeListener(this);
        sw_group_three.setOnCheckedChangeListener(this);
        sw_group_four.setOnCheckedChangeListener(this);
        mGroupNameEdit = (ClearWriteEditText) findViewById(R.id.create_groupname);

    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.sw_group_one:
                if(b){
                   is_join_direct = true;
                }else {
                    is_join_direct = false;
                }
                break;
            case R.id.sw_group_two:
                if(b){
                    is_join_via_pay = true;
                    ll_group_two_two.setVisibility(View.VISIBLE);
                }else {
                    is_join_via_pay =false;
                    ll_group_two_two.setVisibility(View.GONE);
                }
                break;
            case R.id.sw_group_three:
                if(b){
                    is_view_each = true;
                }else {
                    is_view_each = false;
                }
                break;
            case R.id.sw_group_four:
                if(b){
                    is_invite_each= true;
                }else {
                    is_invite_each =false;
                }
                break;
        }

    }

    private  boolean isLookAll = false;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_group_tv_look://查看更多群成员
                if(isLookAll){
                   isLookAll = !isLookAll;
                }else {
                    isLookAll = !isLookAll;
                }
                myAdpter.notifyDataSetChanged();
                break;
            case R.id.img_Group_portrait:
                showPhotoDialog();
                break;
            case R.id.create_ok:
                mGroupName = mGroupNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mGroupName)) {
                    NToast.shortToast(mContext, getString(R.string.group_name_not_is_null));
                    break;
                }
                if (mGroupName.length() == 1) {
                    NToast.shortToast(mContext, getString(R.string.group_name_size_is_one));
                    return;
                }
                if (AndroidEmoji.isEmoji(mGroupName)) {
                    if (mGroupName.length() <= 2) {
                        NToast.shortToast(mContext, getString(R.string.group_name_size_is_one));
                        return;
                    }
                }
                if (groupIds.size() > 1) {
                    LoadDialog.show(mContext);
//                    request(CREATE_GROUP, true);
                    createGroup();
                }

                break;
        }
    }


    private boolean is_join_direct =false;
    private boolean is_join_via_pay =false;
    private boolean is_view_each =false;
    private boolean is_invite_each =false;
    /**
     "groupName": "吃喝玩乐群",
     "groupMemberList": ["e10adc3949ba59abbe56e057f20f883e","e10adc3949ba59abbe56e057f20f883e"],
     "join_direct":"1", //是否直接入群而不需要审核
     "is_join_via_pay":"1", //是否开启付费入群功能,1或0
     "join_amount":"10.00", //入群费用10.00元,上面开关是0的话则这里填0.00
     "is_view_each":"0", //群成员是否可以点开其他成员头像查看资料
     "is_invite_each":"0", //群成员是否可以发送添加好友给其他成员
     */
    private void createGroup() {
        String joinAmount = tv_group_two_two.getText().toString();
        final CreateGroupBean createGroupBean = new CreateGroupBean();
        createGroupBean.setK("create");
        createGroupBean.setM("group");
        createGroupBean.setRid(String.valueOf(System.currentTimeMillis()));
        CreateGroupBean.VBean vBean = new CreateGroupBean.VBean();
        vBean.setGroupMemberList(groupIds);
        vBean.setGroupName(mGroupName);
        if(is_join_direct){
            vBean.setJoin_direct("1");
        }else {
            vBean.setJoin_direct("0");
        }
        if(is_join_via_pay){
            vBean.setIs_join_via_pay("1");
            vBean.setJoin_amount(joinAmount);
        }else {
            vBean.setIs_join_via_pay("0");
            vBean.setJoin_amount("0");
        }


        if(is_view_each){
            vBean.setIs_view_each("1");
        }else {
            vBean.setIs_view_each("0");
        }

        if(is_invite_each){
            vBean.setIs_invite_each("1");
        }else {
            vBean.setIs_invite_each("0");
        }

        createGroupBean.setV(vBean);

        String msg = new Gson().toJson(createGroupBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                CreateGroupReturnBean createGroupReturnBean = new Gson().fromJson(json, CreateGroupReturnBean.class);
                if (null != createGroupReturnBean) {
                    Message message = new Message();
                    message.obj = createGroupReturnBean;
                    message.what = 0;
                    handler.sendMessage(message);

                }

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CreateGroupReturnBean createGroupReturnBean = (CreateGroupReturnBean) msg.obj;
                    NToast.shortToast(mContext, createGroupReturnBean.getDesc());
                    if (createGroupReturnBean.getV().equals("ok")) {
                        mGroupId = createGroupReturnBean.getData().getGroupId(); //id == null
                        if(TextUtils.isEmpty(baseString)){
                            SealUserInfoManager.getInstance().addGroup(new Groups(mGroupId, mGroupName, null, String.valueOf(0)));
                            BroadcastManager.getInstance(mContext).sendBroadcast(REFRESH_GROUP_UI);
                            LoadDialog.dismiss(mContext);
                            RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, mGroupId, mGroupName);
                            finish();
                        }else {
                            uploadPic();
                        }

                    }
                    break;

                case 1:
                    BaseReturnBean upLoadPictureReturnBean = (BaseReturnBean)msg.obj;
                    if (upLoadPictureReturnBean.getV().equals("ok")) {
                        SealUserInfoManager.getInstance().addGroup(new Groups(mGroupId, mGroupName, portraitUri, String.valueOf(0)));
                        BroadcastManager.getInstance(mContext).sendBroadcast(REFRESH_GROUP_UI);
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, upLoadPictureReturnBean.getDesc());
                        RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, mGroupId, mGroupName);
                        finish();
                    }
                    break;
            }
        }
    };


    private void uploadPic() {
        String cacheToken = sp.getString("loginToken", "");
        String cacheAccount = sp.getString("loginAccount", "");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("account", cacheAccount);
        hashMap.put("token", cacheToken);
        hashMap.put("avatar", baseString);
        hashMap.put("groupId", mGroupId);
        ReqSSl.getInstance().requestPost(getApplication(), "http://zhanghai.looklaw.cn/upload/avatar/", hashMap, new ReqSSl.ReqListener() {
            @Override
            public void success(String response) {
                Log.e("response", response);
                if (response.contains("200")) {
                    UpLoadPicReBean upLoadPicReBean = new Gson().fromJson(response, UpLoadPicReBean.class);
                    portraitUri = upLoadPicReBean.getData().getPortraitUri();
                    uploadPicture(portraitUri);
                    LoadDialog.dismiss(mContext);
                }


            }

            @Override
            public void failed() {

            }
        });

    }

    private void uploadPicture(String portraitUri) {
        UploadGroupPicBean upLoadPictureBean = new UploadGroupPicBean();
        upLoadPictureBean.setK("portrait");
        upLoadPictureBean.setM("group");
        upLoadPictureBean.setRid(String.valueOf(System.currentTimeMillis()));
        UploadGroupPicBean.VBean vBean = new UploadGroupPicBean.VBean();
        vBean.setPortraitUri(portraitUri);
        vBean.setGroupId(mGroupId);
        upLoadPictureBean.setV(vBean);
        String msg = new Gson().toJson(upLoadPictureBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                BaseReturnBean upLoadPictureReturnBean = new Gson().fromJson(json, BaseReturnBean.class);
                if (null != upLoadPictureReturnBean) {
                    Message message = new Message();
                    message.obj = upLoadPictureReturnBean;
                    message.what = 1;
                    handler.sendMessage(message);
                }

            }
        });

    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CREATE_GROUP:
                return action.createGroup(mGroupName, groupIds);
            case SET_GROUP_PORTRAIT_URI:
//                return action.setGroupPortrait(mGroupId, imageUrl);
            case GET_QI_NIU_TOKEN:
                return action.getQiNiuToken();
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case CREATE_GROUP:
                    CreateGroupResponse createGroupResponse = (CreateGroupResponse) result;
                    if (createGroupResponse.getCode() == 200) {
//                        mGroupId = createGroupResponse.getResult().getId(); //id == null
//                        if (TextUtils.isEmpty(imageUrl)) {
//                            SealUserInfoManager.getInstance().addGroup(new Groups(mGroupId, mGroupName, imageUrl, String.valueOf(0)));
//                            BroadcastManager.getInstance(mContext).sendBroadcast(REFRESH_GROUP_UI);
//                            LoadDialog.dismiss(mContext);
//                            NToast.shortToast(mContext, getString(R.string.create_group_success));
//                            RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, mGroupId, mGroupName);
//                            finish();
//                        } else {
//                            if (!TextUtils.isEmpty(mGroupId)) {
//                                request(SET_GROUP_PORTRAIT_URI);
//                            }
//                        }
                    }
                    break;
                case SET_GROUP_PORTRAIT_URI:
//                    SetGroupPortraitResponse groupPortraitResponse = (SetGroupPortraitResponse) result;
//                    if (groupPortraitResponse.getCode() == 200) {
//                        SealUserInfoManager.getInstance().addGroup(new Groups(mGroupId, mGroupName, imageUrl, String.valueOf(0)));
//                        BroadcastManager.getInstance(mContext).sendBroadcast(REFRESH_GROUP_UI);
//                        LoadDialog.dismiss(mContext);
//                        NToast.shortToast(mContext, getString(R.string.create_group_success));
//                        RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, mGroupId, mGroupName);
//                        finish();
//                    }
                case GET_QI_NIU_TOKEN:
                    QiNiuTokenResponse response = (QiNiuTokenResponse) result;
                    if (response.getCode() == 200) {
                        uploadImage(response.getResult().getDomain(), response.getResult().getToken(), selectUri);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case CREATE_GROUP:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, getString(R.string.group_create_api_fail));
                break;
            case GET_QI_NIU_TOKEN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, getString(R.string.upload_portrait_failed));
                break;
            case SET_GROUP_PORTRAIT_URI:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, getString(R.string.group_header_api_fail));
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
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
                photoUtils.takePicture(CreateGroupActivity.this);
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                photoUtils.selectPicture(CreateGroupActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PhotoUtils.INTENT_CROP:
            case PhotoUtils.INTENT_TAKE:
            case PhotoUtils.INTENT_SELECT:
                photoUtils.onActivityResult(CreateGroupActivity.this, requestCode, resultCode, data);
                break;
        }
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
//                        imageUrl = "http://" + domain + "/" + key;
//                        Log.e("uploadImage", imageUrl);
//                        if (!TextUtils.isEmpty(imageUrl)) {
//                            ImageLoader.getInstance().displayImage(imageUrl, asyncImageView);
//                            LoadDialog.dismiss(mContext);
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    NToast.shortToast(mContext, getString(R.string.upload_portrait_failed));
                    LoadDialog.dismiss(mContext);
                }
            }
        }, null);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private class MyAdpter extends BaseAdapter{
        private List<String> stringList;

        public MyAdpter(List<String> stringList) {
            this.stringList = stringList;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.activity_create_group_item,viewGroup,false);
            TextView textView = (TextView) view.findViewById(R.id.tv_create_group_item);
            if(null!=stringList&&stringList.size()>0){
                textView.setText(stringList.get(i));
            }

            return view;
        }

        @Override
        public Object getItem(int i) {
            return stringList.get(i);
        }

        @Override
        public int getCount() {
            int  count = stringList.size();
            if(count>10){
                if(isLookAll){
                    count = stringList.size();
                }else {
                    count =10;
                }
            }
            return count;
        }


    }
}
