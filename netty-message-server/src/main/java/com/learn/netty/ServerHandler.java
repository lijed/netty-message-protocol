/*
 * Copyright 2021 tu.cn All right reserved. This software is the
 * confidential and proprietary information of tu.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Tu.cn
 */
package com.learn.netty;

import com.learn.netty.opcode.OpCode;
import com.learn.netty.protocal.MessageRecord;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.awt.font.OpenType;

/**
 * Description:
 *
 * @Author: Administrator
 * Created: 2021/9/12
 **/
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRecord messageRecord = (MessageRecord) msg;
        log.info("Server receive message: " + messageRecord);
        messageRecord.setBody("server response message");
        messageRecord.getHeader().setReqType(OpCode.RES.code());
        ctx.writeAndFlush(messageRecord);

        //不想时间继续传播
//        super.channelRead(ctx, msg);
    }
}
