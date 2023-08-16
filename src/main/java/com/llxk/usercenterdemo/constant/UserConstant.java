package com.llxk.usercenterdemo.constant;

/**
 * ClassName: UserConstant
 * Package: com.llxk.usercenterdemo.constant
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 用户常量
 * @Date 2023/8/16 21:58
 */
public interface UserConstant {
    /**
     * 用户账户长度最小值
     */
    int MIN_USER_ACCOUNT_LENGTH = 4;
    /**
     * 用户密码长度最小值
     */
    int MIN_USER_PASSWORD_LENGTH = 8;
    /**
     * 盐值，混淆密码
     */
    String SALT = "LLXK";
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    //-----权限---------
    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;
}
