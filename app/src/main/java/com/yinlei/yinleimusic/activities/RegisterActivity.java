package com.yinlei.yinleimusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.helps.RealmHelp;
import com.yinlei.yinleimusic.utils.UserUtils;
import com.yinlei.yinleimusic.views.InputView;

public class RegisterActivity extends BaseActivity {

    private InputView mInputPhone, mInputPassword, mInputPasswordConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView(){
        initNavBar(true, "成为我们的一员", false);

        mInputPhone = fd(R.id.input_phone);
        mInputPassword = fd(R.id.input_password);
        mInputPasswordConfirm = fd(R.id.input_password_confirm);
    }


    /**
     * 注册按钮点击事件
     * 1. 用户输入合法性验证
     *     输入的手机号是否合法
     *     用户是否输入了密码和确认密码,2次输入是否相同
     *     当前输入的手机号是否已经被注册。
     * 2. 保存用户输入的手机号和密码（md5加密密码）
     */
    public void onRegisterClick(View view){
        String phone = mInputPhone.getInputStr();
        String password = mInputPassword.getInputStr();
        String passwordConfirm = mInputPasswordConfirm.getInputStr();


        boolean result = UserUtils.registerUser(this,phone,password,passwordConfirm);
        if(!result){
            return;
        }

        onBackPressed();
    }
}
