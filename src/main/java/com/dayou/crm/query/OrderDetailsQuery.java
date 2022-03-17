package com.dayou.crm.query;


import com.dayou.crm.base.BaseQuery;


public class OrderDetailsQuery extends BaseQuery {

    private Integer orderId; // 订单ID

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
