package com.ksk.obama.model;

/**
 * Created by Administrator on 2016/10/13.
 */

public class ChangePerson {

    /**
     * result_code : SUCCESS
     * result_msg : 查询成功
     * result_data : {"createcardnum":0,"createcardmoney":0,"createshouldcardmoney":0,"createcardmoneyPayCash":0,"createcardmoneyPayBank":0,"createcardmoneyPaywechat":0,"createcardmoneyPayali":0,"createcard_integral":0,"cancelcardnum":0,"cancelcardmoney":0,"cancelshouldcardmoney":0,"cancelcardmoneyPayCash":0,"cancelcardmoneyPayBank":0,"cancelcardmoneyPaywechat":0,"cancelcardmoneyPayali":0,"rechargenum":0,"rechargemoney":0,"rechargeshouldcardmoney":0,"rechargePayCash":0,"rechargePayBank":0,"rechargePaywechat":0,"rechargePayali":0,"recharge_integral":0,"membercostnum":0,"membercostmoney":0,"membercostshouldcardmoney":0,"membercostPayCard":0,"membercostPayCash":0,"membercostPayBank":0,"membercostPaywechat":0,"membercostPayali":0,"membercost_integral":0,"nomembercostnum":0,"nomembercostmoney":0,"nomembershouldcostmoney":0,"nomembercostPayCash":0,"nomembercostPayBank":0,"nomembercostPaywechat":0,"nomembercostPayali":0,"cost_money":0,"cost_PayCash":0,"cost_PayBank":0,"cost_Paywechat":0,"cost_Payali":0,"buytimenum":0,"buytimemoney":0,"buytimeshouldmoney":0,"buytimePayCard":0,"buytimePayCash":0,"buytimePayBank":0,"buytimePaywechat":0,"buytimePayali":0,"buytime_integral":0,"give_integral":0,"deduct_integral":0,"gift_integral":0,"PayIntegral":0,"inc_Integral":0,"dec_Integral":0,"allnum":0,"allmoney":0,"allshouldmoney":0,"PayCash":0,"PayBank":0,"PayCard":0,"Paywechat":0,"Payali":0,"PayOther":0}
     */

    private String result_code;
    private String result_msg;
    /**
     * createcardnum : 0
     * createcardmoney : 0
     * createshouldcardmoney : 0
     * createcardmoneyPayCash : 0
     * createcardmoneyPayBank : 0
     * createcardmoneyPaywechat : 0
     * createcardmoneyPayali : 0
     * createcard_integral : 0
     * cancelcardnum : 0
     * cancelcardmoney : 0
     * cancelshouldcardmoney : 0
     * cancelcardmoneyPayCash : 0
     * cancelcardmoneyPayBank : 0
     * cancelcardmoneyPaywechat : 0
     * cancelcardmoneyPayali : 0
     * rechargenum : 0
     * rechargemoney : 0
     * rechargeshouldcardmoney : 0
     * rechargePayCash : 0
     * rechargePayBank : 0
     * rechargePaywechat : 0
     * rechargePayali : 0
     * recharge_integral : 0
     * membercostnum : 0
     * membercostmoney : 0
     * membercostshouldcardmoney : 0
     * membercostPayCard : 0
     * membercostPayCash : 0
     * membercostPayBank : 0
     * membercostPaywechat : 0
     * membercostPayali : 0
     * membercost_integral : 0
     * nomembercostnum : 0
     * nomembercostmoney : 0
     * nomembershouldcostmoney : 0
     * nomembercostPayCash : 0
     * nomembercostPayBank : 0
     * nomembercostPaywechat : 0
     * nomembercostPayali : 0
     * cost_money : 0
     * cost_PayCash : 0
     * cost_PayBank : 0
     * cost_Paywechat : 0
     * cost_Payali : 0
     * buytimenum : 0
     * buytimemoney : 0
     * buytimeshouldmoney : 0
     * buytimePayCard : 0
     * buytimePayCash : 0
     * buytimePayBank : 0
     * buytimePaywechat : 0
     * buytimePayali : 0
     * buytime_integral : 0
     * give_integral : 0
     * deduct_integral : 0
     * gift_integral : 0
     * PayIntegral : 0
     * inc_Integral : 0
     * dec_Integral : 0
     * allnum : 0
     * allmoney : 0
     * allshouldmoney : 0
     * PayCash : 0
     * PayBank : 0
     * PayCard : 0
     * Paywechat : 0
     * Payali : 0
     * PayOther : 0
     */

    private ResultDataBean result_data;

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public static class ResultDataBean {
        private String createcardnum;
        private String createcardmoney;
        private String createshouldcardmoney;
        private String createcardmoneyPayCash;
        private String createcardmoneyPayBank;
        private String createcardmoneyPaywechat;
        private String createcardmoneyPayali;
        private String createcard_integral;
        private String cancelcardnum;
        private String cancelcardmoney;
        private String cancelshouldcardmoney;
        private String cancelcardmoneyPayCash;
        private String cancelcardmoneyPayBank;
        private String cancelcardmoneyPaywechat;
        private String cancelcardmoneyPayali;
        private String rechargenum;
        private String rechargemoney;
        private String rechargeshouldcardmoney;
        private String rechargePayCash;
        private String rechargePayBank;
        private String rechargePaywechat;
        private String rechargePayali;
        private String recharge_integral;
        private String membercostnum;
        private String membercostmoney;
        private String membercostshouldcardmoney;
        private String membercostPayCard;
        private String membercostPayCash;
        private String membercostPayBank;
        private String membercostPaywechat;
        private String membercostPayali;
        private String membercost_integral;
        private String nomembercostnum;
        private String nomembercostmoney;
        private String nomembershouldcostmoney;
        private String nomembercostPayCash;
        private String nomembercostPayBank;
        private String nomembercostPaywechat;
        private String nomembercostPayali;
        private String cost_money;
        private String cost_PayCash;
        private String cost_PayBank;
        private String cost_Paywechat;
        private String cost_Payali;
        private String buytimenum;
        private String buytimemoney;
        private String buytimeshouldmoney;
        private String buytimePayCard;
        private String buytimePayCash;
        private String buytimePayBank;
        private String buytimePaywechat;
        private String buytimePayali;
        private String buytime_integral;
        private String give_integral;
        private String deduct_integral;
        private String gift_integral;
        private String PayIntegral;
        private String inc_Integral;
        private String dec_Integral;
        private String allnum;
        private String allmoney;
        private String allshouldmoney;
        private String PayCash;
        private String PayBank;
        private String PayCard;
        private String Paywechat;
        private String Payali;
        private String PayOther;
        private String allgetmoney;
        private String all_Integral;

        public String getAll_Integral() {
            return all_Integral;
        }

        public void setAll_Integral(String all_Integral) {
            this.all_Integral = all_Integral;
        }

        public String getAllgetmoney() {
            return allgetmoney;
        }

        public void setAllgetmoney(String allgetmoney) {
            this.allgetmoney = allgetmoney;
        }

        public String getCreatecardnum() {
            return createcardnum;
        }

        public void setCreatecardnum(String createcardnum) {
            this.createcardnum = createcardnum;
        }

        public String getCreatecardmoney() {
            return createcardmoney;
        }

        public void setCreatecardmoney(String createcardmoney) {
            this.createcardmoney = createcardmoney;
        }

        public String getCreateshouldcardmoney() {
            return createshouldcardmoney;
        }

        public void setCreateshouldcardmoney(String createshouldcardmoney) {
            this.createshouldcardmoney = createshouldcardmoney;
        }

        public String getCreatecardmoneyPayCash() {
            return createcardmoneyPayCash;
        }

        public void setCreatecardmoneyPayCash(String createcardmoneyPayCash) {
            this.createcardmoneyPayCash = createcardmoneyPayCash;
        }

        public String getCreatecardmoneyPayBank() {
            return createcardmoneyPayBank;
        }

        public void setCreatecardmoneyPayBank(String createcardmoneyPayBank) {
            this.createcardmoneyPayBank = createcardmoneyPayBank;
        }

        public String getCreatecardmoneyPaywechat() {
            return createcardmoneyPaywechat;
        }

        public void setCreatecardmoneyPaywechat(String createcardmoneyPaywechat) {
            this.createcardmoneyPaywechat = createcardmoneyPaywechat;
        }

        public String getCreatecardmoneyPayali() {
            return createcardmoneyPayali;
        }

        public void setCreatecardmoneyPayali(String createcardmoneyPayali) {
            this.createcardmoneyPayali = createcardmoneyPayali;
        }

        public String getCreatecard_integral() {
            return createcard_integral;
        }

        public void setCreatecard_integral(String createcard_integral) {
            this.createcard_integral = createcard_integral;
        }

        public String getCancelcardnum() {
            return cancelcardnum;
        }

        public void setCancelcardnum(String cancelcardnum) {
            this.cancelcardnum = cancelcardnum;
        }

        public String getCancelcardmoney() {
            return cancelcardmoney;
        }

        public void setCancelcardmoney(String cancelcardmoney) {
            this.cancelcardmoney = cancelcardmoney;
        }

        public String getCancelshouldcardmoney() {
            return cancelshouldcardmoney;
        }

        public void setCancelshouldcardmoney(String cancelshouldcardmoney) {
            this.cancelshouldcardmoney = cancelshouldcardmoney;
        }

        public String getCancelcardmoneyPayCash() {
            return cancelcardmoneyPayCash;
        }

        public void setCancelcardmoneyPayCash(String cancelcardmoneyPayCash) {
            this.cancelcardmoneyPayCash = cancelcardmoneyPayCash;
        }

        public String getCancelcardmoneyPayBank() {
            return cancelcardmoneyPayBank;
        }

        public void setCancelcardmoneyPayBank(String cancelcardmoneyPayBank) {
            this.cancelcardmoneyPayBank = cancelcardmoneyPayBank;
        }

        public String getCancelcardmoneyPaywechat() {
            return cancelcardmoneyPaywechat;
        }

        public void setCancelcardmoneyPaywechat(String cancelcardmoneyPaywechat) {
            this.cancelcardmoneyPaywechat = cancelcardmoneyPaywechat;
        }

        public String getCancelcardmoneyPayali() {
            return cancelcardmoneyPayali;
        }

        public void setCancelcardmoneyPayali(String cancelcardmoneyPayali) {
            this.cancelcardmoneyPayali = cancelcardmoneyPayali;
        }

        public String getRechargenum() {
            return rechargenum;
        }

        public void setRechargenum(String rechargenum) {
            this.rechargenum = rechargenum;
        }

        public String getRechargemoney() {
            return rechargemoney;
        }

        public void setRechargemoney(String rechargemoney) {
            this.rechargemoney = rechargemoney;
        }

        public String getRechargeshouldcardmoney() {
            return rechargeshouldcardmoney;
        }

        public void setRechargeshouldcardmoney(String rechargeshouldcardmoney) {
            this.rechargeshouldcardmoney = rechargeshouldcardmoney;
        }

        public String getRechargePayCash() {
            return rechargePayCash;
        }

        public void setRechargePayCash(String rechargePayCash) {
            this.rechargePayCash = rechargePayCash;
        }

        public String getRechargePayBank() {
            return rechargePayBank;
        }

        public void setRechargePayBank(String rechargePayBank) {
            this.rechargePayBank = rechargePayBank;
        }

        public String getRechargePaywechat() {
            return rechargePaywechat;
        }

        public void setRechargePaywechat(String rechargePaywechat) {
            this.rechargePaywechat = rechargePaywechat;
        }

        public String getRechargePayali() {
            return rechargePayali;
        }

        public void setRechargePayali(String rechargePayali) {
            this.rechargePayali = rechargePayali;
        }

        public String getRecharge_integral() {
            return recharge_integral;
        }

        public void setRecharge_integral(String recharge_integral) {
            this.recharge_integral = recharge_integral;
        }

        public String getMembercostnum() {
            return membercostnum;
        }

        public void setMembercostnum(String membercostnum) {
            this.membercostnum = membercostnum;
        }

        public String getMembercostmoney() {
            return membercostmoney;
        }

        public void setMembercostmoney(String membercostmoney) {
            this.membercostmoney = membercostmoney;
        }

        public String getMembercostshouldcardmoney() {
            return membercostshouldcardmoney;
        }

        public void setMembercostshouldcardmoney(String membercostshouldcardmoney) {
            this.membercostshouldcardmoney = membercostshouldcardmoney;
        }

        public String getMembercostPayCard() {
            return membercostPayCard;
        }

        public void setMembercostPayCard(String membercostPayCard) {
            this.membercostPayCard = membercostPayCard;
        }

        public String getMembercostPayCash() {
            return membercostPayCash;
        }

        public void setMembercostPayCash(String membercostPayCash) {
            this.membercostPayCash = membercostPayCash;
        }

        public String getMembercostPayBank() {
            return membercostPayBank;
        }

        public void setMembercostPayBank(String membercostPayBank) {
            this.membercostPayBank = membercostPayBank;
        }

        public String getMembercostPaywechat() {
            return membercostPaywechat;
        }

        public void setMembercostPaywechat(String membercostPaywechat) {
            this.membercostPaywechat = membercostPaywechat;
        }

        public String getMembercostPayali() {
            return membercostPayali;
        }

        public void setMembercostPayali(String membercostPayali) {
            this.membercostPayali = membercostPayali;
        }

        public String getMembercost_integral() {
            return membercost_integral;
        }

        public void setMembercost_integral(String membercost_integral) {
            this.membercost_integral = membercost_integral;
        }

        public String getNomembercostnum() {
            return nomembercostnum;
        }

        public void setNomembercostnum(String nomembercostnum) {
            this.nomembercostnum = nomembercostnum;
        }

        public String getNomembercostmoney() {
            return nomembercostmoney;
        }

        public void setNomembercostmoney(String nomembercostmoney) {
            this.nomembercostmoney = nomembercostmoney;
        }

        public String getNomembershouldcostmoney() {
            return nomembershouldcostmoney;
        }

        public void setNomembershouldcostmoney(String nomembershouldcostmoney) {
            this.nomembershouldcostmoney = nomembershouldcostmoney;
        }

        public String getNomembercostPayCash() {
            return nomembercostPayCash;
        }

        public void setNomembercostPayCash(String nomembercostPayCash) {
            this.nomembercostPayCash = nomembercostPayCash;
        }

        public String getNomembercostPayBank() {
            return nomembercostPayBank;
        }

        public void setNomembercostPayBank(String nomembercostPayBank) {
            this.nomembercostPayBank = nomembercostPayBank;
        }

        public String getNomembercostPaywechat() {
            return nomembercostPaywechat;
        }

        public void setNomembercostPaywechat(String nomembercostPaywechat) {
            this.nomembercostPaywechat = nomembercostPaywechat;
        }

        public String getNomembercostPayali() {
            return nomembercostPayali;
        }

        public void setNomembercostPayali(String nomembercostPayali) {
            this.nomembercostPayali = nomembercostPayali;
        }

        public String getCost_money() {
            return cost_money;
        }

        public void setCost_money(String cost_money) {
            this.cost_money = cost_money;
        }

        public String getCost_PayCash() {
            return cost_PayCash;
        }

        public void setCost_PayCash(String cost_PayCash) {
            this.cost_PayCash = cost_PayCash;
        }

        public String getCost_PayBank() {
            return cost_PayBank;
        }

        public void setCost_PayBank(String cost_PayBank) {
            this.cost_PayBank = cost_PayBank;
        }

        public String getCost_Paywechat() {
            return cost_Paywechat;
        }

        public void setCost_Paywechat(String cost_Paywechat) {
            this.cost_Paywechat = cost_Paywechat;
        }

        public String getCost_Payali() {
            return cost_Payali;
        }

        public void setCost_Payali(String cost_Payali) {
            this.cost_Payali = cost_Payali;
        }

        public String getBuytimenum() {
            return buytimenum;
        }

        public void setBuytimenum(String buytimenum) {
            this.buytimenum = buytimenum;
        }

        public String getBuytimemoney() {
            return buytimemoney;
        }

        public void setBuytimemoney(String buytimemoney) {
            this.buytimemoney = buytimemoney;
        }

        public String getBuytimeshouldmoney() {
            return buytimeshouldmoney;
        }

        public void setBuytimeshouldmoney(String buytimeshouldmoney) {
            this.buytimeshouldmoney = buytimeshouldmoney;
        }

        public String getBuytimePayCard() {
            return buytimePayCard;
        }

        public void setBuytimePayCard(String buytimePayCard) {
            this.buytimePayCard = buytimePayCard;
        }

        public String getBuytimePayCash() {
            return buytimePayCash;
        }

        public void setBuytimePayCash(String buytimePayCash) {
            this.buytimePayCash = buytimePayCash;
        }

        public String getBuytimePayBank() {
            return buytimePayBank;
        }

        public void setBuytimePayBank(String buytimePayBank) {
            this.buytimePayBank = buytimePayBank;
        }

        public String getBuytimePaywechat() {
            return buytimePaywechat;
        }

        public void setBuytimePaywechat(String buytimePaywechat) {
            this.buytimePaywechat = buytimePaywechat;
        }

        public String getBuytimePayali() {
            return buytimePayali;
        }

        public void setBuytimePayali(String buytimePayali) {
            this.buytimePayali = buytimePayali;
        }

        public String getBuytime_integral() {
            return buytime_integral;
        }

        public void setBuytime_integral(String buytime_integral) {
            this.buytime_integral = buytime_integral;
        }

        public String getGive_integral() {
            return give_integral;
        }

        public void setGive_integral(String give_integral) {
            this.give_integral = give_integral;
        }

        public String getDeduct_integral() {
            return deduct_integral;
        }

        public void setDeduct_integral(String deduct_integral) {
            this.deduct_integral = deduct_integral;
        }

        public String getGift_integral() {
            return gift_integral;
        }

        public void setGift_integral(String gift_integral) {
            this.gift_integral = gift_integral;
        }

        public String getPayIntegral() {
            return PayIntegral;
        }

        public void setPayIntegral(String PayIntegral) {
            this.PayIntegral = PayIntegral;
        }

        public String getInc_Integral() {
            return inc_Integral;
        }

        public void setInc_Integral(String inc_Integral) {
            this.inc_Integral = inc_Integral;
        }

        public String getDec_Integral() {
            return dec_Integral;
        }

        public void setDec_Integral(String dec_Integral) {
            this.dec_Integral = dec_Integral;
        }

        public String getAllnum() {
            return allnum;
        }

        public void setAllnum(String allnum) {
            this.allnum = allnum;
        }

        public String getAllmoney() {
            return allmoney;
        }

        public void setAllmoney(String allmoney) {
            this.allmoney = allmoney;
        }

        public String getAllshouldmoney() {
            return allshouldmoney;
        }

        public void setAllshouldmoney(String allshouldmoney) {
            this.allshouldmoney = allshouldmoney;
        }

        public String getPayCash() {
            return PayCash;
        }

        public void setPayCash(String PayCash) {
            this.PayCash = PayCash;
        }

        public String getPayBank() {
            return PayBank;
        }

        public void setPayBank(String PayBank) {
            this.PayBank = PayBank;
        }

        public String getPayCard() {
            return PayCard;
        }

        public void setPayCard(String PayCard) {
            this.PayCard = PayCard;
        }

        public String getPaywechat() {
            return Paywechat;
        }

        public void setPaywechat(String Paywechat) {
            this.Paywechat = Paywechat;
        }

        public String getPayali() {
            return Payali;
        }

        public void setPayali(String Payali) {
            this.Payali = Payali;
        }

        public String getPayOther() {
            return PayOther;
        }

        public void setPayOther(String PayOther) {
            this.PayOther = PayOther;
        }
    }
}
