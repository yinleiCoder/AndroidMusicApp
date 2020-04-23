package com.yinlei.yinleimusic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.yinlei.yinleimusic.R;
import com.yinlei.yinleimusic.activities.LoginActivity;
import com.yinlei.yinleimusic.helps.RealmHelp;
import com.yinlei.yinleimusic.helps.UserHelper;
import com.yinlei.yinleimusic.models.UserModel;

import java.util.List;

import io.realm.annotations.PrimaryKey;

/**
 * 工具类
 */
public class UserUtils {
    /**
     * 验证登录用户
     */
    public static boolean validateLogin(Context context, String phone, String password){
//        RegexUtils.isMobileSimple(phone);//简单的
//        RegexUtils.isMobileExact(phone);//精确的

        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context, "无效手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        /**
         * 1. 用户当前手机号是否已经被注册了
         * 2. 用户输入的手机号和密码是否匹配
         */
        if(!UserUtils.userExistFromPhone(phone)){
            Toast.makeText(context, "当前手机号未注册", Toast.LENGTH_SHORT).show();
            return false;
        }

        RealmHelp realmHelp = new RealmHelp();
        boolean result = realmHelp.validateUser(phone, EncryptUtils.encryptMD5ToString(password));
        if(!result){
            Toast.makeText(context, "手机号/密码不正确！", Toast.LENGTH_SHORT).show();
            return false;
        }

        //保存用户登录标记
        boolean isSave = SPUtils.saveUser(context, phone);
        if(!isSave){
            Toast.makeText(context, "系统错误请稍后重试!", Toast.LENGTH_SHORT).show();
            return false;
        }
        //保存用户标记，在全局单例类中
        UserHelper.getInstance().setPhone(phone);


        //保存音乐元数据
        realmHelp.setMusicSource(context);
        realmHelp.close();

        return true;
    }

    /**
     * 退出登录
     */
    public static void logout(Context context){
        //删除SharedPrefreence保存的用户标记
        boolean isRemove = SPUtils.removeUser(context);
        if(!isRemove){
            Toast.makeText(context, "系统错误请稍后重试!", Toast.LENGTH_SHORT).show();
            return;
        }

        //删除数据源
        RealmHelp realmHelp = new RealmHelp();
        realmHelp.removeMusicSource();
        realmHelp.close();

        Intent intent = new Intent(context, LoginActivity.class);
        //添加intent标识符：清理task栈并新生成一个task栈
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        //定义跳转动画,需要在startActivity后调用
        ((Activity) context).overridePendingTransition(R.anim.open_enter,R.anim.open_exit);
    }

    /**
     * 注册用户
     * @param context
     * @param phone
     * @param password
     * @param confirmPassword
     */
    public static boolean registerUser(Context context, String phone, String password, String confirmPassword){
        if(!RegexUtils.isMobileExact(phone)){
            Toast.makeText(context, "无效手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(StringUtils.isEmpty(password) || !password.equals(confirmPassword)){
            Toast.makeText(context, "请确认密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        //用户输入的手机号是否已经被注册了。
        /**
         * 1. 通过realm获取到当前已经注册的所有用户
         * 2. 根据用户输入的手机号匹配查询的所有用户，如果可以匹配则证明该手机号已经被注册了。否则还未注册。
         */
        if(UserUtils.userExistFromPhone(phone)){
            Toast.makeText(context, "该手机号已存在", Toast.LENGTH_SHORT).show();
            return false;
        }


        UserModel userModel = new UserModel();
        userModel.setPhone(phone);
        userModel.setPassword(EncryptUtils.encryptMD5ToString(password));

        saveUser(userModel);
        return true;
    }

    /**
     * 保存用户到数据库
     * @param userModel
     */
    public static void saveUser(UserModel userModel){
        RealmHelp realmHelp = new RealmHelp();
        realmHelp.saveUser(userModel);
        realmHelp.close();
    }


    /**
     * 根据手机号判断用户是否存在
     */
    public static boolean userExistFromPhone(String phone){
        boolean result =  false;
        RealmHelp realmHelp = new RealmHelp();
        List<UserModel> allUsers = realmHelp.getAllUser();
        for (UserModel userModel: allUsers) {
            if(userModel.getPhone().equals(phone)){
                //当前手机号已经存在于数据库中了。
                result = true;
                break;
            }
        }
        realmHelp.close();
        return result;
    }


    /***
     * 验证是否存在登录用户
     */
    public static boolean validateUserLogin(Context context){
        return SPUtils.isLoginUser(context);
    }

    /**
     * 修改密码
     * 1. 数据验证
     *   原密码是否输入、新密码是否输入、 新密码和确定密码是是否i相同、 原密码输入是否正确
     * 2. 利用Realm模型自动更新的特性完成密码的修改。
     */
    public static boolean changePassword(Context context,String oldPassword, String password, String passwordConfirm){
        if(TextUtils.isEmpty(oldPassword)){
            Toast.makeText(context, "请输入原密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(password) || !password.equals(passwordConfirm)){
            Toast.makeText(context, "请确认密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        ///验证原密码是否正确
        RealmHelp realmHelp = new RealmHelp();
        UserModel userModel = realmHelp.getUser();
        if(!EncryptUtils.encryptMD5ToString(oldPassword).equals(userModel.getPassword())){
            Toast.makeText(context, "原密码不正确", Toast.LENGTH_SHORT).show();
            return false;
        }
        realmHelp.changePassword(EncryptUtils.encryptMD5ToString(password));
        realmHelp.close();
        return true;
    }
}
