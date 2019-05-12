package com.qinfengsa.spring.orm.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户
 * @author: qinfengsa
 * @date: 2019/5/11 07:57
 */
@Entity
@Table(name = "t_member")
@Data
public class Member implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 2093849416544423138L;

    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 地址
     */
    private String addr;

    /**
     * 年龄
     */
    private Integer age;

    @Override
    public String toString() {
        return "Member{" +
                "id= " + id +
                ", name='" + name + '\'' +
                ", addr='" + addr + '\'' +
                ", age=" + age +
                '}';
    }
}
