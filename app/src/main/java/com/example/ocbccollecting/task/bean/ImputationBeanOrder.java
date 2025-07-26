package com.example.ocbccollecting.task.bean;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImputationBeanOrder {
    @JSONField(name = "id")
    private Long id;
    @JSONField(name = "card")
    private String card;//收款卡号
    @JSONField(name = "bank")
    private String bank;//收款卡银行
    @JSONField(name = "payCard")
    private Integer payCard;//付款卡号
    @JSONField(name = "amount")
    private Long amount;//归集金额
}
