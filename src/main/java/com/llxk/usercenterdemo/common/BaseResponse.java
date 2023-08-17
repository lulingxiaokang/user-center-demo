package com.llxk.usercenterdemo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: BaseResponse
 * Package: com.llxk.usercenterdemo.common
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 通用返回类
 * @Date 2023/8/17 23:33
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(int code, String message, String description) {
        this(code, null, message, description);
    }

    public BaseResponse(int code, String message) {
        this(code, null, message, "");
    }

    public BaseResponse(ErrorCode code){
        this(code.getCode(), null, code.getMessage(), code.getDescription());
    }

    public BaseResponse(T data, ErrorCode code){
        this(code.getCode(), data, code.getMessage(), code.getDescription());
    }

}
