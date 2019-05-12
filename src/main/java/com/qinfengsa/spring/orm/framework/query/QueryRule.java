package com.qinfengsa.spring.orm.framework.query;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: qinfengsa
 * @date: 2019/5/11 15:48
 */
public class QueryRule implements Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = 5857071985968882925L;

    /**
     * 升序排列
     */
    public static final int ASC_ORDER = 101;
    /**
     * 降序排列
     */
    public static final int DESC_ORDER = 102;

    /**
     * like
     */
    public static final int LIKE = 1;

    /**
     * in
     */
    public static final int IN = 2;

    /**
     * between
     */
    public static final int BETWEEN = 4;


    /**
     * 等于
     */
    public static final int EQ = 5;

    /**
     * 不等于
     */
    public static final int NOTEQ = 6;

    /**
     * 大于
     */
    public static final int GT = 7;

    /**
     * 大于等于
     */
    public static final int GE = 8;

    /**
     * 小于
     */
    public static final int LT = 9;

    /**
     * 小于等于
     */
    public static final int LE = 10;

    /**
     * and
     */
    public static final int AND = 201;

    /**
     * or
     */
    public static final int OR = 202;

    /**
     * 规则列表集合
     */
    private List<Rule> ruleList = new ArrayList<Rule>();

    public List<Rule> getRuleList() {
        return ruleList;
    }

    /**
     * build模式
     * 添加升序排序
     * @param propertyName
     * @return
     */
    public QueryRule addAscOrder(String propertyName) {
        this.ruleList.add(new Rule(ASC_ORDER, propertyName));
        return this;
    }


    /**
     * 添加降序规则
     * @param propertyName
     * @return
     */
    public QueryRule addDescOrder(String propertyName) {
        this.ruleList.add(new Rule(DESC_ORDER, propertyName));
        return this;
    }

    /**
     * and like ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andLike(String propertyName, Object value) {
        this.ruleList.add(new Rule(LIKE, propertyName, new Object[] { value }));
        return this;
    }

    /**
     * or like ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orLike(String propertyName, Object value) {
        this.ruleList.add(new Rule(LIKE, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }

    /**
     * and = ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(EQ, propertyName, new Object[] { value }));
        return this;
    }

    /**
     * or = ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(EQ, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }

    /**
     * and <> ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andNotEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(NOTEQ, propertyName, new Object[] { value }));
        return this;
    }

    /**
     * or <> ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orNotEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(NOTEQ, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }

    /**
     * and between ? and ?
     * @param propertyName
     * @param value1
     * @param value2
     * @return
     */
    public QueryRule andBetween(String propertyName, Object value1,Object value2) {
        this.ruleList.add(new Rule(BETWEEN, propertyName,  new Object[] { value1,value2 }));
        return this;
    }

    /**
     * or between ? and ?
     * @param propertyName
     * @param value1
     * @param value2
     * @return
     */
    public QueryRule orBetween(String propertyName, Object value1,Object value2) {
        this.ruleList.add(new Rule(BETWEEN, propertyName,  new Object[] { value1,value2 }).setAndOr(OR));
        return this;
    }

    /**
     * and in (?,?)
     * @param propertyName
     * @param values
     * @return
     */
    public QueryRule andIn(String propertyName, List<Object> values) {
        this.ruleList.add(new Rule(IN, propertyName, new Object[] { values }));
        return this;
    }

    /**
     * or in (?,?)
     * @param propertyName
     * @param values
     * @return
     */
    public QueryRule orIn(String propertyName, List<Object> values) {
        this.ruleList.add(new Rule(IN, propertyName, new Object[] { values }).setAndOr(OR));
        return this;
    }

    /**
     * and in (?,?)
     * @param propertyName
     * @param values
     * @return
     */
    public QueryRule andIn(String propertyName, Object... values) {
        this.ruleList.add(new Rule(IN, propertyName, values));
        return this;
    }

    /**
     * or in (?,?)
     * @param propertyName
     * @param values
     * @return
     */
    public QueryRule orIn(String propertyName, Object... values) {
        this.ruleList.add(new Rule(IN, propertyName, values).setAndOr(OR));
        return this;
    }

    /**
     * and > ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andGreaterThan(String propertyName, Object value) {
        this.ruleList.add(new Rule(GT, propertyName, new Object[] { value }));
        return this;
    }

    /**
     * or > ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orGreaterThan(String propertyName, Object value) {
        this.ruleList.add(new Rule(GT, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }

    /**
     * and >= ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andGreaterEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(GE, propertyName, new Object[] { value }));
        return this;
    }

    /**
     * or >= ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orGreaterEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(GE, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }

    /**
     * and < ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andLessThan(String propertyName, Object value) {
        this.ruleList.add(new Rule(LT, propertyName, new Object[] { value }).setAndOr(AND));
        return this;
    }

    /**
     * or < ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orLessThan(String propertyName, Object value) {
        this.ruleList.add(new Rule(LT, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }

    /**
     * and <= ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule andLessEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(LE, propertyName, new Object[] { value }));
        return this;
    }

    /**
     * or <= ?
     * @param propertyName
     * @param value
     * @return
     */
    public QueryRule orLessEqual(String propertyName, Object value) {
        this.ruleList.add(new Rule(LE, propertyName, new Object[] { value }).setAndOr(OR));
        return this;
    }




    /**
     * select 规则
     */
    @Getter
    protected class Rule implements Serializable {

        /**
         * 序列化
         */
        private static final long serialVersionUID = 1L;

        /**
         * 规则类型
         */
        private int type;

        /**
         * 字段名
         */
        private String propertyName;

        /**
         * 字段值 in between 是数组
         */
        private Object[] values;

        /**
         * 连接符
         */
        private int andOr = AND;

        /**
         * 构造
         * @param paramInt
         * @param paramString
         */
        public Rule(int paramInt, String paramString) {
            this.propertyName = paramString;
            this.type = paramInt;
        }


        /**
         * 构造
         * @param paramInt
         * @param paramString
         * @param paramArrayOfObject
         */
        public Rule(int paramInt, String paramString,Object[] paramArrayOfObject) {
            this.propertyName = paramString;
            this.values = paramArrayOfObject;
            this.type = paramInt;
        }

        public Rule setAndOr(int andOr){
            this.andOr = andOr;
            return this;
        }
    }
}
