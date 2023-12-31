package com.llxk.usercenterdemo.constant;

/**
 * ClassName: SystemConstants
 * Package: com.llxk.usercenterdemo.utils
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 系统常量
 * @Date 2023/8/16 16:59
 */
public class SystemConstants {
    /**
     * 用户账户长度最小值
     */
    public static final int MIN_USER_ACCOUNT_LENGTH = 4;
    /**
     * 用户密码长度最小值
     */
    public static final int MIN_USER_PASSWORD_LENGTH = 8;
    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "LLXK";
    /**
     * 用户登录态键
     */
    public static final String USER_LOGIN_STATE = "userLoginState";

    //-----权限---------
    /**
     * 管理员权限
     */
    public static final int ADMIN_ROLE = 1;

    /**
     * 默认权限
     */
    public static final int DEFAULT_ROLE = 0;



}
