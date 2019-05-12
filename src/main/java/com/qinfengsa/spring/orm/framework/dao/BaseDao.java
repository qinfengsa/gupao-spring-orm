package com.qinfengsa.spring.orm.framework.dao;

import com.qinfengsa.spring.orm.framework.query.QueryRule;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * mini版本ORM框架，只实现增删改查
 * @author: qinfengsa
 * @date: 2019/5/9 18:36
 */
public interface BaseDao<T extends Serializable> {

    /**
     * 查询列表
     * @param queryRule
     * @return
     */
    List<T> selectList(QueryRule queryRule);

    /**
     * 查询唯一结果
     * @param params
     * @return
     */
    T selectUnique(Map<String, Object> params);

    /**
     * 插入数据
     * @param entity
     * @return
     * @throws Exception
     */
    boolean insert(T entity) throws Exception ;

    /**
     * 更新数据
     * @param entity
     * @return
     * @throws Exception
     */
    boolean update(T entity) throws Exception ;

    /**
     * 删除数据
     * @param entity
     * @return
     * @throws Exception
     */
    boolean delete(T entity) throws Exception ;

}
