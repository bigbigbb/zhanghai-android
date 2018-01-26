package com.caishi.zhanghai.im.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caishi.zhanghai.im.App;
import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.SealAppContext;
import com.caishi.zhanghai.im.SealConst;
import com.caishi.zhanghai.im.SealUserInfoManager;
import com.caishi.zhanghai.im.bean.AddFriendBean;
import com.caishi.zhanghai.im.bean.AddFriendReturnBean;
import com.caishi.zhanghai.im.bean.SearchFriendBean;
import com.caishi.zhanghai.im.bean.SearchFriendReturnBean;
import com.caishi.zhanghai.im.bean.SearchGroupBean;
import com.caishi.zhanghai.im.bean.SearchGroupReturnBean;
import com.caishi.zhanghai.im.db.Friend;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.SocketClient;
import com.caishi.zhanghai.im.server.network.async.AsyncTaskManager;
import com.caishi.zhanghai.im.server.network.http.HttpException;
import com.caishi.zhanghai.im.server.response.FriendInvitationResponse;
import com.caishi.zhanghai.im.server.response.GetUserInfoByPhoneResponse;
import com.caishi.zhanghai.im.server.utils.AMUtils;
import com.caishi.zhanghai.im.server.utils.CommonUtils;
import com.caishi.zhanghai.im.server.utils.NToast;
import com.caishi.zhanghai.im.server.widget.DialogWithYesOrNoUtils;
import com.caishi.zhanghai.im.server.widget.LoadDialog;
import com.caishi.zhanghai.im.server.widget.SelectableRoundedImageView;
import com.google.gson.Gson;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imlib.model.UserInfo;

public class SearchGroupActivity extends BaseActivity {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private static final int SEARCH_PHONE = 10;
    private static final int ADD_FRIEND = 11;
    private EditText mEtSearch;
    private LinearLayout searchItem;
    private TextView searchName,mTvSearchName,mTvSearchNumber;
    private SelectableRoundedImageView searchImage;
    private String mPhone;
    private String addFriendMessage;
    private String mFriendId;
    private Button mBtnSearch;

    private Friend mFriend;
    private int searchType=0;//默认按群号码搜索

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle((R.string.search_f_group));

        mEtSearch = (EditText) findViewById(R.id.search_edit);
        searchItem = (LinearLayout) findViewById(R.id.search_result);
        searchName = (TextView) findViewById(R.id.search_name);
        mTvSearchNumber = (TextView) findViewById(R.id.ac_search_tv_number);
        mTvSearchName = (TextView) findViewById(R.id.ac_search_tv_name);
        mBtnSearch= (Button) findViewById(R.id.btn_search_group);
        searchImage = (SelectableRoundedImageView) findViewById(R.id.search_header);
        initListener();

    }


    private void initListener(){
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintKbTwo();
                LoadDialog.show(mContext);
                searchGroup();
            }
        });
        mTvSearchNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvSearchName.setTextColor(getResources().getColor(R.color.color_03a9f4));
                mTvSearchNumber.setTextColor(getResources().getColor(R.color.color_434343));
                searchType = 0;

            }
        });
        mTvSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvSearchName.setTextColor(getResources().getColor(R.color.color_03a9f4));
                mTvSearchNumber.setTextColor(getResources().getColor(R.color.color_434343));
                searchType = 1;
            }
        });

    }
    private void searchGroup() {
        SearchGroupBean searchFriendBean = new SearchGroupBean();
        searchFriendBean.setK("search");
        searchFriendBean.setM("group");
        searchFriendBean.setRid(String.valueOf(System.currentTimeMillis()));
        SearchGroupBean.VBean vBean = new SearchGroupBean.VBean();
        mPhone = mEtSearch.getText().toString();
        vBean.setSearchkey(mPhone);
        //type :  "code" 按群编号搜索（精确），"name" 按群名称搜索（模糊）
        if(searchType==0){
            vBean.setType("code");
        }else  if(searchType==1){
            vBean.setType("name");
        }
        searchFriendBean.setV(vBean);
        String msg = new Gson().toJson(searchFriendBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                if (null != json) {
                    SearchGroupReturnBean searchGroupReturnBean = new Gson().fromJson(json, SearchGroupReturnBean.class);
                    if (null != searchGroupReturnBean) {
                        Message message = new Message();
                        message.obj = searchGroupReturnBean;
                        message.what = 0;
                        handler.sendMessage(message);

                    }

                }
            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://查找好友
                    SearchGroupReturnBean searchGroupReturnBean = (SearchGroupReturnBean) msg.obj;
                    LoadDialog.dismiss(mContext);
                    NToast.shortToast(mContext, searchGroupReturnBean.getDesc());
                    if (null != searchGroupReturnBean.getData()) {

                    }

                    searchItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isFriendOrSelf(mFriendId)) {
                                Intent intent = new Intent(SearchGroupActivity.this, UserDetailActivity.class);
                                intent.putExtra("friend", mFriend);
                                intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                                startActivity(intent);
                                SealAppContext.getInstance().pushActivity(SearchGroupActivity.this);
                                return;
                            }
                            DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.add_text), getString(R.string.add_friend), new DialogWithYesOrNoUtils.DialogCallBack() {
                                @Override
                                public void executeEvent() {

                                }

                                @Override
                                public void updatePassword(String oldPassword, String newPassword) {

                                }

                                @Override
                                public void executeEditEvent(String editText) {
                                    if (!CommonUtils.isNetworkConnected(mContext)) {
                                        NToast.shortToast(mContext, R.string.network_not_available);
                                        return;
                                    }
                                    addFriendMessage = editText;
                                    if (TextUtils.isEmpty(editText)) {
                                        addFriendMessage = "我是" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_NAME, "");
                                    }
                                    if (!TextUtils.isEmpty(mFriendId)) {
                                        LoadDialog.show(mContext);
//                                        request(ADD_FRIEND);
                                        addFriend();
                                    } else {
                                        NToast.shortToast(mContext, "id is null");
                                    }
                                }
                            });
                        }
                    });
                    break;

                case 1://添加好友
                    AddFriendReturnBean addFriendReturnBean = (AddFriendReturnBean) msg.obj;
                    AddFriendReturnBean.DataBean dataBean = addFriendReturnBean.getData();
                    NToast.shortToast(mContext, addFriendReturnBean.getDesc());
                    LoadDialog.dismiss(mContext);
                    if(null!=dataBean){


                    }
                    break;
            }
        }
    };


    private void addFriend() {
        AddFriendBean addFriendBean = new AddFriendBean();
        addFriendBean.setK("invite");
        addFriendBean.setM("friend");
        addFriendBean.setRid(String.valueOf(System.currentTimeMillis()));
        AddFriendBean.VBean vBean = new AddFriendBean.VBean();
        vBean.setFriendId(mFriendId);
        vBean.setMessage(addFriendMessage);
        addFriendBean.setV(vBean);
        String json = new Gson().toJson(addFriendBean);
        SocketClient.getInstance().sendMessage(json, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                AddFriendReturnBean addFriendReturnBean = new Gson().fromJson(json, AddFriendReturnBean.class);
                if (null != addFriendReturnBean) {
                    Message message = new Message();
                    message.obj = addFriendReturnBean;
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        });

    }



    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEARCH_PHONE:
                return action.getUserInfoFromPhone("86", mPhone);
            case ADD_FRIEND:
                return action.sendFriendInvitation(mFriendId, addFriendMessage);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case SEARCH_PHONE:
                    final GetUserInfoByPhoneResponse userInfoByPhoneResponse = (GetUserInfoByPhoneResponse) result;
                    if (userInfoByPhoneResponse.getCode() == 200) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "success");
                        mFriendId = userInfoByPhoneResponse.getResult().getId();
                        searchItem.setVisibility(View.VISIBLE);
                        String portraitUri = null;
                        if (userInfoByPhoneResponse.getResult() != null) {
                            GetUserInfoByPhoneResponse.ResultEntity userInfoByPhoneResponseResult = userInfoByPhoneResponse.getResult();
                            UserInfo userInfo = new UserInfo(userInfoByPhoneResponseResult.getId(),
                                    userInfoByPhoneResponseResult.getNickname(),
                                    Uri.parse(userInfoByPhoneResponseResult.getPortraitUri()));
                            portraitUri = SealUserInfoManager.getInstance().getPortraitUri(userInfo);
                        }
                        ImageLoader.getInstance().displayImage(portraitUri, searchImage, App.getOptions());
                        searchName.setText(userInfoByPhoneResponse.getResult().getNickname());
                        searchItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isFriendOrSelf(mFriendId)) {
                                    Intent intent = new Intent(SearchGroupActivity.this, UserDetailActivity.class);
                                    intent.putExtra("friend", mFriend);
                                    intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                                    startActivity(intent);
                                    SealAppContext.getInstance().pushActivity(SearchGroupActivity.this);
                                    return;
                                }
                                DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.add_text), getString(R.string.add_friend), new DialogWithYesOrNoUtils.DialogCallBack() {
                                    @Override
                                    public void executeEvent() {

                                    }

                                    @Override
                                    public void updatePassword(String oldPassword, String newPassword) {

                                    }

                                    @Override
                                    public void executeEditEvent(String editText) {
                                        if (!CommonUtils.isNetworkConnected(mContext)) {
                                            NToast.shortToast(mContext, R.string.network_not_available);
                                            return;
                                        }
                                        addFriendMessage = editText;
                                        if (TextUtils.isEmpty(editText)) {
                                            addFriendMessage = "我是" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_NAME, "");
                                        }
                                        if (!TextUtils.isEmpty(mFriendId)) {
                                            LoadDialog.show(mContext);
                                            request(ADD_FRIEND);
                                        } else {
                                            NToast.shortToast(mContext, "id is null");
                                        }
                                    }
                                });
                            }
                        });

                    }
                    break;
                case ADD_FRIEND:
                    FriendInvitationResponse fres = (FriendInvitationResponse) result;
                    if (fres.getCode() == 200) {
                        NToast.shortToast(mContext, getString(R.string.request_success));
                        LoadDialog.dismiss(mContext);
                    } else {
                        NToast.shortToast(mContext, "请求失败 错误码:" + fres.getCode());
                        LoadDialog.dismiss(mContext);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case ADD_FRIEND:
                NToast.shortToast(mContext, "你们已经是好友");
                LoadDialog.dismiss(mContext);
                break;
            case SEARCH_PHONE:
                if (state == AsyncTaskManager.HTTP_ERROR_CODE || state == AsyncTaskManager.HTTP_NULL_CODE) {
                    super.onFailure(requestCode, state, result);
                } else {
                    NToast.shortToast(mContext, "用户不存在");
                }
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private boolean isFriendOrSelf(String id) {
        String inputPhoneNumber = mEtSearch.getText().toString().trim();
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String selfPhoneNumber = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        if (inputPhoneNumber != null) {
            if (inputPhoneNumber.equals(selfPhoneNumber)) {
                mFriend = new Friend(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""),
                        sp.getString(SealConst.SEALTALK_LOGIN_NAME, ""),
                        Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "")));
                return true;
            } else {
                mFriend = SealUserInfoManager.getInstance().getFriendByID(id);
                if (mFriend != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
