package com.ksk.obama.model;

/**
 * Created by djy on 2017/6/21.
 */

/**
 * "errCode": "0",
 * "errMsg": "支付成功",
 * "out_trade_no": "KM170621105707109169",
 * "trade_status": "PAY",
 * "input_charset": "UTF-8",
 * "cashier_trade_no": "10001933942017062100000018",
 * "operator": "匿名用户",
 * "pay_type": "1001",
 * "pay_info": "支付成功",
 * "thirdDiscount": 0,
 * "thirdMDiscount": 0
 */
public class WangPosPAY {
    private String errCode;
    private String errMsg;
    private String out_trade_no;
    private String trade_status;
    private String input_charset;
    private String cashier_trade_no;
    private String operator;
    private String pay_type;
    private String pay_info;
    private String thirdDiscount;
    private String thirdMDiscount;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getCashier_trade_no() {
        return cashier_trade_no;
    }

    public void setCashier_trade_no(String cashier_trade_no) {
        this.cashier_trade_no = cashier_trade_no;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_info() {
        return pay_info;
    }

    public void setPay_info(String pay_info) {
        this.pay_info = pay_info;
    }

    public String getThirdDiscount() {
        return thirdDiscount;
    }

    public void setThirdDiscount(String thirdDiscount) {
        this.thirdDiscount = thirdDiscount;
    }

    public String getThirdMDiscount() {
        return thirdMDiscount;
    }

    public void setThirdMDiscount(String thirdMDiscount) {
        this.thirdMDiscount = thirdMDiscount;
    }
}
