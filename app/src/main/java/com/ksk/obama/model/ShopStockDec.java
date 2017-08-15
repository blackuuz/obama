package com.ksk.obama.model;

import java.util.List;

/**
 * 项目名称：obama
 * 类描述：
 * 创建人：Create by UUZ
 * 创建时间：2017/7/21 16:50
 * 修改人：Administrator
 * 修改时间：2017/7/21 16:50
 * 修改备注：
 */
public class ShopStockDec {
    /**
     * result_stadus : ERR
     * result_code : 201
     * result_errmsg : 打折积分.库存不足
     * result_data : {"listGoods":[{"id":"27","c_GoodsNO":"P00027","c_GoodsName":"打折积分","n_PriceRetail":"10.00","r_IsMaterial":"1","ROW_NUMBER":"1","n_Amount":".00","n_DePrice":"10.00"}],"isSetNegativeStock":"1"}
     */

    private String result_stadus;
    private String result_code;
    private String result_errmsg;
    private ResultDataBean result_data;

    public String getResult_stadus() {
        return result_stadus;
    }

    public void setResult_stadus(String result_stadus) {
        this.result_stadus = result_stadus;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_errmsg() {
        return result_errmsg;
    }

    public void setResult_errmsg(String result_errmsg) {
        this.result_errmsg = result_errmsg;
    }

    public ResultDataBean getResult_data() {
        return result_data;
    }

    public void setResult_data(ResultDataBean result_data) {
        this.result_data = result_data;
    }

    public static class ResultDataBean {
        /**
         * listGoods : [{"id":"27","c_GoodsNO":"P00027","c_GoodsName":"打折积分","n_PriceRetail":"10.00","r_IsMaterial":"1","ROW_NUMBER":"1","n_Amount":".00","n_DePrice":"10.00"}]
         * isSetNegativeStock : 1
         */

        private String isSetNegativeStock;
        private List<ListGoodsBean> listGoods;

        public String getIsSetNegativeStock() {
            return isSetNegativeStock;
        }

        public void setIsSetNegativeStock(String isSetNegativeStock) {
            this.isSetNegativeStock = isSetNegativeStock;
        }

        public List<ListGoodsBean> getListGoods() {
            return listGoods;
        }

        public void setListGoods(List<ListGoodsBean> listGoods) {
            this.listGoods = listGoods;
        }

        public static class ListGoodsBean {
            /**
             * id : 27
             * c_GoodsNO : P00027
             * c_GoodsName : 打折积分
             * n_PriceRetail : 10.00
             * r_IsMaterial : 1
             * ROW_NUMBER : 1
             * n_Amount : .00
             * n_DePrice : 10.00
             */

            private String id;
            private String c_GoodsNO;
            private String c_GoodsName;
            private String n_PriceRetail;
            private String r_IsMaterial;
            private String ROW_NUMBER;
            private String n_Amount;
            private String n_DePrice;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getN_PriceRetail() {
                return n_PriceRetail;
            }

            public void setN_PriceRetail(String n_PriceRetail) {
                this.n_PriceRetail = n_PriceRetail;
            }

            public String getR_IsMaterial() {
                return r_IsMaterial;
            }

            public void setR_IsMaterial(String r_IsMaterial) {
                this.r_IsMaterial = r_IsMaterial;
            }

            public String getROW_NUMBER() {
                return ROW_NUMBER;
            }

            public void setROW_NUMBER(String ROW_NUMBER) {
                this.ROW_NUMBER = ROW_NUMBER;
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
        }
    }
}
