package com.qinfengsa.test;

import com.qinfengsa.spring.orm.demo.dao.MerberDao;
import com.qinfengsa.spring.orm.demo.entity.Member;
import com.qinfengsa.spring.orm.framework.query.QueryRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: qinfengsa
 * @date: 2019/5/12 10:27
 */
@ContextConfiguration(locations = {"classpath:application-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class OrmTest {


    @Resource
    private MerberDao memberDao;


    @Test
    public void testSelectMember(){
        try {
            QueryRule rule = new QueryRule();
            //rule.andLike("name","%张%");
            //rule.andIn("name", "张三","李四" );
            rule.andBetween("id",1,3);
            List<Member> result = memberDao.selectList(rule);
            log.debug(Arrays.toString(result.toArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelectOneMember(){
        try {
            QueryRule rule = new QueryRule();
            Map<String,Object> params = new HashMap<>(6);
            params.put("id",1);
            Member result = memberDao.selectUnique(params);
            log.debug(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsertMember(){
        try {
            Member member = new Member();
            member.setAge(19);
            member.setName("赵云");
            member.setAddr("常山");
            boolean result = memberDao.insert(member);
            log.debug("result:{}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateMember(){
        try {
            Member member = new Member();
            member.setAge(19);
            member.setName("赵云");
            member.setAddr("常山");
            boolean result = memberDao.update(member);
            log.debug("result:{}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDeleteMember(){
        try {
            Member member = new Member();
            member.setAge(19);
            member.setName("赵云");
            member.setAddr("常山");
            boolean result = memberDao.delete(member);
            log.debug("result:{}",result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
