package com.ksk.obama.model;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/24 17:03
 * 修改人：Administrator
 * 修改时间：2017/7/24 17:03
 * 修改备注：
 */
public class CheckGoods {
    /**
     * result_data : {"stock_s":[{"id":"0","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"18","n_Amount":"120.00","n_DePrice":"777.66","c_GoodsNO":"P00001","c_GoodsName":"草莓","c_Pinyin":"CM","c_GoodsClassify":"G","ROW_NUMBER":"1"},{"id":"21","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"26","n_Amount":"130.00","n_DePrice":".01","c_GoodsNO":"P00026","c_GoodsName":"测试商品1","c_Pinyin":"CSSP1","c_GoodsClassify":"G","ROW_NUMBER":"2"},{"id":"45","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"145","n_Amount":"5.50","n_DePrice":"50.00","c_GoodsNO":"P000144","c_GoodsName":"面部护理","c_Pinyin":"MBHL","c_GoodsClassify":"美容","ROW_NUMBER":"3"},{"id":"46","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"143","n_Amount":"20.00","n_DePrice":"65535.00","c_GoodsNO":"P10086","c_GoodsName":"javalangNullPointerExcept","c_Pinyin":"javalangNullPointerExcept","c_GoodsClassify":"G","ROW_NUMBER":"4"},{"id":"47","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"139","n_Amount":"1.00","n_DePrice":"25.00","c_GoodsNO":"P000139","c_GoodsName":"水蜡洗车","c_Pinyin":"SZXJ","c_GoodsClassify":"默认分类","ROW_NUMBER":"5"},{"id":"48","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"146","n_Amount":"1.00","n_DePrice":"3800000.00","c_GoodsNO":"P000146","c_GoodsName":"陆虎揽胜行政版","c_Pinyin":"LHLSXZB","c_GoodsClassify":"默认分类","ROW_NUMBER":"6"},{"id":"49","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"138","n_Amount":"1.00","n_DePrice":"100.00","c_GoodsNO":"P000138","c_GoodsName":"芳勀","c_Pinyin":"FK","c_GoodsClassify":"G","ROW_NUMBER":"7"},{"id":"50","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"137","n_Amount":"7.00","n_DePrice":"10.00","c_GoodsNO":"P000134","c_GoodsName":"桃子","c_Pinyin":"TZ","c_GoodsClassify":"水果","ROW_NUMBER":"8"},{"id":"58","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"27","n_Amount":"1.00","n_DePrice":"10.00","c_GoodsNO":"P00027","c_GoodsName":"打折积分","c_Pinyin":"DZJF","c_GoodsClassify":"G","ROW_NUMBER":"9"},{"id":"63","i_GroupID":"1","i_ShopID":"1","i_GoodsID":"21","n_Amount":"32.00","n_DePrice":".01","c_GoodsNO":"P00021","c_GoodsName":"iPhone7Plus","c_Pinyin":"iPhone7Plus","c_GoodsClassify":"G","ROW_NUMBER":"10"}],"count_s":[{"num_s":"318.50","money_s":"5204500.8200","ROW_NUMBER":"1"}],"type_s":[{"c_Value":"全部","ROW_NUMBER":"1"},{"c_Value":"G","ROW_NUMBER":"1"},{"c_Value":"R","ROW_NUMBER":"2"},{"c_Value":"水果","ROW_NUMBER":"3"},{"c_Value":"剪发","ROW_NUMBER":"4"},{"c_Value":"测试品","ROW_NUMBER":"5"},{"c_Value":"干果","ROW_NUMBER":"6"},{"c_Value":"海鲜","ROW_NUMBER":"7"},{"c_Value":"水吧","ROW_NUMBER":"8"},{"c_Value":"饮料","ROW_NUMBER":"9"},{"c_Value":"热饮","ROW_NUMBER":"10"},{"c_Value":"零食","ROW_NUMBER":"11"},{"c_Value":"鲜花","ROW_NUMBER":"12"},{"c_Value":"美容","ROW_NUMBER":"13"}],"shop_s":[{"id":"1","c_ShopName":"总店","ROW_NUMBER":"1"},{"id":"81","c_ShopName":"分店01","ROW_NUMBER":"2"},{"id":"82","c_ShopName":"分店2","ROW_NUMBER":"3"},{"id":"83","c_ShopName":"北京智卡A","ROW_NUMBER":"4"},{"id":"84","c_ShopName":"分分店","ROW_NUMBER":"5"}]}
     * result_stadus : SUCCESS
     * result_errmsg:"???"
     */

    private ResultDataBean result_data;
    private String result_stadus;

    public String getResult_errmsg() {
        return result_errmsg;
    }

    public void setResult_errmsg(String result_errmsg) {
        this.result_errmsg = result_errmsg;
    }

    private String result_errmsg;

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public static class ResultDataBean {
        private List<StockSBean> stock_s;
        private List<CountSBean> count_s;
        private List<TypeSBean> type_s;
        private List<ShopSBean> shop_s;

        public List<StockSBean> getStock_s() {
            return stock_s;
        }

        public void setStock_s(List<StockSBean> stock_s) {
            this.stock_s = stock_s;
        }

        public List<CountSBean> getCount_s() {
            return count_s;
        }

        public void setCount_s(List<CountSBean> count_s) {
            this.count_s = count_s;
        }

        public List<TypeSBean> getType_s() {
            return type_s;
        }

        public void setType_s(List<TypeSBean> type_s) {
            this.type_s = type_s;
        }

        public List<ShopSBean> getShop_s() {
            return shop_s;
        }

        public void setShop_s(List<ShopSBean> shop_s) {
            this.shop_s = shop_s;
        }

        public static class StockSBean {
            /**
             * id : 0
             * i_GroupID : 1
             * i_ShopID : 1
             * i_GoodsID : 18
             * n_Amount : 120.00
             * n_DePrice : 777.66
             * c_GoodsNO : P00001
             * c_GoodsName : 草莓
             * c_Pinyin : CM
             * c_GoodsClassify : G
             * ROW_NUMBER : 1
             */

            private String id;
            private String i_GroupID;
            private String i_ShopID;
            private String i_GoodsID;
            private String n_Amount;
            private String n_DePrice;
            private String c_GoodsNO;
            private String c_GoodsName;
            private String c_Pinyin;
            private String c_GoodsClassify;
            private String ROW_NUMBER;
            private String changeNum = "0";

            public String getChangeNum() {
                return changeNum;
            }

            public void setChangeNum(String changeNum) {
                this.changeNum = changeNum;
            }



            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getI_GroupID() {
                return i_GroupID;
            }

            public void setI_GroupID(String i_GroupID) {
                this.i_GroupID = i_GroupID;
            }

            public String getI_ShopID() {
                return i_ShopID;
            }

            public void setI_ShopID(String i_ShopID) {
                this.i_ShopID = i_ShopID;
            }

            public String getI_GoodsID() {
                return i_GoodsID;
            }

            public void setI_GoodsID(String i_GoodsID) {
                this.i_GoodsID = i_GoodsID;
            }

            public String getN_Amount() {
                return n_Amount;
            }

            public void setN_Amount(String n_Amount) {
                this.n_Amount = n_Amount;
            }

            public String getN_DePrice() {
                return n_DePrice;
            }

            public void setN_DePrice(String n_DePrice) {
                this.n_DePrice = n_DePrice;
            }

            public String getC_GoodsNO() {
                return c_GoodsNO;
            }

            public void setC_GoodsNO(String c_GoodsNO) {
                this.c_GoodsNO = c_GoodsNO;
            }

            public String getC_GoodsName() {
                return c_GoodsName;
            }

            public void setC_GoodsName(String c_GoodsName) {
                this.c_GoodsName = c_GoodsName;
            }

            public String getC_Pinyin() {
                return c_Pinyin;
            }

            public void setC_Pinyin(String c_Pinyin) {
                this.c_Pinyin = c_Pinyin;
            }

            public String getC_GoodsClassify() {
                return c_GoodsClassify;
            }

            public void setC_GoodsClassify(String c_GoodsClassify) {
                this.c_GoodsClassify = c_GoodsClassify;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
            }
        }

        public static class CountSBean {
            /**
             * num_s : 318.50
             * money_s : 5204500.8200
             * ROW_NUMBER : 1
             */

            private String num_s;
            private String money_s;
            private String ROW_NUMBER;

            public String getNum_s() {
                return num_s;
            }

            public void setNum_s(String num_s) {
                this.num_s = num_s;
            }

            public String getMoney_s() {
                return money_s;
            }

            public void setMoney_s(String money_s) {
                this.money_s = money_s;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
            }
        }

        public static class TypeSBean {
            /**
             * c_Value : 全部
             * ROW_NUMBER : 1
             */

            private String c_Value;
            private String ROW_NUMBER;

            public String getC_Value() {
                return c_Value;
            }

            public void setC_Value(String c_Value) {
                this.c_Value = c_Value;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
            }
        }

        public static class ShopSBean {
            /**
             * id : 1
             * c_ShopName : 总店
             * ROW_NUMBER : 1
             */

            private String id;
            private String c_ShopName;
            private String ROW_NUMBER;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getC_ShopName() {
                return c_ShopName;
            }

            public void setC_ShopName(String c_ShopName) {
                this.c_ShopName = c_ShopName;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
            }
        }
    }
}
