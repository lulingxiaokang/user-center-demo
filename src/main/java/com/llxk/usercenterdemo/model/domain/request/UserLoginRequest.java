package com.llxk.usercenterdemo.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserLoginRequest
 * Package: com.llxk.usercenterdemo.model.domain.request
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 用户登录请求体
 * @Date 2023/8/16 21:00
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 7555663196741746697L;
    /**
     * 用户账户
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
}
