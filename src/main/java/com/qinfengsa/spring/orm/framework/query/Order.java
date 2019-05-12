package com.qinfengsa.spring.orm.framework.query;

/**
 * 排序方法
 * @author: qinfengsa
 * @date: 2019/5/11 19:17
 */
public class Order {
    /**
     * 升序、降序
     */
    private boolean ascending;

    /**
     * 字段
     */
    private String propertyName;

    /**
     * 构造
     * @param propertyName
     * @param ascending
     */
    protected Order(String propertyName, boolean ascending) {
        this.propertyName = propertyName;
        this.ascending = ascending;
    }

    /**
     * 升序
     * @param propertyName
     * @return Order
     */
    public static Order asc(String propertyName) {
        return new Order(propertyName, true);
    }

    /**
     * 降序
     * @param propertyName
     * @return Order
     */
    public static Order desc(String propertyName) {
        return new Order(propertyName, false);
    }


}
