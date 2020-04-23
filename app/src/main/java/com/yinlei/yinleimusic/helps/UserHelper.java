package com.yinlei.yinleimusic.helps;

/**
 * 用户自动登录帮助类
 * 1.用户登录:
 *   1.1 当用户登录时候，利用sharedpreferences保存登录用户的用户标记(phone)
 *   1.2 利用单例模式UserHelper保存用户登录信息
 *        1.2.1 用户登录后
 *        1.2.2 用户重新打开应用程序，检测sharedpreferences中是否存在用户登录标记，如果存在则未userhelp进行赋值，并且进入主页.不沉溺在则进入登录页
 * 2. 用户退出
 *   2.1 删除掉sharedpreferences保存的用户标记，退出到登录页面
 */
public class UserHelper {
    private static UserHelper instance;

    private UserHelper(){

    }

    public static UserHelper getInstance(){
        if(instance==null){
            synchronized (UserHelper.class){
                if (instance==null){
                    instance= new UserHelper();
                }
            }
        }
        return instance;
    }


    private String phone;//用户登录标记

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
