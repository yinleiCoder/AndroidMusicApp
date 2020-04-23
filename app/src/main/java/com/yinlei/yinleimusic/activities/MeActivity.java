package com.yinlei.yinleimusic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.helps.UserHelper;
import com.yinlei.yinleimusic.utils.UserUtils;

public class MeActivity extends BaseActivity {

    private TextView tvUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        initView();
    }

    private void initView() {
        initNavBar(true, "个人中心", false);
        tvUser = fd(R.id.tv_user);
        tvUser.setText("用户名："+ UserHelper.getInstance().getPhone());
    }

    /**
     * 修改密码点击事件
     * @param view
     */
    public void onChangeClick(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    /**
     * 退出登录点击事件
     * @param view
     */
    public void onlogoutClick(View view) {
        UserUtils.logout(this);
    }
}
