package com.qinfengsa.spring.orm.demo.dao;

import com.qinfengsa.spring.orm.demo.entity.Member;
import com.qinfengsa.spring.orm.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author: qinfengsa
 * @date: 2019/5/12 10:22
 */
@Repository
public class MerberDaoImpl extends BaseDaoImpl<Member>  implements MerberDao   {

    public MerberDaoImpl() {

        super();
    }

    @Override
    @Resource(name="dataSource")
    public void setDataSource(DataSource dataSource){
        super.setDataSource(dataSource);
    }
}
