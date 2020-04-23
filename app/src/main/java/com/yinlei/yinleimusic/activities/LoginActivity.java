package com.yinlei.yinleimusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.utils.UserUtils;
import com.yinlei.yinleimusic.views.InputView;

/**
 *  NavigationBar/Toolbar
 */
public class LoginActivity extends BaseActivity {

    private InputView mInputPhone, mInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    /**
     * 初始化View
     */
    private void initView(){
        initNavBar(false, "登录", false);
        mInputPhone = fd(R.id.input_phone);
        mInputPassword = fd(R.id.input_password);

    }

    /**
     * 注册点击事件
     * @param view
     */
    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 登录点击事件
     * @param view
     */
    public void onCmmitClick(View view) {
        String phone = mInputPhone.getInputStr();
        String password = mInputPassword.getInputStr();

        //验证用户输入是否合法
        if(!UserUtils.validateLogin(this, phone, password)){
            return;
        }
        //跳转到应用主页
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
