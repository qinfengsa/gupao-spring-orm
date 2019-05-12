package com.qinfengsa.spring.orm.framework.query;

import com.qinfengsa.spring.orm.framework.query.QueryRule.Rule;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: qinfengsa
 * @date: 2019/5/11 18:57
 */
public class QueryRuleSqlBuilder {


    /**
     * 列字段
     */
    private List<String> properties;

    /**
     * 参数列表
     */
    private List<Object> values;

    /**
     * 排序列表
     */
    private List<Order> orders;

    /**
     * where语句
     */
    private String whereSql = "";

    /**
     * order语句
     */
    private String orderSql = "";

    /**
     * 参数值列表
     */
    private Object [] valueArr = new Object[]{};

    /**
     * 获取参数列表
     */
    private Map<Object,Object> valueMap = new HashMap<Object,Object>();




    /**
     * 或得查询条件
     * @return
     */
    public String getWhereSql(){
        return this.whereSql;
    }

    /**
     * 获得排序条件
     * @return
     */
    public String getOrderSql(){
        return this.orderSql;
    }

    /**
     * 获得参数值列表
     * @return
     */
    public Object [] getValues(){
        return this.valueArr;
    }

    /**
     * 获取参数列表
     * @return
     */
    public Map<Object,Object> getValueMap(){
        return this.valueMap;
    }

    /**
     * 创建sql语句
     * @param queryRule
     */
    public QueryRuleSqlBuilder(QueryRule queryRule) {

        properties = new ArrayList<String>();
        values = new ArrayList<Object>();
        orders = new ArrayList<Order>();
        for (Rule rule: queryRule.getRuleList()) {
            switch (rule.getType()) {
                case QueryRule.BETWEEN:
                    processBetween(rule);
                    break;
                case QueryRule.EQ:
                    processEqual(rule);
                    break;
                case QueryRule.LIKE:
                    processLike(rule);
                    break;
                case QueryRule.NOTEQ:
                    processNotEqual(rule);
                    break;
                case QueryRule.GT:
                    processGreaterThen(rule);
                    break;
                case QueryRule.GE:
                    processGreaterEqual(rule);
                    break;
                case QueryRule.LT:
                    processLessThen(rule);
                    break;
                case QueryRule.LE:
                    processLessEqual(rule);
                    break;
                case QueryRule.IN:
                    processIN(rule);
                    break;
                case QueryRule.ASC_ORDER:
                    processOrder(rule);
                    break;
                case QueryRule.DESC_ORDER:
                    processOrder(rule);
                    break;
                default:
                    throw new IllegalArgumentException("type " + rule.getType() + " not supported.");
            }

        }

        //拼装where语句
        appendWhereSql();
        //拼装排序语句
        appendOrderSql();
        //拼装参数值
        appendValues();
    }

    private void appendValues() {
        Object [] val = new Object[values.size()];
        for (int i = 0; i < values.size(); i ++) {
            val[i] = values.get(i);
            valueMap.put(i, values.get(i));
        }
        this.valueArr = val;
    }

    private void appendOrderSql() {
        StringBuffer sbSql = new StringBuffer();
        for (int i = 0 ; i < orders.size(); i ++) {
            if(i > 0 ){
                sbSql.append(",");
            }
            sbSql.append(orders.get(i).toString());
        }
        this.orderSql = sbSql.toString();
        
    }

    private void appendWhereSql() {

        StringBuffer sbSql = new StringBuffer();
        for (String p : properties) {
            sbSql.append(p);
        }
        this.whereSql = sbSql.toString();
        
    }

    /**
     * 获取连接符
     * @param andOr
     * @return
     */
    private String getLink(int andOr) {
        String andOrStr = (0 == andOr ? "" :(QueryRule.AND == andOr ? " and " : " or "));
        return andOrStr;
    }

    /**
     * >
     * @param rule
     */
    private void processGreaterThen(Rule rule) {

        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " > ? "  );
        values.add(rule.getValues()[0]);
    }

    private void processEqual(Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " = ? "  );
        values.add(rule.getValues()[0]);
    }

    /**
     * <> 不等于
     * @param rule
     */
    private void processNotEqual(Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " <> ? "  );
        values.add(rule.getValues()[0]);
    }

    /**
     * like
     * @param rule
     */
    private void processLike(Rule rule) {

        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " like ? "  );
        values.add(rule.getValues()[0]);
    }


    private void processGreaterEqual(Rule rule) {

        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " >= ? "  );
        values.add(rule.getValues()[0]);
    }

    private void processLessThen(Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " < ? "  );
        values.add(rule.getValues()[0]);
        
    }

    private void processLessEqual(Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " <= ? "  );
        values.add(rule.getValues()[0]);
        
    }

    private void processIN(Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        StringBuilder sql = new StringBuilder();
        sql.append(andOrStr).append(rule.getPropertyName()).append(" IN (");
        Object[] list = rule.getValues();
        for (int i = 0; i < list.length; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
            values.add(list[i]);
        }
        sql.append(")");
        properties.add(sql.toString());
    }

    private void processOrder(Rule rule) {
        switch (rule.getAndOr()) {
            case QueryRule.ASC_ORDER:
                // propertyName非空
                if (StringUtils.isNotBlank(rule.getPropertyName())) {
                    orders.add(Order.asc(rule.getPropertyName()));
                }
                break;
            case QueryRule.DESC_ORDER:
                // propertyName非空
                if (StringUtils.isNotBlank(rule.getPropertyName())) {
                    orders.add(Order.desc(rule.getPropertyName()));
                }
                break;
            default:
                break;
        }
    }



    private void processBetween(Rule rule) {
        if ((ArrayUtils.isEmpty(rule.getValues()))
                || (rule.getValues().length < 2)) {
            return;
        }
        String andOrStr = getLink(rule.getAndOr());
        properties.add(andOrStr + rule.getPropertyName() + " between ? and ? "  );
        values.add(rule.getValues()[0]);
        values.add(rule.getValues()[1]);

    }
}
