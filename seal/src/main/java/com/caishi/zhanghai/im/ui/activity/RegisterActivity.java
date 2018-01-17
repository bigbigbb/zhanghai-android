package com.caishi.zhanghai.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.caishi.zhanghai.im.R;
import com.caishi.zhanghai.im.bean.GetCodeBean;
import com.caishi.zhanghai.im.bean.GetCodeReturnBean;
import com.caishi.zhanghai.im.bean.LoginReturnBean;
import com.caishi.zhanghai.im.bean.SetPassBean;
import com.caishi.zhanghai.im.net.CallBackJson;
import com.caishi.zhanghai.im.net.SocketClient;
import com.caishi.zhanghai.im.server.network.http.HttpException;
import com.caishi.zhanghai.im.server.response.CheckPhoneResponse;
import com.caishi.zhanghai.im.server.response.RegisterResponse;
import com.caishi.zhanghai.im.server.response.SendCodeResponse;
import com.caishi.zhanghai.im.server.response.VerifyCodeResponse;
import com.caishi.zhanghai.im.server.utils.AMUtils;
import com.caishi.zhanghai.im.server.utils.NToast;
import com.caishi.zhanghai.im.server.utils.downtime.DownTimer;
import com.caishi.zhanghai.im.server.utils.downtime.DownTimerListener;
import com.caishi.zhanghai.im.server.widget.ClearWriteEditText;
import com.caishi.zhanghai.im.server.widget.LoadDialog;
import com.caishi.zhanghai.im.utils.MD5;
import com.google.gson.Gson;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class RegisterActivity extends BaseActivity implements View.OnClickListener, DownTimerListener {

    private static final int CHECK_PHONE = 1;
    private static final int SEND_CODE = 2;
    private static final int VERIFY_CODE = 3;
    private static final int REGISTER = 4;
    private static final int REGISTER_BACK = 1001;
    private ImageView mImgBackground;
    private ClearWriteEditText mPhoneEdit, mCodeEdit, mNickEdit, mPasswordEdit;
    private Button mGetCode, mConfirm;
    private String mPhone, mCode, mNickName, mPassword, mCodeToken;
    private boolean isRequestCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView() {
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.reg_phone);
        mCodeEdit = (ClearWriteEditText) findViewById(R.id.reg_code);
        mNickEdit = (ClearWriteEditText) findViewById(R.id.reg_username);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.reg_password);
        mGetCode = (Button) findViewById(R.id.reg_getcode);
        mConfirm = (Button) findViewById(R.id.reg_button);

        mGetCode.setOnClickListener(this);
        //按照我们暂时的需求改成true
        mGetCode.setClickable(true);
        mConfirm.setOnClickListener(this);

        TextView goLogin = (TextView) findViewById(R.id.reg_login);
        TextView goForget = (TextView) findViewById(R.id.reg_forget);
        goLogin.setOnClickListener(this);
        goForget.setOnClickListener(this);

        mImgBackground = (ImageView) findViewById(R.id.rg_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
                mImgBackground.startAnimation(animation);
            }
        }, 200);

        addEditTextListener();

    }

    private void addEditTextListener() {
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11 && isBright) {
                    if (AMUtils.isMobile(s.toString().trim())) {
                        mPhone = s.toString().trim();
                        //暂时去掉   本来是融云用来调用自己后台的接口来判断输入的手机号是否可用
                        //加上设置true
                        mGetCode.setClickable(true);
                        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
//                        request(CHECK_PHONE, true);
                        AMUtils.onInactive(mContext, mPhoneEdit);
                    } else {
                        Toast.makeText(mContext, R.string.Illegal_phone_number, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    mGetCode.setClickable(false);
                    mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    AMUtils.onInactive(mContext, mCodeEdit);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) {
                    mConfirm.setClickable(true);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    mConfirm.setClickable(false);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CHECK_PHONE:
                return action.checkPhoneAvailable("86", mPhone);
            case SEND_CODE:
                return action.sendCode("86", mPhone);
            case VERIFY_CODE:
                return action.verifyCode("86", mPhone, mCode);
            case REGISTER:
                return action.register(mNickName, mPassword, mCodeToken);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case CHECK_PHONE:
                    CheckPhoneResponse cprres = (CheckPhoneResponse) result;
                    if (cprres.getCode() == 200) {
                        if (cprres.isResult()) {
                            mGetCode.setClickable(true);
                            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                            Toast.makeText(mContext, R.string.phone_number_available, Toast.LENGTH_SHORT).show();
                        } else {
                            mGetCode.setClickable(false);
                            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                            Toast.makeText(mContext, R.string.phone_number_has_been_registered, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case SEND_CODE:
                    SendCodeResponse scrres = (SendCodeResponse) result;
                    if (scrres.getCode() == 200) {
                        NToast.shortToast(mContext, R.string.messge_send);
                    } else if (scrres.getCode() == 5000) {
                        NToast.shortToast(mContext, R.string.message_frequency);
                    }
                    break;

                case VERIFY_CODE:
                    VerifyCodeResponse vcres = (VerifyCodeResponse) result;
                    switch (vcres.getCode()) {
                        case 200:
                            mCodeToken = vcres.getResult().getVerification_token();
                            if (!TextUtils.isEmpty(mCodeToken)) {
                                request(REGISTER);
                            } else {
                                NToast.shortToast(mContext, "code token is null");
                                LoadDialog.dismiss(mContext);
                            }
                            break;
                        case 1000:
                            //验证码错误
                            NToast.shortToast(mContext, R.string.verification_code_error);
                            LoadDialog.dismiss(mContext);
                            break;
                        case 2000:
                            //验证码过期
                            NToast.shortToast(mContext, R.string.captcha_overdue);
                            LoadDialog.dismiss(mContext);
                            break;
                    }
                    break;

                case REGISTER:
                    RegisterResponse rres = (RegisterResponse) result;
                    switch (rres.getCode()) {
                        case 200:
                            LoadDialog.dismiss(mContext);
                            NToast.shortToast(mContext, R.string.register_success);
                            Intent data = new Intent();
                            data.putExtra("phone", mPhone);
                            data.putExtra("password", mPassword);
                            data.putExtra("nickname", mNickName);
                            data.putExtra("id", rres.getResult().getId());
                            setResult(REGISTER_BACK, data);
                            this.finish();
                            break;
                        case 400:
                            // 错误的请求
                            break;
                        case 404:
                            //token 不存在
                            break;
                        case 500:
                            //应用服务端内部错误
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case CHECK_PHONE:
                Toast.makeText(mContext, "手机号可用请求失败", Toast.LENGTH_SHORT).show();
                break;
            case SEND_CODE:
                NToast.shortToast(mContext, "获取验证码请求失败");
                break;
            case VERIFY_CODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "验证码是否可用请求失败");
                break;
            case REGISTER:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "注册请求失败");
                break;
        }
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.reg_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.reg_getcode:
                if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
                    NToast.longToast(mContext, R.string.phone_number_is_null);
                } else {
                    isRequestCode = true;

//                    request(SEND_CODE);
                    getCode(mPhone);
                }
                break;
            case R.id.reg_button:
                mPhone = mPhoneEdit.getText().toString().trim();
                mCode = mCodeEdit.getText().toString().trim();
                mNickName = mNickEdit.getText().toString().trim();
                mPassword = mPasswordEdit.getText().toString().trim();


                if (TextUtils.isEmpty(mNickName)) {
                    NToast.shortToast(mContext, getString(R.string.name_is_null));
                    mNickEdit.setShakeAnimation();
                    return;
                }
                if (mNickName.contains(" ")) {
                    NToast.shortToast(mContext, getString(R.string.name_contain_spaces));
                    mNickEdit.setShakeAnimation();
                    return;
                }

                if (TextUtils.isEmpty(mPhone)) {
                    NToast.shortToast(mContext, getString(R.string.phone_number_is_null));
                    mPhoneEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(mCode)) {
                    NToast.shortToast(mContext, getString(R.string.code_is_null));
                    mCodeEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    NToast.shortToast(mContext, getString(R.string.password_is_null));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                if (mPassword.contains(" ")) {
                    NToast.shortToast(mContext, getString(R.string.password_cannot_contain_spaces));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }

                if (!isRequestCode) {
                    NToast.shortToast(mContext, getString(R.string.not_send_code));
                    return;
                }

                LoadDialog.show(mContext);
                //取消调用他们后台的接口  改用自己后台的接口
//                request(VERIFY_CODE, true);
                confirmRegister(mPhone, mPassword, mCode, mNickName);

                break;
        }
    }

    private void confirmRegister(String mobile, String password, String code, String name) {
        SetPassBean setPassBean = new SetPassBean();
        setPassBean.setM("member");
        setPassBean.setK("reg_mobile");
        setPassBean.setRid(String.valueOf(System.currentTimeMillis()));
        SetPassBean.VBean vBean = new SetPassBean.VBean();
        vBean.setMobile(mobile);
        vBean.setPassword(MD5.getStringMD5(password));
        vBean.setName(name);
        vBean.setSms_code(code);
        setPassBean.setV(vBean);
        String msg = new Gson().toJson(setPassBean);
        SocketClient.getInstance().sendMessage(msg, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("test", "json" + json);
                LoginReturnBean setPassReturnBean = new Gson().fromJson(json, LoginReturnBean.class);
                Message message = new Message();
                message.obj = setPassReturnBean;
                handlerComplete.sendMessage(message);


            }
        });


    }

    private Handler handlerComplete = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginReturnBean setPassReturnBean = (LoginReturnBean) msg.obj;
            Toast.makeText(getApplication(), setPassReturnBean.getDesc(), Toast.LENGTH_LONG).show();
            if (setPassReturnBean.getV().equals("ok")) {
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.register_success);
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("phone", mPhone);
                intent.putExtra("password", mPassword);
                intent.putExtra("nickname", mNickName);
//                data.putExtra("id", rres.getResult().getId());
                setResult(REGISTER_BACK, intent);
                finish();
            }
        }
    };

    /**
     * 获取注册的验证码
     *
     * @param phone
     */
    private void getCode(String phone) {
        final GetCodeBean getCodeBean = new GetCodeBean();
        getCodeBean.setM("common");
        getCodeBean.setK("get_smscode_reg");
        getCodeBean.setRid(String.valueOf(System.currentTimeMillis()));
        GetCodeBean.VBean vBean = new GetCodeBean.VBean();
        vBean.setMobile(phone);
        getCodeBean.setV(vBean);
        String json = new Gson().toJson(getCodeBean);
        SocketClient.getInstance().sendMessage(json, new CallBackJson() {
            @Override
            public void returnJson(String json) {
                Log.e("test", "json" + json);
                GetCodeReturnBean getCodeReturnBean = new Gson().fromJson(json, GetCodeReturnBean.class);
                Message message = new Message();
                message.obj = getCodeReturnBean;
                handler.sendMessage(message);

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GetCodeReturnBean getCodeReturnBean = (GetCodeReturnBean) msg.obj;
            Toast.makeText(getApplication(), getCodeReturnBean.getDesc(), Toast.LENGTH_LONG).show();
            if (getCodeReturnBean.getV().equals("ok")) {
                DownTimer downTimer = new DownTimer();
                downTimer.setListener(RegisterActivity.this);
                downTimer.startDown(60 * 1000);
            }
        }
    };
    boolean isBright = true;

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
        mGetCode.setClickable(false);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
        isBright = false;
    }

    @Override
    public void onFinish() {
        mGetCode.setText(R.string.get_code);
        mGetCode.setClickable(true);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
        isBright = true;
    }

}
