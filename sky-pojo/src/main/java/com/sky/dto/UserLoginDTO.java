package com.sky.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
public class UserLoginDTO implements Serializable {

    private String code;

    /**
     * getter和setter方法
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}