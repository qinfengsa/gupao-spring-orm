package com.qinfengsa.test;

import com.qinfengsa.spring.orm.demo.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: qinfengsa
 * @date: 2019/5/9 18:03
 */
@Slf4j
public class JdbcTest {


    @Test
    public void selectTest() {

        String sql = "select * from t_member";
        List<Member> result = select(sql);
        log.debug("result:{}",result);
    }

    public List<Member> select(String sql) {
        List<Member> result = new ArrayList<>();
        Connection connection = null;

        PreparedStatement statement = null;

        ResultSet rs = null;
        try {
            // 1、加载驱动类
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2、建立连接
            connection = DriverManager.getConnection("jdbc:mysql://192.168.7.11:3306/test","root","123456");
            // 3、创建语句集
            statement = connection.prepareStatement(sql);

            // 4、执行语句集
            rs = statement.executeQuery();
            while (rs.next()) {
                Member instance = new Member();
                instance.setId(rs.getLong("id"));
                instance.setName(rs.getString("name"));

                instance.setAge(rs.getInt("age"));
                instance.setAddr(rs.getString("addr"));
                result.add(instance);
            }

        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(),e);
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {

                if (Objects.nonNull(connection)) {
                    connection.close();
                }

                if (Objects.nonNull(statement)) {
                    statement.close();
                }

                if (Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(e.getMessage(),e);
            }

        }
        return result;
    }


    @Test
    public void select() {
        String sql = "select * from t_member where id = 1";
    }

}
