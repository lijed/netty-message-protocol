/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty.protocal;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/
@Data
public class User implements Serializable {

    //验证对象是否发生了变化
    private static final long serialVersionUID = -3976216411032093911L;

    private String name;

    private Integer age;
}
