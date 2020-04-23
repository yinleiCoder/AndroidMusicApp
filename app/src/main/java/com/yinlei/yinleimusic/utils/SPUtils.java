package com.yinlei.yinleimusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.yinlei.yinleimusic.constants.SPConstant;
import com.yinlei.yinleimusic.helps.UserHelper;

/**
 * SharedPreferences工具类
 */
public class SPUtils {

    /**
     * 用户登录时候，利用sharedpreferences保存登录用户的用户标记(phone)
     */
    public static boolean saveUser(Context context, String phone){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SPConstant.SP_NAME_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SPConstant.SP_KEY_PHONE, phone);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 验证是否存在已登录用户
     */
    public static boolean isLoginUser(Context context) {
        boolean result = false;

        SharedPreferences sharedPreferences  = context.getSharedPreferences(SPConstant.SP_NAME_USER, Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString(SPConstant.SP_KEY_PHONE, "");
        if(!TextUtils.isEmpty(phone)){
            result = true;
            UserHelper.getInstance().setPhone(phone);
        }
        return result;
    }

    /**
     * 删除用户标记
     */
    public static boolean removeUser(Context context){
        SharedPreferences sharedPreferences  = context.getSharedPreferences(SPConstant.SP_NAME_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SPConstant.SP_KEY_PHONE);
        boolean result = editor.commit();
        return result;
    }
}

