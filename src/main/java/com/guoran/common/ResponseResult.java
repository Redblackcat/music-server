package com.guoran.common;

import com.guoran.common.enums.AppHttpCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description 统一响应格式
 * @author chenzixin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> implements Serializable {
    //响应码
    private Integer code;
    //响应信息
    private String msg;
    //响应数据
    private T data;

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult success() {
        return new ResponseResult(AppHttpCodeEnum.SUCCESS.getCode(), AppHttpCodeEnum.SUCCESS.getMsg());
    }

    public ResponseResult success(String msg) {
        return new ResponseResult(AppHttpCodeEnum.SUCCESS.getCode(), msg);
    }

    public ResponseResult success(String msg, T data) {
        return new ResponseResult(AppHttpCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public ResponseResult success(T data) {
        return new ResponseResult(AppHttpCodeEnum.SUCCESS.getCode(), AppHttpCodeEnum.SUCCESS.getMsg(), data);
    }

    public ResponseResult error(String msg) {
        return new ResponseResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), msg, null);
    }

    public ResponseResult error(Integer code, String msg) {
        return new ResponseResult(code, msg, null);
    }

    public ResponseResult error() {
        return new ResponseResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), AppHttpCodeEnum.SYSTEM_ERROR.getMsg(), null);
    }


}