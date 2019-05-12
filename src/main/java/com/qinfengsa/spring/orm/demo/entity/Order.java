package com.qinfengsa.spring.orm.demo.entity;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * 订单
 * @author: qinfengsa
 * @date: 2019/5/11 09:26
 */
@Data
public class Order implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = -3268286175005300202L;

    /**
     * 主键id
     */
    @Id
    private Long id;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 订单详情
     */
    private String detail;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 创建时间（格式化）
     */
    private String createTimeFmt;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", detail='" + detail + '\'' +
                ", createTime=" + createTime +
                ", createTimeFmt='" + createTimeFmt + '\'' +
                '}';
    }
}
