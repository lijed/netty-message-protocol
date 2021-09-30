/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty.protocal;


import lombok.Data;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/11
 **/

@Data
public class Header {
    private long sessionId;  //会话id，8个字节
    private byte reqType;  //消息类型， 1个字节
    private int length;  //消息体的长度  4个字节
}
