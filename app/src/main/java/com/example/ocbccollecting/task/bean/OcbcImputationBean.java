package com.example.ocbccollecting.task.bean;

import lombok.Data;

@Data
public class OcbcImputationBean {
    private String deviceCardNumber;
    private String cardNumber;
    private String bank;
    private Long triggerAmount;
    private Long leaveAmount;
    private long billId;

    public long getMoney() {
        return triggerAmount - leaveAmount;
    }
}
