package com.llxk.usercenterdemo.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: UserRegisterRequest
 * Package: com.llxk.usercenterdemo.model.domain.request
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 用户注册请求体
 * @Date 2023/8/16 20:41
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 2045642837953226083L;
    /**
     * 用户账户
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 校验密码
     */
    private String checkPassword;
    /**
     * 编号
     */
    private String planetCode;
}
