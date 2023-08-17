package com.llxk.usercenterdemo.utils;

import com.llxk.usercenterdemo.common.BaseResponse;
import com.llxk.usercenterdemo.common.ErrorCode;

/**
 * ClassName: ResultUtils
 * Package: com.llxk.usercenterdemo.utils
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 返回工具类
 * @Date 2023/8/17 23:40
 */
public class ResultUtils {
    /**
     * 成功
     * @param data 数据
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(data, ErrorCode.SUCCESS);
    }

    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message, String description){
        return new BaseResponse(errorCode.getCode(), message, description);
    }

    /**
     * 失败
     * @param code
     * @param message
     * @param description
     * @return
     */
    public static BaseResponse error(int code, String message, String description){
        return new BaseResponse(code, message, description);
    }

    /**
     * 失败
     * @param errorCode
     * @param message
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode,String message){
        return new BaseResponse(errorCode.getCode(), message);
    }

}
