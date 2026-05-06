package com.smartdine.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    //订单状�?1待付�?2待接�?3 已接�?4 派送中 5 已完�?6 已取�?7 退�?
    private Integer status;

    /**
     * getter和setter方法
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}