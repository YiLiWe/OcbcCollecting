package com.example.ocbccollecting.hook.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BankAccountBean {
    @JSONField(name = "accountBalances")
    private List<?> accountBalances;
    @JSONField(name = "accountCurrency")
    private String accountCurrency;
    @JSONField(name = "accountId")
    private String accountId;
    @JSONField(name = "accountName")
    private String accountName;
    @JSONField(name = "accountNo")
    private String accountNo;
    @JSONField(name = "accountType")
    private String accountType;
    @JSONField(name = "assetSummaries")
    private List<AssetSummariesDTO> assetSummaries;
    @JSONField(name = "assets")
    private List<?> assets;
    @JSONField(name = "availableBalance")
    private Integer availableBalance;
    @JSONField(name = "availableTransactions")
    private List<?> availableTransactions;
    @JSONField(name = "bank")
    private BankDTO bank;
    @JSONField(name = "bankCode")
    private String bankCode;
    @JSONField(name = "bankName")
    private String bankName;
    @JSONField(name = "branchCode")
    private String branchCode;
    @JSONField(name = "cardNumber")
    private String cardNumber;
    @JSONField(name = "cardType")
    private String cardType;
    @JSONField(name = "cif")
    private String cif;
    @JSONField(name = "createdBy")
    private String createdBy;
    @JSONField(name = "currencyOption")
    private List<?> currencyOption;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "dynamicAccountId")
    private String dynamicAccountId;
    @JSONField(name = "effectiveDate")
    private String effectiveDate;
    @JSONField(name = "feature")
    private FeatureDTO feature;
    @JSONField(name = "identifier")
    private String identifier;
    @JSONField(name = "imageUrl")
    private String imageUrl;
    @JSONField(name = "info")
    private InfoDTO info;
    @JSONField(name = "interestRate")
    private Integer interestRate;
    @JSONField(name = "isActive")
    private Boolean isActive;
    @JSONField(name = "isDisbursement")
    private Boolean isDisbursement;
    @JSONField(name = "isDormant")
    private Boolean isDormant;
    @JSONField(name = "isJuniorAsset")
    private Boolean isJuniorAsset;
    @JSONField(name = "isNeedTandaJuniorAccess")
    private Boolean isNeedTandaJuniorAccess;
    @JSONField(name = "isNeedToSaveAsDefaultAccount")
    private Boolean isNeedToSaveAsDefaultAccount;
    @JSONField(name = "isSharia")
    private Boolean isSharia;
    @JSONField(name = "labelDebitAccount")
    private String labelDebitAccount;
    @JSONField(name = "lockBalance")
    private Integer lockBalance;
    @JSONField(name = "loyaltyId")
    private String loyaltyId;
    @JSONField(name = "maturityDateInIso")
    private String maturityDateInIso;
    @JSONField(name = "maximumBalance")
    private Integer maximumBalance;
    @JSONField(name = "mcBit")
    private String mcBit;
    @JSONField(name = "minimumBalance")
    private Integer minimumBalance;
    @JSONField(name = "minimumInstallmentAmount")
    private Integer minimumInstallmentAmount;
    @JSONField(name = "monthlyPayment")
    private Integer monthlyPayment;
    @JSONField(name = "nccValue")
    private String nccValue;
    @JSONField(name = "productCode")
    private String productCode;
    @JSONField(name = "productDescriptions")
    private List<?> productDescriptions;
    @JSONField(name = "productName")
    private String productName;
    @JSONField(name = "proxy")
    private String proxy;
    @JSONField(name = "qrCode")
    private String qrCode;
    @JSONField(name = "shariaAsset")
    private ShariaAssetDTO shariaAsset;
    @JSONField(name = "swiftCode")
    private String swiftCode;
    @JSONField(name = "tenor")
    private Integer tenor;
    @JSONField(name = "topCardStatus")
    private TopCardStatusDTO topCardStatus;
    @JSONField(name = "totalAssetInIdr")
    private TotalAssetInIdrDTO totalAssetInIdr;
    @JSONField(name = "userCode")
    private String userCode;
    @JSONField(name = "userName")
    private String userName;

    @NoArgsConstructor
    @Data
    public static class BankDTO {
        @JSONField(name = "accountNo")
        private String accountNo;
        @JSONField(name = "address")
        private String address;
        @JSONField(name = "availableTransaction")
        private List<?> availableTransaction;
        @JSONField(name = "bankId")
        private String bankId;
        @JSONField(name = "bankName")
        private String bankName;
        @JSONField(name = "bankType")
        private String bankType;
        @JSONField(name = "city")
        private CityDTO city;
        @JSONField(name = "country")
        private CountryDTO country;
        @JSONField(name = "description")
        private String description;
        @JSONField(name = "isEnabled")
        private Boolean isEnabled;
        @JSONField(name = "nationalClearingCode")
        private NationalClearingCodeDTO nationalClearingCode;

        @NoArgsConstructor
        @Data
        public static class CityDTO {
            @JSONField(name = "code")
            private String code;
            @JSONField(name = "description")
            private String description;
            @JSONField(name = "detail")
            private String detail;
            @JSONField(name = "disclaimer")
            private String disclaimer;
            @JSONField(name = "identifier")
            private String identifier;
            @JSONField(name = "imageResource")
            private Integer imageResource;
            @JSONField(name = "imageUrl")
            private String imageUrl;
            @JSONField(name = "isAvailable")
            private Boolean isAvailable;
            @JSONField(name = "isNew")
            private Boolean isNew;
            @JSONField(name = "isSelected")
            private Boolean isSelected;
            @JSONField(name = "nextPage")
            private Integer nextPage;
            @JSONField(name = "score")
            private Integer score;
            @JSONField(name = "title")
            private String title;
            @JSONField(name = "type")
            private String type;
        }

        @NoArgsConstructor
        @Data
        public static class CountryDTO {
            @JSONField(name = "code")
            private String code;
            @JSONField(name = "description")
            private String description;
            @JSONField(name = "detail")
            private String detail;
            @JSONField(name = "disclaimer")
            private String disclaimer;
            @JSONField(name = "identifier")
            private String identifier;
            @JSONField(name = "imageResource")
            private Integer imageResource;
            @JSONField(name = "imageUrl")
            private String imageUrl;
            @JSONField(name = "isAvailable")
            private Boolean isAvailable;
            @JSONField(name = "isNew")
            private Boolean isNew;
            @JSONField(name = "isSelected")
            private Boolean isSelected;
            @JSONField(name = "nextPage")
            private Integer nextPage;
            @JSONField(name = "score")
            private Integer score;
            @JSONField(name = "title")
            private String title;
            @JSONField(name = "type")
            private String type;
        }

        @NoArgsConstructor
        @Data
        public static class NationalClearingCodeDTO {
            @JSONField(name = "prefix")
            private String prefix;
            @JSONField(name = "validation")
            private ValidationDTO validation;

            @NoArgsConstructor
            @Data
            public static class ValidationDTO {
                @JSONField(name = "maximumAmount")
                private Integer maximumAmount;
                @JSONField(name = "maximumLength")
                private Integer maximumLength;
                @JSONField(name = "minimumAmount")
                private Integer minimumAmount;
                @JSONField(name = "minimumLength")
                private Integer minimumLength;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class FeatureDTO {
        @JSONField(name = "isEligibleAddCurrency")
        private Boolean isEligibleAddCurrency;
        @JSONField(name = "isHiddenLifeGoal")
        private Boolean isHiddenLifeGoal;
        @JSONField(name = "menus")
        private List<?> menus;
    }

    @NoArgsConstructor
    @Data
    public static class InfoDTO {
        @JSONField(name = "backgroundColor")
        private String backgroundColor;
        @JSONField(name = "description")
        private String description;
        @JSONField(name = "descriptionColor")
        private String descriptionColor;
        @JSONField(name = "detail")
        private DetailDTO detail;
        @JSONField(name = "faqType")
        private String faqType;
        @JSONField(name = "iconResource")
        private Integer iconResource;
        @JSONField(name = "iconUrl")
        private String iconUrl;
        @JSONField(name = "title")
        private String title;
        @JSONField(name = "titleColor")
        private String titleColor;
        @JSONField(name = "url")
        private String url;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @JSONField(name = "action")
            private String action;
            @JSONField(name = "isEnabled")
            private Boolean isEnabled;
            @JSONField(name = "isLineVisible")
            private Boolean isLineVisible;
            @JSONField(name = "isVisible")
            private Boolean isVisible;
            @JSONField(name = "note")
            private String note;
            @JSONField(name = "title")
            private String title;
            @JSONField(name = "value")
            private String value;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ShariaAssetDTO {
        @JSONField(name = "isCanUpdateZakat")
        private Boolean isCanUpdateZakat;
        @JSONField(name = "isSharia")
        private Boolean isSharia;
        @JSONField(name = "isUsingZakat")
        private Boolean isUsingZakat;
        @JSONField(name = "zakatPercentage")
        private Integer zakatPercentage;
    }

    @NoArgsConstructor
    @Data
    public static class TopCardStatusDTO {
        @JSONField(name = "backgroundColorInHex")
        private String backgroundColorInHex;
        @JSONField(name = "iconResource")
        private Integer iconResource;
        @JSONField(name = "textColorInHex")
        private String textColorInHex;
        @JSONField(name = "title")
        private String title;
    }

    @NoArgsConstructor
    @Data
    public static class TotalAssetInIdrDTO {
        @JSONField(name = "accountBalances")
        private List<AccountBalancesDTO> accountBalances;
        @JSONField(name = "detail")
        private DetailDTO detail;
        @JSONField(name = "isEnable")
        private Boolean isEnable;
        @JSONField(name = "note")
        private String note;
        @JSONField(name = "title")
        private String title;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @JSONField(name = "action")
            private String action;
            @JSONField(name = "isEnabled")
            private Boolean isEnabled;
            @JSONField(name = "isLineVisible")
            private Boolean isLineVisible;
            @JSONField(name = "isVisible")
            private Boolean isVisible;
            @JSONField(name = "note")
            private String note;
            @JSONField(name = "title")
            private String title;
            @JSONField(name = "value")
            private String value;
        }

        @NoArgsConstructor
        @Data
        public static class AccountBalancesDTO {
            @JSONField(name = "balance")
            private Integer balance;
            @JSONField(name = "currency")
            private CurrencyDTO currency;
            @JSONField(name = "holdBalance")
            private Integer holdBalance;
            @JSONField(name = "isAvailable")
            private Boolean isAvailable;
            @JSONField(name = "percentage")
            private Integer percentage;
            @JSONField(name = "textColor")
            private String textColor;

            @NoArgsConstructor
            @Data
            public static class CurrencyDTO {
                @JSONField(name = "code")
                private String code;
                @JSONField(name = "isEnabled")
                private Boolean isEnabled;
                @JSONField(name = "isSelected")
                private Boolean isSelected;
                @JSONField(name = "nameEn")
                private String nameEn;
                @JSONField(name = "nameId")
                private String nameId;
            }
        }
    }

    @NoArgsConstructor
    @Data
    public static class AssetSummariesDTO {
        @JSONField(name = "accountBalances")
        private List<AccountBalancesDTO> accountBalances;
        @JSONField(name = "detail")
        private DetailDTO detail;
        @JSONField(name = "isEnable")
        private Boolean isEnable;
        @JSONField(name = "note")
        private String note;
        @JSONField(name = "title")
        private String title;

        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            @JSONField(name = "action")
            private String action;
            @JSONField(name = "isEnabled")
            private Boolean isEnabled;
            @JSONField(name = "isLineVisible")
            private Boolean isLineVisible;
            @JSONField(name = "isVisible")
            private Boolean isVisible;
            @JSONField(name = "note")
            private String note;
            @JSONField(name = "title")
            private String title;
            @JSONField(name = "value")
            private String value;
        }

        @NoArgsConstructor
        @Data
        public static class AccountBalancesDTO {
            @JSONField(name = "balance")
            private long balance;
            @JSONField(name = "currency")
            private CurrencyDTO currency;
            @JSONField(name = "holdBalance")
            private long holdBalance;
            @JSONField(name = "isAvailable")
            private Boolean isAvailable;
            @JSONField(name = "percentage")
            private Integer percentage;
            @JSONField(name = "textColor")
            private String textColor;

            @NoArgsConstructor
            @Data
            public static class CurrencyDTO {
                @JSONField(name = "code")
                private String code;
                @JSONField(name = "isEnabled")
                private Boolean isEnabled;
                @JSONField(name = "isSelected")
                private Boolean isSelected;
                @JSONField(name = "nameEn")
                private String nameEn;
                @JSONField(name = "nameId")
                private String nameId;
            }
        }
    }
}
