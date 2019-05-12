package com.qinfengsa.spring.orm.framework.dao;

import com.qinfengsa.spring.orm.framework.entity.EntityOperation;
import com.qinfengsa.spring.orm.framework.query.QueryRule;
import com.qinfengsa.spring.orm.framework.query.QueryRuleSqlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author: qinfengsa
 * @date: 2019/5/9 21:26
 */
@Slf4j
public abstract class BaseDaoImpl<T extends Serializable> implements BaseDao<T> {


    /**
     * jdbc模版
     */
    private JdbcTemplate jdbcTemplate;

    private String tableName = "";

    private String pkColumn = "";

    private DataSource dataSource;

    private EntityOperation<T> op;

    public BaseDaoImpl() {

        try {
            Class<T> entityClass = (Class <T>)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            op = new EntityOperation<T>(entityClass);
            this.setTableName(op.tableName);
            pkColumn = op.pkField.getName() ;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }


    }

    protected void setTableName(String tableName) {

        if(StringUtils.isBlank(tableName)){
            this.tableName = op.tableName;
        }else{
            this.tableName = tableName;
        }
    }



    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    private String getTableName() {
        return tableName;
    }



    /**
     * 查询列表
     * @return
     */
    @Override
    public List<T> selectList(QueryRule queryRule) {
        QueryRuleSqlBuilder bulider = new QueryRuleSqlBuilder(queryRule);

        String whereSql = bulider.getWhereSql();
        StringBuilder sql = new StringBuilder("select ").append(op.allColumn).append(" from ")
        .append(getTableName()).append(" where 1=1 ").append(whereSql) ;

        Object[] values = bulider.getValues();
        String orderSql = bulider.getOrderSql();
        sql.append(orderSql);
        log.debug("sql:{}", sql);
        return (List<T>) this.jdbcTemplate.query(sql.toString(), this.op.rowMapper, values);
    }

    /**
     * 查询唯一结果
     * @param params
     * @return
     */
    @Override
    public T selectUnique(Map<String, Object> params) {
        if (Objects.isNull(params) || params.isEmpty()) {
            throw new IllegalStateException("params can't be empty");
        }

        QueryRule queryRule = new QueryRule();
        for (String key: params.keySet()) {
            queryRule.andEqual(key,params.get(key));
        }
        List<T> list = selectList(queryRule);
        if (list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new IllegalStateException("findUnique return " + list.size() + " record(s).");
        }
    }


    /**
     * 将对象解析为Map (保证有序)
     * @param entity
     * @return
     */
    protected Map<String,Object> parse(T entity){
        return op.parse(entity);
    }

    /**
     * 插入数据
     * @param entity
     * @return
     */
    @Override
    public boolean insert(T entity) throws Exception  {
        Object pkValue = op.pkField.get(entity);
        int result = doInsert(parse(entity));
        return result > 0;
    }

    private int doInsert(Map<String,Object> params) {
        String sql = this.getInsertSql(params);
        int result = this.jdbcTemplate.update(sql, params.values().toArray());
        return result;
    }

    private String getInsertSql(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO " ).append(getTableName());

        StringBuffer sbKey = new StringBuffer();
        StringBuffer sbValue = new StringBuffer();

        sbKey.append("(");
        sbValue.append("(");
        //添加参数
        Set<String> set = params.keySet();
        int index = 0;
        for (String key : set) {
            if (index > 0) {
                sbKey.append(",");
                sbValue.append(",");
            }
            sbKey.append(key);
            sbValue.append(" ?");

            index++;
        }
        sbKey.append(")");
        sbValue.append(")");
        sql.append(sbKey).append(" VALUES ").append(sbValue);

        return sql.toString();
    }

    /**
     * 更新数据
     * @param entity
     * @return
     */
    @Override
    public boolean update(T entity) throws Exception {
        Object pkValue = op.pkField.get(entity);
        if (Objects.isNull(pkValue)) {
            throw new Exception("主键不能为空");
        }
        return doUpdate(pkValue,parse(entity)) > 0;
    }

    private int doUpdate(Object pkValue, Map<String,Object> params) {
        String sql = this.getUpdateSql( params);

        params.put(pkColumn,pkValue);
        int result = this.jdbcTemplate.update(sql, params.values().toArray());
        return result;
    }

    private  String getUpdateSql(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(getTableName()).append(" SET ");

        //添加参数
        Set<String> set = params.keySet();
        int index = 0;
        for (String key : set) {
            if (index > 0) {
                sql.append(",");
            }
            sql.append(key).append(" = :").append(key);

            index++;
        }
        sql.append(" WHERE ").append(pkColumn).append(" =:").append(pkColumn);
        return sql.toString();
    }

    /**
     * 删除数据
     * @param entity
     * @return
     */
    @Override
    public boolean delete(T entity) throws Exception {
        Object pkValue = op.pkField.get(entity);
        if (Objects.isNull(pkValue)) {
            throw new Exception("主键不能为空");
        }
        int result = doDelete(pkValue);
        return result > 0;
    }

    /**
     * 删除
     * @param pkValue
     * @return
     */
    private int doDelete(Object pkValue) {
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ").append(getTableName()).append(" where ").append(pkColumn).append(" = ?");
        int result = this.jdbcTemplate.update(sql.toString(), pkValue);
        return result;
    }
}
