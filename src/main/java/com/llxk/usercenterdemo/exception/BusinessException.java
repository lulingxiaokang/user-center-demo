package com.llxk.usercenterdemo.exception;

import com.llxk.usercenterdemo.common.ErrorCode;

/**
 * ClassName: BusinessException
 * Package: com.llxk.usercenterdemo.exception
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 自定义异常类
 * @Date 2023/8/18 0:25
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description){
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
