/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty.opcode;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/11
 **/
public enum OpCode {
    REQ((byte)1),
    RES((byte)2),
    PING((byte)3),
    PONG((byte)4);


    private byte code;

    OpCode(byte code) {
        this.code = code;
    }

    public byte code() {
        return code;
    }
}

