package com.example.ocbccollecting.eventbus.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageEvent {
    private int code;
}
// {"amount":20100,"bankName":"DANA","cardNumber":"0881022055218","loginPwd":"Device_OTG_1","money":true,"orderNo":"1949849238519152640","payPwd":"1001","payeeName":"0881022055218"}