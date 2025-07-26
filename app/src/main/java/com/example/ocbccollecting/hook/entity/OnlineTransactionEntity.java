package com.example.ocbccollecting.hook.entity;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OnlineTransactionEntity {
    //卡号
    private String cardNumber;

    @JSONField(name = "histories")
    private List<HistoriesDTO> histories;
    @JSONField(name = "pagination")
    private PaginationDTO pagination;

    @NoArgsConstructor
    @Data
    public static class PaginationDTO {
        @JSONField(name = "currentPage")
        private Integer currentPage;
        @JSONField(name = "pageSize")
        private Integer pageSize;
        @JSONField(name = "totalRecord")
        private Integer totalRecord;
    }

    @NoArgsConstructor
    @Data
    public static class HistoriesDTO {
        @JSONField(name = "additionalDescription")
        private String additionalDescription;
        @JSONField(name = "amount")
        private Long amount;
        @JSONField(name = "balance")
        private Long balance;
        @JSONField(name = "sign")
        private String sign;
        @JSONField(name = "transactionCode")
        private String transactionCode;
        @JSONField(name = "transactionDateInIso")
        private String transactionDateInIso;
        @JSONField(name = "transactionDescription")
        private String transactionDescription;
        @JSONField(name = "valueDate")
        private String valueDate;
    }
}
